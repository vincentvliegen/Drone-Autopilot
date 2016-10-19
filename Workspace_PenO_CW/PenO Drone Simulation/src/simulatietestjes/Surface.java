package simulatietestjes;

import com.jogamp.opengl.GL2;

public class Surface {
	
	GL2 gl;
	float[] p1 = new float[3];
	float[] p2 = new float[3];
	float[] p3 = new float[3];
	float[] p4 = new float[3];
	float[] color = new float[3];

	public Surface(GL2 gl, float[] p1, float[] p2, float[] p3, float[] p4, 	float[] color) {
		this.gl = gl;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.color = color;	
	}
	
	public void drawSurface() {
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3f(p1[0], p1[1], p1[2]);
		gl.glVertex3f(p2[0], p2[1], p2[2]);
		gl.glVertex3f(p3[0], p3[1], p3[2]);
		gl.glVertex3f(p4[0], p4[1], p4[2]);
		gl.glEnd();
	}

}
