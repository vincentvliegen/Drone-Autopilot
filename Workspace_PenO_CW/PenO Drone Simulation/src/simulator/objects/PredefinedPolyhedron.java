package simulator.objects;

import java.awt.Color;
import java.util.Random;

import simulator.physics.MathCalculations;
import simulator.world.World;

public abstract class PredefinedPolyhedron extends Polyhedron {

	public PredefinedPolyhedron(World world, PolyhedronType type, double[] position, double[][] vertices) {
		super(world, type, vertices);
		defineTriangles();
		translatePolyhedronOver(MathCalculations.getVector(position, getPosition()));
	}

	protected abstract void defineTriangles();

	protected void addTriangleWithRandomColor(double[] point1, double[] point2, double[] point3) {
		Random r = new Random();
		int hueDegrees = r.nextInt(361);
		float hueRadians = (float)Math.toRadians(hueDegrees);
		float saturation = r.nextFloat() * (1 - 0.56f) + 0.56f;
		float brightness;
		if (getPolyhedronType() == PolyhedronType.TARGET)
			brightness = r.nextFloat() * (1 - 0.56f) + 0.56f;
		else
			brightness = r.nextFloat() * 0.44f;
		int rgb = Color.HSBtoRGB(hueRadians, saturation, brightness);
		int[] color = new int[3];
		color[0] = (rgb >> 16) & 0xFF;
		color[1] = (rgb >> 8) & 0xFF;
		color[2] = rgb & 0xFF;
		
		addTriangle(new Triangle(getGl(), point1, point2, point3, color));
	}

}
