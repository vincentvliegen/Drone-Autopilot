package mission;
import DroneAutopilot.MoveToTarget;
import p_en_o_cw_2016.Drone;

public abstract class Mission {

	private MoveToTarget moveToTarget;
	private Drone drone;

	
	public Mission(Drone drone){
		this.setDrone(drone);
		moveToTarget = new MoveToTarget(drone);
	}

	public abstract void execute();


	//////GETTERS & SETTERS//////
	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public MoveToTarget getMoveToTarget() {
		return moveToTarget;
	}

	public Drone getDrone() {
		return this.drone;
	}
}
