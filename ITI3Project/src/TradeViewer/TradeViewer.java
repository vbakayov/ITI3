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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;



public class TradeViewer {
	
	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, 
												  IllegalAccessException, UnsupportedLookAndFeelException {
	
	// the user is most used to selecting which file to open with a dialog box 
	// that has the system native Look And Feel - and so the file chooser
	// dialog box uses the native Look And Feel of his/her system.
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	
		
	// file selection is enabled via a JFileChooser component 
	JFileChooser fc = new JFileChooser();
	
	// set a file type filter 
	FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV files", "csv");
	fc.setFileFilter(csvFilter);
	
	// open the dialog box
	int returned = fc.showOpenDialog(fc);
	
	// create the TradeViewerFrame
	TradeViewerFrame frame;
	if (returned == JFileChooser.APPROVE_OPTION) {
		// switch to the Web Look And Feel

		frame = new TradeViewerFrame(fc.getSelectedFile().toString());
	}	
	else System.exit(0); // exit program if the file chooser dialog box for is closed
	
	}
	
}






