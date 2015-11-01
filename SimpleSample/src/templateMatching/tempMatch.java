package templateMatching;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class tempMatch {
	
	private static int noMatchCount = 0;
	
	/*
	 * checks whether the given source image contains the given template image. 
	 * if the source contains given template mark the area and draw it in the specified location
	 * return true if a match found
	 * else return false
	 */
	public static boolean match(String sourceImagePath, String templtImagePath, String destImagePath){
		boolean matchFound=false;
		double start = System.currentTimeMillis();
		
		Mat srcImage = Highgui.imread(sourceImagePath, 1);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return false;
		}
		Mat tmpltImage = Highgui.imread(templtImagePath, 1);
		if(tmpltImage.empty()){
			System.out.println("tmpltImage Image is null");
			return false;
		}
		
		//gray scaling the image inorder to comprehend with Imgproc.threshold and Imgproc.floodFill methods
		Mat gsrcImage = new Mat();
		Imgproc.cvtColor(srcImage, gsrcImage, Imgproc.COLOR_BGR2GRAY); 
		Mat gtmpltImage = new Mat();
		Imgproc.cvtColor(tmpltImage, gtmpltImage, Imgproc.COLOR_BGR2GRAY); tmpltImage = null;
		
		/// Create the result matrix
		  int result_cols =  srcImage.cols() - gtmpltImage.cols() + 1;
		  int result_rows = srcImage.rows() - gtmpltImage.rows() + 1;
		  Mat result = new Mat();
		  result.create( result_cols, result_rows, CvType.CV_32FC1 );

		  //template matching
		  /**
		   * 0-method=CV_TM_SQDIFF //minimum value is the best match
		   * 1-method=CV_TM_SQDIFF_NORMED //minimum value is the best match
		   * 2-method=CV_TM_CCORR
		   * 3-method=CV_TM_CCORR_NORMED
		   * 4-method=CV_TM_CCOEFF
		   * 5-method=CV_TM_CCOEFF_NORMED
		   */
		  Imgproc.matchTemplate(gsrcImage, gtmpltImage, result, Imgproc.TM_CCOEFF_NORMED);
		  
		  //thresholding
		  Imgproc.threshold(result, result, 0.65, 1.0, Imgproc.THRESH_TOZERO);
		 
		  while (true){
			  double threshold = 0.65;
			  MinMaxLocResult mmlr = Core.minMaxLoc(result);
			  
			  if(mmlr.maxVal>=threshold){
				  //draws a rectangle surrounding the match
				  Core.rectangle(srcImage, mmlr.maxLoc, new Point(mmlr.maxLoc.x + gtmpltImage.cols(), mmlr.maxLoc.y + gtmpltImage.rows()), new Scalar(0,255,0), 2, 8, 0);
				  matchFound = true;
				  System.out.print("match...");
				  
				  
				  //avoids the already considered value by replacing it with zero
				  Imgproc.floodFill(result, new Mat(), mmlr.maxLoc, new Scalar(0), new Rect(), new Scalar(0.1), new Scalar(1.0), Imgproc.FLOODFILL_FIXED_RANGE);
			  }
			  else{
				  break;
			  }
		  }
	        //saving the resulting images
		  if(matchFound)
			Highgui.imwrite(destImagePath, srcImage);
	
			double end = System.currentTimeMillis();
		    System.out.println("Elapsed Time : " + (end - start));
		    return matchFound;
	}

	/*
	 * runs the match() method using several different template images for better results 
	 */
	public static void run(String sourceImagePath,String destImagePath){
		if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp1.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp2.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp3.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp4.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp5.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp6.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp7.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp8.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp9.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp10.png", destImagePath)){}
		else if(match(sourceImagePath, "/home/thilina/Desktop/tempMatching/temp11.png", destImagePath)){}
		else{
			noMatchCount++;
			System.out.println("NO MATCH FOUND : " + noMatchCount);
			readNdraw(sourceImagePath, destImagePath);
		}
	}


	public static void readNdraw(String sourceImagePath, String destImagePath){
		double start = System.currentTimeMillis();
		
		Mat srcImage = Highgui.imread(sourceImagePath, 1);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return;
		}
		
		 //saving the resulting images
		Highgui.imwrite(destImagePath, srcImage);
	
		double end = System.currentTimeMillis();
		System.out.println("Elapsed Time : " + (end - start));
	}
}
