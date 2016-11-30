package mission;
import DroneAutopilot.MoveToTarget;
import p_en_o_cw_2016.Drone;

public abstract class Mission {

	private MoveToTarget moveToTarget;
	private Drone drone;

	
	public Mission(MoveToTarget moveToTarget, Drone drone){
		this.setDrone(drone);
		this.setMoveToTarget(moveToTarget);
	}

	public abstract void execute();


	//////GETTERS & SETTERS//////
	public void setDrone(Drone drone) {
		this.drone = drone;
	}
	
	public Drone getDrone() {
		return this.drone;
	}
	
	public void setMoveToTarget(MoveToTarget moveToTarget) {
		this.moveToTarget = moveToTarget;
	}

	public MoveToTarget getMoveToTarget() {
		return moveToTarget;
	}
}
