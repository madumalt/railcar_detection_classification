package masking;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

public class MaskHelper {

	public void run(String sourceImagePath, String destImagePath, int factor ){
		double start = System.currentTimeMillis();
		
		Mat srcImage = Highgui.imread(sourceImagePath);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return;
		}
		
		int[] table = new int[256];
	    for (int i = 0; i < 256; i++){
	       table[i] = (factor * (i/factor));
	       System.out.print(" " + table[i]  + " ");
	    }
	    
	    Mat lookupTable = new Mat(1, 256, CvType.CV_8UC1);
	    for(int i=0; i<256; i++){
	    	
	    	lookupTable.put(0, i, table[i]);
	    	for(double d : lookupTable.get(0, i))
	    		System.out.println(d);
	    }
	    
	    Mat modified = new Mat();
	    Core.LUT(srcImage, lookupTable, modified);
	    
	    if(modified.empty()){System.out.println("Gray image empty");}
		else Highgui.imwrite(destImagePath, modified);
	    
	    double end = System.currentTimeMillis();
	    System.out.print("Elapsed Time : " + (end - start));
	}


	public void maskIt(String sourceImagePath, String destImagePath){
		double start = System.currentTimeMillis();
		
		Mat srcImage = Highgui.imread(sourceImagePath);
		if(srcImage.empty()){
			System.out.println("Src Image is null");
			return;
		}
		
		Mat kernal = new Mat (3,3, CvType.CV_8S);
		int[][] Array2D = {{0, -1,  0}, {-1,  5, -1}, {0, -1,  0}};
		
		for(int row=0; row < 3 ; row++){
			for(int col=0; col < 3; col++){
				kernal.put(row, col, Array2D[row][col]);
				for(double d : kernal.get(row, col))
						System.out.print(" " + d);
			}
			System.out.println();
		}
		
		Mat modified = new Mat();
		Imgproc.filter2D(srcImage, modified, srcImage.depth(), kernal);
		
		if(modified.empty()){System.out.println("masked image is empty");}
		else Highgui.imwrite(destImagePath, modified);
		
		double end = System.currentTimeMillis();
	    System.out.print("Elapsed Time : " + (end - start));
	}
}
