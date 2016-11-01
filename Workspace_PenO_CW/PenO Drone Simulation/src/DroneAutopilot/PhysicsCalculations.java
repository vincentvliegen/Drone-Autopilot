package DroneAutopilot;

import DroneAutopilot.GUI.GUI;
import p_en_o_cw_2016.Drone;

public class PhysicsCalculations {
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
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
	
	/*
	 * The amount of pixels in height of the camera view.
	 */
	public int getCameraHeight(){
		int height =  (int) (Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()))*this.getfocalDistance()*2);
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
		this.getGUI().update((int)depth);
		return depth;
	}
		
	public float horizontalAngleDeviation(int[] centerOfGravityL, int[] centerofGravityR){
		float x = (this.getDepth(centerOfGravityL, centerofGravityR) * Math.abs(this.getX1(centerOfGravityL))) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerofGravityR);
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(int[] pointOfGravity){
		//System.out.println(this.getY(pointOfGravity));
		return (float) Math.atan(this.getY(pointOfGravity) / this.getfocalDistance());
	}
	
	public float getVisiblePitch(){
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*visibilityFactor);		
	}
	
	public float getThrust(int[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		if (beta > 0){
			System.out.println("beta groter dan 0");
			thrust = (float) ((this.getDrone().getGravity() * Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta))));
		}
		else{
			//System.out.println("beta kleiner");
			beta = Math.abs(beta);
			thrust = (float) ((this.getDrone().getGravity() * Math.cos(Math.toRadians(beta + this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta))));
		}
		return thrust;
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
