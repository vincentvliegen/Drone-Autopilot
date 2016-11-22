package simulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import simulator.objects.Sphere;
import simulator.world.World;

/**
 * 
 * source: http://stackoverflow.com/questions/5819772/java-parsing-text-file
 * 
 */

public class Parser {

	public Parser(World world) {
		this.world = world;
	}

	private World world;
	private World getWorld() {
		return this.world;
	}

	//TODO verander naar float?
	double horizontalAngleOfView;
	double verticalAngleOfView;
	double imageWidth;
	double imageHeight;
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

	ArrayList<Sphere> obstacleBalls = new ArrayList<Sphere>();
	ArrayList<Sphere> targetBalls = new ArrayList<Sphere>();




	//TODO aanpassen

	float vasteRadius = 0.5f;
	int vasteSlices = 64;
	int vasteStacks = 64;


	/*
	 * 1 	horizontalAngleOfView verticalAngleOfView imageWidth imageHeight cameraSeparation weight gravity drag maxThrust maxPitchRate maxRollRate maxYawRate 
	 * 2 	windSpeedXPoints 
	 * 3	windSpeedYPoints 
	 * 4	windSpeedZPoints 
	 * 5	windRotationRateAroundXPoints 
	  				the wind causes the drone to rotate around an axis through its center parallel 
	  				to the world X axis at this rate. Positive means clockwise when looking at 1 0 0 from 0 0 0. In degrees per second. 
	 * 6	windRotationRateAroundYPoints 
	 * 7	windRotationRateAroundZPoints 
	 * 8	"target_ball" x y z // one or more lines 
	 * 9	"obstacle_ball" x y z // zero or more lines; not applicable to Milestones 1.3 and 1.4
	 */

	public void parse() throws IOException{


		
//		print working dir
//		System.out.println(System.getProperty("user.dir"));
	
		
		
		FileReader input = new FileReader("inputFile.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		// line 1
		myLine = bufRead.readLine();
		String[] splitArray = myLine.split(" ");
		horizontalAngleOfView = Double.parseDouble(splitArray[0]);
		verticalAngleOfView = Double.parseDouble(splitArray[1]);
		imageWidth = Double.parseDouble(splitArray[2]);
		imageHeight = Double.parseDouble(splitArray[3]);
		cameraSeparation = Double.parseDouble(splitArray[4]);
		weight = Double.parseDouble(splitArray[5]);
		gravity = Double.parseDouble(splitArray[6]);
		drag = Double.parseDouble(splitArray[7]);
		maxThrust = Double.parseDouble(splitArray[8]);
		maxPitchRate = Double.parseDouble(splitArray[9]);
		maxRollRate = Double.parseDouble(splitArray[10]);
		maxYawRate = Double.parseDouble(splitArray[11]);


		// line 2
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		int splitArrayLength = splitArray.length;
		System.out.println(Arrays.toString(splitArray));
		arrayXValues = new float[splitArrayLength];
		arrayXTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayXTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayXValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}

		// line 3
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		arrayYValues = new float[splitArrayLength];
		arrayYTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayYTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayYValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}

		// line 4
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		arrayZValues = new float[splitArrayLength];
		arrayZTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayZTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayZValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}

		// line 5
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationXTimes= new float[splitArrayLength];
		windRotationXValues = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationXTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				windRotationXValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}

		// line 6
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationYValues = new float[splitArrayLength];
		windRotationYTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationYTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				windRotationYValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}

		// line 7
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationZValues = new float[splitArrayLength];
		windRotationZTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationZTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				System.out.println(i);

				windRotationZValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}


		Random rand = new Random();
		float r = 0, g = 0, b = 0;

		while ( (myLine = bufRead.readLine()) != null)
		{    
			splitArray = myLine.split(" ");
			double[] position = {Double.parseDouble(splitArray[1]), Double.parseDouble(splitArray[1]), Double.parseDouble(splitArray[3])};

			//target_balls

			if(splitArray[0] == "target_ball") {
				//TODO argumenten aanpassen!!
				while (r == g && r == b && g == b){
					r = rand.nextFloat();
					g = rand.nextFloat();
					b = rand.nextFloat();
				}
				float[] color = {r, g, b};
				targetBalls.add(new Sphere(getWorld().getGL().getGL2(), vasteRadius, vasteSlices, vasteStacks, color , position));

			}

			//obstacle_balls

			else if(splitArray[0] == "obstacle_ball") {
				b = rand.nextFloat();
				float[] color = {b, b, b};
				obstacleBalls.add(new Sphere(getWorld().getGL().getGL2(), vasteRadius, vasteSlices, vasteStacks, color , position));

			}

		}
		
		bufRead.close();

	}


	public double getHorizontalAngleOfView() {
		return horizontalAngleOfView;
	}


	public double getVerticalAngleOfView() {
		return verticalAngleOfView;
	}


	public double getImageWidth() {
		return imageWidth;
	}


	public double getImageHeight() {
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


	public ArrayList<Sphere> getObstacleBalls() {
		return obstacleBalls;
	}


	public ArrayList<Sphere> getTargetBalls() {
		return targetBalls;
	}


	public float getVasteRadius() {
		return vasteRadius;
	}


	public int getVasteSlices() {
		return vasteSlices;
	}


	public int getVasteStacks() {
		return vasteStacks;
	}

}
