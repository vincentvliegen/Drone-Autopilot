package DroneAutopilot.calculations;

import java.util.Arrays;

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
	private float[] directionOfView;
	private float[] directionOfThrust;
	
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
	private float[] externalForces;
	private float thrust;
	private float yawRate;
	private float pitchRate;
	private float rollRate;
	private final static float maxWindTranslation = 0.05f;
	private static final float[] maxWindRotationRate = new float[]{(float) 0.5,(float) 0.5,(float) 0.5};
	private boolean firstMovement;
	private final static float distanceSpeedFactor = 0.5f;
	private final static float dropdownDistance = 2f;
	
	//Vector Rotations
	private float[][] rotationMatrix;
	private float[][] inverseRotationMatrix;

	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setTime(this.getDrone().getCurrentTime());
		this.setPosition(this.getDrone().getX(),this.getDrone().getY(),this.getDrone().getZ());
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
		
		this.focalDistance = calculateFocalDistance();
		this.cameraHeight = calculateCameraHeight();
		
		this.setWindTranslation(0,0,0);
		this.setWindRotation(0,0,0);
		this.setExpectedPosition(this.getDrone().getX(),this.getDrone().getY(),this.getDrone().getZ());
		this.setExpectedOrientation(this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll());
		this.setFirstMovement(true);
		
	}


	//////////DRONE//////////

	public void updateDroneData(){

		//update previous
		this.setPreviousTime(this.getTime());
		this.setPreviousPosition(this.getPosition());

		//update current
		this.setPosition(this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ());
		this.setRotationMatrix(VectorCalculations.createRotationMatrix(this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()));
		this.setInverseRotationMatrix(VectorCalculations.createInverseRotationMatrix(this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()));
		this.calculateDirectionOfView();
		this.calculateDirectionOfThrust();
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

		private void calculateDirectionOfView(){
			float[] view = new float[] {0,0,-1};
			setDirectionOfView(vectorWorldToDrone(view));
		}
	
		private void calculateDirectionOfThrust(){
			float[] thrust = new float[] {0,1,0};
			setDirectionOfThrust(vectorWorldToDrone(thrust));
		}
		

	public float[] getDirectionDroneToPosition(float[] position){//niet genormaliseerd, moest je de grootte willen hebben
		return VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition()));
	}

	public float getDistanceDroneToPosition(float[] position){
		return VectorCalculations.size(getDirectionDroneToPosition(position));
	}

	public float getSpeedDroneToPosition(float[] position){
		float speedDrone = VectorCalculations.size(this.getSpeed());
		//the cosinus of the angle between the speedvector of the drone and the distance of the object to the drone
		float cosAngle = VectorCalculations.cosinusBetweenVectors(VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition())),this.getSpeed());
		return cosAngle*speedDrone;
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
	
		//TODO check vectorDroneToWorld of het worldtodrone moet zijn
		private float[] objectPosDroneToWorld(){
			float[] droneRotated = this.vectorDroneToWorld(getXObject(), getYObject(), getZObject());
			float[] droneTranslated = VectorCalculations.sum(droneRotated, this.getPosition());
			return droneTranslated;
		}
	
		
 	private float calculateFocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}

	private int calculateCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getFocalDistance()*2);
		return height;
	}
	
	
	//////////MOVEMENT//////////

	/**
	 * Deze functie roep je op wanneer je naar een bepaalde positie wilt vliegen, met een gegeven acceleratie
	 * @param targetPosition
	 */
	public void updatePosition(float[] targetPosition){
		if(!isFirstMovement()){
			correctWindTranslation();
			correctWindRotation();
		
			calculateExternalForces();
			calculateThrust(targetPosition);
			calculateWantedOrientationPos(targetPosition);
			calculateRemainingAnglesToObject();
			calculateRotationRates();
		
			this.getDrone().setThrust(this.getThrust());
			this.getDrone().setYawRate(this.getYawRate());
			this.getDrone().setPitchRate(this.getPitchRate());
			this.getDrone().setRollRate(this.getRollRate());
		
			calculateExpectedPosition();
			calculateExpectedOrientation();
		}else{
			
			calculateExternalForces();
			calculateThrust(targetPosition);
			
			this.getDrone().setThrust(this.getThrust());
			this.getDrone().setYawRate(0);
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			
			setFirstMovement(false);
		}
	}

	/**
	 * Deze functie roep je op wanneer je volgens een bepaalde richting wilt kijken, vanaf je huidige positie.
	 * @param direction
	 */
	public void updateOrientation(float[] direction){
		if(!isFirstMovement()){
			correctWindTranslation();
			correctWindRotation();
		
			calculateExternalForces();
			calculateThrust(this.getPosition());
			calculateWantedOrientationDir(direction);
			calculateRemainingAnglesToObject();
			calculateRotationRates();
		
			this.getDrone().setThrust(this.getThrust());
			this.getDrone().setYawRate(this.getYawRate());
			this.getDrone().setPitchRate(this.getPitchRate());
			this.getDrone().setRollRate(this.getRollRate());
		
			calculateExpectedPosition();
			calculateExpectedOrientation();
		}else{
			
			calculateExternalForces();
			calculateThrust(this.getPosition());
			
			this.getDrone().setThrust(this.getThrust());
			this.getDrone().setYawRate(0);
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			
			setFirstMovement(false);
		}
	}
	
		private void correctWindTranslation(){
			float[] deviation = VectorCalculations.sum(this.getPosition(),VectorCalculations.inverse(this.getExpectedPosition()));
			this.getDrone().setPitchRate(this.getPitchRate());
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
	
		private void calculateExternalForces(){
			float weight = this.getDrone().getWeight();
			float gravity = this.getDrone().getGravity();//gravity is negatief
			float drag = this.getDrone().getDrag();
			float[] gravityForce = VectorCalculations.timesScalar(new float[] {0, 1, 0}, weight*gravity);//vector is positief, want gravity is negatief
			for (float x: gravityForce)
				System.out.println(x);
			float[] windForce = this.getWindTranslation();
			for (float x: windForce)
				System.out.println(x);
			float[] dragForce = VectorCalculations.timesScalar(this.getSpeed(),-drag);//- drag want tegengesteld aan de snelheidsvector
			for (float x: dragForce)
				System.out.println(x);
			System.out.println("------");
			float[] totalExternalForce = VectorCalculations.sum(VectorCalculations.sum(gravityForce, windForce), dragForce);
			setExternalForces(totalExternalForce);
		}
		
		//berekent de thrust om naar de positie te vliegen, het dichtst bij de gevraagde positie (afhankelijk van de rotatie van de drone) 
		private void calculateThrust(float[] position){
			//normaal op het vlak gevormd door de thrust en de gravity + wind
			float[] externalForces = this.getExternalForces();
			float[] thrust = this.getDirectionOfThrust();//positief genormaliseerd
			float[] normal = VectorCalculations.crossProduct(externalForces, thrust);
	
			//bereken de richting naar de positie
			float[] dirToPos = VectorCalculations.normalise(getDirectionDroneToPosition(position));
	
			float result;
			if(Arrays.equals(VectorCalculations.sum(position, new float[] {0,0,0}), VectorCalculations.sum(getPosition(), new float[] {0,0,0}))){//als het doel de huidige positie van de drone is
				float[] projectionGravVecOnThrustAxis = VectorCalculations.timesScalar(thrust, VectorCalculations.dotProduct(externalForces, thrust));//thrust is genormaliseerd
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
				if(Arrays.equals(VectorCalculations.sum(thrust, new float[] {0,0,0}), VectorCalculations.sum(VectorCalculations.normalise(externalForces), new float[] {0,0,0}))){//de gravity+wind staat volgens positieve thrust
					signThrustGrav = -1;
				}else{
					signThrustGrav = 1;
				}
	
				//positieve of negatieve thrust ter compensatie vd gravity+wind?
				float compensateDir = 1;//hoeveel keer de kracht om de gravity+wind te compenseren wordt gebruikt om richting het object te vliegen TODO verfijnen/minder statisch maken
				float[] projectionDirectionOnThrustAxis = VectorCalculations.timesScalar(thrust , VectorCalculations.dotProduct(dirToPos, thrust));//thrust is genormaliseerd
				float sizeProjectionDirectionOnGravWindAxis = VectorCalculations.dotProduct(dirToPos, externalForces);
				float signThrustDir;
				if(sizeProjectionDirectionOnGravWindAxis+0 == 0){// geen compensatie wanneer direction in vlak met normaal grav+wind
					signThrustDir = 0;
				}else if(Arrays.equals(VectorCalculations.sum(thrust, new float[] {0,0,0}), VectorCalculations.sum(VectorCalculations.normalise(projectionDirectionOnThrustAxis), new float[] {0,0,0}))){//de richting staat volgens positieve thrust
					signThrustDir = 1;
				}else{
					signThrustDir = -1;
				}
	
				result = VectorCalculations.size(externalForces)*(signThrustGrav+compensateDir*signThrustDir);
			}else{
				//bereken de projectie van de vector in de richting van de positie op het vlak
				float[] projectionDirectionOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(dirToPos, normal));//correcte projectie want normal is genormaliseerd
				float[] approxDir = VectorCalculations.sum(dirToPos, VectorCalculations.inverse(projectionDirectionOnNormal));
	
				//zoek nu een waarde van de thrust waarvoor we het best volgens deze projectie vliegen
				float maxThrust = this.getDrone().getMaxThrust();
				float[] upperLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, maxThrust),externalForces);
				float[] lowerLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, -maxThrust),externalForces);
	
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
					float cosLowGravVec = VectorCalculations.cosinusBetweenVectors(lowerLimit, externalForces);
					float[] coordGravVec = {VectorCalculations.size(externalForces)*cosLowUp,VectorCalculations.size(externalForces)*((float) Math.sqrt(1-cosLowGravVec*cosLowGravVec))};//coordinaten gravAndWind in xy-vlak
	
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
	
		private void calculateWantedOrientationPos(float[] position){
			float acceleration = this.determineAcceleration(position);
			float weight = this.getDrone().getWeight();
			float[] externalForces = this.getExternalForces();
			float[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(getDirectionDroneToPosition(position)), acceleration*weight);
	
			float[] thrustVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(externalForces));
			float[] normal = VectorCalculations.normalise(thrustVector);//normale op het trustvlak, genormaliseerde thrust
			float[] projDirOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(forceToPos, normal));
			float[] viewVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(projDirOnNormal));
			this.setWantedOrientation(new float[][] {thrustVector,viewVector});
		}

			private float determineAcceleration(float[] position) {
				float distance = this.getDistanceDroneToPosition(position);
				float speed = this.getSpeedDroneToPosition(position);
				int minMaxIndex = 1;//accelerate
				if (distance/speed <= PhysicsCalculations.getDistancespeedfactor()) {//d/v <= i -> te snel -> vertragen
					minMaxIndex = 0;//decelerate
				} 
				if (distance <= PhysicsCalculations.getDropdowndistance()){
					return maxAccelerationValues(position)[minMaxIndex]*distance/PhysicsCalculations.getDropdowndistance();
				}else{
					return maxAccelerationValues(position)[minMaxIndex];
				}
			}
			
				private float[] maxAccelerationValues(float[] position){
					float maxthrust = this.getDrone().getMaxThrust();
					float weight = this.getDrone().getWeight();
					float[] externalForces = this.getExternalForces();
					float[] direction = VectorCalculations.normalise(getDirectionDroneToPosition(position));
					
					float[] minMaxAcceleration = accelerationCalc(externalForces, maxthrust, direction, weight);
					
					return new float[] {minMaxAcceleration[0]+PhysicsCalculations.getMaxwindtranslation(),minMaxAcceleration[1]-PhysicsCalculations.getMaxwindtranslation()};//max uitwijking door de wind
				}
				
					private float[] accelerationCalc(float[] externalForces, float thrustSize, float[] direction, float weight){
						//m*acc*Dir - (Grav + Wind + Drag) = Thrust met Thrust = t*(u,v,w) en t = size => u^2+v^2+w^2=1
						//(ax+b)^2+(cx+d)^2+(ex+f)^2=1 met x = acc,
						//a = (m*dirx)/t,
						//b = -(gravx+windx)/t
						//c = (m*diry)/t,
						//d = -(gravy+windy)/t
						//e = (m*dirz)/t,
						//f = -(gravz+windz)/t
						float a = weight*direction[0]/thrustSize;
						float b = -(externalForces[0])/thrustSize;
						float c = weight*direction[1]/thrustSize;
						float d = -(externalForces[1])/thrustSize;
						float e = weight*direction[2]/thrustSize;
						float f = -(externalForces[2])/thrustSize;
						
						//oplossing is van de vorm (-b +- sqrt(b^2-4ac))/2a => (part1 +- sqrt)/part2
						float sqrt = (float) Math.sqrt(Math.pow((2*a*b+2*c*d+2*e*f),2)-4*(a*a+c*c+e*e)*(b*b+d*d+f*f-1));
						float part1 = -(2*a*b+2*c*d+2*e*f);
						float part2 = 2*(a*a+c*c+e*e);
						
						float maxAcc = (part1 + sqrt)/part2;
						float minAcc = (part1 - sqrt)/part2;
						
						return new float[] {minAcc,maxAcc};
					}
	
		
		private void calculateWantedOrientationDir(float[] direction){
			float[] externalForces = this.getExternalForces();
			
			float[] thrustVector = VectorCalculations.inverse(externalForces);// = hover
			float[] normal = VectorCalculations.normalise(thrustVector);//normale op het trustvlak, genormaliseerde thrust
			float[] projDirOnNormal = VectorCalculations.timesScalar(normal , VectorCalculations.dotProduct(direction, normal));
			float[] viewVector = VectorCalculations.sum(direction, VectorCalculations.inverse(projDirOnNormal));
			this.setWantedOrientation(new float[][] {thrustVector,viewVector});
		}
		
		//Geeft de nog te overbruggen hoeken richting het object weer. Yaw & Pitch & Roll
		private void calculateRemainingAnglesToObject() throws ArithmeticException{
			float[] thrustWanted = VectorCalculations.normalise(this.getWantedOrientation()[0]);
			float[] viewWanted = VectorCalculations.normalise(this.getWantedOrientation()[1]);
			float[] inverseDroneAngles = new float[] {-this.getDrone().getHeading(), -this.getDrone().getPitch(), -this.getDrone().getRoll()};
			float pitchWanted = (float) Math.toDegrees(Math.asin(thrustWanted[2]));
			if (pitchWanted == Math.PI){
				throw new ArithmeticException("pitchWanted is PI, division by zero");
			}
			//float rollWanted = (float) Math.toDegrees(-Math.acos(thrustWanted[1]/Math.cos(Math.toRadians(pitchWanted))));  Alles in stukken gehakt, waardoor geen NaN meer.
				float value1 = (float) Math.cos(Math.toRadians(pitchWanted));
				float value2 = (float) (thrustWanted[1]/value1);
				float value3;
				if (Math.abs(value2) > 1){
					value3 = 1*Math.signum(value2);
				} else {
					value3 = (float) -Math.acos(value2);}
				float rollWanted = (float) Math.toDegrees(value3);
			float yawWanted = (float) Math.toDegrees(Math.acos(-viewWanted[2]/Math.cos(Math.toRadians(pitchWanted))));
			float[] wantedDroneAngles = new float[] {yawWanted, pitchWanted, rollWanted};
			float[] remainingAngles = VectorCalculations.sum(inverseDroneAngles, wantedDroneAngles);
			this.setRemainingAngles(remainingAngles);
		}
	
		//Geeft de nodige rotatieRates om in bepaalde tijd de remainingAngles te overbruggen.
		private void calculateRotationRates(){
			float[] remainingAngles = this.getRemainingAngles();
			if(!Arrays.equals(VectorCalculations.sum(remainingAngles, new float[] {0,0,0}),new float[] {0,0,0})){
				float[] maxAngleRates = new float[]{this.getDrone().getMaxYawRate(), this.getDrone().getMaxPitchRate(), this.getDrone().getMaxRollRate()};
				//Gecorrigeerde waarde ifv de maxwindrates.
				// Remain angles in 1 frame => 1/Delta * Remain
				float[] correctAnglesInOneFrames = VectorCalculations.timesScalar(remainingAngles, 1/getDeltaT());
				//Check vs maxRates
				if (correctAnglesInOneFrames[0]+0 != 0)
					this.setYawRate(correctAnglesInOneFrames[0]);
				if (correctAnglesInOneFrames[1]+0 != 0)
					this.setPitchRate(correctAnglesInOneFrames[1]);
				if (correctAnglesInOneFrames[2]+0 != 0) {
					this.setRollRate(correctAnglesInOneFrames[2]);
				}
			}else{
				this.setYawRate(0);
				this.setPitchRate(0);
				this.setRollRate(0);
			}
			
		}
		
		private void calculateExpectedPosition(){
			float weight = this.getDrone().getWeight();
			float[] externalForces = this.getExternalForces();
			float[] thrust = VectorCalculations.timesScalar(this.getDirectionOfThrust(), this.getThrust());

			//(T+G+W+D)/(2m) * deltaT^2  {I} +  v0*deltaT + x0  {II} = Xexp
			float[] part1 = VectorCalculations.timesScalar(VectorCalculations.sum(thrust, externalForces), this.getDeltaT()*this.getDeltaT()/(2*weight));
			float[] part2 = VectorCalculations.sum(VectorCalculations.timesScalar(this.getSpeed(), this.getDeltaT()), this.getPosition());
			this.setExpectedPosition(VectorCalculations.sum(part1, part2));
		}
	
		private void calculateExpectedOrientation(){
			float yawRate = this.getYawRate();
			float pitchRate = this.getPitchRate();
			float rollRate = this.getRollRate();
			float[] droneAngles = {this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll()};
			float[] AnglesDev = {this.getDeltaT()*yawRate+this.getWindRotation()[0], this.getDeltaT()*pitchRate+this.getWindRotation()[1], this.getDeltaT()*rollRate+this.getWindRotation()[2]};
			this.setExpectedOrientation(VectorCalculations.sum(droneAngles, AnglesDev));
		}	
				
		
	//////////VECTOR ROTATIONS//////////

	public float[] vectorDroneToWorld(float x, float y, float z){
		return vectorDroneToWorld(new float[] {x,y,z});
	}

	public float[] vectorDroneToWorld(float[] vector){
		return VectorCalculations.rotate(this.getInverseRotationMatrix(), vector);
	}

	public float[] vectorWorldToDrone(float x, float y, float z){
		return vectorWorldToDrone(new float[] {x,y,z});
	}

	public float[] vectorWorldToDrone(float[] vector){
		return VectorCalculations.rotate(this.getRotationMatrix(), vector);
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

	public float[][] getRotationMatrix() {
		return rotationMatrix;
	}

	private void setRotationMatrix(float[][] rotationMatrix) {
		this.rotationMatrix = rotationMatrix;
	}

	public float[][] getInverseRotationMatrix() {
		return inverseRotationMatrix;
	}

	private void setInverseRotationMatrix(float[][] inverseRotationMatrix) {
		this.inverseRotationMatrix = inverseRotationMatrix;
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

	public final float getFocalDistance() {
		return focalDistance;
	}

	public final int getCameraHeight() {
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

	public static float getMaxwindtranslation() {
		return maxWindTranslation;
	}

	public static float[] getMaxwindrotationrate() {
		return maxWindRotationRate;
	}

	public float[] getDirectionOfView() {
		return directionOfView;
	}
	
	private void setDirectionOfView(float[] directionOfView) {
		this.directionOfView = directionOfView;
	}

	public boolean isFirstMovement() {
		return firstMovement;
	}
	
	private void setFirstMovement(boolean firstMovement) {
		this.firstMovement = firstMovement;
	}	

	public float[] getExternalForces() {
		return externalForces;
	}

	public void setExternalForces(float[] externalForces) {
		this.externalForces = externalForces;
	}
	
	public static float getDistancespeedfactor() {
		return distanceSpeedFactor;
	}

	public static float getDropdowndistance() {
		return dropdownDistance;
	}

	public float[] getDirectionOfThrust() {
		return directionOfThrust;
	}

	public void setDirectionOfThrust(float[] directionOfThrust) {
		this.directionOfThrust = directionOfThrust;
	}

}


