package matadorhacks;

// first download opencv then add an import

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

public class object_detection {	
	
	// image processing:
	
	Mat blurredImage = new Mat();
	Mat hsvImage = new Mat();
	Mat mask = new Mat();
	Mat morphOutput = new Mat();

	// remove some noise
	Imgproc.blur(frame, blurredImage, new Size(7, 7));

	// convert the frame to HSV
	Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
	
	// HSV Values:
	
	// threshold values of UI
	Scalar minValues = new Scalar(this.hueStart.getValue(), this.saturationStart.getValue(),
	this.valueStart.getValue());
	Scalar maxValues = new Scalar(this.hueStop.getValue(), this.saturationStop.getValue(),
	this.valueStop.getValue());

	// show the current selected HSV range
	String valuesToPrint = "Hue range: " + minValues.val[0] + "-" + maxValues.val[0]
	+ "\tSaturation range: " + minValues.val[1] + "-" + maxValues.val[1] + "\tValue range: "
	+ minValues.val[2] + "-" + maxValues.val[2];
	this.onFXThread(this.hsvValuesProp, valuesToPrint);

	// threshold HSV image to select iris
	Core.inRange(hsvImage, minValues, maxValues, mask);
	// show the partial output
	this.onFXThread(maskProp, this.mat2Image(mask));
	
	// morphological operators
	// dilate with large element, erode with small ones
	 Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(24, 24));
	 Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(12, 12));

	 Imgproc.erode(mask, morphOutput, erodeElement);
	 Imgproc.erode(mask, morphOutput, erodeElement);

	 Imgproc.dilate(mask, morphOutput, dilateElement);
	 Imgproc.dilate(mask, morphOutput, dilateElement);

	 // show the partial output
	 this.onFXThread(this.morphProp, this.mat2Image(morphOutput));
	 
	// init
	 List<MatOfPoint> contours = new ArrayList<>();
	 Mat hierarchy = new Mat();

	 // find contours
	 Imgproc.findContours(maskedImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

	 // if any contour exist...
	 if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
	 {
	         // for each contour, display it in blue
	         for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
	         {
	                 Imgproc.drawContours(frame, contours, idx, new Scalar(250, 0, 0));
	         }
	 }

	

	

}

}
