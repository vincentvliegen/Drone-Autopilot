package simulator.objects;

import com.jogamp.opengl.GL2;

public class Triangle {

	private double[] point1;
	private double[] point2;
	private double[] point3;
	private double[] innerPoint1;
	private double[] innerPoint2;
	private double[] innerPoint3;
	private int[] color;

	GL2 gl;
	private double[] translatedPoint1 = new double[3];
	private double[] translatedPoint2 = new double[3];
	private double[] translatedPoint3 = new double[3];

	// TODO voeg kleur vanbuiten toe
	// TODO voeg kleur vanbinnen toe
	public Triangle(GL2 gl, double[] point12, double[] point22, double[] point32, int[] is) {
		this.point1 = point12;
		this.point2 = point22;
		this.point3 = point32;
		this.gl = gl;
		//TODO update positions!
		this.color = is;
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



	private double getGravityX() {
		return (getTranslatedPoint1()[0] + getTranslatedPoint2()[0] + getTranslatedPoint3()[0]) / 3;

	}

	private double getGravityY() {
		return (getTranslatedPoint1()[1] + getTranslatedPoint2()[1] + getTranslatedPoint3()[1]) / 3;

	}


	private double getGravityZ() {
		return (getTranslatedPoint1()[2] + getTranslatedPoint2()[2] + getTranslatedPoint3()[2]) / 3;

	}

	public int[] getColor() {
		return this.color;
	}


	private GL2 getGl() {
		return gl;
	}

	private double[] getTranslatedPoint1() {
		return translatedPoint1;
	}

	private double[] getTranslatedPoint2() {
		return translatedPoint2;
	}

	private double[] getTranslatedPoint3() {
		return translatedPoint3;
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


	private double[] getInnerPoint(double[] outerPoint) {
		return new double[]{(outerPoint[0] - getGravityX()) / Math.sqrt(2) + getGravityX(),
				(outerPoint[1] - getGravityY()) / Math.sqrt(2) + getGravityY(),
				(outerPoint[2] - getGravityZ()) / Math.sqrt(2) + getGravityZ()};
	}

	public void draw() {
		// zowel buitenste als binnenste driehoek, met kleur

		// TODO buitenste kleur moet ingesteld


		// Driehoek zelf
		//		gl.glTranslated(getPolyhedron().getPosition()[0], getPolyhedron().getPosition()[1], getPolyhedron().getPosition()[2]);
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(getColor()[0], getColor()[1], getColor()[2]);

		getGl().glVertex3d(getTranslatedPoint1()[0], getTranslatedPoint1()[1], getTranslatedPoint1()[2]);
		getGl().glVertex3d(getTranslatedPoint2()[0], getTranslatedPoint2()[1], getTranslatedPoint2()[2]);
		getGl().glVertex3d(getTranslatedPoint3()[0], getTranslatedPoint3()[1], getTranslatedPoint3()[2]);

		getGl().glEnd();


		// Binnenste gedeelte

		// TODO binnenste kleur moet ingesteld
		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(0f, 1f, 0f);

		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);
		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);
		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);

		getGl().glEnd();

	}



	public void updatePoints(double[] ds) {
		//TODO nullpointer afvangen?		
		//		if(getPolyhedron() != null) {
		for(int i = 0; i < 3; i++) {
			translatedPoint1[i] = (float) (getPoint1()[i] + ds[i]);
			translatedPoint2[i] = (float) (getPoint2()[i] + ds[i]);
			translatedPoint3[i] = (float) (getPoint3()[i] + ds[i]);
		}
		//		System.out.println(Arrays.toString(point1));
		innerPoint1 = getInnerPoint(getTranslatedPoint1());
		innerPoint2 = getInnerPoint(getTranslatedPoint2());
		innerPoint3 = getInnerPoint(getTranslatedPoint3());
		//		}
	}

}
