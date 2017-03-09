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

	public TwinkleTwinkleLittleStarPolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		// TODO Auto-generated method stub
		addTriangleWithRandomColor(point1, point3, point12);
		addTriangleWithRandomColor(point12, point3, point4);
		addTriangleWithRandomColor(point12, point4, point2);
		addTriangleWithRandomColor(point1, point12, point2);
		
		addTriangleWithRandomColor(point2, point4, point9);
		addTriangleWithRandomColor(point9, point4, point5);
		addTriangleWithRandomColor(point9, point5, point6);
		addTriangleWithRandomColor(point2, point9, point6);
		
		addTriangleWithRandomColor(point1, point2, point10);
		addTriangleWithRandomColor(point10, point2, point6);
		addTriangleWithRandomColor(point7, point10, point6);
		addTriangleWithRandomColor(point7, point1, point10);
		
		addTriangleWithRandomColor(point7, point8, point11);
		addTriangleWithRandomColor(point11, point8, point3);
		addTriangleWithRandomColor(point1, point11, point3);
		addTriangleWithRandomColor(point7, point11, point1);
		
		addTriangleWithRandomColor(point3, point8, point13);
		addTriangleWithRandomColor(point13, point8, point5);
		addTriangleWithRandomColor(point3, point13, point4);
		addTriangleWithRandomColor(point13, point5, point4);
		
		addTriangleWithRandomColor(point6, point5, point14);
		addTriangleWithRandomColor(point14, point5, point8);
		addTriangleWithRandomColor(point7, point14, point8);
		addTriangleWithRandomColor(point7, point6, point14);
	}

}
