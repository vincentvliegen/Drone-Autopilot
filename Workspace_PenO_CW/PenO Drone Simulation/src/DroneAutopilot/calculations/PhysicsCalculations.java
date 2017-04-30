package DroneAutopilot.calculations;


import java.util.Arrays;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private Drone drone;

	//Drone
	private double time;
	private double previousTime;
	private double deltaT;
	
	private double[] position;
	private double[] previousPosition;
	private double[] expectedPosition;
	
	private final static double[][] orientationWorld = new double[][] {{1,0,0},{0,1,0},{0,0,1}};
	private double[][] orientationDrone;
	private double[][] previousOrientationDrone;
	private double[][] expectedOrientationDrone;
	private double[] directionOfView;
	private double[] directionOfThrust;
	
	private double[] speed;
	private double[] previousSpeed;
	
	private double[] externalForces;
	
	private double[] windTranslation;
	private double[] windRotation;
	private final double maxWindTranslation;
	private static final double[] maxWindRotationRate = new double[]{0.5, 0.5, 0.5};
	
	private boolean firstTime;

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
	private double[][] wantedOrientation;
	private double[] remainingAngles;
	private double thrust;
	private double yawRate;
	private double pitchRate;
	private double rollRate;
	private final static double distanceSpeedFactor = 0.2;
	private final static double dropdownDistance = 8;
	

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
//		this.setFirstMovement(true);
		this.maxWindTranslation = 0.05*this.getDrone().getDrag();
	}


	//////////DRONE//////////

	public void updateDroneData(){

		//update previous
		this.setPreviousTime(this.getTime());
		this.setPreviousPosition(this.getPosition());
		this.setPreviousOrientationDrone(this.getOrientationDrone());
		this.setPreviousSpeed(this.getSpeed());

		//update current
		this.setPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
		this.calculateOrientation();
		this.calculateDirectionOfView();
		this.calculateDirectionOfThrust();
		this.setTime((double) this.getDrone().getCurrentTime());
		
		if(!isFirstTime()){//Delta t is nog onbekend
			this.setDeltaT(this.getTime()-this.getPreviousTime());
			this.calculateSpeed();
			calculateExpectedPosition();
			calculateExpectedOrientation();
			
//			correctWindTranslation();
//			correctWindRotation();
		}
		
		this.calculateExternalForces();
		setFirstTime(false);
		
//		System.out.println("x expected: " + Arrays.toString(VectorCalculations.inverse(this.getExpectedOrientationDrone()[2])));
//		System.out.println("y expected: " + Arrays.toString(this.getExpectedOrientationDrone()[1]));
//		System.out.println("z expected: " + Arrays.toString(this.getExpectedOrientationDrone()[0]));
//		
//		System.out.println("x current: " + Arrays.toString(VectorCalculations.inverse(this.getOrientationDrone()[2])));
//		System.out.println("y current: " + Arrays.toString(this.getOrientationDrone()[1]));
//		System.out.println("z current: " + Arrays.toString(this.getOrientationDrone()[0]));
//		
//		double[] errorx = VectorCalculations.sum(this.getExpectedOrientationDrone()[2], VectorCalculations.inverse(this.getOrientationDrone()[2]));
//		double[] errory = VectorCalculations.sum(VectorCalculations.inverse(this.getExpectedOrientationDrone()[1]), this.getOrientationDrone()[1]);
//		double[] errorz = VectorCalculations.sum(VectorCalculations.inverse(this.getExpectedOrientationDrone()[0]), this.getOrientationDrone()[0]);
//
//		
//		System.out.println("error x current expected: " + Arrays.toString(errorx));
//		System.out.println("error y current expected: " + Arrays.toString(errory));
//		System.out.println("error z current expected: " + Arrays.toString(errorz));
	}

		private void calculateOrientation(){
			double[][] axes = VectorCalculations.rotateAxes(PhysicsCalculations.getOrientationworld(), this.getDrone().getHeading(), this.getDrone().getPitch(), this.getDrone().getRoll());
			setOrientationDrone(axes);
		}
	
		private void calculateDirectionOfView(){
			double[] view = VectorCalculations.inverse(getOrientationDrone()[2]);
			setDirectionOfView(view);
		}
	
		private void calculateDirectionOfThrust(){
			double[] thrust = getOrientationDrone()[1];
			setDirectionOfThrust(thrust);
		}
	
		private void calculateExpectedPosition(){
			double weight = this.getDrone().getWeight();
			double[] externalForces = this.getExternalForces();
			double[] thrust = VectorCalculations.timesScalar(this.getPreviousOrientationDrone()[1], this.getThrust());//previous thrust en thrustgrootte
			//(T+G+W+D)/(2m) * deltaT^2  {I} +  v0*deltaT + x0  {II} = Xexp
			double[] part1 = VectorCalculations.timesScalar(VectorCalculations.sum(thrust, externalForces), this.getDeltaT()*this.getDeltaT()/(2*weight));
			double[] acc = VectorCalculations.timesScalar(VectorCalculations.sum(thrust, externalForces), weight);
			double[] part2 = VectorCalculations.sum(VectorCalculations.timesScalar(this.getPreviousSpeed(), this.getDeltaT()), this.getPreviousPosition());
			double[] expectedPos = VectorCalculations.sum(part1, part2);
			this.setExpectedPosition(expectedPos);
		}
	
		private void calculateExpectedOrientation(){
			double yawRate = this.getYawRate();
			double pitchRate = this.getPitchRate();
			double rollRate = this.getRollRate();
			double[][] expectedOrientationNoWind = VectorCalculations.rotateAxes(previousOrientationDrone, yawRate*getDeltaT(), pitchRate*getDeltaT(), rollRate*getDeltaT());
			double[][] expectedOrientationWithWind = this.addRotationWind(expectedOrientationNoWind);	
			this.setExpectedOrientationDrone(expectedOrientationWithWind);
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
	
		private void correctWindRotation(){
			double[][] expectedOrientation = this.getExpectedOrientationDrone();
			double[][] expectedOrientationNoWind = removeRotationWind(expectedOrientation);
			//expectedOrientationNoWind + windrotatie = currentOrientation
			//de assen van EONW verandert na de windrotatie in de assen van CO (currentOrientation)
			//een vector (a,b,c) in EONW wordt na wind (a,b,c) in CO
			//we bepalen de vectoren in EONW die evenwijdig liggen aan worldOrientation
			double[] xworldInEONW = VectorCalculations.changeCoordinateSystem(PhysicsCalculations.getOrientationworld()[0], PhysicsCalculations.getOrientationworld(), expectedOrientationNoWind);
			double[] yworldInEONW = VectorCalculations.changeCoordinateSystem(PhysicsCalculations.getOrientationworld()[1], PhysicsCalculations.getOrientationworld(), expectedOrientationNoWind);
			double[] zworldInEONW = VectorCalculations.changeCoordinateSystem(PhysicsCalculations.getOrientationworld()[2], PhysicsCalculations.getOrientationworld(), expectedOrientationNoWind);
			//na windrotatie hebben deze assen (ai,bi,ci), orientatie (ai,bi,ci) in CO
			//deze assen in CO kunnen weer omgezet worden in WO
			double[] xWithWind = this.droneVectorToWorldVector(xworldInEONW);
			double[] yWithWind = this.droneVectorToWorldVector(yworldInEONW);
			double[] zWithWind = this.droneVectorToWorldVector(zworldInEONW);
			
			//nu kunnen we de windrotatie (vast assenstelsel) bepalen adhv de totale rotatiematrix (zie mupad)
			//yWithWind[2] = -sin(p)
			double pitch = Math.asin(-yWithWind[2]);
			//yWithWind[1] = cos(p)cos(r) -> |r|
			//yWithWind[0] = cos(p)sin(r) -> sign(r)
			double roll = Math.acos(yWithWind[1]/Math.cos(pitch))*Math.signum(yWithWind[0]/Math.cos(pitch));
			//xWithWind[2] = cos(p)sin(y) -> sign(y)
			//zWithWind[2] = cos(p)cos(y) -> |y|			
			double yaw = Math.acos(zWithWind[2]/Math.cos(pitch))*Math.signum(xWithWind[2]/Math.cos(pitch));;
						
			//radians -> degrees
			pitch = Math.toDegrees(pitch);
			yaw = Math.toDegrees(yaw);
			roll = Math.toDegrees(roll);
			
			setWindRotation(new double[] {yaw,pitch,roll});	
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
			double[] droneRotated = droneVectorToWorldVector(new double[] {this.getXObject(),this.getYObject(),this.getZObject()});
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
	
	public void updateMovement(double[] targetPosition, double[] direction){
		calculateThrust(targetPosition);
		calculateWantedOrientation(targetPosition,direction);
		calculateRemainingAnglesToObject();
		calculateRotationRates();
	
		this.getDrone().setThrust((float) this.getThrust());
		this.getDrone().setYawRate((float) this.getYawRate());
		this.getDrone().setPitchRate((float) this.getPitchRate());
		this.getDrone().setRollRate((float) this.getRollRate());
	}

	public void updateMovement(double[] targetPosition){
		updateMovement(targetPosition,getDirectionDroneToPosition(targetPosition));
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
				double breaking=1;
				if(this.isPossibleToStopThrust(position) == false){
					breaking = -1;
				} 
				result = VectorCalculations.size(externalForces)*(signThrustExtForce+compensateDir*signThrustDir*breaking);
				
			}else{
				//we kunnen enkel vliegen binnen een vlak, dus we benaderen de gewenste richting op dat vlak
				double[] approxToDir = VectorCalculations.projectOnPlane(dirToPos, normal);//naar de positie
				double[] approxFromDir = VectorCalculations.inverse(approxToDir);//weg van de positie
	
				//binnen dat vlak kunnen we enkel vliegen tussen de minmaxthrust zone
				double maxThrust = (double) this.getDrone().getMaxThrust();
				double[] upperLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, maxThrust),externalForces);
				double[] lowerLimit = VectorCalculations.sum(VectorCalculations.timesScalar(thrust, -maxThrust),externalForces);
	
				//we tekenen een xy-vlak, en we leggen lowerLimit volgens de x-as en upperLimit heeft een positieve y-waarde
//				double[] coordLow = {VectorCalculations.size(lowerLimit),0};//coordinaten lowerLimit in xy-vlak
				double cosLowUp = VectorCalculations.cosinusBetweenVectors(lowerLimit, upperLimit);
				double[] crossPLowUp = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,upperLimit));
//				double[] coordUp = {VectorCalculations.size(upperLimit)*cosLowUp,VectorCalculations.size(upperLimit)*(Math.sqrt(1-cosLowUp*cosLowUp))};//coordinaten upperLimit in xy-vlak
	
				//TO POSITION
				//ligt approxDir binnen of buiten de kleine hoek gevormd door upperLimit en lowerLimit?
				boolean toDirIsInside = true;
				double cosLowAppToDir = VectorCalculations.cosinusBetweenVectors(lowerLimit, approxToDir);
				if(cosLowAppToDir+0<cosLowUp+0){
					toDirIsInside = false;
				}
				double[] crossPLowAppToDir = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,approxToDir));
				double signAppToDir;
				if(!VectorCalculations.compareVectors(crossPLowUp, crossPLowAppToDir)){
					toDirIsInside = false;
					signAppToDir = -1;
				}else{
					signAppToDir = 1;
				}
				double[] coordAppToDir = {VectorCalculations.size(approxToDir)*cosLowAppToDir,signAppToDir*VectorCalculations.size(approxToDir)*(Math.sqrt(1-cosLowAppToDir*cosLowAppToDir))};//coordinaten approxDir in xy-vlak
				
				//AWAY FROM POSITION
				boolean fromDirIsInside = true;
				double cosLowAppFromDir = VectorCalculations.cosinusBetweenVectors(lowerLimit, approxFromDir);
				if(cosLowAppFromDir+0<cosLowUp+0){
					fromDirIsInside = false;
				}
				double[] crossPLowAppFromDir = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,approxFromDir));
				double signAppFromDir;
				if(!VectorCalculations.compareVectors(crossPLowUp, crossPLowAppFromDir)){
					fromDirIsInside = false;
					signAppFromDir = -1;
				}else{
					signAppFromDir = 1;
				}
				double[] coordAppFromDir = {VectorCalculations.size(approxFromDir)*cosLowAppFromDir,signAppFromDir*VectorCalculations.size(approxFromDir)*(Math.sqrt(1-cosLowAppFromDir*cosLowAppFromDir))};//coordinaten approxDir in xy-vlak

				
				
				//als binnen dan grootte thrust berekenen om exact op approxDir te vliegen
				//als buiten dan dichter bij upper of lower (om op min of max te zetten)
				if(toDirIsInside || fromDirIsInside){//to dir inside
					double cosLowExtForce = VectorCalculations.cosinusBetweenVectors(lowerLimit, externalForces);
					double[] coordExtForce = {VectorCalculations.size(externalForces)*cosLowExtForce,VectorCalculations.size(externalForces)*(Math.sqrt(1-cosLowExtForce*cosLowExtForce))};//coordinaten externalForces in xy-vlak
	
					double cosLowThrust = VectorCalculations.cosinusBetweenVectors(lowerLimit, thrust);
					double[] crossPLowThrust = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit,thrust));
					
					double signThrust;
					if(!VectorCalculations.compareVectors(crossPLowUp, crossPLowThrust)){
						signThrust = -1;
					}else{
						signThrust = 1;
					}
					double[] coordThrust = {VectorCalculations.size(thrust)*cosLowThrust,signThrust*VectorCalculations.size(thrust)*(Math.sqrt(1-cosLowThrust*cosLowThrust))};//coordinaten thrust in xy-vlak					
					
					if(toDirIsInside){//FLY TO DIRECTION
						result =	(coordAppToDir[0]*coordExtForce[1]-coordAppToDir[1]*coordExtForce[0])/
									(coordAppToDir[1]* coordThrust[0] -coordAppToDir[0]* coordThrust[1] );
					}else{//FLY AWAY FROM DIRECTION
						result =	(coordAppFromDir[0]*coordExtForce[1]-coordAppFromDir[1]*coordExtForce[0])/
									(coordAppFromDir[1]* coordThrust[0] -coordAppFromDir[0]* coordThrust[1] );
					}

				}else{//outside -> vlieg zo goed mogelijk naar toDir
					//grootte+zin
					double cosUpAppDir = VectorCalculations.cosinusBetweenVectors(upperLimit, approxToDir);
					if(cosLowAppToDir+0>cosUpAppDir+0){//dichter bij upperLimit
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
	
			//Bereken of het mogelijk is om de speed in de richting van de thrust tegen te werken. (motor uit of 2x de externalforces)
			private boolean isPossibleToStopThrust(double[] position){
				double speed = this.getSpeedDroneToPosition(position);
				double thrust = Math.abs(2*this.getExternalForces()[1]);
				if(position[1] > this.getDrone().getY()){
					thrust = 0;
				}
				double accelerationY = Math.abs((thrust-this.getDrone().getWeight()*9.81)/this.getDrone().getWeight());
				double distance = this.getDistanceDroneToPosition(position);
	
				double breakingDistance = 1.3*(Math.pow(speed, 2))/(2*accelerationY); // 1.3* als veiligheidsfactor
				if(distance<breakingDistance && speed>0){
					//System.out.println("BREAKING");
					return false;
				}else{
					return true;
				}	
			}
	
		private void calculateWantedOrientation(double[] position, double[] direction){
				double acceleration = this.determineAcceleration(position);
				double weight = (double) this.getDrone().getWeight();
				double[] externalForces = this.getExternalForces();
				double[] directionToPos = getDirectionDroneToPosition(position);
				double[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(directionToPos), acceleration*weight);
				double[] thrustVector = VectorCalculations.sum(forceToPos, VectorCalculations.inverse(externalForces));
				if (thrustVector[1] <0){//thrust heeft altijd een positieve y waarde of de drone hangt ondersteboven
					thrustVector = VectorCalculations.inverse(thrustVector);
				}
				double[] viewVector;
				viewVector = VectorCalculations.projectOnPlane(direction, thrustVector);
				if(VectorCalculations.compareVectors(viewVector, new double[] {0,0,0})){
					viewVector = VectorCalculations.projectOnPlane(this.getDirectionOfView(), thrustVector);
				}
				this.setWantedOrientation(new double[][] {thrustVector,viewVector});
			}
		
			private double determineAcceleration(double[] position) {
                double distance = this.getDistanceDroneToPosition(position);
                double speed = this.getSpeedDroneToPosition(position);
                double[] acceleration = maxAccelerationValues(position);                
                int minMaxIndex = 1;//accelerate
                if (speed/distance >= PhysicsCalculations.getDistancespeedfactor()) {
                    minMaxIndex = 0;//decelerate
                } 
//                if(minMaxIndex == 1){
//                    System.out.println("accelerate");
//                }else{
//                    System.out.println("decelerate");
//                }
//                System.out.println("minmaxAcc:= {" + acceleration[0] + ", " + acceleration[1] + "}");
//                
                if (distance <= PhysicsCalculations.getDropdowndistance()){
                    //demping ifv afstand
                    double distanceDamping = Math.pow(distance/PhysicsCalculations.getDropdowndistance(),2);
                    return acceleration[minMaxIndex]*distanceDamping;
                }else{
                    return acceleration[minMaxIndex];
                }
			}
							
				private double[] maxAccelerationValues(double[] position){
					double maxthrust = (double) this.getDrone().getMaxThrust();
					double weight = (double) this.getDrone().getWeight();
					double[] externalForces = this.getExternalForces();
					double[] direction = VectorCalculations.normalise(getDirectionDroneToPosition(position));
					
					double[] minMaxAcceleration = accelerationCalc(externalForces, maxthrust, direction, weight);
					//change of wind;
					minMaxAcceleration[0] += this.getMaxWindTranslation()/this.getDrone().getWeight();
					minMaxAcceleration[1] -= this.getMaxWindTranslation()/this.getDrone().getWeight();
					
					//om een overcompensatie (bij verandering van accelerate naar decelerate of omgekeerd) te voorkomen, moeten de versnellingen van dezelfde grootte zijn
					if((Math.signum(minMaxAcceleration[0]) == 1 || Math.signum(minMaxAcceleration[1]) == -1)&&!(VectorCalculations.compareVectors(direction, new double[] {0,0,0}))){
						System.out.println("thrust can not compensate external forces");
					}else if(Math.abs(minMaxAcceleration[0])>Math.abs(minMaxAcceleration[1])){
						minMaxAcceleration[0] = -minMaxAcceleration[1];
					}else{
						minMaxAcceleration[1] = -minMaxAcceleration[0];
					}
					
					return minMaxAcceleration;
				}
				
					private double[] accelerationCalc(double[] externalForces, double thrustSize, double[] direction, double weight){
//						double[] snelheidscompensatie = VectorCalculations.timesScalar(this.getSpeed(),3);
						//m*acc*Dir - (Grav + Wind + Drag) = Thrust met Thrust = t*(u,v,w) en t = size => u^2+v^2+w^2=1
						//(ax+b)^2+(cx+d)^2+(ex+f)^2=1 met x = acc,
						//a = (m*dirx)/t,
						//b = -(gravx+windx)/t
						//c = (m*diry)/t,
						//d = -(gravy+windy)/t
						//e = (m*dirz)/t,
						//f = -(gravz+windz)/t
						double a = weight*direction[0]/thrustSize;
//						double b = -(externalForces[0]+snelheidscompensatie[0])/thrustSize;
						double b = -(externalForces[0])/thrustSize;
						double c = weight*direction[1]/thrustSize;
//						double d = -(externalForces[1]+snelheidscompensatie[1])/thrustSize;
						double d = -(externalForces[1])/thrustSize;
						double e = weight*direction[2]/thrustSize;
//						double f = -(externalForces[2]+snelheidscompensatie[2])/thrustSize;
						double f = -(externalForces[2])/thrustSize;
						
						//oplossing is van de vorm (-b +- sqrt(b^2-4ac))/2a => (part1 +- sqrt)/part2
						double sqrt = Math.sqrt(Math.pow((2*a*b+2*c*d+2*e*f),2)-4*(a*a+c*c+e*e)*(b*b+d*d+f*f-1));
						double part1 = -(2*a*b+2*c*d+2*e*f);
						double part2 = 2*(a*a+c*c+e*e);
						double maxAcc = 0;
						double minAcc = -0;
						
						if(part2+0!=0){
							 maxAcc = (part1 + sqrt)/part2;
							 minAcc = (part1 - sqrt)/part2;
						}
						return new double[] {minAcc,maxAcc};
					}
	
				
		//Geeft de nog te overbruggen hoeken richting het object weer. Yaw & Pitch & Roll
		private void calculateRemainingAnglesToObject(){
			double[][] currentOrientation = new double[][] {{1,0,0},{0,1,0},{0,0,1}}; //x,y & z van drone
			double[][] WantedOrientation = this.getWantedOrientation();

			//wind in rekening brengen
			WantedOrientation[0] = this.removeRotationWind(WantedOrientation[0]);
			WantedOrientation[1] = this.removeRotationWind(WantedOrientation[1]);
			
			//omzetten naar droneCoordinaten
			WantedOrientation[0] = VectorCalculations.normalise(this.worldVectorToDroneVector(WantedOrientation[0]));
			WantedOrientation[1] = VectorCalculations.normalise(this.worldVectorToDroneVector(WantedOrientation[1]));		

			//Yaw, pitch roll bepalen
			
			//Yaw kan berekend worden adhv de viewvector
			double[] wantedViewOnYawPlane = new double[]{WantedOrientation[1][0],0,WantedOrientation[1][2]};
			double yawWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedViewOnYawPlane, VectorCalculations.inverse(currentOrientation[2]))));
			//sign yaw:
			double[] crossProductViewXaxis = new double[] {0,-1,0}; // = VectorCalculations.crossProduct(VectorCalculations.inverse(currentOrientation[2]), currentOrientation[0]);
			double[] crossProductViewWantedView = VectorCalculations.normalise(VectorCalculations.crossProduct(VectorCalculations.inverse(currentOrientation[2]), wantedViewOnYawPlane));
			if(!VectorCalculations.compareVectors(crossProductViewWantedView, crossProductViewXaxis)){
				yawWanted1 *= -1;
			}
			
			double yawWanted2 = yawWanted1+180;
			if(yawWanted2 > 180){
				yawWanted2 -= 360;
			}
					
			//Nu assenstelsel draaien zodanig de pitch berekend kan worden adhv cosinus tss de vectoren
			double[][] currentOrientation1 = VectorCalculations.yawAxes(currentOrientation, yawWanted1);
			double[][] currentOrientation2 = VectorCalculations.yawAxes(currentOrientation, yawWanted2);
			double pitchWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(WantedOrientation[1], VectorCalculations.inverse(currentOrientation1[2]))));
			//sign pitch:
			if(WantedOrientation[1][1]>0){
				pitchWanted1*=-1;
			}
			
			double pitchWanted2 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(WantedOrientation[1], VectorCalculations.inverse(currentOrientation2[2]))));
			//sign pitch:
			if(WantedOrientation[1][1]>0){
				pitchWanted2*=-1;
			}
			
			//Nu staan de viewvectoren gelijk. Er moet dus enkel nog een rotatie gebeuren rond de rollvector om zo de thrustWanted en thrust gelijk te krijgen.
			// cosinus tss vectoren
			currentOrientation1 = VectorCalculations.pitchAxes(currentOrientation1, pitchWanted1);
			currentOrientation2 = VectorCalculations.pitchAxes(currentOrientation2, pitchWanted2);
			double rollWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(WantedOrientation[0], currentOrientation1[1])));
			//sign roll:
			if(WantedOrientation[0][0]<0){
				rollWanted1*=-1;
			}
			double rollWanted2 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(WantedOrientation[0], currentOrientation2[1])));
			//sign roll:
			if(WantedOrientation[0][0]<0){
				rollWanted2*=-1;
			}
			
			//1 of 2?
			double yawWanted;
			double pitchWanted;
			double rollWanted;
			if(Math.abs(pitchWanted1)+Math.abs(rollWanted1) <= 180){
				yawWanted = yawWanted1;
				pitchWanted = pitchWanted1;
				rollWanted = rollWanted1;
			}else{
				yawWanted = yawWanted2;
				pitchWanted = pitchWanted2;
				rollWanted = rollWanted2;
			}
			
			double[] remainingAngles = new double[] {yawWanted, pitchWanted, rollWanted};
			this.setRemainingAngles(remainingAngles);

//			System.out.println("viewWanted");
//			for(double x: viewWanted)
//				System.out.println(x);
//			System.out.println("position: {" + this.getPosition()[0] + ", " + this.getPosition()[1] + ", " + this.getPosition()[2] + "}");
//			System.out.println("yaw: "+ remainingAngles[0]);
//			System.out.println("pitch: "+ remainingAngles[1]);
//			System.out.println("roll: "+ remainingAngles[2]);
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
				if (correctAnglesInOneFrames[1]+0 != 0 /*&& Math.abs(correctAnglesInOneFrames[0]) < Math.abs(maxAngleRates[0])*/){
					if (Math.abs(correctAnglesInOneFrames[1]) > Math.abs(maxAngleRates[1])) {
						this.setPitchRate(maxAngleRates[1]*Math.signum(correctAnglesInOneFrames[1]));
					}else {
						this.setPitchRate(correctAnglesInOneFrames[1]);
					}
				}else{
					this.setPitchRate(0);
				}
				if (correctAnglesInOneFrames[2]+0 != 0 /*&& Math.abs(correctAnglesInOneFrames[0]) < Math.abs(maxAngleRates[0]) && Math.abs(correctAnglesInOneFrames[1]) < Math.abs(maxAngleRates[1])*/) {
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
		
			
		
	//////////VECTOR ROTATIONS//////////
		
	public double[] droneVectorToWorldVector(double[] vector){
		return VectorCalculations.changeCoordinateSystem(vector, this.getOrientationDrone(), PhysicsCalculations.getOrientationworld());
	}
		
	public double[] worldVectorToDroneVector(double[] vector){
		return VectorCalculations.changeCoordinateSystem(vector, PhysicsCalculations.getOrientationworld(), this.getOrientationDrone());
	}
	
	public double[] removeRotationWind(double[] vector){
		double[] vectorNoRoll = VectorCalculations.rotateVectorAroundAxis(vector, PhysicsCalculations.getOrientationworld()[2], -this.getWindRotation()[2]);
		double[] vectorNoRollNoPitch = VectorCalculations.rotateVectorAroundAxis(vectorNoRoll, PhysicsCalculations.getOrientationworld()[0], -this.getWindRotation()[1]);
		double[] vectorNoWind= VectorCalculations.rotateVectorAroundAxis(vectorNoRollNoPitch, PhysicsCalculations.getOrientationworld()[1], -this.getWindRotation()[0]);
		return vectorNoWind;
	}
	
	public double[][] removeRotationWind(double[][] axes){
		double[][] axesNoRoll = VectorCalculations.rotateAxesAroundAxis(axes, PhysicsCalculations.getOrientationworld()[2], -this.getWindRotation()[2]);
		double[][] axesNoRollNoPitch = VectorCalculations.rotateAxesAroundAxis(axesNoRoll, PhysicsCalculations.getOrientationworld()[0], -this.getWindRotation()[1]);
		double[][] axesNoWind= VectorCalculations.rotateAxesAroundAxis(axesNoRollNoPitch, PhysicsCalculations.getOrientationworld()[1], -this.getWindRotation()[0]);
		return axesNoWind;
	}
	
	public double[] addRotationWind(double[] vector){
		double[] vectorWithYaw = VectorCalculations.rotateVectorAroundAxis(vector, PhysicsCalculations.getOrientationworld()[1], this.getWindRotation()[0]);
		double[] vectorWithYawPitch = VectorCalculations.rotateVectorAroundAxis(vectorWithYaw, PhysicsCalculations.getOrientationworld()[0], this.getWindRotation()[1]);
		double[] vectorWithWind = VectorCalculations.rotateVectorAroundAxis(vectorWithYawPitch, PhysicsCalculations.getOrientationworld()[2], this.getWindRotation()[2]);
		return vectorWithWind;
	}
	
	public double[][] addRotationWind(double[][] axes){
		double[][] axesWithYaw = VectorCalculations.rotateAxesAroundAxis(axes, PhysicsCalculations.getOrientationworld()[1], this.getWindRotation()[0]);
		double[][] axesWithYawPitch = VectorCalculations.rotateAxesAroundAxis(axesWithYaw, PhysicsCalculations.getOrientationworld()[0], this.getWindRotation()[1]);
		double[][] axesWithWind = VectorCalculations.rotateAxesAroundAxis(axesWithYawPitch, PhysicsCalculations.getOrientationworld()[2], this.getWindRotation()[2]);
		return axesWithWind;
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

	private void setPosition(double[] position){
		this.position = position;
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

	public double getMaxWindTranslation() {
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

	public double[][] getOrientationDrone() {
		return orientationDrone;
	}
	
	public void setOrientationDrone(double[][] orientationDrone) {
		this.orientationDrone = orientationDrone;
	}
	
	public static double[][] getOrientationworld() {
		return orientationWorld;
	}

	public double[] getPreviousSpeed() {
		return previousSpeed;
	}
	
	public void setPreviousSpeed(double[] previousSpeed) {
		this.previousSpeed = previousSpeed;
	}
	
	public double[][] getPreviousOrientationDrone() {
		return previousOrientationDrone;
	}
	
	public void setPreviousOrientationDrone(double[][] previousOrientationDrone) {
		this.previousOrientationDrone = previousOrientationDrone;
	}
	
	public double[][] getExpectedOrientationDrone() {
		return expectedOrientationDrone;
	}
	
	public void setExpectedOrientationDrone(double[][] expectedOrientationDrone) {
		this.expectedOrientationDrone = expectedOrientationDrone;
	}
	
}


