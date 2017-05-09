package DroneAutopilot.calculations;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.algoritmes.AvoidObstacles;
import p_en_o_cw_2016.Drone;

public class PhysicsCalculations{

	private DroneAutopilot droneAutopilot;
	private Drone drone;
	private AvoidObstacles avoidObstacle;

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
	private final static double maxSpeedDistanceRatio = 0.2;
	private final static double slowDownDistance = 8;
	
	
	//////////CONSTRUCTOR//////////
	
	public PhysicsCalculations(DroneAutopilot droneAutopilot){
		this.setDroneAutopilot(droneAutopilot);
		this.setDrone(getDroneAutopilot().getDrone());
		this.setAvoidObstacle(avoidObstacle);
		
		this.setTime((double) this.getDrone().getCurrentTime());
		this.setPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
		this.setSpeed(0,0,0);
		this.setFirstTime(true);
		
		this.focalDistance = calculateFocalDistance();
		this.cameraHeight = calculateCameraHeight();
		
		this.setWindTranslation(0,0,0);
		this.setWindRotation(0,0,0);
		this.setExpectedPosition((double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ());
	}
	
	 	private double calculateFocalDistance(){
			double focal = ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
			return focal;
		}
	
		private double calculateCameraHeight(){
			double height = Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getFocalDistance()*2);
			return height;
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
		double cosAngle = VectorCalculations.cosinusBetweenVectors(VectorCalculations.sum(position, VectorCalculations.inverse(this.getPosition())),this.getSpeed());
		return cosAngle*speedDrone;
	}

	public double[] getLinearSpeedDroneToPosition(double[] position){
		if(VectorCalculations.compareVectors(position, this.getPosition()))
			return new double[] {0,0,0};
		double[] linear = VectorCalculations.projectOnAxis(this.getSpeed(), getDirectionDroneToPosition(position));
		return linear;
	}
	
	public double[] getTransverseSpeedDroneToPosition(double[] position){
		if(VectorCalculations.compareVectors(position, this.getPosition()))
			return new double[] {0,0,0};
		double[] transverse = VectorCalculations.projectOnPlane(this.getSpeed(), getDirectionDroneToPosition(position));
		return transverse;
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
		
	
	//////////MOVEMENT//////////
	
	public void updateMovement(double[] targetPosition, double[] direction){
		if(isFirstTime()){
			calculateThrust(targetPosition);
			
			this.getDrone().setThrust((float) this.getThrust());
			this.getDrone().setYawRate(0);
			this.getDrone().setPitchRate(0);
			this.getDrone().setRollRate(0);
			this.setFirstTime(false);
		}else{
			calculateThrust(targetPosition);
			calculateWantedOrientation(targetPosition,direction);
			calculateRemainingAnglesToObject();
			calculateRotationRates();
		
			this.getDrone().setThrust((float) this.getThrust());
			this.getDrone().setYawRate((float) this.getYawRate());
			this.getDrone().setPitchRate((float) this.getPitchRate());
			this.getDrone().setRollRate((float) this.getRollRate());
		}
	}

	public void updateMovement(double[] targetPosition){
		updateMovement(targetPosition,getDirectionDroneToPosition(targetPosition));
	}
			 
		private void calculateThrust(double[] position){
			
			//normaal op het vlak gevormd door de thrust en de uitwendige krachten
			double[] externalForces = this.getExternalForces();
			double[] thrust = this.getDirectionOfThrust();
			double[] normal = VectorCalculations.crossProduct(externalForces, thrust);
	
			//bereken de richting naar de positie
			double[] dirToPos = VectorCalculations.normalise(getDirectionDroneToPosition(position));
			
			
//			double[] linearSpeed = getLinearSpeedDroneToPosition(position);
//			double[] transverseSpeed = getTransverseSpeedDroneToPosition(position);
//			
//			System.out.println("---------------");
//			System.out.println("linearSpeed: " + Arrays.toString(linearSpeed));
//			System.out.println("transverseSpeed: " + Arrays.toString(transverseSpeed));
			
			
			double result;
			if(VectorCalculations.compareVectors(position, this.getPosition()) || isFirstTime()){//DOEL = HUIDIGE POSITIE || EERSTE FRAME = GEEN VERPLAATSINGEN
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
								
				//we kunnen alleen vliegen volgens de thrustas, dus we benaderen de positie op thrustas en berekenen snelheid volgens thrustas;
				double[] direction = VectorCalculations.projectOnAxis(getDirectionDroneToPosition(position), thrust);
				boolean inDirectionOfThrust = VectorCalculations.compareVectors(thrust, VectorCalculations.normalise(direction));
				double distance = VectorCalculations.size(direction);
				double speed = VectorCalculations.size(VectorCalculations.projectOnAxis(getSpeed(), thrust));
				if(!inDirectionOfThrust){
					speed *= -1;
				}
				double currentThrust = VectorCalculations.size(externalForces)*signThrustExtForce;//compenseert external forces
				double maxThrust = this.getDrone().getMaxThrust();
				double weight = this.getDrone().getWeight();
				
				double minAcceleration = -(maxThrust - Math.abs(currentThrust))/weight;
				double maxAcceleration = (maxThrust - Math.abs(currentThrust))/weight;
				double[] acceleration = new double[] {minAcceleration,maxAcceleration};
                
				double wantedAcceleration;
				
				int minMaxIndex = 1;//accelerate
                if (speed/distance >= PhysicsCalculations.getMaxspeeddistanceratio()) {
                    minMaxIndex = 0;//decelerate
                }                 
                if (distance <= PhysicsCalculations.getSlowdowndistance()){
                    //demping ifv afstand
                    double distanceDamping = distance/PhysicsCalculations.getSlowdowndistance();
                    wantedAcceleration = acceleration[minMaxIndex]*distanceDamping;
                }else{
                    wantedAcceleration = acceleration[minMaxIndex];
                }
//                System.out.println(wantedAcceleration);
                result = currentThrust+wantedAcceleration*weight;				
			}else{	
				
				//(T+EF)/2m * (Dt)^2 + v*Dt = Dx = a*Dir met a onbekend, Dir is benaderd
				double[] speed = this.getSpeed();
				double deltaT = this.getDeltaT();
				double weight = this.getDrone().getWeight();
				
				double[] lowerLimit = VectorCalculations.inverse(thrust);//als t = -infinity, gaan we perfect volgens -thrust; genormaliseerd
//				double[] upperlimit = thrust.clone();//als t = infinity; genormaliseerd
				
				//als t = 0
				double[] externalMovement = VectorCalculations.sum(	VectorCalculations.timesScalar(externalForces, deltaT*deltaT/(2*weight)),
																	VectorCalculations.timesScalar(speed, deltaT));						
				
				//nu is het vlak bepaald: lowerlimit/upperlimit en externalMovement
				//benader nu direction
				double[] normalOnMovementPlane = VectorCalculations.normalise(VectorCalculations.crossProduct(lowerLimit, externalMovement));
				double[] approxDir = VectorCalculations.projectOnPlane(dirToPos,normalOnMovementPlane);
				
				//als we niet naar de positie vliegen dan vliegen we er van weg?
				double[] crossProductDirLowLim = VectorCalculations.crossProduct(lowerLimit, approxDir);
				if(VectorCalculations.compareVectors(lowerLimit, crossProductDirLowLim)){
					approxDir = VectorCalculations.inverse(approxDir);//verander appDir van richting
				}
				
				//nu bepalen we hoeveel thrust we nodig hebben om volgens appDir te vliegen
				double thrustNeeded;
				
				//bepaal vectoren in vlak:
				//lower limit = (-infinity,0)
				//upper limit = (infinity,0)
				//alle vectoren hebben positieve y component
				double cosLowThrust = VectorCalculations.cosinusBetweenVectors(lowerLimit, thrust);
				double cosLowExtMov = VectorCalculations.cosinusBetweenVectors(lowerLimit, externalMovement);
				double cosLowDir = VectorCalculations.cosinusBetweenVectors(lowerLimit, approxDir);
				
				double sinLowThrust = Math.sqrt(1-cosLowThrust*cosLowThrust);
				double sinLowExtMov = Math.sqrt(1-cosLowExtMov*cosLowExtMov);
				double sinLowDir = Math.sqrt(1-cosLowDir*cosLowDir);
								
				double sizeExtMov = VectorCalculations.size(externalMovement);
				double sizeThrust = deltaT*deltaT/(2*weight);//component versnelling van thrust
				
				double[] coordThrust = new double[] {cosLowThrust*sizeThrust, sinLowThrust*sizeThrust};
				double[] coordExtMov = new double[] {cosLowExtMov*sizeExtMov,sinLowExtMov*sizeExtMov};
				double[] coordDir = new double[] {cosLowDir,sinLowDir};
				
				thrustNeeded = 	(coordDir[0]*coordExtMov[1] - coordDir[1]*coordExtMov[0])/
								(coordDir[1]*coordThrust[0] - coordDir[0]*coordThrust[1]);
					
				result = thrustNeeded;
			}
			this.setThrust(result);
		}	
	
			
		private void calculateWantedOrientation(double[] position, double[] direction){
				double acceleration = this.determineAcceleration(position);
				double weight = (double) this.getDrone().getWeight();
				double[] externalForces = this.getExternalForces();
				double[] directionToPos = getDirectionDroneToPosition(position);
				double[] directionOfAcceleration = determineDirectionOfAcceleration(acceleration, directionToPos);
				double[] forceToPos = VectorCalculations.timesScalar(VectorCalculations.normalise(directionOfAcceleration), acceleration*weight);
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
                if (speed/distance >= PhysicsCalculations.getMaxspeeddistanceratio()) {
                    minMaxIndex = 0;//decelerate
                } 
//                if(minMaxIndex == 1){
//                    System.out.println("accelerate");
//                }else{
//                    System.out.println("decelerate");
//                }
//                System.out.println("minmaxAcc:= {" + acceleration[0] + ", " + acceleration[1] + "}");
//                
                if (distance <= PhysicsCalculations.getSlowdowndistance()){
                    //demping ifv afstand
                    double distanceDamping = Math.pow(distance/PhysicsCalculations.getSlowdowndistance(),1);
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

					
			private double[] determineDirectionOfAcceleration(double acceleration, double[] directionToPosition){		
				//we moeten bepalen in welke richting we willen versnellen, om de snelheid in laterale richtingen te compenseren
				//a*(Dt)^2/2 + v*Dt = Dx
				//met a = acc * (DirAcc)
				//met Dx = u*Dir met u een cste van onbekende grootte
				//vereenvoudiging: A + V = X
				//AccDirx? AccDiry? AccDirz? u? => 4 onbekenden
				//Ax+Vx = Xx; Ay+Vy = Xy; Az+Vz = Xz; DirAccx^2 + DirAccy^2 + DirAccz^2 = 1; => 4 vgl
				double Dt = this.getDeltaT();
				double[] Dir = VectorCalculations.normalise(directionToPosition);
//				double[] v = VectorCalculations.projectOnPlane(this.getSpeed(), this.getDirectionOfThrust());
				double[] v = VectorCalculations.projectOnPlane(this.getSpeed().clone(), directionToPosition);
//				double[] v = this.getSpeed();
//				double[] v = new double[] {0,0,0};
				double acc = acceleration;
				
				double u;
				double[] DirAcc;
				if(acc+0 == 0){
					DirAcc = new double[] {0,0,0};
				}else{
					//beetje geometrie enzo, teken de drie vectoren dir, v en acc, om de som A + V = X te laten uitkomen
					//X = (1,0)*|X| = (1,0)*u
					//V = (cosXV,sinXV)*|V| = (cosXV,sinXV)*|v|*Dt
					//A = (cosXA,sinXA)*|A| = (cosXA,sinXA)*acc*(Dt)^2/2
					//met sinXA*|A| = -sinXV*|V|
					//=> u = cosXA*|A| + cosXV*|V|
					
					double factor = 0;//niet te veel de snelheid compenseren of de drone gaat schommelen
					
					double sizeV = VectorCalculations.size(v)*Dt*factor;
					double sizeA = Math.abs(acc)*Dt*Dt/2;
					
					double cosXV = VectorCalculations.cosinusBetweenVectors(Dir, v);
					double sinXV = Math.sqrt(1-cosXV*cosXV);
					
					double sinXA = -sinXV*sizeV/sizeA;
					if(Math.abs(sinXA) > 1){
						sinXA = 1*Math.signum(sinXA);
					}
					double cosXA = Math.sqrt(1-sinXA*sinXA);
					
					if(Math.signum(acc) == 1){
						u = cosXA*sizeA + cosXV*sizeV;
					}else{
						u = -cosXA*sizeA + cosXV*sizeV;
					}
					
//					nu we 'u' weten hebben we drie vgl voor DirAcc
					//DirAcc = (u*Dir - v*Dt)*2/(acc*(Dt)^2) = (X - V) * 2/(acc*(Dt)^2)
					double[] X = VectorCalculations.timesScalar(Dir, u);
					double[] negV = VectorCalculations.timesScalar(v, -Dt*factor);
					DirAcc = VectorCalculations.timesScalar(VectorCalculations.sum(X, negV), 2/(acc*Dt*Dt));
				}
				return DirAcc;
			}

			
		private void calculateRemainingAnglesToObject(){
			double[][] currentOrientation = new double[][] {{1,0,0},{0,1,0},{0,0,1}}; //x,y & z van drone
			double[][] wantedOrientation = this.getWantedOrientation().clone();

			//wind in rekening brengen
			wantedOrientation[0] = this.removeRotationWind(wantedOrientation[0]);
			wantedOrientation[1] = this.removeRotationWind(wantedOrientation[1]);
			
			//omzetten naar droneCoordinaten
			wantedOrientation[0] = VectorCalculations.normalise(this.worldVectorToDroneVector(wantedOrientation[0]));
			wantedOrientation[1] = VectorCalculations.normalise(this.worldVectorToDroneVector(wantedOrientation[1]));		

			//Yaw, pitch roll bepalen
			
			//Yaw kan berekend worden adhv de viewvector
			double[] wantedViewOnYawPlane = new double[]{wantedOrientation[1][0],0,wantedOrientation[1][2]};
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
			double pitchWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedOrientation[1], VectorCalculations.inverse(currentOrientation1[2]))));
			//sign pitch:
			if(wantedOrientation[1][1]>0){
				pitchWanted1*=-1;
			}
			
			double pitchWanted2 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedOrientation[1], VectorCalculations.inverse(currentOrientation2[2]))));
			//sign pitch:
			if(wantedOrientation[1][1]>0){
				pitchWanted2*=-1;
			}
			
			//Nu staan de viewvectoren gelijk. Er moet dus enkel nog een rotatie gebeuren rond de rollvector om zo de thrustWanted en thrust gelijk te krijgen.
			// cosinus tss vectoren
			currentOrientation1 = VectorCalculations.pitchAxes(currentOrientation1, pitchWanted1);
			currentOrientation2 = VectorCalculations.pitchAxes(currentOrientation2, pitchWanted2);
			double rollWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedOrientation[0], currentOrientation1[1])));
			//sign roll:
			double[] crossProductThrust1Xaxis = VectorCalculations.crossProduct(currentOrientation1[1], currentOrientation1[0]);//positieve roll van y naar x
			double[] crossProductThrust1WantedThrust= VectorCalculations.normalise(VectorCalculations.crossProduct(currentOrientation1[1], wantedOrientation[0]));
			if(!VectorCalculations.compareVectors(crossProductThrust1WantedThrust, crossProductThrust1Xaxis)){
				rollWanted1 *= -1;
			}

			double rollWanted2 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedOrientation[0], currentOrientation2[1])));
			//sign roll:
			double[] crossProductThrust2Xaxis = VectorCalculations.crossProduct(currentOrientation2[1], currentOrientation2[0]);//positieve roll van y naar x
			double[] crossProductThrust2WantedThrust= VectorCalculations.normalise(VectorCalculations.crossProduct(currentOrientation2[1], wantedOrientation[0]));
			if(!VectorCalculations.compareVectors(crossProductThrust2WantedThrust, crossProductThrust2Xaxis)){
				rollWanted2 *= -1;
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
	
		private void calculateRotationRates(){
			double yawrate = 0;
			double pitchrate = 0;
			double rollrate = 0;
			double yaw = this.getRemainingAngles()[0];
			double maxYawRate = Math.abs(this.getDrone().getMaxYawRate());
			double yawInOneFrameRate = yaw/getDeltaT();
			//yaw
			boolean yawFinished;
			if (Math.abs(yawInOneFrameRate) > maxYawRate) {//kan niet volledig yawen
				yawFinished = false;
				yawrate = maxYawRate*Math.signum(yaw);
			}else {//kan volledig yawen
				yawFinished = true;
				yawrate = yawInOneFrameRate;
			}
			//pitch/roll
			//als yaw niet gedaan is, gaat de thrustvector id foute richting draaien met gegeven pitch/roll
			if(!yawFinished){
				correctRemainingPitchRoll(yawrate*this.getDeltaT());				
			}
			double pitch = this.getRemainingAngles()[1];
			double roll = this.getRemainingAngles()[2];
			double pitchInOneFrameRate = pitch/getDeltaT();
			double rollInOneFrameRate = roll/getDeltaT();
			double maxPitchRate =  Math.abs(this.getDrone().getMaxPitchRate());
			double maxRollRate = Math.abs(this.getDrone().getMaxRollRate());			
			if(roll+0 == 0){//roll = 0
				rollrate = 0;
				if (Math.abs(pitchInOneFrameRate) > maxPitchRate) {//kan niet volledig pitchen
					pitchrate = maxPitchRate*Math.signum(pitch);
				}else {//kan volledig pitchen
					pitchrate = pitchInOneFrameRate;
				}
			}else if(pitch+0==0){//pitch = 0
				pitchrate = 0;
				if (Math.abs(rollInOneFrameRate) > maxRollRate) {//kan niet volledig pitchen
					rollrate = maxRollRate*Math.signum(roll);
				}else {//kan volledig pitchen
					rollrate = rollInOneFrameRate;
				}
			}else{//roll,pitch != 0
				//pitch en roll moeten in gelijke verhoudingen worden doorgevoerd om in de juiste richting de thrust te verplaatsen
				double ratio = Math.abs(pitch/roll);
				double maxRatio = maxPitchRate/maxRollRate;
				boolean pitchGreaterThanMax = (Math.abs(pitchInOneFrameRate) > maxPitchRate);
				boolean rollGreaterThanMax = (Math.abs(rollInOneFrameRate) > maxRollRate);
				
				if(!pitchGreaterThanMax){//pitch kleiner dan max
					if(!rollGreaterThanMax){//roll kleiner dan max
						pitchrate = pitchInOneFrameRate;
						rollrate = rollInOneFrameRate;
					}else{//roll groter dan max
						rollrate = maxRollRate*Math.signum(roll);
						pitchrate = maxRollRate*ratio*Math.signum(pitch);
					}
				}else{//pitch groter dan max
					if(!rollGreaterThanMax){//roll kleiner dan max
						pitchrate = maxPitchRate*Math.signum(pitch);
						rollrate = maxPitchRate/ratio*Math.signum(roll);
					}else{//roll groter dan max
						//beiden zijn groter dan max
						if(maxRatio>=ratio){//maxPitch/maxRoll >= pitch/roll
							rollrate = maxRollRate*Math.signum(roll);
							pitchrate = maxRollRate*ratio*Math.signum(pitch);
						}else{
							pitchrate = maxPitchRate*Math.signum(pitch);
							rollrate = maxPitchRate/ratio*Math.signum(roll);
						}
					}
				}

				//nu is pitchrate/rollrate = ratio -> thrust verandert gelijkmatig
				
			}
			setYawRate(yawrate);
			setPitchRate(pitchrate);
			setRollRate(rollrate);
		}
		
			public void correctRemainingPitchRoll(double expectedYaw){
				double[] wantedThrust = this.getWantedOrientation()[0];
				wantedThrust = this.removeRotationWind(wantedThrust);//windloos
				wantedThrust = VectorCalculations.normalise(this.worldVectorToDroneVector(wantedThrust));//in droneCoordinaten
				double[][] currentOrientation = new double[][] {{1,0,0},{0,1,0},{0,0,1}};//currentThrust = currentOrientation[1]
				//yaw = yawExpected
				//yaw axes
				currentOrientation = VectorCalculations.yawAxes(currentOrientation, expectedYaw);		
				
				//pitch
				//projecteer wantedThrust op pitchvlak
				double[] wantedThrustOnPitchPlane = VectorCalculations.projectOnPlane(wantedThrust, currentOrientation[0]);
				double pitchWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedThrustOnPitchPlane, currentOrientation[1])));
				double[] crossProductThrustNegZaxis = VectorCalculations.crossProduct(currentOrientation[1], VectorCalculations.inverse(currentOrientation[2]));//positieve pitch van thrust (=y) naar neg z
				double[] crossProductThrustWantedThrust= VectorCalculations.normalise(VectorCalculations.crossProduct(currentOrientation[1], wantedThrustOnPitchPlane));
				if(!VectorCalculations.compareVectors(crossProductThrustWantedThrust, crossProductThrustNegZaxis)){
					pitchWanted1 *= -1;
				}
				
				double pitchWanted2 = pitchWanted1+180;
				if(pitchWanted2 > 180){
					pitchWanted2 -= 360;
				}
				//pitch axes
				double[][] currentOrientation1 = VectorCalculations.pitchAxes(currentOrientation, pitchWanted1);
				double[][] currentOrientation2 = VectorCalculations.pitchAxes(currentOrientation, pitchWanted2);
				
				//roll
				double rollWanted1 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedThrust, currentOrientation1[1])));
				double[] crossProductThrust1Xaxis = VectorCalculations.crossProduct(currentOrientation1[1], currentOrientation1[0]);//positieve roll van y naar x
				double[] crossProductThrust1WantedThrust= VectorCalculations.normalise(VectorCalculations.crossProduct(currentOrientation1[1], wantedThrust));
				if(!VectorCalculations.compareVectors(crossProductThrust1WantedThrust, crossProductThrust1Xaxis)){
					rollWanted1 *= -1;
				}
				double rollWanted2 = Math.toDegrees(Math.acos(VectorCalculations.cosinusBetweenVectors(wantedThrust, currentOrientation2[1])));
				double[] crossProductThrust2Xaxis = VectorCalculations.crossProduct(currentOrientation2[1], currentOrientation2[0]);//positieve roll van y naar x
				double[] crossProductThrust2WantedThrust= VectorCalculations.normalise(VectorCalculations.crossProduct(currentOrientation2[1], wantedThrust));
				if(!VectorCalculations.compareVectors(crossProductThrust2WantedThrust, crossProductThrust2Xaxis)){
					rollWanted2 *= -1;
				}
				
				//1 of 2?
				double pitch;
				double roll;
				if(Math.abs(pitchWanted1)+Math.abs(rollWanted1) <= 180){
					pitch = pitchWanted1;
					roll = rollWanted1;
				}else{
					pitch = pitchWanted2;
					roll = rollWanted2;
				}
				setRemainingAngles(new double[] {expectedYaw,pitch,roll});
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
	
	public static double getMaxspeeddistanceratio() {
		return maxSpeedDistanceRatio;
		
	}

	public static double getSlowdowndistance() {
		return slowDownDistance;
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


	public DroneAutopilot getDroneAutopilot() {
		return droneAutopilot;
	}
	

	public void setDroneAutopilot(DroneAutopilot droneAutopilot) {
		this.droneAutopilot = droneAutopilot;
	}

	public AvoidObstacles getAvoidObstacle() {
		return avoidObstacle;
	}

	public void setAvoidObstacle(AvoidObstacles avoidObstacle) {
		this.avoidObstacle = avoidObstacle;
	}
	
}


