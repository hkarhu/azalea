package fi.uef.azalea.desktop;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;

import fi.uef.azalea.PlatformBridge;

public class DesktopPlatformBridge implements PlatformBridge {
	
	private final JFileChooser fc = new JFileChooser();
	
	public DesktopPlatformBridge() {
		
		fc.setVisible(false);
		
	}
	
	@Override
	public void getGalleryImagePath() {
		Gdx.app.log("DesktopGalleryOpener", "Not implemented");
	}

	@Override
	public String getSelectedFilePath() {
		fc.showOpenDialog(null);
		return fc.getSelectedFile().getAbsolutePath();
	}

	
	
}
