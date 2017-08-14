import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.opencv.core.Core;

public class MainClass implements Observer{
	
	Tester tester = new Tester();
	
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MainClass obj = new MainClass();
		obj.run();
	}
	
	public void run(){
		UserInterface.getInstance().addObserver(this);
		UserInterface.getInstance().show();
	}

	private void startTraining(String location){
		String path = location;
		File directory = new File(path);
		String sets[] = directory.list();
		ArrayList<String> list = new ArrayList<String>();
		if(sets != null){
			for(int i = 0 ; i < sets.length; i ++){
				Trainer.getInstance().trainAtLocation(path+"\\"+sets[i],sets[i]);
				if(!list.contains(sets[i])){
					list.add(sets[i]);
				}
			}

		}
		Trainer.getInstance().trainingDone();
		if(Trainer.getInstance().tree1.isEmpty() && Trainer.getInstance().tree2.isEmpty()){
			UserInterface.getInstance().alertWithMsg("Invalid Location\n> The source Folder Should have sub folders\n> Each sub-folder name is a Lable for Trainig Set\n> Sub-Folders should have respective images for training");
		}else{
			UserInterface.getInstance().trainingSuccessfull(list);
		}
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) {
		String argument[] = (String[]) arg;
		switch (argument[0]) {
		case "Start training":
			startTraining(argument[1]);
			break;
		case "Predict":
			tester.predict(argument[1]);
			break;
		case "Find":
			tester.findImagesForKey(argument[1]);
			System.out.println("Finding "+argument[1]+"......");
			break;
		case "Reset":
			Trainer.getInstance().reset();
			break;
		default:
			
			break;
		}
	}
	
}
