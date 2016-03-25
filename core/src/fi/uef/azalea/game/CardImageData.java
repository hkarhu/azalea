package fi.uef.azalea.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CardImageData {
	
	public FileHandle sourceFile;
	public TextureRegion texture;
	
	public CardImageData(FileHandle sourceFile, TextureRegion texture) {
		this.sourceFile = sourceFile;
		this.texture = texture;
	}

}
