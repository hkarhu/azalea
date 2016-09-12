package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

public class Statics {

	public static final String TITLE = "Muistipeli (Azalea 0.7b)";
	public static final boolean DEBUG = false;

	public static final float MAX_PRESS_DELAY = 1.5f;
	
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 800;
	public static final int CARD_PIXMAP_SIZE = 300;
	
	public static final float BRUSH_RADIUS = 4f;
	public static final float ERASER_RADIUS = 15f;
	
	public static final String CARD_IMAGE_CACHE = "card_cache";
	
	public static final float cardMargin = 2f; //How much space between cards
	public static final float cardScaler = 300f; //How much cards will be scaled during wrong pair selection
	public static final float showMargin = 10f; //Margin when large preview of wrong pair shown
	public static final float screenMargin = 32f; //How much space between the screen and card matrix
	
	public static Skin SKIN = new Skin(Gdx.files.internal("uiskin.json"));
	public static final Color SET_DARKEN_TINT = new Color(0, 0, 0, 0.8f);
	
	public static final float REL_BUTTON_PADDING = 0.001f;
	public static final float REL_BUTTON_WIDTH = 0.16f;
	public static final float REL_BUTTON_HEIGHT = 0.07f;
	public static final float REL_ITEM_PADDING = 0.015f;
	
	public static TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("azalea_game.pack"));
	public static TextureRegion MENU_BG;
	public static TextureRegion CORRECT;
	public static TextureRegion WRONG;
	public static TextureRegion PLAYGROUND_BACKGROUND;
	public static TextureRegion TEX_WIN;
	public static TextureRegion DARKEN_MASK;
	public static TextureRegion TEXREGION_TITLE_EMPTY;
	public static TextureRegion CARD_BACK;
	public static TextureRegion NEW_CARD_UP;
	public static TextureRegion NEW_CARD_DOWN;
	
	public static TextureRegion LABEL_EDITOR_BG;
	public static TextureRegion LABEL_EDIT_BG;
	public static TextureRegion LABEL_EDIT_HELP;
	
	public static TiledDrawable TITLE_BG, SELECT_CONTENT_BG, EDIT_CONTENT_BG, DECK_EDITOR_BG;
	//	public static Texture IMAGE_FULLVIEW_BACKGROUND = new Texture(Gdx.files.internal("gray_bg.png"));
	
//	public static Texture TEX_TITLE_HELP = new Texture(Gdx.files.internal("title_help.png"));
	
	//TODO: Language support much?
	public static String STRING_EDIT_SET = "";
	public static String STRING_SELECT_SET = "";
	public static String STRING_PLAY_BUTTON = "";
	public static String STRING_EDIT_BUTTON = "";
	public static String STRING_QUIT_BUTTON = "";
	public static String STRING_BACK_BUTTON = "";
	
	public static void loadTextures(){
		//textures = new TextureAtlas(Gdx.files.internal("azalea_game.pack"));
		MENU_BG = textureAtlas.findRegion("menu");
		CORRECT = textureAtlas.findRegion("right");
		WRONG = textureAtlas.findRegion("wrong");
		PLAYGROUND_BACKGROUND = textureAtlas.findRegion("gray_bg");
		TEX_WIN = textureAtlas.findRegion("win");
		DARKEN_MASK = textureAtlas.findRegion("darkenmask");
		TEXREGION_TITLE_EMPTY = textureAtlas.findRegion("deck_label_new");
		CARD_BACK = textureAtlas.findRegion("card_back");
		TITLE_BG = new TiledDrawable(textureAtlas.findRegion("texture_leather"));
		DECK_EDITOR_BG = SELECT_CONTENT_BG = new TiledDrawable(textureAtlas.findRegion("texture_wood_light"));
		EDIT_CONTENT_BG = new TiledDrawable(textureAtlas.findRegion("texture_paper"));
		LABEL_EDIT_HELP = textureAtlas.findRegion("title_help");
		LABEL_EDIT_BG = textureAtlas.findRegion("texture_paper");
		NEW_CARD_UP = NEW_CARD_DOWN = textureAtlas.findRegion("card_new");

	}
	
	static {
		//PLAYGROUND_BACKGROUND.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//PLAYGROUND_BACKGROUND.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		////DARKEN_MASK.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//TEX_LISTBG.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		//TEX_MENUBG.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	public static void dispose(){
		
		//TEX_CARD_EMPTY.dispose();
		//TEX_WIN.dispose();
		textureAtlas.dispose();
		SKIN.dispose();
	}
	
}