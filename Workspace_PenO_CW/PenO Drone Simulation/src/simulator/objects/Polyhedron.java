package simulator.objects;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import simulator.world.World;

public class Polyhedron extends WorldObject {

	GL2 gl;

	// TODO op deze manier qua constructor enz.?
	public Polyhedron(World world, ArrayList<Triangle> triangles) {
		super(world);
		this.gl = getWorld().getGL().getGL2();
	}

	public GL2 getGl() {
		return gl;
	}

	@Override
	public void draw() {
		// TODO forall triangles: draw triangle (?)

	}

	@Override
	public double[] getPosition() {
		// TODO hoe?
		return null;

	}

	@Override
	public float getRadius() {
		// TODO hoe?
		return 0;
	}

}
