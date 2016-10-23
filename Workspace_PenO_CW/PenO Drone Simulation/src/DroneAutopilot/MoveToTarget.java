package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import implementedClasses.Drone;


public class MoveToTarget {

	public MoveToTarget(){
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
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
		float angle = 0;//uitwerking catch: op max wanneer geen rode pixels, anders wordt die hieronder aangepast
		try{
			int[] centerOfGravity = getImageCalculations().getCOG(getImageCalculations().getRedPixels(drone.getLeftCamera())); //enkel linkercamera herhaal voor rechter
			angle = getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity);
		} catch (EmptyPositionListException exception){
			// geen rode pixels
			angle = maxHorAngleDev;
		}
		this.setYawRate(drone.getMaxYawRate());
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
