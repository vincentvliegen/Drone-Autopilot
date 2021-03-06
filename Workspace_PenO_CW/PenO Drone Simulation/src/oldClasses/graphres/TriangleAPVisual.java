package oldClasses.graphres;

import com.jogamp.opengl.GL2;

/**
 * Visual part of the Triangles, containing a DataAPTriangle and GL2
 *
 */
public class TriangleAPVisual {

	private TriangleAPData dataTriangle;
	private GL2 gl;

	public TriangleAPVisual(TriangleAPData dataTriangle, GL2 gl) {
		this.dataTriangle = dataTriangle;
		this.gl = gl;

	}

	/**
	 * Draw the triangle using the data of the TriangleAPData-object
	 */
	public void draw() {

		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(getDataTriangle().getRgbInner()[0], getDataTriangle().getRgbInner()[1],
				getDataTriangle().getRgbInner()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint1()[0], getDataTriangle().getInnerPoint1()[1],
				getDataTriangle().getInnerPoint1()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint2()[0], getDataTriangle().getInnerPoint2()[1],
				getDataTriangle().getInnerPoint2()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint3()[0], getDataTriangle().getInnerPoint3()[1],
				getDataTriangle().getInnerPoint3()[2]);

		getGl().glEnd();

		getGl().glBegin(GL2.GL_TRIANGLES);

		getGl().glColor3f(getDataTriangle().getRgbOuter()[0], getDataTriangle().getRgbOuter()[1],
				getDataTriangle().getRgbOuter()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint1()[0], getDataTriangle().getInnerPoint1()[1],
				getDataTriangle().getInnerPoint1()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint1()[0], getDataTriangle().getPoint1()[1],
				getDataTriangle().getPoint1()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint2()[0], getDataTriangle().getPoint2()[1],
				getDataTriangle().getPoint2()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint1()[0], getDataTriangle().getInnerPoint1()[1],
				getDataTriangle().getInnerPoint1()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint2()[0], getDataTriangle().getPoint2()[1],
				getDataTriangle().getPoint2()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint2()[0], getDataTriangle().getInnerPoint2()[1],
				getDataTriangle().getInnerPoint2()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint2()[0], getDataTriangle().getInnerPoint2()[1],
				getDataTriangle().getInnerPoint2()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint2()[0], getDataTriangle().getPoint2()[1],
				getDataTriangle().getPoint2()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint3()[0], getDataTriangle().getPoint3()[1],
				getDataTriangle().getPoint3()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint2()[0], getDataTriangle().getInnerPoint2()[1],
				getDataTriangle().getInnerPoint2()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint3()[0], getDataTriangle().getPoint3()[1],
				getDataTriangle().getPoint3()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint3()[0], getDataTriangle().getInnerPoint3()[1],
				getDataTriangle().getInnerPoint3()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint3()[0], getDataTriangle().getInnerPoint3()[1],
				getDataTriangle().getInnerPoint3()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint3()[0], getDataTriangle().getPoint3()[1],
				getDataTriangle().getPoint3()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint1()[0], getDataTriangle().getPoint1()[1],
				getDataTriangle().getPoint1()[2]);

		getGl().glVertex3d(getDataTriangle().getInnerPoint3()[0], getDataTriangle().getInnerPoint3()[1],
				getDataTriangle().getInnerPoint3()[2]);
		getGl().glVertex3d(getDataTriangle().getPoint1()[0], getDataTriangle().getPoint1()[1],
				getDataTriangle().getPoint1()[2]);
		getGl().glVertex3d(getDataTriangle().getInnerPoint1()[0], getDataTriangle().getInnerPoint1()[1],
				getDataTriangle().getInnerPoint1()[2]);

		getGl().glEnd();

	}

	private GL2 getGl() {
		return this.gl;
	}

	private TriangleAPData getDataTriangle() {
		return this.dataTriangle;
	}

}
