package fi.uef.azalea;

import android.os.Bundle;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import fi.uef.azalea.Azalea;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.resizable = true;
		config.vSyncEnabled = true;
		config.title = Azalea.TITLE;
		config.addIcon("icon.png", FileType.Internal);
		initialize(new Azalea(), config);
	}
}
