package DroneAutopilot;

import implementedClasses.Drone;

public class PhysicsCalculations {
		
	public int getX1(Drone drone, int[] centerofGravityL){
		//xafstand linker tussen zwaartepunt en middelpunt
		int distance = centerofGravityL[0] - drone.getLeftCamera().getWidth()/2;
		return distance;
	}
	
	public int getX2(Drone drone, int[] centerofGravityR){
		//xafstand rechter tussen zwaartepunt en middelpunt
		int distance = centerofGravityR[0] - drone.getRightCamera().getWidth()/2;
		return distance;
	}
	
	public int getY(Drone drone, int[] centerofGravity){
		//yafstand zwaartepunt en middelpunt
		int distance = centerofGravity[1] - this.getCameraHeight(drone)/2;
		return distance;
	}
	
	public int getCameraHeight(Drone drone){
		int height =  (int) (Math.tan(drone.getLeftCamera().getVerticalAngleOfView())*this.getfocalDistance(drone)*2);
		return height;
	}
	
	public float getfocalDistance(Drone drone){
		return (float) ((drone.getLeftCamera().getWidth()/2) / Math.tan(drone.getLeftCamera().getHorizontalAngleOfView()/2));
	}
	
	public float getDepth(Drone drone, int[] centerOfGravityL, int[]centerOfGravityR){
		return (drone.getCameraSeparation() * this.getfocalDistance(drone))/(this.getX1(drone, centerOfGravityL) - this.getX2(drone, centerOfGravityR));
	}
		
	public float horizontalAngleDeviation(Drone drone, int[] centerOfGravityL, int[] centerofGravityR){
		float x = (this.getDepth(drone, centerOfGravityL, centerofGravityR) * this.getX1(drone, centerOfGravityL)) / this.getfocalDistance(drone);
		float tanAlfa = (x - drone.getCameraSeparation()/2) / this.getDepth(drone, centerOfGravityL, centerofGravityR);
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(Drone drone, int[] pointOfGravity){
		return (float) Math.atan(this.getY(drone, pointOfGravity) / this.getfocalDistance(drone));
	}
	
}
