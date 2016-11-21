package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import DroneAutopilot.controllers.PitchController;
import DroneAutopilot.controllers.RollController;
import DroneAutopilot.controllers.ThrustController;
import DroneAutopilot.controllers.YawController;
import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class MoveToTarget_new {

	private final PhysicsCalculations_new physicsCalculations;
	private final ImageCalculations_new imageCalculations;
	private final YawController yawPI;
	private final RollController rollPI;
	private final PitchController pitchPI;
	private final ThrustController thrustPI;

	private Drone drone;
	private float startFlyingTime;
	private GUI gui;
	private boolean basisgeval;
	private boolean deceleration;

	private static final float underBoundary = -0.2f;
	private static final float upperBoundary = 0.2f;

	public MoveToTarget_new(Drone drone) {
		this.setDrone(drone);
		this.imageCalculations = new ImageCalculations_new();
		this.physicsCalculations = new PhysicsCalculations_new(drone);
		this.yawPI = new YawController(7,4);
		this.rollPI = new RollController(0,0);
		this.pitchPI = new PitchController(0,0);
		this.thrustPI = new ThrustController(0,0);
	}

	public void execute(int color){
		ArrayList<int[]> leftCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getLeftCamera(), color);
		ArrayList<int[]> rightCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getRightCamera(), color);
		if (this.checkRoll()) {
			this.checkcasespixelsfound(leftCameraList, rightCameraList);
		} else {
			//System.out.println("Correct roll");
			this.correctRoll();
		}
	}

	public boolean checkRoll() {
		//TODO Controller
		if (this.getDrone().getRoll() <= upperBoundary
				&& this.getDrone().getRoll() >= underBoundary) {
			this.getDrone().setRollRate(0);
			this.rollStarted = false;
			return true;
		}
		return false;
	}

	boolean rollStarted;
	public void correctRoll() {
		this.getDrone().setPitchRate(0);
		this.getDrone().setYawRate(0);
		//TODO thrust?
		this.getDrone().setThrust(
				Math.min(this.getDrone().getMaxThrust(),
						Math.abs(this.getDrone().getGravity())
						* this.getDrone().getWeight()));
		//TODO check waarden output + of -
		if (this.getDrone().getRoll() > upperBoundary){
			if(!rollStarted){
				this.rollPI.resetSetpoint(0);
				this.rollStarted = true;
			}else{
				float output =-this.rollPI.calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				this.getDrone().setRollRate(Math.max(output, -this.getDrone().getMaxYawRate()));
			}
		}else if (this.getDrone().getRoll() < underBoundary){
			if(!rollStarted){
				this.rollPI.resetSetpoint(0);
				this.rollStarted = true;
			}else{
				float output =-this.rollPI.calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				this.getDrone().setRollRate(Math.min(output, this.getDrone().getMaxYawRate()));
			}
		}
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

	boolean yawStarted;
	public void targetVisible(ArrayList<int[]> leftCamera,
			ArrayList<int[]> rightCamera) {
		float[] cogLeft = this.findBestCenterOfGravity(leftCamera, this
				.getDrone().getLeftCamera());
		float[] cogRight = this.findBestCenterOfGravity(rightCamera, this
				.getDrone().getRightCamera());
		updateGUI(cogLeft, cogRight);

		System.out.println(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight));
		if(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight)>=underBoundary 
				&& this.getPhysicsCalculations().horizontalAngleDeviation(
						cogLeft, cogRight) <= upperBoundary){
			System.out.println("tussen 1 en -1");
			this.yawStarted = false;
			this.getDrone().setYawRate(0);
			this.flyTowardsTarget(cogLeft, cogRight);
		}else if (this.getPhysicsCalculations().horizontalAngleDeviation(
				cogLeft, cogRight) < upperBoundary) {
			if(!yawStarted){
				this.yawPI.resetSetpoint(0);
				this.yawStarted = true;
			}else{
				float output =-this.yawPI.calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				this.getDrone().setYawRate(Math.max(output, -this.getDrone().getMaxYawRate()));
			}
		} else if (this.getPhysicsCalculations().horizontalAngleDeviation(
				cogLeft, cogRight) > upperBoundary){
			if(!yawStarted){
				this.yawPI.resetSetpoint(0);
				this.yawStarted = true;
			}else{
				float output = -this.yawPI.calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				this.getDrone().setYawRate(Math.min(output, this.getDrone().getMaxYawRate()));
			}
		}	
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

	public void hover(float[] cog) {
		System.out.println("hover");
		if (this.getDrone().getPitch() > 0) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),this.getPhysicsCalculations().getThrust(cog)));
		} else if (this.getDrone().getPitch() < 0) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),this.getPhysicsCalculations().getThrust(cog)));
		} else {
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(
					Math.min(this.getDrone().getMaxThrust(),
							Math.abs(this.getDrone().getGravity())
							* this.getDrone().getWeight()));
		}
	}

	public void flyTowardsTarget(float[] cogL, float[] cogR) { 
		this.getPhysicsCalculations().updateAccSpeed(cogL);
		float partAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;
		if(!basisgeval && !deceleration){
			if(this.getPhysicsCalculations().verticalAngleDeviation(cogL)>partAngleView){
				//TODO: goede waarde vinden vr value.
				this.getDrone().setThrust((float) (-1.2*this.getDrone().getWeight()*this.getDrone().getGravity()));
			} 
			if(this.getPhysicsCalculations().verticalAngleDeviation(cogL)<-partAngleView){
				this.getDrone().setThrust((float) (1.2*this.getDrone().getWeight()*this.getDrone().getGravity()));
			}
			else{
				basisgeval=true;
			}
		}else if(basisgeval && !deceleration){
			if(this.getDrone().getPitch() < partAngleView){
				this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate()/2);
				this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
			}else{
				this.getDrone().setPitchRate(0);
				this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
			}
		}

		//System.out.println("remafstand: " + this.getPhysicsCalculations().getDecelerationDistance());
		//System.out.println("distance: " + this.getPhysicsCalculations().getDistance(cogL, cogR));
		if (this.getPhysicsCalculations().getDistance(cogL, cogR) <= this.getPhysicsCalculations().getDecelerationDistance()) {
			this.startDeceleration(cogL,cogR);
			deceleration = true;
		}

		if(this.getPhysicsCalculations().getSpeed()<=0.3 && deceleration){
			this.hover(cogL);
		}
		else if(deceleration){
			this.startDeceleration(cogL, cogR);
		}

	}

	public void startDeceleration(float[] cogL, float[] cogR) {
		float partAngleView = this.getDrone().getLeftCamera()
				.getVerticalAngleOfView()/10;
		if(this.getDrone().getPitch()>-partAngleView){
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			System.out.println("TRGPITCHEN");
			this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
		}else{
			this.getDrone().setPitchRate(0);
			this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
		}
	}

	public final PhysicsCalculations_new getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	public final ImageCalculations_new getImageCalculations() {
		return this.imageCalculations;
	}
	
	public final YawController getYawPI() {
		return this.yawPI;
	}
	
	public final RollController getRollPI() {
		return this.rollPI;
	}
	
	public final PitchController getPitchPI() {
		return this.pitchPI;
	}
	
	public final ThrustController getThrustPI() {
		return this.thrustPI;
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
