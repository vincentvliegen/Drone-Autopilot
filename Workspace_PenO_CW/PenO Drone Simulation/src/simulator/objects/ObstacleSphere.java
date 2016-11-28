package simulator.objects;

import com.jogamp.opengl.GL2;

public class ObstacleSphere extends Sphere{

	public ObstacleSphere(GL2 gl, float radius, int slices, int stacks, float[] color, double[] translate) {
		super(gl, radius, color, translate);
		assert(color[0] == color[1]);
		assert(color[0] == color[2]);
	}

}
