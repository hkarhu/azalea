package fi.uef.azalea.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Screen;
import fi.uef.azalea.StaticTextures;

public class GameScreen extends Screen {

	private int guesses = 0;
	private Array<Card> cardsInPlay = null;

	private Decal background;

	public void initGame(Array<TextureRegion> inputTextures){

		guesses = 0;
		cardsInPlay = new Array<>();

		int n = inputTextures.size;
		System.out.println("Will use " + n + " cards.");

		//Getting the needed amount of primes (all primes less than the target number)
		ArrayList<Integer> primes = new ArrayList<Integer>();
		primes.add(2); //yay
		for(int f=2; f <= n; f++){
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
		float whRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
		System.out.println("Screen ratio: " + whRatio);

		int width = 1;
		int height = 1;

		while(!factors.isEmpty()){
			if(width/height <= whRatio){
				width *= factors.pollLast();
			} else {
				height *= factors.pollLast();
			}
		}

		System.out.println("Using " + width + "x" + height + " board.");
		
		float xShift = (width-1)*(Azalea.cardSize + Azalea.cardMargin)*0.5f;
		float yShift = (height-1)*(Azalea.cardSize + Azalea.cardMargin)*0.5f;
		int i = 0;
		for(TextureRegion tr : inputTextures){
			cardsInPlay.add(new Card("tag:"+i, tr, new Vector2((i%width)*(Azalea.cardSize + Azalea.cardMargin)-xShift,(i/width)*(Azalea.cardSize + Azalea.cardMargin)-yShift)));
			i++;
		}	

	}

	@Override
	public void init() {

	}

	@Override
	public void render(SpriteBatch sp, DecalBatch db) {
		sp.draw(StaticTextures.PLAYGROUND_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		for(Card c : cardsInPlay){
			c.render(db);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
	public void handleTouchPoint(Vector2 point) {
		for(Card c : cardsInPlay){
			c.isHit(point);
		}
	}

}
