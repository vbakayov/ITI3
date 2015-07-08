package TradeViewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	public LogoStuff(Model model) {
		this.model= model;
		
		 setLayout(new GridBagLayout());
		 GridBagConstraints c = new GridBagConstraints();
		 initPictureLogo();
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.gridx = 0;
		 c.gridy = 0;
		 c.weighty = 10.0; 
		 add(picLabel,c);
		 
		 JPanel labelsText=new JPanel();
		 labelsText.setLayout(new GridLayout(1,2));
		 label = new JLabel("COUNT TOTAL: ");
		 count = new JLabel(Integer.toString(model.dataSize()));
		 
		
		 labelsText.add(label);
		 labelsText.add(count);
		 
		 
		 c.gridx = 0;
		 c.gridy = 1;
		 c.fill = GridBagConstraints.CENTER;
	     c.anchor = GridBagConstraints.PAGE_END;
		 
		 add(labelsText,c);
		 
		
	}
	
	
	
	private void initPictureLogo(){
		 BufferedImage myPicture = null;
			try {
				myPicture = ImageIO.read(new File("logo.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 picLabel = new JLabel(new ImageIcon(myPicture));
	}



	@Override
	public void update(ArrayList<Integer> availableRows,
			ArrayList<Integer> selectedRows) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void delete(boolean delete) {
		//System.out.println("NOTIFEEEEEEEED");
		//System.out.println(delete);
		 count.setText(Integer.toString(model.dataSize()));
		
	}
	
	


}
