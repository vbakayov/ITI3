package TradeViewer;

import java.util.ArrayList;
/**
 * Class to track each filtering menu how many activeFilters it has
 * and the filtered data 
 * **/
public class SelectionFilter {
	private Integer activeFilters;
	private ArrayList<Integer> data;
	
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
	
	public boolean contains(int row){
		return data.contains(row);
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

	public void setData(ArrayList<Integer> data) {
		this.data = data;
	}
	
	public void add(int row){
		data.add(row);
	}

	public void remove(Integer integer) {
		data.remove(integer);
		
	}

}
