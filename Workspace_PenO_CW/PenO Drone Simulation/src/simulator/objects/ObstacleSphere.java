package simulator.objects;

import com.jogamp.opengl.GL2;

public class ObstacleSphere extends Sphere{

	public ObstacleSphere(GL2 gl, float[] color, double[] translate) {
		super(gl, color, translate);
		assert(color[0] == color[1]);
		assert(color[0] == color[2]);
	}
	
	public ObstacleSphere(GL2 gl, double[] position) {
		super(gl, color, position);
	}
	
	
	private static float[] color = {0.5f, 0.5f, 0.5f};



}
