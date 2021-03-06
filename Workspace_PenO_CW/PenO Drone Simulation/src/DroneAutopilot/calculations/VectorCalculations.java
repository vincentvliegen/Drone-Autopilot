package DroneAutopilot.calculations;

public class VectorCalculations {

	public static double size(double x, double y, double z){
		double xNew = x;
		double yNew = y;
		double zNew = z;
		return Math.sqrt(xNew*xNew+yNew*yNew+zNew*zNew);
	}

	public static double size(double[] vector){
		return size(vector[0], vector[1], vector[2]);
	}

	public static double[] normalise(double x, double y, double z){	
		double size = size(x,y,z);
		if (size == 0){
			return new double[] {0,0,0};
		}else{
			double xNorm = x/size;
			double yNorm = y/size;
			double zNorm = z/size;
			if(Math.abs(xNorm) == 1.0f)
				return new double[] {xNorm,0,0};
			if(Math.abs(yNorm) == 1.0f)
				return new double[] {0,yNorm,0};
			if(Math.abs(zNorm) == 1.0f)
				return new double[] {0,0,zNorm};
			return new double[] {xNorm, yNorm, zNorm};
		}
	}

	public static double[] normalise(double[] vector){
		return normalise(vector[0], vector[1], vector[2]);
	}

	public static double[] inverse(double[] vector){
		return new double[] {-vector[0], -vector[1], -vector[2]};
	}

	public static double[] sum(double[] vector1, double[] vector2){
		return new double[] {vector1[0]+vector2[0], vector1[1]+vector2[1], vector1[2]+vector2[2],};
	}

	public static double[] timesScalar(double[] vector, double scalar){
		return new double[] {vector[0]*scalar, vector[1]*scalar, vector[2]*scalar};
	}

	public static double dotProduct(double[] vector1, double[] vector2){
		return vector1[0]*vector2[0] + vector1[1]*vector2[1] + vector1[2]*vector2[2];
	}

	public static double[] crossProduct(double[] vector1, double[] vector2){
		double x = vector1[1]*vector2[2]-vector1[2]*vector2[1];
		double y = vector1[2]*vector2[0]-vector1[0]*vector2[2];
		double z = vector1[0]*vector2[1]-vector1[1]*vector2[0];
		return new double[] {x, y, z};
	}	

	public static double cosinusBetweenVectors(double[] vector1, double[] vector2){
		if(size(vector1)==0||size(vector2)==0){
			return 1;
		}else{
			double cosinus = dotProduct(vector1,vector2)/(size(vector1)*size(vector2));
			if (Math.abs(cosinus) > 1){
				cosinus = 1*Math.signum(cosinus);
			}
			return cosinus;
		}
	}
	
	public static double sinusBetweenVectors(double[] vector1, double[] vector2){
		if(size(vector1)==0||size(vector2)==0){
			return 0;
		}else{
			double sinus = size(crossProduct(vector1,vector2))/(size(vector1)*size(vector2));
			if (Math.abs(sinus) > 1){
				sinus = 1*Math.signum(sinus);
			}
			return sinus;
		}
 	}
	
	public static double distance(double[] vector1, double[] vector2) {
		return Math.sqrt(Math.pow(vector1[0]-vector2[0], 2) + Math.pow(vector1[1]-vector2[1], 2) + Math.pow(vector1[2]-vector2[2], 2));
	}
	
	public static boolean compareVectors(double[] vector1, double[] vector2){
		for( int i = 0; i<3; i++){
			if(Math.abs(vector1[i]-vector2[i]) > 0.00001)
				return false;
		}
		return true;
	}
	
	public static double[] projectOnAxis(double[] vector, double[] axis){
		double sizeAxis = size(axis);
		double[] result = timesScalar(axis, VectorCalculations.dotProduct(vector, axis)/(sizeAxis*sizeAxis));
		return result;
	}
	
	public static double[] projectOnPlane(double[] vector, double[] normal){
		double[] projection = projectOnAxis(vector, normal);
		double[] result = sum(vector, inverse(projection));
		return result;
	}

	/**
	 * Geef een vector, gerepresenteerd in een assenstelsel, een representatie in een ander assenstelsel
	 * @param vector in currentAxescoordinaten
	 * @param currentAxes in worldcoordinaten
	 * @param targetAxes in worldcoordinaten
	 * @return
	 */
	public static double[] changeCoordinateSystem(double[] vector, double[][] currentAxes, double[][] targetAxes){
		
		//beschrijf de vector (in currentAxescoordinaten) in world
		//positie = (x,y,z) = x*(1,0,0) + y(0,1,0) + z(0,0,1) [in currentAxes] = x*x-world + y*y-world + z*z-world = (a,b,c) [in world]
		double[] xcomponent = VectorCalculations.timesScalar(currentAxes[0],vector[0]);
		double[] ycomponent = VectorCalculations.timesScalar(currentAxes[1],vector[1]);
		double[] zcomponent = VectorCalculations.timesScalar(currentAxes[2],vector[2]);

		double[] vectorInWorld = VectorCalculations.sum(xcomponent, VectorCalculations.sum(ycomponent, zcomponent));
		
		//beschrijf de vector (in worldcoordinaten) in targetAxes
		//positie = (a,b,c) = a*(1,0,0) + b(0,1,0) + c(0,0,1) = a*x-target + b*y-target + c*z-target
		double[] orientationWorldx = new double[] {targetAxes[0][0],targetAxes[1][0],targetAxes[2][0]};
		double[] orientationWorldy = new double[] {targetAxes[0][1],targetAxes[1][1],targetAxes[2][1]};
		double[] orientationWorldz = new double[] {targetAxes[0][2],targetAxes[1][2],targetAxes[2][2]};
		
		double[] xcomponentTarget = VectorCalculations.timesScalar(orientationWorldx,vectorInWorld[0]);
		double[] ycomponentTarget = VectorCalculations.timesScalar(orientationWorldy,vectorInWorld[1]);
		double[] zcomponentTarget = VectorCalculations.timesScalar(orientationWorldz,vectorInWorld[2]);
			
		double[] targetVector= VectorCalculations.sum(xcomponentTarget, VectorCalculations.sum(ycomponentTarget, zcomponentTarget));
		return targetVector;
		}
	
	
	//////////ROTATIONS//////////
	
	//https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
	public static double[] rotateVectorAroundAxis(double[] vectorToRotate, double[] rotationAxis, double angle){
		double cosAngle = Math.cos(Math.toRadians(angle));
		double sinAngle = Math.sin(Math.toRadians(-angle));//hoek is negatief volgens de rechterhandregel (yaw/pitch/roll)
		double[] k = normalise(rotationAxis);
		double[] v = vectorToRotate;
		
		//vrot = v*cosAngle + (k x v)sinAngle + k(k*v)(1-cosAngle)
		double[] rotatedVector = 	sum(timesScalar(v,cosAngle),
									sum(timesScalar(crossProduct(k, v), sinAngle),
									timesScalar(k, dotProduct(k, v)* (1-cosAngle)) ));
		//randgeval, zou kleine afwijking kunnen geven
		if	(	compareVectors(normalise(vectorToRotate),normalise(rotationAxis))||
				compareVectors(normalise(vectorToRotate),inverse(normalise(rotationAxis)))){
			rotatedVector = vectorToRotate;
		}
		return rotatedVector;
	}

	public static double[][] rotateAxesAroundAxis(double[][] axes, double[] rotationAxis, double angle){
		double[] newX = rotateVectorAroundAxis(axes[0], rotationAxis, angle);
		double[] newY = rotateVectorAroundAxis(axes[1], rotationAxis, angle);
		double[] newZ = rotateVectorAroundAxis(axes[2], rotationAxis, angle);
		return new double[][] {newX, newY, newZ};
	}

	
	public static double[] pitch(double[] vectorToRotate, double[][] currentOrientation, double pitch){
		return rotateVectorAroundAxis(vectorToRotate, currentOrientation[0], pitch);
	}
	
	public static double[] roll(double[] vectorToRotate, double[][] currentOrientation, double roll){
		return rotateVectorAroundAxis(vectorToRotate, currentOrientation[2], roll);
	}
	
	public static double[] yaw(double[] vectorToRotate, double[][] currentOrientation, double yaw){
		return rotateVectorAroundAxis(vectorToRotate, currentOrientation[1], yaw);
	}

	
	public static double[][] pitchAxes(double[][] currentOrientation, double pitch){
		double[] rotationAxis = currentOrientation[0];
		return rotateAxesAroundAxis(currentOrientation, rotationAxis, pitch);
	}

	public static double[][] yawAxes(double[][] currentOrientation, double yaw){
		double[] rotationAxis = currentOrientation[1];
		return rotateAxesAroundAxis(currentOrientation, rotationAxis, yaw);
	}

	public static double[][] rollAxes(double[][] currentOrientation, double roll){
		double[] rotationAxis = currentOrientation[2];
		return rotateAxesAroundAxis(currentOrientation, rotationAxis, roll);
	}

	
	public static double[] rotate(double[] vectorToRotate, double[][] currentOrientation, double yaw, double pitch, double roll){
		
		double[] vector = vectorToRotate;
		double[][] axes = currentOrientation;
		
		//yaw
		vector = yaw(vector, axes, yaw);
		axes = yawAxes(axes, yaw);
		
		//pitch
		vector = pitch(vector, axes, pitch);
		axes = pitchAxes(axes, pitch);		
		
		//roll
		vector = roll(vector, axes, roll);
		
		return vector;
	}
	
	public static double[][] rotateAxes(double[][] currentOrientation, double yaw, double pitch, double roll){
		double[][] axes = currentOrientation;
		axes = yawAxes(axes, yaw);
		axes = pitchAxes(axes, pitch);
		axes = rollAxes(axes, roll);
		return axes;
	}
	
}
