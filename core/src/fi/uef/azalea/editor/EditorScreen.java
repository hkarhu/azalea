package fi.uef.azalea.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.uef.azalea.CardSet;
import fi.uef.azalea.Screen;
import fi.uef.azalea.Statics;

public class EditorScreen extends Screen {
	
	private Table cardListTable;
	private Stage stage;
	private Skin skin;
	private ScrollPane cardSetScrollPane;
	private Array<CardSet> cardSets;
	
	public EditorScreen() {
		
		cardSets = new Array<CardSet>();
		for(int i=0; i < 100; i++){
			cardSets.add(new CardSet());
		}
		
		skin = new Skin(Statics.SKIN); //TODO: skin this
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		
		cardListTable = new Table();
		cardListTable.setFillParent(true);
		cardListTable.align(Align.center | Align.top);
		
		TextButton doneButton = new TextButton("Done", skin);
		
		List<Array<CardSet>>cardSetList = new List<>(skin);
		cardSetList.setItems(cardSets);
		cardSetScrollPane = new ScrollPane(cardSetList);
		cardSetScrollPane.setFillParent(true);
		cardSetScrollPane.setScrollingDisabled(true, false);
		cardSetScrollPane.setScrollBarPositions(true, true);
		cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardSetScrollPane.setFadeScrollBars(true);
		
		
		
		stage.addActor(cardSetScrollPane);
		
		Gdx.input.setInputProcessor(stage);
        
	}

	@Override
	public void render(SpriteBatch spriteBatch, DecalBatch decalBatch) {
		stage.getBatch().draw(Statics.PLAYGROUND_BACKGROUND, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
	public void handleTouchPoint(float x, float y, int id) {
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
