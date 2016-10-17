package DroneAutopilot;

import p_en_o_cw_2016.*;
import p_en_o_cw_2016.wireprotocol.TestbedStub;


public class DroneCalculations {
	
	protected DroneCalculations(Drone drone){
		this.setDrone(drone);
	}
	
	public void depthcalculation(){
		int cameraDistance = TestbedStub.getCameraSeparation();
		
	}

private void setDrone(Drone drone){
	this.drone = drone;
}

public Drone getDrone(){
	return this.drone;
}

private Drone drone;





//TODO deftige documentatie, Engels
//TODO enum red pixel/ andere manier van aanduiden, nu als int
//TODO
//algoritme uitwaartse spiraal met beginpositie
//zoekt een rode pixel in de buurt van de verwachtte positie
	public int getNearestPosRedPix(int expectedPos, Camera camera){
		Camera camera = camera; 
		int width =  getWidth();
		int currentPos = expectedPos;
		int columnCounter = 1;
		int rowCounter = 1;
		int height = image.length/width;
		

	}
}
