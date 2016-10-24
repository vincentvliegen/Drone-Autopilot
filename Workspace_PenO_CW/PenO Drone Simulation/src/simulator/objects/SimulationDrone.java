package simulator.objects;

import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

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
	float pitch = 0;
	float roll = 0;
	float thrust = 0;
	World world;
	private Movement movement;
	private static float weight = 10f;
	private float pitchRate = 0;
	private float rollRate = 0;
	private float yawRate = 0;
	
	
	public SimulationDrone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings,  float[] color, double[] translate, World world){
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.nsides = nsides;
		this.rings = rings;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
		this.physics = new Physics(this, 10f);
		//this.setThrust(10f*9.81f);
		this.world = world;
		this.movement = new Movement(this);
	}
	
	//TODO afmetingen (voor collision detection)
//	DroneCamera leftCamera = new DroneCamera(this);
//	DroneCamera rightCamera = new DroneCamera(this);
//	
	
	
	public SimulationDrone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings, float[] color, World world){
		this(gl, innerRadius, outerRadius, nsides, rings, color, standardTranslate, world);
	}

	public void drawDrone(){
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslated(translate[0], translate[1], translate[2]);
		GLUT glut = new GLUT();
		glut.glutSolidTorus(innerRadius, outerRadius, nsides, rings);
	}
	
	public void translateDrone(double[] translate){
		gl.glTranslated(translate[0], translate[1], translate[2]);
	}
	
	public Physics getPhysics() {
		return this.physics;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getRoll() {
		return this.roll;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Camera getRightCamera() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getWeight() {
		return SimulationDrone.weight;
	}

	@Override
	public float getGravity() {
		return 9.81f;
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
		return world.delta;
	}

	@Override
	public void setPitchRate(float value) {
		// TODO Check if not greater than max!
		this.pitchRate = value;
	}

	@Override
	public void setRollRate(float value) {
		// TODO Check if not greater than max!
		this.rollRate = value;
	}

	@Override
	public void setYawRate(float value) {
		// TODO Check if not greater than max!
		this.yawRate = value;
	}
	
	
	public World getWorld() {
		return this.world;
	}
	
	public Movement getMovement() {
		return this.movement;
	}
	
	//TODO Implement movement according to delta -> Check every 0.1s! 
	//	   Else unlimited small movements? => Might still work, but annoying for Autopilot => Force interval
	//	   
	
}
