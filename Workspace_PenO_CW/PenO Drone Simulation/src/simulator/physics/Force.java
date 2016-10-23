package simulator.physics;

public class Force {
	private float xNewton;
	private float yNewton;
	private float zNewton;
	
	public Force(float xNewton, float yNewton, float zNewton) {
		this.xNewton = xNewton;
		this.yNewton = yNewton;
		this.zNewton = zNewton;
	}
	
	public void setXNewton(float newX) {
		this.xNewton = newX;
	}
	
	public void setYNewton(float newY) {
		this.yNewton = newY;
	}
	
	public void setZNewton(float newZ) {
		this.zNewton = newZ;
	}
	
	public float getXNewton() {
		return this.xNewton;
	}
	
	public float getYNewton() {
		return this.yNewton;
	}
	
	public float getZNewton() {
		return this.zNewton;
	}
}
