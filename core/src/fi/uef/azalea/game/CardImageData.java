package fi.uef.azalea.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CardImageData {
	
	public final FileHandle sourceFile;
	public final TextureRegion cardTexture;
	
	public CardImageData(FileHandle sourceFile) {
		this.sourceFile = sourceFile;
		TextureData textureData = TextureData.Factory.loadFromFile(sourceFile, Format.RGB565, false);
		Texture t = new Texture(textureData);
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.cardTexture = new TextureRegion(t);
	}
	
	public TextureRegion getOriginalImage(){
		TextureRegion original = new TextureRegion(new Texture(sourceFile));
		return original;
	}

}
