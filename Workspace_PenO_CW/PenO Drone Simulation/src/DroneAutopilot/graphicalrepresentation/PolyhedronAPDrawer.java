package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL2;

public class PolyhedronAPDrawer {
	
	public PolyhedronAPDrawer(GL2 gl) {
		this.gl = gl;
		triangleDrawer =  new TriangleAPDrawer(getGl());
	}
	
	static TriangleAPDrawer triangleDrawer;
	
	private GL2 gl;
	
	private GL2 getGl() {
		return gl;
	}
	
	private static TriangleAPDrawer getTriangleDrawer() {
		return triangleDrawer;
	}
		
	
	public static void draw(PolyhedronAPData datapoly) {
		for(TriangleAPData tri: datapoly.getListOfTriangles()) {
			getTriangleDrawer().draw(tri);
		}
		
	}

}
