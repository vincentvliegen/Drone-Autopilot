package DroneAutopilot.calculations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;

	private float previousTime;
	private float time;
	private float[] previousPosition;
	private float[] position;
	public float deltaT;
	private float[] speed;
	List<Float> rotateMatrix = new ArrayList<>();
	List<Float> inverseRotateMatrix = new ArrayList<>();
	private boolean firstTime;

	private float[] windTranslation;
	private float[] expectedPosition;
	private float[] windRotation;
	private float[] expectedOrientation;

	private float thrust;
	private float yaw;
	private float pitch;
	private float roll;



	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setTime(0);
		this.setPosition(0,0,0);
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
		this.setWindTranslation(0,0,0);
		this.setWindRotation(0,0,0);
		this.setExpectedPosition(this.getDrone().getX(),this.getDrone().getY(),this.getDrone().getZ());
		this.setExpectedOrientation(this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll());
	}


	//////////DRONE//////////

	public void updateDroneData(){

		//update previous
		this.setPreviousTime(this.getTime());
		this.setPreviousPosition(this.getPosition());

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
		setSpeed(VectorCalculations.timesScalar(VectorCalculations.sum(this.getPosition(), VectorCalculations.inverse(this.getPreviousPosition())),1/this.getDeltaT()));
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
		float[] droneTranslated = VectorCalculations.sum(droneRotated, this.getPosition());
		return droneTranslated;
	}

	public float[] calculatePositionObject(float[] cogL, float[] cogR){
		float deltaX = calculateXObject(cogL, cogR);
		float deltaY = calculateYObject(cogL, cogR);
		float deltaZ = calculateZObject(cogL, cogR);
		return this.dronePosToWorldPos(deltaX, deltaY, deltaZ);
	}


	public float[] directionDronePos(float[] position){//niet genormaliseerd, moest je de grootte willen hebben
		return VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition()));
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
		return VectorCalculations.size(directionDronePos(position));
	}

	public float getSpeedTowardsPosition(float[] position){
		float speedDrone = VectorCalculations.size(this.getSpeed());
		//the cosinus of the angle between the speedvector of the drone and the distance of the object to the drone
		float cosAngle = VectorCalculations.cosinusBetweenVectors(VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition())),this.getSpeed());
		return cosAngle*speedDrone;
	}


	//////////OTHER//////////

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
	public void calculateThrust(float[] position){
		//normaal op het vlak gevormd door de thrust en de gravity + wind
		float weight = this.getDrone().getWeight();
		float gravity = this.getDrone().getGravity();//gravity is negatief
		float[] gravVector = VectorCalculations.timesScalar(new float[] {0, 1, 0}, weight*gravity);//vector is positief, want gravity is negatief
		float[] gravAndWind = VectorCalculations.sum(gravVector, this.getWindTranslation());
		float[] thrust = vectorDroneToWorld(new float[] {0,1,0});//positief genormaliseerd
		float[] normal = VectorCalculations.crossProduct(gravAndWind, thrust);

		//bereken de richting naar de positie
		float[] dirToPos = VectorCalculations.normalise(directionDronePos(position));

		float result;
		if(Arrays.equals(VectorCalculations.sum(dirToPos, new float[] {0,0,0}), VectorCalculations.sum(getPosition(), new float[] {0,0,0}))){//als het doel de huidige positie van de drone is
			float[] projectionGravVecOnThrustAxis = VectorCalculations.timesScalar(thrust, VectorCalculations.dotProduct(gravAndWind, thrust));//thrust is genormaliseerd
			float signThrustGrav;
			if(Arrays.equals(VectorCalculations.sum(thrust, new float[] {0,0,0}), VectorCalculations.sum(VectorCalculations.normalise(projectionGravVecOnThrustAxis), new float[] {0,0,0}))){//de gravity+wind staat volgens positieve thrust
				signThrustGrav = -1;
			}else{
				signThrustGrav = 1;
			}
			result = VectorCalculations.size(projectionGravVecOnThrustAxis)*signThrustGrav;
		} else if(Arrays.equals(VectorCalculations.sum(normal, new float[] {0,0,0}), new float[] {0,0,0})){//als normal = {0,0,0} (drone is gericht volgens de gravity+wind)
			//positieve of negatieve thrust ter compensatie vd gravity+wind?
			float signThrustGrav;
			if(Arrays.equals(VectorCalculations.sum(thrust, new float[] {0,0,0}), VectorCalculations.sum(VectorCalculations.normalise(gravAndWind), new float[] {0,0,0}))){//de gravity+wind staat volgens positieve thrust
				signThrustGrav = -1;
			}else{
				signThrustGrav = 1;
			}

			//positieve of negatieve thrust ter compensatie vd gravity+wind?
			float compensateDir = 1;//hoeveel keer de kracht om de gravity+wind te compenseren wordt gebruikt om richting het object te vliegen TODO verfijnen/minder statisch maken
			float[] projectionDirectionOnThrustAxis = VectorCalculations.timesScalar(thrust , VectorCalculations.dotProduct(dirToPos, thrust));//thrust is genormaliseerd
			float signThrustDir;
			if(Arrays.equals(VectorCalculations.sum(thrust, new float[] {0,0,0}), VectorCalculations.sum(VectorCalculations.normalise(projectionDirectionOnThrustAxis), new float[] {0,0,0}))){//de richting staat volgens positieve thrust
				signThrustDir = 1;
			}else{
				signThrustDir = -1;
			}

			result = VectorCalculations.size(gravAndWind)*(signThrustGrav+compensateDir*signThrustDir);
		}else{
			//bereken de projectie van de vector in de richting van de positie op het vlak
			float[] projectionDirectionOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(dirToPos, normal));//correcte projectie want normal is genormaliseerd
			float[] approxDir = VectorCalculations.sum(dirToPos, VectorCalculations.inverse(projectionDirectionOnNormal));

			//zoek nu een waarde van de thrust waarvoor we het best volgens deze projectie vliegen
			float maxThrust = this.getDrone().getMaxThrust();
			float[] upperLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, maxThrust),gravAndWind);
			float[] lowerLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, -maxThrust),gravAndWind);

			//ligt approxDir binnen of buiten de kleine hoek gevormd door upperLimit en lowerLimit?
			boolean isInside = true;
			//we tekenen een xy-vlak, en we leggen lowerLimit volgens de x-as en upperLimit heeft een positieve y-waarde
			//float[] coordLow = {vectorSize(lowerLimit),0};//coordinaten lowerLimit in xy-vlak

			float cosLowUp = VectorCalculations.cosinusBetweenVectors(lowerLimit, upperLimit);
			float[] crossPLowUp = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,upperLimit));
			//float[] coordUp = {vectorSize(upperLimit)*cosLowUp,vectorSize(upperLimit)*((float) Math.sqrt(1-cosLowUp*cosLowUp))};//coordinaten upperLimit in xy-vlak

			float cosLowAppDir = VectorCalculations.cosinusBetweenVectors(lowerLimit, approxDir);
			if(cosLowAppDir+0>cosLowUp+0){
				isInside = false;
			}
			float[] crossPLowAppDir = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,approxDir));
			float signAppDir;
			if(!Arrays.equals(VectorCalculations.sum(crossPLowUp, new float[] {0,0,0}), VectorCalculations.sum(crossPLowAppDir, new float[] {0,0,0}))){
				isInside = false;
				signAppDir = -1;
			}else{
				signAppDir = 1;
			}
			float[] coordAppDir = {VectorCalculations.size(approxDir)*cosLowAppDir,signAppDir*VectorCalculations.size(approxDir)*((float) Math.sqrt(1-cosLowAppDir*cosLowAppDir))};//coordinaten approxDir in xy-vlak
			//als binnen dan hoe groot is thrust om exact op approxDir te vliegen
			//als buiten dan dichter bij upper of lower (om op min of max te zetten)
			if(isInside){//inside
				float cosLowGravVec = VectorCalculations.cosinusBetweenVectors(lowerLimit, gravAndWind);
				float[] coordGravVec = {VectorCalculations.size(gravAndWind)*cosLowUp,VectorCalculations.size(gravAndWind)*((float) Math.sqrt(1-cosLowGravVec*cosLowGravVec))};//coordinaten gravAndWind in xy-vlak

				float cosLowThrust = VectorCalculations.cosinusBetweenVectors(lowerLimit, thrust);
				float[] crossPLowThrust = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,thrust));
				float signThrust;
				if(!Arrays.equals(VectorCalculations.sum(crossPLowUp, new float[] {0,0,0}), VectorCalculations.sum(crossPLowThrust, new float[] {0,0,0}))){
					signThrust = -1;
				}else{
					signThrust = 1;
				}
				float[] coordThrust = {VectorCalculations.size(thrust)*cosLowAppDir,signThrust*VectorCalculations.size(thrust)*((float) Math.sqrt(1-cosLowThrust*cosLowThrust))};//coordinaten thrust in xy-vlak

				result = 	(coordAppDir[0]*coordGravVec[1]-coordAppDir[1]*coordGravVec[0])/
						(coordAppDir[1]*coordThrust[0]-coordAppDir[0]*coordThrust[1]);
			}else{//outside
				float cosUpAppDir = VectorCalculations.cosinusBetweenVectors(upperLimit, approxDir);
				if(cosLowAppDir+0>cosUpAppDir+0){//dichter bij upperLimit
					result = -maxThrust;
				}else{//dichter bij lowerLimit
					result = maxThrust;
				}
			}
		}
		this.setThrust(result);
	}	

	public float[][] wantedOrientation(float[] position, float acceleration){
		float weight = this.getDrone().getWeight();
		float gravity = Math.abs(this.getDrone().getGravity());
		float[] gravVector = VectorCalculations.timesScalar(new float[] {0, -1, 0}, weight*gravity);
		float[] gravAndWind = VectorCalculations.sum(gravVector, this.getWindTranslation());
		float[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(directionDronePos(position)), acceleration*weight);

		float[] thrustVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(gravAndWind));
		float[] normal = VectorCalculations.normalise(thrustVector);//normale op het trustvlak, genormaliseerde thrust
		float[] projDirOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(forceToPos, normal));
		float[] viewVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(projDirOnNormal));
		return new float[][] {thrustVector,viewVector};
	}

	//Geeft de nog te overbruggen hoeken richting het object weer. Yaw & Pitch & Roll
	public float[] getRemainingAnglesToObject(float[] position, float acceleration){
		float[] thrustWanted = VectorCalculations.normalise(this.vectorWorldToDrone(this.wantedOrientation(position, acceleration)[0]));
		float[] droneAngles = new float[] {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
		float desiredPitch = (float) Math.asin(Math.toRadians(thrustWanted[1]));
		float desiredYaw = (float) Math.asin(Math.toRadians(thrustWanted[0]/(Math.cos(desiredPitch)))); // TODO: cos(pitch)=0
		float[] desiredDroneAngles = {desiredYaw, desiredPitch, 0};
		float[] remainingAngles = VectorCalculations.sum(desiredDroneAngles, VectorCalculations.inverse(droneAngles));
		return remainingAngles;
	}


	//////////WIND//////////

	public void calculateExpectedPosition(){
		float weight = this.getDrone().getWeight();
		float gravity = this.getDrone().getGravity();//negatief
		float[] thrust = VectorCalculations.timesScalar(vectorDroneToWorld(new float[] {0,1,0}), this.getThrust());
		float[] gravityVector = VectorCalculations.timesScalar(new float[] {0, 1, 0}, weight*gravity);//positieve volgens y-as, want gravity is negatief
		//(T+G+W)/(2m) * deltaT^2  {I} +  v0*deltaT + x0  {II} = Xexp
		float[] part1 = VectorCalculations.timesScalar(VectorCalculations.sum(VectorCalculations.sum(thrust, gravityVector), this.getWindTranslation()), this.getDeltaT()*this.getDeltaT()/(2*weight));
		float[] part2 = VectorCalculations.sum(VectorCalculations.timesScalar(this.getSpeed(), this.getDeltaT()), this.getPosition());
		this.setExpectedPosition(VectorCalculations.sum(part1, part2));
	}

	public void correctWindTranslation(){
		float[] deviation = VectorCalculations.sum(this.getPosition(),VectorCalculations.inverse(this.getExpectedPosition()));
		float weight = this.getDrone().getWeight();
		//De voorspelde versnelling (som van thrust, gravity en wind) blijkt niet juist te zijn. 
		//De wind kan afwijken (onnauwkeurigheid,verandering van de wind,...) en zorgt ervoor dat de drone ergens anders heen vliegt dan voorspeld.
		//We berekenen hier een correctie obv de afwijking van de positie, en tellen deze hier bij op.
		//Wcorr/(2m) * deltaT^2 = X1-Xexp = deviation
		float[] Wcorr = VectorCalculations.timesScalar(deviation,2*weight/(this.getDeltaT()*this.getDeltaT()));
		this.setWindTranslation(VectorCalculations.sum(this.getWindTranslation(), Wcorr));
	}

	public void calculateExpectedOrientation(float yawRate, float pitchRate, float rollRate){
		float[] droneAngles = {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
		float[] AnglesDev = {this.getDeltaT()*yawRate, this.getDeltaT()*pitchRate, this.getDeltaT()*rollRate};
		this.setExpectedOrientation(VectorCalculations.sum(droneAngles, AnglesDev));
	}

	public void correctWindRotation(){
		float[] droneAngles = {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
		float[] deviation = VectorCalculations.sum(droneAngles,VectorCalculations.inverse(this.getExpectedOrientation()));
		float[] rate = VectorCalculations.timesScalar(deviation, 1/this.getDeltaT());
		this.setWindRotation(VectorCalculations.sum(rate, this.getWindRotation()));
	}

	public void calculateRotationRates(float[] position, float acceleration){
		this.getRemainingAnglesToObject(position, acceleration);
		//TODO
	}


	//////////ROTATEMATRIX//////////

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

	public void setSpeed(float x, float y, float z){
		this.speed = new float[] {x, y, z};
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

	public float[] getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(float x, float y, float z){
		this.previousPosition = new float[] {x, y, z};
	}

	public void setPreviousPosition(float[] previousPosition) {
		this.previousPosition = previousPosition;
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

	public float[] getWindTranslation() {
		return windTranslation;
	}

	public void setWindTranslation(float x, float y, float z){
		this.windTranslation = new float[] {x,y,z};
	}

	public void setWindTranslation(float[] windTranslation) {
		this.windTranslation = windTranslation;
	}

	public float[] getExpectedPosition() {
		return expectedPosition;
	}

	public void setExpectedPosition(float x, float y, float z){
		this.expectedPosition = new float[] {x, y, z};
	}

	public void setExpectedPosition(float[] expectedPosition) {
		this.expectedPosition = expectedPosition;
	}

	public float[] getWindRotation() {
		return windRotation;
	}

	public void setWindRotation(float yawRate, float pitchRate, float rollRate){
		this.windRotation = new float[] {yawRate, pitchRate, rollRate};
	}

	public void setWindRotation(float[] windRotation) {
		this.windRotation = windRotation;
	}

	public float[] getExpectedOrientation() {
		return expectedOrientation;
	}

	public void setExpectedOrientation(float yaw, float pitch, float roll){
		this.expectedOrientation = new float[] {yaw, pitch, roll};
	}

	public void setExpectedOrientation(float[] expectedOrientation) {
		this.expectedOrientation = expectedOrientation;
	}

	public float getThrust() {
		return thrust;
	}

	public void setThrust(float thrust) {
		this.thrust = thrust;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
}
