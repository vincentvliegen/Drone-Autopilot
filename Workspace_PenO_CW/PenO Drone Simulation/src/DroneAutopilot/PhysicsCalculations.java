package DroneAutopilot;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations {
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
	}
	
	public static final double getVisibilityFactor(){
		return PhysicsCalculations.visibilityFactor;
	}
	private static final double visibilityFactor = 0.8;
	
	public static final double getDecelerationFactor(){
		return PhysicsCalculations.decelerationFactor;
	}
	private static final double decelerationFactor = 0.3;
	
	public static float getDecelerationDistance() {
		return DecelerationDistance;
	}
	private static final float DecelerationDistance = 2;

	public float getX1(float[] centerofGravityL){
		float distance = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return distance;
	}
	
	public float getX2(float[] centerofGravityR){
		float distance = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return distance;
	}
	
	/**
	 * The amount of pixels in height of the camera view.
	 */
	public int getCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getfocalDistance()*2);
		return height;
	}
	
	public float getY(float[] centerofGravity){
		float distance = centerofGravity[1] - this.getCameraHeight()/2;
		return distance;
	}
	
	public float getfocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}
	
	public float getDepth(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth=0;
		try{
		depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		} catch(IllegalArgumentException e){
		}
		return depth;
	}
		
	public float horizontalAngleDeviation(float[] centerOfGravityL, float[] centerOfGravityR){
		float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * Math.abs(this.getX1(centerOfGravityL))) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
		return (float) Math.toDegrees(Math.atan(tanAlfa));
	}
	
	public float verticalAngleDeviation(float[] centerOfGravity){
		//System.out.println(this.getY(pointOfGravity));
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}
	
	public float getVisiblePitch(float[] centerOfGravityL, float[] centerOfGravityR){
		if (this.getDepth(centerOfGravityL, centerOfGravityR) <= this.getDecelerationDistance()){
			return  (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getDecelerationFactor());
		}
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getVisibilityFactor());
	}
	
	public float getThrust(float[] cog) {//TODO kies welke versie
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		//System.out.println("beta groter dan of gelijk aan 0");
		thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta)));
		//System.out.println("thrust boven" + thrust);
		return thrust;
	}
	
	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance = /* depth/(cos(hor)*cos(ver)) */(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		return distance;
	}
	
	
	public void setDrone(Drone drone){
		this.drone = drone;
	}
	public Drone getDrone(){
		return this.drone;
	}

	private Drone drone;
	
	
	
}
