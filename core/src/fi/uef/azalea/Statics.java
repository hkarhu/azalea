package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Statics {

	public static final String TITLE = "Azalea 0.1b";
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 800;
			
	public static final float cardMargin = 2f; //How much space between cards
	public static final float showMargin = 400f; //Margin when large preview of wrong pair shown
	public static final float screenMargin = 32f; //How much space between the screen and card matrix
	
	public static Pixmap CARD_MASK_PIXMAP = new Pixmap(Gdx.files.internal("card_mask.png"));
	public static Skin SKIN = new Skin(Gdx.files.internal("uiskin.json"));
	public static final Color SET_DARKEN_TINT = new Color(0, 0, 0, 0.8f);
	
	public static Texture CARD_BACK = new Texture(Gdx.files.internal("card_back.png"));
	public static Texture PLAYGROUND_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture IMAGE_FULLVIEW_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture DARKEN_MASK = new Texture(Gdx.files.internal("darkenmask.png"));
	public static Texture LOADING = new Texture(Gdx.files.internal("load.png"));
	
	static {
		
		PLAYGROUND_BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		PLAYGROUND_BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		DARKEN_MASK.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
	}
	
	public static void dispose(){
		CARD_MASK_PIXMAP.dispose();
		SKIN.dispose();
	}
	
}
