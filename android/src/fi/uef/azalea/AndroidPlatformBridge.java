package fi.uef.azalea;

import android.app.Activity;
import android.content.Intent;

public class AndroidPlatformBridge implements PlatformBridge {
	
	Activity activity;
	public static final int SELECT_IMAGE_CODE = 1;
	
	private String currentImagePath;
	
	public AndroidPlatformBridge(Activity activity){
		this.activity = activity;
	}

	@Override
	public void getGalleryImagePath() {
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(Intent.createChooser(intent, "Select Users Image"), SELECT_IMAGE_CODE); //TODO

	}
	
	public void setImageResult(String path){
		currentImagePath = path;
	}
	
	public String getSelectedFilePath(){
		return currentImagePath;
	}

}