package TradeViewer;

/*	
 *	modified by Dobromir Dobrev - 1103606 
 *  in order to add the selector panel and table view,
 *  remove the redundant list panel and record panel
 *  and improve the layout
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import org.jfree.ui.RefineryUtilities;

class TradeViewerFrame extends JFrame {

    private ScatterplotPanel scatterplotPanel;
    private TablePanel tablePanel;
    private SelectorPanel selectorPanel;
    private StatisticsPanel statisticsPanel;
    private Model model;
    

    public TradeViewerFrame(String filename) {
    	
        model = new Model(filename);
        addWindowListener(new WindowCloser());
        

        // add the main Swing components
    //    scatterplotPanel = new ScatterplotPanel(model);
    //    model.addChild(scatterplotPanel);
        statisticsPanel=new StatisticsPanel(model);
        statisticsPanel.setLayout(new GridLayout(1,2));
        JButton countryButton = new JButton("Country Chart");
        JButton ageButton=new JButton("Age Chart");
        countryButton.setPreferredSize(new Dimension(10, 10));
        statisticsPanel.add(countryButton);
        statisticsPanel.add(ageButton);
        countryButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPressed();
				setEnabled(false);
				
			}

			private void buttonPressed() {
				PieChart demo = new PieChart("Pie Chart",statisticsPanel.countryCountMap);
				setLayout(new BorderLayout());
			    // add("Center", demo); 
				demo.pack();
				RefineryUtilities.centerFrameOnScreen(demo);
				demo.setVisible(true);
				
			}
	
		});
        
    	
        model.addChild(statisticsPanel);
        tablePanel = new TablePanel(model);
        model.addChild(tablePanel);
        selectorPanel = new SelectorPanel(model);
        model.addChild(selectorPanel);
        
        // contains the record panel and the selector panel 
        JPanel dataPanel = new JPanel(new GridLayout(1,2));
        dataPanel.add(statisticsPanel);
        dataPanel.add(selectorPanel);
        // the border is added so that the edges of the 
        // components do not cover the scatterplot
        dataPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#F0F0F0"), 4));

        // prep component layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add("North",dataPanel);
        contentPane.add("Center", tablePanel);

        final int DEFAULT_FRAME_WIDTH = 900;
        final int DEFAULT_FRAME_HEIGHT = 700;

        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setTitle("Trade Viewer");
        setVisible(true);

    }
    
    

    private class WindowCloser extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
            System.exit(0);
        }
        
    }
    
} /* end TradeViewerFrame */
