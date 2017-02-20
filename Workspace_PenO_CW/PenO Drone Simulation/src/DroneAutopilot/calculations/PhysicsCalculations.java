package DroneAutopilot.calculations;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;
	private float[] speed;
	private float[] position;
	private float[] previousposition;
	private float acceleration;
	private float previousTime;
	private float decelerationDistance;
	private float previousPitch;
	private float previousPitchRate;
	public float deltaT;
	public boolean firstTime;
	
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setSpeed(0,0,0);
		this.setPosition(0,0,0);
		this.setFirstTime(true);
	}
	
	
	//DRONE
	
	public void update(){
		this.setPosition(this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ());
		if(!firstTime){
			this.setDeltaT(this.getDrone().getCurrentTime()-this.getPreviousTime());
			this.updateSpeed();
			/*
			System.out.println("--------------");
			System.out.println("Autopilot");
			System.out.println("DeltaT: " + this.getDeltaT());
			System.out.println("PosX: " + this.getDrone().getX());
			System.out.println("PosY: " + this.getDrone().getY());
			System.out.println("PosZ: " + this.getDrone().getZ());
			System.out.println("speedx: " + this.getSpeed()[0]);
			System.out.println("speedy: " + this.getSpeed()[1]);
			System.out.println("speedz: " + this.getSpeed()[2]);
			System.out.println("--------------");	
			*/
		}
		
		//update previous
		this.setPreviousTime(this.getDrone().getCurrentTime());
		this.setPreviousposition(this.getPosition());
		
		firstTime=false;
	}
	
	public float calculateSpeed(float currentPos, float previousPos){
		return (currentPos-previousPos)/this.getDeltaT();
	}
	
	public void updateSpeed(){
		speed = new float[]{calculateSpeed(this.getPosition()[0], this.getPreviousposition()[0]), 
				calculateSpeed(this.getPosition()[1], this.getPreviousposition()[1]), 
				calculateSpeed(this.getPosition()[2], this.getPreviousposition()[2])};
	}
	
	
//OBJECT
	// XCoördinate object
	public float calculateXObject(float[] cogL, float[] cogR){
		float deltaX;
		double angle;
		float depth;
		angle = this.horizontalAngleDeviation(cogL, cogR);
		depth = this.getDepth(cogL, cogR);
		deltaX = (float) (Math.tan(Math.toRadians(angle))*depth);
		return deltaX + this.getDrone().getX();
	}
	
	// YCoördinate object
	public float calculateYObject(float[] cogL, float[] cogR){
		float deltaY;
		double angle;
		float depth;
		angle = this.verticalAngleDeviation(cogL);
		depth = this.getDepth(cogL, cogR);
		deltaY = (float) (Math.tan(Math.toRadians(angle))*depth);
		return deltaY + this.getDrone().getY();
	}
	
	// ZCoördinate object
		public float calculateZObject(float[] cogL, float[] cogR){
			float deltaZ = this.getDepth(cogL, cogR);
			return deltaZ + this.getDrone().getZ();
		}
	
	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		if(Float.isNaN(distance) || Float.isInfinite(distance)){
			distance = 0.5f;
		}
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
	
	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * 
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
	

	//////////Getters & Setters//////////

	public void setDrone(Drone drone){
		this.drone = drone;
	}

	public Drone getDrone(){
		return this.drone;
	}
	
	public void setSpeed(float v_x, float v_y, float v_z){
		this.speed = new float[] {v_x, v_y, v_z};
	}

	public void setSpeed(float[] speed) {
		this.speed = speed;
	}

	public float[] getSpeed() {
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

	public float[] getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y, float z){
		this.position = new float[] {x, y, z};
	}

	public void setPosition(float[] position) {
		this.position = position;
	}

	public float[] getPreviousposition() {
		return previousposition;
	}

	public void setPreviousposition(float[] previousposition) {
		this.previousposition = previousposition;
	}
	
	public void setPreviousposition(float x, float y, float z){
		this.previousposition = new float[] {x, y, z};
	}


	public float getDeltaT() {
		return deltaT;
	}


	public void setDeltaT(float deltaT) {
		this.deltaT = deltaT;
	}


	public boolean isFirstTime() {
		return firstTime;
	}


	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}
	
	
	
//	public void updateAccSpeed(float[] cog){
//		float v0 = getSpeed();
//		float weight = this.getDrone().getWeight();
//		float gravity = this.getDrone().getGravity();
//		float T = this.getThrust(cog);
//		float D = this.getDrone().getDrag(); 
//		float pitch = this.getDrone().getPitch();
//		float beta = this.verticalAngleDeviation(cog)
//				;
//		float delta = beta-pitch;
//		float timeDev = this.getDrone().getCurrentTime() - this.getPreviousTime();
//		float acc = (float) ((T*Math.sin(Math.toRadians(pitch))/(Math.cos(Math.toRadians(delta)) - D*v0)/(/*D*timeDev + */weight)));
//		//System.out.println("acc"+acc);
//		//System.out.println("accdif" + (this.getAcceleration()-acc));
//		this.setAcceleration(acc);
//
//		//wanneer versnelling afhankelijk is van de tijd varieert de snelheid //pitch = p0 + pitchrate*(t-t0)
//		float speed =0;
//		if (getPreviousPitchRate() == 0){
//			speed = acc*timeDev + v0;
//			//System.out.println("without pitchrate speed " + speed );
//		} else{//snelheid ifv pitch(t)
//			speed = (float) (v0 +timeDev*(-v0*D/weight+gravity*Math.sin(Math.toRadians(delta))) 
//					+ gravity*Math.cos(Math.toRadians(delta))*(Math.log(Math.cos(Math.toRadians(beta)))-Math.log(Math.cos(Math.toRadians(/*previousBeta*/delta+getPreviousPitch()))))/getPreviousPitchRate());
//			//System.out.println("with pitchrate speed " + speed );
//		}
//		//System.out.println("beta " + beta);
//		//System.out.println("pitch " + pitch);
//		//System.out.println("delta" + delta); //TODO is niet +- 0 in world 11? gaat helaas niet anders blijkbaar
//		this.setSpeed(speed);
//		this.calculateDecelerationDistance(timeDev,cog);
//		this.setPreviousTime(this.getDrone().getCurrentTime());
//		this.setPreviousPitch(pitch);
//		//TODO elke keer dat de pitchrate wordt ingesteld moet this.setPreviousPitchRate worden aangepast (wanneer target = visible)
//	}
//
//	public void calculateDecelerationDistance(float timeDev,float[] cog){
//		//TODO parameter voor maxPitch (hoogte/2*1/10) in MoveToTarget of hier
//		float pitch = this.getDrone().getPitch();
//		float pitchrate = this.getDrone().getMaxPitchRate();
//		float tegenPitch = -this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;//TODO enkel wanneer max pitch het tegengestelde is, deze moet even groot zijn
//		//tegenbeta = tegenPitch+delta = tegenPitch + (beta-pitch)
//		float tegenBeta = tegenPitch + (this.verticalAngleDeviation(cog)-pitch);
//		float speed = this.getSpeed();
//		float cosDelta = (float) Math.cos(Math.toRadians(tegenBeta-tegenPitch));
//		float T = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
//				cosDelta / 
//				Math.cos(Math.toRadians(tegenBeta)));
//		float D = this.getDrone().getDrag();
//		float acc = (float) ((T*Math.sin(Math.toRadians(tegenPitch)) - D*speed*cosDelta) / (cosDelta*(/* D*timeDev +*/ this.getDrone().getWeight())));
//		if (acc!= 0){
//			float distance = (float) (2*pitch/pitchrate*speed + (Math.pow(0.1, 2)-Math.pow(speed, 2))/(2*acc) + tegenPitch/pitchrate*0.1);
//			//System.out.println("rem " + distance);
//			this.setDecelerationDistance(distance);
//		}
//	}




}
