package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class WorldScan {
	private Drone drone;

	
	public Drone getDrone() {
		return drone;
	}
	public void setDrone(Drone drone) {
		this.drone = drone;
	}
	public WorldScan(Drone drone){
		this.setDrone(drone);
		// Draai 360°, bekijk bollen, vlieg nr dichtsbijzijnde
		//PRBLM 1: wind => ???
		//PRBLM 2: geen bol in zicht (enkel boven of onder..) => wat vooruit of achteruitvliegen tot die in het zicht is? 
	}

	public void scan(Drone drone, ImageCalculations imageCalculations){
		//TODO
	}

	public boolean scan(ArrayList<int[]> leftCameraList, ArrayList<int[]> rightCameraList, int color){
		boolean canStartFly = false;
		ArrayList<Boolean> targetFound = this.checkCasesPixelsFound(leftCameraList, rightCameraList);
	
		if(targetFound.get(0)==false && targetFound.get(1)==true){
			this.rightCameraFoundTarget();
		}else if(targetFound.get(0)==true && targetFound.get(1)==false){
			this.leftCameraFoundTarget();
		}else if(targetFound.get(0)==true && targetFound.get(1)==true){
			canStartFly = true;
		}else if(targetFound.get(0)==false && targetFound.get(1)==false){
			this.noTargetFound();
		}
		return canStartFly;
	}
	
	public ArrayList<Boolean> checkCasesPixelsFound(ArrayList<int[]> leftcamera,
			ArrayList<int[]> rightcamera) {
		ArrayList<Boolean> targetFound = new ArrayList<Boolean>();
		targetFound.add(false);
		targetFound.add(false);
		if (!leftcamera.isEmpty() && rightcamera.isEmpty()){
			targetFound.set(0, true);
			targetFound.set(1, false);
			return targetFound;
		}else if (leftcamera.isEmpty() && !rightcamera.isEmpty()){
			targetFound.set(0, false);
			targetFound.set(1, true);
			return targetFound;
		}else if (!leftcamera.isEmpty() && !rightcamera.isEmpty()) {
			targetFound.set(0, true);
			targetFound.set(1, true);
			return targetFound;
		}else{
			targetFound.set(0, false);
			targetFound.set(1, false);
			return targetFound;
		}
	}
	
	public void noTargetFound() {
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate()/2);
	}

	public void leftCameraFoundTarget() {
		this.getDrone().setYawRate(-this.getDrone().getMaxYawRate()/2);
	}

	public void rightCameraFoundTarget() {
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate()/2);
	}
}
