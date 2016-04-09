package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenu extends Screen {
	
	private Stage stage;
	
	private Button start;
	private Button edit;
	
	public MainMenu() {

		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		
		Table t = new Table(Statics.SKIN);
		start = new TextButton("Pelaa muistipeliae", Statics.SKIN);
		start.setBounds(Gdx.graphics.getWidth()*0.125f, Gdx.graphics.getHeight()*0.1f, Gdx.graphics.getWidth()*0.75f, Gdx.graphics.getHeight()*0.1f);
		edit = new TextButton("Muokkaa kortteja", Statics.SKIN);
		edit.setBounds(Gdx.graphics.getWidth()*0.125f, Gdx.graphics.getHeight()*0.22f, Gdx.graphics.getWidth()*0.75f, Gdx.graphics.getHeight()*0.1f);
		t.addActor(start);
		t.addActor(edit);
		stage.addActor(t);
        
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(Azalea.AppState.select);
			}
		});
		
	}
	
	@Override
	public void init() {
		Gdx.input.setInputProcessor(stage);
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
