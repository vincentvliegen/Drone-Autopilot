package DroneAutopilot.graphicalrepresentation;

/**
 * Data-aspect of the Triangles, containing the coordinates and the colors of the Triangle
 *
 */
public class TriangleAPData {
	
	
	//TODO floats?
	private double[] p1;
	private double[] p2;
	private double[] p3;
	private int innerColor;
	private int outerColor;
	private float[] innerPoint1;
	private float[] innerPoint2;
	private float[] innerPoint3;

	public TriangleAPData(double[] p1, double[] p2, double[] p3, int rgbInner, int rgbOuter) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		innerPoint1 = calculateInnerPoint(getPoint1());
		innerPoint2 = calculateInnerPoint(getPoint2());
		innerPoint3 = calculateInnerPoint(getPoint3());
		//TODO colors in which format, might be needed to adjust this
		this.innerColor = rgbInner;
		this.outerColor = rgbOuter;
		//eigenlijk afdwingen dat dit er maar 3 kunnen zijn
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
	
	/**
	 * 
	 * @return the inner color of the Triangle in RGB-format
	 */
	public int getInnerColor() {
		return innerColor;
	}
	
	/**
	 * 
	 * @return the outer color of the Triangle in RGB-format
	 */
	public int getOuterColor() {
		return outerColor;
	}
	
	public float[] getInnerPoint1() {
		return innerPoint1;
	}

	public float[] getInnerPoint2() {
		return innerPoint2;
	}

	public float[] getInnerPoint3() {
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


	private float[] calculateInnerPoint(double[] outerPoint) {
		return new float[] { (float) ((outerPoint[0] - getGravityX()) / Math.sqrt(2) + getGravityX()),
				(float) ((outerPoint[1] - getGravityY()) / Math.sqrt(2) + getGravityY()),
				(float) ((outerPoint[2] - getGravityZ()) / Math.sqrt(2) + getGravityZ()) };
	}

	public float[] getRgbInner() {
		return new float[]{(getInnerColor() >> 16) & 0xFF, (getInnerColor() >> 8) & 0xFF, getInnerColor() & 0xFF};
	}
	
	
	public float[] getRgbOuter() {
		return new float[]{(getOuterColor() >> 16) & 0xFF, (getOuterColor() >> 8) & 0xFF, getOuterColor() & 0xFF};
	}
	
	
}
