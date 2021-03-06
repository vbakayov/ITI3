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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.List;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.crypto.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class SelectorPanel extends JPanel implements ViewController {

	private Model model;
	// range sliders for the continuous attributes
	private JRangeSlider ageRange;
	// menu bars with menus containing check boxes for the categorical
	// attributes
	private JMenuBar AgeGroupMenuBar, CountrySegmentMenuBar, casesDateMenuBar, ViolenceMenuBar;
	private JMenu ageMenu, countryMenu, CasesMenu, ViolenceMenu;
	// descriptive labels
	private JLabel xLabel, yLabel, cIDLabel, pIDLabel, ageLabel, dtmLabel, amountLabel, fullnameLabel, indexLabel,
			AgeGroupLabel, CountrySegmentLabel, casesLabel, violenceLabel;
	// used for creating the check boxes
	private ArrayList<String> AgeGroupList, countrySegmentList, casesList, violenceList;
	// used for filtering the points
	private ArrayList<Integer> available;
	// used for multiple selection of points
	private ArrayList<Integer> selected;

	private ArrayList<Integer> filtered;
	// sorter for the nameFilter

	private int activeFilters;
	private HashMap<JMenu, SelectionFilter> activeFiltersMap;
	private JLabel NameLabel;
	private JTextField filterText;
	private JLabel closeLabel;
	private JTextField filterDateText;
	private int cureentNameSymbols;
	private int curentNameSymbols;
	private int currentNameSymbols;

	public SelectorPanel(Model m) {

		this.model = m;
		this.available = model.getAvailableRows();
		this.selected = new ArrayList<Integer>();
		this.setLayout(new GridLayout(7, 2));
		this.filtered = new ArrayList<Integer>();
		this.activeFiltersMap = new HashMap<JMenu, SelectionFilter>();
		activeFilters = 0;
		// add a sorter to the table

		// the lists are sorted so that the check boxes appear in alphabetical
		// order
		AgeGroupList = new ArrayList<String>();
		for (int row = 0; row < model.dataSize(); row++) {
			String AgeGroup = (String) model.record(row).get(model.getPosition("Age Group"));
			if (!AgeGroupList.contains(AgeGroup))
				AgeGroupList.add(AgeGroup);
		}
		Collections.sort(AgeGroupList);

		// those are fixed

		countrySegmentList = new ArrayList<String>();
		for (int row = 0; row < model.dataSize(); row++) {
			String countrySegment = (String) model.record(row).get(model.getPosition("Country"));
			if (!countrySegmentList.contains(countrySegment))
				countrySegmentList.add(countrySegment);
		}

		Collections.sort(countrySegmentList);

		casesList = new ArrayList<String>();
		violenceList = new ArrayList<String>();

		String columnName = "";
		for (int column = 0; column < model.getDataType().size(); column++) {
			if (model.getDataType().get(column).equals("Violence")) {
				columnName = model.getLabels(column);
				// System.out.println(columnName);
				violenceList.add(columnName);
			}
			if (model.getDataType().get(column).equals("Case")) {
				columnName = model.getLabels(column);
				// System.out.println(columnName);
				casesList.add(columnName);
			}
		}
		Collections.sort(violenceList);
		Collections.sort(casesList);
		// create and add the menu bars to the panel (along with their
		// descriptive labels)

		// Ageee
		ageMenu = new JMenu("Selection Menu Age       ");
		ageMenu.setName("Age Group");
		ageMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		ageMenu.setBorderPainted(true);

		AgeGroupMenuBar = new JMenuBar();
		AgeGroupMenuBar.add("MIDDLE", ageMenu);
		AgeGroupMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

		for (String str : AgeGroupList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.addItemListener(new CheckBoxListener());
			ageMenu.add(cb);
		}

		AgeGroupLabel = new JLabel("Age Group");
		AgeGroupLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		AgeGroupLabel.setHorizontalAlignment(JLabel.CENTER);
		AgeGroupLabel.setVerticalAlignment(JLabel.CENTER);


		// CountryMenue
		countryMenu = new JMenu("Selection Menu Country  ");
		countryMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		countryMenu.setName("Country");
		countryMenu.setBorderPainted(true);

		CountrySegmentMenuBar = new JMenuBar();
		CountrySegmentMenuBar.add("MIDDLE", countryMenu);
		CountrySegmentMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

		for (String str : countrySegmentList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.addItemListener(new CheckBoxListener());
			countryMenu.add(cb);
		}

		CountrySegmentLabel = new JLabel("Country");
		CountrySegmentLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		CountrySegmentLabel.setHorizontalAlignment(JLabel.CENTER);
		CountrySegmentLabel.setVerticalAlignment(JLabel.CENTER);

		// Cases Menue
		// Union or Intersection

		CasesMenu = new JMenu("Selection Menu Cases   ");
		CasesMenu.setName("Case");
		CasesMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		CasesMenu.setBorderPainted(true);

		casesDateMenuBar = new JMenuBar();
		casesDateMenuBar.add("MIDDLE", CasesMenu);

		casesDateMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

		for (String str : casesList) {
			JCheckBoxMenuItem cb = new JCheckBoxMenuItem(str);
			cb.setName(str);
			cb.addItemListener(new CheckBoxListener());
			CasesMenu.add(cb);
		}

		casesLabel = new JLabel("Cases");
		casesLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		casesLabel.setHorizontalAlignment(JLabel.CENTER);
		casesLabel.setVerticalAlignment(JLabel.CENTER);
		

		NameLabel = new JLabel("Name");
		NameLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		NameLabel.setHorizontalAlignment(JLabel.CENTER);
		NameLabel.setVerticalAlignment(JLabel.CENTER);

		filterText = new HintTextField("Enter Name");
		filterText.setFont(new Font("Courier", Font.BOLD, 12));
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				newFilter(arg0.getType().toString());

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				newFilter(arg0.getType().toString());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				newFilter(arg0.getType().toString());
			}
		});


		closeLabel = new JLabel("Closing Date");
		closeLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		closeLabel.setHorizontalAlignment(JLabel.CENTER);
		closeLabel.setVerticalAlignment(JLabel.CENTER);

		filterDateText = new HintTextField("Ex. 01-Jun-2015");

		// create new Font
		filterDateText.setFont(new Font("Courier", Font.BOLD, 12));
		filterDateText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				newDateFilter();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				newDateFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				newDateFilter();
			}
		});


		//Age
		ageRange = new JRangeSlider(0, 100, 0, 100, JRangeSlider.HORIZONTAL);
		ageRange.addChangeListener(new RangeSliderChangeListener());
		ageRange.addChangeListener(new RangeSliderLabelUpdater());

		ageLabel = new JLabel("Age   [ " + ageRange.getLowValue() + " to " + ageRange.getHighValue() + "]");
		ageLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		ageLabel.setHorizontalAlignment(JLabel.CENTER);
		ageLabel.setVerticalAlignment(JLabel.CENTER);


		ViolenceMenu = new JMenu("Selection Menu Violence");
		ViolenceMenu.setName("Violence");
		ViolenceMenu.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		ViolenceMenu.setBorderPainted(true);

		ViolenceMenuBar = new JMenuBar();
		ViolenceMenuBar.add("MIDDLE", ViolenceMenu);
		ViolenceMenuBar.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

		ViolenceMenuBar.setAlignmentX(JMenuBar.CENTER_ALIGNMENT);
		ViolenceMenuBar.setAlignmentY(JMenuBar.CENTER_ALIGNMENT);

		for (String str : violenceList) {
			JCheckBoxMenuItem vn = new JCheckBoxMenuItem(str);
			vn.setName(str);
			vn.addItemListener(new CheckBoxListener());
			ViolenceMenu.add(vn);
		}

		violenceLabel = new JLabel("Violence Cases");
		violenceLabel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		violenceLabel.setHorizontalAlignment(JLabel.CENTER);
		violenceLabel.setVerticalAlignment(JLabel.CENTER);
		
		add(AgeGroupLabel);
		add( AgeGroupMenuBar);

		add(CountrySegmentLabel);
		add(CountrySegmentMenuBar);
		
		add(casesLabel);
		add(casesDateMenuBar);

		add(violenceLabel);
		add(ViolenceMenuBar);
		
		add(ageLabel);
		add(ageRange);
		
		add(NameLabel);
		add(filterText);
		
		add(closeLabel);
		add(filterDateText);
		

	}

	// inner class that handles events from the check boxes
	private class CheckBoxListener implements ItemListener {

		public void itemStateChanged(ItemEvent event) {
			// get the JCheckBox which triggered the event
			JCheckBoxMenuItem source = (JCheckBoxMenuItem) event.getSource();
			int indexColumn= 0;
			source.requestFocus();
			String value2, value3;
			// find the menue the CheckBox belongs to
			JPopupMenu fromParent = (JPopupMenu) source.getParent();
			JMenu menu = (JMenu) fromParent.getInvoker();
			/// "YES" for range values
			value2 = (menu.getName() == "Violence" || menu.getName() == "Case") ? "YES" : source.getText();
			if (menu.getName() == "Case" || menu.getName() == "Violence") {
				indexColumn = model.getIndexOfLabel(source.getText());}
			 else if (menu.getName() == "Country"  ) {
				 indexColumn= model.getIndexOfLabel("Country of Origin");}
			 else if (menu.getName() == "Age Group") {
				 indexColumn = model.getIndexOfLabel("Age Group");}
			

			// HashMap to track the active menus
			if (!activeFiltersMap.containsKey(menu))
				activeFiltersMap.put(menu, new SelectionFilter());

			// a check box is selected
			if (event.getStateChange() == ItemEvent.SELECTED) {
				// clear the data if there are no active filets for the
				// correspoding menu
		
				if (activeFiltersMap.get(menu).getActiveFilters().equals(0))
					activeFiltersMap.get(menu).getData().clear();

				// increment the number of filters for the corresponding menu
				activeFiltersMap.get(menu).incrementActiveFilters();

				for (int row = 0; row < model.dataSize(); row++) {
					ArrayList record = model.record(row);
					if (menu.getName() == "Case" || menu.getName() == "Violence") {
						if (record.get(indexColumn).equals(value2) ){
							activeFiltersMap.get(menu).addToHashMap(source.getName(), row);
							
						}
					} else if (menu.getName() == "Country"  ) {
						if (record.get(indexColumn).equals(value2))
							activeFiltersMap.get(menu).add(row);
					} else if (menu.getName() == "Age Group") {
						if (record.get(indexColumn).equals(value2))
							activeFiltersMap.get(menu).add(row);
					}
				}

				model.setAvailableRows(filterdata());
				model.select(new ArrayList());
				model.getTable().getSelectionModel().clearSelection();
				model.notifyChangeChildren();

			}
			// a check box is deselected
			if (event.getStateChange() == ItemEvent.DESELECTED) {
				System.out.println("HEREEEEE");
				// decrement the active filters
				activeFiltersMap.get(menu).decrementActiveFilters();

				for (int row = 0; row < model.dataSize(); row++) {
					ArrayList record = model.record(row);
					// no filters for the menu which means all data is available
					if (activeFiltersMap.get(menu).getActiveFilters().equals(0)) {
						if (row == 0)
							activeFiltersMap.get(menu).getData().clear();
						activeFiltersMap.get(menu).add(row);
					}
					// else one or more filters active, data should be further
					// restricted (remove)
					// use new Integer(row) to remove by value , not by
					// index!!!!
					else if (record.get(indexColumn).equals(value2) && activeFiltersMap.get(menu).contains(row))
						activeFiltersMap.get(menu).remove(new Integer(row));
				}
				
				if( menu.getName().equals( "Case") || menu.getName().equals("Violence"))
					activeFiltersMap.get(menu).removeFromHashMap(source.getName());

				model.setAvailableRows(filterdata());
				model.select(new ArrayList());
				model.getTable().getSelectionModel().clearSelection();
				model.notifyChangeChildren();
				// if not filters active for the menue remove its entries from
				// the hashMap
				if (activeFiltersMap.get(menu).getActiveFilters().equals(0)) {
					activeFiltersMap.remove(menu);
				}
			}
		}
	}

	/**
	 * 
	 * Method to find the intersection of all filters Enables multi filtering
	 * 
	 **/
	private ArrayList<Integer> filterdata() {
		filterDateText.setBackground(Color.WHITE);
		// store the elements which occur in every(intersection) menu(filter)
		ArrayList<Integer> output = new ArrayList<Integer>();
		Set<Integer> outputCases = new HashSet<Integer>();
		int first = 0;
		int firstSpecial = 0;
		// iterate on the menus
		Iterator it = activeFiltersMap.entrySet().iterator();
		while (it.hasNext()) {
			System.out.println(" I Am here");
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey());
			// System.out.println(pair.getKey() + " = " + pair.getValue());
			ArrayList<Integer> data = ((SelectionFilter) pair.getValue()).getData();
			System.out.println(data);
			HashMap<String, ArrayList> dataFromViolenceAndCases =((SelectionFilter)pair.getValue()).getMapForViolenceOrCases();
			
			Iterator it2= dataFromViolenceAndCases.entrySet().iterator();
			while(it2.hasNext()){
				Map.Entry pair2 = (Map.Entry) it2.next();
				ArrayList<Integer> data2 =  (ArrayList<Integer>) pair2.getValue();
			//	System.out.println(pair2.getKey()+ " " +data2.toString());
				if(firstSpecial++ == 0)
					outputCases.addAll(data2);
				else{
					outputCases.retainAll(data2);}
			}
			
				
			//if only one filter active
			if (first++ == 0){
				output.addAll(data);
				output.addAll(outputCases);
				}
			// Intersection
			if(data.size() != 0){
				output.retainAll(data);
			}
		}
		if(outputCases.size() != 0)
			output.retainAll(outputCases);
		Collections.sort(output);
		
		System.out.println(output.toString());
		return output;
	}

	// Note: for convenience, we use two separate handler classes for
	// updating the other views refreshing the sliders' labels.

	// inner class that handles events from the range sliders
	private class RangeSliderChangeListener implements ChangeListener {

		// saves typing when parsing doubles from a record's elements
		public double parseD(Object o) {
			return o != "" ? Double.parseDouble((String) o) : 0;
		}

		public void stateChanged(ChangeEvent event) {
			JRangeSlider source = (JRangeSlider) event.getSource();
			ArrayList<Integer> filtered = new ArrayList<Integer>();

			// filters the values from the data set
			for (int row = 0; row < model.dataSize(); row++) {
				ArrayList record = model.record(row);
				boolean passFilters = true;

				//
				// filter for YIELD
				if (parseD(record.get(2)) < ageRange.getLowValue() || parseD(record.get(2)) > ageRange.getHighValue())
					passFilters = false;

				if (passFilters)
					filtered.add(row);

			} // end of loop

			// update the filtered rows
			model.setAvailableRows(filtered);
			// update the views by executing selection with an empty array
			model.select(new ArrayList());
			model.getTable().getSelectionModel().clearSelection();
			model.notifyChangeChildren();
		}
	}

	// inner class that handles updating of the sliders' labels
	private class RangeSliderLabelUpdater implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			JRangeSlider source = (JRangeSlider) event.getSource();
			// update the corresponding descriptive label
			if (source.equals(ageRange))
				ageLabel.setText("Age  [ " + ageRange.getLowValue() + " to " + ageRange.getHighValue() + "]");
			else if (source.equals(ageRange)) {
				// fix formatting
				String lowerBound = "" + ageRange.getLowValue();
				while (lowerBound.length() < 5)
					lowerBound = "0" + lowerBound;
				String upperBound = "" + ageRange.getHighValue();
				while (upperBound.length() < 5)
					upperBound = "0" + upperBound;
				ageLabel.setText("INDEX   [ T" + lowerBound + " to T" + upperBound + " ]");
			}
		}
	}

	/**
	 * Update the row filter regular expression from the expression in the text
	 * box.
	 * 
	 * @param type
	 */
	private void newFilter(String type) {

		System.out.println(type);

		if (!filterText.getText().equals("Enter Name")
				&& (!filterText.getText().equals("") || currentNameSymbols == 0)) {
			clearSelection();

			if (type.equals("REMOVE"))
				currentNameSymbols--;
			if (type.equals("INSERT"))
				currentNameSymbols++;
			System.out.println("COUNT " + currentNameSymbols);
			ArrayList<Integer> output = new ArrayList<Integer>();
			// If current expression doesn't parse, don't update.
			for (int row = 0; row < model.dataSize(); row++) {
				String name = (String) model.record(row).get(0);
				// System.out.println(name + " "+ filterText.getText());
				if (name.toLowerCase().contains(filterText.getText().toLowerCase())) {
					output.add(row);
				}

			}
			// update the filtered rows
			model.setAvailableRows(output);
			// update the views by executing selection with an empty arra
			model.select(new ArrayList());
			model.notifyChangeChildren();
		}
	}

	private void clearSelection() {
	//	activeFiltersMap.clear();
		
	}

	private void newDateFilter() {
		Date dateRecord;
		ArrayList<Integer> output = new ArrayList<Integer>();
		if (filterDateText.getText().trim().length() != 11)
			filterDateText.setBackground(Color.WHITE);
		if (!filterDateText.getText().equals("Ex. 01-Jun-2015") && !filterDateText.getText().equals("")
				&& filterDateText.getText().trim().length() == 11) {
			output.clear();
			boolean allRows = true;

			// filter on whole date object
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			try {
				System.out.println(filterDateText.getText());
				Date date = formatter.parse(filterDateText.getText());

				filterDateText.setBackground(Color.green);
				for (int row = 0; row < model.dataSize(); row++) {
					// System.out.println(model.getPosition("Case Closed"));
					String dateRecord1 = (String) model.record(row).get(model.getPosition("Case Closed"));
					if (!dateRecord1.equals("")) {
						dateRecord = formatter.parse(dateRecord1);
						System.out.println("NOW   ");
						System.out.print(dateRecord);
						if (dateRecord.before(date)) {
							System.out.println("It Matches");
							output.add(row);
							allRows = false;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
				// filterDateText.setBackground(Color.RED);
			}

			if (allRows) {
				for (int row = 0; row < model.dataSize(); row++)
					output.add(row);
			}

			// update the filtered rows
			model.setAvailableRows(output);
			// update the views by executing selection with an empty arra
			model.select(new ArrayList());
			model.notifyChangeChildren();

		} else {
			if (!filterDateText.getText().equals("Ex. 01-Jun-2015")&& !filterDateText.getText().equals("")) {
				output.clear();
				// when the data doesn't match criterion is progress show all
				// available rows
				for (int i = 0; i < model.dataSize(); i++) {
					output.add(i);
				}

				model.setAvailableRows(output);
				// update the views by executing selection with an empty arra
				model.select(new ArrayList());
				model.notifyChangeChildren();

			}

		}
	}

	class HintTextField extends JTextField implements FocusListener {

		private final String hint;
		private boolean showingHint;

		public HintTextField(final String hint) {
			super(hint);
			this.hint = hint;
			this.showingHint = true;
			super.addFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText("");
				showingHint = false;
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText(hint);
				super.setBackground(Color.WHITE);
				showingHint = true;
			}
		}

		@Override
		public String getText() {
			return showingHint ? "" : super.getText();
		}
	}

	public void update(ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows) {
		available = availableRows;
		selected = selectedRows;
	}

	@Override
	public void notify(boolean delete) {
		// TODO Auto-generated method stub

	}

}
