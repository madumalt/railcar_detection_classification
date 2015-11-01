package SIFT;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.*;

public class FeatureDetect {

	public static void detetFeatures(String sourceImagePath, String templtImagePath, String destImagePath){
		Mat srcImage = Highgui.imread(sourceImagePath, 1);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return;
		}
		Mat tmpltImage = Highgui.imread(templtImagePath, 1);
		if(tmpltImage.empty()){
			System.out.println("tmpltImage Image is null");
			return;
		}
		
		//gray scaling the image inorder to comprehend with Imgproc.threshold and Imgproc.floodFill methods
		Mat gsrcImage = new Mat();
		Imgproc.cvtColor(srcImage, gsrcImage, Imgproc.COLOR_BGR2GRAY); 
		Mat gtmpltImage = new Mat();
		Imgproc.cvtColor(tmpltImage, gtmpltImage, Imgproc.COLOR_BGR2GRAY); 
		
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.HARRIS);
		MatOfKeyPoint templateKp = new MatOfKeyPoint();
		fd.detect(gtmpltImage, templateKp);
		MatOfKeyPoint srcKp = new MatOfKeyPoint();
		fd.detect(gsrcImage, srcKp);

		
//		MatOfPoint mop = new MatOfPoint();		
//		Imgproc.goodFeaturesToTrack(gtmpltImage,mop, 100,0.01,10);
//		Point[] points = mop.toArray();
//		for(int i=0; i<points.length; i++){
//			Core.circle(tmpltImage, points[i],10, new Scalar(0,255,0), 2, -1, 0);}
//		Features2d.drawKeypoints(tmpltImage, templateKp, tmpltImage, new Scalar(0,255.0), Features2d.DRAW_RICH_KEYPOINTS);
//		Features2d.drawKeypoints(srcImage, srcKp, srcImage, new Scalar(0,255.0), Features2d.DRAW_RICH_KEYPOINTS);
		
		
		//matching descriptions of features and recognizing objects
		DescriptorExtractor extractor =DescriptorExtractor.create(DescriptorExtractor.SIFT);
		Mat descriptorTemplate = new MatOfDMatch();
		extractor.compute(gtmpltImage, templateKp, descriptorTemplate);
		Mat descriptorSrc = new MatOfDMatch();
		extractor.compute(gsrcImage, srcKp, descriptorSrc);
		
//		Mat queryDescriptors, Mat trainDescriptors, MatOfDMatch matches
		DescriptorMatcher dmatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		MatOfDMatch matches = new MatOfDMatch();
		dmatcher.match(descriptorSrc, descriptorTemplate, matches);
		
		// this will draw all matches, works fine
		Mat output = new Mat();
	    Features2d.drawMatches(srcImage, srcKp, tmpltImage, templateKp, matches, output);

		
		//saving the resulting images
		Highgui.imwrite(destImagePath, output);
	}

	public static void flannBasedDetec(String sourceImagePath, String templtImagePath, String destImagePath){
		Mat srcImage = Highgui.imread(sourceImagePath, 1);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return;
		}
		Mat tmpltImage = Highgui.imread(templtImagePath, 1);
		if(tmpltImage.empty()){
			System.out.println("tmpltImage Image is null");
			return;
		}
		
		//gray scaling the image inorder to comprehend with Imgproc.threshold and Imgproc.floodFill methods
		Mat gsrcImage = new Mat();
		Imgproc.cvtColor(srcImage, gsrcImage, Imgproc.COLOR_BGR2GRAY); 
		Mat gtmpltImage = new Mat();
		Imgproc.cvtColor(tmpltImage, gtmpltImage, Imgproc.COLOR_BGR2GRAY); 
		
		//calculating keypoints
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
		MatOfKeyPoint templateKp = new MatOfKeyPoint();
		fd.detect(gtmpltImage, templateKp);
		MatOfKeyPoint srcKp = new MatOfKeyPoint();
		fd.detect(gsrcImage, srcKp);
		
		
		//extracting descriptors of features of image/objects
		DescriptorExtractor extractor =DescriptorExtractor.create(DescriptorExtractor.SIFT);
		Mat descriptorTemplate = new Mat();
		extractor.compute(gtmpltImage, templateKp, descriptorTemplate);
		Mat descriptorSrc = new Mat();
		extractor.compute(gsrcImage, srcKp, descriptorSrc);
				
		//matching queryImage(srcImage) descriptors with trainImage(tmpltImage) descriptors
		DescriptorMatcher dmatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
		MatOfDMatch matches = new MatOfDMatch();
		dmatcher.match(descriptorSrc, descriptorTemplate, matches);
		
//		// this will draw all matches, works fine
//		Mat output = new Mat();
//		Features2d.drawMatches(srcImage, srcKp, tmpltImage, templateKp, matches, output);

		
	    Scalar RED = new Scalar(255,0,0);
	    Scalar GREEN = new Scalar(0,255,0);
	    Scalar BLUE = new Scalar(0,0,255);
	    
	    List<DMatch> matchesList = matches.toList();
	    Double max_dist = 0.0;
	    Double min_dist = 100.0;
	    //finding minimum distance and maximum distance
	    for(int i = 0;i < matchesList.size(); i++){
	        Double dist = (double) matchesList.get(i).distance;
	        if (dist < min_dist)
	            min_dist = dist;
	        if ( dist > max_dist)
	            max_dist = dist;
	    }

	    //retaining only goodmatches
	    ArrayList<DMatch> good_matches = new ArrayList<DMatch>();
	    for(int i = 0;i < matchesList.size(); i++){
	        if (matchesList.get(i).distance <= Math.max(1.5 * min_dist, 0.02))
	            good_matches.add(matchesList.get(i));
	    }



	    // Printing
	    MatOfDMatch goodMatches = new MatOfDMatch();
	    goodMatches.fromList(good_matches);
	   System.out.println("earlier : " + matches.size() + " now : " + goodMatches.size());

	    Mat output= new Mat();
	    MatOfByte drawnMatches = new MatOfByte();
	    Features2d.drawMatches(srcImage, srcKp, tmpltImage, templateKp, goodMatches, output, GREEN, RED, drawnMatches, Features2d.DRAW_RICH_KEYPOINTS);

	    
//	    List<KeyPoint> keypoints_objectList = templateKp.toList();
//	    List<KeyPoint> keypoints_sceneList = srcKp.toList();
//	    
//	    ArrayList<Point> objList = new ArrayList<Point>();
//	    ArrayList<Point> sceneList = new ArrayList<Point>();
//	    
//	    for(int i = 0; i<good_matches.size(); i++){
//	    	System.out.println( i + " : " + keypoints_objectList.get(good_matches.get(i).queryIdx));
//	    	//System.out.println( i + " : " + keypoints_objectList.get(good_matches.get(i).trainIdx));
//	    	}
	    
//	    for(int i = 0; i<good_matches.size(); i++){
//	    	objList.add(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
//	    	sceneList.add(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
//	    }
	       
//	    MatOfPoint2f obj = new MatOfPoint2f();
//	    MatOfPoint2f scene = new MatOfPoint2f();
//	    obj.fromList(objList);
//	    scene.fromList(sceneList);
//	    Mat matchMask = new Mat();
//	    Mat homography = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5.0, matchMask);
//	    
//	    Mat destination = new Mat();
//	    Core.perspectiveTransform(gtmpltImage, destination, homography);
//	   
	    
	    
		//saving the resulting images
	    Highgui.imwrite(destImagePath, output);
	}


	public static int ratioTest(MatOfDMatch matches){
		
		List<DMatch> matchesList = matches.toList();

	    for(int i = 0;i < matchesList.size(); i++){
//	        Double dist = (double) matchesList.get(i).distance;
//	     // if 2 NN has been identified
//	        if (matchIterator->size() > 1) {
//	        // check distance ratio
//	        if ((*matchIterator)[0].distance/
//	        (*matchIterator)[1].distance > ratio) {
//	        matchIterator->clear(); // remove match
//	        removed++;
//	        }
//	        } else { // does not have 2 neighbours
//	        matchIterator->clear(); // remove match
//	        removed++;
//	        }
	    	
	    	
	    	//if 2 NN has been identified
	    	if(true){
	    		//check distance ratio
	    		
	    	}
	    }
		
		return 0;
	}
}
