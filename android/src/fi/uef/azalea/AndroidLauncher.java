package fi.uef.azalea;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import android.os.Environment;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//config.resizable = true;
		//config.vSyncEnabled = true;
		//config.title = Azalea.TITLE;
		//config.addIcon("icon.png", FileType.Internal);
		Azalea a = new Azalea();
		a.addFileSources(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/");
		initialize(a, config);
	}

}
