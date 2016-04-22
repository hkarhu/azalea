package fi.uef.azalea.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fi.uef.azalea.Azalea;
import fi.uef.azalea.Statics;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.vSyncEnabled = true;
		config.title = Statics.TITLE;
		config.width = Statics.DEFAULT_WIDTH;
		config.height = Statics.DEFAULT_HEIGHT;
		config.addIcon("icon.png", FileType.Internal);

		Azalea a = new Azalea(new DesktopPlatformBridge());
		new LwjglApplication(a, config);
	}
}
