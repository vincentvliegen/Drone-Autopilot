package simulator.objects;

import simulator.world.World;

public class SomeFigure extends PredefinedPolyhedron{


	static double[] point1 = { -0.2, 0, -0.2 };
	static double[] point2 = { -0.2, 0, 0.2 };
	static double[] point3 = { 0.2, 0, 0 };
	static double[] point4 = { 0, 0.2, 0 }; // upper
	static double[][] vertices = new double[][]{point1,point2,point3,point4};

	
	public SomeFigure(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}


	@Override
	protected void defineTriangles() {

		addTriangleWithRandomColor(point1, point3, point2);
		addTriangleWithRandomColor(point1, point2, point4);
		addTriangleWithRandomColor(point1, point4, point3);
		addTriangleWithRandomColor(point2, point3, point4);
		
	}




}
