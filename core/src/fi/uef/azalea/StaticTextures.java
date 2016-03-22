package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class StaticTextures {

	public static Texture CARD_BACK = new Texture(Gdx.files.internal("card_back.png"));
	public static Texture CARD_MASK = new Texture(Gdx.files.internal("card_mask.png"));
	public static Texture PLAYGROUND_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture IMAGE_FULLVIEW_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	
	static {
		
		PLAYGROUND_BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		PLAYGROUND_BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
	}
	
}
