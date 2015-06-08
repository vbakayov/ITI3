package TradeViewer;

/*	ScatterplotView.java
 * 
 *	Scatterplot for displaying financial data.
 *
 *	Modified by Dobromir Dobrev - 1103606 
 *  in order to allow panning of the plot, filtering,
 *  enable drawing the plot with multiple selected items
 *  and make some simple cosmetic changes.
 */

// Note: the color of the X and Y axes in the scatterplot has been changed
// 		 from blue to black, since (subjectively) black matches better the
//		 overall Look And Feel of the program's interface. For the same reason, 
//		 the color of a selected point has been changed from green to blue.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;

class ScatterplotView extends JPanel {

    private Point axisOrigin;
    private int width, height;
    private double xScale, yScale;
    private double cacheX[], cacheY[];
    // used for filtering the points
    private ArrayList<Integer> available;
    // used for multiple selection of points
    private ArrayList<Integer> selected;
    private Model model;
	
	// while this boolean is set to true, the method paint() will not set
    // the axis origin to the center of the panel when it is executed
	private boolean customAxisOrigin;

    public ScatterplotView(Model model, double scale) {
        setSize(new Dimension(100, 100));
        // initialise origin to midpoint of panel
        axisOrigin = new Point();
        // initialise scales
        xScale = scale;
        yScale = scale;
        // initialise data
        this.model = model;
        int dataSize = model.dataSize();
        cacheX = new double[dataSize];
        cacheY = new double[dataSize];
		customAxisOrigin = false;
        loadData(0, 1);
        available = model.getAvailableRows();
        selected = new ArrayList<Integer>();
        addMouseListener(new MouseButtonHandler());
    }

    class MouseButtonHandler extends MouseAdapter {

        public void mouseReleased(MouseEvent me) {
            int row = dotSelected(new Point(me.getX(), me.getY()));
            if (row >= 0) {
            	// since only one item is intended to be selected, this method calls
            	// the selection method using an ArrayList that contains a single item
            	ArrayList<Integer> al = new ArrayList();
            	al.add(row);
            	model.select(al);
            }
        }
    }

    int dotSelected(Point p) {
        int found = -1;
        int max = model.dataSize();
        int i = 0;
        while ((i < max) && (found == -1)) {
            if ((Math.abs(p.getX() - (axisOrigin.getX()
                    + (int) (cacheX[i] * xScale))) <= 2)
                    & (Math.abs(p.getY() - (axisOrigin.getY()
                    - (int) (cacheY[i] * yScale))) <= 2)) {
                found = i;
            } else {
                i++;
            }
        }
        return found;
    } // dotSelected
    
	// to enable panning, this method has been modified so that it sets
    // the axis origin point to the center of the panel only when it is
    // first executed, instead of doing it every time it is executed
    //
    // the color of the axis lines has been changed from blue to black
    // and the color of a selected point has been changed from green to blue,
    // in order for better matching with the program's Look and Feel
    //
    // lastly, the method now supports multiple selection and filtering
    public void paint(Graphics g) {
        double x, y;
        // get panel dimensions
        height = getSize().height;
        width = getSize().width;
        // fix nasty redraw bug
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, width, height);
        // set axis origin        
        if (!customAxisOrigin) {  	
            axisOrigin.setX(width/2);
            axisOrigin.setY(height/2);
            customAxisOrigin = true; }
        // draw the axes
        g.setColor(Color.black);
        g.drawLine(0, axisOrigin.getY(), width, axisOrigin.getY());
        g.drawLine(axisOrigin.getX(), 0, axisOrigin.getX(), height);
        // draw all the elements except the selected one
		for (int j = 0; j < available.size(); j++) {
			int i = available.get(j);
            if (!selected.contains(i)) {
                g.setColor(Color.red);
                g.fillOval(axisOrigin.getX() + (int) (cacheX[i] * xScale) - 2,
                        axisOrigin.getY() - (int) (cacheY[i] * yScale) - 2,
                        4, 4);
            }
        }
        // do selected items last so that they are not obscured
        Color c = new Color(89, 145, 236);
        for (int j = 0; j < selected.size(); j++) {
			int i = selected.get(j);
        	if (available.contains(i)) {
	            g.setColor(c);
	            g.fillOval(axisOrigin.getX() + (int) (cacheX[i] * xScale) - 2,
	                    axisOrigin.getY() - (int) (cacheY[i] * yScale) - 2,
	                    4, 4);
        	}
        }
    }

    void loadData(int xAxisField, int yAxisField) {
        // construct a scatterplot mapping data in field with index xAxisField
        // to the x axis and data in field yAxisField to the y axis
        for (int i = 0; i < model.dataSize(); i++) {
            cacheX[i] = model.fieldAsReal(i, xAxisField);
            cacheY[i] = model.fieldAsReal(i, yAxisField);
        }
        repaint();
    }


    // updating methods
    public void itemSelected(ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows) {
    	available = availableRows;
        selected = selectedRows;
        repaint();
    }
    
    public void update(ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows) {
    	available = availableRows;
        selected = selectedRows;
        repaint();
    }

    public void setXScale(double val) {
        xScale = val;
        repaint();
    }

    public void setYScale(double val) {
        yScale = val;
        repaint();
    }
    
    // gets the axis origin point   
	public Point getAxisOrigin() {
		return axisOrigin;
	}
	
	// sets the axis origin point and repaints the scatterplot
	public void setAxisOrigin(Point axisOrigin) {
		axisOrigin = axisOrigin;
		repaint();
	}
    
} // end ScatterplotPanel
