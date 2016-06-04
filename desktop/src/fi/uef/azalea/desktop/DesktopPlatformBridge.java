package fi.uef.azalea.desktop;
import java.awt.EventQueue;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.files.FileHandle;

import fi.uef.azalea.PlatformBridge;

public class DesktopPlatformBridge implements PlatformBridge {

	//private final JFrame chooserFrame = new JFrame("Beep");

	private final JFrame targetJFrame;
	private final JFileChooser fileChooser;
	private final FileFilter imageFilter; 
	private FileHandle selectedImage = null;

	public DesktopPlatformBridge(JFrame targetJframe) {
		this.targetJFrame = targetJframe;
		fileChooser = new JFileChooser();
		imageFilter = new FileNameExtensionFilter("Kuvatiedostot", ImageIO.getReaderFileSuffixes());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
	}

	@Override
	public void doImageSelect() {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				fileChooser.resetChoosableFileFilters();
				fileChooser.addChoosableFileFilter(imageFilter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if(fileChooser.showOpenDialog(targetJFrame) == JFileChooser.APPROVE_OPTION){
					fileChooser.requestFocus();
					File f = fileChooser.getSelectedFile();
					selectedImage = new FileHandle(f);
				} else {
					selectedImage = null;
				}
			}
		});
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
