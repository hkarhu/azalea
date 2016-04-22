package fi.uef.azalea.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Azalea.AppState;
import fi.uef.azalea.CardSet;
import fi.uef.azalea.Screen;
import fi.uef.azalea.Statics;
import fi.uef.azalea.game.CardImageData;

public class EditorScreen extends Screen {
	
	private Stage editSetStage;
	private Table cardTable;
	private LabelEditorActor labelEditor;
	private NewCardCreator newCardCreator;
	private Stage editCardStage;
	
	private enum EditMode {edit_set, edit_card}
	private EditMode currentMode = EditMode.edit_set;
		
	private CardSet editableCardSet;
	private CardImageData editableCard;
	
	public EditorScreen() {
		
		newCardCreator = new NewCardCreator();
		newCardCreator.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CardImageData newCard = new CardImageData();
				editableCardSet.addCard(newCard);
				editableCard = newCard;
				setMode(EditMode.edit_card);
			}
		});
		
		editSetStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		cardTable = new Table();
		ScrollPane cardScrollPane = new ScrollPane(cardTable);
		cardScrollPane.setBounds(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight()*0.85f);
		cardScrollPane.setScrollingDisabled(false, false);
		cardScrollPane.setForceScroll(true, true);
		//cardSetScrollPane.setScrollBarPositions(true, true);
		//cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardScrollPane.setFadeScrollBars(false);
		Button doneButton = new TextButton("", Statics.SKIN); //TODO
		//doneButton.setDisabled(true);
		//doneButton.setVisible(false);
		
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String filename = System.nanoTime()+".set";
				System.out.println("save set: " + filename);
				editableCardSet.setLabelTexture(labelEditor.getLabelPixmap());
				editableCardSet.save();
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		TextButton cancelButton = new TextButton("CANCEL", Statics.SKIN); //TODO
		cancelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		Button buttonDrawErase = new Button();
		buttonDrawErase.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		Table editTitleTable = new Table();
		labelEditor = new LabelEditorActor();
		editTitleTable.add(labelEditor).size(1024, 256).pad(20).align(Align.center).fill().expand(); //TODO
		
		Table mainEditContent = new Table();
		mainEditContent.setFillParent(true);
		mainEditContent.add(editTitleTable).colspan(2).pad(8).height(256).align(Align.left).growX();
		mainEditContent.row();
		mainEditContent.add(cardScrollPane).colspan(2).pad(20).fill().expand();
		mainEditContent.row();
		mainEditContent.add(cancelButton).pad(8).size(200, 80).align(Align.left).growX();
		mainEditContent.add(doneButton).pad(8).size(200, 80).align(Align.right).growX();
		editSetStage.addActor(mainEditContent);
		editSetStage.setDebugAll(true);
		
	}

	public void setEditableSet(CardSet editableSet){
		this.editableCardSet = editableSet;
	}
	
	private void resetCards(){
		cardTable.clear();
		
		cardTable.add(newCardCreator).size(300, 300).pad(32);
		
		for(CardImageData c : editableCardSet.getCards()){
			EditorCard ec = new EditorCard(c);
			ec.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					System.out.println("edit");
				}
			});
			cardTable.add(ec);
		}
	}
	
	@Override
	public void init() {
		resetCards();
		this.ready = true;
		labelEditor.clearTexture();
		setMode(EditMode.edit_set);
	}

	private void setMode(EditMode mode){
		this.currentMode = mode;
		switch (currentMode) {
		case edit_set:
			Gdx.input.setInputProcessor(editSetStage);
			break;
		case edit_card:
			Gdx.input.setInputProcessor(editCardStage);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		editSetStage.act(Gdx.graphics.getDeltaTime());
		editSetStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
