package DroneAutopilot;

import java.util.ArrayList;

import implementedClasses.Drone;


public class MoveToTarget {

	public MoveToTarget(){
		
	}
	
	public void checkcasespixelsfound( Drone drone, ArrayList leftcamera, ArrayList rightcamera){
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty())
			targetVisible(drone);
	}
	
	private float slowYaw; //TODO bepalen in verhouding tot max
	
	public void noTargetFound(){
		this.setYawRate(this.slowYaw);
	}
	
	public void leftCameraFoundTarget(){
		this.setYawRate(-this.slowYaw);
	}
	
	public void rightCameraFoundTarget(){
		this.setYawRate(this.slowYaw);
	}
	
	//TODO checken wanneer aangekomen (yaw) en die terug op nul zetten -> boolean
	public void targetVisible(Drone drone){
		this.setYawRate(0);
		this.setRollRate(0);
		int[] pointOfGravity = ; //functie zwaartepunt
		float angle = PhysicsCalculations.horizontalAngleDeviation(drone, pointOfGravity);
		this.setYawRate(drone.getMaxYawRate());
	}
	
	public void setPitchRate(float value){
		this.pitchRate = value;
	}
	
	public float getPitchRate(){
		return this.pitchRate;
	}
	
	private float pitchRate;
	
	public void setYawRate(float value){
		this.yawRate = value;
	}
	
	public float getYawRate(){
		return this.yawRate;
	}
	
	private float yawRate;
	
	public void setRollRate(float value){
		this.rollRate = value;
	}
	
	public float getRollRate(){
		return this.rollRate;
	}
	
	private float rollRate;
	
}
