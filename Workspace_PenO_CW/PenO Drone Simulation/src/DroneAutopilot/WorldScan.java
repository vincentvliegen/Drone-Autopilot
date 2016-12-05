package DroneAutopilot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import DroneAutopilot.controllers.RollController;
import p_en_o_cw_2016.Drone;

public class WorldScan {
	private Drone drone;
	private boolean finishedScan;
	private RollController rollPI;
	private boolean rollStarted;
	private boolean firstTime;
	private float previousTime;
	private float degreesTurned;
	private float timePassed;

	private static final float underBoundary = -0.2f;
	private static final float upperBoundary = 0.2f;


	public WorldScan(Drone drone){
		this.setDrone(drone);
		this.rollPI = new RollController(1,0);
	}

	public Set<Integer> scan(Drone drone, ImageCalculations imageCalculations){
		this.setFinished(false);
		this.correctRoll();
		imageCalculations.calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
		HashMap<Integer, ArrayList<int[]>> hashMapOfColors = imageCalculations.getPixelsOfEachColor();
		System.out.println("length " + hashMapOfColors.size());
			if(hashMapOfColors.keySet().size()>=1){
				//System.out.println("iets gevonden");
				this.getDrone().setYawRate(0);
				this.setFinished(true);
				firstTime = false;
				degreesTurned = 0;
				timePassed = 0;
				return hashMapOfColors.keySet();
			}
			//System.out.println("hier kan ie niet in");
			
		else{
			if(!firstTime){
				//System.out.println("firsttime");
				firstTime = true;
				this.setPreviousTime(this.getDrone().getCurrentTime());
			}else{
				//System.out.println("yaw");
				this.getDrone().setYawRate(this.getDrone().getMaxYawRate()/2);
				degreesTurned += this.getDrone().getMaxYawRate()/2 * (this.getDrone().getCurrentTime() - this.getPreviousTime());
				this.setPreviousTime(this.getDrone().getCurrentTime());
//				System.out.println("degrees " + degreesTurned);
				if(degreesTurned >= 540){
					this.getDrone().setYawRate(0);
					this.getDrone().setPitchRate(-1);
					//berekening thrust om horizontaal achterwaarts te vliegen afhankelijk van pitch.
					float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
					this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
					timePassed += this.getDrone().getCurrentTime() - this.getPreviousTime();
					if(timePassed >= 1){
						this.getDrone().setPitchRate(1);
						if(this.getDrone().getPitch()>= -0.01){
							this.getDrone().setPitchRate(0);
						}
					}
				}
			}
			return null;
		}
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

	public void correctRoll() {
		if (this.getDrone().getRoll() <= upperBoundary && this.getDrone().getRoll() >= underBoundary) {
			this.getDrone().setRollRate(0);
			this.setRollStarted(false);
		}
		else if (this.getDrone().getRoll() > upperBoundary){
			if(!this.getRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.max(output, -this.getDrone().getMaxYawRate()));
			}
		}
		else if (this.getDrone().getRoll() < underBoundary){
			if(!this.getRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.min(output, this.getDrone().getMaxYawRate()));
			}
		}
	}

	public final RollController getRollPI() {
		return this.rollPI;
	}

	public void setRollStarted(boolean isStarted){
		this.rollStarted = isStarted;
	}

	public boolean getRollStarted(){
		return this.rollStarted;
	}
	
	public void setPreviousTime(float time){
		this.previousTime = time;
	}
	
	public float getPreviousTime(){
		return this.previousTime;
	}

	public void setFinished(boolean finished){
		this.finishedScan = finished;
	}

	public boolean getFinished(){
		return this.finishedScan;
	}

	public Drone getDrone() {
		return this.drone;
	}
	public void setDrone(Drone drone) {
		this.drone = drone;
	}
}
