package simulator.physics;

import java.util.*;
import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.world.World;

public class Movement {
	private SimulationDrone drone;

	public Movement(SimulationDrone drone) {
		this.drone = drone;
	}

	// Uses acceleration as velocity, completely wrong, should have time based
	// value
	public void calculateMovement() {
		float[] acceleration = drone.getPhysics().getAcceleration();
		translateObjects(acceleration);
	}

	//Implement delta
	private void translateObjects(float[] acceleration) {
		World currentWorld = drone.getWorld();
		List<SimulationDrone> drones = currentWorld.getDrones();
		List<Sphere> spheres = currentWorld.getSpheres();
		List<GeneralCamera> cameras = currentWorld.getGeneralCameras();
	
		for (SimulationDrone currentDrone : drones) {
			// Implementation postponed
		}
		
		for (Sphere currentSphere : spheres) {
			currentSphere.translateSphere(acceleration);
		}
		
		for (GeneralCamera currentCamera : cameras) {
			//currentCamera.translateCamera(acceleration);
		}
			
		
	}

	
}
