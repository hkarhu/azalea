package fi.uef.azalea.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fi.uef.azalea.Statics;
import fi.uef.azalea.game.CardImageData;

public class NewCardCreator extends Table {

	private CardImageData cardImageData;
	
	public NewCardCreator(CardImageData cardImageData) {
		this.cardImageData = cardImageData;
		
	}
	
	public NewCardCreator() {
		
	}

	public void updatePreviews(){
		
	}
	
	@Override
	protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
		if(cardImageData == null){
			batch.draw(Statics.TEX_CARD_EMPTY,x,y,getWidth(), getHeight());
		} else {
			batch.draw(cardImageData.getCardTexture(), x, y, getWidth(), getHeight());
		}
	}
	
}
