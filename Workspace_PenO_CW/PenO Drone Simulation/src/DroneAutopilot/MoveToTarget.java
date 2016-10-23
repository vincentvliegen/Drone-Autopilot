package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import implementedClasses.Drone;


public class MoveToTarget {

	public MoveToTarget(){
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
	}
	
	public void checkcasespixelsfound(Drone drone, ArrayList leftcamera, ArrayList rightcamera){
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
		this.setRollRate(0);
		int[] centerOfGravity = {0,0};
		try{
			centerOfGravity = getImageCalculations().getCOG(getImageCalculations().getRedPixels(drone.getLeftCamera())); //enkel linkercamera herhaal voor rechter
		} catch (EmptyPositionListException exception){
			// geen rode pixels
		}
		if (this.physicsCalculations.horizontalAngleDeviation(drone, centerOfGravity) >= -10 ||	//TODO bepalen betere waarde
				this.physicsCalculations.horizontalAngleDeviation(drone, centerOfGravity) <= 10)
			this.setYawRate(0);
		else
			this.setYawRate(drone.getMaxYawRate());
	}	
	
	public void correctRoll(Drone drone){
		if(drone.getRoll() >= 10 || drone.getRoll()<= -10) //TODO waarde bepalen of splitsen voor weinig bijsturen en veel bijsturen
			this.setRollRate(drone.getMaxRollRate());
	}
	
	public final ImageCalculations getImageCalculations(){
		return this.imageCalculations;
	}
	private final ImageCalculations imageCalculations;
	
	public final PhysicsCalculations getPhysicsCalculations(){
		return this.physicsCalculations;
	}
	private final PhysicsCalculations physicsCalculations;
	
	
	
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
