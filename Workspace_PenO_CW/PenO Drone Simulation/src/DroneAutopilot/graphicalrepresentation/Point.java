package DroneAutopilot.graphicalrepresentation;

import java.util.HashSet;

import DroneAutopilot.graphicalrepresentation.CustomColor;

public class Point {
	
	private double x;
	private double y;
	private double z;
	private HashSet<CustomColor> colors = new HashSet<>();
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}

	public boolean matches(double[] p, double margin) {
		if(p.length != 3) {
			throw new IllegalArgumentException("The length of argument p should be 3, for x, y and z");
		}
		double s = Math.sqrt(Math.pow((getX()-p[0]), 2) + Math.pow((getY()-p[1]), 2) + Math.pow((getZ()-p[2]), 2));
		return (s < margin);
	}
	
	public boolean matches(Point p, double margin) {
		double s = Math.sqrt(Math.pow((getX()-p.getX()), 2) + Math.pow((getY()-p.getY()), 2) + Math.pow((getZ()-p.getZ()), 2));
		return s < margin;
	}
	
	public HashSet<CustomColor> getColors() {
		return colors;
	}
	
	public void addColor(CustomColor c) {
			this.colors.add(c);
	}

}
