package DroneAutopilot.mission;

import DroneAutopilot.calculations.PhysicsCalculations;
import p_en_o_cw_2016.Drone;
import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.GUI.GUI;

public abstract class Mission {

	private final DroneAutopilot droneAutopilot;
	private final PhysicsCalculations physicsCalculations;
	private final GUI GUI;
	private final Drone drone;
	
	public Mission(DroneAutopilot droneAutopilot){
		this.droneAutopilot = droneAutopilot;
		this.GUI = this.getDroneAutopilot().getGUI();
		this.physicsCalculations = this.getDroneAutopilot().getPhysicsCalculations();
		this.drone = this.getDroneAutopilot().getDrone();
	}

	public abstract void execute();
	
	public abstract void updateGUI();

	
	//////////GETTERS & SETTERS//////////
	
	public DroneAutopilot getDroneAutopilot() {
		return this.droneAutopilot;
	}

	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}

	public GUI getGUI() {
		return GUI;
	}

	public Drone getDrone() {
		return drone;
	}
	
}
