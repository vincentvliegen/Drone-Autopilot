package DroneAutopilot.calculations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;

	//Drone
	private float previousTime;
	private float time;
	private float[] previousPosition;
	private float[] position;
	private float deltaT;
	private float[] speed;
	private boolean firstTime;
	
	//Object
	private final float focalDistance;
	private final int cameraHeight;
	private float X1;
	private float X2;
	private float Y;
	private float depth;
	private float horizontalAngleDeviation;
	private float verticalAngleDeviation;
	private float XObject;
	private float YObject;
	private float ZObject;

	//Movement
	private float[] windTranslation;
	private float[] windRotation;
	private float[] expectedPosition;
	private float[] expectedOrientation;
	private float[][] wantedOrientation;
	private float[] remainingAngles;
	private float thrust;
	private float yawRate;
	private float pitchRate;
	private float rollRate;

	//Vector Rotations
	private List<Float> rotateMatrix;
	private List<Float> inverseRotateMatrix;

	
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setTime(0);
		this.setPosition(0,0,0);
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
		
		this.focalDistance = calculatefocalDistance();
		this.cameraHeight = calculateCameraHeight();
		
		this.setWindTranslation(0,0,0);
		this.setWindRotation(0,0,0);
		this.setExpectedPosition(this.getDrone().getX(),this.getDrone().getY(),this.getDrone().getZ());
		this.setExpectedOrientation(this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll());
		
		setRotateMatrix(new ArrayList<>());
		setInverseRotateMatrix(new ArrayList<>());
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

		private void calculateSpeed(){
			setSpeed(VectorCalculations.timesScalar(VectorCalculations.sum(this.getPosition(), VectorCalculations.inverse(this.getPreviousPosition())),1/this.getDeltaT()));
		}


	//////////OBJECT//////////

	public float[] calculatePositionObject(float[] centerOfGravityL, float[]centerOfGravityR){
		calculateX1(centerOfGravityL);
		calculateX2(centerOfGravityR);
		calculateY(centerOfGravityL);
		
		calculateDepth();
		calculateHorizontalAngleDeviation();
		calculateVerticalAngleDeviation();
		
		calculateXObject();
		calculateYObject();
		calculateZObject();	
		float[] objectPosition = objectPosDroneToWorld();
		
		return objectPosition;
	}
	
		private void calculateX1(float[] centerofGravityL){
			float x1 = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
			setX1(x1);
		}
	
		private void calculateX2(float[] centerofGravityR){
			float x2 = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
			setX2(x2);
		}
	
		private void calculateY(float[] centerofGravity){
			float y = ((float) ((this.getCameraHeight()/2) - centerofGravity[1])) ;
			setY(y);
		}
	
		private void calculateDepth(){
			float depth = (this.getDrone().getCameraSeparation() * this.getFocalDistance())/(this.getX1() - this.getX2());
			depth = Math.abs(depth);
			if(Float.isNaN(depth) || Float.isInfinite(depth)){
				depth =0;
			}
			setDepth(depth);
		}
	
		private void calculateHorizontalAngleDeviation(){
			float alfa;
			if (this.getDepth()!=0){
				float x = (this.getDepth() * this.getX1()) / this.getFocalDistance();
				float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth();
				alfa = (float) Math.toDegrees(Math.atan(tanAlfa));
			}else{
				alfa = 0;
			}
			setHorizontalAngleDeviation(alfa);
		}
	
		private void calculateVerticalAngleDeviation(){
			setVerticalAngleDeviation((float) Math.toDegrees(Math.atan(this.getY() / this.getFocalDistance())));
		}
		
		private void calculateXObject(){
			float deltaX;
			double angle;
			float depth;
			angle = this.getHorizontalAngleDeviation();
			depth = this.getDepth();
			deltaX = (float) (Math.tan(Math.toRadians(angle))*depth);
			setXObject(deltaX);
		}
	
		private void calculateYObject(){
			float deltaY;
			double angle;
			float depth;
			angle = this.getVerticalAngleDeviation();
			depth = this.getDepth();
			deltaY = (float) (Math.tan(Math.toRadians(angle))*depth);
			setYObject(deltaY);
		}
	
		private void calculateZObject(){
			//altijd negatief, positieve Z is naar achter
			float deltaZ = -this.getDepth();
			setZObject(deltaZ);
		}
	
		private float[] objectPosDroneToWorld(){
			float[] droneRotated = this.vectorDroneToWorld(getXObject(), getYObject(), getZObject());
			float[] droneTranslated = VectorCalculations.sum(droneRotated, this.getPosition());
			return droneTranslated;
		}
	
		
 	private float calculatefocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}

	private int calculateCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getFocalDistance()*2);
		return height;
	}
	
	private float[] directionDronePos(float[] position){//niet genormaliseerd, moest je de grootte willen hebben
		return VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition()));
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

	
	//////////MOVEMENT//////////

	public void updateMovement(float[] targetPosition, float acceleration){
		correctWindTranslation();
		correctWindRotation();
	
		calculateThrust(targetPosition);
		calculateWantedOrientation(targetPosition, acceleration);
		calculateRemainingAnglesToObject();
		calculateRotationRates();
		
		this.getDrone().setThrust(this.getThrust());
		this.getDrone().setYawRate(this.getYawRate());
		this.getDrone().setPitchRate(this.getPitchRate());
		this.getDrone().setRollRate(this.getRollRate());

		calculateExpectedPosition();
		calculateExpectedOrientation();
	}

		private void correctWindTranslation(){
			float[] deviation = VectorCalculations.sum(this.getPosition(),VectorCalculations.inverse(this.getExpectedPosition()));
			float weight = this.getDrone().getWeight();
			//De voorspelde versnelling (som van thrust, gravity en wind) blijkt niet juist te zijn. 
			//De wind kan afwijken (onnauwkeurigheid,verandering van de wind,...) en zorgt ervoor dat de drone ergens anders heen vliegt dan voorspeld.
			//We berekenen hier een correctie obv de afwijking van de positie, en tellen deze hier bij op.
			//Wcorr/(2m) * deltaT^2 = X1-Xexp = deviation
			float[] Wcorr = VectorCalculations.timesScalar(deviation,2*weight/(this.getDeltaT()*this.getDeltaT()));
			this.setWindTranslation(VectorCalculations.sum(this.getWindTranslation(), Wcorr));
		}
	
		private void correctWindRotation(){
			float[] droneAngles = {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
			float[] deviation = VectorCalculations.sum(droneAngles,VectorCalculations.inverse(this.getExpectedOrientation()));
			float[] rate = VectorCalculations.timesScalar(deviation, 1/this.getDeltaT());
			this.setWindRotation(VectorCalculations.sum(rate, this.getWindRotation()));
		}
	
		//berekent de thrust om naar de positie te vliegen, het dichtst bij de gevraagde positie (afhankelijk van de rotatie van de drone) 
		private void calculateThrust(float[] position){
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
	
		private void calculateWantedOrientation(float[] position, float acceleration){
			float weight = this.getDrone().getWeight();
			float gravity = Math.abs(this.getDrone().getGravity());
			float[] gravVector = VectorCalculations.timesScalar(new float[] {0, -1, 0}, weight*gravity);
			float[] gravAndWind = VectorCalculations.sum(gravVector, this.getWindTranslation());
			float[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(directionDronePos(position)), acceleration*weight);
	
			float[] thrustVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(gravAndWind));
			float[] normal = VectorCalculations.normalise(thrustVector);//normale op het trustvlak, genormaliseerde thrust
			float[] projDirOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(forceToPos, normal));
			float[] viewVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(projDirOnNormal));
			setWantedOrientation(new float[][] {thrustVector,viewVector});
		}
	
		//Geeft de nog te overbruggen hoeken richting het object weer. Yaw & Pitch & Roll
		private void calculateRemainingAnglesToObject(){
			float[] thrustWanted = VectorCalculations.normalise(this.vectorWorldToDrone(getWantedOrientation()[0]));
			float[] droneAngles = new float[] {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
			float desiredPitch = (float) Math.asin(Math.toRadians(thrustWanted[1]));
			float desiredYaw = (float) Math.asin(Math.toRadians(thrustWanted[0]/(Math.cos(desiredPitch)))); // TODO: cos(pitch)=0
			float[] desiredDroneAngles = {desiredYaw, desiredPitch, 0};
			float[] remainingAngles = VectorCalculations.sum(desiredDroneAngles, VectorCalculations.inverse(droneAngles));
			setRemainingAngles(remainingAngles);
		}
	
		private void calculateRotationRates(){
			this.getRemainingAngles();
			//TODO
		}
		
		private void calculateExpectedPosition(){
			float weight = this.getDrone().getWeight();
			float gravity = this.getDrone().getGravity();//negatief
			float[] thrust = VectorCalculations.timesScalar(vectorDroneToWorld(new float[] {0,1,0}), this.getThrust());
			float[] gravityVector = VectorCalculations.timesScalar(new float[] {0, 1, 0}, weight*gravity);//positieve volgens y-as, want gravity is negatief
			//(T+G+W)/(2m) * deltaT^2  {I} +  v0*deltaT + x0  {II} = Xexp
			float[] part1 = VectorCalculations.timesScalar(VectorCalculations.sum(VectorCalculations.sum(thrust, gravityVector), this.getWindTranslation()), this.getDeltaT()*this.getDeltaT()/(2*weight));
			float[] part2 = VectorCalculations.sum(VectorCalculations.timesScalar(this.getSpeed(), this.getDeltaT()), this.getPosition());
			this.setExpectedPosition(VectorCalculations.sum(part1, part2));
		}
	
		private void calculateExpectedOrientation(){
			float yawRate = this.getYawRate();
			float pitchRate = this.getPitchRate();
			float rollRate = this.getRollRate();
			float[] droneAngles = {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
			float[] AnglesDev = {this.getDeltaT()*yawRate, this.getDeltaT()*pitchRate, this.getDeltaT()*rollRate};
			this.setExpectedOrientation(VectorCalculations.sum(droneAngles, AnglesDev));
		}	
	
		
	//////////VECTOR ROTATIONS//////////

	private void createRotateMatrix() {
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

	private void createInverseRotateMatrix(){
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


	//////////OUTDATED//////////  //TODO verwijderen wanneer nieuwe vliegstrategie is uitgewerkt (wanneer moveToTarget kan worden verwijderd)

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
		float depth = (this.getDrone().getCameraSeparation() * this.getFocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		if(Float.isNaN(depth) || Float.isInfinite(depth)){
			depth =0;
		}
		return depth;
	}

	public float horizontalAngleDeviation(float[] centerOfGravityL, float[] centerOfGravityR){
		float alfa;
		if (this.getDepth(centerOfGravityL, centerOfGravityR)!=0){
			float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * this.getX1(centerOfGravityL)) / this.getFocalDistance();
			float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
			alfa = (float) Math.toDegrees(Math.atan(tanAlfa));
		}else{
			alfa = 0;
		}
		return alfa ;
	}

	public float verticalAngleDeviation(float[] centerOfGravity){
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getFocalDistance()));
	}

	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (-this.getDrone().getGravity()*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / 
				Math.cos(Math.toRadians(beta)));
		return thrust;
	}

	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		if(Float.isNaN(distance) || Float.isInfinite(distance)){
			distance = 0.5f;
		}
		return distance;
	}

	
	//////////GETTERS & SETTERS//////////

	private void setDrone(Drone drone){
		this.drone = drone;
	}

	public Drone getDrone(){
		return this.drone;
	}

	public float[] getSpeed() {
		return speed;
	}

	private void setSpeed(float x, float y, float z){
		this.speed = new float[] {x, y, z};
	}

	private void setSpeed(float[] speed) {
		this.speed = speed;
	}

	public float getPreviousTime() {
		return previousTime;
	}

	private void setPreviousTime(float previousTime) {
		this.previousTime = previousTime;
	}

	public float[] getPosition() {
		return position;
	}

	private void setPosition(float x, float y, float z){
		this.position = new float[] {x, y, z};
	}

	private void setPosition(float[] position) {
		this.position = position;
	}

	public float[] getPreviousPosition() {
		return previousPosition;
	}

	private void setPreviousPosition(float x, float y, float z){
		this.previousPosition = new float[] {x, y, z};
	}

	private void setPreviousPosition(float[] previousPosition) {
		this.previousPosition = previousPosition;
	}

	public float getDeltaT() {
		return deltaT;
	}

	private void setDeltaT(float deltaT) {
		this.deltaT = deltaT;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	private void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public float getTime() {
		return this.time;
	}

	private void setTime(float time) {
		this.time = time;
	}

	public List<Float> getRotateMatrix() {
		return rotateMatrix;
	}

	private void setRotateMatrix(List<Float> rotateMatrix) {
		this.rotateMatrix = rotateMatrix;
	}

	public List<Float> getInverseRotateMatrix() {
		return inverseRotateMatrix;
	}

	private void setInverseRotateMatrix(List<Float> inverseRotateMatrix) {
		this.inverseRotateMatrix = inverseRotateMatrix;
	}

	public float[] getWindTranslation() {
		return windTranslation;
	}

	private void setWindTranslation(float x, float y, float z){
		this.windTranslation = new float[] {x,y,z};
	}

	private void setWindTranslation(float[] windTranslation) {
		this.windTranslation = windTranslation;
	}

	public float[] getExpectedPosition() {
		return expectedPosition;
	}

	private void setExpectedPosition(float x, float y, float z){
		this.expectedPosition = new float[] {x, y, z};
	}

	private void setExpectedPosition(float[] expectedPosition) {
		this.expectedPosition = expectedPosition;
	}

	public float[] getWindRotation() {
		return windRotation;
	}

	private void setWindRotation(float yawRate, float pitchRate, float rollRate){
		this.windRotation = new float[] {yawRate, pitchRate, rollRate};
	}

	private void setWindRotation(float[] windRotation) {
		this.windRotation = windRotation;
	}

	public float[] getExpectedOrientation() {
		return expectedOrientation;
	}

	private void setExpectedOrientation(float yaw, float pitch, float roll){
		this.expectedOrientation = new float[] {yaw, pitch, roll};
	}

	private void setExpectedOrientation(float[] expectedOrientation) {
		this.expectedOrientation = expectedOrientation;
	}

	public float getThrust() {
		return thrust;
	}

	private void setThrust(float thrust) {
		this.thrust = thrust;
	}

	public float getYawRate() {
		return yawRate;
	}

	private void setYawRate(float yawRate) {
		this.yawRate = yawRate;
	}

	public float getPitchRate() {
		return pitchRate;
	}

	private void setPitchRate(float pitchRate) {
		this.pitchRate = pitchRate;
	}

	public float getRollRate() {
		return rollRate;
	}

	private void setRollRate(float rollRate) {
		this.rollRate = rollRate;
	}
	
	public float getX1() {
		return X1;
	}

	private void setX1(float x1) {
		X1 = x1;
	}

	public float getX2() {
		return X2;
	}

	private void setX2(float x2) {
		X2 = x2;
	}

	public float getY() {
		return Y;
	}

	private void setY(float y) {
		Y = y;
	}

	public float getDepth() {
		return depth;
	}

	private void setDepth(float depth) {
		this.depth = depth;
	}

	public float getHorizontalAngleDeviation() {
		return horizontalAngleDeviation;
	}

	private void setHorizontalAngleDeviation(float horizontalAngleDeviation) {
		this.horizontalAngleDeviation = horizontalAngleDeviation;
	}

	public float getVerticalAngleDeviation() {
		return verticalAngleDeviation;
	}

	private void setVerticalAngleDeviation(float verticalAngleDeviation) {
		this.verticalAngleDeviation = verticalAngleDeviation;
	}

	public float getFocalDistance() {
		return focalDistance;
	}

	public int getCameraHeight() {
		return cameraHeight;
	}

	public float getXObject() {
		return XObject;
	}

	private void setXObject(float xObject) {
		this.XObject = xObject;
	}

	public float getYObject() {
		return YObject;
	}

	private void setYObject(float yObject) {
		this.YObject = yObject;
	}

	public float getZObject() {
		return ZObject;
	}

	private void setZObject(float zObject) {
		this.ZObject = zObject;
	}



	public float[][] getWantedOrientation() {
		return wantedOrientation;
	}



	private void setWantedOrientation(float[][] wantedOrientation) {
		this.wantedOrientation = wantedOrientation;
	}



	public float[] getRemainingAngles() {
		return remainingAngles;
	}



	private void setRemainingAngles(float[] remainingAngles) {
		this.remainingAngles = remainingAngles;
	}

}


