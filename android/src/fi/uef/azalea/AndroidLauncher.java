package fi.uef.azalea;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class AndroidLauncher extends AndroidApplication {
	
	AndroidPlatformBridge platform;
    
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.resizable = true;
		//config.vSyncEnabled = true;
		//config.title = Azalea.TITLE;
		//config.addIcon("icon.png", FileType.Internal);

		platform = new AndroidPlatformBridge(this);
		Azalea a = new Azalea(platform);
		initialize(a, config);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
			if (requestCode == AndroidPlatformBridge.SELECT_IMAGE_CODE) {
				Uri imageUri = data.getData();
				platform.setPath(new FileHandle(getPath(imageUri)));
				return;
			} else if (requestCode == AndroidPlatformBridge.CAPTURE_IMAGE_CODE) {
				return;
			}
		} else {
			platform.setPath(null);
		}
        super.onActivityResult(requestCode, resultCode, data);
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
	
	/*
	private void selectImage() {
	    final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
	    AlertDialog.Builder builder = new AlertDialog.Builder(AndroidLauncher.this);
	    builder.setTitle("Add Photo!");
	    builder.setItems(items, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int item) {
	            if (items[item].equals("Take Photo")) {
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                startActivityForResult(intent, AndroidPlatformBridge.CAPTURE_IMAGE_CODE);
	            } else if (items[item].equals("Choose from Library")) {
	                Intent intent = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                intent.setType("image/*");
	                startActivityForResult(
	                        Intent.createChooser(intent, "Select File"),
	                        AndroidPlatformBridge.SELECT_IMAGE_CODE);
	            } else if (items[item].equals("Cancel")) {
	                dialog.dismiss();
	            }
	        }
	    });
	    builder.show();
	}
	*/
}
