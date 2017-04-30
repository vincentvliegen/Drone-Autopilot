package DroneAutopilot.scanning;

import DroneAutopilot.graphicalrepresentation.CustomColor;

public class Point {
	
	private double x;
	private double y;
	private double z;
	private CustomColor color1;
	private CustomColor color2;
	private CustomColor color3;
	

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

	public CustomColor getColor1() {
		return color1;
	}

	public void setColor1(CustomColor color1) {
		if(this.color1 == null) {
			this.color1 = color1;
		}
	}

	public CustomColor getColor2() {
		return color2;
	}

	public void setColor2(CustomColor color2) {
		if(this.color2 == null) {
			this.color2 = color2;
		}
	}

	public CustomColor getColor3() {
		return color3;
	}

	public void setColor3(CustomColor color3) {
		if(this.color3 == null) {
			this.color3 = color3;
		}
	}
	
	public boolean matches(double[] p, double margin) {
		if(p.length != 3) {
			throw new IllegalArgumentException("The length of argument p should be 3, for x, y and z");
		}
 		return (Math.abs(p[0] - getX()) < margin && Math.abs(p[1] - getY()) < margin && Math.abs(p[2] - getZ()) < margin);
	}
	
	public boolean matches(Point p, double margin) {
		return (Math.abs(p.getX() - getX()) < margin && Math.abs(p.getY() - getY()) < margin && Math.abs(p.getZ() - getZ()) < margin);
	}

}
