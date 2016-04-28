package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import fi.uef.azalea.Azalea.AppState;

public class SetEditSelectorScreen extends SetSelectorScreen {

	private NewCardSetCreator newCardSetCreator;
	private CardSet selectedSet;

	private TextButton deleteButton;
	
	public SetEditSelectorScreen() {

		super();
		
		doneButton = new TextButton("Muokkaa", Statics.SKIN); //TODO
		doneButton.setDisabled(true);
		doneButton.setVisible(false);
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Azalea.changeState(AppState.edit);
			}
		});
		
		deleteButton = new TextButton("Poista", Statics.SKIN); //TODO
		deleteButton.setDisabled(true);
		deleteButton.setVisible(false);
		deleteButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedSet != null){
					selectedSet.deleteData();
					reloadCardSets();
				}
			}
		});

		newCardSetCreator = new NewCardSetCreator();
		newCardSetCreator.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				FileHandle targetFile = Gdx.files.local("sets/" + System.nanoTime());
				if(!targetFile.exists()) {
					selectedSet = new CardSet(targetFile);
					Azalea.changeState(AppState.edit);
				} else {
					System.out.println("Not gonna make a new set on top of existing file.");
				}
			}
		});

		Table content = new Table();
		content.setFillParent(true);
		content.add(new Label("Muokkaa korttipakkoja", Statics.SKIN)).height(Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).colspan(3).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.center);
		content.row();
		content.add(cardSetScrollPane).colspan(3).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).fill().expand();
		content.row();
		content.add(cancelButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).growX();
		content.add(deleteButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).growX();
		content.add(doneButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).growX();
		stage.addActor(content);

	}

	protected void reloadCardSets() {
		super.reloadCardSets();
		
		cardListTable.add(newCardSetCreator).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).align(Align.center);
		cardListTable.row();
		
		//Add listeners and prepare table
		for(final CardSet s : cardSets){
			s.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(selectedSet != null) selectedSet.setSelected(false);
					selectedSet = null;
					if(s.isSelected()) selectedSet = s;
					
					if(selectedSet != null){
						doneButton.setDisabled(false);
						doneButton.setVisible(true);
						deleteButton.setDisabled(false);
						deleteButton.setVisible(true);
					} else {
						doneButton.setDisabled(true);
						doneButton.setVisible(false);
						deleteButton.setDisabled(true);
						deleteButton.setVisible(false);
					}
				}
			});
			cardListTable.add(s).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).align(Align.center);
			cardListTable.row();
		}

		selectedSet = null;
		doneButton.setDisabled(true);
		doneButton.setVisible(false);
		deleteButton.setDisabled(true);
		deleteButton.setVisible(false);
		
	}

	public CardSet getSelectedSet(){
		return selectedSet;
	}
	
	@Override
	public void init() {
		super.init();
		reloadCardSets();
		ready = true;
	}

}
