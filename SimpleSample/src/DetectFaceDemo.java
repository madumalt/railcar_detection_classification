import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class DetectFaceDemo {
	public static void run() {
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier("/home/thilina/Desktop/data/cascade.xml");
        Mat srcImage = Highgui.imread("/home/thilina/Desktop/TankCars2/pos/2.png");
        
        //gray-scaling srcImage
        Mat gsrcImage = new Mat();
        Imgproc.cvtColor(srcImage, gsrcImage, Imgproc.COLOR_BGR2GRAY); 
        
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(gsrcImage, faceDetections);

        System.out.println(String.format("Detected %d faces",
                faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(gsrcImage, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = "/home/thilina/Desktop/faceDetection1.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, gsrcImage);
    }
}
