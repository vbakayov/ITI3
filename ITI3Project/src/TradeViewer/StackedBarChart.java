package TradeViewer;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.DataUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class StackedBarChart extends JFrame {

    private ArrayList<Map> outcomesCategories;
	private static String title;

	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     * @param outcomesCategories 
     */
    public StackedBarChart(String title, ArrayList<Map> outcomesCategories) {
        super(title);
        this.outcomesCategories= outcomesCategories;
        this.title= title;
        JPanel chartPanel = createDemoPanel();
        setContentPane(chartPanel);
       
    }

    /**
     * Creates a sample dataset.
     *
     * @return a sample dataset.
     */
    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String category= "";
        
        
        for(int i =0; i< outcomesCategories.size();i++){
        	
        	switch(i){
        	case 0:
        		category= "Asylum Application";
        		break;
        	case 1:
        		category= "Asylum Appeal";
        		break;
        	case 2:
        		category= "Fresh Claim for Asylum";
        		break;
        	}
        	
        	Map<String,Integer> caseMap = outcomesCategories.get(i);
        	Iterator it = caseMap.entrySet().iterator();
        	while (it.hasNext()) {
        		Map.Entry pair = (Map.Entry)it.next();
        		System.out.println("INDEX "+ i + "Key  "+ pair.getKey()+ "Value "+ pair.getValue());
        		dataset.addValue(new Integer( (int) pair.getValue()), (Comparable) pair.getKey(), category);
        		
        }
        }
        
//        dataset.addValue(32.4, "Series 1", "Category 1");
//        dataset.addValue(17.8, "Series 2", "Category 1");
//        dataset.addValue(27.7, "Series 3", "Category 1");
//        dataset.addValue(43.2, "Series 1", "Category 2");
//        dataset.addValue(15.6, "Series 2", "Category 2");
//        dataset.addValue(18.3, "Series 3", "Category 2");
//        dataset.addValue(23.0, "Series 1", "Category 3");
//        dataset.addValue(11.3, "Series 2", "Category 3");
//        dataset.addValue(25.5, "Series 3", "Category 3");
//        dataset.addValue(13.0, "Series 1", "Category 4");
//        dataset.addValue(11.8, "Series 2", "Category 4");
//        dataset.addValue(29.5, "Series 3", "Category 4");
        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset for the chart.
     *
     * @return a sample chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createStackedBarChart(
            title,  // chart title
            "Category",                  // domain axis label
            "Value",                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            true,                        // legend
            true,                        // tooltips
            false                        // urls
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator());
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
    
    

    
  
}

