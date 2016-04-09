package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.sun.xml.internal.ws.api.Cancelable;

import fi.uef.azalea.Azalea.AppState;
import fi.uef.azalea.game.CardImageData;

public class SetSelectorScreen extends Screen {
	
	private Table cardListTable;
	private Stage stage;
	private ScrollPane cardSetScrollPane;
	
	private Array<CardSet> cardSets;
	private Array<CardSet> selectedSets;
	
	private int cardAmount = 0;
	
	public SetSelectorScreen() {
		
		cardSets = new Array<CardSet>();
		selectedSets = new Array<CardSet>();
		
		reloadCardSets();
		
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		//stage.setDebugAll(true);
		
		cardListTable = new Table();
		
		cardSetScrollPane = new ScrollPane(cardListTable);
		cardSetScrollPane.setBounds(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight()*0.85f);
		cardSetScrollPane.setScrollingDisabled(true, false);
		cardSetScrollPane.setForceScroll(false, true);
		//cardSetScrollPane.setScrollBarPositions(true, true);
		//cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardSetScrollPane.setFadeScrollBars(false);

		Dialog cardAmountDialog = new Dialog("SELECT_AMOUNT", Statics.SKIN);
		
		cardAmountDialog.setSize(Gdx.graphics.getWidth()*0.6f, Gdx.graphics.getHeight()*0.6f);
		TextButton dialogOK = new TextButton("OK", Statics.SKIN);
		dialogOK.setSize(100, 70);
		dialogOK.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
				
				Azalea.changeState(AppState.game);
			}
		});
				
		TextButton dialogCancel = new TextButton("CANCEL", Statics.SKIN);
		dialogCancel.setSize(100, 70);
		dialogCancel.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
			}
		});

		cardAmountDialog.button(dialogCancel);
		cardAmountDialog.button(dialogOK);
		cardAmountDialog.show(stage);
		
		TextButton doneButton = new TextButton("PLAY", Statics.SKIN); //TODO
		doneButton.setDisabled(true);
		doneButton.setVisible(false);
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.getContentTable().clear();
				int maxNumCards = 2;
				for(CardSet s : selectedSets){
					maxNumCards += s.getNumCards();
				}
				Slider amountSlider = new Slider(2, maxNumCards, 1, false, Statics.SKIN);
				amountSlider.setSize(400, 100);
				amountSlider.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						cardAmount = (int) amountSlider.getValue();
					}
				});
				cardAmountDialog.add(amountSlider).expandX();
				cardAmountDialog.show(stage);
			}
		});
		
		TextButton cancelButton = new TextButton("CANCEL", Statics.SKIN); //TODO
		cancelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.menu);
			}
		});
		
		for(CardSet s : cardSets){
			s.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {					
					if(s.isSelected()){
						selectedSets.add(s);
					} else {
						selectedSets.removeValue(s, true);
					}
					if(selectedSets.size > 0){
						doneButton.setDisabled(false);
						doneButton.setVisible(true);
					} else {
						doneButton.setDisabled(true);
						doneButton.setVisible(false);
					}
				}
			});
			cardListTable.add(s).pad(5).growX();
			cardListTable.row();
		}
		
		Table content = new Table();
		content.setFillParent(true);
		content.add(new Label("SELECT_SET", Statics.SKIN)).colspan(2).pad(20).align(Align.center); //TODO
		content.row();
		content.add(cardSetScrollPane).colspan(2).pad(20).fill().expand();
		content.row();
		content.add(cancelButton).pad(8).size(200, 80).align(Align.left).growX();
		content.add(doneButton).pad(8).size(200, 80).align(Align.right).growX();
		stage.addActor(content);
		
		Gdx.input.setInputProcessor(stage);
        
	}
	
	private void reloadCardSets() {
		cardSets.clear();
		FileHandle[] files = Gdx.files.local("cards/").list();
		for(FileHandle file: files) {
			if(file.isDirectory()){
				cardSets.add(new CardSet(file));
			}
		}
	}

	public Array<CardImageData> getCards() {
		/*
		Array<CardImageData> cards = new Array<CardImageData>();
		int n = cardAmount/selectedSets.size;
		for(CardSet s : selectedSets){
			cards.addAll(s.getRandomCards(n));
		}
		*/
		return selectedSets.get(0).getRandomCards(10);
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
