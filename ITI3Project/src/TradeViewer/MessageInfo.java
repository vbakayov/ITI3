package TradeViewer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public  class MessageInfo {

	private static JFrame frame;


	public  MessageInfo(JFrame frame){
		this.frame = frame;
	}
	
	
	public static void processMessage(String message){
		//JOptionPane.showMessageDialog(frame, message);
		JOptionPane pane = new JOptionPane();
		pane.setMessage(message);
		JDialog dialog = pane.createDialog("MESSAGE");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
	
	
}
