package simulator.objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Sphere {
	
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
	
	public void drawSphere(){
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslated(translate[0], translate[1], translate[2]);
		GLUT glut = new GLUT();
		glut.glutSolidSphere(radius, slices, stacks);	
	}
	
	public void translateSphere(double[] translate){
		gl.glTranslated(translate[0], translate[1], translate[2]);
	}
	
}
