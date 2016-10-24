package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import implementedClasses.Drone;


public class MoveToTarget {

	public MoveToTarget(){
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
	}
	
	public void checkcasespixelsfound(Drone drone, ArrayList<int[]> leftcamera, ArrayList<int[]> rightcamera){
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty())
			targetVisible(drone, leftcamera);
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
	
	public void targetVisible(Drone drone, ArrayList<int[]> leftCamera){
		this.setRollRate(0);
		int[] centerOfGravity = {0,0};
		try{
			centerOfGravity = this.getImageCalculations().getCOG(leftCamera); //enkel linkercamera herhaal voor rechter
		} catch (EmptyPositionListException exception){
			// geen rode pixels
		}
		if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) >= this.underBoundary ||	
				this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) <= this.upperBoundary)
			this.setYawRate(0);
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) >= this.underBoundary)
			this.setYawRate(-drone.getMaxYawRate());
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) <= this.upperBoundary)
			this.setYawRate(drone.getMaxYawRate());
	}	
	
	private final float underBoundary = -10; //TODO bepalen betere waarde
	private final float upperBoundary = 10;
	
	public void correctRoll(Drone drone){
		if(drone.getRoll() >= this.upperBoundary || drone.getRoll()<= this.underBoundary) //TODO mss splitsen voor weinig bijsturen en veel bijsturen
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
