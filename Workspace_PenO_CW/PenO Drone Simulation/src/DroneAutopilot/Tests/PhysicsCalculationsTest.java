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
		
		cOG1 = new float[] {1,1};
		cOG2 = new float[] {5,80};
		cOG3 = new float[] {127,33};
		
		depthXY1 = new float[] {120,80};
		depthXY2 = new float[] {45,80};
	}
	
	@Test
    public void X1Test() {
		assertEquals(-74,calc.getX1(cOG1),0.0001);
		assertEquals(-70,calc.getX1(cOG2),0.0001);
		assertEquals(52,calc.getX1(cOG3),0.0001);
    }
	
	@Test
    public void X2Test() {
		assertEquals(-74,calc.getX2(cOG1),0.0001);
		assertEquals(-70,calc.getX2(cOG2),0.0001);
		assertEquals(52,calc.getX2(cOG3),0.0001);
    }
	
	@Test
    public void YTest() {
		float y = calc.getY(cOG1);
		assertEquals(49,y,0.0001);
		assertEquals(-30,calc.getY(cOG2),0.0001);
		assertEquals(17,calc.getY(cOG3),0.0001);
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
		assertEquals(Math.toDegrees(Math.atan(-0.4)),calc.verticalAngleDeviation(depthXY1),0.00001);
	}
	
	@Test
	public void thrustTest(){
		assertEquals(8.623982,calc.getThrust(depthXY1),0.00001);
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
	
	private float[] cOG1;
	private float[] cOG2;
	private float[] cOG3;
	
	private float[] depthXY1;
	private float[] depthXY2;
	


}
