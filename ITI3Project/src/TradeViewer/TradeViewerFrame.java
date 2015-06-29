package TradeViewer;

/*	
 *	modified by Dobromir Dobrev - 1103606 
 *  in order to add the selector panel and table view,
 *  remove the redundant list panel and record panel
 *  and improve the layout
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,dataPanel, tablePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);

        //Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 200);
        dataPanel.setMinimumSize(minimumSize);
        tablePanel.setMinimumSize(minimumSize);

        // prep component layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
       // contentPane.add("North",dataPanel);
        contentPane.add("Center", splitPane);

        final int DEFAULT_FRAME_WIDTH = 900;
        final int DEFAULT_FRAME_HEIGHT = 700;

        setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
        setTitle("Trade Viewer");
        setVisible(true);

    }
    
    

    private class WindowCloser extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
        System.out.println(	event.getSource().toString());
            System.exit(0);
        }
        
    }
    
} /* end TradeViewerFrame */
