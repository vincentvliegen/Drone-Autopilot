package testopencv;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class lol {

	public final static void main(String[] args) {
		int red = 255;
		int white = 16777215;
		
		int[] image10x10Circle4InCenter = new int[] {white,white,white,white,white,white,white,white,white,white,
				   									white,white,white,white,white,white,white,white,white,white,
				   									white,white,white,white,white,white,white,white,white,white,
				   									white,white,white,white,red  ,red  ,white,white,white,white,
				   									white,white,white,red  ,red  ,red  ,red  ,white,white,white,
				   									white,white,white,red  ,red  ,red  ,red  ,white,white,white,
				   									white,white,white,red  ,red  ,red  ,red  ,white,white,white,
				   									white,white,white,white,red  ,red  ,white,white,white,white,
				   									white,white,white,white,white,white,white,white,white,white,
				   									white,white,white,white,white,white,white,white,white,white};

		
        
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        Mat mat = new Mat();
        mat.put(0,0, image10x10Circle4InCenter);
        	
		
		
		Mat gray = new Mat();
		Imgproc.cvtColor(mat, gray, Imgproc.COLOR_RGBA2GRAY);

		Imgproc.Canny(gray, gray, 50, 200);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		// find contours:
		Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
		for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
		    Imgproc.drawContours(mat, contours, contourIdx, new Scalar(0, 0, 255), -1);
		}
        
 
        
        
	}
	 	 
}
