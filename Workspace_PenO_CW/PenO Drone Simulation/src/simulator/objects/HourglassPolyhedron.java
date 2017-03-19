package simulator.objects;

import simulator.world.World;

public class HourglassPolyhedron extends PredefinedPolyhedron {
	
	//upperhalf
	static double[] point1 = { -0.25, 0.25, -0.25 };
	static double[] point2 = { -0.25, 0.25, 0.25 };
	static double[] point3 = { 0, 0, 0 };
	static double[] point4 = { 0.25, 0.25, -0.25};
	static double[] point5 = { 0.25, 0.25, 0.25};
	static double[] point6 = { -0.25, -0.25, -0.25};
	static double[] point7 = { -0.25, -0.25, 0.25};
	static double[] point8 = { 0.25, -0.25, 0.25};
	static double[] point9 = { 0.25, -0.25, -0.25};
	
	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5,point6,point7,point8,point9};
	//TODO ok?
	static int[][] faces = new int[][]{{1,2,0},{3,1,0},{1,3,4},{4,2,1},{0,2,3},{3,2,4},{6,5,2},{7,6,2},{6,5,2},{5,8,2},{8,7,2},{7,5,6},{5,8,7}};

	public HourglassPolyhedron(World world, PolyhedronType type, double[] position) {
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
	
	public static double[][] getPoints() {
		return vertices;
	}
}
