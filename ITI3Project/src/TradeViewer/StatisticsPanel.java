package TradeViewer;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
	private HashMap <String,Integer> countryCountMap = new HashMap<String,Integer>();
	JPanel chartPanel=new JPanel();
	
	public StatisticsPanel(Model model) {
		this.model = model;
		populateMap();
		printMap();
		PieChart demo = new PieChart("Pie Chart",countryCountMap);
		  setLayout(new BorderLayout());
	     //add("Center", demo); 
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);	
	}
	
	private void printMap() {
		ArrayList<Integer> output = new ArrayList<Integer>();
    
    	//iterate on  the menus
    	Iterator it = countryCountMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		System.out.println(pair.getKey() + " = " + pair.getValue());
    		
    	}
    	
		
	}

	private void populateMap(){
		for(int i =0; i< model.dataSize(); i++){
			String country = model.getCountry(i);
			int count = countryCountMap.containsKey(country) ? countryCountMap.get(country) : 0;
			countryCountMap.put(country, count + 1);
		}
	}

	@Override
	public void update(ArrayList<Integer> availableRows,
			ArrayList<Integer> selectedRows) {
		// TODO Auto-generated method stub
		
	}
	
	

	   

}
