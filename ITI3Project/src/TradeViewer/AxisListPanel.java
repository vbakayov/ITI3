package TradeViewer;

/*	AxisListPanel.java
 *
 *	Provides tools for selecting the axes.
 *
 *	Modified by Dobromir Dobrev - 1103606 
 *  in order to improve axis selection.
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class AxisListPanel extends JPanel {
	private ScatterplotPanel parent;
	private JComboBox xAxisList, yAxisList;
	private JLabel xAxisLabel, yAxisLabel;

	public AxisListPanel(ScatterplotPanel p, ArrayList v) {
		  
		  // create combo boxes to select the axes for X and Y
		  xAxisList = new JComboBox(v.toArray());
		  xAxisList.setFont(new Font("Arial", Font.PLAIN, 14));
		  xAxisList.setSelectedIndex(0);
		  xAxisList.addActionListener(new AxisListListener());
		  
		  yAxisList = new JComboBox(v.toArray());
		  yAxisList.setFont(new Font("Arial", Font.PLAIN, 14));
		  yAxisList.setSelectedIndex(1);
		  yAxisList.addActionListener(new AxisListListener());
		  
		  // create descriptive labels for the combo boxes:
		  xAxisLabel = new JLabel("X-axis Attribute:");
		  xAxisLabel.setHorizontalAlignment(JLabel.CENTER);
		  xAxisLabel.setVerticalAlignment(JLabel.CENTER);
		  xAxisLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			
		  yAxisLabel = new JLabel("Y-axis Attribute:");
		  yAxisLabel.setHorizontalAlignment(JLabel.CENTER);
		  yAxisLabel.setVerticalAlignment(JLabel.CENTER);
		  yAxisLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		  
		  // add the combo boxes and their labels to the parent panel
		  parent = p;
		  setLayout(new GridLayout(2,2,2,0));
		  
		  add(xAxisLabel);
		  add(yAxisLabel);
		  add(xAxisList);
		  add(yAxisList);
		
	}

	// inner class that handles events from the combo boxes
	private class AxisListListener implements ActionListener {
		
		public void actionPerformed(ActionEvent event) {
			JComboBox source = (JComboBox) event.getSource();
			String item = (String) source.getSelectedItem();
			
			if (source.equals(xAxisList)) parent.updateXAxis (item);
			else parent.updateYAxis (item);

		}
		
	}

}
