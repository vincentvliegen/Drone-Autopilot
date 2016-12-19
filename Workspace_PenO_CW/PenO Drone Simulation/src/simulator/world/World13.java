package simulator.world;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;

import com.jogamp.opengl.*;

public class World13 extends World {
	public World13(){
		super();
		super.addGeneralCamera(new GeneralCamera(-2, 1, -1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(-2, 1, 1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(10, 0, 5, 10, 0, 0, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}
	
	@Override
	protected void setup() {
		GL2 gl = getGL().getGL2();
		
		double[] translateSphere = { 7f, 0, 0f };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, colorSphere, translateSphere, this);
		sphere1.draw();
		addSphere(sphere1);
		
		double[] translateSphere2 = { 14f, 2f, 2f };
		float[] colorSphere2 = { 0f, 2f, 1f };
		Sphere sphere2 = new Sphere(gl, colorSphere2, translateSphere2, this);
		sphere2.draw();
		addSphere(sphere2);
		
		double[] translateSphere3 = { -1f, -2f, 3f };
		float[] colorSphere3 = { 0f, 1f, 0f };
		Sphere sphere3 = new Sphere(gl, colorSphere3, translateSphere3, this);
		sphere3.draw();
		addSphere(sphere3);
		
		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .06f, .35f, .35f, colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();
	}

	@Override
	protected void handleCollision(WorldObject object, SimulationDrone drone) {
		if (object instanceof Sphere) {
			getWorldObjectList().remove(object);
			getSpheres().remove(object);
		} else{
			// Collision with something other than a sphere???
		}
	}

}
