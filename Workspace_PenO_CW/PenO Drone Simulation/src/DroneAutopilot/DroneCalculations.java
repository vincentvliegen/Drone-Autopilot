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

//TODO hoe worden pixels gegeven?
private int white;

//TODO
public int[] indexToCoordinates(int index){
	return null;
}

//TODO
public int coordinatesToIndex(int[] coordinates){
	return 0;
}

//TODO
//algoritme uitwaartse spiraal met beginpositie
//zoekt een pixel in de buurt van de verwachtte positie
	public int[] getPosColoredOrb(int[] expectedPos, Camera camera, int colorOrbToLookFor){
		Camera currentCamera = camera;
		int[] currentPos = expectedPos;
		int[] image = currentCamera.takeImage();
		int width =  currentCamera.getWidth();
		int columnCounter = 1;
		int rowCounter = 1;
		int height = image.length/width;
		int orbColor = colorOrbToLookFor;
		int currentColor;
		for(int i = 0; image.length > i; i++){
			currentColor = image[coordinatesToIndex(currentPos)];
			if(currentColor == colorOrbToLookFor){
				
			}
		}
		return null;
	}
	
	
}
