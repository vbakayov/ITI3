package TradeViewer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public  class MessageInfo {

	private static JFrame frame;

	
	public  MessageInfo(JFrame frame){
		this.frame = frame;
	}
	
	
	public static void processMessage(int messageType,String message){
		//JOptionPane.showMessageDialog(frame, message);
		JOptionPane pane = new JOptionPane();
		pane.setMessage(message);
		pane.setMessageType(messageType);
		JDialog dialog = pane.createDialog("MESSAGE");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
	
	
}
