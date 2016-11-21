package DroneAutopilot;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations_new {

	private Drone drone;
	private float speed;
	private float acceleration;
	private float firstDistance;
	private float previousTime;
	private float decelerationDistance;


	public PhysicsCalculations_new(Drone drone){
		this.setDrone(drone);
		setSpeed(0);
		setAcceleration(0);
		setDecelerationDistance(0);
	}

	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
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

	
	public void updateAccSpeed(float[] cog){
		float v0 = getSpeed();
		float T = this.getThrust(cog);
		float D = this.getDrone().getDrag();
		float pitch = this.getDrone().getPitch();
		float cos = (float) Math.cos(Math.toRadians(this.verticalAngleDeviation(cog)-pitch));
		float timeDev = this.getDrone().getCurrentTime() - this.getPreviousTime();
		float acc = (float) ((T*Math.sin(Math.toRadians(pitch)) - D*v0*cos) / (cos*(D*timeDev + this.getDrone().getWeight())));
		//System.out.println("acc"+acc);
		this.calculateDecelerationDistance(timeDev);
		this.setAcceleration(acc);
		float speed = acc*timeDev + v0;
		//System.out.println("speed" +speed);
		this.setSpeed(speed);
		this.setPreviousTime(this.getDrone().getCurrentTime());
	}
	
	public void calculateDecelerationDistance(float timeDev){
		//vertraag halfweg?
//		float decelerationDistance = firstDistance/2;		
//				(float) (Math.pow(this.getSpeed(), 2)/
//				(2*this.getAcceleration()) // hier moet er nog een bepaalde distance bij zodanig dat de tijd vh pitchen gecompenseerd wordt.
//				+3);
		float beta = -this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;
		float tegenpitch = -this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;
		float pitch = this.getDrone().getPitch();
		float pitchrate = this.getDrone().getMaxPitchRate();
		float speed = this.getSpeed();
		float T = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - tegenpitch)) / 
				Math.cos(Math.toRadians(beta)));
		float D = this.getDrone().getDrag();
		float cos = (float) Math.cos(Math.toRadians(beta-tegenpitch));
		float acc = (float) ((T*Math.sin(Math.toRadians(tegenpitch)) - D*speed*cos) / (cos*(D*timeDev + this.getDrone().getWeight())));
		if (acc!= 0){
			float distance = (float) (2*pitch/pitchrate*speed + (Math.pow(0.1, 2)-Math.pow(speed, 2))/(2*acc) + tegenpitch/pitchrate*0.1);
			System.out.println("rem" + distance);
			this.setDecelerationDistance(distance);
		}
	}

	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / 
				Math.cos(Math.toRadians(beta)));
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

	public float getPreviousTime() {
		return previousTime;
	}

	public void setPreviousTime(float previousTime) {
		this.previousTime = previousTime;
	}
	
	public void setDecelerationDistance(float distance){
		this.decelerationDistance = distance;
	}
	
	public float getDecelerationDistance(){
		return this.decelerationDistance;
	}
}
