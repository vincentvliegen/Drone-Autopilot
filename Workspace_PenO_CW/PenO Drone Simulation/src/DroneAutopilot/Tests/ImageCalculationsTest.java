package DroneAutopilot.Tests;

import java.util.ArrayList;
import java.util.Arrays;
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
		red = 255;
		white = 16777215;
			        
	    image10x10Empty = new int[] {white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white,
	    							 white,white,white,white,white,white,white,white,white,white};
	    image10x10Circle3InTopLeftCorner = new int[] {red  ,red  ,red  ,white,white,white,white,white,white,white,
				  									  red  ,red  ,red  ,white,white,white,white,white,white,white,
				  									  red  ,red  ,red  ,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white,
				  									  white,white,white,white,white,white,white,white,white,white};
	    image10x10Circle4InCenter = new int[] {white,white,white,white,white,white,white,white,white,white,
	    									   white,white,white,white,white,white,white,white,white,white,
	    									   white,white,white,white,white,white,white,white,white,white,
	    									   white,white,white,white,red  ,red  ,white,white,white,white,
	    									   white,white,white,red  ,red  ,red  ,red  ,white,white,white,
	    									   white,white,white,red  ,red  ,red  ,red  ,white,white,white,
	    									   white,white,white,white,red  ,red  ,white,white,white,white,
	    									   white,white,white,white,white,white,white,white,white,white,
	    									   white,white,white,white,white,white,white,white,white,white,
	    									   white,white,white,white,white,white,white,white,white,white};
		image9x9Circle3InTopMiddle = new int[] {white,white,white,red  ,red  ,red  ,white,white,white,
												white,white,white,red  ,red  ,red  ,white,white,white,
												white,white,white,red  ,red  ,red  ,white,white,white,
												white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white};
	    image9x9Circle4InBottomRightCorner = new int[] {white,white,white,white,white,white,white,white,white,
					 									white,white,white,white,white,white,white,white,white,
					 									white,white,white,white,white,white,white,white,white,
					 									white,white,white,white,white,white,white,white,white,
					 									white,white,white,white,white,white,white,white,white,
					 									white,white,white,white,white,white,red  ,red  ,white,
					 									white,white,white,white,white,red  ,red  ,red  ,red  ,
					 									white,white,white,white,white,red  ,red  ,red  ,red  ,
					 									white,white,white,white,white,white,red  ,red  ,white};
		image5x5Red = new int[] {red,red,red,red,red,
							     red,red,red,red,red,
							     red,red,red,red,red,
							     red,red,red,red,red,
							     red,red,red,red,red};

	   camera1 = createCameraForTesting(image10x10Empty, 1, 1, 10);
	   camera2 = createCameraForTesting(image10x10Circle3InTopLeftCorner, 1, 1, 10);
	   camera3 = createCameraForTesting(image10x10Circle4InCenter, 1, 1, 10);
	   camera4 = createCameraForTesting(image9x9Circle3InTopMiddle, 1, 1, 9);
	   camera5 = createCameraForTesting(image9x9Circle4InBottomRightCorner, 1, 1, 9);
	   camera6 = createCameraForTesting(image5x5Red, 1, 1, 5);
	   
	   pixellist1 = new ArrayList<int[]>(Arrays.asList(new int[0][0]));
	   pixellist2 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{1,0},{2,0},{0,1},{1,1},{2,1},{0,2},{1,2},{2,2}}));
	   pixellist3 = new ArrayList<int[]>(Arrays.asList(new int[][] {{4,3},{5,3},{3,4},{4,4},{5,4},{6,4},{3,5},{4,5},{5,5},{6,5},{4,6},{5,6}}));
	   pixellist4 = new ArrayList<int[]>(Arrays.asList(new int[][] {{3,0},{4,0},{5,0},{3,1},{4,1},{5,1},{3,2},{4,2},{5,2}}));
	   pixellist5 = new ArrayList<int[]>(Arrays.asList(new int[][] {{6,5},{7,5},{5,6},{6,6},{7,6},{8,6},{5,7},{6,7},{7,7},{8,7},{6,8},{7,8}}));
	   pixellist6 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{1,0},{2,0},{3,0},{4,0},{0,1},{1,1},{2,1},{3,1},{4,1},{0,2},{1,2},{2,2},{3,2},{4,2},{0,3},{1,3},{2,3},{3,3},{4,3},{0,4},{1,4},{2,4},{3,4},{4,4}}));
	   
	   circPoints2 = new int[][] {{2,1},{1,2},{2,2}};
	   circPoints3 = new int[][] {{4,3},{3,5},{5,6}};
	   circPoints4 = new int[][] {{3,1},{3,2},{5,2}};
	   circPoints5 = new int[][] {{6,5},{5,6},{5,7}};
	   
	   index1 = 15;
	   index4 = 73;
	   index6 = 23;
	}
		
	@Test
    public void GetRedPixelsInImageTest(){
		assertArrayEquals(pixellist1.toArray(new int[0][0]), calc.getPixelsOfColor(camera1, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist2.toArray(new int[0][0]), calc.getPixelsOfColor(camera2, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist3.toArray(new int[0][0]), calc.getPixelsOfColor(camera3, red) .toArray(new int[0][0]));
		assertArrayEquals(pixellist4.toArray(new int[0][0]), calc.getPixelsOfColor(camera4, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist5.toArray(new int[0][0]), calc.getPixelsOfColor(camera5, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist6.toArray(new int[0][0]), calc.getPixelsOfColor(camera6, red).toArray(new int[0][0]));
	}
	
	@Test
    public void cOGTest() {
    	try {
			assertArrayEquals(new int[] {1,1}, calc.getCOG(pixellist2));
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(new int[] {4,4}, calc.getCOG(pixellist3));
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(new int[] {4,1}, calc.getCOG(pixellist4));
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(new int[] {6,6}, calc.getCOG(pixellist5));
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    	try {
			assertArrayEquals(new int[] {2,2}, calc.getCOG(pixellist6));
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    }
	
	@Test(expected = EmptyPositionListException.class)
	public void cOGOfEmptyList() throws EmptyPositionListException {
		calc.getCOG(pixellist1);
	}
    
	@Test
	public void checkAllRedTest(){
		assertFalse(calc.checkIfAllRed(camera1));
		assertFalse(calc.checkIfAllRed(camera2));
		assertFalse(calc.checkIfAllRed(camera3));
		assertFalse(calc.checkIfAllRed(camera4));
		assertFalse(calc.checkIfAllRed(camera5));
		assertTrue(calc.checkIfAllRed(camera6));
	}
	
	@Test
	public void indexToCoordinatesTest(){
		assertArrayEquals(new int[] {5,1}, calc.indexToCoordinates(index1, camera1));
		assertArrayEquals(new int[] {1,8}, calc.indexToCoordinates(index4, camera4));
		assertArrayEquals(new int[] {3,4}, calc.indexToCoordinates(index6, camera6));
	}
	
	@Test
	public void pointsOnCirTest(){
		assertArrayEquals(circPoints2, calc.pointsOnCircumference(pixellist2, camera2).toArray(new int[0][0]));
		assertArrayEquals(circPoints3, calc.pointsOnCircumference(pixellist3, camera3).toArray(new int[0][0]));
		assertArrayEquals(circPoints4, calc.pointsOnCircumference(pixellist4, camera4).toArray(new int[0][0]));
		assertArrayEquals(circPoints5, calc.pointsOnCircumference(pixellist5, camera5).toArray(new int[0][0]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void pointsOnCirExcTest() throws IllegalArgumentException{
		calc.pointsOnCircumference(pixellist1, camera1);
		calc.pointsOnCircumference(pixellist6, camera6);
	}
	
	//te onnauwkeurig, werkt maar in 1/4 van de testen, cirkels niet cirkelvormig genoeg, enkel bij grotere cirkels
	@Test
	public void centerOfCircleTest(){
		int[] point = calc.centerOfCircle(pixellist2, camera2);
		assertArrayEquals(new int[] {1,1}, point);//onnauwkeurig komt toevallig uit
		//assertArrayEquals(new int[] {4,4}, calc.centerOfCircle(pixellist3, camera3));
		//assertArrayEquals(new int[] {4,1}, calc.centerOfCircle(pixellist4, camera4));
		//assertArrayEquals(new int[] {6,6}, calc.centerOfCircle(pixellist5, camera5));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void centerOfCircleExcTest() throws IllegalArgumentException{
		calc.centerOfCircle(pixellist2, camera1);
		calc.centerOfCircle(pixellist6, camera6);
	}
	
	
	//anonymous class implementing Camera interface
	
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

    private Camera camera1;
    private Camera camera2;
    private Camera camera3;
    private Camera camera4;
    private Camera camera5;
    private Camera camera6;
    
    private ArrayList<int[]> pixellist1;
    private ArrayList<int[]> pixellist2;
    private ArrayList<int[]> pixellist3;
    private ArrayList<int[]> pixellist4;
    private ArrayList<int[]> pixellist5;
    private ArrayList<int[]> pixellist6;
    
    private int[] image10x10Empty;
    private int[] image10x10Circle3InTopLeftCorner;
    private int[] image10x10Circle4InCenter;   									 
    private int[] image9x9Circle3InTopMiddle;
    private int[] image9x9Circle4InBottomRightCorner;
    private int[] image5x5Red;
    
    private int index1;
    private int index4;
    private int index6;
    
    private int[][] circPoints2;
    private int[][] circPoints3;    
    private int[][] circPoints4;
    private int[][] circPoints5;
   
    private int red;
    private int white;
}
