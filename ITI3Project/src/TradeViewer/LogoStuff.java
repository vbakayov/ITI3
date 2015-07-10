package TradeViewer;

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

	public LogoStuff(Model model) {
		this.model= model;
		
		 setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
		 initPictureLogo();
		 calculateGenderCount();
		 
		 
		 c.gridx = 1;
		 c.gridy = 0;
		 c.weighty=1.0;
		 c.gridwidth = 2; 
		
		 add(picLabel,c);
		 
		 JPanel labelsText=new JPanel();
		 labelsText.setLayout(new GridLayout(1,2));
		 label = new JLabel("COUNT TOTAL: ");
		 count = new JLabel(Integer.toString(model.dataSize()));
		 
		
		 labelsText.add(label);
		 labelsText.add(count);
		 
		 
		 c.gridx = 2;
		 c.gridy = 1;
		 c.gridwidth = 2;
		 //c.fill = GridBagConstraints.CENTER;
		 
		 add(labelsText,c);
		
		 JPanel labelsFemale=new JPanel();
		 labelsFemale.setLayout(new GridLayout(1,2));
		 
		 JPanel labelsMale=new JPanel();
		 labelsMale.setLayout(new GridLayout(1,2));
		 JLabel label2= new JLabel("Female: ");
		 labelsFemale.add(label2);
		 femaleTextField = new JLabel(Integer.toString(genderCount[1]));
		 labelsFemale.add(femaleTextField);
		 
		c.gridx=0;
		c.gridy=2;
		add(labelsFemale,c);
		

		JLabel label3= new JLabel("Male: ");
		labelsMale.add(label3);
		maleTextField = new JLabel(Integer.toString(genderCount[0]));
		labelsMale.add(maleTextField);
		
		JLabel label4 = new JLabel("       ");
		c.gridx=2;
		add(label4,c);
		
		c.gridx=3;
		c.gridy=2;
		c.fill = GridBagConstraints.EAST;
		
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
		
		 count.setText(Integer.toString(model.dataSize()));
		 calculateGenderCount();
		femaleTextField.setText(Integer.toString(genderCount[1]));
		maleTextField.setText(Integer.toString(genderCount[0]));
		
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
