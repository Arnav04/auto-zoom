import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import java.util.Arrays;
import java.awt.Robot;

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
	
 
	public static void findlefteye() throws Exception {
		/*
		 * Initializing Eye Classifier from OpenCV
		 * Change the path name as needed
		 */
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		CascadeClassifier cascadeEyeClassifier = new CascadeClassifier("/Users/rohil/OpenCV-Bundle/opencv-4.0.1/data/haarcascades/haarcascade_eye.xml");

		/*
		 * Initializing Video Capture Device
		 */
		VideoCapture videoDevice = new VideoCapture();
		videoDevice.open(0);
		
		/*
		 * Initializing Eye Rectangle Data
		 */
		int rectx = 0;
		int recty = 0;
		
		/*
		 * Main Video Loop
		 * Else condition asks if video device is on
		 */
		if (videoDevice.isOpened()) {
			while (true) {
				/*
				 * Collecting image from computer camera
				 */
				Mat frameCapture = new Mat();
				videoDevice.read(frameCapture);

				/*
				 * Using Cascade Eye Classifier to dump all Eye Boxes into eyes (Mat)
				 */
				MatOfRect eyes = new MatOfRect();
				cascadeEyeClassifier.detectMultiScale(frameCapture, eyes);	
				
				/*
				 * Converting eyes (Mat) to an array for ease of use later in the program
				 */
				Rect[] eyesarray = eyes.toArray();
				
				/*
				 * Finding the number of valid eyes
				 */
				int len = 0;
				for (int i = 0; i < eyesarray.length; i++) {
					if (isValid(eyesarray[i])) {
						len++;
					}
				}
				
				
				Rect[] validrectangles = new Rect[len];
				
				int j = 0;
				for (int i = 0; i < eyesarray.length; i++) {
					if (isValid(eyesarray[i])) {
						validrectangles[j] = eyesarray[i];
						System.out.println(validrectangles[j]);
						j++;
					}
				}
				
				System.out.println("mid");
				
				
				int[] rectpos = new int[len];
				for (int i = 0; i < validrectangles.length; i++) {
					rectpos[i] = validrectangles[i].x + ((validrectangles[i].y-1)*1080);
					System.out.println(rectpos[i]);
				}
				System.out.println("\n");
				
				//Turning feed (Mat) into a Buffered Image
				BufferedImage currentimage = ConvertMat2Image(frameCapture);
				
				if (rectpos.length == 2) {
					Rect rect = validrectangles[0];
					BufferedImage lefteye = currentimage.getSubimage(rect.x, rect.y, rect.width, rect.height);
					//Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(200, 200, 100),2);

					rect = validrectangles[1];
					BufferedImage righteye = currentimage.getSubimage(rect.x, rect.y, rect.width, rect.height);
					//Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(200, 200, 100),2);
				
					File path = new File("/Users/rohil/Desktop/result/firstcapture.png");
					ImageIO.write(lefteye, "PNG", path);
					return;
					
				} else if (rectpos.length > 2) {
					int firsteyeindex = findMinDiffPairs(rectpos, rectpos.length);
					
					System.out.println(firsteyeindex);
					
					Rect rect = validrectangles[firsteyeindex];
					BufferedImage lefteye = currentimage.getSubimage(rect.x, rect.y, rect.width, rect.height);
					//Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(200, 200, 100),2);

					rect = validrectangles[firsteyeindex+1];
					BufferedImage righteye = currentimage.getSubimage(rect.x, rect.y, rect.width, rect.height);
					//Imgproc.rectangle(frameCapture, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(200, 200, 100),2);
				
					File path = new File("/Users/rohil/Desktop/result/firstcapture.png");
					ImageIO.write(lefteye, "PNG", path);
					return;

				}
				
				

				
				//Find box and center
				org.opencv.core.Point box = new org.opencv.core.Point(rectx, recty);
				
				//System.out.println(box);
				
				//moveMouse(rectx, recty+44);
				//Thread.sleep(3000);
				
				//Push current image to Screen
				PushImage(currentimage);
				
				//System.out.println(String.format("%s eyes detected.",eyes.toArray().length));
			}
			
		} else {
			System.out.println("Video device on? Check your hardware.");
		}
		
		return;
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
	    
		int[] original = new int[n];
	    for (int i = 0; i < arr.length; i++) {
	    	original[i] = arr[i];
	    }

        // Sort array elements 
        Arrays.sort(arr); 

        // Compare differences of adjacent 
        // pairs to find the minimum difference. 
        int minDiff = arr[1] - arr[0]; 
        for (int i = 2; i < n; i++) 
        minDiff = Math.min(minDiff, arr[i] - arr[i-1]); 

        // Traverse array again and print all pairs 
        // with difference as minDiff. 
        for (int i = 1; i < n; i++) {
            if ((arr[i] - arr[i-1]) == minDiff) {
            	int result = Arrays.binarySearch(original, arr[i-1]);
            	
            	if (result >= 0) {
            		return result;
            	} else {
            		return 0;
            	}
            }
        }
        return 0;
	}
	
	
	
    public static void moveMouse(int x, int y) throws Exception {
    	Robot robot = new Robot();
    	robot.mouseMove(x, y);
    }
    
    public static boolean isValid(Rect rect) {
    	return rect.width < 100 && rect.width > 50 && rect.height < 100 && rect.height > 50;
    }
}
