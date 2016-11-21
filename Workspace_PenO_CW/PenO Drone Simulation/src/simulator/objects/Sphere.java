package simulator.objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Sphere implements WorldObject{
	
	GL2 gl;
	float radius;
	int slices;
	int stacks;
	float[] color = new float[3];
	double[] translate = new double[3];
	static double[] standardTranslate = {0,0,0};
	
	
	public Sphere(GL2 gl, float radius, int slices, int stacks, float[] color, double[] translate){
		this.gl = gl;
		this.radius = radius;
		this.slices = slices;
		this.stacks = stacks;
		this.color = color;
		this.translate = translate;
	};
	
	public Sphere(GL2 gl, float radius, int slices, int stacks, float[] color){
		this(gl, radius, slices, stacks, color, standardTranslate);
	}
	
	public void draw(){
		GLUT glut = new GLUT();
		gl.glPushMatrix();
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslated(translate[0], translate[1], translate[2]);
		glut.glutSolidSphere(radius, slices, stacks);	
		gl.glPopMatrix();
	}
	
	public void translateSphere(double[] translate){
		gl.glTranslated(translate[0], translate[1], translate[2]);
	}
	
	public void translateSphere(float[] newTranslate){
		translate[0] -= newTranslate[0];
		translate[1] -= newTranslate[1];
		translate[2] -= newTranslate[2];
	}
	
	public double[] getTranslate() {
		return this.translate;
	}
	
	public float getRadius() {
		return this.radius;
	}
}
