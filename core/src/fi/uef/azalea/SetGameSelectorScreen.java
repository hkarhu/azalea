package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.Azalea.AppState;

public class SetGameSelectorScreen extends SetSelectorScreen {

	private Array<CardSet> selectedSets;

	private int cardAmount = 2;

	public SetGameSelectorScreen() {

		super();

		selectedSets = new Array<CardSet>();

		final Dialog cardAmountDialog = new Dialog("Valitse parien määrä", Statics.SKIN, "dialog"); //TODO

		TextButton dialogOK = new TextButton("Pelaa", Statics.SKIN); //TODO
		dialogOK.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
				Azalea.changeState(AppState.game);
			}
		});

		TextButton dialogCancel = new TextButton("Takaisin", Statics.SKIN); //TODO
		dialogCancel.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				cardAmountDialog.hide();
			}
		});

		cardAmountDialog.getButtonTable().add(dialogCancel).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).expandX();
		cardAmountDialog.getButtonTable().add(dialogOK).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).expandX();

		doneButton = new TextButton("Valitse", Statics.SKIN); //TODO
		doneButton.setDisabled(true);
		doneButton.setVisible(false);
		doneButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int maxNumCards = 0;
				for(CardSet s : selectedSets){
					maxNumCards += s.getCards().size;
				}
				
				if(maxNumCards < 5){
					cardAmount = maxNumCards;
					Azalea.changeState(AppState.game);
					return;
				}

				cardAmountDialog.getContentTable().clear();

				int maxCards = (maxNumCards > 32 ? 32 : maxNumCards);
				final Slider amountSlider = new Slider(2, maxCards, 1, false, Statics.SKIN);
				amountSlider.setValue((cardAmount > maxCards ? maxCards : cardAmount));
				final Label amountLabel = new Label("Pareja: " + cardAmount, Statics.SKIN, "title"); //TODO
				amountSlider.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						cardAmount = (int) amountSlider.getValue();
						amountLabel.setText("Pareja: " + cardAmount); //TODO
					}
				});
				cardAmountDialog.getContentTable().add(amountLabel).align(Align.center).pad(20).expand();
				cardAmountDialog.getContentTable().row();
				cardAmountDialog.getContentTable().add(amountSlider).size(Gdx.graphics.getWidth()*0.6f, Gdx.graphics.getHeight()*0.3f).expandX();
				cardAmountDialog.show(stage);
			}
		});
		
		//cardListTable.setBackground(null);
		
		Table content = new Table();
		content.setFillParent(true);
		content.add(new Label("Valitse pelissä käytettävät korttipakat", Statics.SKIN, "title")).colspan(2).pad(Statics.REL_ITEM_PADDING*Gdx.graphics.getWidth()).align(Align.center); //TODO
		content.row();
		content.add(cardSetScrollPane).colspan(2).fill().expand();
		content.row();
		content.add(cancelButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.left).growX();
		content.add(doneButton).pad(Statics.REL_BUTTON_PADDING*Gdx.graphics.getWidth()).size(Statics.REL_BUTTON_WIDTH*Gdx.graphics.getWidth(), Statics.REL_BUTTON_HEIGHT*Gdx.graphics.getWidth()).align(Align.right).growX();
		stage.addActor(content);

	}

	protected void reloadCardSets() {

		super.reloadCardSets();

		//Add listeners and prepare table
		for(final CardSet s : cardSets){
			//if(s.getCards().size < 2) continue; //If the set is too small for play, skip it
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
			cardListTable.add(s).size(Gdx.graphics.getWidth()*0.8f, Gdx.graphics.getWidth()*0.1f).pad(5).align(Align.center);
			cardListTable.row();
		}

	}

	public Array<CardImageData> getCards() {
		Array<CardImageData> cards = new Array<CardImageData>();
		int n = cardAmount/selectedSets.size;
		for(CardSet s : selectedSets){
			cards.addAll(s.getRandomCards(n));
		}
		return cards;
	}

	private void unselectAll() {
		for(CardSet s : selectedSets){
			s.setSelected(false);
		}
		selectedSets.clear();
	}

	@Override
	public void init() {
		super.init();
		unselectAll();
		reloadCardSets();
		ready = true;
	}

}
