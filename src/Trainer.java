import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Trainer {
	
	private static Trainer t = null;
	private ComparisionParas trainingSet = new ComparisionParas(3); 
	
	private Trainer(){
		
	}
	
	public static Trainer getInstance(){
		if(t == null)
			t = new Trainer();
		return t;
	}
	
	public void trainAtLocation(String loc_str, String setName){
		File directory = new File(loc_str);
		String images[] = directory.list();
		System.out.println("\n"+setName+":");
		for(int i = 0; i < images.length; i++){
			//System.out.println(images[i]);
			if(images[i].endsWith(".jpg")){
				Mat img = Imgcodecs.imread(loc_str+"\\"+images[i]);
				analyseAndStore(img, setName);
			}
		}
	}
	
	public void trainingDone(){
		trainingSet.moldForKtree();
		trainingSet.printSet();
	}
	
	private void analyseAndStore(Mat img, String setName){
		Mat blur = new Mat();
		Mat blur_hsv = new Mat();
		Mat binary = new Mat();
		Mat blur_binary = new Mat();
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Imgproc.GaussianBlur(img, blur, new Size(5,5), 25);
		Imgproc.cvtColor(blur, blur_hsv, Imgproc.COLOR_RGB2HSV);
		Core.inRange(blur_hsv, new Scalar(0,0,200), new Scalar(150,150,255), binary);
		//blur_hsv = extractImage(binary,blur_hsv);
		Imgproc.cvtColor(img, binary, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(blur, blur_binary, Imgproc.COLOR_RGB2GRAY);
		int dist[] = meanValue(blur_hsv,binary, blur_binary);
		trainingSet.Add(dist, setName);
		//System.out.println("("+(int)dist[0]+","+(int)dist[1]+","+(int)dist[2]+")");
	}
	
	public int[] getMeanRGB(String loc_str){
		Mat img = Imgcodecs.imread(loc_str);
		Mat blur = new Mat();
		Mat blur_hsv = new Mat();
		Mat binary = new Mat();
		Mat blur_binary = new Mat();
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Imgproc.GaussianBlur(img, blur, new Size(5,5), 5);
		Imgproc.cvtColor(blur, blur_hsv, Imgproc.COLOR_RGB2HSV);
		Core.inRange(blur_hsv, new Scalar(0,0,200), new Scalar(150,150,255), binary);
		Imgproc.cvtColor(img, binary, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(blur, blur_binary, Imgproc.COLOR_RGB2GRAY);
		int dist[] = meanValue(blur_hsv,binary, blur_binary);
		return dist;
	}
	
	public static Mat extractImage(Mat binary, Mat hsv){
		for(int i = 0; i < hsv.size().height ; i++){
			for(int j = 0; j < hsv.size().width ; j++){
				if(binary.get(i, j)[0] == 255.0){
					byte data[] = {0,0,0};
					hsv.put(i, j, data);
				}
			}
		}
		return hsv;
	}
	
	private int[] meanValue(Mat img1, Mat binary, Mat blur_binary){
		int mean[] = {0,0,0,0};
		double count = 0;  
			for(int i = 0 ; i < img1.size().height; i++){
				for(int j = 0 ; j < img1.size().width; j++){
					mean[0] += (int)img1.get(i, j)[0];
					mean[1] += (int)img1.get(i, j)[1];
					mean[2] += (int)img1.get(i, j)[2];
					mean[3] += (int)binary.get(i, j)[0] - (int)blur_binary.get(i, j)[0];
					if(!(mean[0] == 0 && mean[1] == 0 && mean[2] == 0)){
						count++;
					}
				}
			}
			mean[0] /= count;
			mean[1] /= count;
			mean[2] /= count;
			mean[3] /= count;
		return mean;
	}
	
	private double eucledianDistance(Mat img1, Mat img2){
		double dist = 0;
		if(img2 == null){
			for(int i = 0 ; i < img1.size().height; i++){
				for(int j = 0 ; j < img1.size().width; j++){
					dist += (img1.get(i, j)[0]*img1.get(i, j)[0]);
				}
			}
			return Math.sqrt(dist);
		}
		return dist;
	}
	
}
