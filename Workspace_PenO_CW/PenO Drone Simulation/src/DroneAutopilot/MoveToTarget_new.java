package DroneAutopilot;

import p_en_o_cw_2016.Drone;

public class MoveToTarget_new {
	
	//TODO: setbeginMovementTime bij begin van vliegfunctie.

	private final PhysicsCalculations physicsCalculations;
	private Drone drone;

	public MoveToTarget_new(Drone drone) {
		this.setDrone(drone);
		this.physicsCalculations = new PhysicsCalculations(drone);
	}

	public final PhysicsCalculations getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public Drone getDrone() {
		return this.drone;
	}

}
