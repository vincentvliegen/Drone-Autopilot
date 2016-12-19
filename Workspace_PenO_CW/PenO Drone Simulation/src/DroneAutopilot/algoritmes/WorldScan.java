package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import DroneAutopilot.calculations.ImageCalculations;
import DroneAutopilot.correct.CorrectPitch;
import DroneAutopilot.correct.CorrectRoll;
import p_en_o_cw_2016.Drone;

public class WorldScan {
	private Drone drone;
	private boolean finishedScan;
	private boolean firstTime;
	private float previousTime;
	private float degreesTurned;
	private float timePassed = 0;
	private ImageCalculations imageCalculations;
	private final CorrectRoll rollCorrector;
	private final CorrectPitch	pitchCorrector;
	private final float yawrate;

	public WorldScan(Drone drone){
		this.setDrone(drone);
		this.imageCalculations = new ImageCalculations();
		this.rollCorrector = new CorrectRoll(drone);
		this.pitchCorrector = new CorrectPitch(drone);
		this.yawrate = this.getDrone().getMaxYawRate()/4;
	}

	public boolean foundOrb(Drone drone){
		this.getImageCalculations().calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
		HashMap<Integer, ArrayList<int[]>> hashMapOfColors = this.getImageCalculations().getPixelsOfEachColor();
		//System.out.println("length " + hashMapOfColors.size());
		if(hashMapOfColors.size()>=1){
			return true;
		}
		return false;
	}

	public Set<Integer> getKeySetOfColors(Drone drone){
		this.getImageCalculations().calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
		HashMap<Integer, ArrayList<int[]>> hashMapOfColors = this.getImageCalculations().getPixelsOfEachColor();
		return hashMapOfColors.keySet();
	}

	public Set<Integer> scan(Drone drone){
		System.out.println("scan");
		float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
		this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
		this.setFinished(false);
		this.getRollCorrector().correctRoll();
		//		System.out.println("length " + hashMapOfColors.size());
		if(this.foundOrb(drone)){
			//System.out.println("iets gevonden");
			this.getDrone().setYawRate(0);
			this.getDrone().setPitchRate(0);
			this.setFinished(true);
			firstTime = false;
			degreesTurned = 0;
			timePassed = 0;
			return this.getKeySetOfColors(drone);
		}
		else{
			if(!firstTime){
				//System.out.println("firsttime");
				firstTime = true;
				this.setPreviousTime(this.getDrone().getCurrentTime());
			}else{
				//System.out.println("yaw");
				if(degreesTurned < 630){
					this.getDrone().setPitchRate(0);
					this.getDrone().setYawRate(this.getYawrate());
					degreesTurned += this.getYawrate() * (this.getDrone().getCurrentTime() - this.getPreviousTime());
					this.setPreviousTime(this.getDrone().getCurrentTime());
				}else{
					this.getDrone().setYawRate(0);
					//System.out.println("time passed" + timePassed);
					if(timePassed <30){
						this.getDrone().setPitchRate(-3);
						//berekening thrust om horizontaal achterwaarts te vliegen afhankelijk van pitch.
						gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
						this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
						timePassed += (this.getDrone().getCurrentTime() - this.getPreviousTime());
					}else{
						if(this.getDrone().getPitch()<= -0.01){
							gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
							this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
							this.getDrone().setPitchRate(1);
						}else{
							gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
							this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
							this.getDrone().setPitchRate(0);
							if(!this.foundOrb(drone)){
								degreesTurned = 0;
								timePassed = 0;
								firstTime = false;
								this.setFinished(true);
							}
						}
					}
				}
			}
			return null;
		}
	}

	public boolean scan(ArrayList<int[]> leftCameraList, ArrayList<int[]> rightCameraList, int color){
		//System.out.println("scan move to");
		boolean canStartFly = false;
		ArrayList<Boolean> targetFound = this.checkCasesPixelsFound(leftCameraList, rightCameraList);

		if(targetFound.get(0)==false && targetFound.get(1)==true){
			canStartFly = false;
			//this.rightCameraFoundTarget();
		}else if(targetFound.get(0)==true && targetFound.get(1)==false){
			canStartFly = false;
			//this.leftCameraFoundTarget();
		}else if(targetFound.get(0)==true && targetFound.get(1)==true){
			//this.getDrone().setYawRate(0);
			canStartFly = true;
		}else if(targetFound.get(0)==false && targetFound.get(1)==false){
			canStartFly = false;
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
		System.out.println("no target found");
		//System.out.println("roll " + this.getDrone().getRoll());
		//System.out.println("pitch " + this.getDrone().getPitch());
		//System.out.println("********");
		//naar hover gaan!
		float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
		this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(this.getYawrate());
	}

	public void leftCameraFoundTarget() {
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(-this.getYawrate());
		float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
		this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
	}

	public void rightCameraFoundTarget() {
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(this.getYawrate());
		float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
		this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
	}


	//////////Getters & Setters//////////
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

	public ImageCalculations getImageCalculations() {
		return imageCalculations;
	}

	public void setImageCalculations(ImageCalculations imageCalculations) {
		this.imageCalculations = imageCalculations;
	}

	public CorrectRoll getRollCorrector() {
		return rollCorrector;
	}

	public CorrectPitch getPitchCorrector() {
		return pitchCorrector;
	}

	public float getYawrate() {
		return yawrate;
	}
}
