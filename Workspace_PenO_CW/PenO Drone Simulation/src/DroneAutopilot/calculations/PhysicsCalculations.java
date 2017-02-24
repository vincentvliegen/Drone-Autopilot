package DroneAutopilot.calculations;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;
	
	private float previousTime;
	private float time;
	private float[] previousposition;
	private float[] position;
	public float deltaT;
	private float[] speed;
	
	public boolean firstTime;
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setTime(0);
		this.setPosition(0,0,0);
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
	}
	
	
	//////////DRONE//////////
	
	public void updateDroneData(){
		
		//update previous
		this.setPreviousTime(this.getTime());
		this.setPreviousposition(this.getPosition());
		
		//update current
		this.setPosition(this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ());
		this.setTime(this.getDrone().getCurrentTime());
		if(!isFirstTime()){
			this.setDeltaT(this.getTime()-this.getPreviousTime());
			this.calculateSpeed();
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
		setFirstTime(false);
	}
	
	public void calculateSpeed(){
		setSpeed(vectorTimesScalar(vectorSum(this.getPosition(),vectorInverse(this.getPreviousposition())),1/this.getDeltaT()));
	}

	
	//////////OBJECT//////////
	
	
	//TODO eventueel updateObjectData of een algemene returnObjectPosition (ik had iets vergelijkbaars in de reeds verwijderde physicscalculations_vincent staan) die alle stappen hieronder uitvoert op volgorde (vereenvoudigt onderstaande functies)
	public float getfocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}
	
	public int getCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getfocalDistance()*2);
		return height;
	}
	
	public float getX1(float[] centerofGravityL){
		float x1 = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return x1;
	}

	public float getX2(float[] centerofGravityR){
		float x2 = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return x2;
	}

	public float getY(float[] centerofGravity){
		float y = ((float) ((this.getCameraHeight()/2) - centerofGravity[1])) ;
		return y;
	}
		
	public float getDepth(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		if(Float.isNaN(depth) || Float.isInfinite(depth)){
			depth =0;
		}
		return depth;
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
		
	public float calculateXObject(float[] cogL, float[] cogR){
		float deltaX;
		double angle;
		float depth;
		angle = this.horizontalAngleDeviation(cogL, cogR);
		depth = this.getDepth(cogL, cogR);
		deltaX = (float) (Math.tan(Math.toRadians(angle))*depth);
		return deltaX;
	}
	
	public float calculateYObject(float[] cogL, float[] cogR){
		float deltaY;
		double angle;
		float depth;
		angle = this.verticalAngleDeviation(cogL);
		depth = this.getDepth(cogL, cogR);
		deltaY = (float) (Math.tan(Math.toRadians(angle))*depth);
		return deltaY;
	}
	
	public float calculateZObject(float[] cogL, float[] cogR){
			//altijd negatief, positieve Z is naar achter
			float deltaZ = -this.getDepth(cogL, cogR);
			return deltaZ;
		}
	
	public float[] dronePosToWorldPos(float droneX, float droneY, float droneZ){
		float[] droneRotated = this.vectorDroneToWorld(droneX,droneY,droneZ);
		float[] droneTranslated = vectorSum(droneRotated, this.getPosition());
		return droneTranslated;
	}
	
	public float[] calculatePositionObject(float[] cogL, float[] cogR){
		float deltaX = calculateXObject(cogL, cogR);
		float deltaY = calculateYObject(cogL, cogR);
		float deltaZ = calculateZObject(cogL, cogR);
		return this.dronePosToWorldPos(deltaX, deltaY, deltaZ);
	}

	
	//////////OTHER//////////
	
	//TODO vervangen in movetotarget etc.
	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		if(Float.isNaN(distance) || Float.isInfinite(distance)){
			distance = 0.5f;
		}
		return distance;
	}
	
	public float getDistanceToPosition(float[] position){
		return vectorSize(vectorSum(position, vectorInverse(this.getPosition())));
	}
	
	public float getSpeedTowardsPosition(float[] position){
		float distanceObject = this.getDistanceToPosition(position);
		float speedDrone = vectorSize(this.getSpeed());
		//the angle between the speedvector of the drone and the distance of the object to the drone
		float cosAngle = vectorDotProduct(vectorSum(position, vectorInverse(this.getPosition())),this.getSpeed())/(distanceObject*speedDrone);
		return cosAngle*speedDrone;
	}
	
	//TODO vervangen in movetotarget etc.
	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / 
				Math.cos(Math.toRadians(beta)));
		return thrust;
	}

	/**
	 * berekent de thrust om naar de positie te vliegen, het dichtst bij de gevraagde positie (afhankelijk van de rotatie van de drone)
	 */
	public float[] getThrustToPosition(float[] position){
		//normaal op het vlak gevormd door de thrust en de zwaartekracht (TODO kijk of het zwaartekracht + wind moet zijn)
		float[] gravity = {0, -1, 0};
		float[] thrust = {0, 1, 0};
		float[] normal = vectorCrossProduct(gravity, vectorWorldToDrone(thrust));
		
		//bereken de richting naar de positie
		float[] dirToPos = vectorNormalise(vectorSum(position, vectorInverse(getPosition())));

		//bereken de projectie van de vector in de richting van de positie op het vlak
		//TODO als normal = {0,0,0} (drone is perfect horizontaal)
		float[] projectionDirectionOnNormal = vectorTimesScalar(normal , vectorDotProduct(dirToPos, normal)/vectorDotProduct(normal, normal));
		float[] approxDir = vectorSum(dirToPos, vectorInverse(projectionDirectionOnNormal));
		//TODO zoek nu een waarde van de thrust waarvoor we het best volgens deze projectie vliegen
		
		return null;
	}
	
	
	//////////VECTOR//////////
	
	public float vectorSize(float x, float y, float z){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	
	public float vectorSize(float[] vector){
		return vectorSize(vector[0], vector[1], vector[2]);
	}

	public float[] vectorNormalise(float x, float y, float z){	
		float size = vectorSize(x,y,z);
		return new float[] {x/size, y/size, z/size};
	}
	
	public float[] vectorNormalise(float[] vector){
		return vectorNormalise(vector[0], vector[1], vector[2]);
	}

	
	public float[] vectorInverse(float[] vector){
		return new float[] {-vector[0], -vector[1], -vector[2]};
	}
	
 	public float[] vectorSum(float[] vector1, float[] vector2){
		return new float[] {vector1[0]+vector2[0], vector1[1]+vector2[1], vector1[2]+vector2[2],};
	}
	
 	public float[] vectorTimesScalar(float[] vector, float scalar){
 		return new float[] {vector[0]*scalar, vector[1]*scalar, vector[2]*scalar};
 	}
 	
 	public float vectorDotProduct(float[] vector1, float[] vector2){
 		return vector1[0]*vector2[0] + vector1[1]*vector2[1] + vector1[2]*vector2[2];
 	}
 			
 	public float[] vectorCrossProduct(float[] vector1, float[] vector2){
		float x = vector1[1]*vector2[2]-vector1[2]*vector2[1];
		float y = vector1[2]*vector2[0]-vector1[0]*vector2[2];
		float z = vector1[0]*vector2[1]-vector1[1]*vector2[0];
		return new float[] {x, y, z};
	}	

	
	public float[] vectorDroneToWorld(float x, float y, float z){
		float yaw = (float) Math.toRadians(this.getDrone().getHeading());
		float pitch = (float) Math.toRadians(this.getDrone().getPitch());
		float roll = (float) Math.toRadians(this.getDrone().getRoll());
		//de totale rotatiematrix (eerst roll ontdoen, dan pitch ontdoen, dan yaw ontdoen)
		//TODO corrigeren
		float r11 = (float) (Math.cos(roll)*Math.cos(yaw)-Math.sin(pitch)*Math.sin(roll)*Math.sin(yaw));
		float r12 = (float) (Math.cos(yaw)*Math.sin(roll)+Math.cos(roll)*Math.sin(pitch)*Math.sin(yaw));
		float r13 = (float) (-Math.cos(pitch)*Math.sin(yaw));
		float r21 = (float) (-Math.cos(pitch)*Math.sin(roll));
		float r22 = (float) (Math.cos(pitch)*Math.cos(roll));
		float r23 = (float) (Math.sin(pitch));
		float r31 = (float) (Math.cos(roll)*Math.sin(yaw)+Math.cos(yaw)*Math.sin(pitch)*Math.sin(roll));
		float r32 = (float) (Math.sin(roll)*Math.sin(yaw)-Math.cos(roll)*Math.cos(yaw)*Math.sin(pitch));
		float r33 = (float) (Math.cos(pitch)*Math.cos(yaw));
		//transformeren van drone naar world
		float xRotated = r11*x + r12*y + r13*z;
		float yRotated = r21*x + r22*y + r23*z;
		float zRotated = r31*x + r32*y + r33*z;
		return new float[] {xRotated, yRotated, zRotated};
	}
	
	public float[] vectorDroneToWorld(float[] vector){
		return vectorDroneToWorld(vector[0],vector[1],vector[2]);
	}
	
	public float[] vectorWorldToDrone(float x, float y, float z){
		float yaw = (float) Math.toRadians(this.getDrone().getHeading());
		float pitch = (float) Math.toRadians(this.getDrone().getPitch());
		float roll = (float) Math.toRadians(this.getDrone().getRoll());
		//de totale rotatiematrix (eerst yaw, dan pitch, dan roll)
		//TODO invullen
		float r11 = 0;
		float r12 = 0;
		float r13 = 0;
		float r21 = 0;
		float r22 = 0;
		float r23 = 0;
		float r31 = 0;
		float r32 = 0;
		float r33 = 0;
		//transformeren van world naar drone
		float xRotated = r11*x + r12*y + r13*z;
		float yRotated = r21*x + r22*y + r23*z;
		float zRotated = r31*x + r32*y + r33*z;
		return new float[] {xRotated, yRotated, zRotated};
	}
	
	public float[] vectorWorldToDrone(float[] vector){
		return vectorWorldToDrone(vector[0],vector[1],vector[2]);
	}
	


	//////////GETTERS & SETTERS//////////

	public void setDrone(Drone drone){
		this.drone = drone;
	}

	public Drone getDrone(){
		return this.drone;
	}

	public float[] getSpeed() {
		return speed;
	}

	public void setSpeed(float v_x, float v_y, float v_z){
		this.speed = new float[] {v_x, v_y, v_z};
	}

	public void setSpeed(float[] speed) {
		this.speed = speed;
	}

	public float getPreviousTime() {
		return previousTime;
	}

	public void setPreviousTime(float previousTime) {
		this.previousTime = previousTime;
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

	public void setPreviousposition(float x, float y, float z){
		this.previousposition = new float[] {x, y, z};
	}
	
	public void setPreviousposition(float[] previousposition) {
		this.previousposition = previousposition;
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

	public float getTime() {
		return this.time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	

	
//	private float acceleration;
//	private float decelerationDistance;
//	private float previousPitch;
//	private float previousPitchRate;
	
//	public void setAcceleration(float acceleration) {
//		this.acceleration = acceleration;
//	}
//
//	public float getAcceleration() {
//		return acceleration;
//	}
//	
//	public void setDecelerationDistance(float distance){
//	this.decelerationDistance = distance;
//}
//
//public float getDecelerationDistance(){
//	return this.decelerationDistance;
//}
//
//public float getPreviousPitch() {
//	return previousPitch;
//}
//
//public void setPreviousPitch(float previousPitch) {
//	this.previousPitch = previousPitch;
//}
//
//public float getPreviousPitchRate() {
//	return previousPitchRate;
//}
//
//public void setPreviousPitchRate(float previousPitchRate) {
//	this.previousPitchRate = previousPitchRate;
//}
	
	
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
//		//System.out.println("delta" + delta); //todo is niet +- 0 in world 11? gaat helaas niet anders blijkbaar
//		this.setSpeed(speed);
//		this.calculateDecelerationDistance(timeDev,cog);
//		this.setPreviousTime(this.getDrone().getCurrentTime());
//		this.setPreviousPitch(pitch);
//		//todo elke keer dat de pitchrate wordt ingesteld moet this.setPreviousPitchRate worden aangepast (wanneer target = visible)
//	}
//
//	public void calculateDecelerationDistance(float timeDev,float[] cog){
//		//todo parameter voor maxPitch (hoogte/2*1/10) in MoveToTarget of hier
//		float pitch = this.getDrone().getPitch();
//		float pitchrate = this.getDrone().getMaxPitchRate();
//		float tegenPitch = -this.getDrone().getLeftCamera().getVerticalAngleOfView()/10;//todo enkel wanneer max pitch het tegengestelde is, deze moet even groot zijn
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
