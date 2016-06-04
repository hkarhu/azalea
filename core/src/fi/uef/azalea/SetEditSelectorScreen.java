package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SerializationException;

import fi.uef.azalea.Azalea.AppState;

public class SetEditSelectorScreen extends SetSelectorScreen {

	private NewCardSetCreator newCardSetCreator;
	private CardSet selectedSet;
	
	public SetEditSelectorScreen() {

		super();

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
		
		Table titleRow = new Table(Statics.SKIN);
		titleRow.add(cancelButton).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth());
		titleRow.add(new Label("Muokkaa korttipakkoja", Statics.SKIN, "title")).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).expandX().align(Align.center);
		titleRow.setBackground(new TiledDrawable(new TextureRegion(Statics.TEX_TITLEBG)));
		
		content.setFillParent(true);
		content.setBackground(new TiledDrawable(new TextureRegion(Statics.TEX_LISTBG)));
		content.add(titleRow).height((Statics.REL_BUTTON_HEIGHT+Statics.REL_BUTTON_PADDING)*Gdx.graphics.getWidth()).growX();
		content.row();
		content.add(cardSetScrollPane).colspan(3).fill().expand();
		stage.addActor(content);

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
		
		cardListTable.add(newCardSetCreator).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).padBottom(30).align(Align.center);
		cardListTable.row();
		
		//Add listeners and prepare table
		for(final CardSet s : cardSets){
			//Edit set to fit better for editor (Dirty code!) //TODO: REFACTOR
			s.selectButton.setText("Muokkaa");
			SlowButton deleteButton = new SlowButton("Poista", Statics.SKIN); //TODO
			deleteButton.addListener(new ChangeListener(){
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					s.deleteData();
					reloadCardSets();
				}
			});
			s.add(deleteButton).pad(8).size(150, 100).align(Align.right);
			
			s.selectButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					selectedSet = s;
					Azalea.changeState(AppState.edit);
				}
			});
			cardListTable.add(s).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).align(Align.center);
			cardListTable.row();
		}
		
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
