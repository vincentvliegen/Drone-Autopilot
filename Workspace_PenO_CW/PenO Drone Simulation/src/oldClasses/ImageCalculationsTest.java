package oldClasses;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ImageCalculationsTest{

    private ImageCalculations calc;

    private Camera camera1;
    private Camera camera2;
    private Camera camera3;
    private Camera camera4;
    private Camera camera5;
    private Camera camera6;
    private Camera bigCamera1;
    private Camera bigCamera2;
    private Camera bigCamera3;
    private Camera bigCamera4;
    private Camera bigCamera5;
    private Camera bigCamera6;
    
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
    
    private BufferedImage bigBImageCenter;
    private BufferedImage bigBImageTopRight;
    private BufferedImage bigBImageTopCenter;
    private BufferedImage bigBImageCenterLeft;
    private BufferedImage bigBImageBottomLeft;
    private BufferedImage bigBImageBottomRight;
    
    private int[] bigImageCenter;
    private int[] bigImageTopRight;
    private int[] bigImageTopCenter;
    private int[] bigImageCenterLeft;
    private int[] bigImageBottomLeft;
    private int[] bigImageBottomRight;
    
    private int index1;
    private int index4;
    private int index6;
   
    private int red;
    private int white;
	
	@Before
	public void setUp(){
		calc = new ImageCalculations();
		red = 255;
		white = 16777215;
	   
		pixellist1 = new ArrayList<int[]>(Arrays.asList(new int[0][0]));
		pixellist2 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{1,0},{2,0},{0,1},{1,1},{2,1},{0,2},{1,2},{2,2}}));
		pixellist3 = new ArrayList<int[]>(Arrays.asList(new int[][] {{4,3},{5,3},{3,4},{4,4},{5,4},{6,4},{3,5},{4,5},{5,5},{6,5},{4,6},{5,6}}));
		pixellist4 = new ArrayList<int[]>(Arrays.asList(new int[][] {{3,0},{4,0},{5,0},{3,1},{4,1},{5,1},{3,2},{4,2},{5,2}}));
		pixellist5 = new ArrayList<int[]>(Arrays.asList(new int[][] {{6,5},{7,5},{5,6},{6,6},{7,6},{8,6},{5,7},{6,7},{7,7},{8,7},{6,8},{7,8}}));
		pixellist6 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{1,0},{2,0},{3,0},{4,0},{0,1},{1,1},{2,1},{3,1},{4,1},{0,2},{1,2},{2,2},{3,2},{4,2},{0,3},{1,3},{2,3},{3,3},{4,3},{0,4},{1,4},{2,4},{3,4},{4,4}}));
	   
		index1 = 15;
		index4 = 73;
		index6 = 23;
	}
		
	public void generateSmallCameras(){
		image10x10Empty = new int[]    {white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white,
										white,white,white,white,white,white,white,white,white,white};
		image10x10Circle3InTopLeftCorner = new int[]   { red , red , red ,white,white,white,white,white,white,white,
														 red , red , red ,white,white,white,white,white,white,white,
														 red , red , red ,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white,
														white,white,white,white,white,white,white,white,white,white};
		image10x10Circle4InCenter = new int[]  {white,white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,white,
												white,white,white,white, red , red ,white,white,white,white,
												white,white,white, red , red , red , red ,white,white,white,
												white,white,white, red , red , red , red ,white,white,white,
												white,white,white,white, red , red ,white,white,white,white,
												white,white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,white,
												white,white,white,white,white,white,white,white,white,white};
		image9x9Circle3InTopMiddle = new int[] {white,white,white, red , red , red ,white,white,white,
												white,white,white, red , red , red ,white,white,white,
												white,white,white, red , red , red ,white,white,white,
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
														white,white,white,white,white,white, red , red ,white,
														white,white,white,white,white, red , red , red , red ,
														white,white,white,white,white, red , red , red , red ,
														white,white,white,white,white,white, red , red ,white};
		image5x5Red = new int[]    {red,red,red,red,red,
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

	}
	
	public void generateBigCameras() throws IOException{
		bigBImageCenter = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/center1024x1024.bmp"));
		bigBImageTopRight = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/topright2200x200.bmp"));
		bigBImageTopCenter = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/topcenter1000xn50.bmp"));
		bigBImageCenterLeft = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/centerleft25x775.bmp"));
		bigBImageBottomLeft = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/bottomleft250x2150.bmp"));
		bigBImageBottomRight = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/bottomright1600x2175.bmp"));

		bigImageCenter = convertImageToIntArray(bigBImageCenter);
		bigImageTopRight = convertImageToIntArray(bigBImageTopRight);
		bigImageTopCenter = convertImageToIntArray(bigBImageTopCenter);
		bigImageCenterLeft = convertImageToIntArray(bigBImageCenterLeft);
		bigImageBottomLeft = convertImageToIntArray(bigBImageBottomLeft);
		bigImageBottomRight = convertImageToIntArray(bigBImageBottomRight);
		
		bigCamera1 = createCameraForTesting(bigImageCenter, 1, 1, 2048);
		bigCamera2 = createCameraForTesting(bigImageTopRight, 1, 1, 2048);
		bigCamera3 = createCameraForTesting(bigImageTopCenter, 1, 1, 2048);
		bigCamera4 = createCameraForTesting(bigImageCenterLeft, 1, 1, 2048);
		bigCamera5 = createCameraForTesting(bigImageBottomLeft, 1, 1, 2048);
		bigCamera6 = createCameraForTesting(bigImageBottomRight, 1, 1, 2048);
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
 
	public int[] convertImageToIntArray(BufferedImage image){
		byte[] allPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int pixelLength = 3;
		int[] pixels = new int[allPixels.length/pixelLength];
        for (int i = 0; i < allPixels.length; i += pixelLength) {
        	int brg = 0; //correctie
            brg += ((int) allPixels[i + 2] & 0xff); // blue
            brg += (((int) allPixels[i + 1] & 0xff) << 8); // green
            brg += (((int) allPixels[i] & 0xff) << 16); // red
            pixels[i/pixelLength] = brg;
        }
		return pixels;
	}

	
	
	//TESTS
	
	@Test
    public void GetRedPixelsInImageTest(){
		generateSmallCameras();
		assertArrayEquals(pixellist1.toArray(new int[0][0]), calc.getPixelsOfColor(camera1, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist2.toArray(new int[0][0]), calc.getPixelsOfColor(camera2, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist3.toArray(new int[0][0]), calc.getPixelsOfColor(camera3, red) .toArray(new int[0][0]));
		assertArrayEquals(pixellist4.toArray(new int[0][0]), calc.getPixelsOfColor(camera4, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist5.toArray(new int[0][0]), calc.getPixelsOfColor(camera5, red).toArray(new int[0][0]));
		assertArrayEquals(pixellist6.toArray(new int[0][0]), calc.getPixelsOfColor(camera6, red).toArray(new int[0][0]));
	}
	
	@Test
    public void cOGTest() throws EmptyPositionListException {
			assertArrayEquals(new float[] {1,1}, calc.getCOG(pixellist2),(float) 0.0001);
			assertArrayEquals(new float[] {(float) 4.5,(float) 4.5}, calc.getCOG(pixellist3),(float) 0.0001);
			assertArrayEquals(new float[] {4,1}, calc.getCOG(pixellist4),(float) 0.0001);
			assertArrayEquals(new float[] {(float) 6.5,(float) 6.5}, calc.getCOG(pixellist5),(float) 0.0001);
			assertArrayEquals(new float[] {2,2}, calc.getCOG(pixellist6),(float) 0.0001);
    }
	
	@Test(expected = EmptyPositionListException.class)
	public void cOGOfEmptyList() throws EmptyPositionListException {
		calc.getCOG(pixellist1);
	}
	
	@Test
	public void indexToCoordinatesTest(){
		generateSmallCameras();
		assertArrayEquals(new int[] {5,1}, calc.indexToCoordinates(index1, camera1));
		assertArrayEquals(new int[] {1,8}, calc.indexToCoordinates(index4, camera4));
		assertArrayEquals(new int[] {3,4}, calc.indexToCoordinates(index6, camera6));
	}
	
	@Test(expected = SmallCircleException.class)
	public void pointsOnCirExcSCTest() throws SmallCircleException,EmptyPositionListException{
		generateSmallCameras();
		calc.pointsOnCircumference(pixellist2, camera2);
		calc.pointsOnCircumference(pixellist3, camera3);
		calc.pointsOnCircumference(pixellist4, camera4);
		calc.pointsOnCircumference(pixellist5, camera5);
		calc.pointsOnCircumference(pixellist6, camera6);
	}
	
	@Test(expected = EmptyPositionListException.class)
	public void pointsOnCirExcEPTest() throws SmallCircleException,EmptyPositionListException{
		generateSmallCameras();
		calc.pointsOnCircumference(pixellist1, camera1);
	}
	
	@Test
	public void centerOfCircleTest() throws SmallCircleException, EmptyPositionListException, IOException{
		generateBigCameras();
		ArrayList<int[]> list1 = calc.getPixelsOfColor(bigCamera1, 255);
		ArrayList<int[]> list2 = calc.getPixelsOfColor(bigCamera2, 255);
		ArrayList<int[]> list3 = calc.getPixelsOfColor(bigCamera3, 255);
		ArrayList<int[]> list4 = calc.getPixelsOfColor(bigCamera4, 255);
		ArrayList<int[]> list5 = calc.getPixelsOfColor(bigCamera5, 255);
		ArrayList<int[]> list6 = calc.getPixelsOfColor(bigCamera6, 255);
		assertArrayEquals(new float[] {1024,1024}, calc.centerOfCircle(list1, bigCamera1) ,(float) 2);
		assertArrayEquals(new float[] {2200,200}, calc.centerOfCircle(list2, bigCamera2) ,(float) 2);
		assertArrayEquals(new float[] {1000,-50}, calc.centerOfCircle(list3, bigCamera3) ,(float) 2);
		assertArrayEquals(new float[] {25,775}, calc.centerOfCircle(list4, bigCamera4) ,(float) 2);
		assertArrayEquals(new float[] {250,2150}, calc.centerOfCircle(list5, bigCamera5) ,(float) 2);
		assertArrayEquals(new float[] {1600,2175}, calc.centerOfCircle(list6, bigCamera6) ,(float) 2);
	}
	
	@Test
	public void cogComparison() throws SmallCircleException,EmptyPositionListException, IOException{
		generateBigCameras();
		ArrayList<int[]> list1 = calc.getPixelsOfColor(bigCamera1, 255);
		ArrayList<int[]> list2 = calc.getPixelsOfColor(bigCamera2, 255);
		ArrayList<int[]> list3 = calc.getPixelsOfColor(bigCamera3, 255);
		ArrayList<int[]> list4 = calc.getPixelsOfColor(bigCamera4, 255);
		ArrayList<int[]> list5 = calc.getPixelsOfColor(bigCamera5, 255);
		ArrayList<int[]> list6 = calc.getPixelsOfColor(bigCamera6, 255);
		
		float[] position1 = {1024,1024};
		float[] position2 = {2200,200};
		float[] position3 = {1000,-50};
		float[] position4 = {25,775};
		float[] position5 = {250,2150};
		float[] position6 = {1600,2175};
		
		float radius1 = 300;
		float radius2 = 600;
		float radius3 = 250;
		float radius4 = 100;
		float radius5 = 350;
		float radius6 = 400;

		System.out.println("----------------------------");		
		System.out.println("CIRCLE 1");
		System.out.println("position: {" + position1[0] + "," + position1[1] + "}");
		System.out.println("radius: " + radius1);
		float[] coc1 = calc.centerOfCircle(list1, bigCamera1);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc1[0] + "," + coc1[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position1[0]-coc1[0]) + "," + (position1[1]-coc1[1]) + "}");
		System.out.println("	radius deviation: " + (radius1 - calc.getRadius()));
		float[] cog1 = calc.getCOG(list1);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog1[0] + "," + cog1[1] + "}");
		System.out.println("	position deviation: {" + (position1[0]-cog1[0]) + "," + (position1[1]-cog1[1]) + "}");

		System.out.println("----------------------------");		
		System.out.println("CIRCLE 2");
		System.out.println("position: {" + position2[0] + "," + position2[1] + "}");
		System.out.println("radius: " + radius2);
		float[] coc2 = calc.centerOfCircle(list2, bigCamera2);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc2[0] + "," + coc2[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position2[0]-coc2[0]) + "," + (position2[1]-coc2[1]) + "}");
		System.out.println("	radius deviation: " + (radius2 - calc.getRadius()));
		float[] cog2 = calc.getCOG(list2);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog2[0] + "," + cog2[1] + "}");
		System.out.println("	position deviation: {" + (position2[0]-cog2[0]) + "," + (position2[1]-cog2[1]) + "}");
		
		System.out.println("----------------------------");		
		System.out.println("CIRCLE 3");
		System.out.println("position: {" + position3[0] + "," + position3[1] + "}");
		System.out.println("radius: " + radius3);
		float[] coc3 = calc.centerOfCircle(list3, bigCamera3);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc3[0] + "," + coc3[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position3[0]-coc3[0]) + "," + (position3[1]-coc3[1]) + "}");
		System.out.println("	radius deviation: " + (radius3 - calc.getRadius()));
		float[] cog3 = calc.getCOG(list3);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog3[0] + "," + cog3[1] + "}");
		System.out.println("	position deviation: {" + (position3[0]-cog3[0]) + "," + (position3[1]-cog3[1]) + "}");
		
		System.out.println("----------------------------");		
		System.out.println("CIRCLE 4");
		System.out.println("position: {" + position4[0] + "," + position4[1] + "}");
		System.out.println("radius: " + radius4);
		float[] coc4 = calc.centerOfCircle(list4, bigCamera4);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc4[0] + "," + coc4[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position4[0]-coc4[0]) + "," + (position4[1]-coc4[1]) + "}");
		System.out.println("	radius deviation: " + (radius4 - calc.getRadius()));
		float[] cog4 = calc.getCOG(list4);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog4[0] + "," + cog4[1] + "}");
		System.out.println("	position deviation: {" + (position4[0]-cog4[0]) + "," + (position4[1]-cog4[1]) + "}");
		
		System.out.println("----------------------------");		
		System.out.println("CIRCLE 5");
		System.out.println("position: {" + position5[0] + "," + position5[1] + "}");
		System.out.println("radius: " + radius5);
		float[] coc5 = calc.centerOfCircle(list5, bigCamera5);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc5[0] + "," + coc5[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position5[0]-coc5[0]) + "," + (position5[1]-coc5[1]) + "}");
		System.out.println("	radius deviation: " + (radius5 - calc.getRadius()));
		float[] cog5 = calc.getCOG(list5);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog5[0] + "," + cog5[1] + "}");
		System.out.println("	position deviation: {" + (position5[0]-cog5[0]) + "," + (position5[1]-cog5[1]) + "}");
		
		System.out.println("----------------------------");		
		System.out.println("CIRCLE 6");
		System.out.println("position: {" + position6[0] + "," + position6[1] + "}");
		System.out.println("radius: " + radius6);
		float[] coc6 = calc.centerOfCircle(list6, bigCamera6);
	    System.out.println("----------------------------");
		System.out.println("	CENTERCIRCLE");
		System.out.println("	calculated position: {" + coc6[0] + "," + coc6[1] + "}");
		System.out.println("	calculated radius: " + calc.getRadius());
		System.out.println("	position deviation: {" + (position6[0]-coc6[0]) + "," + (position6[1]-coc6[1]) + "}");
		System.out.println("	radius deviation: " + (radius6 - calc.getRadius()));
		float[] cog6 = calc.getCOG(list6);
	    System.out.println("----------------------------");
		System.out.println("	CENTERGRAVITY");
		System.out.println("	calculated position: {" + cog6[0] + "," + cog6[1] + "}");
		System.out.println("	position deviation: {" + (position6[0]-cog6[0]) + "," + (position6[1]-cog6[1]) + "}");
		
		
		//sqrt(x^2+y^2)= distance
		//circle 1
		assertTrue(Math.sqrt(Math.pow(position1[0]-cog1[0],2)+Math.pow(position1[1]-cog1[1],2)) >= Math.sqrt(Math.pow(position1[0]-coc1[0],2)+Math.pow(position1[1]-coc1[1],2)));
		//circle 2
		assertTrue(Math.sqrt(Math.pow(position2[0]-cog2[0],2)+Math.pow(position2[1]-cog2[1],2)) >= Math.sqrt(Math.pow(position2[0]-coc2[0],2)+Math.pow(position2[1]-coc2[1],2)));		
		//circle 3
		assertTrue(Math.sqrt(Math.pow(position3[0]-cog3[0],2)+Math.pow(position3[1]-cog3[1],2)) >= Math.sqrt(Math.pow(position3[0]-coc3[0],2)+Math.pow(position3[1]-coc3[1],2)));
		//circle 4
		assertTrue(Math.sqrt(Math.pow(position4[0]-cog4[0],2)+Math.pow(position4[1]-cog4[1],2)) >= Math.sqrt(Math.pow(position4[0]-coc4[0],2)+Math.pow(position4[1]-coc4[1],2)));
		//circle 5
		assertTrue(Math.sqrt(Math.pow(position5[0]-cog5[0],2)+Math.pow(position5[1]-cog5[1],2)) >= Math.sqrt(Math.pow(position5[0]-coc5[0],2)+Math.pow(position5[1]-coc5[1],2)));
		//circle 6
		assertTrue(Math.sqrt(Math.pow(position6[0]-cog6[0],2)+Math.pow(position6[1]-cog6[1],2)) >= Math.sqrt(Math.pow(position6[0]-coc6[0],2)+Math.pow(position6[1]-coc6[1],2)));
		
		
	}
	
	@Test(expected = EmptyPositionListException.class)
	public void centerOfCircleExcEPTest() throws SmallCircleException,EmptyPositionListException{
		generateSmallCameras();
		calc.centerOfCircle(pixellist1, camera1);
	}
	
	@Test(expected = SmallCircleException.class)
	public void centerOfCircleExcSCTest() throws SmallCircleException,EmptyPositionListException{
		generateSmallCameras();
		calc.centerOfCircle(pixellist2, camera2);
		calc.centerOfCircle(pixellist3, camera3);
		calc.centerOfCircle(pixellist4, camera4);
		calc.centerOfCircle(pixellist5, camera5);
		calc.centerOfCircle(pixellist6, camera6);
	}
		
}
