package TradeViewer;

/*	
 *	modified by Dobromir Dobrev - 1103606 
 *  in order to add the selector panel and table view,
 *  remove the redundant list panel and record panel
 *  and improve the layout
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import org.jfree.ui.RefineryUtilities;

class TradeViewerFrame extends JFrame {

    private ScatterplotPanel scatterplotPanel;
    private TablePanel tablePanel;
    private SelectorPanel selectorPanel;
    private LogoStuff logoStuff;
    private StatisticsPanel statisticsPanel;
    private Model model;
	private Window jframeRef;
    

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
        logoStuff = new LogoStuff(model);
        model.addChild(logoStuff);
        
        // contains the record panel and the selector panel 
//        JPanel dataPanel = new JPanel(new GridLayout(1,2));
//        
//       
//		 dataPanel.add(statisticsPanel);
//		 dataPanel.add(logoStuff);
//	     dataPanel.add(selectorPanel);
//        // the border is added so that the edges of the 
//        // components do not cover the scatterplot
//        dataPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#F0F0F0"), 4));
//        
//        
       
        
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,statisticsPanel ,logoStuff);
        splitPane1.setOneTouchExpandable(true);
        splitPane1.setDividerLocation(300);
        
        Dimension minimumSize2 = new Dimension(200, 200);
        statisticsPanel.setMinimumSize(minimumSize2);
        logoStuff.setMinimumSize(minimumSize2);
        
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,splitPane1 ,selectorPanel);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setDividerLocation(700);
        selectorPanel.setMinimumSize(minimumSize2);
        splitPane1.setMinimumSize(minimumSize2);

 


        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,splitPane2, tablePanel);
        splitPane.setOneTouchExpandable(true);
        
        
        Dimension maximumSize = new Dimension(400, 500);
        
        splitPane2.setMinimumSize(minimumSize2);
        logoStuff.setMinimumSize(minimumSize2);
        
      
      
      
       
//        contentPane2.setMinimumSize(minimumSize);
//        tablePanel.setMinimumSize(minimumSize);
        
       // contentPane.add(contentPane2);
       add( splitPane);
        
        
        
        Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        setSize(dim.width-350,dim.height-200);
     
        setTitle("Legal Services Agency Viewer");
        //Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);

    }
    
    

    private class WindowCloser extends WindowAdapter {

        public void windowClosing(WindowEvent event) {
        System.out.println(	event.getSource().toString());
            System.exit(0);
        }
        
    }
    
} /* end TradeViewerFrame */
