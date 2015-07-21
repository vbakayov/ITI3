package TradeViewer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

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
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alee.laf.WebLookAndFeel;
import java.lang.reflect.InvocationTargetException;

import javax.swing.filechooser.FileFilter;

public class TradeViewer {

	public static void main(String args[]) throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, UnsupportedLookAndFeelException {

		// the user is most used to selecting which file to open with a dialog box 
		// that has the system native Look And Feel - and so the file chooser
		// dialog box uses the native Look And Feel of his/her system.

		//SplashDemo splashes = new SplashDemo();
		System.out.println(" I AM HEREEEEEEE");
		
		
		if (splash_loading() != true)
			WebLookAndFeel.install ();
	            
	        
		System.out.println(" and heree");
	
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {

					
					// file selection is enabled via a JFileChooser component 
					JFileChooser fc = new JFileChooser();
					FileFilter xlsFilter = new FileTypeFilter(".xlsm", "Microsoft Excel Documents");
					fc.addChoosableFileFilter(xlsFilter);


					// open the dialog box
					boolean firstTime=true;
					String fileType= "";
					// create the TradeViewerFrame
					TradeViewerFrame frame;
					System.out.println(" and hereeeeeeeeee");
					//System.out.println(" I AM HEREEEEEEEeeeeeeeeeeeeeeee");
					while(!fileType.equals( ".xlsm" )|| firstTime){
						System.out.println(fileType.equals(".xlsm")  + "  "+ firstTime);
						firstTime = false;
						int returned = fc.showOpenDialog(fc);
						if (returned == JFileChooser.APPROVE_OPTION) {
							String filePath = fc.getSelectedFile().toString();
							fileType = filePath.substring(filePath.lastIndexOf('.'), filePath.length());


							if(fileType.equals(".xlsm")){
								System.out.println("Start loading");
								frame = new TradeViewerFrame(filePath);
								System.out.println("F");
							}
							else{
								MessageInfo.processMessage(JOptionPane.WARNING_MESSAGE,"The Inputed format is not supported.\n"+
										" The file is not with .xlsm extension!" );
							}
						}	
						else System.exit(0); // exit program if the file chooser dialog box for is closed

					}
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}};
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
		private static  boolean splash_loading(){
			   final SplashScreen splash = SplashScreen.getSplashScreen();
		        if (splash == null) {
		            System.out.println("SplashScreen.getSplashScreen() returned null");
		            return false;
		        }
		        Graphics2D g = splash.createGraphics();
		        if (g == null) {
		            System.out.println("g is null");
		            return false;
		        }
		        for(int i=0; i<10; i++) {
		           renderSplashFrame(g, i);
		           splash.update();
		           if(i==0)   WebLookAndFeel.install ();  
		          
		        }
		        
		        splash.close();
		        return true;
		            
		}
		  static void renderSplashFrame(Graphics2D g, int frame) {
		        final String[] comps = {"Web Look And Feel", "Swing Components"};
		        g.setComposite(AlphaComposite.Clear);
		        g.fillRect(120,140,200,40);
		        g.setPaintMode();
		        g.setColor(Color.BLACK);
		        g.drawString("Loading "+comps[(frame/4)%2]+"...", 120, 150);}

}
