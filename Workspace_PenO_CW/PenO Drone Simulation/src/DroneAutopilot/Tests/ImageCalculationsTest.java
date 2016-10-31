package DroneAutopilot.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import DroneAutopilot.ImageCalculations;
import exceptions.EmptyPositionListException;
import p_en_o_cw_2016.Camera;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ImageCalculationsTest{

	@Before
	public void setUp() {
		calc = new ImageCalculations();
		
	    pixelsPos2 = new ArrayList<int[]>(Arrays.asList(new int[][] {{73,75},{74,74},{74,75},{74,76},{75,75}}));
	        
	    image10x10Empty = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image10x10Square4InCenter = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image10x10Square3InTopLeftCorner = new int[] {ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image9x9Square4InBottomRightCorner = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red};
		image9x9Square3InTopMiddle = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};

	   camera1 = createCameraForTesting(image10x10Empty, 45, 45, 10);
	   camera2 = createCameraForTesting(image10x10Square3InTopLeftCorner, 45, 45, 10);
	   camera3 = createCameraForTesting(image10x10Square4InCenter, 45, 45, 10);
	   camera4 = createCameraForTesting(image9x9Square3InTopMiddle, 45, 45, 9);
	   camera5 = createCameraForTesting(image9x9Square4InBottomRightCorner, 45, 45, 9);	  
	   
	   pixellist1 = new ArrayList<int[]>(Arrays.asList(new int[0][0]));
	   pixellist2 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{1,0},{2,0},{0,1},{1,1},{2,1},{0,2},{1,2},{2,2}}));
	   pixellist3 = new ArrayList<int[]>(Arrays.asList(new int[][] {{3,3},{4,3},{5,3},{6,3},{3,4},{4,4},{5,4},{6,4},{3,5},{4,5},{5,5},{6,5},{3,6},{4,6},{5,6},{6,6}}));
	   pixellist4 = new ArrayList<int[]>(Arrays.asList(new int[][] {{3,0},{4,0},{5,0},{3,1},{4,1},{5,1},{3,2},{4,2},{5,2}}));
	   pixellist5 = new ArrayList<int[]>(Arrays.asList(new int[][] {{5,5},{6,5},{7,5},{8,5},{5,6},{6,6},{7,6},{8,6},{5,7},{6,7},{7,7},{8,7},{5,8},{6,8},{7,8},{8,8}}));

	    
	}
		
	@Test
    public void GetRedPixelsInImageTest(){
		assertArrayEquals(calc.getPixelsOfColor(camera1, ImageCalculations.red).toArray(new int[0][0]),pixellist1.toArray(new int[0][0]));
		assertArrayEquals(calc.getPixelsOfColor(camera2, ImageCalculations.red).toArray(new int[0][0]),pixellist2.toArray(new int[0][0]));
		assertArrayEquals(calc.getPixelsOfColor(camera3, ImageCalculations.red).toArray(new int[0][0]),pixellist3.toArray(new int[0][0]));
		assertArrayEquals(calc.getPixelsOfColor(camera4, ImageCalculations.red).toArray(new int[0][0]),pixellist4.toArray(new int[0][0]));
		assertArrayEquals(calc.getPixelsOfColor(camera5, ImageCalculations.red).toArray(new int[0][0]),pixellist5.toArray(new int[0][0]));
	}
	
	@Test
    public void cOGTest() {
    	try {
			assertArrayEquals(calc.getCOG(pixellist2),new int[] {1,1});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(calc.getCOG(pixellist3),new int[] {4,4});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(calc.getCOG(pixellist4),new int[] {4,1});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(calc.getCOG(pixellist5),new int[] {6,6});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    }
	
	@Test(expected = EmptyPositionListException.class)
	public void cOGOfEmptyList() throws EmptyPositionListException {
		calc.getCOG(pixellist1);
	}
    
	
	
	
	
	
	
	//create camera that returns given images
	public Camera createCameraForTesting(int[] image, int horAngle, int verAngle, int width){
		return new Camera(){
	    	
			@Override
	    	public int[] takeImage() {
	    		return image;
	    	}
	    	@Override
	    	public float getHorizontalAngleOfView() {
	    		return horAngle;
	    	}

	    	@Override
	    	public float getVerticalAngleOfView() {
	    		return verAngle;
	    	}

	    	@Override
	    	public int getWidth() {
	    		return width;
	    	}
	    };
	}
 
    private ImageCalculations calc;

    private ArrayList<int[]> pixelsPos2;
    
    
    private Camera camera1;
    private Camera camera2;
    private Camera camera3;
    private Camera camera4;
    private Camera camera5;
    
    private ArrayList<int[]> pixellist1;
    private ArrayList<int[]> pixellist2;
    private ArrayList<int[]> pixellist3;
    private ArrayList<int[]> pixellist4;
    private ArrayList<int[]> pixellist5;
    
    private int[] image10x10Empty;
    private int[] image10x10Square4InCenter;   									 
    private int[] image10x10Square3InTopLeftCorner;
    private int[] image9x9Square4InBottomRightCorner;
    private int[] image9x9Square3InTopMiddle;
}
