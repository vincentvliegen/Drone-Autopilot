package simulator.physics;

import java.util.*;

import simulator.objects.Drone;

public class Physics {
	private List<Force> forces = new ArrayList<>();
	private float weight;
	private Drone drone;

	public Physics(Drone drone, float weight) {
		Force gravity = new Force(0, -9.81f * weight, 0);
		forces.add(gravity);
		this.weight = weight;
		this.drone = drone;
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
		
		System.out.println(xAcceleration);
		System.out.println(yAcceleration);
		System.out.println(zAcceleration);
		
		xAcceleration /= (1000*weight);
		yAcceleration /= (1000*weight);
		zAcceleration /= (1000*weight);
		
		acceleration[0] = xAcceleration;
		acceleration[1] = yAcceleration;
		acceleration[2] = zAcceleration;
		return acceleration;
	}
	
	// Uses acceleration as velocity, completely wrong, should have time based value
	public void calculateMovement() {
		float[] acceleration = getAcceleration();
		drone.translateDrone(acceleration);
	}
	
	public Force calculateThrustForce() {
		float pitch = drone.getPitch();
		float roll = drone.getRoll();
		float thrust = drone.getThrust();
		
		float forceX = (float) (thrust * (-Math.sin(pitch)) * Math.cos(roll));
		float forceY = (float) (thrust * Math.cos(pitch) * Math.cos(roll));
		float forceZ = (float) (thrust * Math.sin(roll));
		
		Force thrustForce = new Force(forceX, forceY, forceZ); 
		return thrustForce;
	}
}
