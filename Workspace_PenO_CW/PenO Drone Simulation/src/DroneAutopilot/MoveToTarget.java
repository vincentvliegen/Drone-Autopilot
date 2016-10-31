package DroneAutopilot;

import java.util.ArrayList;
import exceptions.EmptyPositionListException;
import p_en_o_cw_2016.Drone;

public class MoveToTarget{

	public MoveToTarget() {
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations();
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

	private final float slowYaw = Math.max(this.getDrone().getMaxYawRate()/4, 5);

	public void noTargetFound() {
		this.getDrone().setYawRate(this.slowYaw);
	}

	public void leftCameraFoundTarget() {
		this.getDrone().setYawRate(-this.slowYaw);
	}

	public void rightCameraFoundTarget() {
		this.getDrone().setYawRate(this.slowYaw);
	}

	
	public void targetVisible(ArrayList<int[]> leftCamera, ArrayList<int[]> rightCamera){
		this.getDrone().setRollRate(0);
		int[] cogLeft = {0,0};
		int[] cogRight = {0,0};
		try{
			cogLeft = this.getImageCalculations().getCOG(leftCamera);
			cogRight = this.getImageCalculations().getCOG(rightCamera);
		} catch (EmptyPositionListException exception){
			exception.printStackTrace();
		}
		if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) >= underBoundary
				|| this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) <= upperBoundary) {
			this.getDrone().setYawRate(0);
			this.flyTowardsTarget(cogLeft);
			}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= underBoundary)
			this.getDrone().setYawRate(-this.getDrone().getMaxYawRate());
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= upperBoundary)
			this.getDrone().setYawRate(this.getDrone().getMaxYawRate());
	}

	private static final float underBoundary = -10; // TODO bepalen betere waarde
	private static final float upperBoundary = 10;

	public void correctRoll() {
		if (this.getDrone().getRoll() >= upperBoundary)
			this.getDrone().setRollRate(-this.getDrone().getMaxRollRate());
		else if (this.getDrone().getRoll() <= underBoundary)
			this.getDrone().setRollRate(this.getDrone().getMaxRollRate());
	}
	
	public boolean checkRoll(){
		if (this.getDrone().getRoll() <= upperBoundary && this.getDrone().getRoll() >= underBoundary)
			return true;
		return false;
	}
	
	public void flyTowardsTarget(int[] cog) {
		//moet dat niet visiblepitch - verticalehoek zijn?
		if (this.getPhysicsCalculations().getVisiblePitch()-Math.abs(this.getPhysicsCalculations().verticalAngleDeviation(cog)) >= pitchUpper) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
		}
		else { 
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			this.getDrone().setYawRate(0);
			this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cog));			
		}
	
	}

	public void hover() {
		if (this.getDrone().getPitch() >= pitchUpper) {
			this.getDrone().setPitchRate(Math.min(-this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
		}
		
		else if (this.getDrone().getPitch() <= pitchUnder) {
			this.getDrone().setPitchRate(Math.min(this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
		}

		else {
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), this.getDrone().getGravity() + this.getDrone().getDrag()));
		}

	}

	private static final float pitchUnder = -3; // TODO betere waarden
	private static final float pitchUpper = 3;

	public final ImageCalculations getImageCalculations() {
		return this.imageCalculations;
	}

	private final ImageCalculations imageCalculations;

	public final PhysicsCalculations getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	private final PhysicsCalculations physicsCalculations;
	

	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;



}
