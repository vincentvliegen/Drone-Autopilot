package simulator.objects;

import simulator.world.World;
//import sun.net.www.content.audio.wav;

public class ThreePeaks extends PredefinedPolyhedron {

	static double[] point1 = { -0.5, 0, -0.5 };
	static double[] point2 = { -0.5, 0, 0.5};
	static double[] point3 = { 0.5, 0, -0.5 };
	static double[] point4 = { 0.5, 0, 0.5};
	static double[] point5 = { -0.5, 0, -1.5};
	static double[] point6 = { 0.5, 0, -1.5};
	static double[] point7 = { -0.5, 0, 1.5};
	static double[] point8 = { 0.5, 0, 1.5};
	static double[] point9 = { 0, 0.5, 0 }; // upper middle
	static double[] point10 = { 0, 0.5, 1}; // right middle
	static double[] point11 = {0, 0.5, -1}; // left middle
	
	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5,point6,point7,point8,point9,point10,point11};
	static int[][] faces = new int[][]{
		{0,3,2},{0,1,3},{0,2,5},{0,5,4},{1,7,3},{1,6,7},{8,1,0},{8,3,1},{8,2,3},{8,0,2},
		{10,0,4},{10,2,0},{10,5,2},{10,4,5},{9,6,1},{9,7,6},{9,3,7},{9,1,3}
	};
	
	public ThreePeaks(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
		
	}

	@Override
	protected void defineTriangles() {
		/*
		addTriangleWithRandomColor(point1, point4, point3);
		addTriangleWithRandomColor(point1, point2, point4);
		addTriangleWithRandomColor(point1, point3, point6);
		addTriangleWithRandomColor(point1, point6, point5);
		addTriangleWithRandomColor(point2, point8, point4);
		addTriangleWithRandomColor(point2, point7, point8);
		addTriangleWithRandomColor(point1, point2, point9);
		addTriangleWithRandomColor(point2, point4, point9);
		addTriangleWithRandomColor(point4, point3, point9);
		addTriangleWithRandomColor(point3, point1, point9);
		
		addTriangleWithRandomColor(point5, point1, point11);
		addTriangleWithRandomColor(point1, point3, point11);
		addTriangleWithRandomColor(point3, point6, point11);
		addTriangleWithRandomColor(point6, point5, point11);
		addTriangleWithRandomColor(point2, point7, point10);
		addTriangleWithRandomColor(point7, point8, point10);
		addTriangleWithRandomColor(point8, point4, point10);
		addTriangleWithRandomColor(point4, point2, point10);
		*/
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
