package DroneAutopilot.Tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.junit.Test;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class TestImageProcessing {

	private TestAP testap;
	private PolyhedraCalculations polyhedraCalc;
	private Camera camera1;
	private Camera camera1b;
	private BufferedImage poly1;
	private BufferedImage poly1b;
	private int[] polylist1;
	private int[] polylist1b;

	private static final float horizontalAngleOfView = 120; 

	private static final float cameraSeparation = 0.25f;
	private static final float droneGravity = -9.81f;
	private static final float droneWeight = 1.0f;
	private static final float dronePitch = 0;
	private static final float maxThrust = 50;
	
	public void createImage() throws IOException{
		poly1 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/img231-left.png"));
		polylist1 = convertImageToIntArray(poly1,400,400);
		camera1 = createCameraForTesting(polylist1, 1, 1, 400);

		poly1b = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/img231-right.png"));
		polylist1b = convertImageToIntArray(poly1b,400,400);
		camera1b = createCameraForTesting(polylist1b, 1, 1, 400);
	}

	@Test
	public void test3DCoordinates() throws IOException{
		createImage();
		createDrone(camera1, camera1b);
		HashMap<ArrayList<float[]>, ArrayList<double[]>> result = polyhedraCalc.getMatchingCorners(camera1, camera1b);
		assertEquals(1,result.keySet().size());
		for(ArrayList<float[]> color: result.keySet()){
			System.out.println("Color: " + color);
			System.out.println(Arrays.toString(result.get(color).get(0)));
			System.out.println(Arrays.toString(result.get(color).get(1)));
			System.out.println(Arrays.toString(result.get(color).get(2)));
		}
	}

	@Test
	public void testNewMEthod() throws IOException{
		createImage();
		createDrone(camera1,camera1b);
		HashMap<float[], ArrayList<double[]>> NEW = polyhedraCalc.newCOGmethod(camera1, camera1b);
	}
	
	/*
	public String testPixelCoordinates() throws IOException{
		createImage();
		createDrone(camera1, camera1);
		polyhedraCalc.SeparateTargetsAndObstacles(camera1);
		HashMap<float[], ArrayList<int[]>> outerTrianglesL = polyhedraCalc
				.findThreePointsTriangles(polyhedraCalc.getHashMapTargetOuterColor());
		HashMap<float[], ArrayList<int[]>> innerTrianglesL = polyhedraCalc
				.findThreePointsTriangles(polyhedraCalc.getHashMapTargetInnerColor());
		String coordinates = new String();
		for(float[] color: outerTrianglesL.keySet()){
			ArrayList<int[]> coordpix = outerTrianglesL.get(color);
			coordinates += (coordpix.get(0)[0] + " " + coordpix.get(0)[1] + " ");
			coordinates += (coordpix.get(1)[0] + " " + coordpix.get(1)[1] + " ");
			coordinates += (coordpix.get(2)[0] + " " + coordpix.get(2)[1] + " ");
		}
		for(float[] color: innerTrianglesL.keySet()){
			ArrayList<int[]> coordpix = innerTrianglesL.get(color);
			coordinates += (coordpix.get(0)[0] + " " + coordpix.get(0)[1] + " ");
			coordinates += (coordpix.get(1)[0] + " " + coordpix.get(1)[1] + " ");
			coordinates += (coordpix.get(2)[0] + " " + coordpix.get(2)[1] + " ");
		}

		return coordinates;

	}
	 */
	
	
	
	
	
	
	//----------------------------------------------------------//

	public void createDrone(Camera cam1, Camera cam2){
		Drone drone = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, dronePitch, 0, cameraSeparation, cam1, cam2);
		testap = new TestAP(drone);
		polyhedraCalc = new PolyhedraCalculations(testap);
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


	private class TestAP extends DroneAutopilot {

		public TestAP(Drone drone) {
			super(drone);
		}

		@Override
		public void timeHasPassed() {
		}

	}


}
