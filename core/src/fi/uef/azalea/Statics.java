package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Statics {

	public static final String TITLE = "Azalea 0.2b";
	public static final boolean DEBUG = false;
	
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 800;
	public static final int CARD_PIXMAP_SIZE = 300;
	
	public static final float BRUSH_RADIUS = 4f;
	public static final float ERASER_RADIUS = 8f;
	
	public static final String CARD_IMAGE_CACHE = "card_cache";
	
	public static final float cardMargin = 2f; //How much space between cards
	public static final float cardScaler = 300f; //How much cards will be scaled during wrong pair selection
	public static final float showMargin = 10f; //Margin when large preview of wrong pair shown
	public static final float screenMargin = 32f; //How much space between the screen and card matrix
	
	public static Pixmap CARD_MASK_PIXMAP = new Pixmap(Gdx.files.internal("card_mask.png"));
	public static Skin SKIN = new Skin(Gdx.files.internal("uiskin.json"));
	public static final Color SET_DARKEN_TINT = new Color(0, 0, 0, 0.8f);
	
	public static final float REL_BUTTON_PADDING = 0.001f;
	public static final float REL_BUTTON_WIDTH = 0.16f;
	public static final float REL_BUTTON_HEIGHT = 0.07f;
	public static final float REL_ITEM_PADDING = 0.015f;
	
	
	public static Texture TEX_CARD_EMPTY = new Texture(Gdx.files.internal("card_new.png"));
	public static Texture TEX_NEW_CARD_BUTTON_UP = new Texture(Gdx.files.internal("card_new.png"));
	public static Texture TEX_NEW_CARD_BUTTON_DOWN = new Texture(Gdx.files.internal("card_new.png"));
	public static Texture TEX_WIN = new Texture(Gdx.files.internal("win.png"));
	
	public static Texture TEX_ICON_ERASE = new Texture(Gdx.files.internal("icon_erase.png"));
	public static Texture TEX_ICON_DRAW = new Texture(Gdx.files.internal("icon_draw.png"));
	public static Texture TEX_ICON_CLEAR = new Texture(Gdx.files.internal("icon_clear.png"));
	public static Texture TEX_ICON_TEXT = new Texture(Gdx.files.internal("icon_text.png"));
	
	public static Texture TEX_LISTBG = new Texture(Gdx.files.internal("list_background.png"));
	public static Texture TEX_MENUBG = new Texture(Gdx.files.internal("texture_leather.png"));
	public static Texture TEX_TITLEBG = new Texture(Gdx.files.internal("texture_paper.png"));
	
	public static Texture MENU = new Texture(Gdx.files.internal("menu.png"));
	public static Texture CORRECT = new Texture(Gdx.files.internal("right.png"));
	public static Texture WRONG = new Texture(Gdx.files.internal("wrong.png"));
	public static Texture CARD_BACK = new Texture(Gdx.files.internal("card_back.png"));
	public static Texture PLAYGROUND_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture IMAGE_FULLVIEW_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	public static Texture DARKEN_MASK = new Texture(Gdx.files.internal("darkenmask.png"));
	public static Texture LOADING = new Texture(Gdx.files.internal("load.png"));	
	public static Texture TEX_TITLE_HELP = new Texture(Gdx.files.internal("title_help.png"));
	
	public static TextureRegion TEXREGION_TITLE_EMPTY = new TextureRegion(new Texture(Gdx.files.internal("deck_label_new.png")));
	
	public static String STRING_EDIT_SET = "";
	public static String STRING_SELECT_SET = "";
	public static String STRING_PLAY_BUTTON = "";
	public static String STRING_EDIT_BUTTON = "";
	public static String STRING_QUIT_BUTTON = "";
	public static String STRING_BACK_BUTTON = "";
	
	static {
		
		PLAYGROUND_BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		PLAYGROUND_BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		DARKEN_MASK.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TEX_LISTBG.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		TEX_MENUBG.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	public static void dispose(){
		CARD_MASK_PIXMAP.dispose();
		
		TEX_CARD_EMPTY.dispose();
		TEX_NEW_CARD_BUTTON_UP.dispose();
		TEX_NEW_CARD_BUTTON_DOWN.dispose();
		TEX_WIN.dispose();
		
		SKIN.dispose();
	}
	
}