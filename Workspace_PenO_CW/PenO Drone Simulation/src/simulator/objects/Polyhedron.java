package simulator.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.jogamp.opengl.GL2;

import simulator.world.World;

public abstract class Polyhedron extends WorldObject {

	GL2 gl;
	private ArrayList<Triangle> triangles = new ArrayList<>();
	private PolyhedronType type;
	private double[] position = new double[]{0,0,0};
	protected abstract void defineTriangles();

	
	protected ArrayList<Triangle> getTriangles() {
		return this.triangles;
	}
	
	protected PolyhedronType getPolyhedronType() {
		return this.type;
	}

	// TODO op deze manier qua constructor enz.?
	public Polyhedron(World world, PolyhedronType type, double[] position) {
		super(world);
		this.type = type;
		this.gl = getWorld().getGL().getGL2();
		translatePolyhedronOver(position);

		defineTriangles();
	}
	
	public Polyhedron(World world, PolyhedronType type) {
		super(world);
		this.type = type;
		this.gl = getWorld().getGL().getGL2();
		defineTriangles();
	}

	public GL2 getGl() {
		return gl;
	}


	@Override
	public void draw() {
		for (Triangle triangle : getTriangles())
			triangle.draw();

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
	
	//TODO aannemen dat je precies 3 waarden krijgt (met exception?)
	public void translatePolyhedronOver(double[] vector) {
		position[0] = position[0] + vector[0];
		position[1] = position[1] + vector[1];
		position[2] = position[2] + vector[2];
		for(Triangle triangle:getTriangles()) {
			triangle.updatePoints();
		}


	}
	
	private ArrayList<float[]> colorsOfTriangles = new ArrayList<>();
	
	private ArrayList<float[]> getColorsOfTriangles() {
		return colorsOfTriangles;
	}

	@Override
	public float getRadius() {
		// TODO hoe?
		return 0;
	}
	
	protected void addTriangleWithRandomColor(double[] point1, double[] point2, double[] point3) {
		Random rand = new Random();
		float r = 0, g=0,b=0;
		float[] color = {r,g,b};
		while((r == g && r == b) || getColorsOfTriangles().contains(color)) {
			r = rand.nextFloat();
			g = rand.nextFloat();
			b = rand.nextFloat();
			color[0] = r;
			color[1] = g;
			color[2] = b;
		}
		addTriangle(new Triangle(getGl(), point1, point2, point3, color, this));
	}
	
	private void addTriangle(Triangle triangle) {
		this.triangles.add(triangle);
	}
}
