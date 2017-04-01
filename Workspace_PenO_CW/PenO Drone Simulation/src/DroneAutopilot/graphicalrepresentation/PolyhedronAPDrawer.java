package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL2;

public class PolyhedronAPDrawer {
	
	public PolyhedronAPDrawer() {
	}
	
	static TriangleAPDrawer triangleDrawer =  new TriangleAPDrawer();

	
	
	private static TriangleAPDrawer getTriangleDrawer() {
		return triangleDrawer;
	}
		
	
	public static void draw(PolyhedronAPData datapoly, GL2 gl) {
		for(Integer key: datapoly.getListOfTriangles().keySet()) {
			getTriangleDrawer().draw(datapoly.getListOfTriangles().get(key), gl);
			
		}
		
	}

}
