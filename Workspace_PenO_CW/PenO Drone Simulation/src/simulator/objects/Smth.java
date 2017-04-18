package simulator.objects;

import simulator.world.World;

public class Smth extends PredefinedPolyhedron {

	private static double[] point1 = new double[]{0,0,0};
	private static double[] point2 = new double[]{0,0,1};
	private static double[] point3 = new double[]{0,1,0};
	private static double[] point4 = new double[]{0,1,1};
	private static double[] point5 = new double[]{0,2,1};
	
	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5};
	static int[][] faces = new int[][]{
		{0,2,1},{2,3,1},{2,4,3}
	};
	
	public Smth(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		for (int[] point: getFaces())
			addTriangleWithRandomColor(vertices[point[0]], vertices[point[1]], vertices[point[2]]);
	}

	public static int[][] getFaces(){
		return faces;
	}}
