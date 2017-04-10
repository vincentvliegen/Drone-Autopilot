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
			return 0;
		}else{
			return dotProduct(vector1,vector2)/(size(vector1)*size(vector2));
		}
	}
	
	public static double sinusBetweenVectors(double[] vector1, double[] vector2){
		if(size(vector1)==0||size(vector2)==0){
			return 0;
		}else{
			return size(crossProduct(vector1,vector2))/(size(vector1)*size(vector2));
		}
 	}
	
	public static double calculateDistanceBetweenCoords(double[] coord1, double[] coord2) {
		return Math.sqrt(Math.pow(coord1[0]-coord2[0], 2) + Math.pow(coord1[1]-coord2[1], 2) + Math.pow(coord1[2]-coord2[2], 2));
	}
	
 	public static double[][] createRotationMatrix(double yaw, double pitch, double roll) {		
		double r11 = (Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))-Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw)));
		double r12 = (Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(roll))+Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)));
		double r13 = (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)));

		double r21 = (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll)));
		double r22 = (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(roll)));
		double r23 = (Math.sin(Math.toRadians(pitch)));

		double r31 = (Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))+Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll)));
		double r32 = (Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))-Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch)));
		double r33 = (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw)));
		
		return new double[][] {{r11,r12,r13},{r21,r22,r23},{r31,r32,r33}};
	}

	public static double[][] createInverseRotationMatrix(double yaw, double pitch, double roll){	
		double[][] rotationMatrix = createRotationMatrix(yaw, pitch, roll);
		
		double r11 = rotationMatrix[0][0];
		double r12 = rotationMatrix[1][0];
		double r13 = rotationMatrix[2][0];
		
		double r21 = rotationMatrix[0][1];
		double r22 = rotationMatrix[1][1];
		double r23 = rotationMatrix[2][1];

		double r31 = rotationMatrix[0][2];
		double r32 = rotationMatrix[1][2];
		double r33 = rotationMatrix[2][2];
		
		return new double[][] {{r11,r12,r13},{r21,r22,r23},{r31,r32,r33}};
	}
	
	public static double[] rotate(double[][] rotationMatrix, double[] vector){
		double r11 = rotationMatrix[0][0];
		double r12 = rotationMatrix[0][1];
		double r13 = rotationMatrix[0][2];
		double r21 = rotationMatrix[1][0];
		double r22 = rotationMatrix[1][1];
		double r23 = rotationMatrix[1][2];
		double r31 = rotationMatrix[2][0];
		double r32 = rotationMatrix[2][1];
		double r33 = rotationMatrix[2][2];
		
		double x = vector[0];
		double y = vector[1];
		double z = vector[2];
		
		double xRotated = r11*x + r12*y + r13*z;
		double yRotated = r21*x + r22*y + r23*z;
		double zRotated = r31*x + r32*y + r33*z;
		return new double[] {xRotated, yRotated, zRotated};
	}
	
	public static boolean compareVectors(double[] vector1, double[] vector2){
		for( int i = 0; i<3; i++){
			if(Math.abs(vector1[i]-vector2[i]) > 0.00001)
				return false;
		}
		return true;
	}
	
}
