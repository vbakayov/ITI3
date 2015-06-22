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

public class PieChart extends ApplicationFrame  {

	private static HashMap <String,Integer> countryCountMap;
	
	
	public PieChart(String title, HashMap<String,Integer> countryCountMap) {
		super(title);
		this.countryCountMap=countryCountMap;		
		setContentPane(createDemoPanel());
	}

private static PieDataset createDataset() {
	Iterator it = countryCountMap.entrySet().iterator();
	DefaultPieDataset dataset = new DefaultPieDataset();
	while (it.hasNext()) {
		Map.Entry pair = (Map.Entry)it.next();
		System.out.println(pair.getKey() + " = " + pair.getValue());	
		dataset.setValue((Comparable) pair.getKey(),new Integer( (int) pair.getValue()));
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
private static JFreeChart createChart(PieDataset dataset) {
    
    JFreeChart chart = ChartFactory.createPieChart(
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
        /*OutputStream output=null;
        try {
			output= new FileOutputStream("H:\\output.png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			ChartUtilities.writeChartAsPNG(output,chart,600, 800);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
     BufferedImage image=chart.createBufferedImage(500, 600);
     try {
		saveToFile(image);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return chart;
    
}

/**
 * Creates a panel for the demo (used by SuperDemo.java).
 * 
 * @return A panel.
 */
public static JPanel createDemoPanel() {
    JFreeChart chart = createChart(createDataset());
    return new ChartPanel(chart);
}

/**
 * Starting point for the demonstration application.
 *
 * @param args  ignored.
 * 
 */

public static void saveToFile(BufferedImage img)
	    throws FileNotFoundException, IOException
	    {

	    File outputfile = new File("C:\\Sample.png");
	    ImageIO.write(img, "png", outputfile);
	    }


}
