package fi.uef.azalea.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fi.uef.azalea.Statics;
import fi.uef.azalea.game.CardImageData;

public class EditorCard extends Table {

	private CardImageData cardImageData;
	
	public EditorCard(CardImageData cardImageData) {
		this.cardImageData = cardImageData;
	}
	
	public EditorCard() {
		
	}

	public void updatePreviews(){
		
	}
	
	@Override
	protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
		batch.draw(cardImageData.getCardTexture(), x, y, getWidth(), getHeight());
	}
	
}
