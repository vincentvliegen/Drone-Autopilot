package DroneAutopilot;

import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.Drone;

public class DroneAutopilot implements Autopilot{
	
	/**
	 * Variable registering the drone linked to the Autopilot.
	 */
	private Drone drone;
	
	/**
	 * An object of the class MoveToTarget.
	 */
	private final MoveToTarget moveToTarget;
	private final ShortestPath shortestPath;
	
	public DroneAutopilot(Drone drone){
		this.setDrone(drone);
		this.moveToTarget = new MoveToTarget(drone);
		this.shortestPath = new ShortestPath(this.getMoveToTarget());
	}
	
	
	/** Called by the testbed in the AWT/Swing GUI thread at a high (but possibly variable)
    frequency, after simulated time has advanced and the simulated world has been
    updated to a new state. Simulated time is frozen for the duration of this call. 
    
    Check if task "fly to red orb" is enabled, if so the drone will start flying towards it.
    Otherwise it will hover until it is given a task.
    */
	@Override
	public void timeHasPassed() {
		if (this.getMoveToTarget().getGUI().lastOrbEnabled) {
			this.getMoveToTarget().execute(16711680);
		} else if (this.getMoveToTarget().getGUI().flyShortest) {
			this.getShortestPath().execute();
		} else if (this.getMoveToTarget().getGUI().test) {
			System.out.println("thp");
			this.getShortestPath().getBiggestOrbs();
		} else {
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity() * this.getDrone().getWeight()));

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
	 * Return an object of the class MoveToTarget, which will execute the movements of the drone.
	 */
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}


	public ShortestPath getShortestPath() {
		return shortestPath;
	}
	
	

}
