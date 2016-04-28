package fi.uef.azalea.desktop;

import java.awt.Canvas;

import javax.swing.JFrame;

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

		Canvas canvas = new Canvas();
		JFrame frame = new JFrame();
		
		Azalea a = new Azalea(new DesktopPlatformBridge(frame));
		new LwjglApplication(a, config, canvas);
	    
	    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    frame.add( canvas );
	    frame.setSize( config.width, config.height );
	    frame.setLocationRelativeTo( null );
	    frame.setVisible(true);
	}
}
