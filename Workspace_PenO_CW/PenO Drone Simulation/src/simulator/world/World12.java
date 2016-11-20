package simulator.world;

import com.jogamp.opengl.GLAutoDrawable;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.physics.Force;

public class World12 extends World11 {

	private static final long serialVersionUID = 1L;

	private Force windForce = new Force(0, 0, 0);
	private float windForceX = 0;
	private float windForceY = 0;
	private float windForceZ = 0;
	private static KeyboardMovement movement = new KeyboardMovement();

	public World12() {
		super();
		super.addGeneralCamera(new GeneralCamera(-1, 1, 2, 0, 0, -2.5f, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(1, 1, 2, 0, 0, -2.5f, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(3, 0, -8, 0, 0, -10, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	public static KeyboardMovement getMovement() {
		return movement;
	}

	public void display(GLAutoDrawable drawable) {

		// TODO wat is de juiste volgorde, if any?
		getWindForce().setXNewton(windForceX);
		getWindForce().setYNewton(windForceY);
		getWindForce().setZNewton(windForceZ);
		super.display(drawable);
		super.getPhysics().run((float) checkTimePassed());

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
}
