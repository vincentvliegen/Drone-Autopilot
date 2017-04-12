package DroneAutopilot.calculations;


import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;

	//Drone
	private double previousTime;
	private double time;
	private double[] previousPosition;
	private double[] position;
	private double deltaT;
	private double[] speed;
	private boolean firstTime;
	private double[] directionOfView;
	private double[] directionOfThrust;
	
	//Object
	private final double focalDistance;
	private final double cameraHeight;
	private double X1;
	private double X2;
	private double Y;
	private double depth;
	private double horizontalAngleDeviation;
	private double verticalAngleDeviation;
	private double XObject;
	private double YObject;
	private double ZObject;

	//Movement
	private double[] windTranslation;
	private double[] windRotation;
	private double[] expectedPosition;
	private double[] expectedOrientation;
	private double[][] wantedOrientation;
	private double[] remainingAngles;
	private double[] externalForces;
	private double thrust;
	private double yawRate;
	private double pitchRate;
	private double rollRate;
	private final static double maxWindTranslation = 0.05;
	private static final double[] maxWindRotationRate = new double[]{0.5, 0.5, 0.5};
	private boolean firstMovement;
	private final static double distanceSpeedFactor = 0.5;
	private final static double dropdownDistance = 3;
	
	//Vector Rotations
	private double[][] rotationMatrix;
	private double[][] inverseRotationMatrix;

	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		this.setTime((double) this.getDrone().getCurrentTime());
		this.setPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
		
		this.focalDistance = calculateFocalDistance();
		this.cameraHeight = calculateCameraHeight();
		
		this.setWindTranslation(0,0,0);
		this.setWindRotation(0,0,0);
		this.setExpectedPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
		this.setExpectedOrientation((double) this.getDrone().getHeading(), (double) this.getDrone().getPitch(), (double) this.getDrone().getRoll());
		this.setFirstMovement(true);
		
	}


	//////////DRONE//////////

	public void updateDroneData(){

		//update previous
		this.setPreviousTime(this.getTime());
		this.setPreviousPosition(this.getPosition());

		//update current
		this.setPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
		this.setRotationMatrix(VectorCalculations.createRotationMatrix((double) this.getDrone().getHeading(), (double) this.getDrone().getPitch(), (double) this.getDrone().getRoll()));
		this.setInverseRotationMatrix(VectorCalculations.createInverseRotationMatrix((double) this.getDrone().getHeading(), (double) this.getDrone().getPitch(), (double) this.getDrone().getRoll()));
		this.calculateDirectionOfView();
		this.calculateDirectionOfThrust();
		this.setTime((double) this.getDrone().getCurrentTime());
		
		
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
			//a*t^2/2 + v0*t + x0 = x1 => a = (x1-x0-v0*t)*2/t^2
			double[] a = VectorCalculations.timesScalar(
							VectorCalculations.sum(
								VectorCalculations.sum(
									this.getPosition(), 
									VectorCalculations.inverse(this.getPreviousPosition())),
								VectorCalculations.timesScalar(this.getSpeed(), -this.getDeltaT())),
							2/(this.getDeltaT()*this.getDeltaT()));
			//v1 = v0 + at
			double[] vNew = VectorCalculations.sum(this.getSpeed(), VectorCalculations.timesScalar(a, this.getDeltaT()));
//			System.out.println("speed:");
//			for(double x:vNew)
//				System.out.println(x);
			setSpeed(vNew);
		}

		private void calculateDirectionOfView(){
			double[] view = new double[] {0,0,-1};
			setDirectionOfView(vectorWorldToDrone(view));
		}
	
		private void calculateDirectionOfThrust(){
			double[] thrust = new double[] {0,1,0};
			setDirectionOfThrust(vectorWorldToDrone(thrust));
		}
		

	public double[] getDirectionDroneToPosition(double[] position){
		return VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition()));
	}

	public double getDistanceDroneToPosition(double[] position){
		return VectorCalculations.size(getDirectionDroneToPosition(position));
	}

	public double getSpeedDroneToPosition(double[] position){
		double speedDrone = VectorCalculations.size(this.getSpeed());
		//the cosinus of the angle between the speedvector of the drone and the distance of the object to the drone
		double cosAngle = VectorCalculations.cosinusBetweenVectors(VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition())),this.getSpeed());
		return cosAngle*speedDrone;
	}

		
	//////////OBJECT//////////

	public double[] calculatePositionObject(double[] centerOfGravityL, double[]centerOfGravityR){
		calculateX1(centerOfGravityL);
		calculateX2(centerOfGravityR);
		calculateY(centerOfGravityL);
		
		calculateDepth();
		calculateHorizontalAngleDeviation();
		calculateVerticalAngleDeviation();
		
		calculateXObject();
		calculateYObject();
		calculateZObject();	
		double[] objectPosition = objectPosDroneToWorld();
		
		return objectPosition;
	}
	
		private void calculateX1(double[] centerofGravityL){
			double x1 = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
			setX1(x1);
		}
	
		private void calculateX2(double[] centerofGravityR){
			double x2 = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
			setX2(x2);
		}
	
		private void calculateY(double[] centerofGravity){
			double y = (this.getCameraHeight()/2) - centerofGravity[1] ;
			setY(y);
		}
	
		private void calculateDepth(){
			double depth = (this.getDrone().getCameraSeparation() * this.getFocalDistance())/(this.getX1() - this.getX2());
			depth = Math.abs(depth);
			if(Double.isNaN(depth) || Double.isInfinite(depth)){
				depth = 0;
			}
			setDepth(depth);
		}
	
		private void calculateHorizontalAngleDeviation(){
			double alfa;
			if (this.getDepth()!=0){
				double x = (this.getDepth() * this.getX1()) / this.getFocalDistance();
				double tanAlfa = (x - (this.getDrone().getCameraSeparation()/2)) / this.getDepth();
				alfa = Math.toDegrees(Math.atan(tanAlfa));
			}else{
				alfa = 0;
			}
			setHorizontalAngleDeviation(alfa);
		}
	
		private void calculateVerticalAngleDeviation(){
			setVerticalAngleDeviation(Math.toDegrees(Math.atan(this.getY() / this.getFocalDistance())));
		}

		
		private void calculateZObject(){
			//altijd negatief, positieve Z is naar achter
			double deltaZ = -this.getDepth();
			setZObject(deltaZ);
		}
	
		private void calculateYObject(){
			double deltaY;
			double angle;
			double depth;
			angle = this.getVerticalAngleDeviation();
			depth = this.getDepth();
			deltaY = (Math.tan(Math.toRadians(angle))*depth);
			setYObject(deltaY);
		}

		private void calculateXObject(){
			double deltaX;
			double angle;
			double depth;
			angle = this.getHorizontalAngleDeviation();
			depth = this.getDepth();
			deltaX = (Math.tan(Math.toRadians(angle))*depth);
			setXObject(deltaX);
		}

		private double[] objectPosDroneToWorld(){
			double[] droneRotated = this.vectorDroneToWorld(getXObject(), getYObject(), getZObject());
			double[] droneTranslated = VectorCalculations.sum(droneRotated, this.getPosition());
			return droneTranslated;
		}
	
		
 	private double calculateFocalDistance(){
		double focal = ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}

	private double calculateCameraHeight(){
		double height = Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getFocalDistance()*2);
		return height;
	}
	
	
	//////////MOVEMENT//////////

	/**
	 * Deze functie roep je op wanneer je naar een bepaalde positie wilt vliegen
	 * @param targetPosition
	 */
	public void updatePosition(double[] targetPosition){
		if(!isFirstMovement()){
			correctWindTranslation();
//			correctWindRotation();
		
			calculateExternalForces();
			calculateThrust(targetPosition);
			calculateWantedOrientationPos(targetPosition);
			calculateRemainingAnglesToObject();
			calculateRotationRates();
		
			this.getDrone().setThrust((float) this.getThrust());
			this.getDrone().setYawRate((float) this.getYawRate());
			this.getDrone().setPitchRate((float) this.getPitchRate());
			this.getDrone().setRollRate((float) this.getRollRate());
		
			calculateExpectedPosition();
			calculateExpectedOrientation();
		}else{
			
			calculateExternalForces();
			calculateThrust(targetPosition);
			
			this.getDrone().setThrust((float) this.getThrust());
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
	public void updateOrientation(double[] direction){
		if(!isFirstMovement()){
			correctWindTranslation();
//			correctWindRotation();
		
			calculateExternalForces();
			calculateThrust(this.getPosition());
			calculateWantedOrientationDir(direction);
			calculateRemainingAnglesToObject();
			calculateRotationRates();
		
			this.getDrone().setThrust((float) this.getThrust());
			this.getDrone().setYawRate((float) this.getYawRate());
			this.getDrone().setPitchRate((float) this.getPitchRate());
			this.getDrone().setRollRate((float) this.getRollRate());
		
			calculateExpectedPosition();
			calculateExpectedOrientation();
		}else{
			
			calculateExternalForces();
			calculateThrust(this.getPosition());
			
			this.getDrone().setThrust((float) this.getThrust());
			this.getDrone().setYawRate(0);
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			
			setFirstMovement(false);
		}
	}
	
		private void correctWindTranslation(){
			double[] deviation = VectorCalculations.sum(this.getPosition(),VectorCalculations.inverse(this.getExpectedPosition()));
			double weight = (double) this.getDrone().getWeight();
			//De voorspelde versnelling (som van thrust, gravity en wind) blijkt niet juist te zijn. 
			//De wind kan afwijken (onnauwkeurigheid,verandering van de wind,...) en zorgt ervoor dat de drone ergens anders heen vliegt dan voorspeld.
			//We berekenen hier een correctie obv de afwijking van de positie, en tellen deze hier bij op.
			//Wcorr/(2m) * deltaT^2 = X1-Xexp = deviation
			double[] Wcorr = VectorCalculations.timesScalar(deviation, 2*weight/(this.getDeltaT()*this.getDeltaT()));
			this.setWindTranslation(VectorCalculations.sum(this.getWindTranslation(), Wcorr));
		}
	
		//TODO windrotation in wereldassenstelsel
		private void correctWindRotation(){
			double[] droneAngles = {(double) this.getDrone().getHeading(), (double) this.getDrone().getPitch(), (double) this.getDrone().getRoll()};
			double[] deviation = VectorCalculations.sum(droneAngles,VectorCalculations.inverse(this.getExpectedOrientation()));
//			System.out.println("WindRotation: ");
//			for (double x: deviation)
//				System.out.println(x);
			this.setWindRotation(VectorCalculations.sum(deviation, this.getWindRotation()));
		}
	
		private void calculateExternalForces(){
			double weight = (double) this.getDrone().getWeight();
			double gravity = (double) this.getDrone().getGravity();//gravity is negatief
			double drag = (double) this.getDrone().getDrag();
			double[] gravityForce = VectorCalculations.timesScalar(new double[] {0, 1, 0}, weight*gravity);//vector is positief, want gravity is negatief
//			System.out.println("------AUTOPILOT------");
//			System.out.println("gravity1");
//			for (double x: gravityForce)
//				System.out.println(x);
			double[] windForce = this.getWindTranslation();
//			System.out.println("wind1");
//			for (double x: windForce)
//				System.out.println(x);
			double[] dragForce = VectorCalculations.timesScalar(this.getSpeed(),-drag);//- drag want tegengesteld aan de snelheidsvector
//			System.out.println("drag1");
//			for (double x: dragForce)
//				System.out.println(x);
			double[] totalExternalForce = VectorCalculations.sum(VectorCalculations.sum(gravityForce, windForce), dragForce);
			setExternalForces(totalExternalForce);
		}
		
		//berekent de thrust om naar de positie te vliegen, het dichtst bij de gevraagde positie (afhankelijk van de rotatie van de drone) 
		private void calculateThrust(double[] position){
			
			//normaal op het vlak gevormd door de thrust en de uitwendige krachten
			double[] externalForces = this.getExternalForces();
			double[] thrust = this.getDirectionOfThrust();
			double[] normal = VectorCalculations.crossProduct(externalForces, thrust);
	
			//bereken de richting naar de positie
			double[] dirToPos = VectorCalculations.normalise(getDirectionDroneToPosition(position));
	
			double result;
			if(VectorCalculations.compareVectors(position, this.getPosition())){//DOEL = HUIDIGE POSITIE
				//grootte + richting
				double[] ExtForceOnThrust = VectorCalculations.projectOnAxis(externalForces, thrust);
				//zin
				double signThrustExtForce;
				if(VectorCalculations.compareVectors(thrust,VectorCalculations.normalise(ExtForceOnThrust))){
					signThrustExtForce = -1;
				}else{
					signThrustExtForce = 1;
				}
				result = VectorCalculations.size(ExtForceOnThrust)*signThrustExtForce;
			}else if(VectorCalculations.compareVectors(normal, new double[] {0,0,0})){//THRUST STAAT VOLGENS AS VAN EXTERNAL FORCES		
				//grootte + richting = externalForces
				//zin
				double signThrustExtForce;
				if(VectorCalculations.compareVectors(thrust, VectorCalculations.normalise(externalForces))){
					signThrustExtForce = -1;
				}else{
					signThrustExtForce = 1;
				}
				
				//grootte compensatie:
				double compensateDir = 1;//externalForces*compensateDir = |compensatie| TODO mag beter
				double cosAngleDirExtForce = VectorCalculations.cosinusBetweenVectors(dirToPos, externalForces);
				if(cosAngleDirExtForce+0 == 0){//kan niet in die richting vliegen
					compensateDir = 0;
				}
				//zin compensatie:
				double[] DirOnThrust = VectorCalculations.projectOnAxis(dirToPos, thrust);
				double signThrustDir;
				if(VectorCalculations.compareVectors(thrust, VectorCalculations.normalise(DirOnThrust))){
					signThrustDir = 1;
				}else{
					signThrustDir = -1;
				}
				result = VectorCalculations.size(externalForces)*(signThrustExtForce+compensateDir*signThrustDir);
			}else{
				//we kunnen enkel vliegen binnen een vlak, dus we benaderen de gewenste richting op dat vlak
				double[] approxDir = VectorCalculations.projectOnPlane(dirToPos, normal);
	
				//binnen dat vlak kunnen we enkel vliegen tussen de minmaxthrust zone
				double maxThrust = (double) this.getDrone().getMaxThrust();
				double[] upperLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, maxThrust),externalForces);
				double[] lowerLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, -maxThrust),externalForces);
	
				//ligt approxDir binnen of buiten de kleine hoek gevormd door upperLimit en lowerLimit?
				boolean isInside = true;
				//we tekenen een xy-vlak, en we leggen lowerLimit volgens de x-as en upperLimit heeft een positieve y-waarde
//				double[] coordLow = {VectorCalculations.size(lowerLimit),0};//coordinaten lowerLimit in xy-vlak
	
				double cosLowUp = VectorCalculations.cosinusBetweenVectors(lowerLimit, upperLimit);
				double[] crossPLowUp = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,upperLimit));
//				double[] coordUp = {VectorCalculations.size(upperLimit)*cosLowUp,VectorCalculations.size(upperLimit)*(Math.sqrt(1-cosLowUp*cosLowUp))};//coordinaten upperLimit in xy-vlak
	
				double cosLowAppDir = VectorCalculations.cosinusBetweenVectors(lowerLimit, approxDir);
				if(cosLowAppDir+0>cosLowUp+0){
					isInside = false;
				}
				double[] crossPLowAppDir = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,approxDir));
				double signAppDir;
				if(!VectorCalculations.compareVectors(crossPLowUp, crossPLowAppDir)){
					isInside = false;
					signAppDir = -1;
				}else{
					signAppDir = 1;
				}
				double[] coordAppDir = {VectorCalculations.size(approxDir)*cosLowAppDir,signAppDir*VectorCalculations.size(approxDir)*(Math.sqrt(1-cosLowAppDir*cosLowAppDir))};//coordinaten approxDir in xy-vlak
				//als binnen dan grootte thrust berekenen om exact op approxDir te vliegen
				//als buiten dan dichter bij upper of lower (om op min of max te zetten)
				if(isInside){//inside
					double cosLowExtForce = VectorCalculations.cosinusBetweenVectors(lowerLimit, externalForces);
					double[] coordExtForce = {VectorCalculations.size(externalForces)*cosLowUp,VectorCalculations.size(externalForces)*(Math.sqrt(1-cosLowExtForce*cosLowExtForce))};//coordinaten externalForces in xy-vlak
	
					double cosLowThrust = VectorCalculations.cosinusBetweenVectors(lowerLimit, thrust);
					double[] crossPLowThrust = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,thrust));
					
					double signThrust;
					if(!VectorCalculations.compareVectors(crossPLowUp, crossPLowThrust)){
						signThrust = -1;
					}else{
						signThrust = 1;
					}
					double[] coordThrust = {VectorCalculations.size(thrust)*cosLowAppDir,signThrust*VectorCalculations.size(thrust)*(Math.sqrt(1-cosLowThrust*cosLowThrust))};//coordinaten thrust in xy-vlak
					//grootte + zin
					result =	(coordAppDir[0]*coordExtForce[1]-coordAppDir[1]*coordExtForce[0])/
								(coordAppDir[1]* coordThrust[0] -coordAppDir[0]* coordThrust[1] );
				}else{//outside
					//grootte+zin
					double cosUpAppDir = VectorCalculations.cosinusBetweenVectors(upperLimit, approxDir);
					if(cosLowAppDir+0>cosUpAppDir+0){//dichter bij upperLimit
						result = -maxThrust;
					}else{//dichter bij lowerLimit
						result = maxThrust;
					}
				}
			}
//			System.out.println("thrust1");
//			double[] thrustWithSize = VectorCalculations.timesScalar(thrust,result);
//			for(double x : thrustWithSize)
//				System.out.println(x);
			this.setThrust(result);
		}	
	
		private void calculateWantedOrientationPos(double[] position){
			double acceleration = this.determineAcceleration(position);
			double weight = (double) this.getDrone().getWeight();
			double[] externalForces = this.getExternalForces();
			double[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(getDirectionDroneToPosition(position)), Math.abs(acceleration)*weight);
			double[] thrustVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(externalForces));
			if (thrustVector[1] <0){//thrust heeft altijd een positieve y waarde of de drone hangt ondersteboven
				thrustVector = VectorCalculations.inverse(thrustVector);
			}
			double[] viewVector;
			if(!VectorCalculations.compareVectors(forceToPos, new double[] {0,0,0})){
				viewVector = VectorCalculations.projectOnPlane(forceToPos, thrustVector);
			}else{
				viewVector = VectorCalculations.projectOnPlane(this.getDirectionOfView(), thrustVector);
			}
			this.setWantedOrientation(new double[][] {thrustVector,viewVector});

		}

			private double determineAcceleration(double[] position) {
				double distance = this.getDistanceDroneToPosition(position);
				double speed = this.getSpeedDroneToPosition(position);
				int minMaxIndex = 1;//accelerate
				if (speed/distance >= PhysicsCalculations.getDistancespeedfactor()) {
					minMaxIndex = 0;//decelerate
				} 
				if (distance <= PhysicsCalculations.getDropdowndistance()){
					return maxAccelerationValues(position)[minMaxIndex]*distance/PhysicsCalculations.getDropdowndistance();
				}else{
					return maxAccelerationValues(position)[minMaxIndex];
				}
			}
			
				private double[] maxAccelerationValues(double[] position){
					double maxthrust = (double) this.getDrone().getMaxThrust();
					double weight = (double) this.getDrone().getWeight();
					double[] externalForces = this.getExternalForces();
					double[] direction = VectorCalculations.normalise(getDirectionDroneToPosition(position));
					
					double[] minMaxAcceleration = accelerationCalc(externalForces, maxthrust, direction, weight);
					
					return new double[] {minMaxAcceleration[0]+PhysicsCalculations.getMaxwindtranslation(),minMaxAcceleration[1]-PhysicsCalculations.getMaxwindtranslation()};//max uitwijking door de wind
				}
				
					private double[] accelerationCalc(double[] externalForces, double thrustSize, double[] direction, double weight){
						//m*acc*Dir - (Grav + Wind + Drag) = Thrust met Thrust = t*(u,v,w) en t = size => u^2+v^2+w^2=1
						//(ax+b)^2+(cx+d)^2+(ex+f)^2=1 met x = acc,
						//a = (m*dirx)/t,
						//b = -(gravx+windx)/t
						//c = (m*diry)/t,
						//d = -(gravy+windy)/t
						//e = (m*dirz)/t,
						//f = -(gravz+windz)/t
						double a = weight*direction[0]/thrustSize;
						double b = -(externalForces[0])/thrustSize;
						double c = weight*direction[1]/thrustSize;
						double d = -(externalForces[1])/thrustSize;
						double e = weight*direction[2]/thrustSize;
						double f = -(externalForces[2])/thrustSize;
						
						//oplossing is van de vorm (-b +- sqrt(b^2-4ac))/2a => (part1 +- sqrt)/part2
						double sqrt = Math.sqrt(Math.pow((2*a*b+2*c*d+2*e*f),2)-4*(a*a+c*c+e*e)*(b*b+d*d+f*f-1));
						double part1 = -(2*a*b+2*c*d+2*e*f);
						double part2 = 2*(a*a+c*c+e*e);
						double maxAcc = 0;
						double minAcc = 0;
						
						if(part2+0!=0){
							 maxAcc = (part1 + sqrt)/part2;
							 minAcc = (part1 - sqrt)/part2;
						}
						return new double[] {minAcc,maxAcc};
					}
	
		
		private void calculateWantedOrientationDir(double[] direction){
			double[] externalForces = this.getExternalForces();		
			double[] thrustVector = VectorCalculations.inverse(externalForces);// = hover
			if (thrustVector[1] <0){//thrust heeft altijd een positieve y waarde of de drone hangt ondersteboven
				thrustVector = VectorCalculations.inverse(thrustVector);
			}		
			double[] viewVector = VectorCalculations.projectOnPlane(direction, thrustVector);
			this.setWantedOrientation(new double[][] {thrustVector,viewVector});
		}
		
		//Geeft de nog te overbruggen hoeken richting het object weer. Yaw & Pitch & Roll
		private void calculateRemainingAnglesToObject() throws ArithmeticException{
			double[] thrustWanted = VectorCalculations.normalise(this.getWantedOrientation()[0]);
			double[] viewWanted = VectorCalculations.normalise(this.getWantedOrientation()[1]);
			//DroneAngles gaan niet meer nodig zijn, aangezien we gwn de nodige hoek berekenen. //TODO
			double[] inverseDroneAngles = new double[] {(double) -this.getDrone().getHeading(), (double) -this.getDrone().getPitch(), (double) -this.getDrone().getRoll()};
			
			double pitchWanted = Math.toDegrees(Math.asin(thrustWanted[2]));
			if (pitchWanted == Math.PI){
				throw new ArithmeticException("pitchWanted is PI, division by zero");
			}
			//double rollWanted = (double) Math.toDegrees(-Math.acos(thrustWanted[1]/Math.cos(Math.toRadians(pitchWanted))));  Alles in stukken gehakt, waardoor geen NaN meer.
			double cosPitch = Math.cos(Math.toRadians(pitchWanted));
				double value1 = (thrustWanted[1]/cosPitch);
				if (Math.abs(value1) > 1){
					value1 = 1*Math.signum(value1);
				}
			double rollWanted = Math.toDegrees(-Math.acos(value1));
				double value2 = -viewWanted[2]/cosPitch;
				if (Math.abs(value2) > 1){
					value2 = 1*Math.signum(value2);
				}
			double yawWanted = Math.toDegrees(Math.acos(value2));
			//richting yaw/roll
			//yaw:
			boolean samehalfyaw = true;
			boolean posheading = true;
			double[] ViewWantedOnView = VectorCalculations.projectOnAxis(viewWanted, this.getDirectionOfView());
			if(!VectorCalculations.compareVectors(VectorCalculations.normalise(ViewWantedOnView), this.getDirectionOfView())){
				samehalfyaw = false;
			}
			if (this.getDrone().getHeading() < 0) {
				posheading = false;
			}
			if((samehalfyaw && !posheading) || (!samehalfyaw && posheading)) {
				yawWanted *= -1;
			}
				
			//roll:
			boolean samehalfroll = true;
			boolean posroll = true;
			double[] ThrustWantedOnThrust = VectorCalculations.projectOnAxis(thrustWanted, this.getDirectionOfThrust());
			if(!VectorCalculations.compareVectors(VectorCalculations.normalise(ThrustWantedOnThrust), this.getDirectionOfThrust())){
				samehalfroll = false;
			}
			if (this.getDrone().getRoll() < 0) {
				posroll = false;
			}
			if((samehalfroll && !posroll) || (!samehalfroll && posroll)) {
				rollWanted *= -1;
			}
			
			double[] wantedDroneAngles = new double[] {yawWanted, pitchWanted, rollWanted};
			double[] remainingAngles = VectorCalculations.sum(inverseDroneAngles, wantedDroneAngles);
//			System.out.println("viewWanted");
//			for(double x: viewWanted)
//				System.out.println(x);
//			System.out.println("heading: "+getDrone().getHeading());
//			System.out.println("yawWanted: " + yawWanted);
//			System.out.println("remaining yaw: " + remainingAngles[0]);
			this.setRemainingAngles(remainingAngles);
		}
	
		//Geeft de nodige rotatieRates om in bepaalde tijd de remainingAngles te overbruggen.
		private void calculateRotationRates(){
			double[] remainingAngles = this.getRemainingAngles();
			if(!VectorCalculations.compareVectors(remainingAngles, new double[] {0,0,0})){
				double[] maxAngleRates = new double[]{(double) this.getDrone().getMaxYawRate(), (double) this.getDrone().getMaxPitchRate(), (double) this.getDrone().getMaxRollRate()};
				//Gecorrigeerde waarde ifv de maxwindrates.
				// Remain angles in 1 frame => 1/Delta * Remain
				double[] correctAnglesInOneFrames = VectorCalculations.timesScalar(remainingAngles, 1/getDeltaT());
				//Check vs maxRates
				if (correctAnglesInOneFrames[0]+0 != 0) {
					if (Math.abs(correctAnglesInOneFrames[0]) > Math.abs(maxAngleRates[0])) {
						this.setYawRate(maxAngleRates[0]*Math.signum(correctAnglesInOneFrames[0]));
					}else {
						this.setYawRate(correctAnglesInOneFrames[0]);
					}
				}else{
					this.setYawRate(0);
				}
//				System.out.println("yawRate: " + this.getYawRate());
//				System.out.println("----------------");
				if (correctAnglesInOneFrames[1]+0 != 0){
					if (Math.abs(correctAnglesInOneFrames[1]) > Math.abs(maxAngleRates[1])) {
						this.setPitchRate(maxAngleRates[1]*Math.signum(correctAnglesInOneFrames[1]));
					}else {
						this.setPitchRate(correctAnglesInOneFrames[1]);
					}
				}else{
					this.setPitchRate(0);
				}
				if (correctAnglesInOneFrames[2]+0 != 0) {
					if (Math.abs(correctAnglesInOneFrames[2]) > Math.abs(maxAngleRates[2])) {
						this.setRollRate(maxAngleRates[2]*Math.signum(correctAnglesInOneFrames[2]));
					}else {
						this.setRollRate(correctAnglesInOneFrames[2]);
					}
				}else{
					this.setRollRate(0);
				}
			}
		}
		
		private void calculateExpectedPosition(){
			double weight = this.getDrone().getWeight();
			double[] externalForces = this.getExternalForces();
			double[] thrust = VectorCalculations.timesScalar(this.getDirectionOfThrust(), this.getThrust());
//			System.out.println("---");
//			System.out.println("speed1");
//			for (double x: getSpeed())
//				System.out.println(x);
//			System.out.println("Position: ");
//			for (double x: getPosition())
//				System.out.println(x);
//			System.out.println("prevPos: ");
//			for (double x: getPreviousPosition())
//				System.out.println(x);
//			System.out.println("-----------");
			//(T+G+W+D)/(2m) * deltaT^2  {I} +  v0*deltaT + x0  {II} = Xexp
			double[] part1 = VectorCalculations.timesScalar(VectorCalculations.sum(thrust, externalForces), this.getDeltaT()*this.getDeltaT()/(2*weight));
			double[] acc = VectorCalculations.timesScalar(VectorCalculations.sum(thrust, externalForces), weight);
//			System.out.println("acceleration1");
//			for(double x:acc)
//				System.out.println(x);
			double[] part2 = VectorCalculations.sum(VectorCalculations.timesScalar(this.getSpeed(), this.getDeltaT()), this.getPosition());
			double[] expectedPos = VectorCalculations.sum(part1, part2);
//			System.out.println("expected position");
//			for(double x:expectedPos)
//				System.out.println(x);
			this.setExpectedPosition(expectedPos);
		}
	
		private void calculateExpectedOrientation(){
			double yawRate = this.getYawRate();
			double pitchRate = this.getPitchRate();
			double rollRate = this.getRollRate();
//			pitch = 180 * atan (accelerationX/sqrt(accelerationY*accelerationY + accelerationZ*accelerationZ))/M_PI;
			
			double[] droneAngles = {(double) this.getDrone().getHeading(), (double) this.getDrone().getPitch(), (double) this.getDrone().getRoll()};
			double[] AnglesDev = {this.getDeltaT()*yawRate+this.getWindRotation()[0], this.getDeltaT()*pitchRate+this.getWindRotation()[1], this.getDeltaT()*rollRate+this.getWindRotation()[2]};
			this.setExpectedOrientation(VectorCalculations.sum(droneAngles, AnglesDev));
		}	
				
		
	//////////VECTOR ROTATIONS//////////

	public double[] vectorDroneToWorld(double x, double y, double z){
		return vectorDroneToWorld(new double[] {x,y,z});
	}

	public double[] vectorDroneToWorld(double[] vector){
		return VectorCalculations.rotate(this.getInverseRotationMatrix(), vector);
	}

	public double[] vectorWorldToDrone(double x, double y, double z){
		return vectorWorldToDrone(new double[] {x,y,z});
	}

	public double[] vectorWorldToDrone(double[] vector){
		return VectorCalculations.rotate(this.getRotationMatrix(), vector);
	}

	
	//////////GETTERS & SETTERS//////////

	private void setDrone(Drone drone){
		this.drone = drone;
	}

	public Drone getDrone(){
		return this.drone;
	}

	public double[] getSpeed() {
		return speed;
	}

	private void setSpeed(double x, double y, double z){
		this.speed = new double[] {x, y, z};
	}

	private void setSpeed(double[] speed) {
		this.speed = speed;
	}

	public double getPreviousTime() {
		return previousTime;
	}

	private void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	public double[] getPosition() {
		return position;
	}

	private void setPosition(double x, double y, double z){
		this.position = new double[] {x, y, z};
	}

	public double[] getPreviousPosition() {
		return previousPosition;
	}

	private void setPreviousPosition(double[] previousPosition) {
		this.previousPosition = previousPosition;
	}

	public double getDeltaT() {
		return deltaT;
	}

	private void setDeltaT(double deltaT) {
		this.deltaT = deltaT;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	private void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public double getTime() {
		return this.time;
	}

	private void setTime(double time) {
		this.time = time;
	}

	public double[][] getRotationMatrix() {
		return rotationMatrix;
	}

	private void setRotationMatrix(double[][] rotationMatrix) {
		this.rotationMatrix = rotationMatrix;
	}

	public double[][] getInverseRotationMatrix() {
		return inverseRotationMatrix;
	}

	private void setInverseRotationMatrix(double[][] inverseRotationMatrix) {
		this.inverseRotationMatrix = inverseRotationMatrix;
	}

	public double[] getWindTranslation() {
		return windTranslation;
	}

	private void setWindTranslation(double x, double y, double z){
		this.windTranslation = new double[] {x,y,z};
	}

	private void setWindTranslation(double[] windTranslation) {
		this.windTranslation = windTranslation;
	}

	public double[] getExpectedPosition() {
		return expectedPosition;
	}

	private void setExpectedPosition(double x, double y, double z){
		this.expectedPosition = new double[] {x, y, z};
	}

	private void setExpectedPosition(double[] expectedPosition) {
		this.expectedPosition = expectedPosition;
	}

	public double[] getWindRotation() {
		return windRotation;
	}

	private void setWindRotation(double yawRate, double pitchRate, double rollRate){
		this.windRotation = new double[] {yawRate, pitchRate, rollRate};
	}

	private void setWindRotation(double[] windRotation) {
		this.windRotation = windRotation;
	}

	public double[] getExpectedOrientation() {
		return expectedOrientation;
	}

	private void setExpectedOrientation(double yaw, double pitch, double roll){
		this.expectedOrientation = new double[] {yaw, pitch, roll};
	}

	private void setExpectedOrientation(double[] expectedOrientation) {
		this.expectedOrientation = expectedOrientation;
	}

	public double getThrust() {
		return thrust;
	}

	private void setThrust(double thrust) {
		this.thrust = thrust;
	}

	public double getYawRate() {
		return yawRate;
	}

	private void setYawRate(double yawRate) {
		this.yawRate = yawRate;
	}

	public double getPitchRate() {
		return pitchRate;
	}

	private void setPitchRate(double pitchRate) {
		this.pitchRate = pitchRate;
	}

	public double getRollRate() {
		return rollRate;
	}

	private void setRollRate(double rollRate) {
		this.rollRate = rollRate;
	}
	
	public double getX1() {
		return X1;
	}

	private void setX1(double x1) {
		X1 = x1;
	}

	public double getX2() {
		return X2;
	}

	private void setX2(double x2) {
		X2 = x2;
	}

	public double getY() {
		return Y;
	}

	private void setY(double y) {
		Y = y;
	}

	public double getDepth() {
		return depth;
	}

	private void setDepth(double depth) {
		this.depth = depth;
	}

	public double getHorizontalAngleDeviation() {
		return horizontalAngleDeviation;
	}

	private void setHorizontalAngleDeviation(double horizontalAngleDeviation) {
		this.horizontalAngleDeviation = horizontalAngleDeviation;
	}

	public double getVerticalAngleDeviation() {
		return verticalAngleDeviation;
	}

	private void setVerticalAngleDeviation(double verticalAngleDeviation) {
		this.verticalAngleDeviation = verticalAngleDeviation;
	}

	public final double getFocalDistance() {
		return focalDistance;
	}

	public final double getCameraHeight() {
		return cameraHeight;
	}

	public double getXObject() {
		return XObject;
	}

	private void setXObject(double xObject) {
		this.XObject = xObject;
	}

	public double getYObject() {
		return YObject;
	}

	private void setYObject(double yObject) {
		this.YObject = yObject;
	}

	public double getZObject() {
		return ZObject;
	}

	private void setZObject(double zObject) {
		this.ZObject = zObject;
	}

	public double[][] getWantedOrientation() {
		return wantedOrientation;
	}

	private void setWantedOrientation(double[][] wantedOrientation) {
		this.wantedOrientation = wantedOrientation;
	}

	public double[] getRemainingAngles() {
		return remainingAngles;
	}

	private void setRemainingAngles(double[] remainingAngles) {
		this.remainingAngles = remainingAngles;
	}

	public static double getMaxwindtranslation() {
		return maxWindTranslation;
	}

	public static double[] getMaxwindrotationrate() {
		return maxWindRotationRate;
	}

	public double[] getDirectionOfView() {
		return directionOfView;
	}
	
	private void setDirectionOfView(double[] directionOfView) {
		this.directionOfView = directionOfView;
	}

	public boolean isFirstMovement() {
		return firstMovement;
	}
	
	private void setFirstMovement(boolean firstMovement) {
		this.firstMovement = firstMovement;
	}	

	public double[] getExternalForces() {
		return externalForces;
	}

	public void setExternalForces(double[] externalForces) {
		this.externalForces = externalForces;
	}
	
	public static double getDistancespeedfactor() {
		return distanceSpeedFactor;
	}

	public static double getDropdowndistance() {
		return dropdownDistance;
	}

	public double[] getDirectionOfThrust() {
		return directionOfThrust;
	}

	public void setDirectionOfThrust(double[] directionOfThrust) {
		this.directionOfThrust = directionOfThrust;
	}

}


