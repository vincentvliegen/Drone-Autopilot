package simulator.objects;

import simulator.world.World;

public abstract class PredefinedPolyhedron extends Polyhedron{

	public PredefinedPolyhedron(World world, PolyhedronType type, double[] position, double[][] vertices) {
		super(world, type, vertices);
		defineTriangles();
		translatePolyhedronOver(position);
	}
	
	protected abstract void defineTriangles();
	


}
