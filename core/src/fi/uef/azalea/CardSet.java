package fi.uef.azalea;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import fi.uef.azalea.editor.LabelEditorActor;

public class CardSet extends Table {
	
	private final FileHandle dataFolder;
	
	private String title = ""; //TODO
	
	private Array<CardImageData> cards; //Serialize!
	private Pixmap labelTextureBack; //Serialize!
	private Pixmap labelTextureFront; //Serialize!
	
	private String amountString = "";

	private Drawable labelDrawableBack;
	private Drawable labelDrawableFront;
	
	private boolean selected = false;
		
	public CardSet(FileHandle dataFolder) {
		
		this.dataFolder = dataFolder;
		
		labelTextureBack = new Pixmap(1024, 128, Format.RGBA8888);
		labelTextureFront = new Pixmap(512, 128, Format.RGBA4444);
		cards = new Array<CardImageData>();
		
		final TextButton selectButton = new TextButton("Valitse", Statics.SKIN, "toggle"); //TODO
		selectButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selected = !selected;
				if(selected){
					selectButton.setText("Valittu");
					selectButton.setChecked(true);
				} else {
					selectButton.setText("Valitse"); //TODO
					selectButton.setChecked(false);
				}
			}
		});
		
		Label cardAmountLabel = new Label(amountString, Statics.SKIN);
		
		this.add(selectButton).pad(8).size(150, 100).align(Align.left);
		this.add(new Label(title, Statics.SKIN)).align(Align.left).expand().fill(); //TODO
		this.add(cardAmountLabel).align(Align.bottomRight).padRight(30).padBottom(20).expandX(); //TODO
		
	}
	
	public void loadData() {
		CardSetData cardSetData = (new Json()).fromJson(CardSetData.class, dataFolder.child("meta.json"));
		this.title = cardSetData.title;
		
		this.labelTextureFront = PixmapIO.readCIM(dataFolder.child("label_front.cim"));
		this.labelTextureBack = PixmapIO.readCIM(dataFolder.child("label_back.cim"));
		
		this.labelDrawableFront = new TextureRegionDrawable(new TextureRegion(new Texture(labelTextureFront)));
		this.labelDrawableBack = new TextureRegionDrawable(new TextureRegion(new Texture(labelTextureBack)));
		
		this.cards = cardSetData.cards;
		amountString = cards.size + "korttia";
	}
	
	public void saveData(){
		CardSetData cardSet = new CardSetData();
		cardSet.title = this.title;
		cardSet.cards = this.cards;
		(new Json()).toJson(cardSet, dataFolder.child("meta.json"));
		PixmapIO.writeCIM(dataFolder.child("label_front.cim"), labelTextureFront);
		PixmapIO.writeCIM(dataFolder.child("label_back.cim"), labelTextureBack);
	}
	
	public void deleteData() {
		dataFolder.deleteDirectory();
	}

	/*
	public void setLabelTexture(Pixmap newLabel){
		Pixmap.setBlending(Blending.None);
		
		Pixmap p = new Pixmap(512, 128, Format.RGBA8888);
		p.setColor(0,0,0,0);
		p.fill();
		p.setColor(1,1,1,0);
		p.drawRectangle(1,1,511,127);
		p.drawPixmap(newLabel, 0,0, newLabel.getWidth(), newLabel.getHeight(), 2, 2, 510, 126);
		//labelTextureFront = new Texture(p);
		p.dispose();
		//TextureRegion region = new TextureRegion(labelTextureFront);
		
		//labelDrawable = new TextureRegionDrawable(region);
	}
	
	/*
	public void generateCardTexture(){

	}
	

	private Drawable generateLabelTexture(String title){
		Pixmap.setBlending(Blending.None);
		Pixmap p = new Pixmap(1024, 128, Format.RGBA8888);
		p.setColor(0,0,0,0);
		p.fill();
		p.setColor(1,1,1,0);
		p.drawRectangle(2,2,1022,126);

		Texture t = new Texture(p);
		p.dispose();

		TextureRegion region = new TextureRegion(t);

		Drawable d = new TextureRegionDrawable(region);
		return d;
	}
	
	private Drawable generateBackgroundTexture(FileHandle backFile) {
		Pixmap.setBlending(Blending.None);
		Pixmap p = new Pixmap(1024, 128, Format.RGBA8888);
		p.setColor(1,1,1,1);
		p.fill();
		
		final int sampleWidth = 64;
		final int scaler = 3;
		final int cs = sampleWidth/scaler;
		
		int shift = 0;
		for(CardImageData cid : getRandomCards(1024/sampleWidth)){
			Pixmap raw = new Pixmap(cid.getSourceFile());
			
			float s = 0;
			for(int yi=0; yi < 128; yi += 1){
				for(int xi=cs; xi < sampleWidth+cs; xi++){
					int tx = (int)(xi+shift+s);
					int cn = raw.getPixel((raw.getWidth()/(sampleWidth*scaler))*xi, (raw.getHeight()/128)*yi);
					int co = p.getPixel(tx, yi);
					//Some color blending magic
					int ct = ( (Math.min((((co >> 8) & 0xFF) + ((cn >>  8) & 0xFF)), 255) << 8) +
							   (Math.min((((co >> 16) & 0xFF) + ((cn >> 16) & 0xFF)), 255) << 16) +
							   (Math.min(((co >> 24) + (cn >> 24)), 255) << 24)
							 ) | 0x000000FF;
					p.drawPixel(tx, yi, ct);
				}
				s += 0.2f;
			}
			
			raw.dispose();
			shift += sampleWidth;
		}
		//PixmapIO.writePNG(backFile, p);
		Texture t = new Texture(p);
		p.dispose();
		
		TextureRegion region = new TextureRegion(t);
		
		Drawable d = new TextureRegionDrawable(region);
		return d;
	}
	*/

	public void updateLabelFront(Pixmap labelPixmap) {
		this.labelTextureFront.drawPixmap(labelPixmap, 0,0, labelPixmap.getWidth(), labelPixmap.getHeight(), 2, 2, 510, 126);
	}
	
	public Array<CardImageData> getCards(){
		return cards;
	}
	
	public Array<CardImageData> getRandomCards(int num){
		Array<CardImageData> out = new Array<CardImageData>();
		cards.shuffle();
		for(int i=0; i < Math.min(num, cards.size); i++){
			out.add(cards.get(i));
		}
		return out;
	}

	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public String toString() {
		return title;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void addCard(CardImageData newCard) {
		this.cards.add(newCard);
		amountString = cards.size + "korttia";
	}
		
	@Override
	protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		
		if(selected) batch.setColor(1,1,1,1); else batch.setColor(0.5f,0.5f,0.5f,1);
		if(labelDrawableBack != null) labelDrawableBack.draw(batch, x, y, getWidth(), getHeight());
		
		//Label
		if(labelDrawableFront != null){
			float sizeScaler = 0.8f/(labelDrawableFront.getMinHeight()/getHeight());
			float shadowShift = 8;
			float yShift = (getHeight()-(labelDrawableFront.getMinHeight()*sizeScaler))*0.5f;
			float xShift = 200;
			
			batch.setColor(0,0,0,0.8f);
			labelDrawableFront.draw(batch, x+xShift+shadowShift, y+yShift-shadowShift, labelDrawableFront.getMinWidth()*sizeScaler, labelDrawableFront.getMinHeight()*sizeScaler);
			batch.setColor(1,1,1,1);
			labelDrawableFront.draw(batch, x+xShift, y+yShift, labelDrawableFront.getMinWidth()*sizeScaler, labelDrawableFront.getMinHeight()*sizeScaler);
		}
		
	}

	public Pixmap getLabelFrontPixmap() {
		return labelTextureFront;
	}

	public void removeCard(CardImageData c) {
		cards.removeValue(c, true);
	}

}
