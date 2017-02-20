package simulator.objects;

import simulator.world.World;

public class SomeFigure extends Polyhedron{



	public SomeFigure(World world, PolyhedronType type) {
		super(world, type);
	}


	protected void defineTriangles() {
		double[] point1 = { -0.2, 0, -0.2 };
		double[] point2 = { -0.2, 0, 0.2 };
		double[] point3 = { 0.2, 0, 0 };
		double[] point4 = { 0, 0.2, 0 }; // upper


		addTriangleWithRandomColor(point1, point2, point3);
		addTriangleWithRandomColor(point1, point2, point4);
		addTriangleWithRandomColor(point1, point3, point4);
		addTriangleWithRandomColor(point2, point3, point4);
	}




}
