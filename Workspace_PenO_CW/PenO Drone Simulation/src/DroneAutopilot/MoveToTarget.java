package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import implementedClasses.Drone;

public class MoveToTarget {

	public MoveToTarget() {
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
		this.physicsCalculations.setDrone(this.getDrone());
	}
	
	public void checkcasespixelsfound(ArrayList<int[]> leftcamera, ArrayList<int[]> rightcamera){
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty())
			targetVisible(leftcamera, rightcamera);
	}

	private final float slowYaw = 0; // TODO bepalen in verhouding tot max

	public void noTargetFound() {
		this.setYawRate(this.slowYaw);
	}

	public void leftCameraFoundTarget() {
		this.setYawRate(-this.slowYaw);
	}

	public void rightCameraFoundTarget() {
		this.setYawRate(this.slowYaw);
	}

	
	public void targetVisible(ArrayList<int[]> leftCamera, ArrayList<int[]> rightCamera){
		this.setRollRate(0);
		int[] cogLeft = {0,0};
		int[] cogRight = {0,0};
		try{
			cogLeft = this.getImageCalculations().getCOG(leftCamera);
			cogRight = this.getImageCalculations().getCOG(rightCamera);
		} catch (EmptyPositionListException exception){
			exception.printStackTrace();
		}
		//TODO rightcamera voor diepte!!!!
		if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) >= this.underBoundary
				|| this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) <= this.upperBoundary) {
			this.setYawRate(0);
			this.flyTowardsTarget(cogLeft);
			}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= this.underBoundary)
			this.setYawRate(-this.getDrone().getMaxYawRate());
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= this.upperBoundary)
			this.setYawRate(this.getDrone().getMaxYawRate());
	}

	private final float underBoundary = -10; // TODO bepalen betere waarde
	private final float upperBoundary = 10;

	public void correctRoll() {
		if (this.getDrone().getRoll() >= this.upperBoundary)
			this.setRollRate(-this.getDrone().getMaxRollRate());
		else if (this.getDrone().getRoll() <= this.underBoundary)
			this.setRollRate(this.getDrone().getMaxRollRate());
	}
	
	public void flyTowardsTarget(int[] cog) {
		if (Math.abs(this.getDrone().getPitch() - this.getPhysicsCalculations().getVisiblePitch()) >= this.pitchUpper) {
			this.setPitchRate(this.getDrone().getMaxPitchRate());
		}
		else { this.setPitchRate(0);
		this.setThrust(this.getPhysicsCalculations().getThrust(cog));			
		}
	
	}

	public void hover() {
		if (this.getDrone().getPitch() >= this.pitchUpper) {
			this.setPitchRate(Math.min(-this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
		}
		
		else if (this.getDrone().getPitch() <= this.pitchUnder) {
			this.setPitchRate(Math.min(this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
		}

		else {
			this.setPitchRate(0);
			this.setThrust(Math.min(this.getDrone().getMaxThrust(), this.getDrone().getGravity() + this.getDrone().getDrag()));
		}

	}

	private final float pitchUnder = -3; // TODO betere waarden
	private final float pitchUpper = 3;

	public final ImageCalculations getImageCalculations() {
		return this.imageCalculations;
	}

	private final ImageCalculations imageCalculations;

	public final PhysicsCalculations getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	private final PhysicsCalculations physicsCalculations;

	public void setPitchRate(float value) {
		this.pitchRate = value;
	}

	public float getPitchRate() {
		return this.pitchRate;
	}

	private float pitchRate;

	public void setYawRate(float value) {
		this.yawRate = value;
	}

	public float getYawRate() {
		return this.yawRate;
	}

	private float yawRate;

	public void setRollRate(float value) {
		this.rollRate = value;
	}

	public float getRollRate() {
		return this.rollRate;
	}

	private float rollRate;

	public void setThrust(float value) {
		this.thrust = value;
	}

	public float getThrust() {
		return this.thrust;
	}

	private float thrust;

	

	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;
}
