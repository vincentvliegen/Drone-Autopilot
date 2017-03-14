package simulator.objects;

import simulator.world.World;

public class LetterLPolyhedron extends PredefinedPolyhedron {
	
	static double[] point1 = {0, 0, 0};
	static double[] point2 = {0, 0, -0.5};
	static double[] point3 = {0, 0.5, 0};
	static double[] point4 = {0, 0.5, -0.5};
	static double[] point5 = {0, -0.5, 0};
	static double[] point6 = {0, -0.5, -0.5};
	static double[] point7 = {0, 0, 0.5};
	static double[] point8 = {0, -0.5, 0.5};
	static double[] point9 = {0.5, 0, 0};
	static double[] point10 = {0.5, 0, 0.5};
	static double[] point11 = {0.5, -0.5, 0.5};
	static double[] point12 = {0.5, -0.5, 0};
	static double[] point13 = {0.5, 0.5, 0};
	static double[] point14 = {0.5, 0.5, -0.5};
	static double[] point15 = {0.5, 0, -0.5};
	static double[] point16 = {0.5, -0.5, -0.5};

	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5,point6,point7,point8,point9,
		point10,point11,point12,point13,point14,point15,point16};
	static int[][] faces = new int[][]{
		{0,1,4},{1,5,4},{0,4,6},{6,4,7},{2,1,0},{3,1,2},{8,0,6},{2,0,8},{12,2,8},{13,3,2},{12,13,2},{12,8,14}, 
		{12,14,13},{13,14,3},{3,14,1},{8,6,9},{6,7,10},{6,10,9},{9,10,11},{9,11,8},{7,4,10},{4,11,10},{4,11,10},
		{8,11,15},{8,15,14},{14,15,1},{1,15,5},{4,5,11},{5,15,11}
	};


	public LetterLPolyhedron(World world, PolyhedronType type, double[] position) {
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
