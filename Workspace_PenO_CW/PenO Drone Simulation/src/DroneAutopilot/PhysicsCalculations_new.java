package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations_new {

	private Drone drone;
	private float speed;
	private float acceleration;
	private float firstDistance;
	private float doneDistance;
	private float totalAcceleration;
	private float totalSpeed;
	private boolean firstTimeUpdate;
	private float[] previousTimeAndDistance;
	private float decelerationDistance;


	public PhysicsCalculations_new(Drone drone){
		this.setDrone(drone);
		
		//Zolang er geen bol in zicht is, zullen acceleration, speed en decelDist 0 geven
		firstTimeUpdate = true;
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

	
	// berekent acceleration, speed en deceleration distance. Elk van deze heeft een eigen getter en setter ter beschikking ipv de calculate...
	// TODO deze functie moet in het begin opgeroepen worden (in moveToTarget) VOORDAT acceleratie, speed of decelerationsdistance wordt gebruikt
	public void updateAccVelDist(float currentDistance){
		float currentTime = this.getDrone().getCurrentTime();
		if(firstTimeUpdate){
			firstTimeUpdate = false;
			firstDistance = currentDistance;
		}else{
			this.calculateAcceleration(currentTime,currentDistance);
			this.calculateSpeed(currentTime);//speed wordt berekend na acceleration, omdat deze de huidige acceleration nodig heeft
			this.calculateDecelerationDistance();//decelerationdist wordt berekend na speed en acceleration, omdat deze de huidige speed en acceleration nodig heeft
		}
		//nadat alle bovenstaande waardes zijn berekend, kunnen de vorige tijd en afstand opnieuw ingesteld worden voor volgende cyclus.
		this.setPreviousTimeAndDistance(new float[] {currentTime,currentDistance});
	}
	
	public void calculateSpeed(float currentTime){
		float previousTime = this.getPreviousTimeAndDistance()[0];//t0
		float acceleration = this.getAcceleration();//a
		float previousSpeed = this.getSpeed();//v0
		//v2 = a*(t1-t0) + v0
		float speed =  acceleration * (currentTime-previousTime) + previousSpeed;
		this.setSpeed(speed);
	}
	
	public void calculateAcceleration(float currentTime, float currentDistance){
		float previousTime = this.getPreviousTimeAndDistance()[0]; //t0
		float previousDistance = this.getPreviousTimeAndDistance()[1]; //x0
		float previousSpeed = this.getSpeed(); //v0
		float previousAcceleration = this.getAcceleration(); //a0
		float acceleration = 0; //a
		if(currentTime!=previousTime){
			//a = 2*((x0-x1)-(v0*(t1-t0)))/((t1-t0)^2)
			acceleration=(float) (2*((previousDistance-currentDistance)-(previousSpeed*(currentTime-previousTime)))/Math.pow((currentTime-previousTime), 2));
		}else{
			//a=a0
			acceleration = previousAcceleration;
		}
		this.setAcceleration(acceleration);
	}

	public void calculateDecelerationDistance(){
		//vertraag halfweg?
		float decelerationDistance = firstDistance/2;		
//				(float) (Math.pow(this.getSpeed(), 2)/
//				(2*this.getAcceleration()) // hier moet er nog een bepaalde distance bij zodanig dat de tijd vh pitchen gecompenseerd wordt.
//				+3);
		this.setDecelerationDistance(decelerationDistance);
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

	public float[] getPreviousTimeAndDistance() {
		return previousTimeAndDistance;
	}

	public void setPreviousTimeAndDistance(float[] previousTimeDistance) {
		this.previousTimeAndDistance = previousTimeDistance;
	}
	
	public void setDecelerationDistance(float distance){
		this.decelerationDistance = distance;
	}
	
	public float getDecelerationDistance(){
		return this.decelerationDistance;
	}
}
