package TradeViewer;

/**	TablePanel.java
 * 
 *  Provides a table view.
 *	
 *	Note that this class replaces ListPanel.java 
 *  and RecordPanel.java from the original package.
 *  
 *  The custom table renderer that enables drawing a bar chart
 *  is reused - the source of classes BarChartCellRenderer.java, 
 *  BarChartIcon.java, BarChartMain.java and TableSorter.java is:
 *  http://www.jroller.com/Thierry/entry/swing_barchart_rendering_in_a
 *  
 *  Only two modification habe been made to the original code:
 *  
 *  =>	The method setToolTipText(String) has been added in the first of those
 *  	classes, in order to provide tool tips for the cells in the table.
 *  =>  The default color of the bars in the same class has been altered so that 
 *      it fits better the overall Look And Feel of the program.
 *  
 *  
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class TablePanel extends JPanel
				 implements ViewController {
	
	private Model model;
    // used for filtering the points
    private ArrayList<Integer> available;
    // used for multiple selection of points
    private ArrayList<Integer> selected;
    
	// a table to display the information from the dataset records
	private JTable recordTable;
	// data model
	private RecordTableModel recordDataModel;
	// selection model
	private ListSelectionModel recordSelectionModel;
	// used to prevent automatic row selection from triggering updates
	private boolean ready;
	// a slider to edit the row height
	private JSlider rowHeightSlider;
	
	private  JScrollPane scrollPane ;
	

	
	public TablePanel(Model m) {
		
		this.model = m;
		this.available = model.getAvailableRows(); 
		this.selected = new ArrayList<Integer>();
		this.ready = true;

		// initialize the table data model
		recordDataModel = new RecordTableModel();
		recordDataModel.load();
		// create table
		recordTable = new JTable(recordDataModel){
		    //Implement table cell tool tips.           
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            
        };
		};
		
		 ToolTipHeader header = new ToolTipHeader(recordTable.getColumnModel());
		 Object[] myArray =model.getLabel().toArray(new String[model.getLabel().size()]);
		 String[] stringArray = Arrays.copyOf(myArray, myArray.length, String[].class);
		    header.setToolTipStrings( stringArray);
		    //header.setToolTipText("Default ToolTip TEXT");
		    recordTable.setTableHeader(header);
		//recordTable.setPreferredScrollableViewportSize(new Dimension(500, 150));
		// enable multiple selection 
		recordTable.setRowSelectionAllowed(true);
		
		recordTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		ListSelectionModel selection= recordTable.getSelectionModel();
		selection.addListSelectionListener(new RecordTableListener());
		recordTable.setAutoCreateRowSorter(false);
	//	 TableRowSorter trs = new TableRowSorter(recordTable);
		
		
		// set selection model
		recordSelectionModel = recordTable.getSelectionModel();
		recordSelectionModel.addListSelectionListener(new RecordTableListener());
		recordTable.setSelectionModel(recordSelectionModel);	
		// set custom renderer
		for (int i = 0; i < recordTable.getColumnCount(); i++)
			recordTable.setDefaultRenderer(Double.class, new BarChartCellRenderer());
		// disable auto resizing so that the columns do not become very shrunk
		recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// enable row sorting
//		 TableRowSorter trs = new TableRowSorter(recordDataModel);
//		 trs.setComparator(2, new IntComparator());
//		 recordTable.setRowSorter(trs);
		 model.setTable(recordTable);
	
	//	recordTable.setAutoCreateRowSorter(true);


		
		// create a slider to edit the row height
        rowHeightSlider = new JSlider();
        rowHeightSlider.setMinimum(-50);
        rowHeightSlider.setMaximum(-1);
        rowHeightSlider.setValue(-20);
        rowHeightSlider.setOrientation(JSlider.VERTICAL );
        rowHeightSlider.addChangeListener(new RowHeightSliderListener ());
		
	    // add the table to a scroll pane
	     scrollPane = new JScrollPane(recordTable);
	    setLayout(new BorderLayout());
	    add("Center", scrollPane);
	    add("West", rowHeightSlider);
	   
	}
	
	// Inner class for the table model.
	private class RecordTableModel extends AbstractTableModel {
		
		
//	    private String[] columnNames = {"Service User ID", "Gender","Age", "Age Group","Coutry of Origin","Ethnicity",
//	    		"Immigration","Asylum","NRM","ILR","ILR DV","EU","Housing","FMPO","Age Assessment",
//	    		"Fresh Claim","JR","Family Reunion", "Appeal","Community Care", "HPDL",
//	    		"CICA", "Nationality", "Welfare/Support", "Other", "HR", 
//	    		"Human Trafficking for purposes of Commercial Sexual Exploitation",
//	    		"Human Trafficking for other Purposes","FGM","Forced Marriage","Domestic Abuse","Violence in the UK","Asylum Application","Asylum Appeal"};
		
		private ArrayList columnNames = model.getLabel();
	    private Object[][] data;
	    
	
	    // override getColumnClass to return our chosen class type - 
	    // getColumnClass has to return Double in order to create a bar chart
	    
	    
        public Class<?> getColumnClass(int column) {
        	//System.out.println("Index is"+ column);
        	if (column ==2)
        		return java.lang.Double.class;
	        Class<?> returnValue;
	        if ((column >= 0) && (column < getColumnCount())) {
	          returnValue = getValueAt(0, column).getClass();
	        } else {
	          returnValue = Object.class;
	        }
	        return returnValue;
	      }
	    // fills the data array with the records that pass the currently set filters
	    public void load() {
	    	//System.out.println(model.getLabel().size());
	    	ArrayList labals = model.getLabel();
	    	data = new Object[available.size()][];
	    	for (int i = 0; i < available.size(); i++) {
	    		// add a record from the dataset to the data array
	    		Object[] record = model.record(available.get(i)).toArray();
	    		data[i] = record;
	    	}
	    }
	    // accessing methods
	    public int getColumnCount() {
	        return columnNames.size();
	    }
	    public int getRowCount() {
	        return data.length;
	    }
	    public String getColumnName(int col) {
	    	//System.out.println(col);
	    	
	        return columnNames.get(col).toString();
	    }
	    public Object getValueAt(int row, int col) {
	    	//Here row and col are view indeces and data is view representaion
	    	//System.out.println("ROW+ COL : "+row + "  "+ col +" ");
	    	//System.out.println(data[row][col]);
	    	return data[row][col];
	    }
	    public void setValue(Object value, int row, int col) {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }    
	}
	
	// inner class that handles selection events from the table
	private class RecordTableListener implements ListSelectionListener {
	//	public void valueChanged(ListSelectionEvent event) {
			// this part will not execute if the event is triggered
			// by automatic selection from the update method
			
		public void valueChanged(ListSelectionEvent e) {
			if (ready){
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            
 
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting(); 
           System.out.println("Event for indexes "
                          + firstIndex + " - " + lastIndex
                          + "; isAdjusting is " + isAdjusting
                          + "; selected indexes:");
           
 
            if (lsm.isSelectionEmpty()) {
                System.out.println(" ");
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                       selected.add(model.getTable().convertRowIndexToModel(i));
                       System.out.println(" " + i + "SIZE"+ selected.size());
                       
                    }
                }
                model.select(selected);
               
            }
		}
        }
        }
	
	// inner class that handles events from the X and Y scale sliders
    private class RowHeightSliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent event) {
        	recordTable.setRowHeight(-rowHeightSlider.getValue());
        }
    }
	
	// updating method
	public void update(ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows) {
	    available = availableRows;
	    selected = selectedRows;
	    recordDataModel.load();
	    // disable the table selection events listener for the period when the
	    // row selection intervals are set, so that it is not triggered by them
		ready = false;
		for (int i = 0; i < selected.size(); i++)
			if (available.contains(selected.get(i))) {
				try {recordTable.addRowSelectionInterval(selected.get(i), selected.get(i));}
				catch (IllegalArgumentException e) {}
			}	
		ready = true;
		 scrollPane.getViewport().revalidate();
		recordTable.repaint();
	}
	
	
	class IntComparator implements Comparator {
        public int compare(Object o1, Object o2) {
        	
            Integer int1 = (Integer)o1;
            Integer int2 = (Integer)o2;
            System.out.println("COMPARE"+ int1 + " "+ int2);
            return int1.compareTo(int2);
        }

        public boolean equals(Object o2) {
            return this.equals(o2);
        }
    }

	@Override
	public void notify(boolean delete) {
		// TODO Auto-generated method stub
		
	}
	
	class ToolTipHeader extends JTableHeader {
	    String[] toolTips;

	    public ToolTipHeader(TableColumnModel model) {
	      super(model);
	    }

	    public String getToolTipText(MouseEvent e) {
	      int col = columnAtPoint(e.getPoint());
	      int modelCol = getTable().convertColumnIndexToModel(col);
	      String retStr;
	      try {
	        retStr = toolTips[modelCol];
	      } catch (NullPointerException ex) {
	        retStr = "";
	      } catch (ArrayIndexOutOfBoundsException ex) {
	        retStr = "";
	      }
	      if (retStr.length() < 1) {
	        retStr = super.getToolTipText(e);
	      }
	      return retStr;
	    }

	    public void setToolTipStrings(String[] toolTips) {
	      this.toolTips = toolTips;
	    }
	  }
	

}