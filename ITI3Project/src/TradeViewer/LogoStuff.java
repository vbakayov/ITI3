package TradeViewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;




import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class LogoStuff extends JPanel implements ViewController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel picLabel;
	private JLabel label;
	private JLabel count;
	private Model model;
	private JLabel femaleTextField;
	private JLabel maleTextField;
	//count gender
		private int[] genderCount = new int [2];
		private JLabel available;

	public LogoStuff(Model model) {
		this.model= model;
		
		 setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
		 initPictureLogo();
		 calculateGenderCount();
		 
		 
		 c.gridx = 1;
		 c.gridy = 0;
		 c.weighty=1.0;
		 c.gridwidth = 3; 
		
		 add(picLabel,c);
		 
		 count = new JLabel("TOTAL: "+ Integer.toString(model.dataSize()));
		 count.setFont(new Font("Helvetica", Font.BOLD, 16));
		 count.setForeground(Color.darkGray);

		 c.gridx = 1;
		 c.gridy = 1;
		 c.fill = GridBagConstraints.CENTER;
		 add(count,c);
		 
		 JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.weightx=1;
		 c.gridy = 2;
		 add(sep,c);
		
		 
		available = new JLabel("Current: "+ Integer.toString(model.getAvailableRows().size()));
		c.fill = GridBagConstraints.CENTER;
		c.gridy = 3;
		add(available,c);
	
		 femaleTextField = new JLabel("Female: "+ Integer.toString(genderCount[1])+ "   ");
		 femaleTextField.setFont(new Font("Courier New", Font.PLAIN, 14));
	
		
		
		maleTextField = new JLabel( "   "+ "Male: " + Integer.toString(genderCount[0]));
		maleTextField.setFont(new Font("Courier New", Font.PLAIN, 14));  
		
		JPanel labelsMale=new JPanel();
		 labelsMale.setLayout(new GridLayout(1,2));
		labelsMale.add(femaleTextField);
		labelsMale.add(maleTextField);
		
//		JLabel label4 = new JLabel("       ");
//		c.gridx=2;
//		add(label4,c);
		
		c.gridx=1;
		c.gridy=4;
		
		add(labelsMale,c);
		
		
		
	}
	
	
	
	private void initPictureLogo(){
		 ClassLoader loader = this.getClass().getClassLoader();
		 picLabel = new JLabel(new ImageIcon(loader.getResource("logo.png")));
	}



	@Override
	public void update(ArrayList<Integer> availableRows,
			ArrayList<Integer> selectedRows) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void delete(boolean delete) {
		
		 count.setText("TOTAL: "+ Integer.toString(model.dataSize()));
		 calculateGenderCount();
		femaleTextField.setText("Female: "+Integer.toString(genderCount[1]));
		maleTextField.setText("   "+ "Male: " +Integer.toString(genderCount[0]));
		
	}
	
	
	private void calculateGenderCount(){
		 int indexGenderColumn = model.getIndexOfLabel("Gender");
		 genderCount = new int[2];
		 System.out.println(indexGenderColumn);
		for(int i =0; i< model.dataSize(); i++){
			String gender = model.getData(indexGenderColumn, i);
			if(gender.equals("Male"))  genderCount[0] ++;
			if(gender.equals("Female")) genderCount[1]++;
			
		}
		
	}
	



}
