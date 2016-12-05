
package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;

public class World11 extends World {

	private static final long serialVersionUID = 1L;

	public World11() {
		super();
		super.addGeneralCamera(new GeneralCamera(-2, 1, -1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(-2, 1, 1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(10, 0, 5, 10, 0, 0, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	@Override
	protected void setup() {
		GL2 gl = getGL().getGL2();

		double[] translateSphere = { 10f, 0, 0f };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, colorSphere, translateSphere);
		sphere1.draw();
		addSphere(sphere1);

		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .06f, .35f, .35f, colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();

	}

	@Override
	protected void handleCollision(WorldObject currentObject, SimulationDrone drone) {
		// Nothing happens with collision
		
	}

}
