package TradeViewer;

/**	SelectorPanel.java
 *	Provides a check box and slider view.
 *
 *	This class is not a view in the true sense, because it can influence the other views,
 *  but its display cannot be affected by actions performed in any other view.
 *	This is done on purpose, since actions from the other views can create combinations  
 *	of selected items that cannot be represented in the check box view.
 *
 *	Reusing open-source range sliders is part of the design - it is neither difficult nor impossible
 *  to create two basic JSliders, assign a listener for each of them and add them to the panel. 
 *  But a range slider provides better experience for the user, and here is an explanation why:
 *  
 *  For most of the continuous attributes, there is a very large difference between the min and
 *  the max value. Hence if the track of the slider (the area that the slider slides in) is small,
 *  then even minimal movements of the slider's thumb will greatly increment/decrement the slider's value,
 *  leading to low accuracy. Considering the limited amount of space in the window (we aim to preserve its 
 *  original dimensions), using two sliders for each attribute will result in sliders with very small tracks 
 *  and very low accuracy. If we use range sliders for the same amount of space, we will need twice 
 *  as less sliders, and in turn they will be twice as large and have twice the accuracy.
 *	
 * 	To sum up, having custom open-source range sliders instead of JSliders is a design choice,
 *	because using only the basic Swing components may provide the required functuinality, 
 *  but does not give the best user experience.
 *  
 *  The code for the JRangeSlider class is reused from
 *	https://github.com/prefuse/Prefuse/blob/master/src/prefuse/util/ui/JRangeSlider.java
 *	API: http://prefuse.org/doc/api/prefuse/util/ui/JRangeSlider.html
 *
 *	@author  Viktor Bakayov
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;


class SelectorPanel extends JPanel 
					implements ViewController {
	
	private Model model;
	// range sliders for the continuous attributes
	private JRangeSlider xRange, yRange, cIDRange, pIDRange, yieldRange, dtmRange,
						 amountRange, fullnameRange, indexRange;
	// menu bars with menus containing check boxes for the categorical attributes
	private JMenuBar AgeGroupMenuBar, CountrySegmentMenuBar, tradeDateMenuBar;
	private JMenu currencyMenu, customerMenu, tradeDateMenu;
	// descriptive labels
	private JLabel xLabel, yLabel, cIDLabel, pIDLabel, yieldLabel, 
				   dtmLabel, amountLabel, fullnameLabel, indexLabel,	   
				   AgeGroupLabel, CountrySegmentLabel, tradeDateLabel;
	// used for creating the check boxes
	private ArrayList<String> AgeGroupList, countrySegmentList, tradeDateList;
    // used for filtering the points
    private ArrayList<Integer> available;
    // used for multiple selection of points
    private ArrayList<Integer> selected;

    
    private ArrayList<Integer> filtered;
    private int activeFilters ;
	private HashMap<JMenu, SelectionFilter> activeFiltersMap;
	public SelectorPanel(Model m) {
		
		this.model = m;
		this.available = model.getAvailableRows();
		this.selected = new ArrayList<Integer>();
		this.setLayout(new GridLayout(12,2));
		this.filtered  = new ArrayList<Integer>()	;
		this.activeFiltersMap= new HashMap<JMenu, SelectionFilter>();
		activeFilters= 0;
		System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		
		// the lists are sorted so that the check boxes appear in alphabetical order
		AgeGroupList = new ArrayList<String>();		
		for (int row = 0; row < model.dataSize(); row++) {
        		String AgeGroup = (String) model.record(row).get(3);
        		if (!AgeGroupList.contains( AgeGroup))
        			AgeGroupList.add( AgeGroup);
		}
		Collections.sort(AgeGroupList);
		
		countrySegmentList = new ArrayList<String>();		
		for (int row = 0; row < model.dataSize(); row++) {
        		String countrySegment = (String) model.record(row).get(4);
        		if (!countrySegmentList.contains(countrySegment))
        			countrySegmentList.add(countrySegment);
		}
		Collections.sort(countrySegmentList);
		
		tradeDateList = new ArrayList<String>();		
//		for (int row = 0; row < model.dataSize(); row++) {
//        		String tradeDate = (String) model.record(row).get(10);
//        		if (!tradeDateList.contains(tradeDate))
//        			tradeDateList.add(tradeDate);
//		}
		Collections.sort(tradeDateList);
		
		// create and add the menu bars to the panel (along with their descriptive labels)
		
		// CURRENCY_ISO
		currencyMenu = new JMenu("Selection Menu");
		currencyMenu.setName("Ageee");
		currencyMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		currencyMenu.setBorderPainted(true);
		
		AgeGroupMenuBar = new JMenuBar();		
		AgeGroupMenuBar.add("MIDDLE", currencyMenu);
		AgeGroupMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		
		for (String str : AgeGroupList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.addItemListener(new CheckBoxListener ());
			currencyMenu.add(cb);
		}
		
		 AgeGroupLabel = new JLabel("AgeGroup");
		 AgeGroupLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		 AgeGroupLabel.setHorizontalAlignment(JLabel.CENTER);
		 AgeGroupLabel.setVerticalAlignment(JLabel.CENTER);
        
        add( AgeGroupLabel);
		add(AgeGroupMenuBar);
		
		// CUSTOMER_SEGMENT
		customerMenu = new JMenu("Selection Menu");
		customerMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		customerMenu.setBorderPainted(true);
		
		CountrySegmentMenuBar = new JMenuBar();		
		CountrySegmentMenuBar.add("MIDDLE", customerMenu);
		CountrySegmentMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		
		for (String str : countrySegmentList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.addItemListener(new CheckBoxListener ());
			customerMenu.add(cb);
		} 
		
		CountrySegmentLabel = new JLabel("Country");
		CountrySegmentLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		CountrySegmentLabel.setHorizontalAlignment(JLabel.CENTER);
		CountrySegmentLabel.setVerticalAlignment(JLabel.CENTER); 
        
		add(CountrySegmentLabel);
		add(CountrySegmentMenuBar);
		
		// TRADE_DATE
		tradeDateMenu = new JMenu("Selection Menu");
		tradeDateMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		tradeDateMenu.setBorderPainted(true);
		
		tradeDateMenuBar = new JMenuBar();		
		tradeDateMenuBar.add("MIDDLE", tradeDateMenu);
		tradeDateMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		
		for (String str : tradeDateList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.addItemListener(new CheckBoxListener ());
			tradeDateMenu.add(cb);
		}
		
		tradeDateLabel = new JLabel("TRADE_DATE");
		tradeDateLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		tradeDateLabel.setHorizontalAlignment(JLabel.CENTER);
        tradeDateLabel.setVerticalAlignment(JLabel.CENTER);  

		add(tradeDateLabel);
		add(tradeDateMenuBar);
		
		// create and add range sliders and descriptive labels
		
		// X
		xRange = new JRangeSlider(-50, 50, -50, 50, JRangeSlider.HORIZONTAL);
		xRange.addChangeListener(new RangeSliderChangeListener());
		xRange.addChangeListener(new RangeSliderLabelUpdater());
		
		xLabel = new JLabel("X   [ " + xRange.getLowValue() + " to " + xRange.getHighValue() + " ]");
		xLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		xLabel.setHorizontalAlignment(JLabel.CENTER);
        xLabel.setVerticalAlignment(JLabel.CENTER);    
        
		add (xLabel);
		add (xRange);
		
		// Y
		yRange = new JRangeSlider(-50, 50, -50, 50, JRangeSlider.HORIZONTAL);
		yRange.addChangeListener(new RangeSliderChangeListener());
		yRange.addChangeListener(new RangeSliderLabelUpdater());
		
		yLabel = new JLabel("Y   [ " + yRange.getLowValue() + " to " + yRange.getHighValue() + " ]");
		yLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		yLabel.setHorizontalAlignment(JLabel.CENTER);
        yLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (yLabel);
		add (yRange);
		
		// CUSTOMER_ID
		cIDRange = new JRangeSlider(1, 5000, 1, 5000, JRangeSlider.HORIZONTAL);
		cIDRange.addChangeListener(new RangeSliderChangeListener());
		cIDRange.addChangeListener(new RangeSliderLabelUpdater());
		
		cIDLabel = new JLabel("CUSTOMER_ID   [ " + cIDRange.getLowValue() + " to " + cIDRange.getHighValue() + " ]");
		cIDLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		cIDLabel.setHorizontalAlignment(JLabel.CENTER);
		cIDLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (cIDLabel);
		add (cIDRange);
		
		// PRODUCTTYPE_ID
		pIDRange = new JRangeSlider(1, 5000, 1, 5000, JRangeSlider.HORIZONTAL);
		pIDRange.addChangeListener(new RangeSliderChangeListener());
		pIDRange.addChangeListener(new RangeSliderLabelUpdater());
		
		pIDLabel = new JLabel("PRODUCTTYPE_ID   [ " + pIDRange.getLowValue() + " to " + pIDRange.getHighValue() + " ]");
		pIDLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		pIDLabel.setHorizontalAlignment(JLabel.CENTER);
		pIDLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (pIDLabel);
		add (pIDRange);
		
		// YIELD
		yieldRange = new JRangeSlider(0, 100, 0, 100, JRangeSlider.HORIZONTAL);
		yieldRange.addChangeListener(new RangeSliderChangeListener());
		yieldRange.addChangeListener(new RangeSliderLabelUpdater());
		
		yieldLabel = new JLabel("Age   [ " + yieldRange.getLowValue() + " to " + yieldRange.getHighValue() + "]");
		yieldLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		yieldLabel.setHorizontalAlignment(JLabel.CENTER);
		yieldLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (yieldLabel);
		add (yieldRange);
		System.out.println("Hafter adding the labels and slider");
//		// DAYS_TO_MATURITY
		dtmRange = new JRangeSlider(1, 10000, 1, 10000, JRangeSlider.HORIZONTAL);
		dtmRange.addChangeListener(new RangeSliderChangeListener());
		dtmRange.addChangeListener(new RangeSliderLabelUpdater());
		
		dtmLabel = new JLabel("DAYS_TO_MATURITY   [ " + dtmRange.getLowValue() + " to " + dtmRange.getHighValue() + " ]");
		dtmLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		dtmLabel.setHorizontalAlignment(JLabel.CENTER);
		dtmLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (dtmLabel);
		add (dtmRange);
		
		// AMOUNT_CHF(000)	
		amountRange = new JRangeSlider(1, 50000, 1, 50000, JRangeSlider.HORIZONTAL);
		amountRange.addChangeListener(new RangeSliderChangeListener());
		amountRange.addChangeListener(new RangeSliderLabelUpdater());

		amountLabel = new JLabel("AMOUNT_CHF(000)   [ " + amountRange.getLowValue() + " to " + amountRange.getHighValue() + " ]");
		amountLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		amountLabel.setHorizontalAlignment(JLabel.CENTER);
		amountLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (amountLabel);
		add (amountRange);
		
		// FULLNAME
		fullnameRange = new JRangeSlider(0, 25, 0, 25, JRangeSlider.HORIZONTAL);
		fullnameRange.addChangeListener(new RangeSliderChangeListener());
		fullnameRange.addChangeListener(new RangeSliderLabelUpdater());
		
		char lowerBound = (char) ('A' + fullnameRange.getLowValue());
		char upperBound = (char) ('A' + fullnameRange.getHighValue());
		fullnameLabel = new JLabel("FULLNAME   [ " + lowerBound + " to " + upperBound + " ]");   
		fullnameLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		fullnameLabel.setHorizontalAlignment(JLabel.CENTER);
		fullnameLabel.setVerticalAlignment(JLabel.CENTER);
		
		add (fullnameLabel);
		add (fullnameRange);
		
		// INDEX
		indexRange = new JRangeSlider(1, 1000, 1, 1000, JRangeSlider.HORIZONTAL);
		// of course, the maximum can be expanded up to 99999, but with the
		// current expected number of total indexes being about several hundred,
		// this will be make the slider incredibly difficult to use
		indexRange.addChangeListener(new RangeSliderChangeListener());
		indexRange.addChangeListener(new RangeSliderLabelUpdater());
		
		String lowerB = "" + indexRange.getLowValue();
		while (lowerB.length() < 5) lowerB = "0" + lowerB;
		String upperB = "" + indexRange.getHighValue();
		while (upperB.length() < 5) upperB = "0" + upperB;
		indexLabel = new JLabel("INDEX   [ T" + lowerB + " to T" + upperB + " ]");
		indexLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		indexLabel.setHorizontalAlignment(JLabel.CENTER);
		indexLabel.setVerticalAlignment(JLabel.CENTER);  
        
		add (indexLabel);
		add (indexRange);

	}
	
	// inner class that handles events from the check boxes
    private class CheckBoxListener implements ItemListener {	
    	
    	public void itemStateChanged(ItemEvent event) {
    		//get the JCheckBox which triggered the event
    		JCheckBoxMenuItem source = (JCheckBoxMenuItem) event.getSource() ;
    		String value2 = source.getText();
    		//find the menue the CheckBox belongs to
    		JPopupMenu fromParent = (JPopupMenu)source.getParent();
    		JMenu menu = (JMenu)fromParent.getInvoker();	

    		//HashMap to track the active menus
    		if(!activeFiltersMap.containsKey(menu)) activeFiltersMap.put(menu, new SelectionFilter());	
    		
        	// a check box is selected 
        	if (event.getStateChange() == ItemEvent.SELECTED) {  
        		//clear the data if there are no active filets for the correspoding menu
        		if(activeFiltersMap.get(menu).getActiveFilters().equals(0))
        			activeFiltersMap.get(menu).getData().clear();
        
        		//increment the number of filters for the corresponding menu
        		 activeFiltersMap.get(menu).incrementActiveFilters() ;
        		
        		//iterate the data, add all items that match the criteria to its menu entries list
	        	for (int row = 0; row < model.dataSize(); row++) {
		        		ArrayList record = model.record(row);
			        	if (record.contains(value2)) activeFiltersMap.get(menu).add(row);
	        	}
        		System.out.println(model.getCountry(0));
	        	model.setAvailableRows(filterdata());
	        	model.select(new ArrayList());
	        	
        		
        	}
        	// a check box is  deselected 
        	if(event.getStateChange()== ItemEvent.DESELECTED){
        		
        		//decrement the active filters
        		 activeFiltersMap.get(menu).decrementActiveFilters() ;

        		for(int row = 0; row<model.dataSize(); row++){
        			ArrayList record = model.record(row);
        			//no filters for the menu which means all data is available
        			if(activeFiltersMap.get(menu).getActiveFilters().equals(0)){
        				if(row == 0) activeFiltersMap.get(menu).getData().clear();
        				activeFiltersMap.get(menu).add(row);
        				}
        			//else one or more filters active, data should be further restricted (remove)
        			//use new Integer(row) to remove by value , not by index!!!!
        			else if(record.contains(value2)) activeFiltersMap.get(menu).remove(new Integer(row));
        		}
        		
        		model.setAvailableRows(filterdata());
        		model.select(new ArrayList());
        		//if not filters active for the menue remove its entries from the hashMap
        		if(activeFiltersMap.get(menu).getActiveFilters().equals(0)){
        			activeFiltersMap.remove(menu);
        		}
        	}
    	}    	  	
    }
    /**
     * 
     * Method to find the  intersection of all filters
     * Enables multi filtering
     * 
     * **/
    private ArrayList<Integer> filterdata(){
    	//store the elements which occur in every(intersection)  menu(filter)
    	ArrayList<Integer> output = new ArrayList<Integer>();
    	int first= 0;
    	//iterate on  the menus
    	Iterator it = activeFiltersMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		//System.out.println(pair.getKey() + " = " + pair.getValue());
    		ArrayList<Integer> data =   ((SelectionFilter) pair.getValue()).getData();
    		if(first++ == 0) output.addAll(data);
    		//Intersection
    		output.retainAll(data);
    	}
    	return output;
    }
    
    
    
    //	Note: for convenience, we use two separate handler classes for
    // 		  updating the other views refreshing the sliders' labels.
    
	// inner class that handles events from the range sliders
    private class RangeSliderChangeListener implements ChangeListener {	
    	
    	// saves typing when parsing doubles from a record's elements 
    	public double parseD (Object o) {
    		return Double.parseDouble((String) o);
    	}
		
		public void stateChanged(ChangeEvent event) {		
			JRangeSlider source = (JRangeSlider) event.getSource() ;
        	ArrayList<Integer> filtered = new ArrayList<Integer>();
        	
        	// filters the values from the data set
    		for (int row = 0; row < model.dataSize(); row++) {
				ArrayList record = model.record(row);
				boolean passFilters = true;
				
				// filter for X
//	        	if (parseD(record.get(0)) < xRange.getLowValue() ||
//	        		parseD(record.get(0)) > xRange.getHighValue()) 
//	        		passFilters = false;
//	        	
	        	// filter for Y
//	        	if (parseD(record.get(1)) < yRange.getLowValue() ||
//		        	parseD(record.get(1)) > yRange.getHighValue()) 
//		        	passFilters = false;
	        	
	        	// filter for CUSTOMER_ID
//	        	if (parseD(record.get(2)) < cIDRange.getLowValue() ||
//		        	parseD(record.get(2)) > cIDRange.getHighValue()) 
//		        	passFilters = false;
	        	
	        	// filter for PRODUCTTYPE_ID
//	        	if (parseD(record.get(3)) < pIDRange.getLowValue() ||
//			        parseD(record.get(3)) > pIDRange.getHighValue()) 
//		        	passFilters = false;
//	        	
	        	// filter for YIELD
	        	if (parseD(record.get(2)) < yieldRange.getLowValue() ||
	        		parseD(record.get(2)) > yieldRange.getHighValue())
		        	passFilters = false;
	        	
//	        	// filter for DAYS_TO_MATURITY
//	        	if (parseD(record.get(7)) < dtmRange.getLowValue() ||
//		        	parseD(record.get(7)) > dtmRange.getHighValue())
//		        	passFilters = false;
//	        	
//	        	// filter for AMOUNT_CHF(000)
//	        	if (parseD(record.get(8)) < amountRange.getLowValue() ||
//		        	parseD(record.get(8)) > amountRange.getHighValue())
//		        	passFilters = false;
//	        	
//	        	// filter for FULLNAME
//	        	if (((String)record.get(9)).charAt(0) < (char)('A' + fullnameRange.getLowValue()) ||
//	        		((String)record.get(9)).charAt(0) > (char)('A' + fullnameRange.getHighValue())) 
//		        	passFilters = false;
//	        	
//	        	// filter for INDEX
//	        	// omit the 'T' at the beginning and parse a double from the rest of the string
//	        	double value = Double.parseDouble(((String) record.get(11)).substring(1));
//	        	if (value < indexRange.getLowValue() || value > indexRange.getHighValue())
//		        	passFilters = false;

	        	if (passFilters) filtered.add(row);
	        			
    		} // end of loop
    		
    		// update the filtered rows
    		model.setAvailableRows(filtered);
    		// update the views by executing selection with an empty array
    		model.select(new ArrayList());
		}     
    }
    
    // inner class that handles updating of the sliders' labels
    private class RangeSliderLabelUpdater implements ChangeListener {	
		public void stateChanged(ChangeEvent event) {	
			JRangeSlider source = (JRangeSlider) event.getSource() ;
			// update the corresponding descriptive label
    		if (source.equals(xRange)) 
    			xLabel.setText("X   [ " + xRange.getLowValue() + " to " + xRange.getHighValue() + " ]");
    		else if (source.equals(yRange)) 
    			yLabel.setText("Y   [ " + yRange.getLowValue() + " to " + yRange.getHighValue() + " ]");
    		else if (source.equals(cIDRange)) 
    			cIDLabel.setText("CUSTOMER_ID   [ " + cIDRange.getLowValue() + " to " + cIDRange.getHighValue() + " ]");
    		else if (source.equals(pIDRange)) 
    			pIDLabel.setText("PRODUCTTYPE_ID   [ " + pIDRange.getLowValue() + " to " + pIDRange.getHighValue() + " ]");
    		else if (source.equals(yieldRange)) 
    			yieldLabel.setText("YIELD   [ " + yieldRange.getLowValue() + "% to " + yieldRange.getHighValue() + "% ]");
    		else if (source.equals(dtmRange)) 
    			dtmLabel.setText("DAYS_TO_MATURITY   [ " + dtmRange.getLowValue() + " to " + dtmRange.getHighValue() + " ]");
    		else if (source.equals(amountRange)) 
    			amountLabel.setText("AMOUNT_CHF(000)   [ " + amountRange.getLowValue() + " to " + amountRange.getHighValue() + " ]"); 
    		else if (source.equals(fullnameRange)) {
    			char lowerBound = (char) ('A' + fullnameRange.getLowValue());
    			char upperBound = (char) ('A' + fullnameRange.getHighValue());
    			fullnameLabel.setText("FULLNAME   [ " + lowerBound + " to " + upperBound + " ]");
    		}
        	else if (source.equals(indexRange)) {
        		// fix formatting
        		String lowerBound = "" + indexRange.getLowValue();
        		while (lowerBound.length() < 5) lowerBound = "0" + lowerBound;
        		String upperBound = "" + indexRange.getHighValue();
        		while (upperBound.length() < 5) upperBound = "0" + upperBound;
        		indexLabel.setText("INDEX   [ T" + lowerBound + " to T" + upperBound + " ]");
        	}
    	}
    }
    
  
    
    public void update(ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows) {
    	available = availableRows;
        selected = selectedRows;
    }
	
}
