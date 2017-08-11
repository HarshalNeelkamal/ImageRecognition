import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class UserInterface extends Observable{
	
	JFrame mainFrame;
	TextArea field4;
	
	public UserInterface(){
		mainFrame = new JFrame("ImageRecognizer");
		mainFrame.setVisible(false);
		mainFrame.setSize(500, 700);
		buildInterface();
	}
	
	public void buildInterface(){
		JLabel label1 = new JLabel("Please paste a suitable URL or Brpowse a directory for your training set");
		TextField field1 = new TextField();
		JButton button1 = new JButton("Start training");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arg[] = {button1.getText(),field1.getText()};
				setChanged();
				notifyObservers(arg);
			}
		});
		JLabel label2 = new JLabel("Please paste a suitable path for the image that you desire to recognise");
		TextField field2 = new TextField();
		JButton button2 = new JButton("Predict");
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arg[] = {button2.getText(),field2.getText()};
				setChanged();
				notifyObservers(arg);
			}
		});
		JLabel label3 = new JLabel("Enter a search key");
		TextField field3 = new TextField();
		JButton button3 = new JButton("Find");
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String arg[] = {button2.getText(),field2.getText()};
				setChanged();
				notifyObservers(arg);
			}
		});
		field4 = new TextArea();
		field4.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(field4);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(10, 1));
		panel1.setBounds(0, 0, mainFrame.getWidth()/2, mainFrame.getHeight() - 10);
		panel1.add(label1);
		panel1.add(field1);
		panel1.add(button1);
		panel1.add(label2);
		panel1.add(field2);
		panel1.add(button2);
		panel1.add(label3);
		panel1.add(field3);
		panel1.add(button3);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 1));
		panel2.add(scrollPane);
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(2, 1));
		panel3.add(panel1);
		panel3.add(panel2);
		mainFrame.add(panel3);
	}
	
	public void addTextToResultsField(String s){
		System.out.println(s);
		field4.setText(s);
	}
	
	public void show(){
		mainFrame.setVisible(true);
	}
	
}
