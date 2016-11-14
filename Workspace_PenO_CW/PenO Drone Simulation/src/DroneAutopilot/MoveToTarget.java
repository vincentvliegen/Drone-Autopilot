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
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);
		this.setSpeed(0);
		this.setAcceleration(0);
	}
	
	private static final float underBoundary = -1f;
	private static final float upperBoundary = 1f;
	
	public void execute(){
		ArrayList<int[]> leftCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getLeftCamera());
		ArrayList<int[]> rightCameraList = this.getImageCalculations().getRedPixels(this.getDrone().getRightCamera());
		if (this.checkRoll()){
			this.checkcasespixelsfound(leftCameraList, rightCameraList);
		}else{
			System.out.println("correct roll");
			this.correctRoll();
		}
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
		this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
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
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate());
	}

	public void leftCameraFoundTarget() {
		this.hover();
		this.getDrone().setYawRate(-this.getDrone().getMaxYawRate());
	}

	public void rightCameraFoundTarget() {
		this.hover();
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate());
	}
	
	public void targetVisible(ArrayList<int[]> leftCamera, ArrayList<int[]> rightCamera){
		this.getDrone().setRollRate(0);
		float[] cogLeft = this.findBestCenterOfGravity(leftCamera, this.getDrone().getLeftCamera());
		float[] cogRight = this.findBestCenterOfGravity(rightCamera, this.getDrone().getRightCamera());
		
		this.updateDistance(cogLeft, cogRight);
		this.updateAcceleration(cogLeft);
		this.updateSpeed(cogLeft, cogRight);
		this.updateGUI();
//		System.out.println("distance "+getDistance());
		System.out.println("speed "+getSpeed());
//		System.out.println("acceleration "+getAcceleration());
		
		if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) >= underBoundary
				&& this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft,cogRight) <= upperBoundary) {
			this.getDrone().setYawRate(0);
			this.flyTowardsTarget(cogLeft,cogRight);
			}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= underBoundary){
			this.getDrone().setYawRate(-this.getDrone().getMaxYawRate());
			}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= upperBoundary)
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
	
	private boolean decel = false;
	public void flyTowardsTarget(float[] cogL, float[] cogR) {
		float halfAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView()/2;
		//System.out.println("depth " + this.getPhysicsCalculations().getDepth(cogL, cogR));
		//System.out.println("rem " + this.calculateDecelerationDistance());
		if (this.getPhysicsCalculations().getDistance(cogL, cogR) <= this.getPhysicsCalculations().calculateDecelerationDistance()){
			decel = true;
		}
		if(decel){
			this.startDeceleration(cogL,cogR);
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
		}
	}

	public void hover() {
		if (this.getDrone().getPitch() >0){
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}else if (this.getDrone().getPitch() <0){
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}else{
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));
		}
	}
	
	public void updateGUI(){
		this.getGUI().update(this.getDistance());
	}

	public void updateDistance(float[] cogL, float[] cogR){
		this.setDistance(this.getPhysicsCalculations().getDistance(cogL, cogR));
	}
	
	public void updateSpeed(float[] cogL, float[] cogR){
		float distance =this.getPhysicsCalculations().getDistance(cogL, cogR);
		float time = this.getDrone().getCurrentTime();
		this.setSpeed(this.getPhysicsCalculations().calculateSpeed(time,distance));
	}
	
	public void updateAcceleration(float[] cog){
		this.setAcceleration(this.getPhysicsCalculations().calculateAcceleration(cog));
	}
	
	public void startDeceleration(float[] cogL, float[] cogR){
//		System.out.println("pitch" + this.getDrone().getPitch());
//		System.out.println(Math.abs(this.getDrone().getPitch())/this.getDrone().getMaxPitchRate());
//		System.out.println(this.getPhysicsCalculations().getDepth(cogL, cogR));
//		System.out.println("    startdecel");
		if(this.getSpeed()<0.5){
			System.out.println("hover");
			this.hover();
		}
		else{
			if (this.getDrone().getPitch() > 0){
				this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
				this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
			} else if (this.getDrone().getPitch() <= 0 && this.getDrone().getPitch() > -5f){
				this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
				float thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()/Math.cos(Math.toRadians(this.getDrone().getPitch())));
				this.getDrone().setThrust(thrust);
			}else{
				this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
				float thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()/Math.cos(Math.toRadians(this.getDrone().getPitch())));
				this.getDrone().setThrust(thrust);
			}
		}
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
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	private float distance;
	
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
	
	/**
	 * @return the acceleration
	 */
	public float getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	private float acceleration;
	
}
