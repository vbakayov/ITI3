package TradeViewer;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PieChart extends JFrame  {

	private  HashMap <String,Integer> map;
	private  JFreeChart chart;
	private  boolean otherGroup;
	
	
	public PieChart(String title, HashMap<String,Integer> countryCountMap, boolean otherGroupp) {
		super(title);
		this.map=countryCountMap;	
		this.otherGroup = otherGroupp;
	
		JPanel frame = createDemoPanel();
		setContentPane(frame);
		
	}

private  PieDataset createDataset() {
	Iterator it = map.entrySet().iterator();
	DefaultPieDataset dataset = new DefaultPieDataset();
	int others=0;
	while (it.hasNext()) {
		Map.Entry pair = (Map.Entry)it.next();
		System.out.println(pair.getKey() + " = " + pair.getValue());
		System.out.println("Boolean "+ otherGroup);
		if (otherGroup){
			if((int)pair.getValue()<3){
				others=others+(int)pair.getValue();
			}
			else{
				dataset.setValue((Comparable) pair.getKey(),new Integer( (int) pair.getValue()));
			}
			dataset.setValue("Others", others);
		}
		else{
			
			dataset.setValue((Comparable) pair.getKey(),new Integer( (int) pair.getValue()));
		}
	}
    return dataset;        
}

/**
 * Creates a chart.
 * 
 * @param dataset  the dataset.
 * 
 * @return A chart.
 */
private  JFreeChart createChart(PieDataset dataset) {
    
     chart = ChartFactory.createPieChart(
        "Pie Chart Demo",  // chart title
        dataset,             // data
        true,               // include legend
        true,
        false
    );

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    plot.setNoDataMessage("No data available");
    plot.setCircular(false);
    plot.setLabelGap(0.02);
    PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
            "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
     
    return chart;
    
}

/**
 * Creates a panel for the demo (used by SuperDemo.java).
 * 
 * @return A panel.
 */
public  JPanel createDemoPanel() {
    JFreeChart chart = createChart(createDataset());
    return new ChartPanel(chart);
}

/**
 * Starting point for the demonstration application.
 *
 * @param args  ignored.
 * 
 */

public  void saveToFile(String path)
	    
	    {
		
	  BufferedImage image=chart.createBufferedImage(800, 600);
	     try {
	    	 
	    	 File outputfile = new File(path+".png");
	 	    ImageIO.write(image, "png", outputfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    }


}
