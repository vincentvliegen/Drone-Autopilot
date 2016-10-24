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
	// Implement rates for turning and maxTurnRate
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
	
	//TODO afstand tussen camera's
	//TODO afmetingen (voor collision detection)
	DroneCamera leftCamera = new DroneCamera(this);
	DroneCamera rightCamera = new DroneCamera(this);
	
	
	
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
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getGravity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDrag() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxThrust() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxPitchRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxRollRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxYawRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCurrentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPitchRate(float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRollRate(float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setYawRate(float value) {
		// TODO Auto-generated method stub
		
	}
	
	
	public World getWorld() {
		return this.world;
	}
	
	public Movement getMovement() {
		return this.movement;
	}
	
}
