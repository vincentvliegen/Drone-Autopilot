package simulator.objects;

import com.jogamp.opengl.GL2;

public class Triangle {

	private double[] point1;
	private double[] point2;
	private double[] point3;
	private double[] innerPoint1;
	private double[] innerPoint2;
	private double[] innerPoint3;

	GL2 gl;

	// TODO voeg kleur vanbuiten toe
	// TODO voeg kleur vanbinnen toe
	public Triangle(Polyhedron polyhedron, double[] point1, double[] point2, double[] point3) {
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.gl = polyhedron.getGl();
		this.innerPoint1 = getInnerPoint(point1);
		this.innerPoint2 = getInnerPoint(point2);
		this.innerPoint3 = getInnerPoint(point3);
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

		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);
		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);
		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);

		getGl().glEnd();

	}

}
