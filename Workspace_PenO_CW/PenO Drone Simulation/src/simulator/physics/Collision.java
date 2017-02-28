package simulator.physics;

import java.util.ArrayList;

import simulator.objects.Polyhedron;
import simulator.objects.SimulationDrone;
import simulator.objects.Triangle;
import simulator.objects.WorldObject;
import simulator.world.World;

public class Collision {

	private World world;
	private double a;
	private double b;
	private double c;
	private double d;

	public Collision(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public double[] getVector(double[] point1, double[] point2) {
		return new double[] { point1[0] - point2[0], point1[1] - point2[1],
				point1[2] - point2[2] };
	}

	// Requires two vertices of triangle + 1 point of triangle
	public void calculatePlane(double[] vector1, double[] vector2,
			double[] point) {
		double[] crossProd = getCrossProduct(vector1, vector2);
		this.a = crossProd[0];
		this.b = crossProd[1];
		this.c = crossProd[2];
		this.d = a * point[0] + b * point[1] + c * point[2];
	}

	public double calculateDistanceBetweenCurrentPlaneAndPoint(double[] point) {
		return (a * point[0] + b * point[1] + c * point[2] + d)
				/ getRootOfABCSquared();
	}

	// Requires point not on plane (doesn't matter?)
	public double[] getPointPerpendicularOnPlane(double[] point) {
		double vectorLength = getRootOfABCSquared();
		double distance = calculateDistanceBetweenCurrentPlaneAndPoint(point);
		double fraction = distance / vectorLength;
		double[] newVector = { a * fraction, b * fraction, c * fraction };
		return new double[] { point[0] - newVector[0], point[1] - newVector[1],
				point[2] - newVector[2] };
	}

	public double getRootOfABCSquared() {
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2));
	}

	public double[] getNormalVector() {
		return new double[] { a, b, c };
	}

	public double getDotProduct(double[] vector1, double[] vector2) {
		double sum = 0;
		for (int i = 0; i < vector1.length; i++)
			sum += vector1[i] * vector2[i];
		return sum;
	}

	public double[] getCrossProduct(double[] vector1, double[] vector2) {
		// AxB = (AyBz − AzBy, AzBx − AxBz, AxBy − AyBx)
		double x = vector1[1] * vector2[2] - vector1[2] * vector2[1];
		double y = vector1[2] * vector2[0] - vector1[0] * vector2[2];
		double z = vector1[0] * vector2[1] - vector1[1] * vector2[0];
		return new double[] { x, y, z };
	}

	public boolean isPointInTriangle(double[] triangle1, double[] triangle2,
			double[] triangle3, double[] point) {
		// Compute vectors
		double[] v0 = getVector(triangle3, triangle1);
		double[] v1 = getVector(triangle2, triangle1);
		double[] v2 = getVector(point, triangle1);

		// Compute dot products
		double dot00 = getDotProduct(v0, v0);
		double dot01 = getDotProduct(v0, v1);
		double dot02 = getDotProduct(v0, v2);
		double dot11 = getDotProduct(v1, v1);
		double dot12 = getDotProduct(v1, v2);

		// Compute barycentric coordinates
		double denominator = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * denominator;
		double v = (dot00 * dot12 - dot01 * dot02) * denominator;

		// Check if point is in triangle
		return (u >= 0) && (v >= 0) && ((u + v) < 1);
	}

	public boolean checkTrianglesForHit(WorldObject polyhedron,
			SimulationDrone drone) {
		ArrayList<Triangle> trianglesList = ((Polyhedron) polyhedron)
				.getTriangles();
		for (Triangle currTriangle : trianglesList) {
			double[] T1 = currTriangle.getTranslatedPoint1();
			double[] T2 = currTriangle.getTranslatedPoint2();
			double[] T3 = currTriangle.getTranslatedPoint3();
			double[] vector12 = getVector(T1, T2);
			double[] vector13 = getVector(T1, T3);
			calculatePlane(vector13, vector12, T1);
			double[] perpPoint = getPointPerpendicularOnPlane(drone
					.getPosition());
			if (isPointInTriangle(T1, T2, T3, perpPoint))
				return true;
		}
		return false;
	}

	public double getDistanceBetweenPoints(double[] point1, double[] point2) {
		return Math.sqrt(Math.pow(point1[0] - point2[0], 2)
				+ Math.pow(point1[1] - point2[1], 2)
				+ Math.pow(point1[2] - point2[2], 2));
	}
}
