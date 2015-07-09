package TradeViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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
	public HashMap <String,Integer> localAMap = new HashMap<String,Integer>();
	private HashMap<String, Integer> ageGroupsMap = new HashMap<String,Integer>();
	//hash map integer-integer(colomn intex, count) for the different cases
	private HashMap<String, Integer> casesMap = new HashMap<String,Integer>();
	
	//hash map for counting the barChart violance data
	private HashMap<String,TupleViolence> violenceMap = new HashMap<String,TupleViolence>();
	//indexes asalym applicaton-refugee status, asalym app-refused,asalym appeal- No appeal, asalym appeal- outstanding
	//fresh claim for asylum- No appeal,fresh claim for asylum- outstanding
	private int[] outcomesCount= new int[7];
	
	
	JPanel chartPanel=new JPanel();
	private PieChart countryPieChart;
	private PieChart ageGroupPieChart;
	private JButton saveButton;
	private JButton saveButtonAge;
	private JButton deleteButton;
	private JButton localAButton;
	private JButton outcomesButton;
	private JButton barChartButton;
	
	private JButton loadmoreData;
	
	
	public StatisticsPanel(Model model) {
		this.model = model;
		
		
		
		
		printMap();
		GUI();
		
	}
	

	

	
	private void getOutcomesCount() {
		outcomesCount=new int[7];
		int indexColAsylum= model.getIndexOfLabel("Asylum Application");
		int indexColAppeal= model.getIndexOfLabel("Asylum Appeal");
		int indexColFreshClaim= model.getIndexOfLabel("Fresh Claim for Asylum");
					
			for(int row= 0; row< model.dataSize(); row++){
				String Asylum  = model.getData(indexColAsylum,row);
				String Appeal  = model.getData(indexColAppeal,row);
				String FreshClaim  = model.getData(indexColFreshClaim,row);
				if(Asylum.equals("refugee status"))outcomesCount[0]++;
				if(Asylum.equals("refused")) outcomesCount[1]++;
				if(Appeal.equals("refused")) outcomesCount[2]++;
				if(Appeal.equals("no appeal")) outcomesCount[3]++;
				if(Appeal.equals("outstanding")) outcomesCount[4]++;
				if(FreshClaim.equals("no appeal")) outcomesCount[5]++;
				if(FreshClaim.equals("outstanding")) outcomesCount[6]++;
				
			}
			
			//for(int i=0;i<outcomesCount.length;i++){
				//System.out.println("Yet another print statement because we are cool "+outcomesCount[i]);
			//}
		
	}



	public void GUI(){
		 setLayout(new GridLayout(5,2));
		 JPanel labelsText1=new JPanel();
		 labelsText1.setLayout(new GridLayout(1,1));
		 JPanel labelsText2=new JPanel();
		 labelsText2.setLayout(new GridLayout(1,1));
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
	        JButton localAButton = new JButton("Local Autority");
	        JButton outcomesButton = new JButton("Cases Outcomes");
	        JButton barChartButton = new JButton("Bar Chart");
	        JButton loadmoreData = new JButton("Load Closed Cases");
	        
	        add(casesButton);
	        add(deleteButton);
	        add(localAButton);
	        add(outcomesButton);
	        add(barChartButton);
	        add(loadmoreData);
	        
	        
	        ageGroupButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed();
					   saveButtonAge.setEnabled(true);
					//countryButton.setEnabled(false);
					
				}
				
				private void buttonPressed() {
					populateMap();
					ageGroupPieChart= new PieChart("Age Groups",ageGroupsMap,false);
					setLayout(new BorderLayout());
					
				    // add("Center", demo);
					
					ageGroupPieChart.pack();
					RefineryUtilities.centerFrameOnScreen(ageGroupPieChart);
					ageGroupPieChart.setVisible(true);
					
				}
			
		
	        });
	        
	        localAButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed();
					
				}
				
				private void buttonPressed() {
					getLocalACount();
					String[] arrayColumn = {"Local Authority", "Count"};
					Table LocalATable	= new Table("Local Authority",model,localAMap,totalNumberCases(localAMap), arrayColumn);
					LocalATable.setVisible( true );
					
				}
			
		
	        });
	        
				
	        outcomesButton.addActionListener(new ActionListener(){
	        	@Override
				public void actionPerformed(ActionEvent e) {
	        		getOutcomesCount();
	        		OutcomesPanel outcomes = new OutcomesPanel (outcomesCount);
	        		outcomes.setVisible(true);
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
					//0 for yes 1 for no
					int returnValue = showConfurmBoxes();
					if(returnValue == 0){
						String absolutePath = showFileChooser() ;
						if(absolutePath != null){
							System.out.println("HEREEEE");
//							MyThread myRunnable = new MyThread(absolutePath);
//							Thread myThread = new Thread(myRunnable);
//							myThread.start();
							new GuiWorker(absolutePath).execute();
							

						
							System.out.println("Second");
							//myThread.start();
							System.out.println("AFTEEEER");
						
							//myThread.interrupt();
		
							
							
						
						
						}
					}
					
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
					populateMap();
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
					getCaseCount();
					String[] arrayColumn = {"Case Type","Count","Types of Cases/number of clients", "Types of Cases/total cases" };
					Table mainFrame	= new Table("Legal Cases",model,casesMap,totalNumberCases(casesMap), arrayColumn);
					mainFrame.setVisible( true );
					
					
				}
	        	
	        });
	        
	        barChartButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getViolenceCount();
					BarChart barChart=new BarChart(model,violenceMap,"Violence Bar Chart");
					barChart.pack();
					RefineryUtilities.centerFrameOnScreen(barChart);
					barChart.setVisible(true);
					
				}
			});
	        
	        
	       loadmoreData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("DataSize "+model.dataSize());
				String path = showFileChooser();
				if(path != null){
					model.load(path, false);
					model.refreshView();
					System.out.println("DataSize2 "+model.dataSize());
				}
				
			}
	    	   
	       });
	        
	}
	private void printMap() {
    	//iterate on  the menus
    	//Iterator it = countryCountMap.entrySet().iterator();
		Iterator it = violenceMap.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry pair = (Map.Entry)it.next();
    		
    		System.out.println(pair.getKey() + " = " + pair.getValue().toString());
    		
    	}
    	
		
	}

	private void populateMap(){
		 countryCountMap.clear();
		 ageGroupsMap.clear();
		
		for(int i =0; i< model.dataSize(); i++){
			String country = model.getCountry(i);
			String ageGroup = model.getAgeGroup(i);
			
			int count = countryCountMap.containsKey(country) ? countryCountMap.get(country) : 0;
			countryCountMap.put(country, count + 1);
			
			int count2 =  ageGroupsMap.containsKey(ageGroup) ?  ageGroupsMap.get(ageGroup) : 0;
			 ageGroupsMap.put(ageGroup, count2 + 1);
			 
		
		}
	}
	
	
	

	
	private void getLocalACount(){
		localAMap.clear();
		for (int column = 0 ; column <model.getDataType().size() ; column ++){
			if(model.getDataType().get(column).equals("Local")){
			for(int row= 0; row< model.dataSize(); row++){
				String local  = model.getData(column,row);
				if(local != ""){
					int count = localAMap.containsKey(local) ? localAMap.get(local) : 0;
					localAMap.put(local, count + 1);
				}
			}
			}
		}
	}

	
	
	//fixed 
	private void getCaseCount(){
		casesMap.clear();
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
	
	
	private void getViolenceCount(){
		violenceMap.clear();
		for (int column = 0 ; column <model.getDataType().size() ; column ++){
			if(model.getDataType().get(column).equals("Violence")){
			for(int row= 0; row< model.dataSize(); row++){
				String info = model.getData(column,row);
				String label = model.getLabels(column);
				if(!violenceMap.containsKey(label)) violenceMap.put(label, new TupleViolence(0,0));
				if(row == model.dataSize()-1 && !violenceMap.containsKey(label))	violenceMap.put(label, new TupleViolence(0,0));
				if(info.equals("YES")){
					
					violenceMap.get(label).incrementYes();
					violenceMap.put(label, violenceMap.get(label));
				}
				if(info.equals("NO")){
					violenceMap.get(label).incrementNo();
					violenceMap.put(label, violenceMap.get(label));
					
				}
			}
			}
		}
	}
	
	
	
	
	private int totalNumberCases(HashMap map){
		int total=0;
		Iterator it = map.entrySet().iterator();
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
    
    private int showConfurmBoxes(){
    	//Custom button text
    	int n;
    	Object[] options = {"Yes, please",
    	                    "No, thanks",
    	                    };
    	n = JOptionPane.showOptionDialog(null,
    	    "Are you sure you want to remove all rows you currently see?",
    	    "Verify",
    	    JOptionPane.YES_NO_CANCEL_OPTION,
    	    JOptionPane.QUESTION_MESSAGE,
    	    null,
    	    options,
    	    options[1]);
    	
    	return n;
    }

    private String showFileChooser() {
		
    	JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");

		int userSelection = fileChooser.showSaveDialog(chartPanel);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			return fileToSave.getAbsolutePath();
			
		}
		return null;
	}



	@Override
	public void delete(boolean delete) {
		
		
	}
	


class GuiWorker extends SwingWorker<Integer, Integer> {

	  /*
	  * This should just create a frame that will hold a progress bar until the
	  * work is done. Once done, it should remove the progress bar from the dialog
	  * and add a label saying the task complete.
	  */

	  private JFrame frame = new JFrame();
	  private JDialog dialog = new JDialog(frame, "Deletion in Progress", true);
	  private JProgressBar progressBar = new JProgressBar();
	private String absolutePath;


	  public GuiWorker(String absolutePath) {
		  this.absolutePath = absolutePath;
	    progressBar.setString("In Progress");
	    progressBar.setStringPainted(true);
	    progressBar.setIndeterminate(true);
	    dialog.getContentPane().add(progressBar);
	    dialog.setPreferredSize(new Dimension(200, 200));
	    dialog.setLocationRelativeTo(null);
	    dialog.setAlwaysOnTop(true);
	    dialog.pack();
	    dialog.setModal( false );
	    dialog.setVisible(true);
	  }

	  @Override
	  protected Integer doInBackground() throws Exception {
	    System.out.println( "GuiWorker.doInBackground" );
	    model.CopyRowXLSXFile(absolutePath);
		model.notifyDelete();
	    return 0;
	  }

	  @Override
	  protected void done() {
	    System.out.println("done");
	    JLabel label = new JLabel("Task Completed");
	    dialog.getContentPane().remove(progressBar);
	    dialog.getContentPane().add(label);
	    dialog.getContentPane().validate();
	    dialog.dispose();
	  }

	}
	
	

}



