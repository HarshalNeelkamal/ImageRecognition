import java.io.File;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import org.opencv.core.Core;

public class MainClass implements Observer{
	
	UserInterface i;
	Trainer t;
	
	public static void main(String args[]) {
		// TODO Auto-generated constructor stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MainClass obj = new MainClass();
		obj.run();
	}
	
	public void run(){
		i = new UserInterface();
		i.addObserver(this);
		i.show();
	}

	private void startTraining(String location){
		t = Trainer.getInstance();
		String path = location;
		File directory = new File(path);
		String sets[] = directory.list();
		for(int i = 0 ; i < sets.length; i ++){
			t.trainAtLocation(path+"\\"+sets[i],sets[i]);
		}
		t.trainingDone();
	}
	
	private void predict(String testpath){
		int data[] = t.getMeanRGB(testpath);
		if(data == null){
			System.out.println("invalid image path");
			return;
		}
		System.out.println("("+data[0]+", "+data[1]+", "+data[2]+", "+data[0]+")");
		HashMap<String,Integer> map = t.tree1.nearestNeighoubrsFor(data);
		String s = "";
		int count = 0;
		int max = 0;
		String result = "";
		for(String key : map.keySet()){
			count += map.get(key);
			if(map.get(key) > max){
				max = map.get(key);
				result = key;
			}
		}
		s = "prediction is: "+result+"\nacuraccy: "+((max*1.0)/count)*100+"%\n";
		for(String key : map.keySet()){
			s += "Object is "+key+" with probablity "+map.get(key)+"/"+count+"\n";
		}
		System.out.println(s);
		i.addTextToResultsField(s);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		String argument[] = (String[]) arg;
		switch (argument[0]) {
		case "Start training":
			startTraining(argument[1]);
			break;
		case "Predict":
			predict(argument[1]);
			break;
		case "Find":
			System.out.println("Finding "+argument[1]+"......");
			break;
		default:
			
			break;
		}
	}
	
}
