package simulator.objects;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Drone {
	
	GL2 gl;
	float innerRadius;
	float outerRadius;
	int nsides;
	int rings;
	float[] color = new float[3];
	double[] translate = new double[3];
	static double[] standardTranslate = {0,0,0};
	
	public Drone(GL2 gl, float innerRadius, float outerRadius, int nsides, int rings,  float[] color, double[] translate){
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.nsides = nsides;
		this.rings = rings;
		this.gl = gl;
		this.color = color;
		this.translate = translate;
	}
	
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
}
