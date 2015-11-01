import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import masking.MaskHelper;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class MakingPositivesHelp {
	
	public static void cvtGrayAndBlur(String Url){
		ArrayList<String> ar = glblPath(Url);
		
		int count=0;
		for(String s: ar){
			System.out.println(s);
			
			//load the image
			Mat image = new Mat();
			image = Highgui.imread(s);
			
			if(image.empty()){
				  System.out.println("NUll Image..!!!"+ " Count: " + "	path: " + s);
			  }else{
				  System.out.println("got it..!!!");
				  
				  //converting to grayScale
				  Mat gray = new Mat();
				  Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
				  
				  //Blurring down the image
				  Imgproc.GaussianBlur(gray, gray, new Size(9,9),1.5);
				  
				  if(gray.empty()){System.out.println("Gray image empty..!!!");}
				  else Highgui.imwrite(s, gray);
			  }
			
			count++;
		}
	}

	public static ArrayList<String> glblPath(String url){
		ArrayList<String> paths = new ArrayList<String>();
		String data = null;
		
		File file = new File(url);
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			data = br.readLine();
			while(data != null){
				paths.add(data);
				data = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			
		}
		
		return paths;
	}
	
}
