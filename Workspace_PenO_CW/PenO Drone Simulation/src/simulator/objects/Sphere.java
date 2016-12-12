package simulator.objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import simulator.world.World;

public class Sphere extends WorldObject{
	
	GL2 gl;
	static float radius = 0.5f;
	int slices = 64;
	int stacks = 64;
	float[] color = new float[3];
	double[] position = new double[3];
	static double[] standardTranslate = {0,0,0};
	
	
	public Sphere(GL2 gl, float[] color, double[] translate, World world){
		super(world);
		this.gl = gl;
		this.color = color;
		this.position = translate;
	};
	
	
	
	public void draw(){
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslated(position[0], position[1], position[2]);
		glut.glutSolidSphere(radius, slices, stacks);	
		gl.glPopMatrix();
	}
	
	public void translateSphere(double[] translate){
		gl.glTranslated(translate[0], translate[1], translate[2]);
	}
	
	public void translateSphere(float[] newTranslate){
		position[0] -= newTranslate[0];
		position[1] -= newTranslate[1];
		position[2] -= newTranslate[2];
	}
	
	public double[] getPosition() {
		return this.position;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	public float[] getColor() {
		return color;
	}
	
	public void setPosition(float[] pos) {
		position[0] = pos[0];
		position[1] = pos[1];
		position[2] = pos[2];
	}
	
}
