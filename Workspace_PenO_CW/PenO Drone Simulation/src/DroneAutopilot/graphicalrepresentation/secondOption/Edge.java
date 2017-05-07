package DroneAutopilot.graphicalrepresentation.secondOption;

import java.util.ArrayList;
import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.scanning.Point;

public class Edge {

	private final Point endPoint1;
	private final Point endPoint2;
	private ArrayList<CustomColor> colors = new ArrayList<>(2);

	/**
	 * Edge, consisting of two Points and at least one color at the time the edge is created.
	 */
	public Edge(Point p1, Point p2, CustomColor c1) {
		this.endPoint1 = p1;
		this.endPoint2 = p2;
		colors.add(c1);
	}
	
	public Point getEndPoint1() {
		return endPoint1;
	}
	
	public Point getEndPoint2() {
		return endPoint2;
	}
	
	public void addColor(CustomColor c) throws Exception {
		if(getColors().size() > 2) {
			throw new Exception("Illegal operation. An edge cannot have more than two CustomColors.");
		}
		else {
			colors.add(c);
		}
	}
	
	public ArrayList<CustomColor> getColors() {
		return new ArrayList<>(colors);
	}
	
	public boolean containsColor(CustomColor c) {
		return getColors().contains(c);
	}
	
	public boolean consistsOfPoint(Point p) {
		return (getEndPoint1() == p || getEndPoint2() == p);
	}
	
}
