package DroneAutopilot.calculations;

public class VectorCalculations {

	public static float size(float x, float y, float z){
		double xNew = (double) x;
		double yNew = (double) y;
		double zNew = (double) z;
		return (float) Math.sqrt(xNew*xNew+yNew*yNew+zNew*zNew);
	}

	public static float size(float[] vector){
		return size(vector[0], vector[1], vector[2]);
	}

	public static float[] normalise(float x, float y, float z){	
		float size = size(x,y,z);
		if (size == 0){
			return new float[] {0,0,0};
		}else{
			float xNorm = x/size;
			float yNorm = y/size;
			float zNorm = z/size;
			if(Math.abs(xNorm) == 1.0f)
				return new float[] {xNorm,0,0};
			if(Math.abs(yNorm) == 1.0f)
				return new float[] {0,yNorm,0};
			if(Math.abs(zNorm) == 1.0f)
				return new float[] {0,0,zNorm};
			return new float[] {xNorm, yNorm, zNorm};
		}
	}

	public static float[] normalise(float[] vector){
		return normalise(vector[0], vector[1], vector[2]);
	}

	public static float[] inverse(float[] vector){
		return new float[] {-vector[0], -vector[1], -vector[2]};
	}

	public static float[] sum(float[] vector1, float[] vector2){
		return new float[] {vector1[0]+vector2[0], vector1[1]+vector2[1], vector1[2]+vector2[2],};
	}

	public static float[] timesScalar(float[] vector, float scalar){
		return new float[] {vector[0]*scalar, vector[1]*scalar, vector[2]*scalar};
	}

	public static float dotProduct(float[] vector1, float[] vector2){
		return vector1[0]*vector2[0] + vector1[1]*vector2[1] + vector1[2]*vector2[2];
	}

	public static float[] crossProduct(float[] vector1, float[] vector2){
		float x = vector1[1]*vector2[2]-vector1[2]*vector2[1];
		float y = vector1[2]*vector2[0]-vector1[0]*vector2[2];
		float z = vector1[0]*vector2[1]-vector1[1]*vector2[0];
		return new float[] {x, y, z};
	}	

	public static float cosinusBetweenVectors(float[] vector1, float[] vector2){
		if(size(vector1)==0||size(vector2)==0){
			return 0;
		}else{
			return dotProduct(vector1,vector2)/(size(vector1)*size(vector2));
		}
	}
	
	public static float sinusBetweenVectors(float[] vector1, float[] vector2){
		if(size(vector1)==0||size(vector2)==0){
			return 0;
		}else{
			return size(crossProduct(vector1,vector2))/(size(vector1)*size(vector2));
		}
 	}
	
	public static float calculateDistanceBetweenCoords(float[] coord1, float[] coord2) {
		return (float) Math.sqrt(Math.pow(coord1[0]-coord2[0], 2) + Math.pow(coord1[1]-coord2[1], 2) + Math.pow(coord1[2]-coord2[2], 2));
	}
	
 	public static float[][] createRotationMatrix(float yaw, float pitch, float roll) {		
		float r11 = (float) (Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))-Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw)));
		float r12 = (float) (Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(roll))+Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)));
		float r13 = (float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw)));

		float r21 = (float) (-Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll)));
		float r22 = (float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(roll)));
		float r23 = (float) (Math.sin(Math.toRadians(pitch)));

		float r31 = (float) (Math.cos(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))+Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch))*Math.sin(Math.toRadians(roll)));
		float r32 = (float) (Math.sin(Math.toRadians(roll))*Math.sin(Math.toRadians(yaw))-Math.cos(Math.toRadians(roll))*Math.cos(Math.toRadians(yaw))*Math.sin(Math.toRadians(pitch)));
		float r33 = (float) (Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw)));
		
		return new float[][] {{r11,r12,r13},{r21,r22,r23},{r31,r32,r33}};
	}

	public static float[][] createInverseRotationMatrix(float yaw, float pitch, float roll){	
		float[][] rotationMatrix = createRotationMatrix(yaw, pitch, roll);
		
		float r11 = rotationMatrix[0][0];
		float r12 = rotationMatrix[1][0];
		float r13 = rotationMatrix[2][0];
		
		float r21 = rotationMatrix[0][1];
		float r22 = rotationMatrix[1][1];
		float r23 = rotationMatrix[2][1];

		float r31 = rotationMatrix[0][2];
		float r32 = rotationMatrix[1][2];
		float r33 = rotationMatrix[2][2];
		
		return new float[][] {{r11,r12,r13},{r21,r22,r23},{r31,r32,r33}};
	}
	
	public static float[] rotate(float[][] rotationMatrix, float[] vector){
		float r11 = rotationMatrix[0][0];
		float r12 = rotationMatrix[0][1];
		float r13 = rotationMatrix[0][2];
		float r21 = rotationMatrix[1][0];
		float r22 = rotationMatrix[1][1];
		float r23 = rotationMatrix[1][2];
		float r31 = rotationMatrix[2][0];
		float r32 = rotationMatrix[2][1];
		float r33 = rotationMatrix[2][2];
		
		float x = vector[0];
		float y = vector[1];
		float z = vector[2];
		
		float xRotated = r11*x + r12*y + r13*z;
		float yRotated = r21*x + r22*y + r23*z;
		float zRotated = r31*x + r32*y + r33*z;
		return new float[] {xRotated, yRotated, zRotated};
	}
	
	public static boolean compareVectors(float[] vector1, float[] vector2){
		for( int i = 0; i<3; i++){
			if(Math.abs(vector1[i]-vector2[i]) > 0.00001)
				return false;
		}
		return true;
	}
	
}
