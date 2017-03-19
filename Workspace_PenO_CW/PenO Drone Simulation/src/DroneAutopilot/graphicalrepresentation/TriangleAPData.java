package DroneAutopilot.graphicalrepresentation;

/**
 * Data-aspect of the Triangles, containing the coordinates and the colors of the Triangle
 *
 */
public class TriangleAPData {
	
	private double[] p1;
	private double[] p2;
	private double[] p3;
	private int[] rgbInner;
	private int[] rgbOuter;
	private double[] innerPoint1;
	private double[] innerPoint2;
	private double[] innerPoint3;

	public TriangleAPData(double[] p1, double[] p2, double[] p3, int[] rgbInner, int[] rgbOuter) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		innerPoint1 = calculateInnerPoint(getPoint1());
		innerPoint2 = calculateInnerPoint(getPoint2());
		innerPoint3 = calculateInnerPoint(getPoint3());
		//TODO colors in which format, might be needed to adjust this
		this.rgbInner = rgbInner;
		this.rgbOuter = rgbOuter;
	}

	public double[] getPoint1() {
		return p1;
	}
	
	public double[] getPoint2() {
		return p2;
	}
	
	public double[] getPoint3() {
		return p3;
	}
	
	public int[] getRgbInner() {
		return rgbInner;
	}
	
	public int[] getRgbOuter() {
		return rgbOuter;
	}
	
	public double[] getInnerPoint1() {
		return innerPoint1;
	}

	public double[] getInnerPoint2() {
		return innerPoint2;
	}

	public double[] getInnerPoint3() {
		return innerPoint3;
	}
	
	private double getGravityX() {
		return (getPoint1()[0] + getPoint2()[0] + getPoint3()[0]) / 3;

	}

	private double getGravityY() {
		return (getPoint1()[1] + getPoint2()[1] + getPoint3()[1]) / 3;

	}

	private double getGravityZ() {
		return (getPoint1()[2] + getPoint2()[2] + getPoint3()[2]) / 3;

	}


	private double[] calculateInnerPoint(double[] outerPoint) {
		return new double[] { (outerPoint[0] - getGravityX()) / Math.sqrt(2) + getGravityX(),
				(outerPoint[1] - getGravityY()) / Math.sqrt(2) + getGravityY(),
				(outerPoint[2] - getGravityZ()) / Math.sqrt(2) + getGravityZ() };
	}
	
	
}
