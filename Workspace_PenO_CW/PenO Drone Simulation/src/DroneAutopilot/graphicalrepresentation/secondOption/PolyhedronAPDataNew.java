package DroneAutopilot.graphicalrepresentation.secondOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.scanning.Point;

public class PolyhedronAPDataNew {

	private HashSet<Point> points = new HashSet<>();
	private HashMap<CustomColor, ArrayList<Point>> colorPointsPairs = new HashMap<>();
	
	public HashSet<Point> getPoints() {
		return points;
	}
	
	public HashMap<CustomColor, ArrayList<Point>> getColorPointsPairs() {
		return colorPointsPairs;
	}
	
	
	public void addPoint(Point point) {
		this.points.add(point);
	}
	
	public void addColor_Point(CustomColor color, ArrayList<Point> points) {
		this.colorPointsPairs.put(color, points);
	}
	



}
