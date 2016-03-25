package fi.uef.azalea.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import fi.uef.azalea.StaticTextures;

public class CardInstance {

	private Decal cardBackDecal;
	private Decal cardFrontDecal;
	private Rectangle dimensions = new Rectangle(0,0,GameScreen.cardSize,GameScreen.cardSize);

	//For flip and zoom animations
	boolean flipped = false;
	boolean zoomed = false;
	float flip_rotation = 0;
	float zoom_amount = 0;

	public CardInstance(TextureRegion texture, Vector2 position) {
		this.cardFrontDecal = Decal.newDecal(texture, true);
		this.cardBackDecal = Decal.newDecal(new TextureRegion(StaticTextures.CARD_BACK), true);
		dimensions.setPosition(position);

		cardFrontDecal.setPosition(dimensions.x, dimensions.y, -GameScreen.cardSize);
		cardBackDecal.setPosition(dimensions.x, dimensions.y, -GameScreen.cardSize);
		cardFrontDecal.setDimensions(dimensions.width, dimensions.height);
		cardBackDecal.setDimensions(dimensions.width, dimensions.height);

		//After setting the decal's position, center the rectangle so touch detection makes sense (Decal centers on x:y but on Rectangle it's the top corner)
		dimensions.setCenter(position.x, position.y);
	}

	public void render(DecalBatch batch){
		//Flip animations
		if(flipped){
			if(flip_rotation < 1){
				flip_rotation += Gdx.graphics.getDeltaTime();
			} else flip_rotation = 1;
		} else {
			if(flip_rotation > 0){
				flip_rotation -= Gdx.graphics.getDeltaTime();
			} else flip_rotation = 0;
		}
		float r = (float) (Math.sin(flip_rotation*Math.PI*0.5)*180);
		cardFrontDecal.setRotationY(r+180);
		cardBackDecal.setRotationY(r);

		//To the renderer
		batch.add(cardBackDecal);
		batch.add(cardFrontDecal);
	}

	public void flip(){
		flipped = !flipped;
	}

	public void setFlipped(boolean flipped){
		this.flipped = flipped;
	}

	public boolean isHit(float x, float y){
		return dimensions.contains(x, y);
	}

}
