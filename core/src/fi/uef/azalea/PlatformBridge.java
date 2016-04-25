package fi.uef.azalea;

import com.badlogic.gdx.files.FileHandle;

public interface PlatformBridge {

	//public enum Platform { ANDROID, DESKTOP }; 
	//private final Platform currentPlatform;

	public void doImageSelect(CardImageData cid);
	public void doImageCapture(CardImageData cid);
	public FileHandle getImagePath();
	
}
