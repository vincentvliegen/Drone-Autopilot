package DroneAutopilot.Tests;

import DroneAutopilot.PhysicsCalculations;
import DroneAutopilot.GUI.GUI;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PhysicsCalculationsTest {

	@Before
	public void setUp() {
		verticalAngleOfView = (float) (2*Math.toDegrees(Math.atan((heightCamera/widthCamera)*Math.tan(Math.toRadians(horizontalAngleOfView/2)))));
		calc = new PhysicsCalculations(drone);
		gui = new GUI();
		camera = createCameraForTesting(horizontalAngleOfView, verticalAngleOfView, widthCamera);
		drone = createDroneForTesting(droneWeight, droneGravity, cameraSeparation, dronePitch, camera, camera);
		calc.setDrone(drone);
		calc.setGUI(gui);
		
		cOG1 = new int[] {1,1};
		cOG2 = new int[] {5,80};
		cOG3 = new int[] {127,33};
		
		depthXY1 = new int[] {120,120};
		depthXY2 = new int[] {45,120};
	}
	
	@Test
    public void X1Test() {
		assertEquals(calc.getX1(cOG1),-74);
		assertEquals(calc.getX1(cOG2),-70);
		assertEquals(calc.getX1(cOG3),52);
    }
	
	@Test
    public void X2Test() {
		assertEquals(calc.getX2(cOG1),-74);
		assertEquals(calc.getX2(cOG2),-70);
		assertEquals(calc.getX2(cOG3),52);
    }
	
	@Test
    public void YTest() {
		assertEquals(calc.getY(cOG1),-49);
		assertEquals(calc.getY(cOG2),30);
		assertEquals(calc.getY(cOG3),-17);
    }
	
	//TODO fix
	@Test
	public void cameraHeightTest(){
		assertEquals(calc.getCameraHeight(),100);
	}
	
	@Test
	public void focalDistanceTest(){
		assertEquals(calc.getfocalDistance(), 75,0.0003);
	}
	
	@Test
	public void depthTest(){
		assertEquals(calc.getDepth(depthXY1, depthXY2),0.0/*TODO bereken depth*/, 0.0003 /*TODO betere delta?*/);
	}
	
	@Test
	public void horAngleDevTest(){
		assertEquals(calc.horizontalAngleDeviation(depthXY1, depthXY2), 0.0,0.0003);/*TODO idem*/
	}
	
	@Test
	public void verAngleDevTest(){
		assertEquals(calc.verticalAngleDeviation(depthXY1),0.0,0.0003);/*TODO idem*/
	}
	
	@Test
	public void visPitchTest(){
		assertEquals(calc.getVisiblePitch(),0.0,0.0003);/*TODO idem*/
	}
	
	@Test
	public void ThrustTest(){
		assertEquals(calc.getThrust(depthXY1),0.0,0.0003);/*TODO idem*/
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
			public void setPitchRate(float value) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void setRollRate(float value) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void setYawRate(float value) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void setThrust(float value) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public float getDrag() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getCurrentTime() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getMaxThrust() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getMaxPitchRate() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getMaxRollRate() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getMaxYawRate() {
				// TODO Auto-generated method stub
				return 0;
			}
			@Override
			public float getRoll() {
				// TODO Auto-generated method stub
				return 0;
			}

	    };
	}

	//Assign cameras en drone met deze waardes
	private static final int widthCamera = 150;
	private static final int heightCamera = 100;
	private static final float horizontalAngleOfView = (float) 90; 
	private float verticalAngleOfView; 

	
	private static final float cameraSeparation = 20;
	private static final float droneGravity = 10;
	private static final float droneWeight = 1;
	private static final float dronePitch =(float) 30;
	
	private Camera camera;
	private PhysicsCalculations calc;
	private Drone drone;
	private GUI gui;
	
	private int[] cOG1;
	private int[] cOG2;
	private int[] cOG3;
	
	private int[] depthXY1;
	private int[] depthXY2;
	


}
