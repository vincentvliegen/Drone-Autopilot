package simulator.parser;

import java.io.IOException;

import simulator.world.World;

public abstract class Parser {
	
	
	private World world;
	public Parser(World world) {
		this.world = world;
	}
	
	protected World getWorld() {
		return this.world;
	}

	public abstract void parse() throws IOException;
	
	
	
	//TODO verander naar float?
	double horizontalAngleOfView;
	double verticalAngleOfView;
	int imageWidth;
	int imageHeight;
	double cameraSeparation; 
	double weight ;
	double gravity; 
	double drag ;
	double maxThrust ;
	double maxPitchRate; 
	double maxRollRate; 
	double maxYawRate; 
	float[] arrayXValues;
	float[] arrayXTimes;
	float[] arrayYValues;
	float[] arrayYTimes;
	float[] arrayZValues;
	float[] arrayZTimes;

	float[] windRotationXValues;
	float[] windRotationXTimes;
	float[] windRotationYValues;
	float[] windRotationYTimes;
	float[] windRotationZValues;
	float[] windRotationZTimes;

	
	public double getHorizontalAngleOfView() {
		return horizontalAngleOfView;
	}

	public double getVerticalAngleOfView() {
		return verticalAngleOfView;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public double getCameraSeparation() {
		return cameraSeparation;
	}

	public double getWeight() {
		return weight;
	}

	public double getGravity() {
		return gravity;
	}

	public double getDrag() {
		return drag;
	}

	public double getMaxThrust() {
		return maxThrust;
	}

	public double getMaxPitchRate() {
		return maxPitchRate;
	}

	public double getMaxRollRate() {
		return maxRollRate;
	}

	public double getMaxYawRate() {
		return maxYawRate;
	}

	public float[] getArrayXValues() {
		return arrayXValues;
	}

	public float[] getArrayXTimes() {
		return arrayXTimes;
	}

	public float[] getArrayYValues() {
		return arrayYValues;
	}

	public float[] getArrayYTimes() {
		return arrayYTimes;
	}

	public float[] getArrayZValues() {
		return arrayZValues;
	}

	public float[] getArrayZTimes() {
		return arrayZTimes;
	}

	public float[] getWindRotationXValues() {
		return windRotationXValues;
	}

	public float[] getWindRotationXTimes() {
		return windRotationXTimes;
	}

	public float[] getWindRotationYValues() {
		return windRotationYValues;
	}

	public float[] getWindRotationYTimes() {
		return windRotationYTimes;
	}

	public float[] getWindRotationZValues() {
		return windRotationZValues;
	}

	public float[] getWindRotationZTimes() {
		return windRotationZTimes;
	}
	
}
