package fi.uef.azalea.desktop;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import fi.uef.azalea.CardImageData;
import fi.uef.azalea.PlatformBridge;

public class DesktopPlatformBridge implements PlatformBridge {
	
	private final JFileChooser fc = new JFileChooser();
	
	public DesktopPlatformBridge() {
		
		fc.setVisible(false);
		
	}

	@Override
	public void doImageSelect(CardImageData cid) {
		fc.setVisible(true);
	}

	@Override
	public void doImageCapture(CardImageData cid) {
		// TODO Auto-generated method stub
		fc.setVisible(true);
	}

	@Override
	public FileHandle getImagePath() {
		return null;
	}
	

	
	
}
