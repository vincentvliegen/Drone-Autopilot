package DroneAutopilot.graphicalrepresentation.secondOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.scanning.Point;

public class PolyhedronAPDataNew {

	private HashSet<Point> points = new HashSet<>();
	private HashMap<CustomColor, ArrayList<Point>> colorPointsPairs = new HashMap<>();
	private HashMap<Integer, CustomColor> integerColors = new HashMap<>();
	private HashMap<Point, ArrayList<Edge>> pointsWithTheirEdges = new HashMap<>();
	private ArrayList<Edge> unfinishedEdges = new ArrayList<>();
	private boolean anEdgeWasAdded = false;
	
	
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
		this.integerColors.put(color.getColor(), color);
	}
	
	public HashMap<Integer, CustomColor> getIntegerColors() {
		return integerColors;
	}
	
	public HashMap<Point, ArrayList<Edge>> getPointsWithTheirEdges() {
		return pointsWithTheirEdges;
	}
	
	private void addEdgeToPoint(Point p, Edge e) {
		if(pointsWithTheirEdges.containsKey(p)) {
			pointsWithTheirEdges.get(p).add(e);
		}
		else{
			ArrayList<Edge> list = new ArrayList<Edge>();
			list.add(e);
			pointsWithTheirEdges.put(p, list);
		}
	}
	

//	private void addEdge(Point p1, Point p2, CustomColor c) {
//		if(pointsWithTheirEdges.containsKey(p1)) {
//			boolean hit = false;
//			for(Edge e: getPointsWithTheirEdges().get(p1)) {
//				if(e.consistsOfPoint(p2)) {
//					try {
//						e.addColor(c);
//						hit = true;
//						break;
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//				}
//			}
//			if(!hit) {
//				Edge e = new Edge(p1, p2, c);
//				
//			}
//		}
//		
//		else if(pointsWithTheirEdges.containsKey(p2)) {
//			
//		}
//
//	}
	
	public void addNewEdgeWithThesePoints(Point p1, Point p2, CustomColor c) {
		Edge e = new Edge(p1, p2, c);
		addEdgeToPoint(p1, e);
		addEdgeToPoint(p2, e);
		addUnfinishedEdge(e);
		this.anEdgeWasAdded = true;
	}
	
	private void addUnfinishedEdge(Edge e) {
		if(e.getColors().size() != 2) {
			this.unfinishedEdges.add(e);
		}
		this.anEdgeWasAdded = true;

	}
	
	public void removeFromUnfinishedEdges(Edge e) {
		if(e.getColors().size() == 2) {
			unfinishedEdges.remove(e);
		}
	}
	
	public ArrayList<Edge> getUnfinishedEdges() {
		return unfinishedEdges;
	}
	
	public boolean anEdgeWasAdded() {
		return anEdgeWasAdded;
	}
	



}
