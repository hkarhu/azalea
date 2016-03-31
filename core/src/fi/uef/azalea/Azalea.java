package fi.uef.azalea;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;

import fi.uef.azalea.camera.ResizeableOrthographicCamera;
import fi.uef.azalea.editor.EditorScreen;
import fi.uef.azalea.game.CardImageData;
import fi.uef.azalea.game.GameScreen;

public class Azalea extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	
	public static final String TITLE = "Azalea 0.1b";
	public static final float cardMargin = 2f; //How much space between cards
	public static final float showMargin = 400f; //Margin when large preview of wrong pair shown
	public static final float screenMargin = 32f; //How much space between the screen and card matrix
	
	private SpriteBatch spriteBatch;
	private DecalBatch decalBatch;
	private ResizeableOrthographicCamera camera;
	
	private Screen currentScreen;
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	private EditorScreen editorScreen;
	
	public Azalea() {
		
	}

	@Override
	public void create () {
		
		//Screens need to be created here because constructors might need to access the render threads
		gameScreen = new GameScreen();
		
		camera = new ResizeableOrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.01f;
		camera.far = 300f;
		camera.position.set(0, 0, camera.far*0.5f);
		camera.update();
		
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		spriteBatch = new SpriteBatch();
		
		FileHandle[] files = Gdx.files.internal("cards/testset/").list();
		Array<CardImageData> inputImages = new Array<CardImageData>();
		System.out.println(Gdx.files.getLocalStoragePath());
		for(FileHandle file: files) {
			inputImages.add(new CardImageData(file));
		}
		
		gameScreen.initGame(inputImages, 2); //Pairs only, for now
		
		currentScreen = gameScreen;
		
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glCullFace(GL20.GL_BACK);
		
	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		
		spriteBatch.begin();
		currentScreen.render(spriteBatch, decalBatch);
		spriteBatch.end();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		decalBatch.flush();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.resize(width, height);
		camera.position.set(0, 0, camera.far*0.5f);
		camera.update();
		if(currentScreen != null) currentScreen.resize(width, height);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		decalBatch.dispose();
		spriteBatch.dispose();
		gameScreen.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 tp3 = new Vector3(screenX, screenY, 1);
		camera.unproject(tp3);
		currentScreen.handleTouchPoint(tp3.x, tp3.y, pointer);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
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
