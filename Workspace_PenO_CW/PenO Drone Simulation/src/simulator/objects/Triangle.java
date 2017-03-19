package simulator.objects;

import java.awt.Color;
import java.util.Arrays;

import com.jogamp.opengl.GL2;

public class Triangle {

	private double[] point1;
	private double[] point2;
	private double[] point3;
	private double[] innerPoint1;
	private double[] innerPoint2;
	private double[] innerPoint3;
	private float[] color = new float[3];
	private int[] intColor = new int[3];

	GL2 gl;
	private float[] innerColor;

	public Triangle(GL2 gl, double[] point12, double[] point22, double[] point32, int[] color) {
		this.point1 = new double[]{point12[0],point12[1],point12[2]};
		this.point2 = new double[]{point22[0],point22[1],point22[2]};
		this.point3 = new double[]{point32[0],point32[1],point32[2]};
		this.gl = gl;
		this.color[0] = color[0]/255f;
		this.color[1] = color[1]/255f;
		this.color[2] = color[2]/255f;
		this.intColor = color;
		createInnerColor();
		updatePoints(new double[]{0,0,0});
	}

	private void createInnerColor() {
		float[] temp = new float[3];
		Color.RGBtoHSB(getIntColor()[0], getIntColor()[1], getIntColor()[2], temp);
		//TODO niet goed want als s = 0.50, 1-0.5 == 0.5 :(
//		temp[1] = 1 - temp[1];
//		TODO met bovenstaande of met deze, want s kan niet onder 0.55 zijn per definitie van triangle
		temp[1] -= 0.55;
		int inner = Color.HSBtoRGB(temp[0], temp[1], temp[2]);
		this.innerColor = new float[]{((inner>>16)&0xFF)/255f, ((inner>>8)&0xFF)/255f, (inner&0xFF)/255f};
		
		
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
		return (getPoint1()[0] + getPoint2()[0] + getPoint3()[0]) / 3;

	}

	private double getGravityY() {
		return (getPoint1()[1] + getPoint2()[1] + getPoint3()[1]) / 3;

	}

	private double getGravityZ() {
		return (getPoint1()[2] + getPoint2()[2] + getPoint3()[2]) / 3;

	}

	public float[] getColor() {
		return this.color;
	}

	public int[] getIntColor() {
		return intColor;
	}
	
	public float[] getInnerColor() {
		return innerColor;
	}

	private GL2 getGl() {
		return gl;
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

	private double[] calculateInnerPoint(double[] outerPoint) {
		return new double[] { (outerPoint[0] - getGravityX()) / Math.sqrt(2) + getGravityX(),
				(outerPoint[1] - getGravityY()) / Math.sqrt(2) + getGravityY(),
				(outerPoint[2] - getGravityZ()) / Math.sqrt(2) + getGravityZ() };
	}

	public void draw() {
		// zowel buitenste als binnenste driehoek, met kleur


		// buitenkant van driehoek, getekend als polygon rond binnendriehoek, zodat beiden niet overlappen
		
		// Binnenste gedeelte

		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(getInnerColor()[0], getInnerColor()[1], getInnerColor()[2]);
		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);
		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);
		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);

		getGl().glEnd();

		
		
		
		getGl().glBegin(GL2.GL_TRIANGLES);

		getGl().glColor3f(getColor()[0], getColor()[1], getColor()[2]);
		
		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);
		getGl().glVertex3d(getPoint1()[0], getPoint1()[1], getPoint1()[2]);
		getGl().glVertex3d(getPoint2()[0], getPoint2()[1], getPoint2()[2]);
	
		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);
		getGl().glVertex3d(getPoint2()[0], getPoint2()[1], getPoint2()[2]);
		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);

		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);
		getGl().glVertex3d(getPoint2()[0], getPoint2()[1], getPoint2()[2]);
		getGl().glVertex3d(getPoint3()[0], getPoint3()[1], getPoint3()[2]);

		getGl().glVertex3d(getInnerPoint2()[0], getInnerPoint2()[1], getInnerPoint2()[2]);
		getGl().glVertex3d(getPoint3()[0], getPoint3()[1], getPoint3()[2]);
		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);

		
		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);
		getGl().glVertex3d(getPoint3()[0], getPoint3()[1], getPoint3()[2]);
		getGl().glVertex3d(getPoint1()[0], getPoint1()[1], getPoint1()[2]);

		getGl().glVertex3d(getInnerPoint3()[0], getInnerPoint3()[1], getInnerPoint3()[2]);
		getGl().glVertex3d(getPoint1()[0], getPoint1()[1], getPoint1()[2]);
		getGl().glVertex3d(getInnerPoint1()[0], getInnerPoint1()[1], getInnerPoint1()[2]);

		
		//		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles
//		getGl().glColor3f(getColor()[0], getColor()[1], getColor()[2]);
//		getGl().glVertex3d(getTranslatedPoint1()[0], getTranslatedPoint1()[1], getTranslatedPoint1()[2]);
//		getGl().glVertex3d(getTranslatedPoint2()[0], getTranslatedPoint2()[1], getTranslatedPoint2()[2]);
//		getGl().glVertex3d(getTranslatedPoint3()[0], getTranslatedPoint3()[1], getTranslatedPoint3()[2]);

		getGl().glEnd();

	}

	// for placing the mass point of the polyhedron on the polyhedron's position
	// this is the reason for "-ds[i]" instead of +ds[i]
	public void updateOriginalPoints(double[] ds) {
		for (int i = 0; i < 3; i++) {
			point1[i] = (float) (getPoint1()[i] - ds[i]);
			point2[i] = (float) (getPoint2()[i] - ds[i]);
			point3[i] = (float) (getPoint3()[i] - ds[i]);
		}
	}

	public void updatePoints(double[] ds) {
		// TODO nullpointer afvangen?
		// if(getPolyhedron() != null) {
		for (int i = 0; i < 3; i++) {
			point1[i] = getPoint1()[i] + ds[i];
			point2[i] = getPoint2()[i] + ds[i];
			point3[i] = getPoint3()[i] + ds[i];
		}
		innerPoint1 = calculateInnerPoint(getPoint1());
		innerPoint2 = calculateInnerPoint(getPoint2());
		innerPoint3 = calculateInnerPoint(getPoint3());
		// }
	}

}
