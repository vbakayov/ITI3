package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class StatisticsPanel extends JPanel 
							implements ViewController{
	
	
	private Model model;
	public HashMap <String,Integer> countryCountMap = new HashMap<String,Integer>();
	private HashMap<String, Integer> ageGroupsMap = new HashMap<String,Integer>();
	//hash map integer-integer(colomn intex, count) for the different cases
	private HashMap<String, Integer> casesMap = new HashMap<String,Integer>();
	JPanel chartPanel=new JPanel();
	private PieChart countryPieChart;
	private PieChart ageGroupPieChart;
	private JButton saveButton;
	private JButton saveButtonAge;
	private JButton deleteButton;
	
	public StatisticsPanel(Model model) {
		this.model = model;
		populateMap();
		countAgeGroups();
		getCaseCount();
		printMap();
		GUI();
	}
	
	
	
	public void GUI(){
		 setLayout(new GridLayout(4,2));
	        JButton countryButton = new JButton("Country Chart");
	         saveButton=new JButton("Save");
	      
	        add(countryButton);
	        add(saveButton);
	        
	       // AgeGroups
//	        Iterator it =  ageGroupsMap.entrySet().iterator();
//	    	while (it.hasNext()) {
//	    		Map.Entry pair = (Map.Entry)it.next();
//	    		System.out.println(pair.getKey() + " = " + pair.getValue());
//	    		JLabel age = new JLabel((String) pair.getKey());
//	    		JLabel  value = new JLabel(Integer.toString((int) pair.getValue()));
//	    		add(age);
//	    		add(value);
//	    		}
	        
	        JButton ageGroupButton = new JButton ("AgeGroup chart");
	        saveButtonAge = new JButton("Save");
	      
	        
	        add(ageGroupButton);
	        add(saveButtonAge);
	        
	        JButton casesButton=new JButton("Legal Cases");
	        JButton deleteButton = new JButton("Remove rows");
	        
	        
	        add(casesButton);
	        add(deleteButton);
	        ageGroupButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed();
					   saveButtonAge.setEnabled(true);
					//countryButton.setEnabled(false);
					
				}
				
				private void buttonPressed() {
					ageGroupPieChart= new PieChart("Age Groups",ageGroupsMap,false);
					setLayout(new BorderLayout());
				    // add("Center", demo);
					
					ageGroupPieChart.pack();
					RefineryUtilities.centerFrameOnScreen(ageGroupPieChart);
					ageGroupPieChart.setVisible(true);
					
				}
			
		
	        });
				
	        
	        
	        
	        
	        saveButton.addActionListener( new CustomButtonListener());
	        saveButton.setEnabled(false);
	        saveButtonAge.addActionListener( new CustomButtonListener());
	        saveButtonAge.setEnabled(false);
	        deleteButton.addActionListener(new ActionListener()
	        {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					model.CopyRowXLSXFile();
					model.removeRowsFromDataSet();
				}
	        	
	        });
	        countryButton.addActionListener(new ActionListener() {
				
			

				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed();
					saveButton.setEnabled(true);
					//countryButton.setEnabled(false);
					
				}

				private void buttonPressed() {
					//System.out.println("INVOKEEE HEREE");
					countryPieChart= new PieChart("Country of Origin",countryCountMap, true);
					setLayout(new BorderLayout());
				    // add("Center", demo);
					
					countryPieChart.pack();
					RefineryUtilities.centerFrameOnScreen(countryPieChart);
					countryPieChart.setVisible(true);
					
				}
		
	        });
	        
	        casesButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					Table mainFrame	= new Table(model,casesMap,totalNumberCases());
					mainFrame.setVisible( true );
					
					
				}
	        	
	        });
	        
	}
	private void printMap() {
		ArrayList<Integer> output = new ArrayList<Integer>();
    
    	//iterate on  the menus
    	//Iterator it = countryCountMap.entrySet().iterator();
		Iterator it = casesMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		
    		//System.out.println(pair.getKey() + " = " + pair.getValue());
    		
    	}
    	
		
	}

	private void populateMap(){
		for(int i =0; i< model.dataSize(); i++){
			String country = model.getCountry(i);
			int count = countryCountMap.containsKey(country) ? countryCountMap.get(country) : 0;
			countryCountMap.put(country, count + 1);
		}
	}
	
	private void countAgeGroups(){
		for(int i =0; i< model.dataSize(); i++){
			String country = model.getAgeGroup(i);
			int count =  ageGroupsMap.containsKey(country) ?  ageGroupsMap.get(country) : 0;
			 ageGroupsMap.put(country, count + 1);
		}
	}
	//fixed 
	private void getCaseCount(){
		for (int column = 0 ; column <model.getDataType().size() ; column ++){
			if(model.getDataType().get(column).equals("Case")){
			for(int row= 0; row< model.dataSize(); row++){
				String info = model.getData(column,row);
				String label = model.getLabels(column);
				if(row == model.dataSize()-1 && !casesMap.containsKey(label))	casesMap.put(label, 0);
				if(info.equals("YES")){
					
					int count = casesMap.containsKey(label) ? casesMap.get(label) : 0;
					casesMap.put(label, count + 1);
				}
			}
			}
		}
	}
	
	
	
	private int totalNumberCases(){
		int total=0;
		Iterator it = casesMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		total+=(int)pair.getValue();
    	}
    	return total;
		
		
	}
	

	@Override
	public void update(ArrayList<Integer> availableRows,
			ArrayList<Integer> selectedRows) {
		// TODO Auto-generated method stub
		
	}
	
	
	class CustomButtonListener implements  ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()== saveButton)
				showSaveDialog(countryPieChart);
			if(e.getSource() == saveButtonAge)
				showSaveDialog(ageGroupPieChart);
			//if(e.getSource()== deleteButton){
			    //model.RemoveROwwriteXLSXFile(1);

				//model.CopyRowXLSXFile(1);
				
				
			//}
		}

		
		private void showSaveDialog(PieChart inputChart) {
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save");

			int userSelection = fileChooser.showSaveDialog(chartPanel);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				inputChart.saveToFile(fileToSave.getAbsolutePath());
			}
		}
			
    };

	   

}



