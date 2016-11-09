package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Drone;

public class MoveToTarget{

	public MoveToTarget(Drone drone) {
		this.setDrone(drone);
		setSlowYaw();
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);
	}
	
	private float slowYaw;
	private static final float underBoundary = -10;
	private static final float upperBoundary = 10;
	private static final float pitchUnder = -3;
	private static final float pitchUpper = 3;
	
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
		float[] cogLeft = {0,0};
		float[] cogRight = {0,0};
		try{
			cogLeft = this.getImageCalculations().centerOfCircle(leftCamera, this.getDrone().getLeftCamera());
		} catch (SmallCircleException e) {
			try {
				cogLeft = this.getImageCalculations().getCOG(leftCamera);
			} catch (EmptyPositionListException e1) {
				e1.printStackTrace();
			}
			
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
		try{
			cogRight = this.getImageCalculations().centerOfCircle(rightCamera, this.getDrone().getRightCamera());
		} catch (SmallCircleException e) {
			try {
				cogRight = this.getImageCalculations().getCOG(rightCamera);
			} catch (EmptyPositionListException e1) {
				e1.printStackTrace();
			}
			
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
		
		this.updateGUI(cogLeft, cogRight);
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

	public void flyTowardsTarget(float[] cogL, float[] cogR) {
		float halfAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView()/2;
		if (this.getPhysicsCalculations().getVisiblePitch(cogL,cogR)-Math.abs(this.getPhysicsCalculations().verticalAngleDeviation(cogL)) >= 0) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));			
		}
		else if (halfAngleView-Math.abs(this.getPhysicsCalculations().verticalAngleDeviation(cogL)) <= pitchUpper) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL),this.getDrone().getMaxThrust()));			
			//System.out.println("terugpitch");
		} else if (this.getPhysicsCalculations().getVisiblePitch(cogL,cogR)== PhysicsCalculations.getDecelerationFactor()*halfAngleView){
				//System.out.println("max hoek changed" + this.getPhysicsCalculations().getVisiblePitch(cogL, cogR));
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
		if (this.getDrone().getPitch() >= pitchUpper) {
			this.getDrone().setPitchRate(Math.min(-this.getDrone().getMaxPitchRate(), -this.getDrone().getPitch()));
			this.getDrone().setThrust(Math.min(this.getDrone().getMaxThrust(), Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()));

		}
		
		else if (this.getDrone().getPitch() <= pitchUnder) {
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
		this.getGUI().update((int)(distance*100));//TODO verplaatsen naar MoveToTarget, GUI uit PhysicsCalculations halen
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


}
