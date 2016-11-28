package simulator.physics;

import simulator.objects.SimulationDrone;

public class Movement {
	private SimulationDrone drone;
	private float[] velocity = {0,0,0};
	public double[] currentPosition;
	

	public Movement(SimulationDrone drone) {
		this.drone = drone;
	}

	public void calculateMovement(float timePassed, float[] acceleration) {
		double[] currentPos = drone.getPosition();
		currentPos[0] += (velocity[0] * timePassed + acceleration[0]* timePassed* timePassed/ 2);
		currentPos[1] += (velocity[1] * timePassed + acceleration[1]* timePassed* timePassed/ 2);
		currentPos[2] += (velocity[2] * timePassed + acceleration[2]* timePassed* timePassed/ 2);
		
		velocity[0] += (acceleration[0] * timePassed);
		velocity[1] += (acceleration[1] * timePassed);
		velocity[2] += (acceleration[2] * timePassed);
		/*
		System.out.println("--------------");
		System.out.println("Total time passed " + drone.getWorld().getCurrentTime());
		System.out.println("Time " + timePassed);
		System.out.println("PosX " + currentPos[0]);
		System.out.println("PosY " + currentPos[1]);
		System.out.println("PosZ " + currentPos[2]);
		System.out.println("VelocityX " + velocity[0]);
		System.out.println("VelocityY " + velocity[1]);
		System.out.println("VelocityZ " + velocity[2]);
		System.out.println("AccelerationX " + acceleration[0]);
		System.out.println("AccelerationY " + acceleration[1]);
		System.out.println("AccelerationZ " + acceleration[2]);
		System.out.println("Thrust " + drone.getThrust());
		System.out.println("--------------");
		*/
		setCurrentPosition(currentPos);
		drone.translateDrone(currentPos);
	}

	public void setCurrentPosition(double[] currentPosition) {
		this.currentPosition = currentPosition;
	}

	public double[] getCurrentPosition() {
		return currentPosition;
	}

	public float[] getVelocity(){
		return velocity;
	}

	
}
