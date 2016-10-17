package simulatietestjes;

import java.util.Vector;

import com.jogamp.opengl.GL2;

public class Surface {

	public Surface(GL2 gl, Vector<Float> p1, Vector<Float> p2, Vector<Float> p3, Vector<Float> p4, 	Vector<Float> color) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.color = color;
		this.gl = gl;

	}
	
	
	Vector<Float> p1;
	Vector<Float> p2;
	Vector<Float> p3;
	Vector<Float> p4;
	
	GL2 gl;

	Vector<Float> color = new Vector<Float>(3);
	public void drawSurface() {
		gl.glColor3f(color.elementAt(0), color.elementAt(1), color.elementAt(2));
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3f(p1.elementAt(0), p1.elementAt(1), p1.elementAt(2));
		gl.glVertex3f(p2.elementAt(0), p2.elementAt(1), p2.elementAt(2));
		gl.glVertex3f(p3.elementAt(0), p3.elementAt(1), p3.elementAt(2));
		gl.glVertex3f(p4.elementAt(0), p4.elementAt(1), p4.elementAt(2));
		gl.glEnd();

	}




}
