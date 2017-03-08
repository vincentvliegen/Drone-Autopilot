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
	
	public HollowCubePolyhedron(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
	}

	@Override
	protected void defineTriangles() {
		//voorkant
		addTriangleWithRandomColor(point9, point18, point1);
		addTriangleWithRandomColor(point11, point18, point9);
		addTriangleWithRandomColor(point11, point3, point18);
		addTriangleWithRandomColor(point19, point3, point11);
		addTriangleWithRandomColor(point12, point19, point11);
		addTriangleWithRandomColor(point4, point19, point12);
		addTriangleWithRandomColor(point20, point4, point12);
		addTriangleWithRandomColor(point20, point12, point10);
		addTriangleWithRandomColor(point2, point20, point10);
		addTriangleWithRandomColor(point2, point10, point17);
		addTriangleWithRandomColor(point10, point9, point17);
		addTriangleWithRandomColor(point17, point9, point1);
		
		//achterkant
		addTriangleWithRandomColor(point8, point16, point23);
		addTriangleWithRandomColor(point8, point24, point16);
		addTriangleWithRandomColor(point16, point24, point14);
		addTriangleWithRandomColor(point24, point6, point14);
		addTriangleWithRandomColor(point14, point6, point21);
		addTriangleWithRandomColor(point14, point21, point13);
		addTriangleWithRandomColor(point13, point21, point5);
		addTriangleWithRandomColor(point22, point13, point5);
		addTriangleWithRandomColor(point15, point13, point22);
		addTriangleWithRandomColor(point7, point15, point22);
		addTriangleWithRandomColor(point7, point23, point15);
		
		//grote vlakken
		addTriangleWithRandomColor(point7, point3, point4);
		addTriangleWithRandomColor(point8, point7, point4);
		addTriangleWithRandomColor(point7, point5, point1);
		addTriangleWithRandomColor(point3, point7, point1);
		addTriangleWithRandomColor(point4, point2, point8);
		addTriangleWithRandomColor(point2, point6, point8);
		addTriangleWithRandomColor(point1, point5, point2);
		addTriangleWithRandomColor(point2, point5, point6);
		
		//binnenkant
		addTriangleWithRandomColor(point9, point10, point13);
		addTriangleWithRandomColor(point10, point14, point13);
		addTriangleWithRandomColor(point11, point9, point13);
		addTriangleWithRandomColor(point11, point13, point15);
		addTriangleWithRandomColor(point11, point15, point12);
		addTriangleWithRandomColor(point12, point15, point16);
		addTriangleWithRandomColor(point16, point14, point10);
		addTriangleWithRandomColor(point12, point16, point10);
		
	}

}
