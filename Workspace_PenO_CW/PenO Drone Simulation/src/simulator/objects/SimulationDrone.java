package simulator.objects;

import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

import java.util.ArrayList;
import java.util.List;

import DroneAutopilot.Autopilot;
import DroneAutopilot.AutopilotFactory;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import simulator.camera.DroneCamera;
import simulator.physics.Movement;
import simulator.physics.Physics;
import simulator.world.World;

public class SimulationDrone implements Drone {
	GL2 gl;
	float innerRadius;
	float outerRadius;
	int nsides;
	int rings;
	float[] color = new float[3];
	double[] translate = new double[3];
	static double[] standardTranslate = {0,0,0};
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
	private Autopilot autopilot;
	
	
	public SimulationDrone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings,  float[] color, double[] translate, World world){
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.nsides = nsides;
		this.rings = rings;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
		this.physics = new Physics(this, 10f);
		this.setThrust(10f*9.81f);
		this.world = world;
		this.movement = new Movement(this);
		generateDroneCameras();
		AutopilotFactory ap = new AutopilotFactory();
		this.autopilot = ap.create(this);
	}
	
	private void createRTrans() {
		rTrans.clear();
		rTrans.add(Math.cos(pitch)*Math.cos(yaw) + Math.sin(pitch)*Math.sin(roll)*Math.sin(yaw));
		rTrans.add(Math.cos(roll)*Math.sin(pitch));
		rTrans.add(Math.cos(yaw)*Math.sin(pitch)*Math.sin(roll) - Math.cos(pitch)*Math.sin(yaw));
		rTrans.add(Math.cos(pitch)*Math.sin(roll)*Math.sin(yaw) - Math.cos(yaw)*Math.sin(pitch));
		rTrans.add(Math.cos(pitch)*Math.cos(roll));
		rTrans.add(Math.sin(pitch)*Math.sin(yaw) + Math.cos(pitch)*Math.cos(yaw)*Math.sin(roll));
		rTrans.add(Math.cos(roll)*Math.sin(yaw));
		rTrans.add(-Math.sin(roll));
		rTrans.add(Math.cos(roll)*Math.cos(yaw));
	}

	//TODO afmetingen (voor collision detection)
	
	
	public SimulationDrone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings, float[] color, World world){
		this(gl, innerRadius, outerRadius, nsides, rings, color, standardTranslate, world);
	}

	public void drawDrone(){
		
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslated(translate[0], translate[1], translate[2]);
		System.out.println("drone" + translate[0] + "  " + translate[1] + "  " + translate[2]);
		glut.glutSolidTorus(innerRadius, outerRadius, nsides, rings);
		gl.glPopMatrix();
	}
	
	public void translateDrone(double[] translate){
		gl.glTranslated(translate[0], translate[1], translate[2]);
	}
	
	public double[] getTranslate(){
		return this.translate;
	}
	
	public Physics getPhysics() {
		return this.physics;
	}
	
	// TODO: Calculate current angles?
	// TODO: Check
	public float getPitch() {
		return (float) (this.pitch * Math.cos(yaw) + this.roll * Math.sin(yaw));
	}
	
	// TODO: Calculate current angles?
	// TODO: Check
	public float getRoll() {
		return (float) (this.roll * Math.cos(yaw) - this.pitch * Math.sin(yaw));
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getThrust() {
		return this.thrust;
	}
	
	public void setThrust(float value) {
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
		return 3f*SimulationDrone.weight*getGravity();
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
		if(value > getMaxPitchRate())
			this.pitchRate = getMaxPitchRate();
		else 
			this.pitchRate = value;	
	}

	@Override
	public void setRollRate(float value) {
		if(value > getMaxRollRate())
			this.rollRate = getMaxRollRate();
		else 
			this.rollRate = value;	
	}

	@Override
	public void setYawRate(float value) {
		if(value > getMaxYawRate())
			this.yawRate = getMaxYawRate();
		else 
			this.yawRate = value;	
	}
	
	public float getDronedepth() {
		// This will change when the drone changes into an other shape. 
		return outerRadius;
	}

	public float getDroneWidth() {
		// This will change when the drone changes into an other shape. 
		return outerRadius;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public Movement getMovement() {
		return this.movement;
	}
	
	private void generateDroneCameras() {
		
		//TODO
		//lookat moet recht vooruit zijn
		//dus X moet zelfde zijn, Y ook, Z iets verder

		float commonY = 0;
		
		//left
		float leftX = -getDroneWidth()/2;
		float leftZ = getDronedepth()/2;
		leftCamera = new DroneCamera(leftX, commonY, leftZ, leftX, commonY, leftZ+100, 0, 1, 0, this);	
		
		//right
		float rightX = getDroneWidth()/2;
		float rightZ = getDronedepth()/2;
		rightCamera = new DroneCamera(rightX, commonY, rightZ, rightX, commonY, rightZ+100, 0, 1, 0, this);
		
		//add to list in world
		getWorld().addDroneCamera(leftCamera);
		getWorld().addDroneCamera(rightCamera);
	}
	
	public void timeHasPassed(float timePassed) {
		this.getMovement().calculateMovement(timePassed);
		this.yaw += this.yawRate * timePassed;
		
		double rollPass = this.rollRate * timePassed;
		createRTrans();
		this.roll += rollPass*rTrans.get(0);
		this.yaw  += rollPass*rTrans.get(3);
		this.pitch += rollPass*rTrans.get(6);
		
		
		double pitchPass = this.pitchRate * timePassed;
		createRTrans();
		this.roll += pitchPass*rTrans.get(2);
		this.yaw += pitchPass*rTrans.get(5);
		this.pitch += pitchPass*rTrans.get(8);
		this.autopilot.timeHasPassed();
		try {
			wait();
		} catch (InterruptedException e) {

		}
	}
}
