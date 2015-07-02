package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OutcomesPanel extends JFrame{

	private int[] outcomesCount;
	private JPanel topPanel;

	public OutcomesPanel(int[] outcomesCount) {
		this.outcomesCount= outcomesCount;
		// Set the frame characteristics
		setTitle( "Outcomes");
		  // here's the part where i center the jframe on screen
		setSize(400,400);
		setLocationRelativeTo(null);
		setBackground( Color.gray );
		GUI();
	}

	private void GUI() {
		setLayout( new GridLayout(8,2) );

		
		JLabel label2= new JLabel("Asylum- refugee status");
		JTextField text2 = new JTextField(Integer.toString(outcomesCount[0]));
		text2.setEnabled(false);
		add(label2);
		add(text2);
		JLabel label3= new JLabel("Asylum-refused");
		JTextField text3 = new JTextField(Integer.toString(outcomesCount[1]))	;
		text3.setEnabled(false);
		add(label3);
		add(text3);
		text3.setEnabled(false);
		JLabel label4= new JLabel("Appeal-refused");
		JTextField text4 = new JTextField(Integer.toString(outcomesCount[2]))	;
		add(label4);
		add(text4);
		text4.setEnabled(false);
		JLabel label5= new JLabel("Appeal-no appeal");
		JTextField text5 = new JTextField(Integer.toString(outcomesCount[3]))	;
		add(label5);
		add(text5);
		text5.setEnabled(false);
		JLabel label6= new JLabel("Appeal- outstanding");
		JTextField text6 = new JTextField(Integer.toString(outcomesCount[4]));
		add(label6);
		add(text6);
		text6.setEnabled(false);
		JLabel label7= new JLabel("Fresh Claim - no appeal");
		JTextField text7 = new JTextField(Integer.toString(outcomesCount[5]))	;
		add(label7);
		add(text7);
		text7.setEnabled(false);
		JLabel label8 = new JLabel("Fresh Claim - outstanding");
		JTextField text8 = new JTextField(Integer.toString(outcomesCount[6]))	;
		add(label8);
		add(text8);
		text8.setEnabled(false);
		
		//for(int i = 0 ; i< outcomesCount.length; i++){
			//JTextField name = "text"+i;
			// name.setText(Integer.toString(outcomesCount[i]));
		//}
		
		
		
		
		
//		JButton button2 = new JButton("Sometinggg");
//		add(button2);
//		JButton button3 = new JButton("Sometingggg");
//		add(button3);
//		JButton button4 = new JButton("Sometinggggg");
//		add(button4);
	}
	

}
