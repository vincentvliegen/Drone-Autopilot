package simulator.objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import simulator.camera.DroneCamera;
import simulator.physics.Physics;

public class Drone {
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
	
	
	public Drone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings,  float[] color, double[] translate){
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.nsides = nsides;
		this.rings = rings;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
		this.physics = new Physics(this, 10f);
		this.setThrust(10f*9.81f);
		this.pitch = 45f;
	}
	
	DroneCamera leftCamera = new DroneCamera(this);
	DroneCamera rightCamera = new DroneCamera(this);
	
	
	
	public Drone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings, float[] color){
		this(gl, innerRadius, outerRadius, nsides, rings, color, standardTranslate);
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
	
	public void translateDrone(float[] newTranslate){
		translate[0] += newTranslate[0];
		translate[1] += newTranslate[1];
		translate[2] += newTranslate[2];
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
	
	
	
	
}
