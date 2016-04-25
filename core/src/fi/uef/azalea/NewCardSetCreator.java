package fi.uef.azalea;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class NewCardSetCreator extends Table {
	
	private Drawable coverTextureBack;
	private Drawable coverTextureTitle;
	
	public NewCardSetCreator() {
		
		coverTextureBack = new TextureRegionDrawable(Statics.TEXREGION_TITLE_EMPTY);
		
		final TextButton selectButton = new TextButton("Uusi", Statics.SKIN); //TODO
		
		this.add(selectButton).pad(8).size(150, 100).align(Align.left);
		if(coverTextureTitle == null) this.add(new Label("Uusi korttipakka", Statics.SKIN)).align(Align.left).expand().fill(); //TODO
		
	}

	@Override
	protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		
		batch.setColor(0.5f,0.5f,0.5f,1);
		if(coverTextureBack != null) coverTextureBack.draw(batch, x, y, getWidth(), getHeight());
		
		//Label
		if(coverTextureTitle != null){
			float sizeScaler = 0.8f/(coverTextureTitle.getMinHeight()/getHeight());
			float shadowShift = 8;
			float yShift = (getHeight()-(coverTextureTitle.getMinHeight()*sizeScaler))*0.5f;
			float xShift = 200;
			
			batch.setColor(0,0,0,0.8f);
			coverTextureTitle.draw(batch, x+xShift+shadowShift, y+yShift-shadowShift, coverTextureTitle.getMinWidth()*sizeScaler, coverTextureTitle.getMinHeight()*sizeScaler);
			batch.setColor(1,1,1,1);
			coverTextureTitle.draw(batch, x+xShift, y+yShift, coverTextureTitle.getMinWidth()*sizeScaler, coverTextureTitle.getMinHeight()*sizeScaler);
		}
		
	}
	
}
