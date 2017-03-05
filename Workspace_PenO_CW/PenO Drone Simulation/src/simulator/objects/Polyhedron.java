package simulator.objects;

import java.util.ArrayList;
import com.jogamp.opengl.GL2;

import simulator.physics.MathCalculations;
import simulator.world.World;

public class Polyhedron extends WorldObject {

	GL2 gl;
	private ArrayList<Triangle> triangles = new ArrayList<>();
	private PolyhedronType type;
	private double[] position = new double[] { 0, 0, 0 };
	private double[][] vertices;
	private float radius;

	public ArrayList<Triangle> getTriangles() {
		// defensieve manier
		return new ArrayList<>(this.triangles);
	}

	public double[][] getVertices() {
		return vertices;
	}

	public PolyhedronType getPolyhedronType() {
		return this.type;
	}

	// TODO op deze manier qua constructor enz.?
	public Polyhedron(World world, PolyhedronType type, double[][] vertices) {
		super(world);
		this.type = type;
		this.gl = getWorld().getGL().getGL2();
		this.vertices = vertices;
		handlePositionAndMassPoint();
		this.radius = calculateRadius();

	}

	// making the polyhedron's position and the polyhedron's mass point the same
	// points

	protected void handlePositionAndMassPoint() {
		// TODO pas punten van polyhedron aan en die van driehoeken

		double[] massPoint = new double[3];
		double temp = 0;
		for (int i = 0; i < 3; i++) {
			for (double[] vertex : getVertices()) {
				temp += vertex[i];
			}
			massPoint[i] = temp;
			temp = 0;
		}

		// 1. zet de points op hun nieuwe plaats op basis van het masspoint
		// 2. tel masspoint op bij positie

		for (Triangle triangle : getTriangles()) {
			triangle.updateOriginalPoints(massPoint);
		}
		translatePolyhedronOver(massPoint);

	}

	public GL2 getGl() {
		return gl;
	}

	@Override
	public void draw() {
		if (triangles != null) {
			for (Triangle triangle : getTriangles())
				triangle.draw();
		}

	}

	/*
	 * TODO hoe met posities van driehoeken? want je bepaalt driehoeken die dan
	 * samen een polyhedron vormen maar als je een polyhedron op een bepaalde
	 * plaats wilt, moet je alle driehoeken verschuiven zodat de polyhedron op
	 * die plaats komt evt definieren rond (0,0,0) en dan verschuiven voor een
	 * bepaalde positie? (dan los je meteen het probleem op van getPosition()
	 */

	@Override
	public double[] getPosition() {
		return this.position;
	}

	// TODO aannemen dat je precies 3 waarden krijgt (met exception?)
	public void translatePolyhedronOver(double[] vector) {
		position[0] = position[0] + vector[0];
		position[1] = position[1] + vector[1];
		position[2] = position[2] + vector[2];
		for (Triangle triangle : getTriangles()) {
			triangle.updatePoints(getPosition());
		}

	}

	// TODO is dit ok? lijst van vertices wordt op dit moment niet aangepast,
	// dus radius zou in begin ok moeten zijn
	// (we berekenen radius pas nadat de positie berekend is als het massapunt van de polyhedron)

	private float calculateRadius() {
		double maximumDistance = 0;
		for (double[] currVertex : getVertices()) {
			double currDistance = MathCalculations.getDistanceBetweenPoints(currVertex, getPosition());
			if (currDistance > maximumDistance)
				maximumDistance = currDistance;
		}
		return (float) maximumDistance;
	}

	@Override
	public float getRadius() {
		return this.radius;
	}



	protected void addTriangle(Triangle triangle) {
		this.triangles.add(triangle);
	}

	public void addTriangleList(ArrayList<Triangle> list) {
		// defensief
		this.triangles = new ArrayList<>(list);
	}
}
