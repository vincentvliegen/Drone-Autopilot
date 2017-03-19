package simulator.objects;

import simulator.world.World;

public class HollowCubePolyhedron extends PredefinedPolyhedron {

	static double[] point1 = {0, 0, 0};
	static double[] point2 = {0, 0, 0.5};
	static double[] point3 = {0, 0.5, 0};
	static double[] point4 = {0, 0.5, 0.5};
	static double[] point5 = {0.5, 0, 0};
	static double[] point6 = {0.5, 0, 0.5};
	static double[] point7 = {0.5, 0.5, 0};
	static double[] point8 = {0.5, 0.5,0.5};
	static double[] point9 = {0, 0.125, 0.125};
	static double[] point10 = {0, 0.125, 0.375};
	static double[] point11 = {0, 0.375, 0.125};
	static double[] point12 = {0, 0.375, 0.375};
	static double[] point13 = {0.5, 0.125, 0.125};
	static double[] point14 = {0.5, 0.125, 0.375};
	static double[] point15 = {0.5, 0.375, 0.125};
	static double[] point16 = {0.5, 0.375, 0.375};
	static double[] point17 = {0, 0, 0.25};
	static double[] point18 = {0, 0.25, 0};
	static double[] point19 = {0, 0.5, 0.25};
	static double[] point20 = {0, 0.25, 0.5};
	static double[] point21 = {0.5, 0, 0.25};
	static double[] point22 = {0.5, 0.25, 0};
	static double[] point23 = {0.5, 0.5, 0.25};
	static double[] point24 = {0.5, 0.25, 0.5};
	
	
	static double[][] vertices = new double[][]{point1,point2,point3,point4,point5,point6,point7,point8,point9,
		point10,point11,point12,point13,point14,point15,point16,point17,point18,point19,point20,point21,point22,
		point23,point24};
	static int[][] faces = new int[][]{
		
		//voorkant
	{0,17,8}, {8, 17, 10},{17,2,10},{10,2,18},{10,18,11},{11,18,3},{11,3,19},{9,11,19},{9,19,1},{16,9,1},{16,8,9},{0,8,16}, 
		//achterkant
	{22,15,7}, {15,23,7},{13,23,15},{13,5,23}, {20,5,13}, {12,20,13}, {4,20,12}, {4,12,21},{21,12,14},{21,14,6},{14,22,6}, {15, 22, 14},
		//grote vlakken
	{3,2,6},{3,6,7},{0,4,6},{0,6,2},{7,1,3},{7,5,1},{1,4,0},{5,4,1},
		//binnenkant
	{12,9,8},{12,13,9},{12,8,10},{14,12,10},{11,14,10},{15,14,11},{9,13,15},{9,15,11}};
	
	public HollowCubePolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		for (int[] points: getFaces())
			addTriangleWithRandomColor(vertices[points[0]], vertices[points[1]], vertices[points[2]]);
	}

	public static int[][] getFaces() {
		return faces;
	}
	
	public static double[][] getPoints() {
		return vertices;
	}

}
