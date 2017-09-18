import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Tester {
	
	
	public void predict(String testpath, String method){
		
		
		if(!testpath.endsWith(".jpg") || !(new File(testpath).exists())){
			UserInterface.getInstance().addTextToResultsField("Invalid address");
			return;
		}
		
		
		String s = "";
		HashMap<String,Integer> map = null;
		
		if(method.equals("KD-Tree")){
			map = KNNDataFor(testpath);
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
			map.put("total", count);
		}else{
			s = predictionForMeansPoint(testpath);
		}
		
		UserInterface.getInstance().addGraphChart(map);
		System.out.println(s);
		UserInterface.getInstance().addTextAndImageToResultsField(s,testpath);
		
	}
	
	private String predictionForMeansPoint(String testpath){
		int data[] = TrainingUtility.getInstance().getMeanRGB(testpath);
		int data1[] = TrainingUtility.getInstance().getShape(testpath);
		return Trainer.getInstance().graph.isInCategory(data1, data);
	}
	
	private HashMap<String,Integer> KNNDataFor(String testpath){
		int data[] = TrainingUtility.getInstance().getMeanRGB(testpath);
		if(data == null){
			System.out.println("invalid image path");
			return null;
		}
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
		
		if(results.size() == 15){
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
				
//				if(false){
//					String result = predictionForMeansPoint(dir);
//					System.out.println("result "+result);
//					if(result.contains(searchKey)){
//						results.add(dir);
//					}
//				}else{
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
//				}
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
