package simulator.physics;

import java.util.*;

import simulator.objects.SimulationDrone;
import simulator.world.World;

public class Physics {
	private List<Force> forces = new ArrayList<>();
	private World world;

	public Physics(World world) {
		this.world = world;
	}

	public void addForce(Force newForce) {
		forces.add(newForce);
	}

	public void removeForce(Force newForce) {
		forces.remove(newForce);
	}

	public float[] getAcceleration(SimulationDrone drone) {
		float[] acceleration = new float[3];
		float xAcceleration = 0;
		float yAcceleration = 0;
		float zAcceleration = 0;
		
		Force thrustForce = calculateThrustForce(drone);
		Force gravity = new Force(0, drone.getGravity() * drone.getWeight(), 0);
		Force drag = new Force(-drone.getDrag()*drone.getMovement().getVelocity()[0], -drone.getDrag()*drone.getMovement().getVelocity()[1],-drone.getDrag()*drone.getMovement().getVelocity()[2]);
		forces.add(thrustForce);
		forces.add(gravity);
		forces.add(drag);
		for (Force currentForce: forces) {
			xAcceleration += currentForce.getXNewton();
			yAcceleration += currentForce.getYNewton();
			zAcceleration += currentForce.getZNewton();
		}
		forces.remove(thrustForce);
		forces.remove(gravity);
		forces.remove(drag);
		
		xAcceleration /= drone.getWeight();
		yAcceleration /= drone.getWeight();
		zAcceleration /= drone.getWeight();
		
		acceleration[0] = xAcceleration;
		acceleration[1] = yAcceleration;
		acceleration[2] = zAcceleration;
		return acceleration;
	}
	
	public Force calculateThrustForce(SimulationDrone drone) {
		drone.createRotateMatrix();
		float thrust = drone.getThrust();
		
		float forceX = (float) (thrust * drone.getRotateMatrix().get(1)); 
		float forceY = (float) (thrust * drone.getRotateMatrix().get(4));
		float forceZ = (float) (thrust * drone.getRotateMatrix().get(7));
		
		Force thrustForce = new Force(forceX, forceY, forceZ); 
		return thrustForce;
	}
	
	public void run(float timePassed) {
		for (SimulationDrone currentDrone: world.getDrones()) {
			float[] acceleration = getAcceleration(currentDrone);
			currentDrone.getMovement().calculateMovement(timePassed, acceleration);
		}
	}
	
	
}
