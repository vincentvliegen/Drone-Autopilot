package DroneAutopilot.graphicalrepresentation;

public class CustomColor {
	
	private final int color;
	private final int innerColor;

	public CustomColor(int color, int innerColor) {
		this.color = color;
		this.innerColor = innerColor;
	}
	
	@Override
	public int hashCode() {
		return getColor();
	}
	
	public final int getColor() {
		return color;
	}
	
	public final int getInnerColor() {
		return innerColor;
	}

}
