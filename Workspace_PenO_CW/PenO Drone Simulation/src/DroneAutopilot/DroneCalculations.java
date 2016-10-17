package DroneAutopilot;

import p_en_o_cw_2016.*;
import p_en_o_cw_2016.wireprotocol.TestbedStub;


public class DroneCalculations {
	
	public DroneCalculations(Drone drone){
		this.setDrone(drone);
		this.cameraSeparation = this.getDrone().getCameraSeparation();
		this.horizontalAngleOfLeftView = this.getDrone().getLeftCamera().getHorizontalAngleOfView();
		this.leftCameraWidth = this.getDrone().getLeftCamera().getWidth();
	}
	
	private void setDrone(Drone drone){
		this.drone = drone;
	}
	
	public Drone getDrone(){
		return this.drone;
	}
	
	private Drone drone;
	private float cameraSeparation;
	private float horizontalAngleOfLeftView;
	private float leftCameraWidth;
	
	public float getfocalDistance(){
		return (float) ((this.leftCameraWidth/2) / Math.tan(this.horizontalAngleOfLeftView/2));
	}
	
	public float getDepth(){
		return (this.cameraSeparation * this.getfocalDistance())/(this.getX1() - this.getX2());
	}

	//TODO zwaartepunt kunnen opvragen
	public float getX1(){
		//xafstand linker tussen zwaartepunt en middelpunt
	}
	
	//TODO zwaartepunt kunnen opvragen
	public float getX2(){
		//xafstand rechter tussen zwaartepunt en middelpunt
	}
	
	//TODO zwaartepunt kunnen opvragen
	public float getY1(){
		//yafstand zwaartepunt en middelpunt
	}
	
	
	public float horizontalAngleDeviation(){
		float x = (this.getDepth() * this.getX1()) / this.getfocalDistance();
		float tanAlfa = (x - this.cameraSeparation/2) / this.getDepth();
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(){
		return (float) Math.atan(this.getY1() / this.getfocalDistance());
	}


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
