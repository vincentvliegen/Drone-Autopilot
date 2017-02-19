package simulator.objects;

import java.util.ArrayList;

import simulator.world.World;

public class SomeFigure {

	static double[] point1 = { -0.2, 0, -0.2 };
	static double[] point2 = { -0.2, 0, 0.2 };
	static double[] point3 = { 0.2, 0, 0 };
	static double[] point4 = { 0, 0.2, 0 }; // upper

	static Triangle triangle1;
	static Triangle triangle2;
	static Triangle triangle3;
	static Triangle triangle4;

	


	private static Polyhedron poly;
	private World world;
	
	static ArrayList<Triangle> getArrayListOfTriangles() {

		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		triangles.add(triangle1);
		triangles.add(triangle2);
		triangles.add(triangle3);
		triangles.add(triangle4);

		return triangles;
	}
	
	private World getWorld() {
		return world;
	}

	public SomeFigure(World world) {
		this.world = world;
		triangle1 = new Triangle(world.getGL().getGL2(), point1, point2, point3);
		triangle2 = new Triangle(world.getGL().getGL2(), point1, point2, point4);
		triangle3 = new Triangle(world.getGL().getGL2(), point1, point3, point4);
		triangle4 = new Triangle(world.getGL().getGL2(), point2, point3, point4);

		poly = new Polyhedron(getWorld(), getArrayListOfTriangles());

	}
	
	public Polyhedron getPoly() {
		System.out.println("Jep!");
		return poly;
	}

}
