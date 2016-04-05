package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

public class Statics {

	public static Pixmap CARD_MASK_PIXMAP = new Pixmap(Gdx.files.internal("card_mask.png"));
	
	public static Texture CARD_BACK = new Texture(Gdx.files.internal("card_back.png"));
	public static Texture PLAYGROUND_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture IMAGE_FULLVIEW_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture DARKEN_MASK = new Texture(Gdx.files.internal("darkenmask.png"));

	public static FileHandle SKIN = Gdx.files.internal("uiskin.json");
	
	static {
		
		PLAYGROUND_BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		PLAYGROUND_BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		DARKEN_MASK.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}
	
	public static void dispose(){
		CARD_MASK_PIXMAP.dispose();
	}
	
}
