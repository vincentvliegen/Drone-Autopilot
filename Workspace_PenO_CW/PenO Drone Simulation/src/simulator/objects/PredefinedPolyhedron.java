package simulator.objects;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

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
		int r = 0, g = 0, b = 0;
		int[] color = { r, g, b };
		int min = 0;
		// TODO 256?
		int max = 255;
		while ((r == g && r == b) || getWorld().getTriangleColors().contains(color)) {
			r = ThreadLocalRandom.current().nextInt(min, max + 1);
			g = ThreadLocalRandom.current().nextInt(min, max + 1);
			b = ThreadLocalRandom.current().nextInt(min, max + 1);
			color[0] = r;
			color[1] = g;
			color[2] = b;
		}
		float[] temp = new float[3];
		Color.RGBtoHSB(color[0], color[1], color[2], temp);

		// make sure that the outer color has the correct saturation value

		// TODO nodig?
		boolean hasChanged = false;
		// TODO niet goed want als S = 0.50, 1-0.5 == 0.5 :(
		// --> ok nu?
		if (temp[1] < 0.45) {
			temp[1] += 0.55;
			hasChanged = true;
		} else if (temp[1] < 0.55) {
			temp[1] += 0.45;
		}

		// TODO niet goed want als v = 0.50, 1-0.5 == 0.5 :(
		// --> OK nu?
		if (getPolyhedronType() == PolyhedronType.OBSTACLE) {
			if (temp[2] > 0.55) {
				temp[2] -= 0.55;
				hasChanged = true;
			} else if (temp[2] > 0.45) {
				temp[2] -= 0.45;
				hasChanged = true;
			}
		} else if (getPolyhedronType() == PolyhedronType.TARGET) {
			if (temp[2] <= 0.45) {

				temp[2] += 0.55;
				hasChanged = true;
			} else if (temp[2] < 0.55) {
				temp[2] += 0.45;
				hasChanged = true;
			}
		}

		// enkel als er iets gewijzigd is, ook effectief kleuren aanpassen,
		// anders gewoon overhead
		if (hasChanged) {
			int inner = Color.HSBtoRGB(temp[0], temp[1], temp[2]);
			color[0] = ((inner >> 16) & 0xFF);
			color[1] = ((inner >> 8) & 0xFF);
			color[2] = (inner & 0xFF);
		}
		addTriangle(new Triangle(getGl(), point1, point2, point3, color));
	}

}
