package DroneAutopilot.calculations;

public class VectorCalculations {

	public float size(float x, float y, float z){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}

	public float size(float[] vector){
		return size(vector[0], vector[1], vector[2]);
	}

	public float[] normalise(float x, float y, float z){	
		float size = size(x,y,z);
		return new float[] {x/size, y/size, z/size};
	}

	public float[] normalise(float[] vector){
		return normalise(vector[0], vector[1], vector[2]);
	}

	public float[] inverse(float[] vector){
		return new float[] {-vector[0], -vector[1], -vector[2]};
	}

	public float[] sum(float[] vector1, float[] vector2){
		return new float[] {vector1[0]+vector2[0], vector1[1]+vector2[1], vector1[2]+vector2[2],};
	}

	public float[] timesScalar(float[] vector, float scalar){
		return new float[] {vector[0]*scalar, vector[1]*scalar, vector[2]*scalar};
	}

	public float dotProduct(float[] vector1, float[] vector2){
		return vector1[0]*vector2[0] + vector1[1]*vector2[1] + vector1[2]*vector2[2];
	}

	public float[] crossProduct(float[] vector1, float[] vector2){
		float x = vector1[1]*vector2[2]-vector1[2]*vector2[1];
		float y = vector1[2]*vector2[0]-vector1[0]*vector2[2];
		float z = vector1[0]*vector2[1]-vector1[1]*vector2[0];
		return new float[] {x, y, z};
	}	

	public float cosinusBetweenVectors(float[] vector1, float[] vector2){
		return dotProduct(vector1,vector2)/(size(vector1)*size(vector2));
	}
	
	public float calculateDistanceBetweenCoords(float[] coord1, float[] coord2) {
		return (float) Math.sqrt(Math.pow(coord1[0]-coord2[0], 2) + Math.pow(coord1[1]-coord2[1], 2) + Math.pow(coord1[2]-coord2[2], 2));
	}
}
