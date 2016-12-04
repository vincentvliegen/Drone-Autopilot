package DroneAutopilot;

import p_en_o_cw_2016.Drone;

public class AvoidObstacles {

	private Drone drone;

	public AvoidObstacles(Drone drone){
 		this.setDrone(drone);
 	}
 	
	public void execute(){
		// Grijze tint testen (alle rgb gelijk)
		// Opwaarts adhv vergeleggen doelpunt vd controller
		// Erover vliegen op straal vd bol + 2kr de drone? (veiligheidmarge)
	}
	
	
	//////////Getters & Setters//////////
 	
 	public Drone getDrone() {
 		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}
}