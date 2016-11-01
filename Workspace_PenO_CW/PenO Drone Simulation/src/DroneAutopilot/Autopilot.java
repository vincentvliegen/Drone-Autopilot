package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class Autopilot implements p_en_o_cw_2016.Autopilot{
	
	public Autopilot(){
		this.imageCalculations = new ImageCalculations();
		this.moveToTarget = new MoveToTarget();
	}

	/** Called by the testbed in the AWT/Swing GUI thread at a high (but possibly variable)
    frequency, after simulated time has advanced and the simulated world has been
    updated to a new state. Simulated time is frozen for the duration of this call. */
	@Override
	public void timeHasPassed() {
		if (this.getMoveToTarget().getPhysicsCalculations().getGUI().redOrbEnabled) {
			ArrayList<int[]> leftCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getLeftCamera());
			ArrayList<int[]> rightCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getRightCamera());
			if (this.getImageCalculations().checkIfAllRed(this.getDrone().getLeftCamera()) 
					&& this.getImageCalculations().checkIfAllRed(this.getDrone().getRightCamera())){
				this.getMoveToTarget().hover();
			}else{
				if (this.getMoveToTarget().checkRoll()){
					this.getMoveToTarget().checkcasespixelsfound(leftCameraList, rightCameraList);
				}else{
					this.getMoveToTarget().correctRoll();
				}
				}
			}else{
				this.getMoveToTarget().hover();
			}
		}
		//TODO nog verder aanvullen vooruitvliegen kan ook bij vorige


	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;

	
	public final ImageCalculations getImageCalculations(){
		return this.imageCalculations;
	}
	private final ImageCalculations imageCalculations;
	
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}
	private final MoveToTarget moveToTarget;
	
	
}
