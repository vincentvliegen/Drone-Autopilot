package DroneAutopilot;

import DroneAutopilot.GUI.GUI;
import p_en_o_cw_2016.Drone;

public class PhysicsCalculations {
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
	}
	
	public static final double getVisibilityFactor(){
		return PhysicsCalculations.visibilityFactor;
	}
	private static final double visibilityFactor = 0.8;

	public int getX1(int[] centerofGravityL){
		int distance = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return distance;
	}
	
	public int getX2(int[] centerofGravityR){
		int distance = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return distance;
	}
	
	/**
	 * The amount of pixels in height of the camera view.
	 */
	public int getCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getfocalDistance()*2);
		return height;
	}
	
	public int getY(int[] centerofGravity){
		int distance = centerofGravity[1] - this.getCameraHeight()/2;
		return distance;
	}
	
	public float getfocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}
	
	public float getDepth(int[] centerOfGravityL, int[]centerOfGravityR){
		float depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		return depth;
	}
		
	public float horizontalAngleDeviation(int[] centerOfGravityL, int[] centerOfGravityR){
		float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * Math.abs(this.getX1(centerOfGravityL))) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
		return (float) Math.toDegrees(Math.atan(tanAlfa));
	}
	
	public float verticalAngleDeviation(int[] centerOfGravity){
		//System.out.println(this.getY(pointOfGravity));
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}
	
	public float getVisiblePitch(){
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getVisibilityFactor());		
	}
	
	public float getThrust(int[] cog) {
		//ik denk dat de oorsprokelijke code fout is, aangezien ik voor pitch = 0, voor thrust het tegengestelde van de zwaartekracht krijg, terwijl thrust oneindig moet zijn, omdat je niet vooruit kan

//		float thrust;
//		float beta = this.verticalAngleDeviation(cog);
//		if (beta >= 0){
//			//System.out.println("beta groter dan of gelijk aan 0");
//			thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta)));
//			//System.out.println("thrust boven" + thrust);
//		}
//		else{
//			//System.out.println("beta kleiner dan 0");
//			beta = Math.abs(beta);
//			thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * Math.cos(Math.toRadians(beta + this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta)));
//			//System.out.println("thrust onder" + thrust);
//		}
		
		//dit is wat ik denk dat het moet zijn
		
		float beta = (float) Math.toRadians(this.verticalAngleDeviation(cog));
		float pitch = (float) Math.toRadians(this.getDrone().getPitch());//opgelet: positieve pitch is naar beneden gericht
		float delta = beta-pitch;//de hoek tussen het horizontaal vlak en de camera en de bol (= beta als de drone horizontaal zou hangen)
		float gravity = this.getDrone().getGravity();
		float weight = this.getDrone().getWeight();
		float thrust = (float) (weight*gravity/(Math.sin(pitch)*(Math.tan(delta)-Math.tan(pitch))));// zie tekening die ik doorstuur op fb
		return thrust;
	}
	
	public float getDistance(int[] centerOfGravityL, int[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance = /* depth/(cos(hor)*cos(ver)) */(float) (depth/(Math.cos(Math.toRadians(horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(verticalAngleDeviation(centerOfGravityL)))));
		this.getGUI().update((int)distance);//TODO verplaatsen naar MoveToTarget, GUI uit PhysicsCalculations halen
		return distance;
	}
	
	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}
	private Drone drone;
	
	
	public void setGUI(GUI gui){
		this.gui = gui;
	}
	public GUI getGUI(){
		return this.gui;
	}
	private GUI gui;
}
