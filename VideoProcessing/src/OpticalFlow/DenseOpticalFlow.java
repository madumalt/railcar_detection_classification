package OpticalFlow;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import imShowJavaHelp.Panel;
import imShowJavaHelp.imShowHelp;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.video.BackgroundSubtractorMOG2;

public class DenseOpticalFlow {

	public static void denseOpticalFlowCals(String Url, boolean setDelay, boolean cam){
		
		int scaleDown = 2;
		
		//frames to display videos
		JFrame frame1 = new JFrame("Backgroung + Foreground");  
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
        Panel panel1 = new Panel();  
        frame1.setContentPane(panel1);   
        
        JFrame frame2 = new JFrame("Foreground");  
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame2.setBounds(300,100, frame2.getWidth()+300, 100+frame2.getHeight());  
    	Panel panel2 = new Panel();  
    	frame2.setContentPane(panel2);
      
    	JFrame frame3 = new JFrame("Threshold");  
    	frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	frame3.setBounds(0, 0, frame3.getWidth(), frame3.getHeight());  
    	Panel panel3 = new Panel();  
      	frame3.setContentPane(panel3); 
		
        //make frames visible
        frame1.setVisible(true);
        frame2.setVisible(true);
        frame3.setVisible(true);
	
        Mat currentFrame = new Mat();
		Mat grayCurrent = new Mat();
		Mat nextFrame = new Mat();
		Mat grayNext = new Mat();
		Mat fgmask = new Mat();
		Mat hsv = new Mat();
		
		VideoCapture vc = null;
		if(!cam) vc = new VideoCapture(Url);
		else vc = new VideoCapture(0);
		if(!vc.isOpened()){
			System.out.println("videoCapture is not Opened..!!!");
			return;
		}
		
		vc.read(currentFrame);
		Imgproc.resize(currentFrame, currentFrame, new Size(currentFrame.width()/scaleDown, currentFrame.height()/scaleDown));
		Imgproc.cvtColor(currentFrame, grayCurrent, Imgproc.COLOR_BGR2GRAY);

		//hsv must be 3 channel 
		hsv = Mat.zeros(currentFrame.size(), currentFrame.type());
		System.out.println("hsv depth: " + hsv.depth()+"    hsv chnnels: "+hsv.channels());	
		
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
					Imgproc.resize(nextFrame, nextFrame, new Size(nextFrame.width()/scaleDown, nextFrame.height()/scaleDown));
					Imgproc.cvtColor(nextFrame, grayNext, Imgproc.COLOR_BGR2GRAY);
					
					if(setDelay){
					//this delay is to ease computational load and get desired frame rate when displaying. possible performance leakage
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					
					//calculate optical flow
					 Mat flow = new Mat();  
					 Video.calcOpticalFlowFarneback(grayCurrent, grayNext, flow, 0.5, 3, 15, 3, 5, 1.2, 0);
//					 System.out.println("channels: "+flow.channels()+"       depth: "+flow.depth());
					 
					 //extracting individual channels from the detected optical Flow
					 Mat x = new Mat();
					 Mat y = new Mat();
					 Core.extractChannel(flow, x, 0);
					 Core.extractChannel(flow, y, 1);
					 /**
					  * equivalent code segment to the above using Core.split()
					  * List<Mat> xy = new ArrayList<Mat>();
					  * Core.split(flow, xy);
					  */
					 
					 //no clear idea abt what this do
					 Mat magnitude = new Mat();
					 Mat angle = new Mat();
					 Core.cartToPolar(x, y, magnitude, angle);
					 
					 //begin: assigning each channel seperately for a HSV color-spaced Mat
					 
					 Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_BGR2HSV);
					// System.out.println(hsv.channels()+" "+hsv.depth()+" "+hsv.type());
					 
					 List<Mat> hsvChannels = new ArrayList<Mat>();
					 Core.split(hsv, hsvChannels);
					 
					 Mat h=hsvChannels.get(0);
//					 System.out.println("H");
//					 System.out.println(h.channels()+" "+h.depth()+" "+h.type());
					 Mat s=hsvChannels.get(1);
//					 System.out.println("S");
//					 System.out.println(s.channels()+" "+s.depth()+" "+s.type());
					 Mat v=hsvChannels.get(2);
//					 System.out.println("V");
//					 System.out.println(v.channels()+" "+v.depth()+" "+v.type());
					 
					 hsvChannels = new ArrayList<Mat>();

//					 Core.multiply(angle, Mat.ones(angle.size(), angle.type()), h, 180/Math.PI/2, h.type());
					 hsvChannels.add(0, h);
//					 System.out.println("H");
//					 System.out.println(h.channels()+" "+h.depth()+" "+h.type());
				 
//					 Core.add(s, new Scalar(255), s);
					 hsvChannels.add(1, s);
//					 System.out.println("S");
//					 System.out.println(s.channels()+" "+s.depth()+" "+s.type());
					 
					Core.normalize(magnitude, v, 0, 255, Core.NORM_MINMAX, v.type());
					hsvChannels.add(2, v);
//					System.out.println("V");
//					System.out.println(v.channels()+" "+v.depth()+" "+v.type());
					 
					Mat cflow = new Mat();
					Core.merge(hsvChannels, cflow);
//					Imgproc.cvtColor(cflow, cflow, Imgproc.COLOR_HSV2BGR);
					System.out.println("BGR");
					System.out.println(cflow.channels()+" "+cflow.depth()+" "+cflow.type());
					 //end: assigning each channel seperately for a HSV color-spaced Mat
				
					
					//Current Frame (this is actually named as nextFrame in the code)
					BufferedImage image1 = imShowHelp.Mat2BufferedImage(grayNext);
					if(init1){
					 frame1.setSize(image1.getWidth(null)+50, image1.getHeight(null)+50);
					 init1 = false;
					}
					panel1.setimage(image1);
					
					//previous Frame (this is actually named as currentFrame in the code)
					BufferedImage image2 = imShowHelp.Mat2BufferedImage(grayCurrent);
					if(init2){
					 frame2.setSize(image2.getWidth(null)+50, image2.getHeight(null)+50);
					 init2 = false;
					}
					panel2.setimage(image2);
					
					
					//OpticalFlow detected Frame
					BufferedImage image3 = imShowHelp.Mat2BufferedImage(cflow);
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
					nextFrame = new Mat();
				}
			}
			
			vc.release();
	}
	
	public static void backgroundsubstraction(){
		
	}
}
