package DroneAutopilot.Tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import DroneAutopilot.calculations.PolyhedraCalculations;
import p_en_o_cw_2016.Camera;

public class PolyhedraCalculationsTests {
	
	private PolyhedraCalculations polyhedraCalc;
	private Camera camera1;
	private Camera camera2;
	private Camera camera3;
	private Camera camera3b;
	private BufferedImage poly1;
	private BufferedImage poly2;
	private BufferedImage poly3;
	private BufferedImage poly3b;
	private int[] polylist1;
	private int[] polylist2;
	private int[] polylist3;
	private int[] polylist3b;

	@Before
	public void setup(){
		polyhedraCalc = new PolyhedraCalculations();
	}

	public void createImage() throws IOException{
		poly1 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/polyhedra1.png"));
		polylist1 = convertImageToIntArray(poly1,331,222);
		camera1 = createCameraForTesting(polylist1, 1, 1, 331);
//		this.writeTakeImageToFile(polylist1,331,222);
		
		poly2 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/polyhedra2.png"));
		polylist2 = convertImageToIntArray(poly2,167,177);
		camera2 = createCameraForTesting(polylist2, 1, 1, 167);
//		this.writeTakeImageToFile(polylist2,167,177);
		
		poly3 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/testsimulator-left.png"));
		polylist3 = convertImageToIntArray(poly3,200,200);
		camera3 = createCameraForTesting(polylist3, 1, 1, 200);
		
		poly3b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/testsimulator-right.png"));
		polylist3b = convertImageToIntArray(poly3b,200,200);
		camera3b = createCameraForTesting(polylist3b, 1, 1, 200);

	}
	
	public int[] convertImageToIntArray(BufferedImage image, int width, int height){
		int[] result = new int[width*height];
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				Color mycolor = new Color(image.getRGB(j, i));
				result[i*width + j]=mycolor.getRGB()+16777216;
			}
		}
		return result;
	}
	
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
	
	public void writeTakeImageToFile(int[] temp, int width, int height) {
		  //int width = getWidth();
		  String path = "C:/Users/laura/Documents/zilver/Workspace_PenO_CW/PenO Drone Simulation/src/DroneAutopilot/Tests/imagesForTests/test.png";
		  File file = new File(path); // The file to save to.
		  String format = "PNG"; // Example: "PNG" or "JPG"


		  BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		  for(int x = 0; x < width; x++)
		  {
		    for(int y = 0; y < height; y++)
		    {

		      image.setRGB(x, y, temp[y*width+x]);

		      //image.setRGB(x, getHeight() - (y + 1), (0xFF << 24) | (r << 16) | (g << 😎 | b);

		    }
		  }
		  try {
		    ImageIO.write(image, format, file);
		  } catch (IOException e) { e.printStackTrace(); }
		}
	
	@Test
	public void testimage() throws IOException {
		createImage();
//		polyhedraCalc.execute(camera1);
//		polyhedraCalc.execute(camera2);
		polyhedraCalc.execute(camera3, camera3b);
	}
	
	@Test
	public void testRGBtoHSV() throws IOException {
		assertArrayEquals(new float[] {311/360,0.93f,0.79f},polyhedraCalc.colorIntToHSV(13242279), 2);
	}
}
