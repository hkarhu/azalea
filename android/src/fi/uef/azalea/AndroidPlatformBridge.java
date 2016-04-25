package fi.uef.azalea;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.badlogic.gdx.files.FileHandle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class AndroidPlatformBridge implements PlatformBridge {
	
	Activity activity;
	public static final int SELECT_IMAGE_CODE = 1;
	public static final int CAPTURE_IMAGE_CODE = 2;
	
	private FileHandle currentImagePath;
	
	public AndroidPlatformBridge(Activity activity){
		this.activity = activity;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	public void doImageSelect(CardImageData cid) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(Intent.createChooser(intent, "Valitse kuva"), SELECT_IMAGE_CODE); //TODO		
	}
	
	@Override
	public void doImageCapture(CardImageData cid) {
		
		try {
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    String imageFileName = "muistipeli_" + timeStamp + "_";
		    File storageDir = Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_PICTURES);
		    File destination = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpg",         /* suffix */
		        storageDir      /* directory */
		    );
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
	        activity.startActivityForResult(intent, AndroidPlatformBridge.CAPTURE_IMAGE_CODE);
	        currentImagePath = new FileHandle(destination.getAbsolutePath());
		} catch(Exception e){
			System.err.println("Capture failed!");
			e.printStackTrace();
			currentImagePath = null;
			return;
		}
	}
	
	public void setPath(FileHandle fileHandle){
		this.currentImagePath = fileHandle;
	}

	@Override
	public FileHandle getImagePath() {
		return currentImagePath;
	}
}