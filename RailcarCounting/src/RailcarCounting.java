import imageShow.ImageWindow;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.video.*;


public class RailcarCounting {
	
	public static void main(String[] arg){
		//linking libraries
		System.out.println("Hello, OpenCV");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Hello, OpenCV");
		
		ImageWindow window1 = new ImageWindow("Window1");
		ImageWindow window2 = new ImageWindow("Window2");
		ImageWindow window3 = new ImageWindow("Window3");
	
		VideoCapture vc = new VideoCapture(0);
		if(!vc.isOpened()){
			System.out.println("videoCapture is not Opened..!!!");
			return;
		}
		
		BackgroundSubtractorGMG bgSub = new BackgroundSubtractorGMG();
		
		Mat currentFrame = new Mat();
		Mat fgmask = new Mat();
		
		boolean init1 = true;
		boolean init2 = true;
		boolean init3 = true;
		while (true){
			// read next frame if any
			if (!vc.read(currentFrame)) {
				System.out.println("endOfVideo");
				break;
			}
			
			
			
			if(init1) {window1.repaintWindow(currentFrame, true); init1=false;} else {window1.repaintWindow(currentFrame, false);}
			if(init2) {window2.repaintWindow(currentFrame, true); init2=false;} else {window2.repaintWindow(currentFrame, false);}
			if(init3) {window3.repaintWindow(currentFrame, true); init3=false;} else {window3.repaintWindow(currentFrame, false);}
		}
		
		
		vc.release();
	}
}
