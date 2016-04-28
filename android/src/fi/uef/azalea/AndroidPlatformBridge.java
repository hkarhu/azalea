package fi.uef.azalea;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.badlogic.gdx.files.FileHandle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class AndroidPlatformBridge implements PlatformBridge {
	
	Activity activity;
	public static final int SELECT_IMAGE_CODE = 1;
	public static final int CAPTURE_IMAGE_CODE = 2;
	
	private FileHandle selectedImage;
	
	public AndroidPlatformBridge(Activity activity){
		this.activity = activity;
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	public void doImageSelect() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(Intent.createChooser(intent, "Valitse kuva"), SELECT_IMAGE_CODE); //TODO		
	}
	
	@Override
	public boolean imageCaptureSupported() {
        if (activity.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
	}
	@Override
	public void doImageCapture() {
		
		try {
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		    File destination = File.createTempFile(
		    	"muistipeli_" + timeStamp,
		        ".jpg",
		        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
		    );
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
	        activity.startActivityForResult(intent, AndroidPlatformBridge.CAPTURE_IMAGE_CODE);
	        selectedImage = new FileHandle(destination.getAbsolutePath());
		} catch(Exception e){
			System.err.println("Capture failed!");
			e.printStackTrace();
			selectedImage = null;
			return;
		}
	}
	
	//Called from main-activity when the intent returns
	public void setPath(FileHandle fileHandle){
		this.selectedImage = fileHandle;
	}

	@Override
	public FileHandle getImagePath() {
		return selectedImage;
	}

	@Override
	public void interrupt() {
		activity.finishActivity(AndroidPlatformBridge.CAPTURE_IMAGE_CODE);
		activity.finishActivity(AndroidPlatformBridge.SELECT_IMAGE_CODE);
		selectedImage = null;
	}
	
}