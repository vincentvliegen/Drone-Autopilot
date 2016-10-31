package simulator.physics;

import java.util.*;

import simulator.objects.SimulationDrone;

public class Physics {
	private List<Force> forces = new ArrayList<>();
	private float weight;
	private SimulationDrone drone;

	public Physics(SimulationDrone simulationDrone, float weight) {
		Force gravity = new Force(0, -9.81f * weight, 0);
		forces.add(gravity);
		this.weight = weight;
		this.drone = simulationDrone;
	}

	public void addForce(Force newForce) {
		forces.add(newForce);
	}

	public void removeForce(Force newForce) {
		forces.remove(newForce);
	}

	public float[] getAcceleration() {
		float[] acceleration = new float[3];
		float xAcceleration = 0;
		float yAcceleration = 0;
		float zAcceleration = 0;
		
		Force thrustForce = calculateThrustForce();
		forces.add(thrustForce);
		for (Force currentForce: forces) {
			xAcceleration += currentForce.getXNewton();
			yAcceleration += currentForce.getYNewton();
			zAcceleration += currentForce.getZNewton();
		}
		forces.remove(thrustForce);
		
		xAcceleration /= (weight);
		yAcceleration /= (weight);
		zAcceleration /= (weight);
		
		acceleration[0] = xAcceleration;
		acceleration[1] = yAcceleration;
		acceleration[2] = zAcceleration;
		return acceleration;
	}
	
	public Force calculateThrustForce() {
		float pitch = drone.getPitch();
		float roll = drone.getRoll();
		float thrust = drone.getThrust();
		
		float forceX = (float) (thrust * (-Math.sin(Math.toRadians(pitch))) * Math.cos(Math.toRadians(roll)));
		float forceY = (float) (thrust * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(roll)));
		float forceZ = (float) (thrust * Math.sin(Math.toRadians(roll)));
		
		Force thrustForce = new Force(forceX, forceY, forceZ); 
		return thrustForce;
	}
}
