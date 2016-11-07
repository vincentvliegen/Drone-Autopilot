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
	
	public float[] getVelocity(){
		return velocity;
	}

	public Movement(SimulationDrone drone) {
		this.drone = drone;
	}

	public void calculateMovement(float timePassed, float[] acceleration) {
		double[] currentPos = drone.getTranslate();
		currentPos[0] += (velocity[0] * timePassed + acceleration[0]* timePassed* timePassed/ 2);
		currentPos[1] += (velocity[1] * timePassed + acceleration[1]* timePassed* timePassed/ 2);
		currentPos[2] += (velocity[2] * timePassed + acceleration[2]* timePassed* timePassed/ 2);
		
		velocity[0] += (acceleration[0] * timePassed);
		velocity[1] += (acceleration[1] * timePassed);
		velocity[2] += (acceleration[2] * timePassed);
	
		setCurrentPosition(currentPos);
		drone.translateDrone(currentPos);
	}

	
}
