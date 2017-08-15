import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Tester {
	
	
	public void predict(String testpath){
		
		if(!testpath.endsWith(".jpg") || !(new File(testpath).exists())){
			UserInterface.getInstance().addTextToResultsField("Invalid address");
			return;
		}
		
		HashMap<String,Integer> map = KNNDataFor(testpath);
		
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
		s = "  prediction is: "+result+"\n  acuraccy: "+((max*1.0)/count)*100+"%\n";
		for(String key : map.keySet()){
			s += "  Object is "+key+" with probablity "+map.get(key)+"/"+count+"\n";
		}
		
		System.out.println(s);
		UserInterface.getInstance().addTextAndImageToResultsField(s,testpath);
		map.put("total", count);
		UserInterface.getInstance().addGraphChart(map);
	}
	
	private HashMap<String,Integer> KNNDataFor(String testpath){
		int data[] = TrainingUtility.getInstance().getMeanRGB(testpath);
		if(data == null){
			System.out.println("invalid image path");
			return null;
		}
		//System.out.println("("+data[0]+", "+data[1]+", "+data[2]+", "+data[0]+")");
		HashMap<String,Integer> map1 = Trainer.getInstance().tree1.nearestNeighoubrsFor(data);
		data = TrainingUtility.getInstance().getShape(testpath);
		HashMap<String,Integer> map2 = Trainer.getInstance().tree2.nearestNeighoubrsFor(data);
		
		for(String key : map2.keySet()){
			if(map1.containsKey(key)){
				map1.put(key, map1.get(key)+map2.get(key));
			}else{
				map1.put(key, map2.get(key));
			}
		}
		return map1;
	}
	
	private ArrayList<String> callForImages(String dir, ArrayList<String> results, String searchKey, int max){
		
		if(results.size() == 10){
			return results;
		}
		
		if(new File(dir).isDirectory()){
			File directory = new File(dir);
			String images[] = directory.list();
			for(int i = 0; i< images.length; i++){
				results = callForImages(dir+"\\"+images[i], results, searchKey, max);
			}
		}else{
			if(dir.endsWith(".jpg")){
				HashMap<String,Integer> map = KNNDataFor(dir);
				String result = "";
				for(String key : map.keySet()){
					if(map.get(key) > max){
						max = map.get(key);
						result = key;
					}
				}
				if(result == searchKey){
					 results.add(dir); 
				}else if(map.containsKey(searchKey)){
					if(map.get(searchKey) == max){
						 results.add(dir); 
					}
				}
			}
		}
		
		return results;
	}
	
	public void findImagesForKey(String searchKey){
		String baseAdd = "images\\DataBase";//UserInterface.getInstance().dataBase.getText();
		if(!(new File(baseAdd).exists())){
			UserInterface.getInstance().addTextToResultsField("Invalid address" );
			return;
		}
		
		/////////////////////////////////////////////
		ArrayList<String> results = new ArrayList<String>();
		callForImages(baseAdd, results, searchKey, 0);
		/////////////////////////////////////////////
		
//		File directory = new File(baseAdd);
//		String images[] = directory.list();
//		ArrayList<String> results = new ArrayList<String>();
//		int resultsCount = 0;
//		for(int i = 0; i < images.length; i++){
//			HashMap<String,Integer> map = KNNDataFor(baseAdd+"\\"+images[i]);
//			if(map == null){
//				UserInterface.getInstance().addTextToResultsField("No pitchures found in database that match key:" );
//				return;
//			}
//			int max = 0;
//			String result = "";
//			for(String key : map.keySet()){
//				if(map.get(key) > max){
//					max = map.get(key);
//					result = key;
//				}
//			}
//			if(result == searchKey){
//				 results.add(baseAdd+"\\"+images[i]); 
//				resultsCount++;
//			}else if(map.containsKey(searchKey)){
//				if(map.get(searchKey) == max){
//					 results.add(baseAdd+"\\"+images[i]); 
//					resultsCount++;
//				}
//			}
//			if(resultsCount == 10)
//				break;
//		}
		
		if(results.size() == 0){
			UserInterface.getInstance().addTextToResultsField("No pitchures found in database that match key: "+ searchKey);
			return;
		}
		
		UserInterface.getInstance().addFoundImages(results);
		int truePositive = 0;
		int falsePositive = 0;
		for(int i = 0; i < results.size(); i++){
			if(results.get(i).contains("\\"+searchKey+"\\"))
				truePositive++;
			else
				falsePositive++;
		}
		double precision = (truePositive*1.0)/(truePositive+falsePositive);
		UserInterface.getInstance().dataBase.setText(" Precision = "+precision);
		
	}
	
}
