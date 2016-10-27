package DroneAutopilot;

import DroneAutopilot.GUI.GUI;
import implementedClasses.Drone;

public class PhysicsCalculations {
		
	public int getX1(int[] centerofGravityL){
		//xafstand linker tussen zwaartepunt en middelpunt
		int distance = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return distance;
	}
	
	public int getX2(int[] centerofGravityR){
		//xafstand rechter tussen zwaartepunt en middelpunt
		int distance = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return distance;
	}
	
	public int getY(int[] centerofGravity){
		//yafstand zwaartepunt en middelpunt
		int distance = centerofGravity[1] - this.getCameraHeight()/2;
		return distance;
	}
	
	public int getCameraHeight(){
		int height =  (int) (Math.tan(this.getDrone().getLeftCamera().getVerticalAngleOfView())*this.getfocalDistance()*2);
		return height;
	}
	
	public float getfocalDistance(){
		return (float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2));
	}
	
	public float getDepth(int[] centerOfGravityL, int[]centerOfGravityR){
		float depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		this.getGUI().update((int)depth);
		return depth;
	}
		
	public float horizontalAngleDeviation(int[] centerOfGravityL, int[] centerofGravityR){
		float x = (this.getDepth(centerOfGravityL, centerofGravityR) * this.getX1(centerOfGravityL)) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerofGravityR);
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(int[] pointOfGravity){
		return (float) Math.atan(this.getY(pointOfGravity) / this.getfocalDistance());
	}
	
	public float getVisiblePitch(){
		return (float) (this.getDrone().getLeftCamera().getVerticalAngleOfView()*.8);		
	}
	
	public float getThrust(int[] cog) {
		return (float) (this.getDrone().getGravity()/(Math.cos((this.getVisiblePitch()) - Math.tan(this.verticalAngleDeviation(cog))* Math.sin(this.getVisiblePitch()))));
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
