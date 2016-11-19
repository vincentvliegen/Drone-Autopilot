package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations_new {

	private Drone drone;
	private float speed;
	private float acceleration;
	private float previousSpeed;
	private float previousDistance;
	private float previousTimeDev;
	private float firstDistance;
	private float doneDistance;
	private float totalAcceleration;
	private float totalSpeed;
	private boolean firstTime;


	public PhysicsCalculations_new(Drone drone){
		this.setDrone(drone);
		setSpeed(0);
	}

	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		if(!firstTime){
			firstDistance = distance;
			firstTime=true;
		}
		return distance;
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

	public float getfocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}

	public float getX1(float[] centerofGravityL){
		float x1 = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return x1;
	}

	public float getX2(float[] centerofGravityR){
		float x2 = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return x2;
	}

	public float horizontalAngleDeviation(float[] centerOfGravityL, float[] centerOfGravityR){
		float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * this.getX1(centerOfGravityL)) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
		return (float) Math.toDegrees(Math.atan(tanAlfa));
	}

	public float verticalAngleDeviation(float[] centerOfGravity){
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}

	public float calculateSpeed(float[] centerOfGravityL, float[] centerOfGravityR, float startTime){
		float timeDev=this.getDrone().getCurrentTime()-startTime; 
		float timeCalculation = this.getDrone().getCurrentTime()-startTime-this.getPreviousTimeDev();
		float acceleration = calculateAcceleration(centerOfGravityL, centerOfGravityR, startTime);
		this.setPreviousTimeDev(timeDev);
		float speed = acceleration * timeCalculation + this.getPreviousSpeed();
		this.setPreviousSpeed(speed);
		this.setSpeed(speed);
		return speed;
	}

	public float calculateAcceleration(float[] centerOfGravityL, float[] centerOfGravityR, float startTime){
		float timeCalculation = this.getDrone().getCurrentTime()-startTime-this.getPreviousTimeDev();
		float distanceDev=this.getPreviousDistance()-this.getDistance(centerOfGravityL, centerOfGravityR);
		float acceleration = this.getAcceleration();
		if(timeCalculation!=0){
			acceleration=(float) (2*(distanceDev-(this.getPreviousSpeed()*timeCalculation))/Math.pow(timeCalculation, 2));
		}
		this.setPreviousDistance(this.getDistance(centerOfGravityL, centerOfGravityR));
		System.out.println("disdev" + distanceDev);
		System.out.println("acc" + acceleration);
		this.setAcceleration(acceleration);
		return acceleration;
	}

	public float calculateDecelerationDistance(float[] centerOfGravityL, float[] centerOfGravityR, float startTime){
		//System.out.println("acc: " + this.getAcceleration());
		this.calculateSpeed(centerOfGravityL, centerOfGravityR, startTime);
		System.out.println("speed: " + this.getSpeed());
		float decelerationDistance = firstDistance/2;
				
//				(float) (Math.pow(this.getSpeed(), 2)/
//				(2*this.getAcceleration()) // hier moet er nog een bepaalde distance bij zodanig dat de tijd vh pitchen gecompenseerd wordt.
//				+3);
		return decelerationDistance;
	}

	private void setPreviousTimeDev(float time) {
		this.previousTimeDev = time;
	}

	private float getPreviousTimeDev() {
		return previousTimeDev;
	}

	private void setPreviousSpeed(float speed) {
		previousSpeed = speed;
	}

	private float getPreviousSpeed() {
		return previousSpeed;
	}

	private void setPreviousDistance(float distance) {
		previousDistance = distance;
	}

	private float getPreviousDistance() {
		return previousDistance;
	}

	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / 
				Math.cos(Math.toRadians(beta)));
		//		System.out.println("thrust "+thrust);
		//		System.out.println("pitch "+this.getDrone().getPitch());
		return thrust;
	}

	public float getY(float[] centerofGravity){
		float y = ((float) ((this.getCameraHeight()/2) - centerofGravity[1])) ;
		return y;
	}

	public int getCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getfocalDistance()*2);
		return height;
	}

	public void setDrone(Drone drone){
		this.drone = drone;
	}

	public Drone getDrone(){
		return this.drone;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getAcceleration() {
		return acceleration;
	}


}
