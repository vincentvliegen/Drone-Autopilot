package simulator.objects;

import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.AutopilotFactory;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.DroneAutopilotFactory;
import simulator.camera.DroneCamera;
import simulator.camera.DroneCameraPlace;
import simulator.physics.Movement;
import simulator.world.World;
import simulator.world.WorldParser;

public class SimulationDrone extends WorldObject implements Drone {
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
	private double fakeYaw = 0;

	public SimulationDrone(GL2 gl, float height, float width, float depth, float[] color, double[] translate,
			World world) {
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
		pitch = 0;
		roll = 0;
		yaw = 0;
		this.autopilot = world.getAutopilotFactory().create(this);
		this.radius = (float) Math.sqrt(Math.pow(height / 2, 2) + Math.pow(width / 2, 2) + Math.pow(depth / 2, 2));
	}

	public SimulationDrone(GL2 gl, float height, float width, float depth, float[] color, World world) {
		this(gl, height, width, depth, color, standardTranslate, world);
	}

	public Autopilot getAutopilot() {
		return autopilot;
	}

	public void draw() {
		gl.glEnable(GL.GL_CULL_FACE);

		gl.glPushMatrix(); // store current transform, so we can undo the
							// rotation
		// System.out.println(getTranslate()[0] + " " + getTranslate()[1] + " "
		// + getTranslate()[2]);
		translateDrone(getPosition());
		rotateDrone(-getYaw(), getGlobalRoll(), -getGlobalPitch());
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glBegin(GL2ES3.GL_TRIANGLES);
		gl.glCullFace(GL.GL_BACK);

		// TOP new
		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);

		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);

		// BOTTOM new

		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);

		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);

		// FRONT new
		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);

		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);

		// BACK new

		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);

		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);

		// LEFT new

		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);

		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, -getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, -getDroneWidth() / 2);

		// RIGHT new

		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);

		gl.glVertex3f(getDroneDepth() / 2, getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(-getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);
		gl.glVertex3f(getDroneDepth() / 2, -getDroneHeight() / 2, getDroneWidth() / 2);

		gl.glEnd();
		gl.glPopMatrix();// undo rotation

		gl.glDisable(GL.GL_CULL_FACE);

	}

	public void createRotateMatrix() {
		this.getRotateMatrix().clear();
		this.getRotateMatrix().add((Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))));
		this.getRotateMatrix().add((Math.sin(Math.toRadians(pitch))));
		this.getRotateMatrix().add(-(Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw))));

		this.getRotateMatrix().add(-(Math.sin(Math.toRadians(yaw)) * Math.sin(Math.toRadians(roll))
				- Math.sin(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw))));
		this.getRotateMatrix().add((Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll))));
		this.getRotateMatrix().add(-(Math.sin(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw))
				+ Math.cos(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch))));

		this.getRotateMatrix().add((Math.cos(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw)))
				- Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll)));
		this.getRotateMatrix().add((Math.sin(Math.toRadians(roll)) * Math.cos(pitch)));
		this.getRotateMatrix().add((Math.cos(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw)))
				+ Math.sin(Math.toRadians(roll)) * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
	}

	public void translateDrone(double[] translate) {
		gl.glTranslated(translate[0], translate[1], translate[2]);
		this.translate = translate;
	}

	public void rotateDrone(float yaw, float roll, float pitch) {
		gl.glRotated(roll, 1, 0, 0);
		gl.glRotated(pitch, 0, 0, 1);
		gl.glRotated(yaw, 0, 1, 0);
	}

	public void timeHasPassed(float timePassed) {
		updateRPY(timePassed);
		// Wind rotation stuff
		this.yaw -= getWorld().getWindRotationY() * timePassed;
		this.roll += getWorld().getWindRotationX() * timePassed;
		this.pitch -= getWorld().getWindRotationZ() * timePassed;
		
//		 System.out.println("--------------");
//		 System.out.println("global pitch " + this.pitch);
//		 System.out.println("global yaw " + this.yaw);
//		 System.out.println("global roll " + this.roll);
//		 System.out.println("current pitch " + getPitch());
//		 System.out.println("current roll " + getRoll());
//		 System.out.println("pitchRate " + this.pitchRate);
//		 System.out.println("yawRate " + this.yawRate);
//		 System.out.println("rollRate " + this.rollRate);
//		 System.out.println("--------------");
		 
		getLeftDroneCamera().updateDroneCamera();
		getRightDroneCamera().updateDroneCamera();
		getMiddleCamera().updateDroneCamera();

		this.autopilot.timeHasPassed();
	}

	private void updateRPY(float timePassed) {
		double[][] currentAxis = new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
//		yawRate = 0;
//		pitchRate = 720;
//		rollRate = 0;
		double cosAngleY = Math.cos(Math.toRadians(yaw));
		double sinAngleY = Math.sin(Math.toRadians(-yaw));
		double cosAngleP = Math.cos(Math.toRadians(pitch));
		double sinAngleP = Math.sin(Math.toRadians(-pitch));
		double cosAngleR = Math.cos(Math.toRadians(roll));
		double sinAngleR = Math.sin(Math.toRadians(roll));

		// angles
		double[] yawMat = currentAxis[1];
		currentAxis[0] = rotate(currentAxis[0], yawMat, cosAngleY, sinAngleY);
		currentAxis[2] = rotate(currentAxis[2], yawMat, cosAngleY, sinAngleY);

		double[] pitchMat = currentAxis[2];
		currentAxis[0] = rotate(currentAxis[0], pitchMat, cosAngleP, sinAngleP);
		currentAxis[1] = rotate(currentAxis[1], pitchMat, cosAngleP, sinAngleP);

		double[] rollMat = currentAxis[0];
		currentAxis[1] = rotate(currentAxis[1], rollMat, cosAngleR, sinAngleR);
		currentAxis[2] = rotate(currentAxis[2], rollMat, cosAngleR, sinAngleR);

//		for (double[] x: currentAxis)
//			System.out.println(Arrays.toString(x));
		
		double newPitch = (pitchRate*Math.cos(Math.toRadians(yaw)) + rollRate*Math.sin(Math.toRadians(yaw))) * timePassed;
		double newRoll = (rollRate*Math.cos(Math.toRadians(yaw)) - pitchRate * Math.sin(Math.toRadians(yaw))) * timePassed;
		double newYaw = yawRate * timePassed;

		double ncosAngleY = Math.cos(Math.toRadians(newYaw));
		double nsinAngleY = Math.sin(Math.toRadians(-newYaw));
		double ncosAngleP = Math.cos(Math.toRadians(newPitch));
		double nsinAngleP = Math.sin(Math.toRadians(-newPitch));
		double ncosAngleR = Math.cos(Math.toRadians(newRoll));
		double nsinAngleR = Math.sin(Math.toRadians(newRoll));

		// rates
		double[] nyawMat = currentAxis[1];
		currentAxis[0] = rotate(currentAxis[0], nyawMat, ncosAngleY, nsinAngleY);
		currentAxis[2] = rotate(currentAxis[2], nyawMat, ncosAngleY, nsinAngleY);

		double[] npitchMat = currentAxis[2];
		currentAxis[0] = rotate(currentAxis[0], npitchMat, ncosAngleP, nsinAngleP);
		currentAxis[1] = rotate(currentAxis[1], npitchMat, ncosAngleP, nsinAngleP);

		double[] nrollMat = currentAxis[0];
		currentAxis[1] = rotate(currentAxis[1], nrollMat, ncosAngleR, nsinAngleR);
		currentAxis[2] = rotate(currentAxis[2], nrollMat, ncosAngleR, nsinAngleR);
				
		//Als pitch 90 is, geen opl???
//		for (double[] x: currentAxis)
//			System.out.println(Arrays.toString(x));
//		System.out.println("-----");
		
		pitch = (float) Math.toDegrees(Math.asin(-currentAxis[0][1]));
		float yawCorr = (float) ( Math.signum(currentAxis[0][0]/Math.cos(Math.toRadians(pitch)))*Math.min(Math.abs(currentAxis[0][0]/Math.cos(Math.toRadians(pitch))), 1)); 
		if (Math.cos(Math.toRadians(pitch)) != 0)
			yaw = (float) Math.toDegrees(Math.acos(yawCorr));
		else
			yaw = Float.NaN;
		yaw = (float)Math.signum(currentAxis[0][2]/Math.cos(Math.toRadians(pitch))) * yaw;
		
		float rollCorr = (float) ( Math.signum(currentAxis[1][1]/Math.cos(Math.toRadians(pitch)))*Math.min(Math.abs(currentAxis[1][1]/Math.cos(Math.toRadians(pitch))), 1));
		if (Math.cos(Math.toRadians(pitch)) != 0)
			roll = (float) Math.toDegrees(Math.acos(rollCorr));
		else
			roll = Float.NaN;
		roll *= Math.signum(currentAxis[2][1]/Math.cos(Math.toRadians(pitch)));
	}

	private double[] rotate(double[] vec, double[] axis, double cos, double sin) {
		double[] rotatedVector = sum(timesScalar(vec, cos),
				sum(timesScalar(crossProduct(axis, vec), sin), timesScalar(axis, dotProduct(axis, vec) * (1 - cos))));
		return rotatedVector;
	}

	private double[] timesScalar(double[] vec, double scalar) {
		return new double[] { vec[0] * scalar, vec[1] * scalar, vec[2] * scalar };
	}

	private double[] sum(double[] vec1, double[] vec2) {
		return new double[] { vec1[0] + vec2[0], vec1[1] + vec2[1], vec2[2] + vec1[2] };
	}

	public static double dotProduct(double[] vector1, double[] vector2) {
		return vector1[0] * vector2[0] + vector1[1] * vector2[1] + vector1[2] * vector2[2];
	}

	public static double[] crossProduct(double[] vector1, double[] vector2) {
		double x = vector1[1] * vector2[2] - vector1[2] * vector2[1];
		double y = vector1[2] * vector2[0] - vector1[0] * vector2[2];
		double z = vector1[0] * vector2[1] - vector1[1] * vector2[0];
		return new double[] { x, y, z };
	}

	private void checkYawPitchRoll() {
		if (Math.abs(pitch) > 90)
			pitch = Math.signum(pitch) * 180 - pitch;
		if (Math.abs(yaw) > 360)
			yaw -= Math.signum(yaw) * 360;
		if (Math.abs(roll) > 180)
			roll -= Math.signum(roll) * 360;
	}

	public void createInverseRotate() {
		inverseRotateMatrix.clear();
		inverseRotateMatrix.add((Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))));
		inverseRotateMatrix.add(-(Math.sin(Math.toRadians(yaw)) * Math.sin(Math.toRadians(roll)))
				- (Math.sin(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw))));
		inverseRotateMatrix.add((Math.cos(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw))
				- Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll))));

		inverseRotateMatrix.add((Math.sin(Math.toRadians(pitch))));
		inverseRotateMatrix.add((Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll))));
		inverseRotateMatrix.add((Math.sin(Math.toRadians(roll)) * Math.cos(Math.toRadians(pitch))));

		inverseRotateMatrix.add(-(Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw))));
		inverseRotateMatrix
				.add((Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(roll))
						- Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(roll))));
		inverseRotateMatrix.add((Math.cos(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw))
				+ Math.sin(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll))));
	}

	private void generateDroneCameras() {

		float commonY = 0;
		float commonX = 0;

		// left
		float leftZ = -getCameraSeparation() / 2;
		leftCamera = new DroneCamera(commonX, commonY, leftZ, commonX + 100, commonY, leftZ, 0, 1, 0, getWorld(), this,
				DroneCameraPlace.LEFT);

		// right
		float rightZ = getCameraSeparation() / 2;
		rightCamera = new DroneCamera(commonX, commonY, rightZ, commonX + 100, commonY, rightZ, 0, 1, 0, getWorld(),
				this, DroneCameraPlace.RIGHT);

		// middle
		middleCamera = new DroneCamera(-2, 0.5f, 0, -1 + 100, 0, 0, 0, 1, 0, getWorld(), this, DroneCameraPlace.MIDDLE);

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
		if (value != value) {
			throw new IllegalArgumentException("NaN thrust????");
		} else if (value > getMaxThrust())
			this.thrust = getMaxThrust();
		else
			this.thrust = value;
	}

	@Override
	public void setPitchRate(float value) {
		// System.out.println("Wind");
		// System.out.println("windx = " +
		// getWorld().getWindSpeedX()*this.getDrag());
		// System.out.println("windy = " +
		// getWorld().getWindSpeedY()*this.getDrag());
		// System.out.println("windz = " +
		// getWorld().getWindSpeedZ()*this.getDrag());
		// System.out.println("wind roll = " + getWorld().getWindRotationX());
		// System.out.println("wind yaw = " + getWorld().getWindRotationY());
		// System.out.println("wind pitch = " + getWorld().getWindRotationZ());
		if (value != value) {
			throw new IllegalArgumentException("NaN pitchrate????");
		} else if (Math.abs(value) > getMaxPitchRate())
			this.pitchRate = getMaxPitchRate() * Math.signum(value);
		else
			this.pitchRate = value;
	}

	@Override
	public void setRollRate(float value) {
		if (value != value) {
			throw new IllegalArgumentException("NaN rollrate????");
		} else if (Math.abs(value) > getMaxRollRate())
			this.rollRate = getMaxRollRate() * Math.signum(value);
		else
			this.rollRate = value;
	}

	@Override
	public void setYawRate(float value) {
		if (value != value) {
			throw new IllegalArgumentException("NaN yawrate????");
		} else if (Math.abs(value) > getMaxYawRate())
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
		return (float) (this.pitch * Math.cos(Math.toRadians(yaw)) + this.roll * Math.sin(Math.toRadians(yaw)));
	}

	public float getRoll() {
		return (float) (this.roll * Math.cos(Math.toRadians(yaw)) - this.pitch * Math.sin(Math.toRadians(yaw)));
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

	@Override
	public float getX() {
		// float xCo = (getLeftDroneCamera().getEyeX() +
		// getRightDroneCamera().getEyeX())/2;
		// return xCo;
		return (float) this.getMovement().getCurrentPosition()[0];
	}

	@Override
	public float getY() {
		// float yCo = (getLeftDroneCamera().getEyeY() +
		// getRightDroneCamera().getEyeY())/2;
		// return yCo;
		return (float) this.getMovement().getCurrentPosition()[1];
	}

	@Override
	public float getZ() {
		// float zCo = (getLeftDroneCamera().getEyeZ() +
		// getRightDroneCamera().getEyeZ())/2;
		// return zCo;
		return (float) this.getMovement().getCurrentPosition()[2];
	}

	@Override
	public float getHeading() {
		float newYaw = 90 + yaw;
		if (Math.abs(newYaw) > 180)
			newYaw -= Math.signum(newYaw) * 360;
		return newYaw;
	}

	public List<Double> getInverseRotateMatrix() {
		return inverseRotateMatrix;
	}

	/**
	 * May only be used for testing purposes!
	 */
	public void setAutopilot(Autopilot autopilot) {
		this.autopilot = autopilot;
	}

	public void fakeTimeHasPassed() {
		if (fakeYaw == 360) {
			pitch = 90;
			yaw = 0;
			roll = 0;
			translate = new double[]{1, 1, 0};
			fakeYaw += 1;
		} else if (fakeYaw == 361) {
			pitch = -90;
			yaw = 0;
			roll = 0;
			translate = new double[]{1, -1, 0};	
		} else if (fakeYaw < 360) {
			pitch = 0;
			yaw = (float) -fakeYaw;
			roll = 0;
			translate = new double[]{1-0.8*Math.cos(Math.toRadians(fakeYaw)), 0, 0.8*Math.sin(Math.toRadians(fakeYaw))};
			fakeYaw += 5;
		}
		autopilot.timeHasPassed();
	}

}
