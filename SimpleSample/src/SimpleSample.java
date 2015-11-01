import java.util.ArrayList;

import masking.MaskHelper;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.*;

import SIFT.FeatureDetect;
import templateMatching.tempMatch;

public class SimpleSample {

  public static void main(String[] args) {

	  // Load the native library.
	  System.out.println("Hello, OpenCV");
	  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	  
	  DetectFaceDemo.run();
	  
//	  for(int i=1; i<=66; i++){
//	  //loading an image
//		  Mat image;
//			  image = Highgui.imread("/home/thilina/Desktop/pos/"+i+".jpg");
//		  
//	  if(image.empty()){
//		  System.out.println("NUll Image..!!!");
//	  }else{
//		  System.out.println("got it..!!!");
//		  Mat gray = new Mat();
//		  Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
//		  if(gray.empty()){System.out.println("Gray image empty..!!!");}
//		  else Highgui.imwrite("/home/thilina/Desktop/pos/"+i+".jpg", gray);}
//	  }
  
//	  	//normalizing the pixel values
//	  	MaskHelper mh = new MaskHelper();
//		mh.run("/home/thilina/Downloads/facedec.jpg", "/home/thilina/Desktop/normalized.png", 10);
	  
//	  //masking using kernal matrix
//	  MaskHelper mhlp = new MaskHelper();
//	  mhlp.maskIt("/home/thilina/Downloads/facedec.jpg", "/home/thilina/Desktop/masked.png");
	  
//	  //template matching
//	  for(int i=1; i<162; i++){
//		  if(i<10)
//			  tempMatch.run("/home/thilina/Desktop/tempMatching/TestSample/8458-COUNT ERROR-page-00"+i+".jpg", "/home/thilina/Desktop/tempMatching/result/matched"+i+".png");
//		  else if(i>9 && i<100)
//			  tempMatch.run("/home/thilina/Desktop/tempMatching/TestSample/8458-COUNT ERROR-page-0"+i+".jpg", "/home/thilina/Desktop/tempMatching/result/matched"+i+".png");
//		  else
//			  tempMatch.run("/home/thilina/Desktop/tempMatching/TestSample/8458-COUNT ERROR-page-"+i+".jpg", "/home/thilina/Desktop/tempMatching/result/matched"+i+".png");
//	  }
	  
	//  FeatureDetect.flannBasedDetec("/home/thilina/Desktop/featureDetect/source.jpg", "/home/thilina/Desktop/featureDetect/CACH.jpg", "/home/thilina/Desktop/tempMatching/fd.png");
	  
  }
  
}