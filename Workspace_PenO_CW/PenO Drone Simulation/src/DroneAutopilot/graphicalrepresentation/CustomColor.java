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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Integer) {
			return (int) obj == color;
		}
		else if (obj instanceof CustomColor) {
			System.out.println(((CustomColor)obj).getColor());
			System.out.println(getColor());
			System.out.println(((CustomColor) obj).getColor() == getColor());
			return ((CustomColor) obj).getColor() == getColor();
		}
		return false;

	}
	
	public final int getColor() {
		return color;
	}
	
	public final int getInnerColor() {
		return innerColor;
	}

}
