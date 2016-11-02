package simulator.objects;

import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

import java.util.ArrayList;
import java.util.List;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.DroneAutopilotFactory;

import com.jogamp.opengl.GL2;

import simulator.camera.DroneCamera;
import simulator.camera.DroneCameraPlace;
import simulator.physics.Movement;
import simulator.physics.Physics;
import simulator.world.World;

public class SimulationDrone implements Drone {
	GL2 gl;
	float height;
	float width;
	float depth;
	float[] color = new float[3];
	double[] translate = new double[3];
	static double[] standardTranslate = { 0, 0, 0 };
	Physics physics;
	private float pitch = 0;
	private float roll = 0;
	private float thrust = 0;
	private float yaw = 0;
	World world;
	private Movement movement;
	private static float weight = 10f;
	private float pitchRate = 0;
	private float rollRate = 0;
	private float yawRate = 0;
	public DroneCamera leftCamera;
	public DroneCamera rightCamera;
	List<Double> rTrans = new ArrayList<>();
	private DroneAutopilot autopilot;

	public SimulationDrone(GL2 gl, float height, float width, float depth, float[] color, double[] translate, World world) {
		this.height = height;
		this.width = width;
		this.depth = depth;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
		this.physics = new Physics(this, 10f);
		this.world = world;
		this.movement = new Movement(this);
		generateDroneCameras();
		DroneAutopilotFactory ap = new DroneAutopilotFactory();
		this.autopilot = ap.create(this);
	}

	private void createRTrans() {
		rTrans.clear();
		rTrans.add(Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))
				+ Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw)));
		rTrans.add(Math.cos(Math.toRadians(roll)) * Math.sin(Math.toRadians(pitch)));
		rTrans.add(Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll))
				- Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
		rTrans.add(Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw))
				- Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(pitch)));
		rTrans.add(Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll)));
		rTrans.add(Math.sin(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw))
				+ Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)) * Math.sin(Math.toRadians(roll)));
		rTrans.add(Math.cos(Math.toRadians(roll)) * Math.sin(Math.toRadians(yaw)));
		rTrans.add(-Math.sin(Math.toRadians(roll)));
		rTrans.add(Math.cos(Math.toRadians(roll)) * Math.cos(Math.toRadians(yaw)));
	}

	// TODO afmetingen (voor collision detection)

	public SimulationDrone(GL2 gl, float height, float width, float depth, float[] color, World world) {
		this(gl, height, width, depth, color, standardTranslate, world);
	}

	public void drawDrone() {
		
		gl.glPushMatrix(); // store current transform, so we can undo the rotation
		System.out.println(getTranslate()[0] + "  " + getTranslate()[1] + "  " + getTranslate()[2]);
		translateDrone(translate);
		System.out.println("Roll " + getRoll());
		System.out.println("Pitch " + getPitch());
		System.out.println("Yaw " + getYaw());
		gl.glRotated(getRoll(), 0, 0, 1);
		gl.glRotated(getPitch(), 1, 0, 0);
		gl.glRotated(getYaw(), 0, 1, 0);
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glBegin(gl.GL_QUADS);
		
		//Top
		gl.glVertex3f(getDroneWidth()/2, getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);

		//Bottom
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);

		//Front
		gl.glVertex3f(getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);

		//back
		gl.glVertex3f(getDroneWidth()/2,  getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2,  getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);

		//Left
		gl.glVertex3f(-getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2,  getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(-getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);
		
		//Right
		
		gl.glVertex3f(getDroneWidth()/2,  getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2,  getDroneHeight()/2, -getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, getDroneDepth()/2);
		gl.glVertex3f(getDroneWidth()/2, -getDroneHeight()/2, -getDroneDepth()/2);
		
		gl.glEnd();
		gl.glPopMatrix();// undo rotation
		
	}

	public void translateDrone(double[] translate) {
		gl.glTranslated(translate[0], translate[1], translate[2]);
		this.translate = translate;
	}

	public double[] getTranslate() {
		return this.translate;
	}

	public Physics getPhysics() {
		return this.physics;
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

	public void setThrust(float value) {
		if (value > getMaxThrust())
			this.thrust = getMaxThrust();
		else
			this.thrust = value;
	}

	@Override
	public float getCameraSeparation() {
		return 0.5f;
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
		return SimulationDrone.weight;
	}

	@Override
	public float getGravity() {
		return -9.81f;
	}

	@Override
	public float getDrag() {
		return 0.1f;
	}

	@Override
	public float getMaxThrust() {
		return -3f * SimulationDrone.weight * getGravity();
	}

	@Override
	public float getMaxPitchRate() {
		return 5f;
	}

	@Override
	public float getMaxRollRate() {
		return 5f;
	}

	@Override
	public float getMaxYawRate() {
		return 5f;
	}

	@Override
	public float getCurrentTime() {
		return world.getCurrentTime();
	}

	@Override
	public void setPitchRate(float value) {
		if (value > getMaxPitchRate())
			this.pitchRate = getMaxPitchRate();
		else
			this.pitchRate = value;
	}

	@Override
	public void setRollRate(float value) {
		if (value > getMaxRollRate())
			this.rollRate = getMaxRollRate();
		else
			this.rollRate = value;
	}

	@Override
	public void setYawRate(float value) {
		if (value > getMaxYawRate())
			this.yawRate = getMaxYawRate();
		else
			this.yawRate = value;
	}

	public float getDroneDepth() {
		return depth;
	}

	public float getDroneWidth() {
		return width;
	}
	
	public float getDroneHeight(){
		return height;
	}

	public World getWorld() {
		return this.world;
	}

	public Movement getMovement() {
		return this.movement;
	}

	private void generateDroneCameras() {

		// TODO
		// lookat moet recht vooruit zijn
		// dus X moet zelfde zijn, Y ook, Z iets verder

		float commonY = 0;

		// left
		float leftX = -getDroneWidth() / 2;
		float leftZ = -getDroneDepth() / 2;
		leftCamera = new DroneCamera(leftX, commonY, leftZ, leftX, commonY, leftZ - 100, 0, 1, 0, this, DroneCameraPlace.LEFT);

		// right
		float rightX = getDroneWidth() / 2;
		float rightZ = -getDroneDepth() / 2;
		rightCamera = new DroneCamera(rightX, commonY, rightZ, rightX, commonY, rightZ - 100, 0, 1, 0, this, DroneCameraPlace.RIGHT);

		// add to list in world
		getWorld().addDroneCamera(leftCamera);
		getWorld().addDroneCamera(rightCamera);
	}

	public void timeHasPassed(float timePassed) {
		this.getMovement().calculateMovement(timePassed);
		this.yaw += this.yawRate * timePassed;

		double rollPass = this.rollRate * timePassed;
		createRTrans();
		this.roll += rollPass * rTrans.get(0);
		this.yaw += rollPass * rTrans.get(3);
		this.pitch += rollPass * rTrans.get(6);

		double pitchPass = this.pitchRate * timePassed;
		createRTrans();
		this.roll += pitchPass * rTrans.get(2);
		this.yaw += pitchPass * rTrans.get(5);
		this.pitch += pitchPass * rTrans.get(8);
		this.autopilot.timeHasPassed();
	}
}
