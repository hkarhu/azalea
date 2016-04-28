package fi.uef.azalea;

import com.badlogic.gdx.files.FileHandle;

public interface PlatformBridge {

	//public enum Platform { ANDROID, DESKTOP }; 
	//private final Platform currentPlatform;

	public void doImageSelect();
	public void doImageCapture();
	public FileHandle getImagePath();
	public boolean imageCaptureSupported();
	public void interrupt();
	
}
