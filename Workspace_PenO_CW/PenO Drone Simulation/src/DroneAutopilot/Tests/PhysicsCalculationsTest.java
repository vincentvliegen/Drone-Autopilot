package DroneAutopilot.Tests;

import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PhysicsCalculations;

public class PhysicsCalculationsTest {

	//Assign cameras en drone met deze waardes
	private TestAP testap;
	private TestAP testap1;
	private static final int widthCamera = 150;
	private static final int heightCamera = 100;
	private static final float horizontalAngleOfView = 90; 
	private float verticalAngleOfView; 
	
	private static final float cameraSeparation = 20;
	private static final float droneGravity = -10;
	private static final float droneWeight = 1;
	private static final float dronePitch = 15;
	
	private static final float maxThrust = 50;
	
	private Camera camera;
	private PhysicsCalculations calc, calc1;
	private Drone drone, drone1;
	
	private double[] cOG1;
	private double[] cOG2;
	private double[] cOG3;
	
	private float[] depthXY1;
	private float[] depthXY2;
	
	@Before
	public void setUp() {
		verticalAngleOfView = (float) (2*Math.toDegrees(Math.atan((((double) heightCamera/ (double) widthCamera)*Math.tan(Math.toRadians(horizontalAngleOfView/2))))));
		camera = createCameraForTesting(horizontalAngleOfView, verticalAngleOfView, widthCamera);
		drone = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, dronePitch, 0, cameraSeparation, camera, camera);
		drone1 = createDroneForTesting(droneWeight, droneGravity, 0, 0, 0, 0, 0, 0, cameraSeparation, camera, camera);
		testap = new TestAP(drone);
		calc = testap.getPhysicsCalculations();
		testap1 = new TestAP(drone1);
		calc1 = testap1.getPhysicsCalculations();
		
		//calc1.setWindTranslation(0,(float) 0.5, 0);

		cOG1 = new double[] {1,1};
		cOG2 = new double[] {5,80};
		cOG3 = new double[] {127,33};
		
		depthXY1 = new float[] {120,80};
		depthXY2 = new float[] {45,80};
	}
	
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

	
	//TESTS
	
	@Test
	public void calculatePositionObjectTest1(){
		calc.updateDroneData();
		calc.calculatePositionObject(cOG1, cOG2);
		assertEquals(-74, calc.getX1(), 0.0001);
		assertEquals(-70, calc.getX2(), 0.0001);
		assertEquals(49, calc.getY(), 0.0001);
		//TODO: Werken met eventueel gegenereerde afbeeldingen vn generator met juiste afmetingen.
		//assertEquals(20, calc.getDepth(), 0.0001);
		//assertEquals(Math.toDegrees(Math.atan(0.1)), calc.getHorizontalAngleDeviation(), 0.0001);
		//assertEquals(Math.toDegrees(Math.atan(-0.4)), calc.getHorizontalAngleDeviation(), 0.0001);
	}
	
	@Test
	public void calculatePositionObjectTest2(){
		calc.updateDroneData();
		calc.calculatePositionObject(cOG2, cOG3);
		assertEquals(-70, calc.getX1(), 0.0001);
		assertEquals(52, calc.getX2(), 0.0001);
		assertEquals(-30, calc.getY(), 0.0001);
	}
	
	@Test
	public void cameraHeightTest(){
		assertEquals(100, calc.getCameraHeight(), 0.0001);
	}
	
	@Test
	public void focalDistanceTest(){
		assertEquals(75, calc.getFocalDistance(), 0.0001);
	}
	
	/*
	 *TODO: calculateThrust
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
	
	@Test 
	public void getThrustToPositionTest(){
//		calc1.updatePosition(new float[] {0,0,-4},5);
//		assertEquals(10, calc1.getThrust(), 0.0001);
		calc1.updatePosition(new float[] {0,0,0},4);
		assertEquals(10, calc1.getThrust(), 0.0001);
	}
	
//	@Test
//	public void acctest(){
//		float[] pos = new float[] {0,5,9};
//		float[] acc = calc1.getMaxAccelerationValues(pos);
//		System.out.println("acc= { "+acc[0] + ", " + acc[1] + "}");
//		calc1.calculateWantedOrientation(pos,acc[1]);
//		float[][] wo = calc1.getWantedOrientation();
//		System.out.println("wo: thrust = { "+wo[0][0] + ", " + wo[0][1] + ", " + wo[0][2] + "}, view =  " + "{ "+wo[1][0] + ", " + wo[1][1] + ", " + wo[1][2] + "} ");
//		System.out.println("sizeThrust = " + VectorCalculations.size(wo[0]));
//	}
*/	
	
	private class TestAP extends DroneAutopilot {

		public TestAP(Drone drone) {
			super(drone);
		}

		@Override
		public void timeHasPassed() {
		}

	}
}
