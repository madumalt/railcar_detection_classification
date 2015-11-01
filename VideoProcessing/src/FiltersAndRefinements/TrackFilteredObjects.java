package FiltersAndRefinements;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class TrackFilteredObjects {
	
	//max number of objects to be detected in frame
	public static final int MAX_NUM_OBJECTS=50;
	//minimum and maximum object area
	public static final int MIN_OBJECT_AREA = 20*20;
	public static final int MAX_OBJECT_AREA = (int) (640*480/1.5);
	
	public static int[] detectNdrawObject (Mat Threshold , Mat Src){
		
		int[] xy = {-1, -1};
		//find and draw contours
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		
		Mat temp = new Mat();
		Threshold.copyTo(temp);
		
		//find contours of filtered image using openCV findContours function
		//Imgproc.findContours(image, contours, hierarchy, mode, method, offset);
		Imgproc.findContours ( temp, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE );
		
		//use moments method to find our filtered object
		double refArea = 0;
		boolean objectFound = false;
		
		if(contours.size()<MAX_NUM_OBJECTS){
			for ( int contourIdx=0; contourIdx < contours.size(); contourIdx++ )
			  {
				Moments moments = Imgproc.moments(contours.get(contourIdx), true);
				double area = moments.get_m00();
				
				//if the area is less than 20 px by 20px then it is probably just noise
				//if the area is the same as the 3/2 of the image size, probably just a bad filter
				//we only want the object with the largest area so we safe a reference area each
				//iteration and compare it to the area in the next iteration.
				if(area>MIN_OBJECT_AREA && area<MAX_OBJECT_AREA && area>refArea){
					xy[0] = (int)(moments.get_m10()/area);
					xy[1] = (int)(moments.get_m01()/area);
					objectFound = true;
					refArea = area;
				}
				else{objectFound = false;}
				
				//let user know you found an object
				if(objectFound == true){
					Core.putText(Src, "Tracking Object", new Point(0,50), 2, 1, new Scalar(0,255,0), 2);
					//draw object location on screen
					drawObject(xy[0], xy[1], Src);
				}
				else {
					Core.putText(Src ,"TOO MUCH NOISE! ADJUST FILTER" , new Point(0,50), 1, 2, new Scalar(0,0,255), 2);
				}
			  }
		}
		
		return xy;
	}
	
	public static void drawObject(int x, int y, Mat Src){
		//use some of the openCV drawing functions to draw crosshairs
		//on your tracked image!

	    //UPDATE:JUNE 18TH, 2013
	    //added 'if' and 'else' statements to prevent
	    //memory errors from writing off the screen (ie. (-25,-25) is not within the window!)

		Core.circle(Src, new Point(x,y), 20, new Scalar(0,255,0), 2);
	    if(y-25>0)
	    	Core.line(Src, new Point(x,y), new Point(x,y-25), new Scalar(0,255,0), 2);
	    else 
	    	Core.line(Src, new Point(x,y), new Point(x,0), new Scalar(0,255,0), 2);
	    if(y+25<Src.height())
	    	Core.line(Src, new Point(x,y), new Point(x,y+25), new Scalar(0,255,0), 2);
	    else 
	    	Core.line(Src, new Point(x,y), new Point(x,Src.height()), new Scalar(0,255,0), 2);
	    if(x-25>0)
	    	Core.line(Src, new Point(x,y), new Point(x-25,y), new Scalar(0,255,0), 2);
	    else 
	    	Core.line(Src, new Point(x,y), new Point(0,y), new Scalar(0,255,0), 2);
	    if(x+25<Src.width())
	    	Core.line(Src, new Point(x,y), new Point(x+25,y), new Scalar(0,255,0), 2);
	    else 
	    	Core.line(Src, new Point(x,y), new Point(Src.width(),y), new Scalar(0,255,0), 2);

		Core.putText(Src, Integer.toString(x)+","+Integer.toString(y), new Point(x,y+30), 1, 1, new Scalar(0,255,0), 2);
	}
}
