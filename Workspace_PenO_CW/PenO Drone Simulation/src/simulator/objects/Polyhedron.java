package simulator.objects;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import simulator.world.World;

public abstract class Polyhedron extends WorldObject {

	GL2 gl;
	protected abstract ArrayList<Triangle> getTriangles();

	// TODO op deze manier qua constructor enz.?
	public Polyhedron(World world) {
		super(world);
		this.gl = getWorld().getGL().getGL2();
	}

	public GL2 getGl() {
		return gl;
	}


	@Override
	public void draw() {
		for (Triangle triangle : getTriangles())
			triangle.draw();

	}

	/*
	 * TODO hoe met posities van driehoeken? want je bepaalt driehoeken die dan
	 * samen een polyhedron vormen maar als je een polyhedron op een bepaalde
	 * plaats wilt, moet je alle driehoeken verschuiven zodat de polyhedron op
	 * die plaats komt evt definieren rond (0,0,0) en dan verschuiven voor een
	 * bepaalde positie? (dan los je meteen het probleem op van getPosition()
	 */

	@Override
	public double[] getPosition() {
		// TODO hoe?
		return new double[] {0,0,0};

	}

	@Override
	public float getRadius() {
		// TODO hoe?
		return 0;
	}

}
