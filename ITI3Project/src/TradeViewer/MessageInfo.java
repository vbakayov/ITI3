package TradeViewer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public  class MessageInfo {

	private static JFrame frame;


	public  MessageInfo(JFrame frame){
		this.frame = frame;
	}
	
	
	public static void processMessage(String message){
		JOptionPane.showMessageDialog(frame, message);
	}
	
	
}
