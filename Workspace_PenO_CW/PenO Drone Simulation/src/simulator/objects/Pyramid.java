package simulator.objects;

import simulator.world.World;

public class Pyramid extends PredefinedPolyhedron{


	static double[] point1 = { -0.5, 0, -0.5 };
	static double[] point2 = { -0.5, 0, 0.5 };
	static double[] point3 = { 0.5, 0, 0 };
	static double[] point4 = { 0, 0.5, 0 }; // upper
	static double[][] vertices = new double[][]{point1,point2,point3,point4};
	static int[][] faces = new int[][]{
		{1,2,0},{3,1,0},{2,3,0},{3,2,1}
	};

	
	public Pyramid(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}


	@Override
	protected void defineTriangles() {
		for (int[] point: getFaces())
			addTriangleWithRandomColor(vertices[point[0]], vertices[point[1]], vertices[point[2]]);
	}

	public static int[][] getFaces(){
		return faces;
	}

	public static double[][] getPoints(){
		return vertices;
	}
}
