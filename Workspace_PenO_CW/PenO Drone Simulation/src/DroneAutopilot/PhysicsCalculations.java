package DroneAutopilot;

import implementedClasses.Drone;

public class PhysicsCalculations {
		
	public int getX1(Drone drone, int[] pointOfGravity){
		//xafstand linker tussen zwaartepunt en middelpunt
		int distance = pointOfGravity[0] - drone.getLeftCamera().getWidth()/2;
		return distance;
	}
	
	public int getX2(Drone drone, int[] pointOfGravity){
		//xafstand rechter tussen zwaartepunt en middelpunt
		int distance = pointOfGravity[0] - drone.getRightCamera().getWidth()/2;
		return distance;
	}
	
	public int getY(Drone drone, int[] pointOfGravity){
		//yafstand zwaartepunt en middelpunt
		int distance = pointOfGravity[1] - this.getCameraHeight(drone)/2;
		return distance;
	}
	
	public int getCameraHeight(Drone drone){
		int height =  (int) (Math.tan(drone.getLeftCamera().getVerticalAngleOfView())*this.getfocalDistance(drone)*2);
		return height;
	}
	
	public float getfocalDistance(Drone drone){
		return (float) ((drone.getLeftCamera().getWidth()/2) / Math.tan(drone.getLeftCamera().getHorizontalAngleOfView()/2));
	}
	
	public float getDepth(Drone drone, int[] pointOfGravity){
		return (drone.getCameraSeparation() * this.getfocalDistance(drone))/(this.getX1(drone, pointOfGravity) - this.getX2(drone, pointOfGravity));
	}
		
	public float horizontalAngleDeviation(Drone drone, int[] pointOfGravity){
		float x = (this.getDepth(drone, pointOfGravity) * this.getX1(drone, pointOfGravity)) / this.getfocalDistance(drone);
		float tanAlfa = (x - drone.getCameraSeparation()/2) / this.getDepth(drone, pointOfGravity);
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(Drone drone, int[] pointOfGravity){
		return (float) Math.atan(this.getY(drone, pointOfGravity) / this.getfocalDistance(drone));
	}
	
}
