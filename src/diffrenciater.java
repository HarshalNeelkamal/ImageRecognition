import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.omg.CORBA.PUBLIC_MEMBER;

public class diffrenciater extends JPanel{

	private static final long serialVersionUID = -3074384754503776057L;

	
	
	public void paint(Graphics g){
		
	}
	
	private void draw(Graphics g){
		JButton browseImage1 = new JButton("Browse");
		JButton browseImage2 = new JButton("Browse");
		JPanel imagePanel1 = new JPanel();
		JPanel imagePanel2 = new JPanel();
		imagePanel1.setPreferredSize(new Dimension(this.getWidth()/3, this.getHeight()/3));
		imagePanel2.setPreferredSize(new Dimension(this.getWidth()/3, this.getHeight()/3));

	}
	
}
