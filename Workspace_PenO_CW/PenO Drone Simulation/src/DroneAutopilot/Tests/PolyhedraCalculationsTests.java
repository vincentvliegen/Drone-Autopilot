package DroneAutopilot.Tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import p_en_o_cw_2016.*;

public class PolyhedraCalculationsTests {
	
	private PolyhedraCalculations polyhedraCalc;
	private Camera camera1;
	private Camera camera2;
	private Camera camera3;
	private Camera camera3b;
	private Camera camera4;
	private Camera camera4b;
	private Camera camera5;
	private Camera camera5b;
	private Camera camera6;
	private Camera camera6b;
	private Camera camera7;
	private Camera camera7b;
	private Camera camera8;
	private Camera camera8b;
	private Camera camera9;
	private Camera camera9b;
	private BufferedImage poly1;
	private BufferedImage poly2;
	private BufferedImage poly3;
	private BufferedImage poly3b;
	private BufferedImage poly4;
	private BufferedImage poly4b;
	private BufferedImage poly5;
	private BufferedImage poly5b;
	private BufferedImage poly6;
	private BufferedImage poly6b;
	private BufferedImage poly7;
	private BufferedImage poly7b;
	private BufferedImage poly8;
	private BufferedImage poly8b;
	private BufferedImage poly9;
	private BufferedImage poly9b;
	private int[] polylist1;
	private int[] polylist2;
	private int[] polylist3;
	private int[] polylist3b;
	private int[] polylist4;
	private int[] polylist4b;
	private int[] polylist5;
	private int[] polylist5b;
	private int[] polylist6;
	private int[] polylist6b;
	private int[] polylist7;
	private int[] polylist7b;
	private int[] polylist8;
	private int[] polylist8b;
	private int[] polylist9;
	private int[] polylist9b;
	
	private static final int widthCamera = 150;
	private static final int heightCamera = 100;
	private static final float horizontalAngleOfView = 90; 
	private float verticalAngleOfView; 
	
	private static final float cameraSeparation = 0.35f;
	private static final float droneGravity = -9.81f;
	private static final float droneWeight = 1.4f;
	private static final float dronePitch = 0;
	private static final float cameraSeparation2 = 0.25f;
	private static final float maxThrust = 50;
	
	public void createDrone(Camera cam1, Camera cam2){
		Drone drone = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, dronePitch, 0, cameraSeparation, cam1, cam2);
		polyhedraCalc = new PolyhedraCalculations(drone);
	}
	
	public void createDrone2(Camera cam1, Camera cam2){
		Drone drone = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, dronePitch, 0, cameraSeparation2, cam1, cam2);
		polyhedraCalc = new PolyhedraCalculations(drone);
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
		
		poly3 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/links.png"));
		polylist3 = convertImageToIntArray(poly3,628,739);
		camera3 = createCameraForTesting(polylist3, 38.78388f, 45.0f, 628);
		
		poly3b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/rechts.png"));
		polylist3b = convertImageToIntArray(poly3b,628,739);
		camera3b = createCameraForTesting(polylist3b, 38.78388f, 45.0f, 628);
		
		poly4 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/testsimulator-left.png"));
		polylist4 = convertImageToIntArray(poly4,200,200);
		camera4 = createCameraForTesting(polylist4, 38.78388f, 45.0f, 200);
		
		poly4b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/testsimulator-right.png"));
		polylist4b = convertImageToIntArray(poly4b,200,200);
		camera4b = createCameraForTesting(polylist4b, 38.78388f, 45.0f, 200);
		
		poly5 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/driehoekLinks.png"));
		polylist5 = convertImageToIntArray(poly5,628,739);
		camera5 = createCameraForTesting(polylist5, 38.78388f, 45.0f, 628);
		
		poly5b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/driehoekRechts.png"));
		polylist5b = convertImageToIntArray(poly5b,628,739);
		camera5b = createCameraForTesting(polylist5b, 38.78388f, 45.0f, 628);
		
		poly7 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test2Links.png"));
		polylist7 = convertImageToIntArray(poly7,200,200);
		camera7 = createCameraForTesting(polylist7, 38.78388f, 45.0f, 200);
		
		poly7b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test2Rechts.png"));
		polylist7b = convertImageToIntArray(poly7b,200,200);
		camera7b = createCameraForTesting(polylist7b, 38.78388f, 45.0f, 200);
		
		poly8 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test5Links.png"));
		polylist8 = convertImageToIntArray(poly8,200,200);
		camera8 = createCameraForTesting(polylist8, 120f, 120f, 200);
		
		poly8b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test5Rechts.png"));
		polylist8b = convertImageToIntArray(poly8b,200,200);
		camera8b = createCameraForTesting(polylist8b, 120f, 120f, 200);
		
		poly9 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test4Links.png"));
		polylist9 = convertImageToIntArray(poly9,200,200);
		camera9 = createCameraForTesting(polylist9, 120f, 120f, 200);
		
		poly9b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/test4Rechts.png"));
		polylist9b = convertImageToIntArray(poly9b,200,200);
		camera9b = createCameraForTesting(polylist9b, 120f, 120f, 200);

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
	
	public Camera createCameraForTesting(int[] image, float horAngle, float verAngle, int width){
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
	public void testAllCOGs() throws IOException {
		createImage();
		createDrone(camera4, camera4b);
//		polyhedraCalc.execute(camera1);
//		polyhedraCalc.execute(camera2);
		assertEquals(polyhedraCalc.findAllCOGs(camera4, camera4b).size(),6);
	}
	
	@Test
	public void testAllCorners() throws IOException{
		createImage();
//		createDrone(camera3, camera3b);
//		polyhedraCalc.getMatchingCorners(camera3, camera3b);
//		createDrone(camera5, camera5b);
//		polyhedraCalc.getMatchingCorners(camera5, camera5b);
//		createDrone(camera7, camera7b);
//		polyhedraCalc.getMatchingCorners(camera7, camera7b);
		createDrone2(camera8, camera8b);
		polyhedraCalc.getMatchingCorners(camera8, camera8b);
//		createDrone2(camera9,camera9b);
//		polyhedraCalc.getMatchingCorners(camera9, camera9b);
	}
	
	@Test
	public void testRGBtoHSV() throws IOException {
		createImage();
		this.createDrone(camera1,camera1);
		assertArrayEquals(new float[] {311/360,0.93f,0.79f},polyhedraCalc.colorIntToHSV(13242279), 2);
	}
	
	public Drone createDroneForTesting(float weight, float gravity, float x, float y, float z, float yaw, float pitch, float roll, float cameraSeparation, Camera leftCamera, Camera rightCamera){
		return new Drone(){
			
			@Override
			public float getCameraSeparation() {
				return cameraSeparation;
			}

			@Override
			public Camera getLeftCamera() {
				return leftCamera;
			}

			@Override
			public Camera getRightCamera() {
				return rightCamera;
			}

			@Override
			public float getWeight() {
				return weight;
			}

			@Override
			public float getGravity() {
				return gravity;
			}

			@Override
			public float getPitch() {
				return pitch;
			}
			
			//ongebruikt
			@Override
			public void setPitchRate(float value) {}
			@Override
			public void setRollRate(float value) {}
			@Override
			public void setYawRate(float value) {}
			@Override
			public void setThrust(float value) {}
			@Override
			public float getDrag() {
				return 0;
			}
			@Override
			public float getCurrentTime() {
				return 0;
			}
			@Override
			public float getMaxThrust() {
				return maxThrust;
			}
			@Override
			public float getMaxPitchRate() {
				return 50;
			}
			@Override
			public float getMaxRollRate() {
				return 50;
			}
			@Override
			public float getMaxYawRate() {
				return 50;
			}
			@Override
			public float getRoll() {
				return roll;
			}

			@Override
			public float getX() {
				return x;
			}

			@Override
			public float getY() {
				return y;
			}

			@Override
			public float getZ() {
				return z;
			}

			@Override
			public float getHeading() {
				return yaw;
			}

	    };
	}
}
