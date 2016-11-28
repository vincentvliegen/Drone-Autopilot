package simulator.world;

import com.jogamp.opengl.GLAutoDrawable;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.physics.Force;

public class World12 extends World11 {

	private static final long serialVersionUID = 1L;
	private static KeyboardMovement movement = new KeyboardMovement();

	public World12() {
		super();
		setCurrentCamera(getGeneralCameras().get(0));
	}

	public static KeyboardMovement getMovement() {
		return movement;
	}

	public void display(GLAutoDrawable drawable) {
		super.display(drawable);

	}

	@Override
	protected void setup() {
		super.setup();
	}
}
