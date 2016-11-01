package simulator.physics;

import java.util.*;
import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.world.World;

public class Movement {
	private SimulationDrone drone;
	private float[] velocity = {0,0,0};
	public double[] currentPosition;
	

	public double[] getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double[] currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Movement(SimulationDrone drone) {
		this.drone = drone;
	}

	public void calculateMovement(float timePassed) {
		if (timePassed < 0.016666) {
			return;
		}
		float[] acceleration = drone.getPhysics().getAcceleration();
		acceleration[0] *= timePassed;
		acceleration[1] *= timePassed;
		acceleration[2] *= timePassed;
		velocity[0] += acceleration[0];
		velocity[1] += acceleration[1];
		velocity[2] += acceleration[2];
		velocity[0] -= drone.getDrag()*velocity[0];
		velocity[1] -= drone.getDrag()*velocity[1];
		velocity[2] -= drone.getDrag()*velocity[2];
		
		double[] currentPos = drone.getTranslate();
		currentPos[0] += velocity[0] * timePassed;
		currentPos[1] += velocity[1] * timePassed;
		currentPos[2] += velocity[2] * timePassed;
		setCurrentPosition(currentPos);
		drone.translateDrone(currentPos);
	}

	
}
