package fi.uef.azalea.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
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
	private Button deleteButton;
	private Stage editCardStage;
	
	private enum EditMode {edit_set, edit_card, edit_label, get_image}
	private EditMode currentMode = EditMode.edit_set;
	
	private final Dialog cardAddFailDialog;
	
	
		
	private CardSet editableCardSet;
	private CardImageData editableCard;
	
	public EditorScreen() {
		
		cardAddFailDialog = new Dialog("Kuva oli jo pakassa!", Statics.SKIN, "default"); //TODO
		Label failLabel = new Label("Et voi lisätä samaa kuvaa kahta kertaa.\nMuutenhan pelistä tulisi ihan hassu.", Statics.SKIN, "medium-font", Color.WHITE);
		TextButton failOKButton = new TextButton("Hups, anteeksi!", Statics.SKIN); //TODO
		failOKButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAddFailDialog.hide();
			}
		});
		cardAddFailDialog.setSize(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getHeight()*0.5f);
		cardAddFailDialog.getButtonTable().add(failOKButton);
		cardAddFailDialog.getContentTable().add(failLabel);
		
		final Dialog newCardDialog = new Dialog("Haetaanko kuva kameralla vai laitteen muistista?", Statics.SKIN, "default"); //TODO
		
		TextButton cameraButton = new TextButton("Kamerasta", Statics.SKIN); //TODO
		cameraButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				newCardDialog.hide();
				Azalea.platform.doImageCapture();
				setMode(EditMode.get_image);
			}
		});

		TextButton storageButton = new TextButton("Muistista", Statics.SKIN); //TODO
		storageButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				newCardDialog.hide();
				Azalea.platform.doImageSelect();
				setMode(EditMode.get_image);
			}
		});
		
		newCardDialog.getButtonTable().add(storageButton);
		newCardDialog.getButtonTable().add(cameraButton);
		
		newCardCreator = new Button(new TextureRegionDrawable(new TextureRegion(Statics.TEX_NEW_CARD_BUTTON_UP)),
						 			new TextureRegionDrawable(new TextureRegion(Statics.TEX_NEW_CARD_BUTTON_DOWN)));
		newCardCreator.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(Azalea.platform.imageCaptureSupported()){
					newCardDialog.show(editSetStage);
				} else {
					Azalea.platform.doImageSelect();
					setMode(EditMode.get_image);
				}
			}
		});

		editSetStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		cardTable = new Table();
		ScrollPane cardScrollPane = new ScrollPane(cardTable);
		cardScrollPane.setBounds(Gdx.graphics.getWidth()*0.05f, Gdx.graphics.getHeight()*0.05f, Gdx.graphics.getWidth()*0.9f, Gdx.graphics.getHeight()*0.85f);
		cardScrollPane.setScrollingDisabled(false, true);
		cardScrollPane.setForceScroll(true, false);
		
		//cardSetScrollPane.setScrollBarPositions(true, true);
		//cardSetScrollPane.setupFadeScrollBars(1, 1);
		cardScrollPane.setFadeScrollBars(false);
		Button doneButton = new TextButton("Valmis", Statics.SKIN); //TODO

		Button editLabelButton = new TextButton("Muokkaa otsikkoa", Statics.SKIN); //TODO
		editLabelButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
			}
		});
		
		deleteButton = new TextButton("Poista tämä pakka", Statics.SKIN); //TODO
		deleteButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(editableCardSet != null){
					editableCardSet.deleteData();
					Azalea.changeState(AppState.select_edit);
				}
			}
		});
		//doneButton.setDisabled(true);
		//doneButton.setVisible(false);
		
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(editableCardSet.getCards().size > 0 || !labelEditor.isLabelEmpty()){
					editableCardSet.updateLabelFront(labelEditor.getLabelPixmap());
					editableCardSet.saveData();
				}
				Azalea.changeState(AppState.select_edit);
			}
		});
		
		TextureRegion eraseDrawable = new TextureRegion(Statics.TEX_ICON_ERASE);
		TextureRegion drawDrawable = new TextureRegion(Statics.TEX_ICON_DRAW);
		TextureRegion clearDrawable = new TextureRegion(Statics.TEX_ICON_CLEAR);

		final TextButton buttonErase = new TextButton("Kumita", Statics.SKIN, "toggle");
		final TextButton buttonDraw = new TextButton("Piirrä", Statics.SKIN, "toggle");
//		final Button buttonErase = new LogoButton(eraseDrawable, Statics.SKIN);
//		final Button buttonDraw = new LogoButton(drawDrawable, Statics.SKIN);
		final Button buttonClear = new LogoButton(clearDrawable, Statics.SKIN);
		
		buttonErase.setProgrammaticChangeEvents(false);
		buttonErase.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.setErase(true);
				buttonErase.setChecked(!buttonErase.isChecked());
				buttonDraw.setChecked(false);
			}
		});
		
		buttonDraw.setProgrammaticChangeEvents(false);
		buttonDraw.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.setErase(false);
				buttonDraw.setChecked(!buttonDraw.isChecked());
				buttonErase.setChecked(false);
			}
		});
		
		buttonClear.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				labelEditor.clearTexture();
				buttonDraw.setChecked(false);
				buttonErase.setChecked(false);
			}
		});
		
		Table editTitleToolsTable = new Table();
		editTitleToolsTable.add(buttonDraw).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*0.75f*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		editTitleToolsTable.row();
		editTitleToolsTable.add(buttonErase).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*0.75f*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		editTitleToolsTable.row();
		editTitleToolsTable.add(buttonClear).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*0.75f*Gdx.graphics.getWidth(),Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expand();
		
		
		labelEditor = new LabelEditorActor();
		Table editTitleTable = new Table();
		editTitleTable.add(labelEditor).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.2f).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.center).expandX();
		editTitleTable.add(editTitleToolsTable).align(Align.left).expandX();
		//editTitleTable.setBackground(new TiledDrawable(new TextureRegion(Statics.TEX_LISTBG)));
		
		Table mainEditContent = new Table();
		mainEditContent.setBackground(new TiledDrawable(new TextureRegion(Statics.TEX_MENUBG)));
		mainEditContent.setFillParent(true);
		mainEditContent.add(editTitleTable).colspan(2).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).height(Gdx.graphics.getWidth()*0.2f).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.left).growX();
		mainEditContent.row();
		//mainEditContent.add(editLabelButton).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth()*2, Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.center).growX();
		
		//mainEditContent.row();
		mainEditContent.add(cardScrollPane).colspan(2).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).fill().expand();
		mainEditContent.row();
		mainEditContent.add(deleteButton).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth()*2, Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.center).growX();
		mainEditContent.add(doneButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).growX();
		editSetStage.addActor(mainEditContent);
		
		if(Statics.DEBUG) editSetStage.setDebugAll(true);
		
	}

	public void setEditableSet(CardSet editableSet){
		this.editableCardSet = editableSet;
		this.labelEditor.setEditableLabel(editableCardSet.getLabelFrontPixmap());
	}
	
	private void resetCards(){
		cardTable.clear();
		cardTable.add(newCardCreator).size(0.234f*Gdx.graphics.getWidth(), 0.234f*Gdx.graphics.getWidth()).pad(32);
		for(final CardImageData c : editableCardSet.getCards()){
			final EditorCard ec = new EditorCard(c);
			Button del = new TextButton("Poista", Statics.SKIN); //TODO
			ec.add(del).align(Align.top).height(80).expand();
			del.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					editableCardSet.removeCard(c);
					resetCards();
				}
			});
			cardTable.add(ec).size(0.234f*Gdx.graphics.getWidth(), 0.234f*Gdx.graphics.getWidth()).pad(32).align(Align.center);
		}
		if(!editableCardSet.hasSavedData()){
			deleteButton.setVisible(false);
			deleteButton.setDisabled(true);
		} else {
			deleteButton.setVisible(true);
			deleteButton.setDisabled(false);
		}
	}
	
	@Override
	public void init() {
		resetCards();
		this.ready = true;
		setMode(EditMode.edit_set);
	}

	private void setMode(EditMode mode){
		this.currentMode = mode;
		if(mode == EditMode.edit_label){
			labelEditor.setEdit(false);
		} else {
			labelEditor.setEdit(true);
		}
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
		case get_image:
		case edit_label:
		case edit_set:
			editSetStage.act(Gdx.graphics.getDeltaTime());
			editSetStage.draw();
			break;
		case edit_card:
			editCardStage.act(Gdx.graphics.getDeltaTime());
			editCardStage.draw();
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

	private void getImage(){
		FileHandle targetPath = Azalea.platform.getImagePath();
		if(targetPath != null && targetPath.exists()){
			editableCard = new CardImageData();
			editableCard.setSource(targetPath);
			if(editableCardSet.containsCard(editableCard)){
				cardAddFailDialog.show(editSetStage);
			} else {
				editableCard.generateCardTexture();
				editableCardSet.addCard(editableCard);
			}
		}
		resetCards();
		setMode(EditMode.edit_set);
	}
	
	@Override
	public void resume() {
		if(currentMode == EditMode.get_image) getImage();
	}

	@Override
	public void touchDown(float x, float y, int id) {
		if(currentMode == EditMode.get_image) getImage();		
	}

	@Override
	public void touchUp(float x, float y, int id) {}


}
