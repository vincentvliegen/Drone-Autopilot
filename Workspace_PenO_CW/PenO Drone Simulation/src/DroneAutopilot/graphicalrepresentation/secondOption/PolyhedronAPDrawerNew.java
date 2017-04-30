package DroneAutopilot.graphicalrepresentation.secondOption;

import com.jogamp.opengl.GL2;

import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.graphicalrepresentation.TriangleAPDrawer;

public class PolyhedronAPDrawerNew {
	
	public PolyhedronAPDrawerNew() {
	}
	
		
	
	public static void draw(PolyhedronAPDataNew poly, GL2 gl) {
//		for(Integer key: poly.getListOfTriangles().keySet()) {
//			getTriangleDrawer().draw(poly.getListOfTriangles().get(key), gl);
//			
//		}
		for(CustomColor key: poly.getColorPointsPairs().keySet()) {
//			gl.glBegin(GL2.GL_TRIANGLES); // Drawing Using Triangles
//
//			gl.glColor3f(((dataTriangle.getInnerColor() >> 16) & 0xFF)/255f, ((dataTriangle.getInnerColor() >> 8) & 0xFF)/255f,
//					(dataTriangle.getInnerColor() & 0xFF)/255f);
//			gl.glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
//					dataTriangle.getInnerPoint1()[2]);
//			gl.glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
//					dataTriangle.getInnerPoint2()[2]);
//			gl.glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
//					dataTriangle.getInnerPoint3()[2]);
//
//			gl.glEnd();
//
//			gl.glBegin(GL2.GL_TRIANGLES);
//
//			gl.glColor3f(((dataTriangle.getOuterColor() >> 16) & 0xFF)/255f, ((dataTriangle.getOuterColor() >> 8) & 0xFF)/255f,
//					(dataTriangle.getOuterColor() & 0xFF)/255f);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
//					dataTriangle.getInnerPoint1()[2]);
//			gl.glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
//					dataTriangle.getPoint1()[2]);
//			gl.glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
//					dataTriangle.getPoint2()[2]);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
//					dataTriangle.getInnerPoint1()[2]);
//			gl.glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
//					dataTriangle.getPoint2()[2]);
//			gl.glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
//					dataTriangle.getInnerPoint2()[2]);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
//					dataTriangle.getInnerPoint2()[2]);
//			gl.glVertex3d(dataTriangle.getPoint2()[0], dataTriangle.getPoint2()[1],
//					dataTriangle.getPoint2()[2]);
//			gl.glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
//					dataTriangle.getPoint3()[2]);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint2()[0], dataTriangle.getInnerPoint2()[1],
//					dataTriangle.getInnerPoint2()[2]);
//			gl.glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
//					dataTriangle.getPoint3()[2]);
//			gl.glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
//					dataTriangle.getInnerPoint3()[2]);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
//					dataTriangle.getInnerPoint3()[2]);
//			gl.glVertex3d(dataTriangle.getPoint3()[0], dataTriangle.getPoint3()[1],
//					dataTriangle.getPoint3()[2]);
//			gl.glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
//					dataTriangle.getPoint1()[2]);
//
//			gl.glVertex3d(dataTriangle.getInnerPoint3()[0], dataTriangle.getInnerPoint3()[1],
//					dataTriangle.getInnerPoint3()[2]);
//			gl.glVertex3d(dataTriangle.getPoint1()[0], dataTriangle.getPoint1()[1],
//					dataTriangle.getPoint1()[2]);
//			gl.glVertex3d(dataTriangle.getInnerPoint1()[0], dataTriangle.getInnerPoint1()[1],
//					dataTriangle.getInnerPoint1()[2]);
//
//			gl.glEnd();

			
		}
		
	}

}
