package TradeViewer;

/*	TradeViewer.java - a wrapper for TradeViewerFrame 
 *  
 *	modified by Dobromir Dobrev - 1103606 
 *  in order to enable file selection
 *  and change the Look And Feel
 */

// The default Swing look and feel is not very good looking
// and so the program uses the following open-source one -
// http://code.google.com/p/weblookandfeel/

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alee.laf.WebLookAndFeel;

import javax.swing.filechooser.FileFilter;

public class TradeViewer {

	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, 
												  IllegalAccessException, UnsupportedLookAndFeelException {
	
	// the user is most used to selecting which file to open with a dialog box 
	// that has the system native Look And Feel - and so the file chooser
	// dialog box uses the native Look And Feel of his/her system.
	//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
//		try {
//		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//		        if ("Nimbus".equals(info.getName())) {
//		            UIManager.setLookAndFeel(info.getClassName());
//		            break;
//		        }
//		    }
//		} catch (Exception e) {
//		    // If Nimbus is not available, you can set the GUI to another look and feel.
//		}
	
	WebLookAndFeel.install ();
		// file selection is enabled via a JFileChooser component 
	JFileChooser fc = new JFileChooser();
	FileFilter xlsFilter = new FileTypeFilter(".xlsm", "Microsoft Excel Documents");
	fc.addChoosableFileFilter(xlsFilter);
	
	
	// open the dialog box
	boolean firstTime=true;
	 String fileType= "";
	// create the TradeViewerFrame
	TradeViewerFrame frame;
	while(!fileType.equals( ".xlsm" )|| firstTime){
		System.out.println(fileType.equals(".xlsm")  + "  "+ firstTime);
		firstTime = false;
		int returned = fc.showOpenDialog(fc);
		if (returned == JFileChooser.APPROVE_OPTION) {
		String filePath = fc.getSelectedFile().toString();
		fileType = filePath.substring(filePath.lastIndexOf('.'), filePath.length());
	
		
		if(fileType.equals(".xlsm")){
			frame = new TradeViewerFrame(filePath);
			}
		else{
			MessageInfo.processMessage(JOptionPane.WARNING_MESSAGE,"The Inputed format is not supported.\n"+
								 	" The file is not with .xlsm extension!" );
			}
		}	
	else System.exit(0); // exit program if the file chooser dialog box for is closed
	
	}
	}

	static class FileTypeFilter extends FileFilter {
		private String extension;
		private String description;

		public FileTypeFilter(String extension, String description) {
			this.extension = extension;
			this.description = description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			return file.getName().endsWith(extension);
		}

		public String getDescription() {
			return description + String.format(" (*%s)", extension);
		}

	}
}
