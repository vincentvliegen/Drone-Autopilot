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


	public LetterLPolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		addTriangleWithRandomColor(point1, point2, point5);
		addTriangleWithRandomColor(point2, point6, point5);
		addTriangleWithRandomColor(point1, point5, point7);
		addTriangleWithRandomColor(point7, point5, point8);
		addTriangleWithRandomColor(point3, point2, point1);
		addTriangleWithRandomColor(point4, point2, point3);
		addTriangleWithRandomColor(point9, point1, point7);
		addTriangleWithRandomColor(point3, point1, point9);
		addTriangleWithRandomColor(point13, point3, point9);
		addTriangleWithRandomColor(point14, point4, point3);
		addTriangleWithRandomColor(point13, point14, point3);
		addTriangleWithRandomColor(point13, point9, point15);
		addTriangleWithRandomColor(point13, point15, point14);
		addTriangleWithRandomColor(point14, point15, point4);
		addTriangleWithRandomColor(point4, point15, point2);
		addTriangleWithRandomColor(point9, point7, point10);
		addTriangleWithRandomColor(point7 , point8, point11);
		addTriangleWithRandomColor(point7, point11, point10);
		addTriangleWithRandomColor(point10, point11, point12);
		addTriangleWithRandomColor(point10, point12, point9);
		addTriangleWithRandomColor(point8, point5, point11);
		addTriangleWithRandomColor(point5, point12, point11);
		addTriangleWithRandomColor(point9, point12, point16);
		addTriangleWithRandomColor(point9, point16, point15);
		addTriangleWithRandomColor(point15, point16, point2);
		addTriangleWithRandomColor(point2, point16, point6);
		addTriangleWithRandomColor(point5, point6, point12);
		addTriangleWithRandomColor(point6, point16, point12);
	}

}
