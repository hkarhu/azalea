package fi.uef.azalea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class AndroidLauncher extends AndroidApplication {
	
	String userImagePath = null;
	AndroidPlatformBridge platform;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.resizable = true;
		//config.vSyncEnabled = true;
		//config.title = Azalea.TITLE;
		//config.addIcon("icon.png", FileType.Internal);
		
        // we need to change the default pixel format - since it does not include an alpha channel 
        // we need the alpha channel so the camera preview will be seen behind the GL scene
        config.r = 8;
        config.g = 8;
        config.b = 8;
        config.a = 8;
		
		platform = new AndroidPlatformBridge(this);
		Azalea a = new Azalea(platform);
		initialize(a, config);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == AndroidPlatformBridge.SELECT_IMAGE_CODE) {
			Uri imageUri = data.getData();
			this.userImagePath = getPath(imageUri);
			Gdx.app.log("AndroidGalleryOpener", "Image path is " + userImagePath);
			platform.setImageResult(userImagePath);
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}

	private String getPath(Uri uri) {

		if(uri.getScheme().equalsIgnoreCase("file")){
			return uri.getPath();
		}

		Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA } , null, null, null);

		if (cursor == null) {
			return null;
		}

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		String filePath = cursor.getString(column_index);

		cursor.close();

		return filePath;

	}

}
