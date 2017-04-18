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
		{4,1,0},{4,5,1},{6,4,0},{7,4,6},{0,1,2},{2,1,3},{6,0,8},{8,0,2},{8,2,12},{2,3,13},{2,13,12},{14,8,12}, 
		{13,14,12},{3,14,13},{1,14,3},{9,6,8},{10,7,6},{9,10,6},{11,10,9},{8,11,9},{10,4,7},{10,11,4},
		{15,11,8},{14,15,8},{1,15,14},{5,15,1},{11,5,4},{11,15,5}
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
