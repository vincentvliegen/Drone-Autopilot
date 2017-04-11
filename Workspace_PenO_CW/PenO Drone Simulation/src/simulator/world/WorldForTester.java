package simulator.world;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.DroneAutopilotFactory;
import simulator.objects.SimulationDrone;

public class WorldForTester extends WorldParser_v2 {

	private DroneAutopilot ap;
	private DroneAutopilotFactory testFactory;

	public WorldForTester(DroneAutopilotFactory factory) {
		this.testFactory = factory;
	}

	SimulationDrone drone;

	
	@Override
	protected void setup() {
		float[] colorDrone = { 0f, 0f, 1f };
		super.setup();
		drone = new SimulationDrone(getGL().getGL2(), .06f, .35f, .35f, colorDrone, this);
		ap = testFactory.create(drone);
		drone.setAutopilot(ap);
		getDrones().add(drone);
		getWorldObjectList().add(drone);


		
	}

	public DroneAutopilot getAp() {
		return ap;
	}
	
	@Override
	public void addSimulationDrone(SimulationDrone drone) {
	}

}
