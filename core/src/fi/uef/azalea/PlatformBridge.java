package fi.uef.azalea;

public interface PlatformBridge {

	//public enum Platform { ANDROID, DESKTOP }; 
	//private final Platform currentPlatform;

	public void getGalleryImagePath();
	public String getSelectedFilePath();
	
}
