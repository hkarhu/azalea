package fi.uef.azalea.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fi.uef.azalea.Statics;

public class Card {

	private final int group;
	public final Vector3 position;
	
	private Decal cardBackDecal;
	private Decal cardFrontDecal;

	private Rectangle dimensions = new Rectangle(0,0,GameScreen.cardSize,GameScreen.cardSize);

	//For flip and zoom animations
	boolean opened = false;
	private float flip_rotation = 0;
	
	public float zoom_amount = 0;
	public float zoom_position = 0;

	public Card(int groupID, TextureRegion texture, Vector2 position) {
		this.group = groupID;
		this.cardFrontDecal = Decal.newDecal(texture, true);
		this.cardBackDecal = Decal.newDecal(new TextureRegion(Statics.CARD_BACK), true);
		dimensions.setPosition(position);

		this.position = new Vector3(dimensions.x, dimensions.y, 0);
		
		cardFrontDecal.setPosition(this.position);
		cardBackDecal.setPosition(this.position);
		cardFrontDecal.setDimensions(dimensions.width, dimensions.height);
		cardBackDecal.setDimensions(dimensions.width, dimensions.height);

		//After setting the decal's position, center the rectangle so touch detection makes sense (Decal centers on x:y but on Rectangle it's the top corner)
		dimensions.setCenter(position.x, position.y);
	}
	
	public void lerpTowards(Vector3 newPosition){
		cardFrontDecal.getPosition().interpolate(newPosition, Gdx.graphics.getDeltaTime(), Interpolation.exp5Out);
		cardBackDecal.setPosition(cardFrontDecal.getPosition());
	}
		
	public void render(DecalBatch batch){
		
		//Flip animations
		if(opened){
			if(flip_rotation < 1){
				flip_rotation += Gdx.graphics.getDeltaTime()*2;
			} else flip_rotation = 1;
		} else {
			if(flip_rotation > 0){
				if(zoom_amount < 0.1f){
					flip_rotation -= Gdx.graphics.getDeltaTime();
				}
			} else flip_rotation = 0;
		}
		float r = (float) (Math.sin(flip_rotation*Math.PI*0.5)*180);
		cardFrontDecal.setRotationY(r+180);
		cardBackDecal.setRotationY(r);
		
		cardFrontDecal.setScale(1+zoom_amount);
		cardBackDecal.setScale(1+zoom_amount);
		
		//if not open, bounce back to origin
		if(!opened){
			if(!cardFrontDecal.getPosition().idt(position)){
				cardFrontDecal.getPosition().interpolate(position, 5*Gdx.graphics.getDeltaTime(), Interpolation.sineOut);
				cardFrontDecal.setZ(position.z + GameScreen.cardSize); //Force card to be above all others until returned to grid
				cardBackDecal.setPosition(cardFrontDecal.getPosition());
			}
			if(zoom_amount > 0){
				zoom_amount -= Gdx.graphics.getDeltaTime()*10;
			} else {
				zoom_amount = 0;
			}
		}
		
		//To the renderer
		batch.add(cardBackDecal);
		batch.add(cardFrontDecal);
	}

	public void flip(){
		opened = !opened;
	}

	public void setOpen(boolean open){
		this.opened = open;
	}

	public boolean isHit(float x, float y){
		return dimensions.contains(x, y);
	}

	public int getGroup() {
		return group;
	}

	public void resetPosition() {
		flip_rotation = 0;
		zoom_amount = 0;
		cardFrontDecal.setPosition(position);
		cardBackDecal.setPosition(position);
	}

}
