package DroneAutopilot.Tests;

import DroneAutopilot.PhysicsCalculations;
import DroneAutopilot.GUI.GUI;
import simulator.objects.SimulationDrone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PhysicsCalculationsTest {

	@Before
	public void setUp() {
		calc = new PhysicsCalculations();
		gui = new GUI();
		drone = new SimulationDrone(null, 0, 0, 0, 0, null, null, null); //TODO Waardes cameras: width = 150; heigth = 120
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
	
	//Assign cameras en drone met deze waardes
	private static final int widthCamera = 150;
	private static final int cameraSeparation = 20;//TODO units?cm? betere waarde eventueel
	private static final int horizontalAngleOfView = 45; //TODO degrees
	private static final int verticalAngleOfView = 45;// TODO degrees? 
	private static final int droneGravity = 10;//TODO betere waarde?
	
	
	private PhysicsCalculations calc;
	private SimulationDrone drone;
	private GUI gui;
	
	private int[] cOG1;
	private int[] cOG2;
	private int[] cOG3;
	
	private int[] depthXY1;
	private int[] depthXY2;
	


}
