package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class MoveToTarget{

	public MoveToTarget(Drone drone) {
		this.setDrone(drone);
		setSlowYaw();
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);
		this.firstDistanceAndTime = true;
		this.setSpeed(0);
	}
	
	private float slowYaw;
	private static final float underBoundary = -5;
	private static final float upperBoundary = 5;
	
	public void execute(){
	ArrayList<int[]> leftCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getLeftCamera());
	ArrayList<int[]> rightCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getRightCamera());
	if (this.getImageCalculations().checkIfAllRed(this.getDrone().getLeftCamera()) 
			&& this.getImageCalculations().checkIfAllRed(this.getDrone().getRightCamera())){
		System.out.println("bereikt");
		this.hover();
	}else{
		if (this.checkRoll()){
			this.checkcasespixelsfound(leftCameraList, rightCameraList);
		}else{
			System.out.println("correct roll");
			this.correctRoll();
		}
		}}
	private void setSlowYaw() {
		 this.slowYaw = Math.max(this.getDrone().getMaxYawRate()/4, 5);
	}

	public boolean checkRoll(){
		if (this.getDrone().getRoll() <= upperBoundary && this.getDrone().getRoll() >= underBoundary){
			this.getDrone().setRollRate(0);
			return true;
		}
		return false;
	}

	public void correctRoll() {
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(0);
		this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight());
		if (this.getDrone().getRoll() >= upperBoundary)
			this.getDrone().setRollRate(-this.getDrone().getMaxRollRate());
		else if (this.getDrone().getRoll() <= underBoundary)
			this.getDrone().setRollRate(this.getDrone().getMaxRollRate());
	}
	
	public void checkcasespixelsfound(ArrayList<int[]> leftcamera, ArrayList<int[]> rightcamera){
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty()){
			targetVisible(leftcamera, rightcamera);
		}
	}

	public void noTargetFound() {
		this.hover();
		this.getDrone().setYawRate(slowYaw);
	}

	public void leftCameraFoundTarget() {
		this.hover();
		this.getDrone().setYawRate(-slowYaw);
	}

	public void rightCameraFoundTarget() {
		this.hover();
		this.getDrone().setYawRate(slowYaw);
	}
	
	public void targetVisible(ArrayList<int[]> leftCamera, ArrayList<int[]> rightCamera){
		this.getDrone().setRollRate(0);
		float[] cogLeft = this.findBestCenterOfGravity(leftCamera, this.getDrone().getLeftCamera());
		float[] cogRight = this.findBestCenterOfGravity(rightCamera, this.getDrone().getRightCamera());
		this.updateGUI(cogLeft, cogRight);
		if(firstDistanceAndTime){
			this.setPreviousDistance(this.getPhysicsCalculations().getDistance(cogLeft, cogRight));
			this.setPreviousTime(this.getDrone().getCurrentTime());
		}else{
			this.updateSpeed(cogLeft, cogRight);
		}
		if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) >= underBoundary
				|| this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) <= upperBoundary) {
			this.getDrone().setYawRate(0);
			this.flyTowardsTarget(cogLeft,cogRight);
			}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= underBoundary)
			this.getDrone().setYawRate(-this.getDrone().getMaxYawRate());
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= upperBoundary)
			this.getDrone().setYawRate(this.getDrone().getMaxYawRate());
	}

	public float[] findBestCenterOfGravity(ArrayList<int[]> pixelsFound, Camera camera){
		float[] cog = {0,0};
		try{
			cog = this.getImageCalculations().centerOfCircle(pixelsFound, camera);
		} catch (SmallCircleException e) {
			try {
				cog = this.getImageCalculations().getCOG(pixelsFound);
			} catch (EmptyPositionListException e1) {
				e1.printStackTrace();
			}
			
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
		return cog;
	}
	
	public void flyTowardsTarget(float[] cogL, float[] cogR) {
		float halfAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView()/2;
		if (this.getPhysicsCalculations().getDepth(cogL, cogR) <= 10f){
			this.hover();
			System.out.println("hover");
		}
		else if (this.getPhysicsCalculations().getVisiblePitch(cogL,cogR)-this.getPhysicsCalculations().verticalAngleDeviation(cogL) >= 0) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));
		}
		else if (this.getPhysicsCalculations().getVisiblePitch(cogL, cogR)-this.getPhysicsCalculations().verticalAngleDeviation(cogL) <= underBoundary) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));			
		} else if (this.getPhysicsCalculations().getVisiblePitch(cogL,cogR)== PhysicsCalculations.getDecelerationFactor()*halfAngleView){
				this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
				this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));			
		}
		else { 
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			this.getDrone().setYawRate(0);
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));		
			//System.out.println("thrust" + this.getPhysicsCalculations().getThrust(cogL));
		}
	}

	public void hover() {
		if (this.getDrone().getPitch() > 0) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}
		else if (this.getDrone().getPitch() < 0) {
			this.getDrone().setPitchRate(Math.min(this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}
		else {
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}

	}
	
	public void updateGUI(float[] centerOfGravityL, float[] centerOfGravityR){
		float distance = this.getPhysicsCalculations().getDistance(centerOfGravityL, centerOfGravityR);
		this.getGUI().update((int)(distance*100));
	}

	public void updateSpeed(float[] cogL, float[] cogR){
		float newDistance =this.getPhysicsCalculations().getDistance(cogL, cogR);
		float newTime = this.getDrone().getCurrentTime();
		this.setSpeed(this.getPhysicsCalculations().calculateSpeed(previousDistance, newDistance, previousTime, newTime));
		this.setPreviousDistance(newDistance);
		this.setPreviousTime(newTime);
	}
	
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

	public void setGUI(GUI gui){
		this.gui = gui;
	}
	public GUI getGUI(){
		return this.gui;
	}

	private GUI gui;
	
	/**
	 * @return the previousDistance
	 */
	public float getPreviousDistance() {
		return previousDistance;
	}
	/**
	 * @param previousDistance the previousDistance to set
	 */
	public void setPreviousDistance(float previousDistance) {
		this.previousDistance = previousDistance;
	}

	private float previousDistance;
	
	/**
	 * @return the previousTime
	 */
	public float getPreviousTime() {
		return previousTime;
	}
	/**
	 * @param previousTime the previousTime to set
	 */
	public void setPreviousTime(float previousTime) {
		this.previousTime = previousTime;
	}

	private float previousTime;
	
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	private float speed;
	
	private boolean firstDistanceAndTime;
}
