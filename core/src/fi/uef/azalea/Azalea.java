package fi.uef.azalea;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.camera.ResizeableOrthographicCamera;
import fi.uef.azalea.game.CardImageData;
import fi.uef.azalea.game.GameScreen;

public class Azalea extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	
	public static final String TITLE = "Azalea 0.1b";
	public static final float cardMargin = 12f;
	
	private SpriteBatch spriteBatch;
	private DecalBatch decalBatch;
	private ResizeableOrthographicCamera camera;
	
	private Screen currentScreen;
	private GameScreen gameScreen;
	
	public Azalea() {
		gameScreen = new GameScreen();
	}

	@Override
	public void create () {
		
		camera = new ResizeableOrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.1f;
		camera.far = 300f;
		camera.position.set(0, 0, 5f);
		camera.direction.set(0, 0, -1f);
		
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		spriteBatch = new SpriteBatch();
		
		FileHandle[] files = Gdx.files.local("cards/testset/").list();
		Array<CardImageData> inputImages = new Array<>();
		System.out.println(Gdx.files.getLocalStoragePath());
		for(FileHandle file: files) {
			TextureRegion tr = new TextureRegion(new Texture(file));
			inputImages.add(new CardImageData(file, tr));
		}
		
		gameScreen.initGame(inputImages, 2); //Pairs only, for now
		
		currentScreen = gameScreen;
		
		Gdx.input.setInputProcessor(this);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL20.GL_BACK);
	}
	
	@Override
	public void render () {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		camera.position.set(0, 0, 5f);
		camera.update();
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		spriteBatch.begin();
		currentScreen.render(spriteBatch, decalBatch);
		spriteBatch.end();
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		decalBatch.flush();
		
	}
	
	@Override
	public void resize(int width, int height) {
		//cam.resize(width, height);
		super.resize(width, height);
		camera.resize(width, height);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		decalBatch.dispose();
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
		currentScreen.handleTouchPoint(tp3.x, tp3.y);
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
}
