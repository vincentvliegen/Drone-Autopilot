package implementedClasses;

import DroneAutopilot.ImageCalculations;
import DroneAutopilot.MoveToTarget;

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
		// TODO Auto-generated method stub
		int[][] leftCameraList = this.getImageCalculations().getRedPixels(this.getLeftCamera());
		int[][] rightCameraList = this.getImageCalculations().getRedPixels(this.getRightCamera());
		if (this.getImageCalculations().checkIfAllRed(this.getLeftCamera()) 
				&& this.getImageCalculations().checkIfAllRed(this.getRightCamera())){
			//hover
		}
		this.getMoveToTarget().checkcasespixelsfound(this.getDrone(),leftCameraList, rightCameraList); //nadenken over int[][] en Arraylist
		//nog verder aanvullen vooruitvliegen kan ook bij vorige
	}

	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;
	
	
	public void setLeftCamera(Camera leftCamera){
		this.leftCamera = leftCamera;
	}
	public Camera getLeftCamera(){
		return this.leftCamera;
	}
	private Camera leftCamera;
	
	
	public void setRightCamera(Camera rightCamera){
		this.rightCamera = rightCamera;
	}
	public Camera getRightCamera(){
		return this.rightCamera;
	}
	private Camera rightCamera;

	
	public final ImageCalculations getImageCalculations(){
		return this.imageCalculations;
	}
	private final ImageCalculations imageCalculations;
	
	public final MoveToTarget getMoveToTarget(){
		return this.moveToTarget;
	}
	private final MoveToTarget moveToTarget;
	
	
}
