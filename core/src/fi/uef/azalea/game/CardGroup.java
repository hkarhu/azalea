package fi.uef.azalea.game;

import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CardGroup {
	
	private CardImageData data;
	private Array<CardInstance> cardsInGroup;
	private boolean solved = false;
	
	public CardGroup(CardImageData data, Array<Vector2> positions) {
		this.data = data;
		this.cardsInGroup = new Array<>();
		
		for(Vector2 p : positions){
			this.cardsInGroup.add(new CardInstance(data.texture, p));
		}
	}

	public void render(DecalBatch batch) {
		//if(solved) return; //TODO: This more prettier <3
		
		for(CardInstance ci : cardsInGroup){
			ci.render(batch);
		}
	}

	public CardInstance processTouch(float x, float y) {
		boolean s = true;
		for(CardInstance ci : cardsInGroup){
			if(ci.isHit(x, y)){
				ci.setFlipped(true);
			}
			if(!ci.flipped) s = false;
		}
		solved = s;
		return null;
	}
	
	public void reset(){
		for(CardInstance ci : cardsInGroup){
			ci.setFlipped(false);
		}
	}
	
	public boolean isSolved(){
		return solved;
	}
	
}
