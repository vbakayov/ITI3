package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		setSize(200,300 );
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
						

				// Create a new table instance
				table = new JTable(convertHashMaptoDoubleArray(), columnNames );
				// Add the table to a scrolling pane
				scrollPane = new JScrollPane( table );
				topPanel.add( scrollPane, BorderLayout.CENTER );
	}

}
