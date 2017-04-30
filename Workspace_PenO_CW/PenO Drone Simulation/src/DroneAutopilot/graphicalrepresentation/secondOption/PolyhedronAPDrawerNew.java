package DroneAutopilot.graphicalrepresentation.secondOption;

import com.jogamp.opengl.GL2;

import DroneAutopilot.graphicalrepresentation.PolyhedronAPData;
import DroneAutopilot.graphicalrepresentation.TriangleAPDrawer;

public class PolyhedronAPDrawerNew {
	
	public PolyhedronAPDrawerNew() {
	}
	
	static TriangleAPDrawer triangleDrawer =  new TriangleAPDrawer();

	
	
	private static TriangleAPDrawer getTriangleDrawer() {
		return triangleDrawer;
	}
		
	
	public static void draw(PolyhedronAPDataNew poly, GL2 gl) {
//		for(Integer key: poly.getListOfTriangles().keySet()) {
//			getTriangleDrawer().draw(poly.getListOfTriangles().get(key), gl);
//			
//		}
		//TODO
		
	}

}
