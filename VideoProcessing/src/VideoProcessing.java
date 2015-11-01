import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import imShowJavaHelp.HSVTrackbars;
import imShowJavaHelp.imShowHelp;
import imShowJavaHelp.Panel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import FiltersAndRefinements.MorphologicalOps;
import FiltersAndRefinements.TrackFilteredObjects;
import OpticalFlow.DenseOpticalFlow;

public class VideoProcessing {

	public static void main(String[] args) {
		//linking libraries
		System.out.println("Hello, OpenCV");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
		DenseOpticalFlow.denseOpticalFlowCals("/home/thilina/Downloads/Alibi ALI-IPU3030RV IP Camera Highway Surveillance.mp4", true, false);
		
		
		//detectObject("/home/thilina/Downloads/CN 711 oil tanker train w- BNSF engine at Beare..mp4");
		
		//10 400
		//detectBall(10, 400);
		
		//detectObject("/home/thilina/Downloads/Alibi ALI-IPU3030RV IP Camera Highway Surveillance.mp4", 15, 50);
	}
	
	/*
	 * detect a colored ball/object 
	 * tuning required
	 */
	public static void detectObject(String Url){
		
		new HSVTrackbars();

		JFrame frame1 = new JFrame("Camera");  
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
        Panel panel1 = new Panel();  
        frame1.setContentPane(panel1);   
        
//        JFrame frame2 = new JFrame("HSV");  
//        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
//        frame2.setBounds(300,100, frame2.getWidth()+300, 100+frame2.getHeight());  
//        Panel panel2 = new Panel();  
//        frame2.setContentPane(panel2);
        
        JFrame frame3 = new JFrame("Threshold");  
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame3.setBounds(0, 0, frame3.getWidth(), frame3.getHeight());  
        Panel panel3 = new Panel();  
        frame3.setContentPane(panel3); 
		
	    VideoCapture vc = new VideoCapture(Url);
		if(!vc.isOpened()){
			System.out.println("videoCapture is not Open..!!!");
			return;
		}
		
		frame1.setVisible(true);
//		frame2.setVisible(true);
		frame3.setVisible(true);
		
		Mat currentFrame = new Mat();
		Mat modified = new Mat();
		
		//public static final int CV_CAP_PROP_FPS	=  5,
		//java binding missing for Highgui.CV_CAP_PROP_FPS
		double frameRate = vc.get(5);
		System.out.println("frame rate : " + frameRate);
		
		int delay = 1000/(int)frameRate;

		boolean init1 = true;
		boolean init2 = true;
		boolean init3 = true;
		while (true){
			// read next frame if any
			if (!vc.read(currentFrame)) break;
			
			if(!currentFrame.empty()){
				
				//convert BRG to HSV
				Imgproc.cvtColor(currentFrame, modified, Imgproc.COLOR_BGR2HSV);
				
				//this delay is to ease computational load and get desired frame rate when displaying. possible performance leakage
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				//HSV showing window
//				BufferedImage image2 = imShowHelp.Mat2BufferedImage(modified);
//				if(init2){
//				 frame2.setSize(image2.getWidth(null)+50, image2.getHeight(null)+50);
//				 init2 = false;
//				}
//				panel2.setimage(image2);
				
				
				//inRange method 
				Mat Threshold = new Mat();
				Scalar lower = new Scalar(HSVTrackbars.H_MIN, HSVTrackbars.S_MIN, HSVTrackbars.V_MIN); //blue
				Scalar upper = new Scalar(HSVTrackbars.H_MAX, HSVTrackbars.S_MAX, HSVTrackbars.V_MAX);
				Core.inRange(modified, lower, upper, Threshold);
				
				//morphological operations to smooth out the threshold images
				MorphologicalOps.erodeNdilate(Threshold, 3, 7);
				
				//Imgproc.GaussianBlur(Threshold, Threshold, new Size(9,9),1.5);
				
				TrackFilteredObjects.detectNdrawObject(Threshold, currentFrame);
				
				
				//Threshold image Showing window
				BufferedImage image3 = imShowHelp.Mat2BufferedImage(Threshold);
				if(init3){
				 frame3.setSize(image3.getWidth(null)+50, image3.getHeight(null)+50);
				 init3 = false;
				}
				panel3.setimage(image3);
				
				//camera feed
				BufferedImage image = imShowHelp.Mat2BufferedImage(currentFrame);
				if(init1){
				 frame1.setSize(image.getWidth(null)+50, image.getHeight(null)+50);
				 init1 = false;
				}
				panel1.setimage(image);
				
				//repainting the frames
				frame1.repaint();
//				frame2.repaint();
				frame3.repaint();
			}
		}
		
		vc.release();
	}   

	/*
	 * detect a colored ball/circle using hough circle
	 * tuning required
	 */
	public static void detectBall(int minRadius, int maxRadius){
		new HSVTrackbars();

		JFrame frame1 = new JFrame("Camera");  
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
        Panel panel1 = new Panel();  
        frame1.setContentPane(panel1);   
        
//        JFrame frame2 = new JFrame("HSV");  
//        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
//        frame2.setBounds(300,100, frame2.getWidth()+300, 100+frame2.getHeight());  
//        Panel panel2 = new Panel();  
//        frame2.setContentPane(panel2);
        
        JFrame frame3 = new JFrame("Threshold");  
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame3.setBounds(0, 0, frame3.getWidth(), frame3.getHeight());  
        Panel panel3 = new Panel();  
        frame3.setContentPane(panel3); 
		
	    VideoCapture vc = new VideoCapture(0);
		if(!vc.isOpened()){
			System.out.println("videoCapture is not Open..!!!");
			return;
		}
		
		frame1.setVisible(true);
//		frame2.setVisible(true);
		frame3.setVisible(true);
		
		Mat currentFrame = new Mat();
		Mat modified = new Mat();

		boolean init1 = true;
//		boolean init2 = true;
		boolean init3 = true;
		while (true){
			// read next frame if any
			if (!vc.read(currentFrame)) break;
			
			if(!currentFrame.empty()){
				
				//convert BRG to HSV
//				Imgproc.GaussianBlur(currentFrame, currentFrame, new Size(9,9),1.5);
				Imgproc.cvtColor(currentFrame, modified, Imgproc.COLOR_BGR2HSV);
				
//				//HSV image showing window
//				BufferedImage image2 = imShowHelp.Mat2BufferedImage(modified);
//				if(init2){
//				 frame2.setSize(image2.getWidth(null)+50, image2.getHeight(null)+50);
//				 init2 = false;
//				}
//				panel2.setimage(image2);
				
				
				//inRange method 
				Mat Threshold = new Mat();
				Scalar lower = new Scalar(HSVTrackbars.H_MIN, HSVTrackbars.S_MIN, HSVTrackbars.V_MIN); //blue
				Scalar upper = new Scalar(HSVTrackbars.H_MAX, HSVTrackbars.S_MAX, HSVTrackbars.V_MAX);
				Core.inRange(modified, lower, upper, Threshold);
				
				//smoothes the Threshold image which makes it easier for the HoughCircle method
				Imgproc.GaussianBlur(Threshold, Threshold, new Size(9,9),1.5);
				
				//erode and dilate the threshold image couple of times
				//MorphologicalOps.erodeNdilate(Threshold, 3, 7);
				
				Mat circles = new Mat();
				Imgproc.HoughCircles(Threshold, 
										circles, 
										Imgproc.CV_HOUGH_GRADIENT,  //two-pass algorithm for detecting circles
										2, 						//"accumulator resolution" = image size / this value
										Threshold.rows()/4,     //minimum distance between the centers of the detected circles  
										100, 					// high threshold of canny-edge detector (called by HoughCircles method)
										50,        				//low threshold of canny-dege detector (typically 1/2 of high threshold) 
										minRadius, 				//minimum circle radius
										maxRadius); 			//maximum circle radius (rest is ignored)	
				
				//drawing circles around circles
				 int radius;
		         Point pt;
		         for (int x = 0; x < circles.cols(); x++) {
		            double vCircle[] = circles.get(0,x);

		            if (vCircle == null)
		                break;

		            pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
		            radius = (int)Math.round(vCircle[2]);

		            // draw the found circle
		            Core.circle(currentFrame, pt, radius, new Scalar(0,255,255), 3);
		            Core.circle(currentFrame, pt, 3, new Scalar(255,255,255), 3);
		            }
				
				//Threshold image Showing window
				BufferedImage image3 = imShowHelp.Mat2BufferedImage(Threshold);
				if(init3){
				 frame3.setSize(image3.getWidth(null)+50, image3.getHeight(null)+50);
				 init3 = false;
				}
				panel3.setimage(image3);
				
				//camera feed
				BufferedImage image = imShowHelp.Mat2BufferedImage(currentFrame);
				if(init1){
				 frame1.setSize(image.getWidth(null)+50, image.getHeight(null)+50);
				 init1 = false;
				}
				panel1.setimage(image);
				
				//repainting the frames
				frame1.repaint();
//				frame2.repaint();
				frame3.repaint();
		
			}
		}
	}


	/*
	 * detect moving objects in a static background by comparing subsequent images
	 * 
	 * @param blurSiZe Size of the scalar used in blur method, should be an odd number 
	 */
	public static void detectObject(String videoUrl , int threshIntensity, int blurSize){
		
		
		JFrame frame1 = new JFrame("Camera");  
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
        Panel panel1 = new Panel();  
        frame1.setContentPane(panel1);   
        
        JFrame frame2 = new JFrame("Difference");  
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame2.setBounds(300,100, frame2.getWidth()+300, 100+frame2.getHeight());  
        Panel panel2 = new Panel();  
        frame2.setContentPane(panel2);
        
        JFrame frame3 = new JFrame("Threshold");  
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame3.setBounds(0, 0, frame3.getWidth(), frame3.getHeight());  
        Panel panel3 = new Panel();  
        frame3.setContentPane(panel3); 
		
	    VideoCapture vc = new VideoCapture(videoUrl);
		if(!vc.isOpened()){
			System.out.println("videoCapture is not Open..!!!");
			return;
		}
		
		frame1.setVisible(true);
		frame2.setVisible(true);
		frame3.setVisible(true);
		
		Mat currentFrame = new Mat();
		Mat grayCurrent = new Mat();
		Mat nextFrame = new Mat();
		Mat grayNext = new Mat();
		Mat difference = new Mat();
		Mat threshold = new Mat();
		
		//public static final int CV_CAP_PROP_FPS	=  5,
		//java binding missing for Highgui.CV_CAP_PROP_FPS
		double frameRate = vc.get(5);
		System.out.println("frame rate : " + frameRate);
		
		int delay = 1000/(int)frameRate;

		boolean init1 = true;
		boolean init2 = true;
		boolean init3 = true;
		while (true){
			// read next frame if any
			if (!vc.read(nextFrame)) {
				System.out.println("endOfVideo");
				break;
			}
			
			if(!nextFrame.empty()){
				
				//convert BGR to GrayScale
				Imgproc.cvtColor(nextFrame, grayNext, Imgproc.COLOR_BGR2GRAY);
				
				if(currentFrame.empty()){
					nextFrame.copyTo(currentFrame);
					nextFrame = new Mat();
					grayNext.copyTo(grayCurrent);
					grayNext = new Mat();
					continue;
				}
				
				//calculating difference between subsequent images
				Core.absdiff(grayCurrent, grayNext, difference);
				
				//threshold intensity/difference image at a given intensity value
				Imgproc.threshold(difference, threshold, threshIntensity, 255, Imgproc.THRESH_BINARY);
				
				//blur out the image
				Imgproc.blur(threshold, threshold, new Size(blurSize, blurSize));
				Imgproc.threshold(threshold, threshold, threshIntensity, 255, Imgproc.THRESH_BINARY);
				
				//this delay is to ease computational load and get desired frame rate when displaying. possible performance leakage
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//detect and draw objects
				TrackFilteredObjects.detectNdrawObject(threshold, currentFrame);
				
				//camera feed
				BufferedImage image1 = imShowHelp.Mat2BufferedImage(currentFrame);
				if(init1){
				 frame1.setSize(image1.getWidth(null)+50, image1.getHeight(null)+50);
				 init1 = false;
				}
				panel1.setimage(image1);
				
				//Difference showing window
				BufferedImage image2 = imShowHelp.Mat2BufferedImage(difference);
				if(init2){
				 frame2.setSize(image2.getWidth(null)+50, image2.getHeight(null)+50);
				 init2 = false;
				}
				panel2.setimage(image2);
				
				
				//Threshold image Showing window
				BufferedImage image3 = imShowHelp.Mat2BufferedImage(threshold);
				if(init3){
				 frame3.setSize(image3.getWidth(null)+50, image3.getHeight(null)+50);
				 init3 = false;
				}
				panel3.setimage(image3);
				
				//repainting the frames
				frame1.repaint();
				frame2.repaint();
				frame3.repaint();
				
				nextFrame.copyTo(currentFrame);
				grayNext.copyTo(grayCurrent);
			}
		}
		
		vc.release();
	}
}

