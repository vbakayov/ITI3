package TradeViewer;

/*	Model.java
 *
 *	Basic model for tradeviewer spreadsheet data
 *	Based on Phil Gray's sourceDomain.java
 *
 *	@author matthew
 *
 *
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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alee.utils.FileUtils;

import java.util.*;

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

	private JTable recordTable;
	org.apache.poi.ss.usermodel.Sheet sheet=null;

	public XSSFSheet sheetHSSF;
	
	private ArrayList<String> dataType;
	private String filename;

	private MessageInfo messageGui;
	//private static int numberofTimesRemoved;

	

    /* Constructor creates the main data structures and then loads data in 
     * the given file. After this, children should be null but the other 
     * ArrayLists should be filled with the initial data for analysis.
     */

    public Model(String filename) {
        children = new ArrayList();
        labels = new ArrayList();
        types = new ArrayList();
        dataset = new ArrayList();
        dataType=new ArrayList();
        availableRows = new ArrayList();
        selectedRows = new ArrayList();
        messageGui= new MessageInfo(null);
       
        
        load(filename,true);
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
    	//System.out.println("FROM MODEL "+ rows.size());
    	selectedRows.clear();
    	
    
    
		// the children are updated
        for (int i = 0; i < children.size(); i++) {
            ViewController kid = (ViewController) children.get(i);
            kid.update(availableRows, selectedRows);
        }   
    }
    
    void load(String filename, Boolean addLabels) {

    	//Create a file  from the xlsx/xls file
    	try{
        
        String filetype = filename.substring(filename.lastIndexOf('.'), filename.length());
        System.out.println(filetype);
        switch(filetype){
			case ".xlsm":
				load_xlms(filename,addLabels);
				break;
			default :
				System.out.println("The Inputed format is not supported");

        }}

        catch(Exception e){
        	System.out.println("No file with such name");
        	e.printStackTrace();
        	}
        }
     
    	
    
    
    protected void load_xlms(String filename2, Boolean addLabels) {
 	   //Create Workbook instance holding reference to .xlsx file
    	short max = 0;
    	FileInputStream file = null;
    	this.filename= filename2;
        Workbook workbook=null;
        boolean isHeader=true;
        long isType=0;
		try {
			
			workbook = WorkbookFactory.create(new File(filename2));
		
			 //Get the workbook instance for XLS file 
			file = new FileInputStream(filename2);
			
			 XSSFWorkbook workbookHSSF = new XSSFWorkbook(file);
		    //Get first sheet from the workbook
		       sheetHSSF = workbookHSSF.getSheetAt(0);
	
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(workbook);

        //printing number of sheet avilable in workbook
        //org.apache.poi.ss.usermodel.Sheet sheet=null;
        sheet = workbook.getSheetAt(0);
        removeTrailing(sheet);
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext())
        {
        	ArrayList rowArray = new ArrayList();
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
           // Iterator<Cell> cellIterator = row.cellIterator();
            short  current =  row.getLastCellNum();
            //IMPORTANT!!! COLUMNS MUST BE NAMED OR THIS WILL FAIL
            max = (max > current) ? max :  current;
            for(int cn=0; cn< max; cn++) {
            	//System.out.println(row.getLastCellNum());
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
                if (isHeader && addLabels ){
                	labels.add(cell.toString());
                }
                if(isType==1 && addLabels) dataType.add(cell.toString());
                else{
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
        }
            if(!isHeader && isType!=1) 
            	dataset.add(rowArray);
            isHeader= false;
            isType++;
        }
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
    
    public String getAgeGroup(int index){
    	return ((ArrayList)dataset.get(index)).get(3).toString();
    }
    

    public void printRow(int rowNumber) {
        if (rowNumber >= dataset.size()) {
            System.out.println("Error: Model.printRow(): lllegal row number");
        } else {
            printRow((ArrayList) dataset.get(rowNumber - 1));
        }
    }

	public String getData(int column, int row) {
		return ((ArrayList)dataset.get(row)).get(column).toString();
	}
	
	public String getLabels(int index){
		//System.out.println(labels.size()+"   "+ index);
		//System.out.println(labels.get(index));
		return labels.get(index).toString();
	}

	public ArrayList getLabel(){
		return labels;
	}
	
	public int getIndexOfLabel(String itemName)
	{
	    for (int i = 0; i < labels.size(); i++)
	    {
	   
	    	String string = labels.get(i).toString();
	       if (itemName.equals(string)){
	        	//System.out.println("INDEX OF"+ itemName + labels.get(i));
	            return i;
	        }
	    }

	    return -1;
	}
	public ArrayList getData(){
		return dataset;
	}

	public void setTable(JTable recordTable) {
		this.recordTable = recordTable;
		
	}
	public JTable getTable(){
		return recordTable;
	}
	
	private void removeTrailing( Sheet sheet){
			
		  boolean stop = false;
          boolean nonBlankRowFound;
          short c;
          Row lastRow = null;
          Cell cell = null;

          while (stop == false) {
              nonBlankRowFound = false;
              lastRow = sheet.getRow(sheet.getLastRowNum());
              for (c = lastRow.getFirstCellNum(); c <= lastRow.getLastCellNum(); c++) {
                  cell = lastRow.getCell(c);
                  if (cell != null && lastRow.getCell(c).getCellType() != HSSFCell.CELL_TYPE_BLANK) {
                      nonBlankRowFound = true;
                  }
              }
              if (nonBlankRowFound == true) {
                  stop = true;
              } else {
                  sheet.removeRow(lastRow);
              }
          }
	}
	
	public void CopyRowXLSXFile (String absolutePath){
		
		FileInputStream output;
	
		try {
			output = new FileInputStream(new File(absolutePath));
		
			//open exel document	
		XSSFWorkbook workbook2 = new XSSFWorkbook(output); 
		XSSFSheet worksheet2 = workbook2.getSheetAt(0);
		removeTrailing(worksheet2);
		int lastRowNumCopy = worksheet2.getLastRowNum();
		System.out.println("Last "+lastRowNumCopy);
		
		//copy the information from the avaiable rows to the output file
		for( int rowIndex= 0 ; rowIndex< availableRows.size(); rowIndex++){
			XSSFRow row2 =worksheet2.createRow(lastRowNumCopy+rowIndex+1);
			System.out.println("DATASET SIZE"+dataset.size());
			for(int column =0; column<labels.size(); column++){   		  				
    			String value = (String) (((ArrayList) dataset.get(availableRows.get(rowIndex))).get(column));   		
    			row2.createCell(column).setCellValue(value);
    			System.out.println("CELL IS "+row2.getCell(column).toString());;
	
    		}
    	}
		
		Collections.sort(availableRows);
	
//		

    	FileOutputStream out = 
                new FileOutputStream(new File(absolutePath));
        workbook2.write(out);
        
    	//remove rows from the spreadsheet original data 
		for( int row= availableRows.size()-1 ; row>= 0; row--)
			RemoveROwwriteXLSXFile(availableRows.get(row)+2);
     
        out.close();
        System.out.println("Excel written successfully..");
    	
        //remove the available rows from the programs' data model
        removeRowsFromDataSet();
		//clear all available rows
        availableRows.clear();
        
        // update all children
        for (int i = 0; i < children.size(); i++) {
            ViewController kid = (ViewController) children.get(i);
            kid.update(availableRows, selectedRows);
        }   
		
			}
    	catch (FileNotFoundException e) {
			if(e.getMessage().contains("The process cannot access the file because it is being used by another process")){
				MessageInfo.processMessage("Cannot access the file because it is being used by another process- probably Excel. Close Excel and try again");}
			else if(e.getMessage().contains("The system cannot find the file specified"))
				{MessageInfo.processMessage("The system cannot find the file specified");}
		} catch (IOException e) {
			System.out.println("Viktor '>>>' StackOverflow");
			e.printStackTrace();
		}
		
	}
	
	

	public void removeRowsFromDataSet() {
		int numberofTimesRemoved =  0;
		for( int rowIndex= 0 ; rowIndex< availableRows.size(); rowIndex++){
		System.out.println("INDEDEX" + rowIndex);
//		dataset.remove(integer-numberofTimesRemoved);
		
		dataset.remove((int) availableRows.get(rowIndex)-numberofTimesRemoved);
		numberofTimesRemoved++;
		System.out.println("SIZEEEE IS  "+ dataset.size());
		for(int i =0 ; i<availableRows.size(); i++){
			System.out.println("Index of available rows"+ availableRows.get(i));
		}
		//select(new ArrayList());
		}
	}

	public void RemoveROwwriteXLSXFile( int rowIndex) {
		 //Read Excel document first
		try{
        FileInputStream input_document = new FileInputStream(new File(filename)); 
        // convert it into a POI object
        XSSFWorkbook my_xlsx_workbook = new XSSFWorkbook(input_document); 
      
        // Read excel sheet that needs to be updated
        XSSFSheet my_worksheet = my_xlsx_workbook.getSheetAt(0);
        // declare a Cell object
     //   for( int row= availableRows.size()-1 ; row>= 0; row--){
        
        	System.out.println("REMOVING ROW"+ rowIndex);
        	int lastRowNum=my_worksheet.getLastRowNum();
    	
        	if(rowIndex>=0&&rowIndex<lastRowNum){
        		my_worksheet.shiftRows(rowIndex+1,lastRowNum, -1);
        	
        }
        	if(rowIndex==lastRowNum){
        		Row  removingRow=my_worksheet.getRow(rowIndex);
        		if(removingRow!=null){
        			my_worksheet.removeRow(removingRow);
            }
        }
        
       // }
        //important to close InputStream
        input_document.close();
        //Open FileOutputStream to write updates
        FileOutputStream output_file =new FileOutputStream(new File(filename));
       
        //write changes
        my_xlsx_workbook.write(output_file);
        //close the stream
        output_file.close(); 
        System.out.println("SUCEESS");
        
		}
//		catch (FileNotFoundException e) {
//			if(e.getMessage().contains("The process cannot access the file because it is being used by another process")){
//				MessageInfo.processMessage("Cannot access the file because it is being used by another process- probably Excel. Close Excel and try again");}
//			else if(e.getMessage().contains("The system cannot find the file specified"))
//				{MessageInfo.processMessage("The system cannot find the file specified");}
//		}
			catch(IOException e){
			e.printStackTrace();
		}
	}
	 public ArrayList getDataType(){
			return dataType;
		}
	 
	 public int getPosition(String type){
			int index=0;
			for(int i=0;i<dataType.size();i++){
				if(type.equals(dataType.get(i))) index = i;
					}
			return index;
		}

	public void notifyDelete() {
		 for (int i = 0; i < children.size(); i++) {
	            ViewController kid = (ViewController) children.get(i);
	            kid.delete(true);
	        }   
		
	}
	
	public void refreshView(){
		availableRows.clear();
		for(int row = 0; row < dataSize(); row++) availableRows.add(row);
		select(new ArrayList());
	    
		for (int i = 0; i < children.size(); i++) {
            ViewController kid = (ViewController) children.get(i);
            kid.delete(true);
		}
		
	}
	
	
}
