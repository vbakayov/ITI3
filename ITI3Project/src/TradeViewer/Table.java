package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Table extends JFrame {
	private	JPanel		topPanel;
	private	JTable		table;
	private	JScrollPane scrollPane;
	HashMap<String, Integer>casesMap;
	Model model;
	
	public Table(Model model, HashMap<String, Integer> casesMap)
	{
		this.casesMap=casesMap;
		this.model=model;
		// Set the frame characteristics
		setTitle( "Legal Cases" );
		setSize(500, 600 );
		setBackground( Color.gray );
		initTable();

		
	}
	
	private String[][] convertHashMaptoDoubleArray(){
		
		String data[][] = new String [1][casesMap.size()];
		//int i = 0;
		System.out.println("Size of CasesMap: "+ casesMap.size());
		int j=0;
		for( String key : casesMap.keySet()) {
		    String tValue = casesMap.get(key).toString();
		    
		    	//System.out.prirntln("OFFFFF "+ j);
		        data[0][j] = tValue;
		      
		    
		   j++;
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
				String columnNames[]=new String[20];
				for(int i=6;i<26;i++){
					columnNames[i-6] =model.getLabels(i);
				}
						

				// Create a new table instance
				table = new JTable(convertHashMaptoDoubleArray(), columnNames );

				// Add the table to a scrolling pane
				scrollPane = new JScrollPane( table );
				topPanel.add( scrollPane, BorderLayout.CENTER );
	}

}
