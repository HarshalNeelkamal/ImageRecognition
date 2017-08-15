import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class UserInterface extends Observable{
	
	JFrame mainFrame;
	JTextArea field4;
	JPanel panel3;
	JButton button1;
	JButton button2;
	JButton button3;
//	JButton previous;
//	JButton next;
	JPanel panel2;
	JComboBox<String> box;
	JLabel imageholder;
	TextField dataBase;
	
	private static UserInterface instance = null;
	
	private UserInterface(){
		mainFrame = new JFrame("ImageRecognizer");
		mainFrame.setVisible(false);
		mainFrame.setSize(500, 700);
		buildInterface();
	}
	
	public static UserInterface getInstance(){
		if(instance == null)
			instance = new UserInterface();
		return instance;
	}
	
	public void addGraphChart(HashMap<String, Integer> map){
		
		DefaultCategoryDataset dcd = new DefaultCategoryDataset();
		
		if(map == null){
			dcd.addValue(.1, "probablity1", "test1");
			dcd.addValue(.1, "probablity2", "test3");
			dcd.addValue(.1, "probablity3", "test2");
			dcd.addValue(.1, "probablity4", "test4");
		}else{
			int total = map.get("total");
			for(String key: map.keySet()){
				if(key.equals("total"))
					continue;
				dcd.addValue((map.get(key)*1.0)/total, key, key);
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("predictions", "fruits", "Probability", dcd, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setRangeGridlinePaint(Color.black);
		
		ChartPanel panel = new ChartPanel(chart);
		panel3.remove(1);
		panel3.add(panel,2,1);
		panel3.updateUI();
	}
	
	public void buildInterface(){
		JLabel label1 = new JLabel("  Please paste a suitable URL or Brpowse a directory for your training set");
		TextField field1 = new TextField();
		JButton browse1 = new JButton("Browse");
		button1 = new JButton("Start training");
		JPanel innerPanel1 = new JPanel();		
		SpringLayout layout = new SpringLayout();
		innerPanel1.setLayout(layout);
		innerPanel1.add(field1);
		innerPanel1.add(browse1);
		innerPanel1.add(button1);
		layout.putConstraint(SpringLayout.EAST, field1, -5, SpringLayout.WEST, browse1);
		layout.putConstraint(SpringLayout.EAST, browse1, -5, SpringLayout.WEST, button1);
		layout.putConstraint(SpringLayout.EAST, button1, -5, SpringLayout.EAST, innerPanel1);
		layout.putConstraint(SpringLayout.WEST, field1, 5, SpringLayout.WEST, innerPanel1);
		layout.putConstraint(SpringLayout.NORTH, field1, 1, SpringLayout.NORTH, innerPanel1);
		layout.putConstraint(SpringLayout.SOUTH, field1, -7, SpringLayout.SOUTH, innerPanel1);
		layout.putConstraint(SpringLayout.NORTH, browse1, 1, SpringLayout.NORTH, innerPanel1);
		layout.putConstraint(SpringLayout.SOUTH, browse1, -7, SpringLayout.SOUTH, innerPanel1);
		layout.putConstraint(SpringLayout.NORTH, button1, 1, SpringLayout.NORTH, innerPanel1);
		layout.putConstraint(SpringLayout.SOUTH, button1, -7, SpringLayout.SOUTH, innerPanel1);
		
		JLabel label2 = new JLabel("  Please paste a suitable path for the image that you desire to recognise");
		TextField field2 = new TextField();
		JButton browse2 = new JButton("Browse");
		button2 = new JButton("Predict");
		button2.setEnabled(false);
		JPanel innerPanel2 = new JPanel();
		innerPanel2.setLayout(layout);
		innerPanel2.add(field2);
		innerPanel2.add(browse2);
		innerPanel2.add(button2);
		layout.putConstraint(SpringLayout.EAST, field2, -5, SpringLayout.WEST, browse2);
		layout.putConstraint(SpringLayout.EAST, browse2, -5, SpringLayout.WEST, button2);
		layout.putConstraint(SpringLayout.EAST, button2, -5, SpringLayout.EAST, innerPanel2);
		layout.putConstraint(SpringLayout.WEST, field2, 5, SpringLayout.WEST, innerPanel2);
		layout.putConstraint(SpringLayout.NORTH, field2, 1, SpringLayout.NORTH, innerPanel2);
		layout.putConstraint(SpringLayout.SOUTH, field2, -7, SpringLayout.SOUTH, innerPanel2);
		layout.putConstraint(SpringLayout.NORTH, browse2, 1, SpringLayout.NORTH, innerPanel2);
		layout.putConstraint(SpringLayout.SOUTH, browse2, -7, SpringLayout.SOUTH, innerPanel2);
		layout.putConstraint(SpringLayout.NORTH, button2, 1, SpringLayout.NORTH, innerPanel2);
		layout.putConstraint(SpringLayout.SOUTH, button2, -7, SpringLayout.SOUTH, innerPanel2);
		
		JLabel label3 = new JLabel("  Enter a search key");
		String options[] = {"apples", "bananas", "kiwi", "peaches", "Oranges", "Pineapple"};
		box = new JComboBox<String>(options);
		box.setSelectedItem(0);
		button3 = new JButton("Find");
		button3.setEnabled(false);
		JPanel innerPanel3 = new JPanel();
		innerPanel3.setLayout(layout);
		innerPanel3.add(box);
		innerPanel3.add(button3);
		layout.putConstraint(SpringLayout.EAST, box, -5, SpringLayout.WEST, button3);
		layout.putConstraint(SpringLayout.EAST, button3, -5, SpringLayout.EAST, innerPanel3);
		layout.putConstraint(SpringLayout.WEST, box, 5, SpringLayout.WEST, innerPanel3);
		layout.putConstraint(SpringLayout.NORTH, box, 1, SpringLayout.NORTH, innerPanel3);
		layout.putConstraint(SpringLayout.SOUTH, box, -7, SpringLayout.SOUTH, innerPanel3);
		layout.putConstraint(SpringLayout.NORTH, button3, 1, SpringLayout.NORTH, innerPanel3);
		layout.putConstraint(SpringLayout.SOUTH, button3, -7, SpringLayout.SOUTH, innerPanel3);
		
		JLabel label4 = new JLabel("  Please paste a suitable URL or Browse a directory for your image Data Base");
		dataBase = new TextField();
		JButton browse3 = new JButton("Browse");
		JPanel innerPanel4 = new JPanel();		
		innerPanel4.setLayout(layout);
		innerPanel4.add(dataBase);
		innerPanel4.add(browse3);
		layout.putConstraint(SpringLayout.EAST, dataBase, -5, SpringLayout.WEST, browse3);
		layout.putConstraint(SpringLayout.EAST, browse3, -5, SpringLayout.EAST, innerPanel4);
		layout.putConstraint(SpringLayout.WEST, dataBase, 5, SpringLayout.WEST, innerPanel4);
		layout.putConstraint(SpringLayout.NORTH, dataBase, 1, SpringLayout.NORTH, innerPanel4);
		layout.putConstraint(SpringLayout.SOUTH, dataBase, -7, SpringLayout.SOUTH, innerPanel4);
		layout.putConstraint(SpringLayout.NORTH, browse3, 1, SpringLayout.NORTH, innerPanel4);
		layout.putConstraint(SpringLayout.SOUTH, browse3, -7, SpringLayout.SOUTH, innerPanel4);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(button1.getText().equals("Reset")){
					String arg[] = {button1.getText(),field1.getText()};
					setChanged();
					notifyObservers(arg);
					button1.setText("Start training");
					toggleEnabledButton();
				}else{
					String arg[] = {button1.getText(),field1.getText()};
					setChanged();
					notifyObservers(arg);
				}
			}
		});
		browse1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\Harshal\\Desktop\\CSYE6205");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = chooser.showOpenDialog(button1);
				if (choice != JFileChooser.APPROVE_OPTION) return;
				File chosenFile = chooser.getSelectedFile();
				String path = chosenFile.getPath();
				field1.setText(path);
			}
		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arg[] = {button2.getText(),field2.getText()};
				setChanged();
				notifyObservers(arg);
			}
		});
		browse2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\Harshal\\Desktop\\CSYE6205\\RandomImages");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int choice = chooser.showOpenDialog(button2);
				if (choice != JFileChooser.APPROVE_OPTION) return;
				File chosenFile = chooser.getSelectedFile();
				String path = chosenFile.getPath();
				field2.setText(path);
			}
		});
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arg[] = {button3.getText(),box.getSelectedItem().toString()};
				setChanged();
				notifyObservers(arg);
			}
		});
		browse3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\Harshal\\Desktop\\CSYE6205");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int choice = chooser.showOpenDialog(mainFrame);
				if (choice != JFileChooser.APPROVE_OPTION) return;
				File chosenFile = chooser.getSelectedFile();
				String path = chosenFile.getPath();
				dataBase.setText(path);
			}
		});
		
		
		field4 = new JTextArea();
		field4.setEditable(false);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1, 1));
		JPanel panel2inner = new JPanel();
		panel2inner.setLayout(new GridLayout(1, 1));
		panel2inner.add(field4,0,0);
		imageholder = new JLabel();
				
		panel2.add(panel2inner);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(8, 1));
		panel1.setBounds(0, 0, mainFrame.getWidth()/2, mainFrame.getHeight() - 10);
		panel1.add(label1);
		panel1.add(innerPanel1);
		panel1.add(label2);
		panel1.add(innerPanel2);
		panel1.add(label4);
		panel1.add(innerPanel4);
		panel1.add(label3);
		panel1.add(innerPanel3);

		
		panel3 = new JPanel();
		panel3.setLayout(new GridLayout(3, 1));
		panel3.add(panel1,0,0);
		panel3.add(new JPanel(),2,1);
		panel3.add(panel2,1,2);
		mainFrame.add(panel3);
		addGraphChart(null);
	}
	
	private void toggleEnabledButton(){
		if(!button2.isEnabled()){
			button2.setEnabled(true);
			button3.setEnabled(true);
		}else{
			button2.setEnabled(false);
			button3.setEnabled(false);
		}
	}
	
	public void trainingSuccessfull(ArrayList<String> list){
		changeSelectionOptions(list);
		toggleEnabledButton();
		button1.setText("Reset");
	}
	
	private void changeSelectionOptions(ArrayList<String> list){
		box.removeAllItems();
		for(int i = 0; i < list.size(); i++){
			box.addItem(list.get(i));
		}
	}
	
	public void addTextToResultsField(String s){
		
		panel2.removeAll();
		JPanel panel2inner = new JPanel();
		panel2inner.setLayout(new GridLayout(1, 1));
		panel2inner.add(field4,0,0);		
		panel2.add(panel2inner);
		field4.setText(s);
		imageholder.setIcon(null);
	}
	
	public void addTextAndImageToResultsField(String s, String loc){
		
		panel2.removeAll();
		JPanel panel2inner = new JPanel();
		panel2inner.setLayout(new GridLayout(1, 2));
		panel2inner.add(field4,0,0);		
		panel2inner.add(imageholder, 1, 1);
		panel2.add(panel2inner);
		
		field4.setText(s);
		if(!loc.endsWith(".jpg")){
			return;
		}
		Mat img = Imgcodecs.imread(loc);
		Image image = mat2Img(img,  panel2.getWidth()/2, panel2.getHeight());
		ImageIcon icon = new ImageIcon(image);
		imageholder.setIcon(icon);
		panel2.repaint();
		panel2inner.repaint();
	}
	
	public void addFoundImages(ArrayList<String> list){
		panel2.removeAll();
		JPanel panel2inner = new JPanel();
		panel2inner.setLayout(new GridLayout(2, 5));
		panel2.add(panel2inner);
		
		for(int i = 0; i < list.size(); i++){
			Mat img = Imgcodecs.imread(list.get(i));
			JLabel tempLab = new JLabel();
			panel2inner.add(tempLab, 0, 0);
			Image image = mat2Img(img, panel2.getWidth()/5, panel2.getHeight()/2);
			ImageIcon icon = new ImageIcon(image);
			tempLab.setIcon(icon);
		}
			
		panel2.repaint();
		panel2inner.repaint();
	}
	
	private BufferedImage mat2Img(Mat in, int width, int height)
    {
        BufferedImage out;
        Imgproc.cvtColor(in, in, Imgproc.COLOR_BGR2RGB);
        Imgproc.resize(in, in, new Size(width, height));
        byte[] data = new byte[width * height * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(width, height, type);

        out.getRaster().setDataElements(0, 0, width, height, data);
        return out;
    } 
	
	public void alertWithMsg(String msg){
		JOptionPane.showMessageDialog(mainFrame, msg);
	}
	
	public void show(){
		mainFrame.setVisible(true);
	}
	
}
