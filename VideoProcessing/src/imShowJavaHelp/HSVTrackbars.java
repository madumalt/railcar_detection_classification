package imShowJavaHelp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSVTrackbars extends JFrame{
	
	public static int H_MIN = 0;
	public static int H_MAX = 255;
	public static int S_MIN = 0;
	public static int S_MAX = 255;
	public static int V_MIN = 0;
	public static int V_MAX = 255;
	
	public HSVTrackbars() {
	    getContentPane().add(new TColor());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(800, 400);
	    setVisible(true);
	  }
}

class TColor extends JPanel {
	 // DrawingCanvas canvas = new DrawingCanvas();
	  JLabel rgbValue = new JLabel("");

	  JSlider sliderH_min, sliderH_max, sliderS_min, sliderS_max, sliderV_min, sliderV_max;

	  public TColor() {
		  sliderH_min = getSlider(0, 255, HSVTrackbars.H_MIN, 50, 1);
		  sliderH_max = getSlider(0, 255, HSVTrackbars.H_MAX, 50, 1);
		  sliderS_min = getSlider(0, 255, HSVTrackbars.S_MIN, 50, 1);
		  sliderS_max = getSlider(0, 255, HSVTrackbars.S_MAX, 50, 1);
		  sliderV_min = getSlider(0, 255, HSVTrackbars.V_MIN, 50, 1);
		  sliderV_max = getSlider(0, 255, HSVTrackbars.V_MAX, 50, 1);

	    JPanel panel = new JPanel();
	    panel.setLayout(new GridLayout(5, 3, 10, 20));

	    panel.add(new JLabel(""));
	    panel.add(new JLabel("MIN"));
	    panel.add(new JLabel("MAX"));
	    
	    panel.add(new JLabel("H"));
	    panel.add(sliderH_min);
	    panel.add(sliderH_max);
	    
	    panel.add(new JLabel("S"));
	    panel.add(sliderS_min);
	    panel.add(sliderS_max);
	    
	    panel.add(new JLabel("V"));
	    panel.add(sliderV_min);
	    panel.add(sliderV_max);

	    add(panel, BorderLayout.SOUTH);
	  }
	  
	  public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp) {
		    JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
		    slider.setPaintTicks(true);
		    slider.setMajorTickSpacing(mjrTkSp);
		    slider.setMinorTickSpacing(mnrTkSp);
		    slider.setPaintLabels(true);
		    slider.addChangeListener(new SliderListener());
		    return slider;
		  }

class SliderListener implements ChangeListener {
	
    public void stateChanged(ChangeEvent e) {
      JSlider slider = (JSlider) e.getSource();

      //sliderH_min, sliderH_max, sliderS_min, sliderS_max, sliderV_min, sliderV_max;
      
      if (slider == sliderH_min) {
    	  System.out.println("sliderH_min : " + slider.getValue());
    	  HSVTrackbars.H_MIN = slider.getValue();
      } else if (slider == sliderH_max) {
    	  System.out.println("sliderH_max : " + slider.getValue());
    	  HSVTrackbars.H_MAX = slider.getValue();
      } else if (slider == sliderS_min) {
    	  System.out.println("sliderS_min : " + slider.getValue());
    	  HSVTrackbars.S_MIN = slider.getValue();
      } else if (slider == sliderS_max) {
    	  System.out.println("sliderS_max : " + slider.getValue());
    	  HSVTrackbars.S_MAX = slider.getValue();
      } else if (slider == sliderV_min) {
    	  System.out.println("sliderV_min : " + slider.getValue());
    	  HSVTrackbars.V_MIN = slider.getValue();
      } else if (slider == sliderV_max) {
    	  System.out.println("sliderV_max : " + slider.getValue());
    	  HSVTrackbars.V_MAX = slider.getValue();
      }
     
    }
}

}


	  
	  
