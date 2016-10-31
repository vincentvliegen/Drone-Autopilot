package DroneAutopilot;

import DroneAutopilot.GUI.GUI;
import p_en_o_cw_2016.Drone;

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
	
	//GUI moet in andere functie geset worden, niet in deze getter. Maak functie die GUI updated in movetotarget class, hier irrelevant.
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
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*.8);		
	}
	
	public float getThrust(int[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		if (beta > 0){
			thrust = (float) ((this.getDrone().getGravity() * Math.cos(2*beta-this.getVisiblePitch())) / Math.cos(beta));
		}
		else{
			beta = Math.abs(beta);
			thrust = (float) ((this.getDrone().getGravity() * Math.cos(2*beta+this.getVisiblePitch())) / Math.cos(beta));
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
