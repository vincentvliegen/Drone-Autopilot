package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations {
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		timeDistanceList = new ArrayList<float[]>();
		float[] startTimeDistance = {0,0};
		setPreviousTimeDistance(startTimeDistance);
		setSpeed(0);
	}
	
	private static final float visibilityFactor = 0.8f;
	
	private static final float decelerationFactor = 0.4f;
	
	private static final float DecelerationDistance = 25f;

	private Drone drone;

	public float horizontalAngleDeviation(float[] centerOfGravityL, float[] centerOfGravityR){
		float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * Math.abs(this.getX1(centerOfGravityL))) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
		return (float) Math.toDegrees(Math.atan(tanAlfa));
	}
	
	public float verticalAngleDeviation(float[] centerOfGravity){
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}
	
	public void setDrone(Drone drone){
		this.drone = drone;
	}

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
		float distance = ((float) this.getCameraHeight())/2 - centerofGravity[1];
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

	public float getVisiblePitch(float[] centerOfGravityL, float[] centerOfGravityR){
		if (this.getDepth(centerOfGravityL, centerOfGravityR) <= getDecelerationDistance()){
			return  (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getDecelerationFactor());
		}
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getVisibilityFactor());
	}
	
	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta)));
		return thrust;
	}
	
	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		return distance;
	}
	
	public float calculateSpeed(float time, float distance){
		float[] newTD = {time,distance};
		timeDistanceList.add(newTD);
		if(timeDistanceList.size() >= avgcounter){
			float avgTime = 0;
			float avgDistance = 0;
			float size = timeDistanceList.size();
			for(int i = timeDistanceList.size()-1; i > 0; i--){
				float[] currentTD = timeDistanceList.get(i);
				avgTime += currentTD[0];
				avgDistance += currentTD[1];
				timeDistanceList.remove(i);
			}
			avgTime = avgTime/size;
			avgDistance = avgDistance/size;
			this.setSpeed((getPreviousTimeDistance()[1] - avgDistance)/(avgTime - getPreviousTimeDistance()[0]));
			this.setPreviousTimeDistance(new float[]{avgTime,avgDistance});
		}
		System.out.println(distance);
		System.out.println("speed: " + this.getSpeed());
		return this.getSpeed();
	}
	
	public Drone getDrone(){
		return this.drone;
	}

	public static final float getVisibilityFactor(){
		return PhysicsCalculations.visibilityFactor;
	}

	public static final float getDecelerationFactor(){
		return PhysicsCalculations.decelerationFactor;
	}

	public static float getDecelerationDistance() {
		return DecelerationDistance;
	}
	
	/**
	 * @return the timeDistanceList
	 */
	public ArrayList<float[]> getTimeDistanceList() {
		return timeDistanceList;
	}

	private ArrayList<float[]> timeDistanceList;
	
	/**
	 * @return the previousTimeDistance
	 */
	public float[] getPreviousTimeDistance() {
		return previousTimeDistance;
	}

	/**
	 * @param previousTimeDistance the previousTimeDistance to set
	 */
	public void setPreviousTimeDistance(float[] previousTimeDistance) {
		this.previousTimeDistance = previousTimeDistance;
	}
	
	private float[] previousTimeDistance;
	
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	private float speed;
	
	public final static int avgcounter = 7;
	
}
