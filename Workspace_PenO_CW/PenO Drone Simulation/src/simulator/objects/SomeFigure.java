package simulator.objects;

import java.util.ArrayList;

import simulator.world.World;

public class SomeFigure extends Polyhedron{


	private World world;

	protected ArrayList<Triangle> getTriangles() {
		double[] point1 = { -0.2, 0, -0.2 };
		double[] point2 = { -0.2, 0, 0.2 };
		double[] point3 = { 0.2, 0, 0 };
		double[] point4 = { 0, 0.2, 0 }; // upper

		ArrayList<Triangle> triangles = new ArrayList<Triangle>();

		triangles.add(new Triangle(world.getGL().getGL2(), point1, point2, point3));
		triangles.add(new Triangle(world.getGL().getGL2(), point1, point2, point4));
		triangles.add(new Triangle(world.getGL().getGL2(), point1, point3, point4));
		triangles.add(new Triangle(world.getGL().getGL2(), point2, point3, point4));

		return triangles;
	}



	public SomeFigure(World world) {
		super(world);
		this.world = world;
	}







}
