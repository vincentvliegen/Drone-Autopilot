package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import DroneAutopilot.algoritmes.WorldScan;
import DroneAutopilot.calculations.ImageCalculations;
import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.controllers.DistanceController;
import DroneAutopilot.correct.CorrectHeight;
import DroneAutopilot.correct.CorrectPitch;
import DroneAutopilot.correct.CorrectRoll;
import DroneAutopilot.correct.CorrectYaw;
import p_en_o_cw_2016.Drone;

public class MoveToTarget {

	private final PhysicsCalculations physicsCalculations;
	private final ImageCalculations imageCalculations;
	private final WorldScan worldScan;
	private final DistanceController distancePI;
	private final CorrectHeight heightCorrector;
	private final CorrectRoll rollCorrector;
	private final CorrectYaw yawCorrector;
	private final CorrectPitch pitchCorrector;

	private Drone drone;
	private GUI gui;
	private boolean pitchStarted;
	private float[] cogL;
	private float[] cogR;

	private boolean scanning;
	private int color;

	public MoveToTarget(Drone drone) {
		this.setDrone(drone);
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);
		this.worldScan = new WorldScan(drone);
		this.pitchCorrector = new CorrectPitch(drone);
		this.heightCorrector = new CorrectHeight(drone);
		this.rollCorrector = new CorrectRoll(drone);
		this.yawCorrector = new CorrectYaw(drone);
		this.distancePI = new DistanceController(0.5, 0.5);
	}

	public void execute(int color) {
		this.setColor(color);
		ArrayList<int[]> leftCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getLeftCamera(),
				color);
		ArrayList<int[]> rightCameraList = this.getImageCalculations()
				.getPixelsOfColor(this.getDrone().getRightCamera(), color);

		this.getRollCorrector().correctRoll();
		if (this.getWorldScan().scan(leftCameraList, rightCameraList, color) == true) {
			this.setScanning(false);
			this.targetVisible(leftCameraList, rightCameraList);
		} else {
			this.setScanning(true);
		}
		// this.getWorldScan().scan(this.getDrone(),
		// this.getImageCalculations()); @Jef dit uncomment en de if erboven
		// comment
	}

	public void targetVisible(ArrayList<int[]> leftCameraList, ArrayList<int[]> rightCameraList) {
		float[] cogLeft = this.getImageCalculations().findBestCenterOfGravity(leftCameraList,
				this.getDrone().getLeftCamera());
		this.setCogL(cogLeft);
		float[] cogRight = this.getImageCalculations().findBestCenterOfGravity(rightCameraList,
				this.getDrone().getRightCamera());
		this.setCogR(cogRight);
		this.updateGUI();
		this.flyTowardsTarget();
	}

	public void updateGUI() {
		this.getGUI().update(this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()),this.getColor());
	}

	public void flyTowardsTarget() {
		this.getYawCorrector().correctYaw(this.getCogL(), this.getCogR());
		this.getHeightCorrector().correctHeight(this.getCogL(), this.getCogR());
		//this.getPhysicsCalculations().calculateSpeed(this.getDrone().getCurrentTime(),this.getPhysicsCalculations().getDistance(getCogL(), getCogR()));
		/*
		System.out.println("-------------");
		System.out.println("dist: " + this.getPhysicsCalculations().getDistance(cogL, cogR));
		System.out.println("setpoint1: " + this.getDistancePI().getSetpoint());
		System.out.println("-------------");
		*/
		//System.out.println("Horiz " + this.getPhysicsCalculations().horizontalAngleDeviation(getCogL(), getCogR()));
		//		System.out.println("distance: " + this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()));
		if (this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()) <= 0.6 && !this.getYawCorrector().isYawStarted()) {
			this.getPitchCorrector().hover();
			this.setPitchStarted(false);
		}else if ( Math.abs(this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()) - this.getDistancePI().getSetpoint())>0.2f || !this.getPitchStarted()) {
			this.getDistancePI().resetSetpoint(this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()) - 0.1f);
			this.getDrone().setPitchRate(0);
			this.setPitchStarted(true);
		} else if (!this.getYawCorrector().isYawStarted()){
			float output = -this.getDistancePI().calculateRate(
					this.getPhysicsCalculations().getDistance(this.getCogL(), this.getCogR()),
					this.getDrone().getCurrentTime());
			// this.updategraphPI((int) (this.getDrone().getCurrentTime()),
			// (int) (this.getPhysicsCalculations().getDistance(cogL,
			// cogR))*10);
			this.getDrone().setPitchRate(output);
			//System.out.println("Pitch: " + this.getDrone().getPitch());
			if (this.getDrone().getPitch()<0) {
				this.setPitchStarted(false);
			}
		}

	}

	public void startDeceleration() {
		float partAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView() / 20;
		if (this.getDrone().getPitch() > -partAngleView) {
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			System.out.println("TRGPITCHEN");
		} else {
			this.getDrone().setPitchRate(0);
		}
	}

	////////// Getters & Setters//////////

	public final PhysicsCalculations getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	public final ImageCalculations getImageCalculations() {
		return this.imageCalculations;
	}

	public final DistanceController getDistancePI() {
		return this.distancePI;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public Drone getDrone() {
		return this.drone;
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	public GUI getGUI() {
		return this.gui;
	}

	public void setPitchStarted(boolean isStarted) {
		this.pitchStarted = isStarted;
	}

	public boolean getPitchStarted() {
		return this.pitchStarted;
	}

	public WorldScan getWorldScan() {
		return worldScan;
	}

	public CorrectHeight getHeightCorrector() {
		return heightCorrector;
	}

	public CorrectRoll getRollCorrector() {
		return rollCorrector;
	}

	public CorrectYaw getYawCorrector() {
		return yawCorrector;
	}

	public CorrectPitch getPitchCorrector() {
		return pitchCorrector;
	}

	/**
	 * @return the cogL
	 */
	public float[] getCogL() {
		return cogL;
	}

	/**
	 * @param cogL
	 *            the cogL to set
	 */
	public void setCogL(float[] cogL) {
		this.cogL = cogL;
	}

	/**
	 * @return the cogR
	 */
	public float[] getCogR() {
		return cogR;
	}

	/**
	 * @param cogR
	 *            the cogR to set
	 */
	public void setCogR(float[] cogR) {
		this.cogR = cogR;
	}

	public boolean isScanning() {
		return scanning;
	}

	public void setScanning(boolean scanning) {
		this.scanning = scanning;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
