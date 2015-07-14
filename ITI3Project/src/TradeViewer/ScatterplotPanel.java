package TradeViewer;

/*	ScatterplotPanel.java
 * 
 *	Instrumented scatterplot for displaying financial data.
 * 
 *	Modified by Dobromir Dobrev - 1103606 
 *  in order to improve the scaling controls. 
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class ScatterplotPanel extends JPanel 
					   implements ViewController {

	private Model model;
    private ScatterplotView scatterplotView;
    private int xAxisIndex = 0, yAxisIndex = 1;
    private JSlider xScaleSlider, yScaleSlider;
    private JLabel xScaleLabel, yScaleLabel;
    private JButton panLeftButton, panRightButton, panUpButton, panDownButton;

    public ScatterplotPanel(Model m) {
    		
    	this.model = m;
        double scale = 5.0;	// initial scale
        
        // create sliders to edit the scales for X and Y
        xScaleSlider = new JSlider();
        xScaleSlider.setMinimum(0);
        xScaleSlider.setMaximum(40);
        xScaleSlider.setValue((int) scale);
        xScaleSlider.addChangeListener(new ScaleSliderListener ());
        
        yScaleSlider = new JSlider();
        yScaleSlider.setMinimum(0);
        yScaleSlider.setMaximum(40);
        yScaleSlider.setValue((int) scale);
        yScaleSlider.addChangeListener(new ScaleSliderListener ());

        // add ticks
        xScaleSlider.setMajorTickSpacing(5);
        xScaleSlider.setMinorTickSpacing(1);
        xScaleSlider.setSnapToTicks(true);
        xScaleSlider.setPaintTicks(true);
        
        yScaleSlider.setMajorTickSpacing(5);
        yScaleSlider.setMinorTickSpacing(1);
        yScaleSlider.setSnapToTicks(true);
        yScaleSlider.setPaintTicks(true);
        
        // create labels displaying the current values of the sliders
        xScaleLabel = new JLabel("X-axis Scale:  " + xScaleSlider.getValue());
        xScaleLabel.setHorizontalAlignment(JLabel.CENTER);
        xScaleLabel.setVerticalAlignment(JLabel.CENTER);
        xScaleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        yScaleLabel = new JLabel("Y-axis Scale:  " + yScaleSlider.getValue());
        yScaleLabel.setHorizontalAlignment(JLabel.CENTER);
        yScaleLabel.setVerticalAlignment(JLabel.CENTER);
        yScaleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // create buttons to pan left, right, up and down    
        panLeftButton = new JButton("Pan Left");
        panLeftButton.setFont(new Font("Arial", Font.PLAIN, 14));
        panLeftButton.addActionListener(new PanningButtonListener ());
        
        panRightButton = new JButton("Pan Right");
        panRightButton.setFont(new Font("Arial", Font.PLAIN, 14));
        panRightButton.addActionListener(new PanningButtonListener ());
        
        panUpButton = new JButton("Pan Up");
        panUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        panUpButton.addActionListener(new PanningButtonListener ());
        
        panDownButton = new JButton("Pan Down");
        panDownButton.setFont(new Font("Arial", Font.PLAIN, 14));
        panDownButton.addActionListener(new PanningButtonListener ());
        
        // create a panel that contains the sliders with their respective
        // labels as well as the buttons for panning left, right, up and down
        JPanel scalePanel = new JPanel(new GridLayout(2,4,1,0));
        // first row of the grid 
        scalePanel.add(xScaleLabel);
        scalePanel.add(panLeftButton);
        scalePanel.add(yScaleLabel);
        scalePanel.add(panUpButton);
        // second row of the grid        
        scalePanel.add(xScaleSlider);
        scalePanel.add(panRightButton);
        scalePanel.add(yScaleSlider);
        scalePanel.add(panDownButton);
        
        // create the 2D view and load it with data 
        scatterplotView = new ScatterplotView(model, scale);

        // Bring order to chaos: use a sub panel and border layout
        // managers to place the different subcomponents of the
        // scatterplotPanel into specific positions
        JPanel p = new JPanel(new BorderLayout());
        p.add("East", new AxisListPanel(this, model.numericFieldLabels()));
        p.add ("Center", scalePanel);
        
        // the border is added so that the edges of the 
        // components do not cover the scatterplot
        p.setBorder(BorderFactory.createLineBorder(Color.decode("#F0F0F0"), 2));
        
        setLayout(new BorderLayout());
        add("Center", scatterplotView); 
        add("North", p);
        
    }

	// inner class that handles events from the X and Y scale sliders
    private class ScaleSliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent event) {
        	JSlider source = (JSlider) event.getSource() ;
        	
        	if (source.equals(xScaleSlider)) {
        		scatterplotView.setXScale(source.getValue());
        		xScaleLabel.setText("X-axis Scale:  " + source.getValue());
        	}
        	else {
                scatterplotView.setYScale(source.getValue());
                yScaleLabel.setText("Y-axis Scale:  " + source.getValue());
            }
        }
    }
    
   // inner class that handles events from the panning buttons
   private class PanningButtonListener implements ActionListener {

       public void actionPerformed(ActionEvent event) {
    	   JButton source = (JButton) event.getSource();
    	
    	   Point axisOrigin = scatterplotView.getAxisOrigin();
           int step = 25; 
           
           if (source.equals(panLeftButton)) 
        	   axisOrigin.setX(axisOrigin.getX() - step);
           else if (source.equals(panRightButton))
        	   axisOrigin.setX(axisOrigin.getX() + step);
           else if (source.equals(panUpButton))
        	   axisOrigin.setY(axisOrigin.getY() - step);
           else 
        	   axisOrigin.setY(axisOrigin.getY() + step);
           
           scatterplotView.setAxisOrigin(axisOrigin);
       }
       
   } 

   /* 
   Fits with the ViewController interface.
   Updates a single point in the scatterplot based on row-th 
   element of the source domain. 
   */
  public void update(ArrayList<Integer> availableRows, ArrayList<Integer>selectedRows) {
      // System.out.println("ScatterplotPanel.update " + row);
      scatterplotView.update(availableRows, selectedRows);  /* pass it down to the scatterplotView */
  }
    
    public void updateXAxis(String itemLabel) {
        // get index of the field called itemLabel from the model
        xAxisIndex = model.indexOfField(itemLabel);

        // update the scatterplot based on the x and y field indices
        scatterplotView.loadData(xAxisIndex, yAxisIndex);
    }

    public void updateYAxis(String itemLabel) {
        // get index of the field called itemLabel from the source domain
        yAxisIndex = model.indexOfField(itemLabel);

        // update the scatterplot based on the x and y field indices
        scatterplotView.loadData(xAxisIndex, yAxisIndex);
        
    }

	@Override
	public void notify(boolean delete) {
		// TODO Auto-generated method stub
		
	}
    
} // end ScatterplotPanel
