package fi.uef.azalea;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import fi.uef.azalea.game.CardImageData;

public class CardSet extends Table {
	
	private final FileHandle datafile;
	
	private String title = "Uusi korttisarja"; //TODO
	private TextureAtlas cardAtlas;
	private Array<CardImageData> cards;
	
	private Label cardAmountLabel;
	private Texture labelTextureBack;
	private Texture labelTexture;
	private Drawable labelDrawableBack;
	private Drawable labelDrawable;
	
	private boolean selected = false;
		
	public CardSet(FileHandle datafile) {
		
		this.datafile = datafile;
		
		cardAtlas = new TextureAtlas();
		cards = new Array<CardImageData>();
		
		final TextButton selectButton = new TextButton("SELECT", Statics.SKIN); //TODO
		selectButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selected = !selected;
				if(selected) selectButton.setText("SELECTED"); else selectButton.setText("SELECT"); //TODO  
			}
		});
		
		cardAmountLabel = new Label(cards.size + " CARDS", Statics.SKIN);
		
		this.add(selectButton).pad(8).size(150, 100).align(Align.left);
		this.add(new Label(title, Statics.SKIN)).align(Align.left).expand().fill(); //TODO
		this.add(cardAmountLabel).align(Align.bottomRight).padRight(30).padBottom(20).expandX(); //TODO		
		
		/*
		FileHandle[] files = targetFolder.list();
		cards = new Array<CardImageData>();
		for(FileHandle file: files) {
			cards.add(new CardImageData(file));
		}
		FileHandle labelFile = targetFolder.parent().child(title+"_label.png");
		FileHandle backFile = targetFolder.parent().child(title+"_back.png");
		
		if(labelFile.exists()){
			coverTextureTitle = new TextureRegionDrawable(new TextureRegion(new Texture(labelFile)));
		} else {
			coverTextureTitle = generateLabelTexture(title);
		}
		
		if(backFile.exists()){
			coverTextureBack = new TextureRegionDrawable(new TextureRegion(new Texture(backFile)));
		} else {
			coverTextureBack = generateBackgroundTexture(backFile);
		}
		*/

		
	}
	
	public void setLabelTexture(Pixmap newLabel){
		Pixmap.setBlending(Blending.None);
		
		Pixmap p = new Pixmap(512, 128, Format.RGBA8888);
		p.setColor(0,0,0,0);
		p.fill();
		p.setColor(1,1,1,0);
		p.drawRectangle(1,1,511,127);
		p.drawPixmap(newLabel, 0,0, newLabel.getWidth(), newLabel.getHeight(), 2, 2, 510, 126);
		labelTexture = new Texture(p);
		p.dispose();
		
		TextureRegion region = new TextureRegion(labelTexture);
		
		labelDrawable = new TextureRegionDrawable(region);
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
			Pixmap raw = new Pixmap(cid.sourceFile);
			
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
	
	public void save(){
		
	}
	
	@Override
	protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
		
		if(selected) batch.setColor(1,1,1,1); else batch.setColor(0.5f,0.5f,0.5f,1);
		if(labelDrawableBack != null) labelDrawableBack.draw(batch, x, y, getWidth(), getHeight());
		
		//Label
		if(labelDrawable != null){
			float sizeScaler = 0.8f/(labelDrawable.getMinHeight()/getHeight());
			float shadowShift = 8;
			float yShift = (getHeight()-(labelDrawable.getMinHeight()*sizeScaler))*0.5f;
			float xShift = 200;
			
			batch.setColor(0,0,0,0.8f);
			labelDrawable.draw(batch, x+xShift+shadowShift, y+yShift-shadowShift, labelDrawable.getMinWidth()*sizeScaler, labelDrawable.getMinHeight()*sizeScaler);
			batch.setColor(1,1,1,1);
			labelDrawable.draw(batch, x+xShift, y+yShift, labelDrawable.getMinWidth()*sizeScaler, labelDrawable.getMinHeight()*sizeScaler);
		}
		
	}
	
	@Override
	public String toString() {
		return title;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void addCard(CardImageData newCard) {
		newCard.setTextureAtlas(cardAtlas);
		this.cards.add(newCard);
	}

	
}
