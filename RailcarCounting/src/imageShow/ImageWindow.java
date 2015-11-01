package imageShow;

import javax.swing.JFrame;

import org.opencv.core.Mat;

public class ImageWindow {

	private JFrame frame = null;
	private Panel panel = null;
	
	public ImageWindow(String windowName){
		//frames to display videos
		frame = new JFrame(windowName);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setBounds(0, 0, frame.getWidth(), frame.getHeight());  
		panel = new Panel();  
		frame.setContentPane(panel);
		
		//make frames visible
        frame.setVisible(true);
	}
	
	/**
	 * 
	 * @param image : the Mat which you want to display in the window
	 * @param setSize : This enables to set size using dimensions of the Mat passed. This must be true if it is the first time setting an image for this window.
	 */
	public void repaintWindow(Mat image, boolean setSize){
		
		//this should be true at the initial image when a video sequence is playing rest false
		if(setSize){
			 frame.setSize(image.width()+50, image.height()+50);
		}
		
		panel.setimagewithMat(image);
		
		frame.repaint();
	}
}
