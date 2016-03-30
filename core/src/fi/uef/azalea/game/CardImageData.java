package fi.uef.azalea.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.corba.se.impl.orb.ParserTable.TestAcceptor1;

public class CardImageData {
	
	public FileHandle sourceFile;
	public TextureRegion texture;
	
	public CardImageData(FileHandle sourceFile, TextureRegion texture) {
		this.sourceFile = sourceFile;
		this.texture = texture;
	}

}
