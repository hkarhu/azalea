package fi.uef.azalea.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Azalea.AppState;
import fi.uef.azalea.CardImageData;
import fi.uef.azalea.CardSet;
import fi.uef.azalea.Screen;
import fi.uef.azalea.Statics;

public class EditorScreen extends Screen {
	
	private Stage editSetStage;
	private Table cardTable;
	private LabelEditorActor labelEditor;
	private Button newCardCreator;
	private Stage editCardStage;
	
	private enum EditMode {edit_set, edit_card, get_image}
	private EditMode currentMode = EditMode.edit_set;
		
	private CardSet editableCardSet;
	private CardImageData editableCard;
	
	public EditorScreen() {
		
		newCardCreator = new Button(new TextureRegionDrawable(new TextureRegion(Statics.TEX_NEW_CARD_BUTTON_UP)),
									new TextureRegionDrawable(new TextureRegion(Statics.TEX_NEW_CARD_BUTTON_DOWN)));
		
		newCardCreator.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CardImageData cid = new CardImageData();
				Azalea.platform.doImageCapture(cid);
				setMode(EditMode.get_image);
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
		Button doneButton = new TextButton("TALLENNA", Statics.SKIN); //TODO
		//doneButton.setDisabled(true);
		//doneButton.setVisible(false);
		
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("SAVE");
				editableCardSet.updateLabelFront(labelEditor.getLabelPixmap());
				editableCardSet.saveData();
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		TextButton cancelButton = new TextButton("TAKAISIN", Statics.SKIN); //TODO
		cancelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		final ImageButton buttonErase = new ImageButton(Statics.SKIN);
		final ImageButton buttonDraw = new ImageButton(Statics.SKIN);
		final ImageButton buttonClear = new ImageButton(Statics.SKIN);
		
		buttonDraw.setBackground(new TextureRegionDrawable(new TextureRegion(Statics.TEX_ICON_DRAW)));
		buttonErase.setBackground(new TextureRegionDrawable(new TextureRegion(Statics.TEX_ICON_ERASE)));
		buttonClear.setBackground(new TextureRegionDrawable(new TextureRegion(Statics.TEX_ICON_CLEAR)));
		
		ButtonGroup<Button> bg = new ButtonGroup<Button>();
		bg.add(buttonDraw);
		bg.add(buttonErase);
		bg.setMaxCheckCount(1);
		
		buttonErase.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.setErase(true);
			}
		});
		buttonDraw.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.setErase(false);
			}
		});
		
		buttonClear.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.clearTexture();
				buttonClear.setChecked(false); //TODO: bad practice, change button
			}
		});
		
		Table editTitleToolsTable = new Table();
		editTitleToolsTable.add(buttonErase).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		editTitleToolsTable.add(buttonClear).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		editTitleToolsTable.row();
		editTitleToolsTable.add(buttonDraw).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		
		
		labelEditor = new LabelEditorActor();
		Table editTitleTable = new Table();
		editTitleTable.add(labelEditor).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.2f).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.center).expandX();
		editTitleTable.add(editTitleToolsTable).align(Align.left).expandX();
		
		Table mainEditContent = new Table();
		mainEditContent.setFillParent(true);
		mainEditContent.add(editTitleTable).colspan(2).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).height(Gdx.graphics.getWidth()*0.2f).align(Align.left).growX();
		mainEditContent.row();
		mainEditContent.add(cardScrollPane).colspan(2).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).fill().expand();
		mainEditContent.row();
		mainEditContent.add(cancelButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).growX();
		mainEditContent.add(doneButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).growX();
		editSetStage.addActor(mainEditContent);
		
		if(Statics.DEBUG) editSetStage.setDebugAll(true);
		
	}

	public void setEditableSet(CardSet editableSet){
		this.editableCardSet = editableSet;
		this.labelEditor.setLabel(editableSet.getLabelFrontPixmap());
	}
	
	private void resetCards(){
		cardTable.clear();
		cardTable.add(newCardCreator).size(300, 300).pad(32);
		for(final CardImageData c : editableCardSet.getCards()){
			final EditorCard ec = new EditorCard(c);
			Button del = new TextButton("Poista", Statics.SKIN, "contrast"); //TODO
			ec.add(del).align(Align.top).height(80).expand();
			del.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					editableCardSet.removeCard(c);
					resetCards();
				}
			});
			cardTable.add(ec).size(300, 300).pad(32);
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
		
		switch (currentMode) {
		case edit_set:
			editSetStage.act(Gdx.graphics.getDeltaTime());
			editSetStage.draw();
			break;
		case edit_card:
			editSetStage.act(Gdx.graphics.getDeltaTime());
			editSetStage.draw();
			break;

		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void dispose() {}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		if(currentMode == EditMode.get_image){
			FileHandle targetPath = Azalea.platform.getImagePath();
			if(targetPath.exists()){
				CardImageData c = new CardImageData();
				c.setSource(targetPath);
				c.generateCardTexture();
				editableCardSet.addCard(c);
			}
			resetCards();
			setMode(EditMode.edit_set);
		}
	}

	@Override
	public void touchDown(float x, float y, int id) {}

	@Override
	public void touchUp(float x, float y, int id) {}


}
