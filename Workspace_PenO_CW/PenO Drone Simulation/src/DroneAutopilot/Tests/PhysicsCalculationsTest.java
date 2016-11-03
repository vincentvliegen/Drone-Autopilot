package DroneAutopilot.Tests;

import DroneAutopilot.PhysicsCalculations;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PhysicsCalculationsTest {

	@Before
	public void setUp() {
		verticalAngleOfView = (float) (2*Math.toDegrees(Math.atan((((double) heightCamera/ (double) widthCamera)*Math.tan(Math.toRadians(horizontalAngleOfView/2))))));
		calc = new PhysicsCalculations(drone);
		camera = createCameraForTesting(horizontalAngleOfView, verticalAngleOfView, widthCamera);
		drone = createDroneForTesting(droneWeight, droneGravity, dronePitch, cameraSeparation, camera, camera);
		calc.setDrone(drone);
		
		cOG1 = new int[] {1,1};
		cOG2 = new int[] {5,80};
		cOG3 = new int[] {127,33};
		
		depthXY1 = new int[] {120,80};
		depthXY2 = new int[] {45,80};
	}
	
	@Test
    public void X1Test() {
		assertEquals(-74,calc.getX1(cOG1));
		assertEquals(-70,calc.getX1(cOG2));
		assertEquals(52,calc.getX1(cOG3));
    }
	
	@Test
    public void X2Test() {
		assertEquals(-74,calc.getX2(cOG1));
		assertEquals(-70,calc.getX2(cOG2));
		assertEquals(52,calc.getX2(cOG3));
    }
	
	@Test
    public void YTest() {
		assertEquals(-49,calc.getY(cOG1));
		assertEquals(30,calc.getY(cOG2));
		assertEquals(-17,calc.getY(cOG3));
    }
	
	@Test
	public void cameraHeightTest(){
		assertEquals(100,calc.getCameraHeight());
	}
	
	@Test
	public void focalDistanceTest(){
		assertEquals(75,calc.getfocalDistance(),0.00001);
	}
	
	@Test
	public void depthTest(){
		assertEquals(20,calc.getDepth(depthXY1, depthXY2), 0.00001);
	}
	
	@Test
	public void horAngleDevTest(){
		assertEquals(Math.toDegrees(Math.atan(0.1)),calc.horizontalAngleDeviation(depthXY1, depthXY2),0.00001);
	}
	
	@Test
	public void verAngleDevTest(){
		assertEquals(Math.toDegrees(Math.atan(0.4)),calc.verticalAngleDeviation(depthXY1),0.00001);
	}
	
	@Test
	public void visPitchTest(){
		assertEquals(verticalAngleOfView/2*PhysicsCalculations.getVisibilityFactor(),calc.getVisiblePitch(),0.00001);
	}
	
	@Test
	public void thrustTest(){
		assertEquals(10.694534,calc.getThrust(depthXY1),0.00001);
	}
	
	@Test
	public void distanceTest(){
		double cosa = Math.cos(Math.atan(0.1));
		double cosb = Math.cos(Math.atan(0.4));
		assertEquals(20.0/cosa/cosb,calc.getDistance(depthXY1, depthXY2),0.00001);
	}
	
	//TODO tests aanvullen met extreme voorbeelden
	
	
	public Camera createCameraForTesting(float horAngle, float verAngle, int width){
		return new Camera(){
	    				
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

	    	
	    	//ongebruikt
			@Override
	    	public int[] takeImage() {
	    		return null;
	    	}		
		
		};
	}

	public Drone createDroneForTesting(float weight, float gravity, float pitch, float cameraSeparation, Camera leftCamera, Camera rightCamera){
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
				return 0;
			}
			@Override
			public float getMaxPitchRate() {
				return 0;
			}
			@Override
			public float getMaxRollRate() {
				return 0;
			}
			@Override
			public float getMaxYawRate() {
				return 0;
			}
			@Override
			public float getRoll() {
				return 0;
			}

	    };
	}

	//Assign cameras en drone met deze waardes
	private static final int widthCamera = 150;
	private static final int heightCamera = 100;
	private static final float horizontalAngleOfView = 90; 
	private float verticalAngleOfView; 

	
	private static final float cameraSeparation = 20;
	private static final float droneGravity = -10;
	private static final float droneWeight = 1;
	private static final float dronePitch = 15;
	
	private Camera camera;
	private PhysicsCalculations calc;
	private Drone drone;
	
	private int[] cOG1;
	private int[] cOG2;
	private int[] cOG3;
	
	private int[] depthXY1;
	private int[] depthXY2;
	


}
