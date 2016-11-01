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
		calc = new PhysicsCalculations(drone);
		gui = new GUI();
		drone = createDroneForTesting(0, 0, 0, 0, null, null); //TODO Waardes cameras: width = 150; heigth = 120
		calc.setDrone(drone);
		calc.setGUI(gui);
		
		cOG1 = new int[] {1,1};
		cOG1 = new int[] {5,80};
		cOG1 = new int[] {127,33};
		
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
		assertEquals(calc.getY(cOG1),-74);
		assertEquals(calc.getY(cOG2),5);
		assertEquals(calc.getY(cOG3),-42);
    }
	
	@Test
	public void cameraHeightTest(){
		assertEquals(calc.getCameraHeight(),150);
	}
	
	@Test
	public void focalDistanceTest(){
		assertEquals(calc.getfocalDistance(), 0.0/*TODO waarde berekenen*/,0.0003/*TODO betere delta?*/);
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
	
	public Camera createCameraForTesting(int horAngle, int verAngle, int width){
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

	public Drone createDroneForTesting(float weight, float gravity,float cameraSeparation, float pitch, Camera leftCamera, Camera rightCamera){
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
	private static final int cameraSeparation = 20;//TODO units?cm? betere waarde eventueel
	private static final float horizontalAngleOfView = (float) (Math.PI/4); 
	private static final float verticalAngleOfView = (float) (Math.PI/4); 
	private static final int droneGravity = 1;//TODO betere waarde?
	
	
	private PhysicsCalculations calc;
	private Drone drone;
	private GUI gui;
	
	private int[] cOG1;
	private int[] cOG2;
	private int[] cOG3;
	
	private int[] depthXY1;
	private int[] depthXY2;
	


}
