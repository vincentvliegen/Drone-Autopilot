package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class MoveToTarget_new {

	private final PhysicsCalculations_new physicsCalculations;
	private final ImageCalculations_new imageCalculations;
	private Drone drone;
	private float startFlyingTime;
	private GUI gui;
	private boolean basisgeval;
	private boolean firstTimeFlyTowardsTarget;
	
	private static final float underBoundary = -1f;
	private static final float upperBoundary = 1f;

	public MoveToTarget_new(Drone drone) {
		this.setDrone(drone);
		this.imageCalculations = new ImageCalculations_new();
		this.physicsCalculations = new PhysicsCalculations_new(drone);
	}

	public void execute(int color){
		ArrayList<int[]> leftCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getLeftCamera(), color);
		ArrayList<int[]> rightCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getRightCamera(), color);
		if (this.checkRoll()) {
			this.checkcasespixelsfound(leftCameraList, rightCameraList);
		} else {
			System.out.println("Correct roll");
			this.correctRoll();
		}
	}

	public boolean checkRoll() {
		//TODO Controller
		if (this.getDrone().getRoll() == 0
				&& this.getDrone().getRoll() == 0) {
			this.getDrone().setRollRate(0);
			return true;
		}
		return false;
	}

	public void correctRoll() {
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(0);
		this.getDrone().setThrust(
				Math.min(this.getDrone().getMaxThrust(),
						Math.abs(this.getDrone().getGravity())
						* this.getDrone().getWeight()));
		if (this.getDrone().getRoll() > 0)
			this.getDrone().setRollRate(-this.getDrone().getMaxRollRate());
		else if (this.getDrone().getRoll() < 0)
			this.getDrone().setRollRate(this.getDrone().getMaxRollRate());
	}

	public void checkcasespixelsfound(ArrayList<int[]> leftcamera,
			ArrayList<int[]> rightcamera) {
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty()) {
			targetVisible(leftcamera, rightcamera);
		}
	}

	public void noTargetFound() {
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate()/2);
	}

	public void leftCameraFoundTarget() {
		this.getDrone().setYawRate(-this.getDrone().getMaxYawRate()/2);
	}

	public void rightCameraFoundTarget() {
		this.getDrone().setYawRate(this.getDrone().getMaxYawRate()/2);
	}

	public void targetVisible(ArrayList<int[]> leftCamera,
			ArrayList<int[]> rightCamera) {
		float[] cogLeft = this.findBestCenterOfGravity(leftCamera, this
				.getDrone().getLeftCamera());
		float[] cogRight = this.findBestCenterOfGravity(rightCamera, this
				.getDrone().getRightCamera());
		updateGUI(cogLeft, cogRight);

		if(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight)>=underBoundary 
				&& this.getPhysicsCalculations().horizontalAngleDeviation(
				cogLeft, cogRight) <= upperBoundary){
			this.getDrone().setYawRate(0);
			this.flyTowardsTarget(cogLeft, cogRight);
		}else if (this.getPhysicsCalculations().horizontalAngleDeviation(
				cogLeft, cogRight) <= upperBoundary) {
			this.getDrone().setYawRate(-this.getDrone().getMaxYawRate());
		} else if (this.getPhysicsCalculations().horizontalAngleDeviation(
				cogLeft, cogRight) >= upperBoundary)
			this.getDrone().setYawRate(this.getDrone().getMaxYawRate());
	}

	public void updateGUI(float[] centerOfGravityL, float[] centerOfGravityR) {
		this.getGUI().update(this.getPhysicsCalculations().getDistance(centerOfGravityL, centerOfGravityR));
	}

	public float[] findBestCenterOfGravity(ArrayList<int[]> pixelsFound,
			Camera camera) {
		float[] cog = { 0, 0 };
		try {
			cog = this.getImageCalculations().centerOfCircle(pixelsFound,
					camera);
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

	public void hover() {
		if (this.getDrone().getPitch() > 0) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),
							Math.abs(this.getDrone().getGravity())
							* this.getDrone().getWeight()));
		} else if (this.getDrone().getPitch() < 0) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),
							Math.abs(this.getDrone().getGravity())
							* this.getDrone().getWeight()));
		} else {
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),
							Math.abs(this.getDrone().getGravity())
							* this.getDrone().getWeight()));
		}
	}

	public void flyTowardsTarget(float[] cogL, float[] cogR) { 
		if(!firstTimeFlyTowardsTarget){
			this.setStartFlyingTime(this.getDrone().getCurrentTime());
			firstTimeFlyTowardsTarget=true;
		}
		float fourthAngleView = this.getDrone().getLeftCamera()
				.getVerticalAngleOfView() / 4;

		if(!basisgeval){
			if(this.getPhysicsCalculations().verticalAngleDeviation(cogL)>upperBoundary){
				//TODO: goede waarde vinden vr value.
				this.getDrone().setThrust((float) (-1.2*this.getDrone().getWeight()*this.getDrone().getGravity()));
			} 
			if(this.getPhysicsCalculations().verticalAngleDeviation(cogL)<underBoundary){
				this.getDrone().setThrust((float) (1.2*this.getDrone().getWeight()*this.getDrone().getGravity()));
			}
			else{
				basisgeval=true;
			}
		}else{
			if(this.getDrone().getPitch() < fourthAngleView){
				this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate()/2);
				this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
			}else{
				this.getDrone().setPitchRate(0);
				this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
			}
		}
		
		System.out.println("remafstand: " + this.getPhysicsCalculations().calculateDecelerationDistance(cogL, cogR, startFlyingTime));
		System.out.println("distance: " + this.getPhysicsCalculations().getDistance(cogL, cogR));
		if (this.getPhysicsCalculations().getDistance(cogL, cogR) <= this
				.getPhysicsCalculations().calculateDecelerationDistance(cogL,cogR,this.getStartFlyingTime())) {
			this.startDeceleration(cogL,cogR);
		}
		
		if(this.getPhysicsCalculations().getDistance(cogL, cogR)==1){
			this.hover();
		}
		
		this.setStartFlyingTime(this.getDrone().getCurrentTime());

	}

	public void startDeceleration(float[] cogL, float[] cogR) {
		float fourthAngleView = this.getDrone().getLeftCamera()
				.getVerticalAngleOfView() / 4;
		while(this.getDrone().getPitch()>fourthAngleView)
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
		this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
	}

	public final PhysicsCalculations_new getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	public final ImageCalculations_new getImageCalculations() {
		return this.imageCalculations;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public Drone getDrone() {
		return this.drone;
	}

	public float getStartFlyingTime() {
		return startFlyingTime;
	}

	public void setStartFlyingTime(float time) {
		this.startFlyingTime = time;
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	public GUI getGUI() {
		return this.gui;
	}




}
