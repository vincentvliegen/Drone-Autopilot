package DroneAutopilot;

import java.util.ArrayList;

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
		}}}
		//TODO nog verder aanvullen vooruitvliegen kan ook bij vorige


	public void setDrone(p_en_o_cw_2016.Drone drone){
		this.drone = drone;
	}
	public p_en_o_cw_2016.Drone getDrone(){
		return this.drone;
	}
	private p_en_o_cw_2016.Drone drone;

	
	public final ImageCalculations getImageCalculations(){
		return this.imageCalculations;
	}
	private final ImageCalculations imageCalculations;
	
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}
	private final MoveToTarget moveToTarget;
	
	
}
