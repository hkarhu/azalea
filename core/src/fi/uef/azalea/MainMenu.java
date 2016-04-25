package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sun.glass.ui.Application;

public class MainMenu extends Screen {
	
	private Stage stage;
	
	private Button start;
	private Button edit;
	private Button quit;
	
	public MainMenu() {

		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		Image background = new Image(Statics.MENU);
		Table t = new Table(Statics.SKIN);
		quit = new TextButton("Lopeta", Statics.SKIN);
		quit.setBounds(Gdx.graphics.getWidth()*0.125f, Gdx.graphics.getHeight()*0.1f, Gdx.graphics.getWidth()*0.75f, Gdx.graphics.getHeight()*0.1f);
		edit = new TextButton("Muokkaa kortteja", Statics.SKIN);
		edit.setBounds(Gdx.graphics.getWidth()*0.125f, Gdx.graphics.getHeight()*0.22f, Gdx.graphics.getWidth()*0.75f, Gdx.graphics.getHeight()*0.1f);
		start = new TextButton("Pelaa muistipeliä", Statics.SKIN);
		start.setBounds(Gdx.graphics.getWidth()*0.125f, Gdx.graphics.getHeight()*0.34f, Gdx.graphics.getWidth()*0.75f, Gdx.graphics.getHeight()*0.1f);
		t.addActor(quit);
		t.addActor(start);
		t.addActor(edit);
		stage.addActor(background);
		stage.addActor(t);

		edit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(Azalea.AppState.select_edit);
			}
		});
		
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(Azalea.AppState.select_game);
			}
		});
		
		quit.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
	}
	
	@Override
	public void init() {
		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		ready = true;
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void touchDown(float x, float y, int id) {}

	@Override
	public void touchUp(float x, float y, int id) {}

}
