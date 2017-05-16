package oldClasses.graphres;

import java.util.HashMap;

public class PolyhedronAPData {

	private HashMap<Integer, TriangleAPData> listOfTriangles = new HashMap<Integer, TriangleAPData>();
	//	
	public HashMap<Integer, TriangleAPData> getListOfTriangles() {
		return listOfTriangles;
	}	
	public void addTriangleToPolyhedron(TriangleAPData triangle) {
		if(!listOfTriangles.containsKey(triangle.getOuterColor())) {
				this.listOfTriangles.put(triangle.getOuterColor(),triangle);
		}
	}



}
