package simulator.world;

import simulator.movement.KeyboardMovement;

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

}
