package TradeViewer;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
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
    //final PieChart demo = new PieChart("Pie Chart Demo 4");
    //demo.pack();
   // RefineryUtilities.centerFrameOnScreen(demo);
   // demo.setVisible(true);




}