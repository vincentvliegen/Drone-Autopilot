package simulator.objects;

import simulator.world.World;

public class TwinkleTwinkleLittleStarPolyhedron extends PredefinedPolyhedron{
	
	static double[] point1 = {-0.25, 0.25, -0.25};
	static double[] point2 = {-0.25, 0.25, 0.25};
	static double[] point3 = {-0.25, -0.25, -0.25};
	static double[] point4 = {-0.25, -0.25, 0.25};
	static double[] point5 = {0.25, -0.25, 0.25};
	static double[] point6 = {0.25, 0.25, 0.25};
	static double[] point7 = {0.25, 0.25, -0.25};
	static double[] point8 = {0.25, -0.25, -0.25};
	static double[] point9 = {0, 0, 0.75};
	static double[] point10 = {0, 0.75, 0};
	static double[] point11 = {0, 0, -0.75};
	static double[] point12 = {-0.75, 0, 0};
	static double[] point13 = {0, -0.75, 0};
	static double[] point14 = {0.75, 0, 0};
	
	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5,point6,point7,point8,point9,
		point10,point11,point12,point13,point14};
	static int[][] faces = new int[][]{
		{0,2,11},{11,2,3},{11,3,1},{0,11,1},{1,3,8},{8,3,4},{8,4,5},{1,8,5},
		{0,1,9},{9,1,5},{6,9,5},{6,0,9},{6,7,10},{10,7,2},{0,10,2},{6,10,0},
		{2,7,12},{12,7,4},{2,12,3},{12,4,3},{5,4,13},{13,4,7},{6,13,7},{6,5,13}
	};

	public TwinkleTwinkleLittleStarPolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		for (int[] point: getFaces())
			addTriangleWithRandomColor(vertices[point[0]], vertices[point[1]], vertices[point[2]]);
	}

	public static int[][] getFaces() {
		return faces;
	}
	
	public static double[][] getPoints(){
		return vertices;
	}
}
