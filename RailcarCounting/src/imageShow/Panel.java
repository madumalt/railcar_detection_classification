package imageShow;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JPanel;

import org.opencv.core.Mat;

public class Panel extends JPanel{  
	  private static final long serialVersionUID = 1L;  
	  private BufferedImage image;    
	  // Create a constructor method  
	  public Panel(){  
	    super();  
	  }  
	  private BufferedImage getimage(){  
	    return image;  
	  }  
	  public void setimage(BufferedImage newimage){  
	    image=newimage;  
	    return;  
	  }  
	  public void setimagewithMat(Mat newimage){  
	    image=Panel.matToBufferedImage(newimage);  
	    return;  
	  }  
	  
	  public static BufferedImage matToBufferedImage(Mat m){
		  // source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
		  // Fastest code
		  // The output can be assigned either to a BufferedImage or to an Image
			
		  int type = BufferedImage.TYPE_BYTE_GRAY;
		  if ( m.channels() > 1 ) {
			  type = BufferedImage.TYPE_3BYTE_BGR;
		  }
		  int bufferSize = m.channels()*m.cols()*m.rows();
		  byte [] b = new byte[bufferSize];
		  m.get(0,0,b); // get all the pixels
		  BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		  final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		  System.arraycopy(b, 0, targetPixels, 0, b.length);  
		  return image;
	  }
	  
	  @Override  
	  protected void paintComponent(Graphics g){  
	     super.paintComponent(g);  
	     //BufferedImage temp=new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);  
	     BufferedImage temp=getimage();  
	     //Graphics2D g2 = (Graphics2D)g;
	     if( temp != null)
	       g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);  
	  }  
}
