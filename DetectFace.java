import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.util.Arrays;

/* CREDIT TO:
 * Contact Info:
 * mesutpiskin.com
 * mesutpiskin@outlook.com
 * 
 * See Also:
 * http://mesutpiskin.com/blog/opencv-ile-gui-uygulamalar.html
 * http://mesutpiskin.com/blog/opencv-matris-uzerinde-cizim-islemleri.html
 * http://mesutpiskin.com/blog/321.html
 * 
 * OpenCV version 4.0.1
*/

public class DetectFace {
 
	static JFrame frame;
	static JLabel label;
	static ImageIcon icon;
	
 
	public static void main(String[] args) throws Exception {
		//Initializing OpenCV Cascade Classifier
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CascadeClassifier cascadeEyeClassifier = new CascadeClassifier("/Users/rohil/OpenCV-Bundle/opencv-4.0.1/data/haarcascades/haarcascade_eye.xml");

		//Initializing Video Capture Device
		VideoCapture videoDevice = new VideoCapture();
		videoDevice.open(0);
		
		//Initializing Eye Box location data
		int rectx = 0;
		int recty = 0;
		int rectw = 0;
		int recth = 0;
		
		//Initalizing Pupil Finder/Mouse Mover
		findPupil fp = new findPupil();
		mouseMover mm = new mouseMover();
		
		//Main video loop
		if (videoDevice.isOpened()) {
			while (true) {
				//Collecting image from computer camera as a Mat
				Mat frameCapture = new Mat();
				videoDevice.read(frameCapture);
				
				//Using Cascade Eye Classifier to dump all Eye Boxes into the Mat eyes
				MatOfRect eyes = new MatOfRect();
				cascadeEyeClassifier.detectMultiScale(frameCapture, eyes);	
				
				Rect[] eyesarray = eyes.toArray();
				int[] rectpos = new int[eyesarray.length];
				for (int i = 0; i < eyesarray.length; i++) {
					rectpos[i] = eyesarray[i].x + ((eyesarray[i].y-1)*1080);
				}
				
				if (rectpos.length > 1) {
					int firsteye = Arrays.binarySearch(rectpos, findMinDiffPairs(rectpos, rectpos.length));

					//Looping through all Eye Boxes as a rect
					for (int i = firsteye; i <= firsteye + 1; i++) {
						
						Rect rect = eyesarray[i];
						
						//Simple filtering system to ignore garbage boxes
						if (rect.width < 100 
						 && rect.height < 100
						 && rect.width > 50
						 && rect.height > 50) {
	
							//Uncomment to show blue rectangles around eyes in pushed image
							Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(200, 200, 100),2);
							
							//Assigning rectangle location data to previously initialized variables
							rectx = rect.x;
							recty = rect.y;
							rectw = rect.width;
							recth = rect.height;
	
						}
	
					}
				}
				
				//Turning feed (Mat) into a Buffered Image
				BufferedImage currentimage = ConvertMat2Image(frameCapture);
				
				//Find box and center
				org.opencv.core.Point box = new org.opencv.core.Point(rectx, recty);
				org.opencv.core.Point pupil = fp.findcenter(currentimage, rectx, recty, rectw, recth);
				
				System.out.println(box);
				System.out.println(pupil);
				
				//mm.moveMouse(rectx, recty+44);
				//Thread.sleep(3000);
				
				//Push current image to Screen
				PushImage(currentimage);
				
				//System.out.println(String.format("%s eyes detected.",eyes.toArray().length));
			}
			
		} else {
			System.out.println("Video device on? Check your hardware.");
		}
	}
	
	
	private static BufferedImage ConvertMat2Image(Mat cameradata) {
		MatOfByte byteMatData = new MatOfByte();
		Imgcodecs.imencode(".jpg", cameradata, byteMatData);
		byte[] byteArray = byteMatData.toArray();
		
		BufferedImage image = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			image = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return image;
	}
  	
	
	public static void WindowReady() {
		frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(700, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void PushImage(Image img2) {
		if (frame == null)
			WindowReady();
		if (label != null)
			frame.remove(label);
		
		icon = new ImageIcon(img2);
		label = new JLabel();
		label.setIcon(icon);
		frame.add(label);
		frame.revalidate();
		
	}
	
	public static int findMinDiffPairs(int[] arr, int n) {
		// Sort array elements 
	    Arrays.sort(arr); 
	      
	    // Compare differences of adjacent 
	    // pairs to find the minimum difference. 
	    int minDiff = arr[1] - arr[0]; 
	    for (int i = 2; i < n; i++) {
	    	minDiff = Math.min(minDiff, arr[i] - arr[i-1]); 
	    }
	       
	    // Traverse array again and print all pairs 
	    // with difference as minDiff. 
	    for ( int i = 1; i < n; i++) { 
	        if ((arr[i] - arr[i-1]) == minDiff) 
	        { 
	           return arr[i-1];
	        }
	    }
	    return 0;
	  }
}