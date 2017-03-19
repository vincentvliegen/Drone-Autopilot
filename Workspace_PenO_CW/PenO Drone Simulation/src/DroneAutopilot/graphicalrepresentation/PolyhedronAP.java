package DroneAutopilot.graphicalrepresentation;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class PolyhedronAP {
	
	public PolyhedronAP(GL2 gl) {
		this.gl = gl;
	}
	
	private ArrayList<TriangleAPVisual> listOfTriangles = new ArrayList<>();
	private GL2 gl;
	
	public ArrayList<TriangleAPVisual> getListOfTriangles() {
		return new ArrayList<>(listOfTriangles);
		//defensive way
	}
	
	public void addTriangleToPolyhedron(TriangleAPData triangle) {
		//TODO if triangle not has same color!
		this.listOfTriangles.add(new TriangleAPVisual(triangle, gl));
	}
	
	public void draw() {
		for(TriangleAPVisual triangle: listOfTriangles) {
			triangle.draw();
		}
	}

}
