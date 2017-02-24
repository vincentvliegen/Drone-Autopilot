package simulator.objects;

import com.jogamp.opengl.GL2;

public class Triangle {

	private double[] point1;
	private double[] point2;
	private double[] point3;
	private double[] innerPoint1;
	private double[] innerPoint2;
	private double[] innerPoint3;
	private float[] color;

	GL2 gl;
	private Polyhedron polyhedron;

	// TODO voeg kleur vanbuiten toe
	// TODO voeg kleur vanbinnen toe
	public Triangle(GL2 gl, double[] point1, double[] point2, double[] point3, float[] color, Polyhedron poly) {
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.gl = gl;
		this.innerPoint1 = getInnerPoint(point1);
		this.innerPoint2 = getInnerPoint(point2);
		this.innerPoint3 = getInnerPoint(point3);
		this.color = color;
		this.polyhedron = poly;
	}

	private Polyhedron getPolyhedron() {
		return this.polyhedron;
	}

	private double getGravityX() {
		return (getPoint1()[0] + getPoint2()[0] + getPoint3()[0]) / 3;

	}

	private double getGravityY() {
		return (getPoint1()[1] + getPoint2()[1] + getPoint3()[1]) / 3;

	}


	private double getGravityZ() {
		return (getPoint1()[2] + getPoint2()[2] + getPoint3()[2]) / 3;

	}

	private float[] getColor() {
		return this.color;
	}


	private GL2 getGl() {
		return gl;
	}

	private double[] getPoint1() {
		return point1;
	}

	private double[] getPoint2() {
		return point2;
	}

	private double[] getPoint3() {
		return point3;
	}

	private double[] getInnerPoint1() {
		return innerPoint1;
	}

	private double[] getInnerPoint2() {
		return innerPoint2;
	}

	private double[] getInnerPoint3() {
		return innerPoint3;
	}


	private double[] getInnerPoint(double[] innerPoint) {
		return new double[]{(innerPoint[0] - getGravityX()) / Math.sqrt(2) + getGravityX(),
		(innerPoint[1] - getGravityY()) / Math.sqrt(2) + getGravityY(),
		(innerPoint[2] - getGravityZ()) / Math.sqrt(2) + getGravityZ()};
	}

	public void draw() {
		// zowel buitenste als binnenste driehoek, met kleur

		// TODO buitenste kleur moet ingesteld


		// Driehoek zelf
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(getColor()[0], getColor()[1], getColor()[2]);

		getGl().glVertex3d(getPoint1()[0]+getPolyhedron().getPosition()[0], getPoint1()[1], getPoint1()[2]+getPolyhedron().getPosition()[2]);
		getGl().glVertex3d(getPoint2()[0]+getPolyhedron().getPosition()[0], getPoint2()[1], getPoint2()[2]+getPolyhedron().getPosition()[2]);
		getGl().glVertex3d(getPoint3()[0]+getPolyhedron().getPosition()[0], getPoint3()[1], getPoint3()[2]+getPolyhedron().getPosition()[2]);

		getGl().glEnd();


		// Binnenste gedeelte

		// TODO binnenste kleur moet ingesteld
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(0f, 1f, 0f);

		getGl().glVertex3d(getInnerPoint1()[0]+getPolyhedron().getPosition()[0], getInnerPoint1()[1]+getPolyhedron().getPosition()[1], getInnerPoint1()[2]+getPolyhedron().getPosition()[2]);
		getGl().glVertex3d(getInnerPoint2()[0]+getPolyhedron().getPosition()[0], getInnerPoint2()[1]+getPolyhedron().getPosition()[1], getInnerPoint2()[2]+getPolyhedron().getPosition()[2]);
		getGl().glVertex3d(getInnerPoint3()[0]+getPolyhedron().getPosition()[0], getInnerPoint3()[1]+getPolyhedron().getPosition()[1], getInnerPoint3()[2]+getPolyhedron().getPosition()[2]);

		getGl().glEnd();

	}

}
