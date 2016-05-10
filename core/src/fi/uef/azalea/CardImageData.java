package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class CardImageData implements Serializable {
	
	private FileHandle sourceFile;
	private FileHandle thumbFile;
	private Pixmap cardPixmap;
	private TextureRegion cardTexture;

	public void setSource(FileHandle sourceFile){
		this.sourceFile = sourceFile;

		if(sourceFile == null){
			System.err.println("Source was set as null!");
		}
	}
	
	public void generateCardTexture(){
		thumbFile = Gdx.files.local(Statics.CARD_IMAGE_CACHE).child(sourceFile.nameWithoutExtension() + "_thumb.png");
		if(thumbFile.exists()){
			cardPixmap = new Pixmap(thumbFile);
		} else {
			Pixmap.setBlending(Blending.None);
			Pixmap texture = new Pixmap(sourceFile);
			cardPixmap = new Pixmap(Statics.CARD_PIXMAP_SIZE, Statics.CARD_PIXMAP_SIZE, Format.RGBA4444); //TODO: check performance with 8888
			cardPixmap.drawPixmap(texture, 0, 0, texture.getWidth(), texture.getHeight(), 0, 0, Statics.CARD_PIXMAP_SIZE, Statics.CARD_PIXMAP_SIZE);
			texture.dispose();
		
			//TODO: do nice edges
			//cardbase.drawPixmap(StaticTextures.CARD_MASK_PIXMAP, 0, 0, StaticTextures.CARD_MASK_PIXMAP.getWidth(), StaticTextures.CARD_MASK_PIXMAP.getHeight(), 0, 0, PIXMAP_SIZE, PIXMAP_SIZE);
			PixmapIO.writePNG(thumbFile, cardPixmap);
		}
		Texture tex = new Texture(cardPixmap);
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.cardTexture = new TextureRegion(tex);
	}
	
	public TextureRegion getCardTexture(){
		if(cardTexture == null){
			generateCardTexture();
		}
		return cardTexture;
	}
	
	public TextureRegion getFullImageTexture(){
		TextureRegion original = new TextureRegion(new Texture(sourceFile));
		return original;
	}
	
	public FileHandle getSourceFile(){
		return sourceFile;
	}

	@Override
	public void write(Json json) {
		System.out.println("serialize");
		json.writeValue("sourceFile", sourceFile.path());
		if(thumbFile == null) generateCardTexture();
		json.writeValue("thumbFile", thumbFile.path());
		System.out.println("write source file: " + sourceFile.path());
		System.out.println("write thumbFile file: " + thumbFile.path());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		System.out.println("unserialize");
		if(jsonData.child().name().equals("sourceFile")){
			sourceFile = new FileHandle(jsonData.child().asString());
			System.out.println("load source file: " + sourceFile.path());
		} 
		if(jsonData.child().name().equals("thumbFile")){
			thumbFile = new FileHandle(jsonData.child().asString());
			System.out.println("load source file: " + thumbFile.path());
		} 
	}

}
