package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.Drone;

public class DroneAutopilot implements Autopilot{
	
	public DroneAutopilot(Drone drone){
		this.setDrone(drone);
		this.moveToTarget = new MoveToTarget(drone);
	}

	/** Called by the testbed in the AWT/Swing GUI thread at a high (but possibly variable)
    frequency, after simulated time has advanced and the simulated world has been
    updated to a new state. Simulated time is frozen for the duration of this call. */
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


	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;
	
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}
	private final MoveToTarget moveToTarget;
	
	
}
