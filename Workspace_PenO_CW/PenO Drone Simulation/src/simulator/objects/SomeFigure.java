package simulator.objects;

import simulator.world.World;

public class SomeFigure extends PredefinedPolyhedron{


	static double[] point1 = { -0.2, 0, -0.2 };
	static double[] point2 = { -0.2, 0, 0.2 };
	static double[] point3 = { 0.2, 0, 0 };
	static double[] point4 = { 0, 0.2, 0 }; // upper
	static double[][] vertices = new double[][]{point1,point2,point3,point4};

	
	public SomeFigure(World world, PolyhedronType type, double[] position) {
		super(world, type, position, vertices);
		defineTriangles();
	}



	protected void defineTriangles() {
		double[] massPoint = {-(point1[0]+point2[0]+point3[0]+point4[0])/4, -(point1[1]+point2[1]+point3[1]+point4[1])/4, -(point1[2]+point2[2]+point3[2]+point4[2])/4};

		// double[] point1new = {point1[0]-massPoint[0],point1[1]-massPoint[1],point1[2]-massPoint[2]};
		// double[] point2new = {point2[0]-massPoint[0],point2[1]-massPoint[1],point2[2]-massPoint[2]};
		// double[] point3new = {point3[0]-massPoint[0],point3[1]-massPoint[1],point3[2]-massPoint[2]};
		// double[] point4new = {point4[0]-massPoint[0],point4[1]-massPoint[1],point4[2]-massPoint[2]};


		addTriangleWithRandomColor(point1, point2, point3);
		addTriangleWithRandomColor(point1, point2, point4);
		addTriangleWithRandomColor(point1, point4, point3);
		addTriangleWithRandomColor(point2, point4, point3);
		
		translatePolyhedronOver(massPoint);
	}




}
