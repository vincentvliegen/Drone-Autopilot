package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import implementedClasses.Drone;

public class MoveToTarget {

	public MoveToTarget() {
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
	}
<<<<<<< HEAD

	public void checkcasespixelsfound(Drone drone, ArrayList leftcamera, ArrayList rightcamera) {
=======
	
	public void checkcasespixelsfound(Drone drone, ArrayList<int[]> leftcamera, ArrayList<int[]> rightcamera){
>>>>>>> a1bc0af6dad383bef2c8197f72d981287a0774c3
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty())
			targetVisible(drone, leftcamera);
	}

	private float slowYaw; // TODO bepalen in verhouding tot max

	public void noTargetFound() {
		this.setYawRate(this.slowYaw);
	}

	public void leftCameraFoundTarget() {
		this.setYawRate(-this.slowYaw);
	}

	public void rightCameraFoundTarget() {
		this.setYawRate(this.slowYaw);
	}
<<<<<<< HEAD

	public void targetVisible(Drone drone) {
		this.setRollRate(0);
		int[] centerOfGravity = { 0, 0 };
		try {
			// TODO nieuwe imagecalc aanmaken -> opnieuw over foto loopen..
			// beter arraylist meegeven.
			centerOfGravity = this.getImageCalculations()
					.getCOG(getImageCalculations().getRedPixels(drone.getLeftCamera())); // enkel linkercamera herhaal voor rechter
		} catch (EmptyPositionListException exception) {
=======
	
	public void targetVisible(Drone drone, ArrayList<int[]> leftCamera){
		this.setRollRate(0);
		int[] centerOfGravity = {0,0};
		try{
			centerOfGravity = this.getImageCalculations().getCOG(leftCamera); //enkel linkercamera herhaal voor rechter
		} catch (EmptyPositionListException exception){
>>>>>>> a1bc0af6dad383bef2c8197f72d981287a0774c3
			// geen rode pixels
		}
		if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) >= this.underBoundary
				|| this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) <= this.upperBoundary)
			this.setYawRate(0);
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) >= this.underBoundary)
			this.setYawRate(-drone.getMaxYawRate());
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(drone, centerOfGravity) <= this.upperBoundary)
			this.setYawRate(drone.getMaxYawRate());
	}

	private final float underBoundary = -10; // TODO bepalen betere waarde
	private final float upperBoundary = 10;

	public void correctRoll(Drone drone) {
		if (drone.getRoll() >= this.upperBoundary || drone.getRoll() <= this.underBoundary) // TODO mss splitsen  voor weinig  bijsturen  en veel / bijsturen
			this.setRollRate(drone.getMaxRollRate());
	}

	public void hover(Drone drone) {
		if (drone.getPitch() >= this.pitchUpper || drone.getPitch() <= this.pitchUnder) {
			this.setPitchRate(Math.min(drone.getMaxPitchRate(), -drone.getPitch()));
		}

		else {
			this.setThrust(Math.min(drone.getMaxThrust(), drone.getGravity() + drone.getDrag()));
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

}
