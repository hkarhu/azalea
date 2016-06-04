package fi.uef.azalea.desktop;

import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.UIManager;

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
		
		//Decoration stuff
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			System.out.println("Setting look and feel");
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				// windows-support so doesn't look ugly
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {
			System.err.println("Unable to set LookAndFeel");
		}
		
		Azalea a = new Azalea(new DesktopPlatformBridge(frame));
		new LwjglApplication(a, config, canvas);
	    
	    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    frame.setTitle(Statics.TITLE);
	    frame.add( canvas );
	    frame.setSize( config.width, config.height );
	    frame.setLocationRelativeTo( null );
	    frame.setVisible(true);
	}
}
