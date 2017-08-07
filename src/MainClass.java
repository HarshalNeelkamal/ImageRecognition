import java.io.File;
import java.util.Scanner;

import org.opencv.core.Core;

public class MainClass {
	
	public static void main(String args[]) {
		// TODO Auto-generated constructor stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MainClass obj = new MainClass();
		obj.run();
	}
	
	public void run(){
		Trainer t = Trainer.getInstance();
		String path = "C:\\Users\\Harshal\\Desktop\\CSYE6205\\trainingSet";
		File directory = new File(path);
		String sets[] = directory.list();
		for(int i = 0 ; i < sets.length; i ++){
			t.trainAtLocation(path+"\\"+sets[i],sets[i]);
		}
		t.trainingDone();
		while(true){
			System.out.println("give an image path");
			Scanner s = new Scanner(System.in);
			String testpath = s.next();
			int data[] = t.getMeanRGB(testpath);
			System.out.println("("+data[0]+", "+data[1]+", "+data[2]+", "+data[0]+")");
			KDTree.returnInstance().nearestNeighoubrsFor(data);
		}
		
	}
	
}
