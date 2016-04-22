package fi.uef.azalea;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.editor.EditorScreen;
import fi.uef.azalea.game.GameScreen;

public class Azalea extends ApplicationAdapter implements ApplicationListener {
	
	private static SetSelectorScreen setSelector;
	private static GameScreen gameScreen; 
	private static MainMenu mainMenu;
	private static EditorScreen editorScreen; 
	 
	public enum AppState { menu, game, edit, select_game, select_edit };
	public static AppState currentState = AppState.menu;
	private static Screen currentScreen;
	
	public static PlatformBridge platform;
	
	//Loading screen
	private SpriteBatch spriteBatch;
	private static float load_transition = 1;
	
	public Azalea(PlatformBridge p) {
		platform = p;
	}
	
	@Override
	public void create () {
		
		Gdx.files.local("sets/").mkdirs();
		
		//Loading screen stuff
		spriteBatch = new SpriteBatch();

		//Screens need to be created here because constructors might need to access the render threads
		mainMenu = new MainMenu();
		setSelector = new SetSelectorScreen();
		gameScreen = new GameScreen();
		editorScreen = new EditorScreen();

		changeState(AppState.menu);

	}
	
	@Override
	public void render () {
		/*
		if(load_transition > 0){
			spriteBatch.begin();
			spriteBatch.setColor(0, 0, 0, 1-load_transition);
			spriteBatch.draw(Statics.LOADING, 0, 0);
			spriteBatch.end();
			
			load_transition -= 0.01f;
		}*/
	
		if(currentScreen.ready){
			currentScreen.render();
		}	
		
	}
	
	//Toggle screens and handle diipadaapa
	public static void changeState(AppState newState){
		load_transition = 1;
		switch (newState) {
			case game:
				gameScreen.ready = false;
				currentScreen = gameScreen;
				gameScreen.setBoard(setSelector.getCards(), 2); //Pairs only, for now
				break;
				
			case menu:
				currentScreen = mainMenu;
				break;
				
			case select_game:
				setSelector.selectForEditor(false);
				currentScreen = setSelector;
				break;
				
			case select_edit:
				setSelector.selectForEditor(true);
				currentScreen = setSelector;
				break;
				
			case edit:
				editorScreen.setEditableSet(setSelector.getSelectedSet());
				currentScreen = editorScreen;
				break;
	
			default:
				System.err.println("Something went wrong.");
				break;
		}
		
		currentScreen.init();
		currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		currentState = newState;
		System.out.println("State: " + currentState);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		if(currentScreen != null) currentScreen.resize(width, height);
	}
	
	@Override
	public void dispose() {
		mainMenu.dispose();
		gameScreen.dispose();
		setSelector.dispose();
		editorScreen.dispose();
		super.dispose();
	}
	
	@Override
	public void pause() {
		super.pause();
		if(currentScreen != null) currentScreen.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
		if(currentScreen != null) currentScreen.resume();
	}
	
}
