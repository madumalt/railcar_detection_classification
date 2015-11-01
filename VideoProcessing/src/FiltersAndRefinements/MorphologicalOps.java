package FiltersAndRefinements;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MorphologicalOps {
	
	public static void erodeNdilate(Mat Src, int erosion_Size, int dilation_size){
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, 
		         new Size(erosion_Size , erosion_Size));
		
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
		         new Size(dilation_size, dilation_size));
		
		Imgproc.erode(Src, Src, erodeElement);
		Imgproc.erode(Src, Src, erodeElement);
		
		Imgproc.dilate(Src, Src, dilateElement);
		Imgproc.dilate(Src, Src, dilateElement);
		
	}

}
