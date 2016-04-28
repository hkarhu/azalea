package fi.uef.azalea.editor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.uef.azalea.Statics;

public class LogoButton extends Button {

	private TextureRegion logo;
	private float padding;
	
	public LogoButton(TextureRegion logo, Skin skin) {
		super(skin);
		padding = Math.min(getWidth()*0.05f, getHeight()*0.05f);
		this.logo = logo;
	}

	public void setLogo(TextureRegion logo){
		this.logo = logo;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		super.draw(batch, parentAlpha);
		batch.draw(logo, getX()+padding, getY()+padding, getWidth()-2*padding, getHeight()-2*padding);
		
	}
	
}
