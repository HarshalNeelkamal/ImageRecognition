import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TrainingUtility {

	private static TrainingUtility instance = null;
	
	private TrainingUtility(){
		
	}
	
	public static TrainingUtility getInstance(){
		if(instance == null){
			instance = new TrainingUtility();
		}
		return instance;
	}
	
	
	public int[] getMeanRGB(String loc_str){
		Mat img = Imgcodecs.imread(loc_str);
		if(!img.isContinuous())
			return null;
		Mat blur = new Mat();
		Mat blur_hsv = new Mat();
		Mat binary = new Mat();
		Mat blur_binary = new Mat();
		//Imgproc.resize(img, img, new Size(200,200));
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Imgproc.GaussianBlur(img, blur, new Size(5,5), 25);
		Imgproc.cvtColor(blur, blur_hsv, Imgproc.COLOR_RGB2HSV);
		Imgproc.cvtColor(img, binary, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(blur, blur_binary, Imgproc.COLOR_RGB2GRAY);
		int dist[] = meanValue(blur_hsv,binary, blur_binary);
		return dist;
	}
	
	public int[] getShape(String loc_str){
		Mat img = Imgcodecs.imread(loc_str);
		if(!img.isContinuous())
			return null;
		Mat blur = new Mat();
		Mat blur_hsv = new Mat();
		Mat binary = new Mat();
		//Imgproc.resize(img, img, new Size(200,200));
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Imgproc.GaussianBlur(img, blur, new Size(5,5), 25);
		Imgproc.cvtColor(blur, blur_hsv, Imgproc.COLOR_RGB2HSV);
		Core.inRange(blur_hsv, new Scalar(0,0,200), new Scalar(150,150,255), binary);
		binary = extractImage(binary,binary);
		int shape[] = shapeParas(binary);
		return shape;
	}
	
	public int[] shapeParas(Mat extImg){
		double data[] = new double[4];
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;
		int imageWidth = (int)extImg.size().width;
		int imageHeight = (int)extImg.size().height;
		for(int i = 0; i < imageWidth; i++){
			for(int j = 0; j < imageHeight; j++){
				if(extImg.get(j, i)[0] == 255){
					int count = 0;
					int pixels = 0;
					while(pixels < 15){
						if(i+pixels < extImg.size().width && extImg.get(j, i + pixels)[0] == 255){
							count++;
							if(count == 10){
								left = i;
								break;
							}
						}
						pixels++;
					}
				}
				if(left != 0)
					break;
			}
			if(left != 0)
				break;
		}
		for(int i = (int)extImg.size().width - 1; i > 0; i--){
			for(int j = (int)extImg.size().height - 1; j > 0; j--){
				if(extImg.get(j, i)[0] == 255){
					int count =0;
					int pixels = 0;
					while(pixels < 15){
						if(i-pixels > 0 && extImg.get(j, i - pixels)[0] == 255){
							count++;
							if(count == 10){
								right = i;
								break;
							}
						}
						pixels++;
					}
				}
				if(right != 0)
					break;
			}
			if(right != 0)
				break;
		}
		data[0] = right - left; //width;
		
		for(int i = 0; i <extImg.size().height; i++){
			for(int j = 0; j <extImg.size().width; j++){
				if(extImg.get(i, j)[0] == 255){
					int count =0;
					int pixels = 0;
					while(pixels < 15){
						if(i + pixels < extImg.size().height && extImg.get(i + pixels, j)[0] == 255){
							count++;
							if(count == 10){
								top = i;
								break;
							}
						}
						pixels++;
					}
				}
				if(top != 0)
					break;
			}
			if(top != 0)
				break;
		}
		for(int i = (int)extImg.size().height - 1; i > 0; i--){
			for(int j = (int)extImg.size().width - 1; j > 0; j--){
				if(extImg.get(i, j)[0] == 255){
					int count =0;
					int pixels = 0;
					while(pixels < 15){
						if(i - pixels > 0 && extImg.get(i- pixels, j )[0] == 255){
							count++;
							if(count == 10){
								bottom = i;
								break;
							}
						}
						pixels++;
					}
				}
				if(bottom != 0)
					break;
			}
			if(bottom != 0)
				break;
		}
		data[2] = bottom - top;
		int tempTop = top;
		int tempLeft = left;
		int leftDiag = 0;
		while(tempTop < bottom && tempLeft < right){
			if(extImg.get(tempTop, tempLeft)[0] == 255)
				leftDiag++;
			tempTop++;
			tempLeft++;
		}
		tempTop = top;
		tempLeft = right;
		int rightDiag = 0;
		while(tempTop < bottom && tempLeft > left){
			if(extImg.get(tempTop, tempLeft)[0] == 255)
				rightDiag++;
			tempTop++;
			tempLeft--;
		}
		System.out.println(rightDiag+" "+leftDiag+" "+top+" "+bottom+" "+left+" "+right);

		data[1] = (rightDiag*1.0)/data[0];
		data[0] = (leftDiag*1.0)/data[0];
		data[3] = (rightDiag*1.0)/data[2];
		data[2] = (leftDiag*1.0)/data[2];
		int intData[] = new int[data.length];
		System.out.println("double: "+data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" ");
		for(int i = 0; i < intData.length; i++){
			intData[i] = (int)(data[i]*100);
		}
		System.out.println("int: "+intData[0]+" "+intData[1]+" "+intData[2]+" "+intData[3]+" ");
		return intData;
	}
	
	public Mat extractImage(Mat binary, Mat hsv){
		for(int i = 0; i < hsv.size().height ; i++){
			for(int j = 0; j < hsv.size().width ; j++){
				if(binary.get(i, j)[0] == 255.0){
					byte data[] = {0};
					hsv.put(i, j, data);
				}else{
					byte data[] = {(byte)255};
					hsv.put(i, j, data);
				}
			}
		}
		return hsv;
	}
	
	public int[] meanValue(Mat img1, Mat binary, Mat blur_binary){
		int mean[] = {0,0,0,0};
		double count = 0;  
		//double count1 = 0;
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
	
}
