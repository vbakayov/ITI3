package TradeViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Class to track each filtering menu how many activeFilters it has
 * and the filtered data 
 * **/
public class SelectionFilter {
	private Integer activeFilters;
	private ArrayList<Integer> data;
	
	private HashMap<String, ArrayList> filterstroubles = new HashMap<String,ArrayList>(); 
	
	public SelectionFilter(Integer count, ArrayList<Integer> data){
		this.setActiveFilters(count);
		this.setData(data);
	}

	public SelectionFilter() {
		this.setActiveFilters(0);
		this.setData(new ArrayList<Integer>());
	}

	public Integer getActiveFilters() {
		return activeFilters;
	}
	
	
	
	public void incrementActiveFilters() {
		activeFilters++;
	}
	
	public void decrementActiveFilters(){
		activeFilters--;
	}

	public void setActiveFilters(Integer activeFilters) {
		this.activeFilters = activeFilters;
	}

	public ArrayList<Integer> getData() {
		return data;
	}
	
	public HashMap<String, ArrayList>  getMapForViolenceOrCases(){
		return filterstroubles;
	}
	
	public void removeFromHashMap (String nameCheckBox){
		System.out.println("removing selection for "+ nameCheckBox);
		filterstroubles.remove(nameCheckBox);
	}

	public void setData(ArrayList<Integer> data) {
		this.data = data;
	}
	
	public void add(int row){
		data.add(row);
	}
	
	public void addToHashMap(String nameCheckBox,int row){
		ArrayList<Integer> itemsList = filterstroubles.get(nameCheckBox);

	    // if list does not exist create it
	    if(itemsList == null) {
	         itemsList = new ArrayList<Integer>();
	         itemsList.add(row);
	         filterstroubles.put(nameCheckBox, itemsList);
	    } else {
	        // add if item is not already in list
	        if(!itemsList.contains(row)) itemsList.add(row);
	    }
	}
	
	public boolean contains(int row){
		return data.contains(row);
	}
	
	public void remove(Integer integer) {
		data.remove(integer);
		
	}

}
