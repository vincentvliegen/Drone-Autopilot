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

	public HourglassPolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		addTriangleWithRandomColor(point1, point3, point2);
		addTriangleWithRandomColor(point1, point2, point4);
		addTriangleWithRandomColor(point5, point4, point2);
		addTriangleWithRandomColor(point2, point3, point5);
		addTriangleWithRandomColor(point4, point3, point1);
		addTriangleWithRandomColor(point5, point3, point4);
		addTriangleWithRandomColor(point3, point6, point7);
		addTriangleWithRandomColor(point3, point7, point8);
		addTriangleWithRandomColor(point3, point9, point6);
		addTriangleWithRandomColor(point3, point8, point9);
		addTriangleWithRandomColor(point7, point6, point8);
		addTriangleWithRandomColor(point8, point9, point6);
//		addTriangleWithRandomColor(point4, point9, point6);
//		addTriangleWithRandomColor(point1, point4, point6);
//		addTriangleWithRandomColor(point1, point6, point9);
//		addTriangleWithRandomColor(point1, point9, point4);
//		addTriangleWithRandomColor(point5, point8, point7);
//		addTriangleWithRandomColor(point5, point7, point2);
//		addTriangleWithRandomColor(point5, point2, point7);
//		addTriangleWithRandomColor(point5, point7, point8);
	}

}
