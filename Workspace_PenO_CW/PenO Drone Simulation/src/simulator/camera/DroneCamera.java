package simulator.camera;

import simulator.objects.SimulationDrone;

public class DroneCamera extends GeneralCamera{
	
	public DroneCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, SimulationDrone drone){
		super(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ);
		this.setDrone(drone);
	}
	

	private SimulationDrone drone;
	
	
	public SimulationDrone getDrone() {
		return drone;
	}

	public void setDrone(SimulationDrone drone) {
		this.drone = drone;
	}

	
	
	
	

}
