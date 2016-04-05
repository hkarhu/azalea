package fi.uef.azalea.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile.BlendMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Screen;
import fi.uef.azalea.Statics;

public class GameScreen extends Screen {

	public static float cardSize = 0f;
	private static float cardScaler = 1f;
	
	private int guesses = 0;
	private int max_tries = 0;
	
	private Array<Card> cardsInPlay = null;
	private Array<Card> openedCards = null;
	private HashMap<Integer, CardImageData> cardImages;

	private Vector3 screenShowPosition_L = new Vector3(0,0,100);
	private Vector3 screenShowPosition_R = new Vector3(0,0,100);
	
	private Decal darkenDecal;
	private Decal prizeDecal;

	//Game states and animations
	private enum GameStates { pick, show_wrong, show_success, hide }
	private GameStates lastState = null;
	private GameStates currentState = GameStates.pick;
	private float transition = 0;
	
	public GameScreen() {
		//Other gui stuff
		TextureRegion darken = new TextureRegion(Statics.DARKEN_MASK);
		darkenDecal = Decal.newDecal(darken, true);
		prizeDecal = Decal.newDecal(darken, false);
	}

	public void initGame(Array<CardImageData> inputData, int numCardsInGroup){
		
		max_tries = numCardsInGroup;
		guesses = 0;
		cardsInPlay = new Array<Card>();
		openedCards = new Array<Card>();
		cardImages = new HashMap<Integer, CardImageData>();

		int n = inputData.size*numCardsInGroup;
		System.out.println("Will use " + n + " cards.");

		//Getting the needed amount of primes (all primes less than the target number)
		ArrayList<Integer> primes = new ArrayList<Integer>();
		primes.add(2); //yay
		for(int f=2; f <= (n/2)+1; f++){
			boolean prime = true;
			for(int a=2; a <= Math.ceil(Math.sqrt(f)); a++){
				if(f%a == 0){
					prime = false;
				}
			}
			if(prime){
				primes.add(f);
			}
		}

		System.out.println("Needed primes: " + primes);

		//Extracting factors
		LinkedList<Integer> factors = new LinkedList<Integer>();
		int fdiv = n;
		int pindex = 0;
		while(fdiv > 1){
			if(fdiv%primes.get(pindex) == 0){
				factors.add(primes.get(pindex));
				fdiv /= primes.get(pindex);
				pindex = 0;
			} else {
				pindex++;
			}
		}

		System.out.println("Factors for " + n + ": " + factors);

		//Calculating optimal board size
		float whRatio = Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight();
		System.out.println("Screen ratio: " + whRatio);

		int gridWidth = 1;
		int gridHeight = 1;

		while(!factors.isEmpty()){
			if(gridWidth/gridHeight <= whRatio){
				gridWidth *= factors.pollLast();
			} else {
				gridHeight *= factors.pollLast();
			}
		}

		System.out.println("Using " + gridWidth + "x" + gridHeight + " board.");
		
		if(Gdx.graphics.getHeight() > Gdx.graphics.getWidth()){
			cardSize = (Gdx.graphics.getWidth()-(gridWidth*Azalea.cardMargin) - Azalea.screenMargin)/(float)gridWidth;
			cardScaler = (Gdx.graphics.getWidth()-Azalea.showMargin)/cardSize;
		} else {
			cardSize = (Gdx.graphics.getHeight()-(gridHeight*Azalea.cardMargin) - Azalea.screenMargin)/(float)gridHeight;
			cardScaler = (Gdx.graphics.getHeight()-Azalea.showMargin)/cardSize;
		}

		float xShift = (gridWidth-1)*(cardSize + Azalea.cardMargin)*0.5f;
		float yShift = (gridHeight-1)*(cardSize + Azalea.cardMargin)*0.5f;

		//Make positions
		Array<Vector2> positions = new Array<Vector2>();		
		for(int i=0; i < n; i++){
			positions.add(new Vector2((i%gridWidth)*(cardSize + Azalea.cardMargin)-xShift,(i/gridWidth)*(cardSize + Azalea.cardMargin)-yShift));
		}

		//Shuffle positions
		positions.shuffle();
		positions.shuffle();

		//Make card "groups" (pairs) and give cards predefined positions.
		int groupID = 0;
		for(CardImageData d : inputData){
			cardImages.put(groupID, d);

			//assign positions per card
			for(int i=0; i < numCardsInGroup; i++){
				cardsInPlay.add(new Card(groupID, d.cardTexture, positions.pop()));
			}

			groupID++;
		}	
		
		//Other gui stuff here
		darkenDecal.setBlending(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA); //Blending "multiply"
		darkenDecal.setPosition(0, 0, 1);
		prizeDecal.setPosition(0, 0, 100);
		
	}

	@Override
	public void render(SpriteBatch sp, DecalBatch db) {		
		sp.draw(Statics.PLAYGROUND_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//if transitioning
		if(transition > 0){
			transition -= Gdx.graphics.getDeltaTime()*0.8f;
		} else {
			transition = 0;
		}
		
		for(Card c : cardsInPlay){
			c.render(db);
		}

		Color cf = darkenDecal.getColor();
		
		switch (currentState) {
			case show_success: //Set prize image as the target and show it
				if(transition > 0){			
					cf.a = 1-transition;
					darkenDecal.setColor(cf);
				}
				db.add(darkenDecal);
				db.add(prizeDecal);
				break;
			case hide:
				if(transition > 0){			
					cf.a = transition;
					darkenDecal.setColor(cf);
					db.add(darkenDecal);
					if(lastState == GameStates.show_success){
						prizeDecal.setScale(transition);
						db.add(prizeDecal);
					}
				}
				break;
				
			case show_wrong:
				if(transition > 0){
					cf.a = (1-transition);
					darkenDecal.setColor(cf);
				}
				db.add(darkenDecal);
				if(openedCards.get(0).position.x > openedCards.get(1).position.x){
					openedCards.get(0).lerpTowards(screenShowPosition_R);
					openedCards.get(1).lerpTowards(screenShowPosition_L);
				} else {
					openedCards.get(1).lerpTowards(screenShowPosition_R);
					openedCards.get(0).lerpTowards(screenShowPosition_L);
				}
				
				openedCards.get(1).zoom_amount = (1-transition)*cardScaler;
				openedCards.get(0).zoom_amount = (1-transition)*cardScaler;
				break;
				
			default:
				break;

		}
		
	}

	@Override
	public void resize(int width, int height) {
		screenShowPosition_L = new Vector3(-width*0.25f, 0, 100);
		screenShowPosition_R = new Vector3(width*0.25f, 0, 100);
		darkenDecal.setDimensions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void handleTouchPoint(float x, float y, int id) {

		if(transition > 0){
			transition = 0; 
		}

		switch (currentState) {
			case show_success:
				cardsInPlay.removeAll(openedCards, true);
				openedCards.clear();
				swapState(GameStates.hide);
				break;
				
			case show_wrong: //Hasten cleaning up, go directly to hide
				for(Card c : openedCards){
					c.setOpen(false);
				}
				swapState(GameStates.hide);
				break;
	
			case hide:
				for(Card c : openedCards){
					c.resetPosition(); //Make sure all cards return to origin
				}
				openedCards.clear();
				swapState(GameStates.pick);
				//break; //No break here to go directly to pick (special case because we need to reset only after transitioning)
				
			case pick:
				for(Card c : cardsInPlay){
					if(c.opened == false && c.isHit(x, y)){
						c.setOpen(true);
						openedCards.add(c);
					}
				}
				if(openedCards.size >= max_tries){
					guesses++;
					
					swapState(GameStates.show_wrong);
					boolean allSame = true;
					Card lastCard = null;
					for(Card c : openedCards){
						if(lastCard != null && lastCard.getGroup() != c.getGroup()) allSame = false;
						lastCard = c;
					}
					if(allSame){
						CardImageData d = cardImages.get(lastCard.getGroup());
						TextureRegion tr = d.getOriginalImage();
						prizeDecal.setTextureRegion(tr);
						prizeDecal.setScale(1);
						//TODO: get prize scaling from previous code
						float pWidth = tr.getRegionWidth();
						float pHeight = tr.getRegionHeight();
						float s = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - Azalea.screenMargin*0.5f;
						if(pWidth > pHeight){
							prizeDecal.setWidth(s);
							prizeDecal.setHeight(s*(pHeight/pWidth));
						} else {
							prizeDecal.setWidth(s*(pWidth/pHeight));
							prizeDecal.setHeight(s);
						}
						swapState(GameStates.show_success);
					} else {
						swapState(GameStates.show_wrong);
					}
				}
				break;
	
			default:
				break;
		}
	}

	private void swapState(GameStates newState) {
		lastState = currentState;
		currentState = newState;
		transition = 1;
	}

}
