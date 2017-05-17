package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL2;

import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.graphicalrepresentation.Point;

public class PolyhedronAPDrawerNew {

	public PolyhedronAPDrawerNew() {
	}

	private static double[] calculateInnerPoint(Point targetPoint, Point p1, Point p2, Point p3) {
		return new double[] { (targetPoint.getX() - getGravityX(p1, p2, p3)) / Math.sqrt(2) + getGravityX(p1, p2, p3),
				(targetPoint.getY() - getGravityY(p1, p2, p3)) / Math.sqrt(2) + getGravityY(p1, p2, p3),
				(targetPoint.getZ() - getGravityZ(p1, p2, p3)) / Math.sqrt(2) + getGravityZ(p1, p2, p3) };
	}

	private static double getGravityX(Point p1, Point p2, Point p3) {
		return (p1.getX() + p2.getX() + p3.getX()) / 3;

	}

	private static double getGravityY(Point p1, Point p2, Point p3) {
		return (p1.getY() + p2.getY() + p3.getY()) / 3;

	}

	private static double getGravityZ(Point p1, Point p2, Point p3) {
		return (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

	}

	public static void draw(PolyhedronAPDataNew poly, GL2 gl) {
		// for(Integer key: poly.getListOfTriangles().keySet()) {
		// getTriangleDrawer().draw(poly.getListOfTriangles().get(key), gl);
		//
		// }
		for (CustomColor key : poly.getColorPointsPairs().keySet()) {
			Point p1 = poly.getColorPointsPairs().get(key).get(0);
			Point p2 = poly.getColorPointsPairs().get(key).get(1);
			Point p3 = poly.getColorPointsPairs().get(key).get(2);
			double[] innerPoint1 = calculateInnerPoint(p1, p1, p2, p3);
			double[] innerPoint2 = calculateInnerPoint(p2, p1, p2, p3);
			double[] innerPoint3 = calculateInnerPoint(p3, p1, p2, p3);
			double[][] innerPoints = new double[][] { innerPoint1, innerPoint2, innerPoint3 };

			gl.glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles

			gl.glColor3f(((key.getInnerColor() >> 16) & 0xFF) / 255f, ((key.getInnerColor() >> 8) & 0xFF) / 255f,
					(key.getInnerColor() & 0xFF) / 255f);

			for (double[] d : innerPoints) {
				gl.glVertex3d(d[0], d[1], d[2]);
			}
			gl.glEnd();

			gl.glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles
			gl.glColor3f(((key.getColor() >> 16) & 0xFF) / 255f, ((key.getColor() >> 8) & 0xFF) / 255f,
					(key.getColor() & 0xFF) / 255f);

			gl.glVertex3d(innerPoint1[0], innerPoint1[1], innerPoint1[2]);
			gl.glVertex3d(p1.getX(), p1.getY(), p1.getZ());
			gl.glVertex3d(p2.getX(), p2.getY(), p2.getZ());

			gl.glVertex3d(innerPoint1[0], innerPoint1[1], innerPoint1[2]);
			gl.glVertex3d(p2.getX(), p2.getY(), p2.getZ());
			gl.glVertex3d(innerPoint2[0], innerPoint2[1], innerPoint2[2]);

			gl.glVertex3d(innerPoint2[0], innerPoint2[1], innerPoint2[2]);
			gl.glVertex3d(p2.getX(), p2.getY(), p2.getZ());
			gl.glVertex3d(p3.getX(), p3.getY(), p3.getZ());

			gl.glVertex3d(innerPoint2[0], innerPoint2[1], innerPoint2[2]);
			gl.glVertex3d(p3.getX(), p3.getY(), p3.getZ());
			gl.glVertex3d(innerPoint3[0], innerPoint3[1], innerPoint3[2]);

			gl.glVertex3d(innerPoint3[0], innerPoint3[1], innerPoint3[2]);
			gl.glVertex3d(p3.getX(), p3.getY(), p3.getZ());
			gl.glVertex3d(p1.getX(), p1.getY(), p1.getZ());

			gl.glVertex3d(innerPoint3[0], innerPoint3[1], innerPoint3[2]);
			gl.glVertex3d(p1.getX(), p1.getY(), p1.getZ());
			gl.glVertex3d(innerPoint1[0], innerPoint1[1], innerPoint1[2]);

			gl.glEnd();

		}

	}

}
