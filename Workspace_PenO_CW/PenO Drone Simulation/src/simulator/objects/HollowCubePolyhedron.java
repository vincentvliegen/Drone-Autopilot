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
	{8,17,0}, {10, 17, 8},{10,2,17},{18,2,10},{11,18,10},{3,18,11},{19,3,11},{19,11,9},{1,19,9},{1,9,16},{9,8,16},{16,8,0}, 
	{7,15,22}, {7,23,15},{15,23,13},{23,5,13}, {13,5,20}, {13,20,12}, {12,20,4}, {21,12,4},{14,12,21},{6,14,21},{6,22,14},
	{6,2,3},{7,6,3},{6,4,0},{2,6,0},{3,1,7},{1,5,7},{0,4,1},{1,4,5},
	{8,9,12},{9,13,12},{10,8,12},{10,12,14},{10,14,11},{11,14,15},{15,13,9},{11,15,9}};
	
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
