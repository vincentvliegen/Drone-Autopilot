 package DroneAutopilot.Tests;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import DroneAutopilot.calculations.PolyhedraCalculations;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class TestImageProcessing {
	
	private PolyhedraCalculations polyhedraCalc;
	private Camera camera1;
	private BufferedImage poly1;
	private int[] polylist1;
	
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
	
	public void createImage() throws IOException{
		poly1 = ImageIO.read(this.getClass().getResource("/DroneAutopilot/Tests/imagesForTests/img0-left.png"));
		polylist1 = convertImageToIntArray(poly1,331,222);
		camera1 = createCameraForTesting(polylist1, 1, 1, 331);
	}
	
	public void createDrone(Camera cam1, Camera cam2){
		Drone drone = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, dronePitch, 0, cameraSeparation, cam1, cam2);
		polyhedraCalc = new PolyhedraCalculations(drone);
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
	
}
