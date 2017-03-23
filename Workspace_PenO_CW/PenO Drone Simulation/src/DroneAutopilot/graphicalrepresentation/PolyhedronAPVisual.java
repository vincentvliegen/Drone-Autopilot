package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL2;

public class PolyhedronAPVisual {
	
	public PolyhedronAPVisual(GL2 gl, PolyhedronAPData datapoly) {
		this.gl = gl;
		this.dataPoly = datapoly;
		triangleDrawer =  new TriangleAPDrawer(getGl());
	}
	
	TriangleAPDrawer triangleDrawer;
	PolyhedronAPData dataPoly;
	
	private GL2 gl;
	
	private GL2 getGl() {
		return gl;
	}
	
	private TriangleAPDrawer getTriangleDrawer() {
		return triangleDrawer;
	}
	
	private PolyhedronAPData getDataPoly() {
		return dataPoly;
	}
	
	
	public void draw() {
		for(TriangleAPData tri: getDataPoly().getListOfTriangles()) {
			getTriangleDrawer().draw(tri);
		}
		
	}

}
