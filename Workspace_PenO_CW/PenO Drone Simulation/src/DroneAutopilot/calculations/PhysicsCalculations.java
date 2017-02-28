package DroneAutopilot.calculations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;
	
	private float previousTime;
	private float time;
	private float[] previousposition;
	private float[] position;
	public float deltaT;
	private float[] speed;
	List<Float> rotateMatrix = new ArrayList<>();
	List<Float> inverseRotateMatrix = new ArrayList<>();
	
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
	
	public float[] directionDronePos(float[] position){//niet genormaliseerd, moest je de grootte willen hebben
		return vectorSum(position, vectorInverse(this.getPosition()));
	}
	
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
		return vectorSize(directionDronePos(position));
	}
	
	public float getSpeedTowardsPosition(float[] position){
		float speedDrone = vectorSize(this.getSpeed());
		//the cosinus of the angle between the speedvector of the drone and the distance of the object to the drone
		float cosAngle = vectorCosinusBetweenVectors(vectorSum(position, vectorInverse(this.getPosition())),this.getSpeed());
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
	public float getThrustToPosition(float[] position){
		//normaal op het vlak gevormd door de thrust en de zwaartekracht TODO + wind
		float weight = this.getDrone().getWeight();
		float gravity = this.getDrone().getGravity();
		float[] gravityVector = vectorTimesScalar(new float[] {0, -1, 0}, weight*gravity);
		float[] thrust = vectorDroneToWorld(new float[] {0,1,0});
		float[] normal = vectorCrossProduct(gravityVector, thrust);//TODO gravity + wind
		
		//bereken de richting naar de positie
		float[] dirToPos = vectorNormalise(directionDronePos(position));

		//bereken de projectie van de vector in de richting van de positie op het vlak
		//TODO als normal = {0,0,0} (drone is perfect horizontaal)
		float[] projectionDirectionOnNormal = vectorTimesScalar(normal , vectorDotProduct(dirToPos, normal));//correcte projectie want normal is genormaliseerd
		float[] approxDir = vectorSum(dirToPos, vectorInverse(projectionDirectionOnNormal));
		
		//zoek nu een waarde van de thrust waarvoor we het best volgens deze projectie vliegen
		float maxThrust = this.getDrone().getMaxThrust();
		float[] upperLimit = vectorSum(vectorTimesScalar(thrust, maxThrust),gravityVector);//TODO gravity + wind
		float[] lowerLimit = vectorSum(vectorTimesScalar(thrust, -maxThrust),gravityVector);//TODO gravity + wind
		
		//ligt approxDir binnen of buiten de kleine hoek gevormd door upperLimit en lowerLimit?
		boolean isInside = true;
		//we tekenen een xy-vlak, en we leggen lowerLimit volgens de x-as en upperLimit heeft een positieve y-waarde
		//float[] coordLow = {vectorSize(lowerLimit),0};//coordinaten lowerLimit in xy-vlak
		
		float cosLowUp = vectorCosinusBetweenVectors(lowerLimit, upperLimit);
		float[] crossPLowUp = vectorNormalise(vectorCrossProduct(lowerLimit,upperLimit));
		//float[] coordUp = {vectorSize(upperLimit)*cosLowUp,vectorSize(upperLimit)*((float) Math.sqrt(1-cosLowUp*cosLowUp))};//coordinaten upperLimit in xy-vlak
		
		float cosLowAppDir = vectorCosinusBetweenVectors(lowerLimit, approxDir);
		if(cosLowAppDir>cosLowUp){
			isInside = false;
		}
		float[] crossPLowAppDir = vectorNormalise(vectorCrossProduct(lowerLimit,approxDir));
		float signAppDir;
		if(!Arrays.equals(crossPLowUp, crossPLowAppDir)){
			isInside = false;
			signAppDir = -1;
		}else{
			signAppDir = 1;
		}
		float[] coordAppDir = {vectorSize(approxDir)*cosLowAppDir,signAppDir*vectorSize(approxDir)*((float) Math.sqrt(1-cosLowAppDir*cosLowAppDir))};//coordinaten approxDir in xy-vlak
		//als binnen dan hoe groot is thrust om exact op approxDir te vliegen
		//als buiten dan dichter bij upper of lower (om op min of max te zetten)
		float result;
		if(isInside){//inside
			float cosLowGravVec = vectorCosinusBetweenVectors(lowerLimit, gravityVector);//TODO gravity + wind
			float[] coordGravVec = {vectorSize(gravityVector)*cosLowUp,vectorSize(gravityVector)*((float) Math.sqrt(1-cosLowGravVec*cosLowGravVec))};//coordinaten gravityVector in xy-vlak TODO gravity + wind
			
			float cosLowThrust = vectorCosinusBetweenVectors(lowerLimit, thrust);
			float[] crossPLowThrust = vectorNormalise(vectorCrossProduct(lowerLimit,thrust));
			float signThrust;
			if(!Arrays.equals(crossPLowUp, crossPLowThrust)){
				signThrust = -1;
			}else{
				signThrust = 1;
			}
			float[] coordThrust = {vectorSize(thrust)*cosLowAppDir,signThrust*vectorSize(thrust)*((float) Math.sqrt(1-cosLowThrust*cosLowThrust))};//coordinaten thrust in xy-vlak
			
			result = 	(coordAppDir[0]*coordGravVec[1]-coordAppDir[1]*coordGravVec[0])/
						(coordAppDir[1]*coordThrust[0]-coordAppDir[0]*coordThrust[1]);//TODO gravity + wind	
		}else{//outside
			float cosUpAppDir = vectorCosinusBetweenVectors(upperLimit, approxDir);
			if(cosLowAppDir>cosUpAppDir){//dichter bij upperLimit
				result = -maxThrust;
			}else{//dichter bij lowerLimit
				result = maxThrust;
			}
		}
		return result;
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

 	public float vectorCosinusBetweenVectors(float[] vector1, float[] vector2){
 		return vectorDotProduct(vector1,vector2)/(vectorSize(vector1)*vectorSize(vector2));
 	}
 	
// 	public float vectorSinusBetweenVectors(float[] vector1, float[] vector2){
// 		return vectorSize(vectorCrossProduct(vector1,vector2))/(vectorSize(vector1)*vectorSize(vector2));
// 	}
 	
 	public void createRotateMatrix() {
 		float yaw = this.getDrone().getHeading();
		float pitch = this.getDrone().getPitch();
		float roll = this.getDrone().getRoll();
		
		this.getRotateMatrix().clear();
		this.getRotateMatrix().add((float) (Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))-Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))));
		this.getRotateMatrix().add((float) (Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(roll))+Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw))));
		this.getRotateMatrix().add((float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw))));
		
		this.getRotateMatrix().add((float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))));
		this.getRotateMatrix().add((float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(roll))));
		this.getRotateMatrix().add((float) (Math.sin(Math.toRadians(pitch))));
		
		this.getRotateMatrix().add((float) (Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))+Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))));
		this.getRotateMatrix().add((float) (Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))-Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))));
		this.getRotateMatrix().add((float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw))));
	}
 	
 	public void createInverseRotateMatrix(){
 		this.getInverseRotateMatrix().clear();
 		float yaw = this.getDrone().getHeading();
		float pitch = this.getDrone().getPitch();
		float roll = this.getDrone().getRoll();
		
		this.getInverseRotateMatrix().clear();
		this.getInverseRotateMatrix().add((float) (Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))-Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))));
		this.getInverseRotateMatrix().add((float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))));
		this.getInverseRotateMatrix().add((float) (Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))+Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))));

		this.getInverseRotateMatrix().add((float) (Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(roll))+Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw))));
		this.getInverseRotateMatrix().add((float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(roll))));
		this.getInverseRotateMatrix().add((float) (Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))-Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))));

		this.getInverseRotateMatrix().add((float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw))));
		this.getInverseRotateMatrix().add((float) (Math.sin(Math.toRadians(pitch))));
		this.getInverseRotateMatrix().add((float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw))));
 	}
	
	public float[] vectorDroneToWorld(float x, float y, float z){
		this.createInverseRotateMatrix();
		float r11 = this.getInverseRotateMatrix().get(0);
		float r12 = this.getInverseRotateMatrix().get(1);
		float r13 = this.getInverseRotateMatrix().get(2);
		float r21 = this.getInverseRotateMatrix().get(3);
		float r22 = this.getInverseRotateMatrix().get(4);
		float r23 = this.getInverseRotateMatrix().get(5);
		float r31 = this.getInverseRotateMatrix().get(6);
		float r32 = this.getInverseRotateMatrix().get(7);
		float r33 = this.getInverseRotateMatrix().get(8);
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
		this.createRotateMatrix();
		float r11 = this.getRotateMatrix().get(0);
		float r12 = this.getRotateMatrix().get(1);
		float r13 = this.getRotateMatrix().get(2);
		float r21 = this.getRotateMatrix().get(3);
		float r22 = this.getRotateMatrix().get(4);
		float r23 = this.getRotateMatrix().get(5);
		float r31 = this.getRotateMatrix().get(6);
		float r32 = this.getRotateMatrix().get(7);
		float r33 = this.getRotateMatrix().get(8);
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


	public List<Float> getRotateMatrix() {
		return rotateMatrix;
	}


	public void setRotateMatrix(List<Float> rotateMatrix) {
		this.rotateMatrix = rotateMatrix;
	}


	public List<Float> getInverseRotateMatrix() {
		return inverseRotateMatrix;
	}


	public void setInverseRotateMatrix(List<Float> inverseRotateMatrix) {
		this.inverseRotateMatrix = inverseRotateMatrix;
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
