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
import com.badlogic.gdx.utils.Align;

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
		
		cardListTable.setBackground(new TiledDrawable(new TextureRegion(Statics.TEX_TITLEBG)));
		
		content.setFillParent(true);
		content.add(new Label("Muokkaa korttipakkoja", Statics.SKIN, "title")).height(Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).colspan(3).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.center);
		content.row();
		content.add(cardSetScrollPane).colspan(3).fill().expand();
		content.row();
		content.add(cancelButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).growX();
		stage.addActor(content);

	}

	protected void reloadCardSets() {
		super.reloadCardSets();
		
		cardListTable.add(newCardSetCreator).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).padBottom(30).align(Align.center);
		cardListTable.row();
		
		//Add listeners and prepare table
		for(final CardSet s : cardSets){
			s.addListener(new ChangeListener() {
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
