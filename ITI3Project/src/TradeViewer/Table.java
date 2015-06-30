package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import TradeViewer.TablePanel.IntComparator;

public class Table extends JFrame {
	private	JPanel		topPanel;
	private	JTable		table;
	private	JScrollPane scrollPane;
	HashMap<String, Integer>casesMap;
	Model model;
	private int caseTotal;
	
	public Table(Model model, HashMap<String, Integer> casesMap, int caseTotal)
	{
		this.casesMap=casesMap;
		this.model=model;
		this.caseTotal=caseTotal;
		// Set the frame characteristics
		setTitle( "Legal Cases" );
		setSize(400,400 );
		  // here's the part where i center the jframe on screen
		setLocationRelativeTo(null);
		setBackground( Color.gray );
		initTable();

		
	}
	
	private String[][] convertHashMaptoDoubleArray(){
		
		String data[][] = new String [casesMap.size()][4];
		int i = 0;
		//System.out.println("Size of CasesMap: "+ casesMap.size());
		Iterator it = casesMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		data[i][0] = (String) pair.getKey();
    		data[i][1] =  pair.getValue().toString();
    		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
    		defaultFormat.setMinimumFractionDigits(2);
    		String fromDoubletoPer = defaultFormat.format(Double.parseDouble(pair.getValue().toString())/(model.getData().size()));
    		String totalCasesPer= defaultFormat.format(Double.parseDouble(pair.getValue().toString())/(caseTotal));
    		//System.out.println("TOTAL CASES: "+ caseTotal);
    		data[i][2]= fromDoubletoPer;
    		data[i][3]=totalCasesPer;
    		
    		
    		//System.out.println(pair.getKey() + " = " + pair.getValue());
    		i++;
    	}
		System.out.println("Data Size"+data.length);
		return data;
		
	}
	
	private void initTable(){
		// Create a panel to hold all other components
				topPanel = new JPanel();
				topPanel.setLayout( new BorderLayout() );
				getContentPane().add( topPanel );

				// Create columns names
				String columnNames[]={"Case Type","Count","Types of Cases/number of clients", "Types of Cases/total cases" };
						
				Object rows[][] = convertHashMaptoDoubleArray();
				
				TableModel model = new DefaultTableModel(rows, columnNames) {
				      public Class<?> getColumnClass(int column) {
				        Class<?> returnValue;
				        if ((column >= 0) && (column < getColumnCount())) {
				          returnValue = getValueAt(0, column).getClass();
				        } else {
				          returnValue = Object.class;
				        }
				        return returnValue;
				      }
				      
				      @Override
				      public boolean isCellEditable(int row, int column) {
				         //all cells false
				         return false;
				      }
				
				
				};
				      
				      
				      
				// Create a new table instance
				table = new JTable(model );
				
				RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
			    table.setRowSorter(sorter);
			
				// Add the table to a scrolling pane
				scrollPane = new JScrollPane( table );
				topPanel.add( scrollPane, BorderLayout.CENTER );
				CopyTable();
	}
	
	
	public void CopyTable(){
	//	table.add
          table.getActionMap().put("copy", new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
            	  
                  int[] row = table.getSelectedRows();
                  StringBuilder sb = new StringBuilder();
                  sb.append("<table border=1 width=100%>");
                  for(int roww= 0 ; roww<table.getSelectedRowCount(); roww++){
                	  
                 sb.append("<tr>");
                  for (int col = 0; col < table.getColumnCount(); col++) {
                      sb.append("<td>");
                      sb.append(table.getValueAt(row[roww], col).toString());
                      sb.append("</td>");
                    
                  }
                  sb.append("</tr>");
                  }
                  sb.append("</table>");
                  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                  clipboard.setContents(new HtmlSelection(sb.toString()), null);
              }
          });
          

	}

	 private static class HtmlSelection implements Transferable {

	        private static ArrayList htmlFlavors = new ArrayList();

	        static {

	            try {

	                htmlFlavors.add(new DataFlavor("text/html;class=java.lang.String"));

	                htmlFlavors.add(new DataFlavor("text/html;class=java.io.Reader"));

	                htmlFlavors.add(new DataFlavor("text/html;charset=unicode;class=java.io.InputStream"));

	            } catch (ClassNotFoundException ex) {

	                ex.printStackTrace();

	            }

	        }

	        private String html;

	        public HtmlSelection(String html) {

	            this.html = html;

	        }

	        public DataFlavor[] getTransferDataFlavors() {

	            return (DataFlavor[]) htmlFlavors.toArray(new DataFlavor[htmlFlavors.size()]);

	        }

	        public boolean isDataFlavorSupported(DataFlavor flavor) {

	            return htmlFlavors.contains(flavor);

	        }

	        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {

	            if (String.class.equals(flavor.getRepresentationClass())) {

	                return html;

	            } else if (Reader.class.equals(flavor.getRepresentationClass())) {

	                return new StringReader(html);

	            } else if (InputStream.class.equals(flavor.getRepresentationClass())) {

	                return new StringBufferInputStream(html);

	            }

	            throw new UnsupportedFlavorException(flavor);

	        }

	    }

}
