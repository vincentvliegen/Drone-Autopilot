package simulator.world;

import com.jogamp.opengl.GLAutoDrawable;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.physics.Force;

public class World12 extends World11 {

	private static final long serialVersionUID = 1L;

	private Force windForce;
	private float windForceX = 0;
	private float windForceY = 0;
	private float windForceZ = 0;
	private static KeyboardMovement movement = new KeyboardMovement();

	public World12() {
		super();
		setCurrentCamera(getGeneralCameras().get(0));
	}

	public static KeyboardMovement getMovement() {
		return movement;
	}

	public void display(GLAutoDrawable drawable) {

		getWindForce().setXNewton(windForceX);
		getWindForce().setYNewton(windForceY);
		getWindForce().setZNewton(windForceZ);
		super.display(drawable);

	}

	public void setWindForceX(float value) {
		System.out.println(value);
		this.windForceX = value;
	}

	public void setWindForceY(float value) {
		this.windForceY = value;
	}

	public void setWindForceZ(float value) {
		this.windForceZ = value;
	}

	private Force getWindForce() {
		return windForce;
	}

	@Override
	protected void setup() {
		super.setup();
		windForce = new Force(0, 0, 0);
		physics.addForce(windForce);
	}
}
