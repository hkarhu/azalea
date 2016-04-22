package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

import fi.uef.azalea.Azalea.AppState;
import fi.uef.azalea.game.CardImageData;

public class SetSelectorScreen extends Screen {
	
	private Table cardListTable;
	private Stage stage;
	private ScrollPane cardSetScrollPane;
	
	private NewCardSetCreator newCardSetCreator;
	
	private Array<CardSet> cardSets;
	private Array<CardSet> selectedSets;
	
	private int cardAmount = 0;
	
	private final TextButton doneButton;
	private boolean selectForEditor = false; //TODO: tape and glue ;-;
	
	public SetSelectorScreen() {
		
		cardSets = new Array<CardSet>();
		selectedSets = new Array<CardSet>();
		
		stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		stage.setDebugAll(true);
		
		cardListTable = new Table();
		
		cardSetScrollPane = new ScrollPane(cardListTable);
		cardSetScrollPane.setBounds(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight()*0.85f);
		cardSetScrollPane.setScrollingDisabled(true, false);
		cardSetScrollPane.setForceScroll(false, true);
		//cardSetScrollPane.setScrollBarPositions(true, true);
		//cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardSetScrollPane.setFadeScrollBars(false);

		final Dialog cardAmountDialog = new Dialog("SELECT_AMOUNT", Statics.SKIN); //TODO
		
		TextButton dialogOK = new TextButton("OK", Statics.SKIN); //TODO
		dialogOK.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
				Azalea.changeState(AppState.game);
			}
		});
				
		TextButton dialogCancel = new TextButton("CANCEL", Statics.SKIN); //TODO
		dialogCancel.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
			}
		});

		cardAmountDialog.getButtonTable().add(dialogCancel).size(200, 80).align(Align.left).growX();
		cardAmountDialog.getButtonTable().add(dialogOK).size(200, 80).align(Align.right).growX();
		
		doneButton = new TextButton("", Statics.SKIN); //TODO
		doneButton.setDisabled(true);
		doneButton.setVisible(false);
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!selectForEditor){
					cardAmountDialog.getContentTable().clear();
					int maxNumCards = 2;
					for(CardSet s : selectedSets){
						maxNumCards += s.getCards().size;
					}
					final Slider amountSlider = new Slider(2, (maxNumCards > 32 ? 32 : maxNumCards), 1, false, Statics.SKIN);
					final Label amountLabel = new Label("Kortteja: " + cardAmount, Statics.SKIN); //TODO
					amountSlider.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							cardAmount = (int) amountSlider.getValue();
							amountLabel.setText("Kortteja: " + cardAmount); //TODO
						}
					});
					cardAmountDialog.getContentTable().add(amountLabel).align(Align.center).expand();
					cardAmountDialog.getContentTable().row();
					cardAmountDialog.getContentTable().add(amountSlider).size(700, 80).expandX();
					cardAmountDialog.show(stage);
				} else {
					Azalea.changeState(AppState.edit);
				}
			}
		});
		
		TextButton cancelButton = new TextButton("CANCEL", Statics.SKIN); //TODO
		cancelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.menu);
			}
		});
		
		newCardSetCreator = new NewCardSetCreator();
		newCardSetCreator.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectedSets.clear();
				FileHandle targetFile = Gdx.files.local("sets/" + System.nanoTime() + ".set");
				if(!targetFile.exists()) {
					CardSet newSet = new CardSet(targetFile);
					selectedSets.add(newSet);
					cardSets.add(newSet);
					Azalea.changeState(AppState.edit);
				} else {
					System.out.println("Not gonna make a new set on top of existing file.");
				}
			}
		});
		
		Table content = new Table();
		content.setFillParent(true);
		content.add(new Label("SELECT_SET", Statics.SKIN)).colspan(2).pad(20).align(Align.center); //TODO
		content.row();
		content.add(cardSetScrollPane).colspan(2).pad(20).fill().expand();
		content.row();
		content.add(cancelButton).pad(8).size(200, 80).align(Align.left).growX();
		content.add(doneButton).pad(8).size(200, 80).align(Align.right).growX();
		stage.addActor(content);

	}
	
	private void reloadCardSets() {
		cardSets.clear();
		cardListTable.clear();
        if(selectForEditor){
            cardListTable.add(newCardSetCreator).pad(5).growX();
            cardListTable.row();
        }
        
        /*
		for (String path : Azalea.getFileSources()) {
			FileHandle[] files = Gdx.files.local(path).list("set");
			System.out.println("Scanning path " + path);
			if (files.length > 0)
				for (FileHandle file : files) {
					cardSets.add(new CardSet(new CardSetData(file)));
				}
		}
		*/
		
		//Add listeners and prepare table
		for(final CardSet s : cardSets){
			s.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(s.isSelected()){
						if(selectedSets.size > 0 && selectForEditor) unselectAll();
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
		
	}

	public CardSet getSelectedSet(){
		return selectedSets.first();
	}

	public Array<CardImageData> getCards() {
		Array<CardImageData> cards = new Array<CardImageData>();
		int n = cardAmount/selectedSets.size;
		for(CardSet s : selectedSets){
			cards.addAll(s.getRandomCards(n));
		}
		return cards;
	}
	
	public void selectForEditor(boolean editorSelect){
		this.selectForEditor = editorSelect;
		if(!editorSelect){
			doneButton.setText("PLAY"); //TODO
		} else {
			doneButton.setText("EDIT"); //TODO
		}
	}

	private void unselectAll() {
		for(CardSet s : selectedSets){
			s.setSelected(false);
		}
		selectedSets.clear();
	}
	
	@Override
	public void init() {
		Gdx.input.setInputProcessor(stage);
		unselectAll();
        reloadCardSets();
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
