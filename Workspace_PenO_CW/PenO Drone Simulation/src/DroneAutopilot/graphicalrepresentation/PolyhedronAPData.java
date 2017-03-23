package DroneAutopilot.graphicalrepresentation;

import java.util.ArrayList;

public class PolyhedronAPData {

	
	private ArrayList<TriangleAPData> listOfTriangles = new ArrayList<>();
	
	public ArrayList<TriangleAPData> getListOfTriangles() {
		return listOfTriangles;
	}
	
	public void addTriangleToPolyhedron(TriangleAPData triangle) {
		//TODO if triangle not has same color!
		this.listOfTriangles.add(triangle);
	}
	

	
}
