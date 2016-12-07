package simulator.objects;

import com.jogamp.opengl.GL2;

import simulator.world.World;

public class ObstacleSphere extends Sphere{

	public ObstacleSphere(GL2 gl, float[] color, double[] translate, World world) {
		super(gl, color, translate, world);
		assert(color[0] == color[1]);
		assert(color[0] == color[2]);
	}
	
	public ObstacleSphere(GL2 gl, double[] position, World world) {
		super(gl, color, position, world);
	}
	
	
	private static float[] color = {0.5f, 0.5f, 0.5f};



}
