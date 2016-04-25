package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.uef.azalea.Azalea.AppState;

public abstract class SetSelectorScreen extends Screen {

	protected Table cardListTable;
	protected Stage stage;
	protected ScrollPane cardSetScrollPane;
	protected static Array<CardSet> cardSets = new Array<CardSet>();

	protected TextButton doneButton;
	protected TextButton cancelButton;

	public SetSelectorScreen() {
	
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		
		if(Statics.DEBUG) stage.setDebugAll(true);

		cardListTable = new Table();
		cardSetScrollPane = new ScrollPane(cardListTable);
		cardSetScrollPane.setBounds(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight()*0.85f);
		cardSetScrollPane.setScrollingDisabled(true, false);
		cardSetScrollPane.setForceScroll(false, true);
		//cardSetScrollPane.setScrollBarPositions(true, true);
		//cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardSetScrollPane.setFadeScrollBars(false);

		cancelButton = new TextButton("Takaisin", Statics.SKIN); //TODO
		cancelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.menu);
			}
		});

	}

	protected void reloadCardSets() {
		cardSets.clear();
		cardListTable.clear();

		FileHandle[] files = Gdx.files.local("sets/").list(); //TODO: put strings into statics
		if (files.length > 0){
			for (FileHandle file : files) {
				CardSet c = new CardSet(file);
				try {
					c.loadData();
				} catch (SerializationException e){
					continue;
				}
				cardSets.add(c);
			}
		}

	}

	@Override
	public void init() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchDown(float x, float y, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touchUp(float x, float y, int id) {
		// TODO Auto-generated method stub

	}

}
