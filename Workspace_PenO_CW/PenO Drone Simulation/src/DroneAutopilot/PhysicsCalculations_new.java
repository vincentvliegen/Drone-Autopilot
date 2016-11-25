package DroneAutopilot;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations_new {

	private Drone drone;
	private float speed;
	private float acceleration;
	private float firstDistance;
	private float previousTime;
	private float decelerationDistance;
	private float previousPitch;
	private float previousPitchRate;


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
		float depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		if(Float.isNaN(depth) || Float.isInfinite(depth)){
			depth =0;
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
		float alfa;
		if (this.getDepth(centerOfGravityL, centerOfGravityR)!=0){
			float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * this.getX1(centerOfGravityL)) / this.getfocalDistance();
			float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
			alfa = (float) Math.toDegrees(Math.atan(tanAlfa));
		}else{
			alfa = 0;
		}
		return alfa ;
	}

	public float verticalAngleDeviation(float[] centerOfGravity){
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}

	
	public void updateAccSpeed(float[] cog){
		float v0 = getSpeed();
		float weight = this.getDrone().getWeight();
		float gravity = this.getDrone().getGravity();
		float T = this.getThrust(cog);
		float D = this.getDrone().getDrag(); 
		float pitch = this.getDrone().getPitch();
		float beta = this.verticalAngleDeviation(cog);
		float delta = beta-pitch;
		float timeDev = this.getDrone().getCurrentTime() - this.getPreviousTime();
		float acc = (float) ((T*Math.sin(Math.toRadians(pitch))/(Math.cos(Math.toRadians(delta)) - D*v0)/(/*D*timeDev + */weight)));
		//System.out.println("acc"+acc);
		this.setAcceleration(acc);
		
		//wanneer versnelling afhankelijk is van de tijd varieert de snelheid //pitch = p0 + pitchrate*(t-t0)
		float speed =0;
		if (previousPitchRate == 0){
			speed = acc*timeDev + v0;
		} else{
			speed = (float) (v0 +timeDev*(v0*D/weight-gravity*Math.sin(Math.toRadians(delta))) 
					- gravity*Math.cos(Math.toRadians(delta))/previousPitchRate*(Math.log(Math.cos(Math.toRadians(delta+previousPitch + timeDev*previousPitchRate))-Math.log(Math.cos(Math.toRadians(delta+previousPitch))))));
			
		}
		//System.out.println("beta " + beta);
		System.out.println("pitch " + pitch);
		//System.out.println("delta" + delta); //TODO is niet 0 in world 11?
		System.out.println("speed " +speed);
		this.setSpeed(speed);
		this.calculateDecelerationDistance(timeDev,cog);
		this.setPreviousTime(this.getDrone().getCurrentTime());
		this.setPreviousPitch(pitch);
		//TODO elke keer dat de pitchrate wordt ingesteld moet this.setPreviousPitchRate worden aangepast (wanneer target = visible)
	}
		
	public void calculateDecelerationDistance(float timeDev,float[] cog){
		//TODO parameter voor maxPitch (hoogte/2*1/10) in MoveToTarget of hier
		float pitch = this.getDrone().getPitch();
		float pitchrate = this.getDrone().getMaxPitchRate();
		float tegenPitch = -this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;//TODO enkel wanneer max pitch het tegengestelde is, deze moet even groot zijn
		//tegenbeta = tegenPitch+delta = tegenPitch + (beta-pitch)
		float tegenBeta = tegenPitch + (this.verticalAngleDeviation(cog)-pitch);
		float speed = this.getSpeed();
		float cosDelta = (float) Math.cos(Math.toRadians(tegenBeta-tegenPitch));
		float T = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
				cosDelta / 
				Math.cos(Math.toRadians(tegenBeta)));
		float D = this.getDrone().getDrag();
		float acc = (float) ((T*Math.sin(Math.toRadians(tegenPitch)) - D*speed*cosDelta) / (cosDelta*(/* D*timeDev +*/ this.getDrone().getWeight())));
		if (acc!= 0){
			float distance = (float) (2*pitch/pitchrate*speed + (Math.pow(0.1, 2)-Math.pow(speed, 2))/(2*acc) + tegenPitch/pitchrate*0.1);
			System.out.println("rem " + distance);
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
	
	public float getPreviousPitch() {
		return previousPitch;
	}

	public void setPreviousPitch(float previousPitch) {
		this.previousPitch = previousPitch;
	}
	
	public float getPreviousPitchRate() {
		return previousPitchRate;
	}

	public void setPreviousPitchRate(float previousPitchRate) {
		this.previousPitchRate = previousPitchRate;
	}
}
