package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL2;

/**
 * Visual part of the Triangles, containing a DataAPTriangle and GL2
 *
 */
public class TriangleAPDrawer {

	private GL2 gl;

	public TriangleAPDrawer(GL2 gl) {
				this.gl = gl;

	}

	/**
	 * Draw the triangle using the data of the TriangleAPData-object
	 */
	public void draw(TriangleAPData dataTriangle) {

		getGl().glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

		getGl().glColor3f(dataTriangle.getRgbInner()[0], dataTriangle.getRgbInner()[1],
				dataTriangle.getRgbInner()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
				dataTriangle.getInnerPoint1()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
				dataTriangle.getInnerPoint2()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
				dataTriangle.getInnerPoint3()[2]);

		getGl().glEnd();

		getGl().glBegin(GL2.GL_TRIANGLES);

		getGl().glColor3f(dataTriangle.getRgbOuter()[0], dataTriangle.getRgbOuter()[1],
				dataTriangle.getRgbOuter()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
				dataTriangle.getInnerPoint1()[2]);
		getGl().glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
				dataTriangle.getPoint1()[2]);
		getGl().glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
				dataTriangle.getPoint2()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
				dataTriangle.getInnerPoint1()[2]);
		getGl().glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
				dataTriangle.getPoint2()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
				dataTriangle.getInnerPoint2()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
				dataTriangle.getInnerPoint2()[2]);
		getGl().glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
				dataTriangle.getPoint2()[2]);
		getGl().glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
				dataTriangle.getPoint3()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
				dataTriangle.getInnerPoint2()[2]);
		getGl().glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
				dataTriangle.getPoint3()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
				dataTriangle.getInnerPoint3()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
				dataTriangle.getInnerPoint3()[2]);
		getGl().glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
				dataTriangle.getPoint3()[2]);
		getGl().glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
				dataTriangle.getPoint1()[2]);

		getGl().glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
				dataTriangle.getInnerPoint3()[2]);
		getGl().glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
				dataTriangle.getPoint1()[2]);
		getGl().glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
				dataTriangle.getInnerPoint1()[2]);

		getGl().glEnd();

	}

	private GL2 getGl() {
		return this.gl;
	}
}
