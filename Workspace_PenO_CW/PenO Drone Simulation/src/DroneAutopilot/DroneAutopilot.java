package DroneAutopilot;

import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.Drone;

public class DroneAutopilot implements Autopilot{
	
	/**
	 * Create a new Autopilot for the given drone.
	 * @param drone
	 * 		| The drone for which the Autopilot is created.
	 */
	public DroneAutopilot(Drone drone){
		this.setDrone(drone);
		this.moveToTarget = new MoveToTarget(drone);
	}

	/** Called by the testbed in the AWT/Swing GUI thread at a high (but possibly variable)
    frequency, after simulated time has advanced and the simulated world has been
    updated to a new state. Simulated time is frozen for the duration of this call. 
    
    Check if task "fly to red orb" is enabled, if so the drone will start flying towards it.
    Otherwise it will hover until it is given a task.
    */
	@Override
	public void timeHasPassed() {
		System.out.println("pitch" + this.getDrone().getPitch());
		System.out.println("roll" + this.getDrone().getRoll());
		if (this.getMoveToTarget().getGUI().redOrbEnabled) {
			this.getMoveToTarget().execute();
			}else{
				this.getMoveToTarget().hover();
			}
		}

	/**
	 * Set the drone, linked to this Autopilot, to the given drone.
	 * @param drone
	 * 		| the drone linked to the Autopilot.
	 */
	public final void setDrone(Drone drone){
		this.drone = drone;
	}
	/**
	 * Return the drone linked to the Autopilot.
	 */
	public final Drone getDrone(){
		return this.drone;
	}
	/**
	 * Variable registering the drone linked to the Autopilot.
	 */
	private Drone drone;
	
	
	/**
	 * Return an object of the class MoveToTarget, which will execute the movements of the drone.
	 */
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}
	/**
	 * An object of the class MoveToTarget.
	 */
	private final MoveToTarget moveToTarget;
	
}
