package DroneAutopilot.graphicalrepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import DroneAutopilot.graphicalrepresentation.CustomColor;
import DroneAutopilot.graphicalrepresentation.Point;

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
	public void mergePoints(double margin) {
		HashSet<Point> done = new HashSet<>();
		HashSet<Point> toRemove = new HashSet<>(); //TODO
		for(Point p: getPoints()) {
			done.add(p);
			for(Point p2 : getPoints()) {
				if(!done.contains(p2)) {

					if(p.matches(p2, margin)) {
						handleMerge(p, p2);
//						System.out.println("p:");
//						System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
//						System.out.println(p2.getX() + " " + p2.getY() + " " + p2.getZ());
						toRemove.add(p2);
					}
				}
			}
		}
		points.removeAll(toRemove);
	}

	
	private void handleMerge(Point p, Point p2) {
		
		//p2 is the victim
		p.setX((p.getX() + p2.getX())/2);
		p.setY((p.getY() + p2.getY())/2);
		p.setZ((p.getZ() + p2.getZ())/2);

		for(CustomColor c: p2.getColors()) {
			//TODO if hoeft niet normaal gezien
//			if(! p.getColorsInt().contains(c.getColor())) {
//				p.addColor(c);
//			}
//			else {
//				System.out.println("dit kan niet (polyapdata)");
//			}
			p.addColor(c);
			getColorPointsPairs().get(c).remove(p2);
			getColorPointsPairs().get(c).add(p);
		}
		
		for(Edge e: getPointsWithTheirEdges().get(p2)) {
			if(e.getEndPoint1() == p2) {
				e.setEndPoint1(p);
				break;
			}
			else if(e.getEndPoint2() == p2) {
				e.setEndPoint2(p2);
				break;
			}
			

		}
	}
	



}
