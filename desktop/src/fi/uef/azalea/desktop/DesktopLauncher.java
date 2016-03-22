package fi.uef.azalea.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.uef.azalea.Azalea;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.vSyncEnabled = true;
		config.title = Azalea.TITLE;
		config.width = 1280;
		config.height = 800;
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new Azalea(), config);
	}
}
