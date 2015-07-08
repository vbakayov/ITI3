package TradeViewer;

/*	ViewController.java
 *	Interface for MVC protocol. Each VC pair should implement this
 *
 *	modified by Dobromir Dobrev - 1103606
 *	in order to enable multiple selection of items in the Trade Viewer
 */

import java.util.ArrayList;

public interface ViewController {
	public void update (ArrayList<Integer> availableRows, ArrayList<Integer> selectedRows);
	
	public void delete(boolean delete);
}

