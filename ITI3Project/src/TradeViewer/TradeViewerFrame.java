package TradeViewer;



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
        
        
        statisticsPanel=new StatisticsPanel(model);
        model.addChild(statisticsPanel);
        tablePanel = new TablePanel(model);
        model.addChild(tablePanel);
        selectorPanel = new SelectorPanel(model);
        model.addChild(selectorPanel);
        logoStuff = new LogoStuff(model);
        model.addChild(logoStuff);
        
  
       
        Dimension minimumSize2 = new Dimension(180, 180);
        
        JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,statisticsPanel ,logoStuff);
        splitPane1.setOneTouchExpandable(true);
      
        splitPane1.setMinimumSize(minimumSize2);
        
       
   
        
        
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,splitPane1 ,selectorPanel);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setResizeWeight(1);
        splitPane2.setMaximumSize(new Dimension(400,400));
 
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,splitPane2, tablePanel);
        splitPane.setOneTouchExpandable(true);
        
        
  
        
       add( splitPane);
        
        
        
        Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        setSize(dim.width-350,dim.height-200);
        System.out.println("BLa- BANANAA" +getSize().toString());
        splitPane1.setDividerLocation(getWidth()/4);
        splitPane2.setDividerLocation(getWidth()-(getWidth()/3));
        setTitle("Legal Services Agency Viewer - " +filename.substring(filename.lastIndexOf('\\')+1, filename.length()));
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
