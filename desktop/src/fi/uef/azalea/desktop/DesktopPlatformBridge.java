package fi.uef.azalea.desktop;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.badlogic.gdx.files.FileHandle;

import fi.uef.azalea.PlatformBridge;

public class DesktopPlatformBridge implements PlatformBridge {
	
	//private final JFrame chooserFrame = new JFrame("Beep");
	
	private final JFrame targetJFrame;
	private final JFileChooser fileChooser;
	private FileHandle selectedImage = null;
	
	public DesktopPlatformBridge(JFrame targetJframe) {
		this.targetJFrame = targetJframe;
		fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new ImageFileFilter());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	@Override
	public void doImageSelect() {
		if(fileChooser.showOpenDialog(targetJFrame) == JFileChooser.APPROVE_OPTION){
			selectedImage = new FileHandle(fileChooser.getSelectedFile());
		} else {
			selectedImage = null;
		}
	}

	@Override
	public boolean imageCaptureSupported() {
		return false;
	}
	
	@Override
	public void doImageCapture() {
		System.err.println("Capturing not supported on desktop! (yet!)");
	}

	@Override
	public FileHandle getImagePath() {
		return selectedImage;
	}

	@Override
	public void interrupt() {
		selectedImage = null;
		fileChooser.setVisible(false);
	}
	
}
