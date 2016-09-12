package fi.uef.azalea.game;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile.BlendMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Azalea.AppState;
import fi.uef.azalea.CardImageData;
import fi.uef.azalea.Screen;
import fi.uef.azalea.SlowButton;
import fi.uef.azalea.Statics;
import fi.uef.azalea.camera.ResizeableOrthographicCamera;

public class GameScreen extends Screen implements InputProcessor {

	public static float cardSize = 0f;
	
	private Stage stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	
	private int guesses = 0;
	private int max_tries = 0;
	
	private Array<Card> cardsInPlay = null;
	private Array<Card> openedCards = null;
	private HashMap<Integer, CardImageData> cardImages;

	private Vector3 screenShowPosition_L = new Vector3(0,0,99);
	private Vector3 screenShowPosition_R = new Vector3(0,0,99);
	
	private Decal darkenDecal;
	private Decal prizeDecal;
	private Decal correctDecal;
	private Decal wrongDecal;
	private Decal winDecal;
	
	//Game states and animations
	private enum GameStates { pick, show_wrong, show_success, hide, end }
	private GameStates lastState = null;
	private GameStates currentState = GameStates.pick;
	private float transition = 0;
	
	private SpriteBatch spriteBatch;
	private DecalBatch decalBatch;
	private ResizeableOrthographicCamera camera;
	
	private SlowButton exitButton;
	
	public GameScreen() {
		
		exitButton = new SlowButton("X", Statics.SKIN);
		exitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.menu);
			}
		});
		exitButton.setSize(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getWidth()*0.05f);
		exitButton.setPosition(Gdx.graphics.getWidth()*0.001f*0, Gdx.graphics.getHeight()-Gdx.graphics.getWidth()*0.05f);
		stage.addActor(exitButton);
		
		cardsInPlay = new Array<Card>();
		openedCards = new Array<Card>();
		cardImages = new HashMap<Integer, CardImageData>();
		camera = new ResizeableOrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		spriteBatch = new SpriteBatch();
		
		//Other gui stuff
		wrongDecal = Decal.newDecal(new TextureRegion(Statics.WRONG), true);
		
		correctDecal = Decal.newDecal(new TextureRegion(Statics.CORRECT), true);
		//correctDecal.setPosition(Gdx.graphics.getWidth()*0.35f, Gdx.graphics.getHeight()*0.35f, 110);
		
		TextureRegion darken = new TextureRegion(Statics.DARKEN_MASK);
		prizeDecal = Decal.newDecal(darken, true);
		prizeDecal.setPosition(0, 0, 100);
		
		darkenDecal = Decal.newDecal(darken, true);
		darkenDecal.setBlending(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA); //Blending "multiply"
		darkenDecal.setPosition(0, 0, 1);
		darkenDecal.setDimensions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		winDecal = Decal.newDecal(new TextureRegion(Statics.TEX_WIN), true);
		winDecal.setPosition(0, 0, 100);

	}
	
	private void swapState(GameStates newState) {
		lastState = currentState;
		currentState = newState;
		transition = 1;
	}

	public void setBoard(Array<CardImageData> inputData, int numCardsInGroup){
		currentState = GameStates.pick;
		max_tries = numCardsInGroup;
		guesses = 0;
		cardsInPlay.clear();
		openedCards.clear();
		cardImages.clear();

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
		
		System.out.println("Board ratio: " + gridWidth/(float)gridHeight);
		
		boolean fixBoard = false;
		int gridWidthF = 0;
		if(gridWidth/(float)gridHeight > 3){
			fixBoard = true;
			int shiftCards = ((gridWidth*gridHeight)/4)*2;
			gridWidthF = gridWidth/2+1;
			System.out.println("Board shape is crap. Applying hack. Shifting " + shiftCards + " cards, with " + gridWidthF );
		}
		
		if(fixBoard){
			gridHeight = gridHeight*2;
			gridWidth = gridWidthF;
		}
		if(Gdx.graphics.getHeight()/gridHeight > Gdx.graphics.getWidth()/gridWidth){
			cardSize = (Gdx.graphics.getWidth()-(gridWidth*Statics.cardMargin) - Statics.screenMargin)/(float)gridWidth;
		} else {
			cardSize = (Gdx.graphics.getHeight()-(gridHeight*Statics.cardMargin) - Statics.screenMargin)/(float)gridHeight;
		}

		float topMargin = Gdx.graphics.getHeight()*0.05f*0;
		float xShift = (gridWidth-1)*(cardSize + Statics.cardMargin)*0.5f;
		float yShift = (gridHeight-1)*(cardSize + Statics.cardMargin)*0.5f + topMargin;

		//Make positions
		Array<Vector2> positions = new Array<Vector2>();
		if(fixBoard){
			for(int i=0; i < n; i++){
				if((i/gridWidthF)%2 != 0){
					positions.add(new Vector2((i%gridWidthF)*(cardSize + Statics.cardMargin)-xShift,(i/gridWidthF)*(cardSize + Statics.cardMargin)-yShift));
				} else {
					if((i%gridWidthF) == 0){
						n++;
						continue;
					}

					positions.add(new Vector2((i%gridWidthF-0.5f)*(cardSize + Statics.cardMargin)-xShift,(i/gridWidthF)*(cardSize + Statics.cardMargin)-yShift));
					
				}
			}
		} else {
			for(int i=0; i < n; i++){
				positions.add(new Vector2((i%gridWidth)*(cardSize + Statics.cardMargin)-xShift,(i/gridWidth)*(cardSize + Statics.cardMargin)-yShift));
			}
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
				cardsInPlay.add(new Card(groupID, d.getCardTexture(), positions.pop()));
			}

			groupID++;
		}
		
		this.ready = true;
		
	}

	@Override
	public void init(){
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glCullFace(GL20.GL_BACK);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
	}
	
	@Override
	public void render() {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		spriteBatch.begin();
			spriteBatch.setColor(1, 1, 1, 1);
			spriteBatch.draw(Statics.PLAYGROUND_BACKGROUND, 0, 0, 1280, 800);
			exitButton.draw(spriteBatch, 0.7f);
		spriteBatch.end();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		
		//if transitioning
		if(transition > 0){
			transition -= Gdx.graphics.getDeltaTime()*0.5f;
		} else {
			transition = 0;
		}
		
		for(Card c : cardsInPlay){
			c.render(decalBatch);
		}

		Color cf = darkenDecal.getColor();
		
		switch (currentState) {
		
			case show_success: //Set prize image as the target and show it
				if(transition > 0){
					for(Card c : openedCards){
						c.zoom_amount *= transition;
					}
					cf.a = 1-transition;
					Color cp = prizeDecal.getColor();
					cp.a = (float) Math.pow(1-transition, 2);
					darkenDecal.setColor(cf);
					prizeDecal.setColor(cp);
				}
				decalBatch.add(darkenDecal);
				decalBatch.add(prizeDecal);
				
				correctDecal.setPosition(Gdx.graphics.getWidth()*0.4f, -Gdx.graphics.getHeight()*0.38f, cardSize*2);
				correctDecal.setScale(0.2f+(2+(float)Math.sin(Gdx.graphics.getFrameId()*0.1f))*0.1f);
				decalBatch.add(correctDecal);
				
				break;
				
			case hide:
				if(transition > 0){			
					cf.a = transition;
					darkenDecal.setColor(cf);
					decalBatch.add(darkenDecal);
					if(lastState == GameStates.show_success){
						prizeDecal.setScale(transition);
						prizeDecal.setColor(cf);
						decalBatch.add(prizeDecal);
					}
					transition -= Gdx.graphics.getDeltaTime();
				}
				break;
				
			case show_wrong:
				if(transition > 0){
					cf.a = (1-transition);
					darkenDecal.setColor(cf);
				}
				decalBatch.add(darkenDecal);
				wrongDecal.setPosition(0, 0, cardSize*2);
				wrongDecal.setScale(0.2f+(2+(float)Math.sin(Gdx.graphics.getFrameId()*0.1f))*0.1f);
				decalBatch.add(wrongDecal);
				if(openedCards.get(0).position.x > openedCards.get(1).position.x){
					openedCards.get(0).lerpTowards(screenShowPosition_R);
					openedCards.get(1).lerpTowards(screenShowPosition_L);
				} else {
					openedCards.get(1).lerpTowards(screenShowPosition_R);
					openedCards.get(0).lerpTowards(screenShowPosition_L);
				}
				
				openedCards.get(1).zoom_amount = (1-transition)*(1/cardSize)*Statics.cardScaler*0.75f;
				openedCards.get(0).zoom_amount = (1-transition)*(1/cardSize)*Statics.cardScaler*0.75f;
				break;
				
			case end:
				decalBatch.add(winDecal);
				wrongDecal.setScale(0.5f+(2+(float)Math.sin(Gdx.graphics.getFrameId()*0.1f))*0.1f);
				
			default:
				break;

		}
		
		decalBatch.flush();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	@Override
	public void resize(int width, int height) {
		
		//stage.getViewport().update(width, height, false);
		
		camera.updateViewport();
		camera.near = 0.01f;
		camera.far = 1000f;
		camera.direction.set(0, 0, -1);
		camera.position.set(0, 0, camera.far*0.75f);
		camera.update();
		
		screenShowPosition_L = new Vector3(-Statics.cardScaler, 0, cardSize*2);
		screenShowPosition_R = new Vector3(Statics.cardScaler, 0, cardSize*2);
		
		int s = Math.max(width, height);
		darkenDecal.setDimensions(s, s);
		
	}

	@Override
	public void dispose() {
		stage.dispose();
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		Vector3 tp3 = new Vector3(screenX, screenY, 1);
		camera.unproject(tp3);
			
		if(transition > 0){
			if(currentState == GameStates.show_success) return true; //If showing the prize image, don't accept input before image is fully shown 
			transition = 0;
		}

		switch (currentState) {
			case end:
				Azalea.changeState(AppState.menu);
				break;
				
			case show_success:
				for(Card c : openedCards){
					c.dispose();
				}
				cardsInPlay.removeAll(openedCards, true);
				openedCards.clear();
				if(cardsInPlay.size > 0) swapState(GameStates.hide); else swapState(GameStates.end);
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
				if(cardsInPlay.size > 0) swapState(GameStates.pick); else swapState(GameStates.end);
				//break; //No break here to go directly to pick (special case because we need to reset only after transitioning)
				
			case pick:
				for(Card c : cardsInPlay){
					if(c.opened == false && c.isHit(tp3.x, tp3.y)){
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
						prizeDecal.getTextureRegion().getTexture().dispose();
						TextureRegion tr = d.getFullImageTexture();
						prizeDecal.setTextureRegion(tr);
						prizeDecal.setScale(1);
						float pWidth = tr.getRegionWidth();
						float pHeight = tr.getRegionHeight();
						float s = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) - Statics.screenMargin*0.5f;
						if(pWidth < pHeight){
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
		
		return stage.touchDown(screenX, screenY, pointer, button);

	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Vector3 tp3 = new Vector3(screenX, screenY, 1);
		//camera.unproject(tp3);
		//if(currentScreen != null) currentScreen.touchUp(tp3.x, tp3.y, pointer);
		return stage.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean keyDown(int keyCode) {
		return stage.keyDown(keyCode);
	}

	@Override
	public boolean keyUp(int keyCode) {
		return stage.keyUp(keyCode);
	}

	@Override
	public boolean keyTyped(char character) {
		return stage.keyTyped(character);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return stage.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return stage.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		return stage.scrolled(amount);
	}

	@Override
	public void touchDown(float x, float y, int id) {
		//stage.touchDown(x,y,id,0);
	}

	@Override
	public void touchUp(float x, float y, int id) {
		// TODO Auto-generated method stub
	}


}
