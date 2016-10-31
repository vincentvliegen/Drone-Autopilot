package DroneAutopilot.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import DroneAutopilot.*;
import simulator.objects.SimulationDrone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MoveToTargetTest {

	@Before
	public void setUp() {
		this.mTT = new MoveToTarget();
		this.drone = new SimulationDrone(null, 0, 0, 0, 0, null, null, null);//TODO waardes
		this.mTT.setDrone(drone);
	}
	
	@Test
    public void cOGTest() {
		assertArrayEquals(calc.getCOG(pixelsPos1),new int[] {1,1});
    }
	
	private MoveToTarget mTT;
	private SimulationDrone drone;
	
}
