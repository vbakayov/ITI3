package TradeViewer;

/*	Model.java
 *
 *	Basic model for tradeviewer spreadsheet data
 *	Based on Phil Gray's sourceDomain.java
 *
 *	@author matthew
 *
 *	Modified by Dobromir Dobrev - 1103606 
 *  to meet the specified requirements, such as
 *  multiple selection and filtering of the data.
 */
 
// Note: multiple selection is implemented by:
//    => replacing the method selected(row) with a method that adds each newly
//	     selected rows to an ArrayList of integers that stores all selected items
//       and updates all of the children with that ArrayList.
//    => changing the update() method in ViewController.java to take
//   	 an ArrayList of integers instead of with a single integer.
//    => modifying all classes that implement the ViewController interface so that 
//	     they work with an ArrayList of integers instead of with a single integer.

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Model {

    private ArrayList children;  /* set of subcomponent viewControllers */

    private ArrayList labels, /* attribute names */
            types, 			  /* attribute types (as strings) */
            dataset;		  /* attribute values (ArrayList of ArrayLists */
    
    // used for multiple selection - stores all selected rows
    private static ArrayList<Integer> selectedRows;
    // contains only the rows from the original dataset that pass the currently set filters -
    // filtering is achieved by making all views use this dataset instead of the default one
    private ArrayList<Integer> availableRows; 

    /* Constructor creates the main data structures and then loads data in 
     * the given file. After this, children should be null but the other 
     * ArrayLists should be filled with the initial data for analysis.
     */

    public Model(String filename) {
        children = new ArrayList();
        labels = new ArrayList();
        types = new ArrayList();
        dataset = new ArrayList();
        availableRows = new ArrayList();
        selectedRows = new ArrayList();
        
        load(filename);
        for(int row = 0; row < dataSize(); row++) availableRows.add(row);
    }

    /* 
     * addChild
     * Adds a new view-controller to the set of children of this model
     */
    void addChild(ViewController vc) {
        children.add(vc);
    }

    
    // a method to add an ArrayList of integers (representing the rows 
    // of the records in the dataset) to the ArrayList of selected rows
    public void select(ArrayList<Integer> rows) {
    	// add the rows to the ArrayList of selected rows
        for (int i = 0; i < rows.size(); i++) {
        	int row = rows.get(i);
        	// check if selection is out of range
        	if ((row < 0) | (row >= dataSize())) {
                System.err.println("TradeViewerFrame: selection out of range ("+ row + ")");
                return;
        	}
        	// add the row only if it is not already selected
	        if (!selectedRows.contains(row)) selectedRows.add(row);
		}
		// the children are updated
        for (int i = 0; i < children.size(); i++) {
            ViewController kid = (ViewController) children.get(i);
            kid.update(availableRows, selectedRows);
        }   
    }
    
    void load(String filename) {

    	//Create a file  from the xlsx/xls file
    	try{
        File f=new File(filename);
        String filetype = filename.substring(filename.lastIndexOf('.'), filename.length());
        System.out.println(filetype);
        switch(filetype){
			case ".csv":
				load_csv(filename);
				break;
			case ".xlsm":
				load_xlms(f);
				break;
			default :
				System.out.println("The Inputed format is not supported");

        }}

        catch(Exception e){
        	System.out.println("No file with such name");
        	e.printStackTrace();
        	}
        }
     
    	
    
    
    private void load_xlms(File filename) {
    	   //Create Workbook instance holding reference to .xlsx file
        Workbook workbook=null;
		try {
			workbook = WorkbookFactory.create(filename);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(workbook);

        //printing number of sheet avilable in workbook
        org.apache.poi.ss.usermodel.Sheet sheet=null;
        sheet = workbook.getSheetAt(0);
        
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext())
        {
        	ArrayList rowArray = new ArrayList();
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
           // Iterator<Cell> cellIterator = row.cellIterator();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
                rowArray.add(cell.toString());
                //Check the cell type and format accordingly
                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                      //  System.out.print(cell.getNumericCellValue() + "t");
                        break;
                    case Cell.CELL_TYPE_STRING:
                     //   System.out.print(cell.getStringCellValue() + "t");
                        break;
                }
            }
            dataset.add(rowArray);
        }
        
        
        
    	}
    	
		
	

	void load_csv(String filename){
      FileReader fileReader;
      BufferedReader buffer;
      String line;
      try {
          fileReader = new FileReader(new File(filename));
          buffer = new BufferedReader(fileReader);

          // read labels line
         // readHeader(buffer.readLine(), labels);
          // read the types line
         // readHeader(buffer.readLine(), types);
          while ((line = buffer.readLine()) != null) {
              // now read a line of data
              readALine(line);
          }
          // close the file
          fileReader.close();
      } catch (FileNotFoundException e) {
          System.out.println("File not found when loading graph");

      } catch (IOException e) {
          System.out.println("IO exception when loading graph");
      }
    }

    void readHeader(String line, ArrayList headers) {
        // create a tokenizer to parse the line
        StringTokenizer segmentedLine = new StringTokenizer(line, ",");
        // put the elements in the ArrayList Line
        while (segmentedLine.hasMoreTokens()) {
            headers.add(segmentedLine.nextToken());
        }
    }

    void readALine(String line) {
        // create an ArrayList to hold the elements
        ArrayList row = new ArrayList();
        // create a tokenizer to parse the line
        StringTokenizer segmentedLine = new StringTokenizer(line, ",");
        // put the elements in the ArrayList Line
        while (segmentedLine.hasMoreTokens()) {
            row.add(segmentedLine.nextToken());
        }
        // put the row into the dataset
        dataset.add(row);
    }

    /* accessing methods */
    public ArrayList labels() {
        return labels;
    }

    public ArrayList types() {
        return types;
    }

    /* return labels of fields which are reals or integers */
    public ArrayList numericFieldLabels() {
        ArrayList numericFields = new ArrayList();
        String typeElement;
        String labelElement;
        for (int i = 0; i < types.size(); i++) {
            typeElement = types.get(i).toString();

            if (typeElement.equals("REAL")) {
                numericFields.add(labels.get(i).toString());
            } else if (typeElement.equals("INT")) {
                numericFields.add(labels.get(i).toString());
            }
        }
        return numericFields;
    }

    /* return index of field with name 'fieldName' */
    public int indexOfField(String fieldName) {
        boolean found = false;
        int index = 0;
        while ((!found) && (index <= labels.size())) {
            if (labels.get(index).toString().equals(fieldName)) {
                found = true;
            } else {
                index = index + 1;
            }
        }
        if (found) {
            return index;
        } else {
            return -1;
        }
    }

    public int dataSize() {
        return dataset.size();
    }
    
    public ArrayList record(int row) {
        return (ArrayList) dataset.get(row);
    }
    
    // get the available rows - we need this when constructing the views
  	public ArrayList<Integer> getAvailableRows() {
  		return this.availableRows;
  	}
    
    // sets the available rows - enables filtering
	public void setAvailableRows(ArrayList<Integer>  rows) {
		this.availableRows = rows;
	}

    /* return real value of field in field 'fieldNumber' in row 'rowNumber' */
    public double fieldAsReal(int rowNumber, int fieldNumber) {
        ArrayList rowValue = (ArrayList) dataset.get(rowNumber);
        String fieldValue = (String) rowValue.get(fieldNumber);
        Double returnVal = new Double(fieldValue);
        return returnVal.doubleValue();
    }

    /* return real value of field in field 'fieldNumber' of row 'row' */
    public double fieldAsReal(ArrayList row, int fieldNumber) {
        String fieldValue = (String) row.get(fieldNumber);
        Double returnVal = new Double(fieldValue);
        return returnVal.doubleValue();
    }

    /* return ArrayList of all the record keys */
    public ArrayList ids() {
        ArrayList row;
        ArrayList idList = new ArrayList();
        ListIterator iter = dataset.listIterator();

        while (iter.hasNext()) {
            row = (ArrayList) iter.next();
            idList.add((String) row.get(11));
        }
        return idList;
    }

    /* printing methods */
    public void printDataset() {
        ArrayList row;
        ListIterator iter = dataset.listIterator();
        System.out.println("Printout of full dataset");
        System.out.println("++++++++++++++++++++++++");
        while (iter.hasNext()) {
            row = (ArrayList) iter.next();
            printRow(row);
        }
    }
    

    public void printRow(ArrayList row) {
        Iterator labelIter = labels.iterator();
        Iterator rowIter = row.iterator();

        System.out.println("***************************");
        while (rowIter.hasNext()) {
            System.out.println(labelIter.next().toString() + " = "
                    + rowIter.next().toString());
            System.out.println("***************************");
        }
    }
    
    public String getCountry(int index){
    	return ((ArrayList)dataset.get(index)).get(4).toString();
    }
    
    public String getNaima(int index){
    	return ((ArrayList)dataset.get(48)).get(2).toString();
    }

    public void printRow(int rowNumber) {
        if (rowNumber >= dataset.size()) {
            System.out.println("Error: Model.printRow(): lllegal row number");
        } else {
            printRow((ArrayList) dataset.get(rowNumber - 1));
        }
    }

}
