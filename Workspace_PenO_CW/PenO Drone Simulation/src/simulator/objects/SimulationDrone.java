package simulator.objects;

import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.AutopilotFactory;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;

import DroneAutopilot.DroneAutopilotFactory;
import simulator.camera.DroneCamera;
import simulator.camera.DroneCameraPlace;
import simulator.physics.Movement;
import simulator.world.World;
import simulator.world.WorldParser;

public class SimulationDrone extends WorldObject implements Drone{
	private float pitch = 0;
	private float roll = 0;
	private float thrust = 0;
	private float yaw = 0;
	private Movement movement;
	private static float weight = 1.4f;
	private float pitchRate = 0;
	private float rollRate = 0;
	private float yawRate = 0;
	private Autopilot autopilot;
	private float gravityConstant = -9.81f;
	public DroneCamera leftCamera;
	public DroneCamera rightCamera;
	public DroneCamera middleCamera;
	GL2 gl;
	float height;
	float width;
	float depth;
	float[] color = new float[3];
	double[] translate = new double[3];
	static double[] standardTranslate = { 0, 0, 0 };
	List<Double> rotateMatrix = new ArrayList<>();
	List<Double> inverseRotateMatrix = new ArrayList<>();
	private float cameraSeperation = 0;
	private float radius;

	public SimulationDrone(GL2 gl, float height, float width, float depth,
			float[] color, double[] translate, World world) {
		super(world);
		this.height = height;
		this.width = width;
		this.depth = depth;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
		this.movement = new Movement(this);
		this.cameraSeperation = width;
		generateDroneCameras();
		AutopilotFactory factory = new DroneAutopilotFactory();
		this.autopilot = factory.create(this);
		this.radius = (float) Math.sqrt(Math.pow(height / 2, 2)
				+ Math.pow(width / 2, 2) + Math.pow(depth / 2, 2));
	}

	// TODO afmetingen (voor collision detection)

	public SimulationDrone(GL2 gl, float height, float width, float depth,
			float[] color, World world) {
		this(gl, height, width, depth, color, standardTranslate, world);
	}

	// TODO afmetingen (voor collision detection)

	public void draw() {

		gl.glPushMatrix(); // store current transform, so we can undo the
							// rotation
		// System.out.println(getTranslate()[0] + " " + getTranslate()[1] + " "
		// + getTranslate()[2]);
		translateDrone(getPosition());
		rotateDrone(-getYaw(), getRoll(), getPitch());
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glBegin(GL2ES3.GL_QUADS);

		// Top
		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);

		// Bottom
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);

		// Front
		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);

		// back
		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);

		// Left
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(-getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);

		// Right

		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, getDroneHeight() / 2,
				-getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				getDroneDepth() / 2);
		gl.glVertex3f(getDroneWidth() / 2, -getDroneHeight() / 2,
				-getDroneDepth() / 2);

		gl.glEnd();
		gl.glPopMatrix();// undo rotation

	}

	public void createRotateMatrix() {
		rotateMatrix.clear();
		rotateMatrix.add(Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw)) - Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw)));
		rotateMatrix.add(Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(pitch)));
		rotateMatrix.add(-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)) - Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll)));
		
		rotateMatrix.add(-Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch)) - Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw)));
		rotateMatrix.add(Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(roll)));
		rotateMatrix.add(Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)) - Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(roll)));
		
		rotateMatrix.add(Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw)));
		rotateMatrix.add(Math.sin(Math.toRadians(roll)));
		rotateMatrix.add(Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw)));
	}

	// TODO afmetingen (voor collision detection)

	public void translateDrone(double[] translate) {
		gl.glTranslated(translate[0], translate[1], translate[2]);
		this.translate = translate;
	}

	public void rotateDrone(float yaw, float roll, float pitch) {
		gl.glRotated(yaw, 0, 1, 0);
		gl.glRotated(roll, 1, 0, 0);
		gl.glRotated(pitch, 0, 0, 1);
	}

	public void timeHasPassed(float timePassed) {
		double yawPass = this.yawRate * timePassed;
		createInverseRotate();
		this.pitch += yawPass
				* new BigDecimal(inverseRotateMatrix.get(7)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.yaw += yawPass
				* new BigDecimal(inverseRotateMatrix.get(4)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.roll += yawPass
				* new BigDecimal(inverseRotateMatrix.get(1)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		checkYawPitchRoll();

		double rollPass = this.rollRate * timePassed;
		createInverseRotate();
		this.pitch += rollPass
				* new BigDecimal(inverseRotateMatrix.get(6)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.yaw += rollPass
				* new BigDecimal(inverseRotateMatrix.get(3)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.roll += rollPass
				* new BigDecimal(inverseRotateMatrix.get(0)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		checkYawPitchRoll();

		double pitchPass = this.pitchRate * timePassed;
		createInverseRotate();
		this.pitch += pitchPass
				* new BigDecimal(inverseRotateMatrix.get(8)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.yaw += pitchPass
				* new BigDecimal(inverseRotateMatrix.get(5)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		this.roll += pitchPass
				* new BigDecimal(inverseRotateMatrix.get(2)).setScale(2,
						BigDecimal.ROUND_HALF_DOWN).doubleValue();
		checkYawPitchRoll();

		// Wind rotation stuff
		this.yaw -= getWorld().getWindRotationY() * timePassed;
		this.roll += getWorld().getWindRotationX() * timePassed;
		this.pitch -= getWorld().getWindRotationZ() * timePassed;
		/*
		System.out.println("--------------");
		System.out.println("global pitch " + this.pitch);
		System.out.println("global yaw " + this.yaw);
		System.out.println("global roll " + this.roll);
		System.out.println("current pitch " + getPitch());
		System.out.println("current roll " + getRoll());
		System.out.println("pitchRate " + this.pitchRate);
		System.out.println("yawRate " + this.yawRate);
		System.out.println("rollRate " + this.rollRate);
		System.out.println("--------------");
		*/
		getLeftDroneCamera().updateDroneCamera();
		getRightDroneCamera().updateDroneCamera();
		getMiddleCamera().updateDroneCamera();

		this.autopilot.timeHasPassed();
	}

	private void checkYawPitchRoll() {
		if (Math.abs(pitch) > 90)
			pitch = Math.signum(pitch) * 180 - pitch;
		if (Math.abs(yaw) > 360)
			yaw -= Math.signum(yaw) * 360;
		if (Math.abs(roll) > 180)
			roll -= Math.signum(roll) * 360;
	}

	private void createInverseRotate() {
		inverseRotateMatrix.clear();
		inverseRotateMatrix.add(Math.cos(Math.toRadians(pitch))
				* Math.cos(Math.toRadians(yaw))
				- Math.sin(Math.toRadians(pitch))
				* Math.sin(Math.toRadians(roll))
				* Math.sin(Math.toRadians(yaw)));
		inverseRotateMatrix.add(Math.cos(Math.toRadians(yaw))
				* Math.sin(Math.toRadians(pitch))
				- Math.cos(Math.toRadians(pitch))
				* Math.sin(Math.toRadians(roll))
				* Math.sin(Math.toRadians(yaw)));
		inverseRotateMatrix.add(Math.cos(Math.toRadians(roll))
				* Math.sin(Math.toRadians(yaw)));

		inverseRotateMatrix.add(Math.cos(Math.toRadians(roll))
				* Math.sin(Math.toRadians(pitch)));
		inverseRotateMatrix.add(Math.cos(Math.toRadians(pitch))
				* Math.cos(Math.toRadians(roll)));
		inverseRotateMatrix.add(Math.sin(Math.toRadians(roll)));

		inverseRotateMatrix.add(-Math.cos(Math.toRadians(pitch))
				* Math.sin(Math.toRadians(yaw)) - Math.cos(Math.toRadians(yaw))
				* Math.sin(Math.toRadians(pitch))
				* Math.sin(Math.toRadians(roll)));
		inverseRotateMatrix.add(Math.sin(Math.toRadians(pitch))
				* Math.sin(Math.toRadians(yaw))
				- Math.cos(Math.toRadians(pitch))
				* Math.cos(Math.toRadians(yaw))
				* Math.sin(Math.toRadians(roll)));
		inverseRotateMatrix.add(Math.cos(Math.toRadians(roll))
				* Math.cos(Math.toRadians(yaw)));
	}

	private void generateDroneCameras() {

		// TODO
		// lookat moet recht vooruit zijn
		// dus X moet zelfde zijn, Y ook, Z iets verder

		float commonY = getDroneHeight() / 2;

		// left
		float leftX = getDroneWidth() / 2;
		float leftZ = -getCameraSeparation() / 2;
		leftCamera = new DroneCamera(leftX, commonY, leftZ, leftX + 100,
				commonY, leftZ, 0, 1, 0, getWorld(), this,
				DroneCameraPlace.LEFT);

		// right
		float rightX = getDroneWidth() / 2;
		float rightZ = getCameraSeparation() / 2;
		rightCamera = new DroneCamera(rightX, commonY, rightZ, rightX + 100,
				commonY, rightZ, 0, 1, 0, getWorld(), this,
				DroneCameraPlace.RIGHT);

		// middle
		middleCamera = new DroneCamera(-2, 0.5f, 0, -1 + 100, 0, 0, 0, 1, 0,
				getWorld(), this, DroneCameraPlace.MIDDLE);

		// add to list in world
		getWorld().addDroneCamera(leftCamera);
		getWorld().addDroneCamera(rightCamera);
		getWorld().addDroneCamera(middleCamera);
	}

	public void addRandomRotation() {
		Random r = new Random();
		int choice = r.nextInt(6);
		switch (choice) {
		case (0):
			this.pitch += Math.random() * 0.01;
			break;
		case (1):
			this.pitch -= Math.random() * 0.01;
			break;
		case (2):
			this.roll += Math.random() * 0.01;
			break;
		case (3):
			this.roll -= Math.random() * 0.01;
			break;
		case (4):
			this.yaw += Math.random() * 0.01;
			break;
		case (5):
			this.yaw -= Math.random() * 0.01;
			break;
		}
	}

	public void setThrust(float value) {
		if (value > getMaxThrust())
			this.thrust = getMaxThrust();
		else
			this.thrust = value;
	}

	@Override
	public void setPitchRate(float value) {
		if (Math.abs(value) > getMaxPitchRate())
			this.pitchRate = getMaxPitchRate() * Math.signum(value);
		else
			this.pitchRate = value;
	}

	@Override
	public void setRollRate(float value) {
		if (Math.abs(value) > getMaxRollRate())
			this.rollRate = getMaxRollRate() * Math.signum(value);
		else
			this.rollRate = value;
	}

	@Override
	public void setYawRate(float value) {
		if (Math.abs(value) > getMaxYawRate())
			this.yawRate = getMaxYawRate() * Math.signum(value);
		else
			this.yawRate = value;
	}

	public void setGravity(float value) {
		this.gravityConstant = value;
	}

	public float getDroneDepth() {
		return depth;
	}

	public float getDroneWidth() {
		return width;
	}

	public float getDroneHeight() {
		return height;
	}

	public Movement getMovement() {
		return this.movement;
	}

	public float getGlobalPitch() {
		return this.pitch;
	}

	public float getGlobalRoll() {
		return this.roll;
	}

	@Override
	public float getCameraSeparation() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getCameraSeparation();
		return cameraSeperation;
	}

	@Override
	public Camera getLeftCamera() {
		return leftCamera;
	}

	@Override
	public Camera getRightCamera() {
		return rightCamera;
	}

	@Override
	public float getWeight() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getWeight();
		return SimulationDrone.weight;
	}

	@Override
	public float getGravity() {
		if (getWorld() instanceof WorldParser)
			return (float) -getWorld().getParser().getGravity();
		return this.gravityConstant;
	}

	@Override
	public float getDrag() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getDrag();
		return 0.1f;
	}

	@Override
	public float getMaxThrust() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getMaxThrust();
		return -3f * SimulationDrone.weight * getGravity();
	}

	@Override
	public float getMaxPitchRate() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getMaxPitchRate();
		return 720f;
	}

	@Override
	public float getMaxRollRate() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getMaxRollRate();
		return 720f;
	}

	@Override
	public float getMaxYawRate() {
		if (getWorld() instanceof WorldParser)
			return (float) getWorld().getParser().getMaxYawRate();
		return 720f;
	}

	@Override
	public float getCurrentTime() {
		return getWorld().getCurrentTime();
	}

	public List<Double> getRotateMatrix() {
		return rotateMatrix;
	}

	public double[] getPosition() {
		return this.translate;
	}

	public DroneCamera getLeftDroneCamera() {
		return this.leftCamera;
	}

	public DroneCamera getRightDroneCamera() {
		return this.rightCamera;
	}

	public float getPitch() {
		return (float) (this.pitch*Math.cos(Math.toRadians(yaw)) + this.roll*Math.sin(Math.toRadians(yaw)));
	}

	public float getRoll() {
		return (float) (this.roll*Math.cos(Math.toRadians(yaw)) - this.pitch*Math.sin(Math.toRadians(yaw)));
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getThrust() {
		return this.thrust;
	}

	public float getRadius() {
		return this.radius;
	}

	public DroneCamera getMiddleCamera() {
		return middleCamera;
	}
}
