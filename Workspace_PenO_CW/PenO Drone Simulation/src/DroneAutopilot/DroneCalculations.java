package DroneAutopilot;

import p_en_o_cw_2016.*;
import p_en_o_cw_2016.wireprotocol.TestbedStub;
import java.util.ArrayList;
import java.util.List;

public class DroneCalculations {
	
	public DroneCalculations(Drone drone){
		this.setDrone(drone);
		this.cameraSeparation = drone.getCameraSeparation();
		this.horizontalAngleOfLeftView = drone.getLeftCamera().getHorizontalAngleOfView();
		this.leftCameraWidth = drone.getLeftCamera().getWidth();
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
	
	//TODO zwaartepunt kunnen opvragen
	public float getX1(){
		//xafstand linker tussen zwaartepunt en middelpunt
		return 0;
	}
	
	//TODO zwaartepunt kunnen opvragen
	public float getX2(){
		//xafstand rechter tussen zwaartepunt en middelpunt
		return 0;
	}
	
	//TODO zwaartepunt kunnen opvragen
	public float getY1(){
		//yafstand zwaartepunt en middelpunt
		return 0;
	}
	
	public float getfocalDistance(){
		return (float) ((this.leftCameraWidth/2) / Math.tan(this.horizontalAngleOfLeftView/2));
	}
	
	public float getDepth(){
		return (this.cameraSeparation * this.getfocalDistance())/(this.getX1() - this.getX2());
	}
		
	public float horizontalAngleDeviation(){
		float x = (this.getDepth() * this.getX1()) / this.getfocalDistance();
		float tanAlfa = (x - this.cameraSeparation/2) / this.getDepth();
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(){
		return (float) Math.atan(this.getY1() / this.getfocalDistance());
	}

	
	
	
	//TODO hoe worden pixels gegeven? store als int
	private int white;
	
	public int[] indexToCoordinates(int index){
		int x = (int) (index / leftCameraWidth);
		int y = (int) (index % leftCameraWidth);
		int[] coord = {x,y};
	    return coord;
	}
	
	public int coordinatesToIndex(int[] coordinates){
		int index = (int) (coordinates[0]+coordinates[1]*leftCameraWidth);
	    return index;
	}


	//TODO
	//zwaartepunt van groepje pixels bepalen
	public int[] getCenterOfGravity (int[][] listOfPixelCoordinates){
		return null;
		
	}
	
	//TODO 
	//algoritme dat heel de afbeelding afgaat en alle zwaartepunten/alle pixels van alle kleuren stockeert
	//dit werkt niet voor kleurenovergangen
	private void getAllPosColPixels(Camera camera){
		
	}
	
	//alle pixels of zwtp?
	private List<int[]> allColoredPixelsLeft = new ArrayList<int[]>();
	private List<int[]> allColoredPixelsRight = new ArrayList<int[]>();
	
	
	//TODO
	//algoritme uitwaartse spiraal met beginpositie
	//zoekt alle pixels af tot de gekleurde bol helemaal omvat wordt
	//zoekt in de buurt van de verwachtte positie
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
        List<int[]> colorList = new ArrayList<int[]>();
        for(int i = 0; image.length > i; i++){
            currentColor = image[coordinatesToIndex(currentPos)];
            if(currentColor == colorOrbToLookFor){//werkt niet voor kleurenovergangen
                colorList
            }
        }
        return null;		
	}
    
    //TODO calculate expected pos (adhv autopilot?)
}
