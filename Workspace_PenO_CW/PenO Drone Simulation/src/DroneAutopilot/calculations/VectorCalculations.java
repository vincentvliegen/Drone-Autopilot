package DroneAutopilot.calculations;

public class VectorCalculations {

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
}
