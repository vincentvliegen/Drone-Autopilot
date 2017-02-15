package simulator.objects;

import com.jogamp.opengl.GL2;

public class Triangle {

	private double[] point1;
	private double[] point2;
	private double[] point3;

	GL2 gl;

	// TODO voeg kleur vanbuiten toe
	// TODO voeg kleur vanbinnen toe
	public Triangle(Polyhedron polyhedron, double[] point1, double[] point2, double[] point3) {
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.gl = polyhedron.getGl();
	}

	public GL2 getGl() {
		return gl;
	}

	public double[] getPoint1() {
		return point1;
	}

	public double[] getPoint2() {
		return point2;
	}

	public double[] getPoint3() {
		return point3;
	}

	// TODO voeg berekening binnenste driehoek toe

	public void draw() {
		// zowel buitenste als binnenste driehoek, met kleur

		// TODO buitenste kleur moet ingesteld
		getGl().glColor3f(0f, 0.5f, 1f);

		// Driehoek zelf
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles
		getGl().glVertex3d(getPoint1()[0], getPoint1()[1], getPoint1()[2]);
		getGl().glVertex3d(getPoint2()[0], getPoint2()[1], getPoint2()[2]);
		getGl().glVertex3d(getPoint3()[0], getPoint3()[1], getPoint3()[2]);

		/*
		 getGl().glVertex3f(-50f,-50f,0.0f);
		 getGl().glVertex3f(-50f,50f,0.0f); 
		 getGl().glVertex3f(50f,-50f,0.0f);
		 */

		getGl().glEnd();

		// Binnenste gedeelte

		// TODO binnenste kleur moet ingesteld
		getGl().glColor3f(0f, 1f, 0f);

		// TODO punten op basis van buitenste en zwaartepunt
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles
		getGl().glVertex3f(-20f, -20f, 0.0f);
		getGl().glVertex3f(-20f, 20f, 0.0f);
		getGl().glVertex3f(20f, -20f, 0.0f);

		getGl().glEnd();

	}

}
