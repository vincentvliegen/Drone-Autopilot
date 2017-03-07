package simulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import simulator.world.World;

/**
 * 
 * source: http://stackoverflow.com/questions/5819772/java-parsing-text-file
 * 
 */

public class Parser_v1 extends Parser {

	public Parser_v1(World world) {
		super(world);
	}




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

	public void parse() throws IOException {

		// print working dir
		// System.out.println(System.getProperty("user.dir"));

		FileReader input = new FileReader("inputFiles/inputFileGenerator.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		// line 1
		myLine = bufRead.readLine();
		String[] splitArray = myLine.split(" ");
		horizontalAngleOfView = Double.parseDouble(splitArray[0]);
		verticalAngleOfView = Double.parseDouble(splitArray[1]);
		imageWidth = Integer.parseInt(splitArray[2]);
		imageHeight = Integer.parseInt(splitArray[3]);
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
		arrayXValues = readInTimesAndValues(myLine, false);
		arrayXTimes = readInTimesAndValues(myLine, true);

		// line 3
		myLine = bufRead.readLine();
		arrayYValues = readInTimesAndValues(myLine, false);
		arrayYTimes = readInTimesAndValues(myLine, true);

		// line 4
		myLine = bufRead.readLine();
		arrayZValues = readInTimesAndValues(myLine, false);
		arrayZTimes = readInTimesAndValues(myLine, true);

		// line 5
		myLine = bufRead.readLine();
		windRotationXTimes = readInTimesAndValues(myLine, true);
		windRotationXValues = readInTimesAndValues(myLine, false);

		// line 6
		myLine = bufRead.readLine();
		windRotationYValues = readInTimesAndValues(myLine, false);
		windRotationYTimes = readInTimesAndValues(myLine, true);

		// line 7
		myLine = bufRead.readLine();
		windRotationZValues = readInTimesAndValues(myLine, false);
		windRotationZTimes = readInTimesAndValues(myLine, true);

		while ((myLine = bufRead.readLine()) != null) {

			splitArray = myLine.split(" ");
			double[] position = { Double.parseDouble(splitArray[1]), Double.parseDouble(splitArray[2]),
					Double.parseDouble(splitArray[3]) };

			// target_balls

			if (splitArray[0].equals("target_ball")) {

				getWorld().addSphereWithRandomColor(position);

			}

			// obstacle_balls

			else if (splitArray[0].equals("obstacle_ball")) {
				getWorld().addObstacleSphereWithRandomColor(position);
			}

		}

		bufRead.close();

	}

	private float[] readInTimesAndValues(String myLine, boolean time) {
		String[] splitArray = myLine.split(" ");
		int splitArrayLength = splitArray.length;
		float[] returnArray = new float[splitArrayLength / 2];
		for (int i = 0; i < splitArrayLength; i++) {
			if (time) {
				if (i % 2 == 0) {
					returnArray[i / 2] = Float.parseFloat(splitArray[i]);
				}
			} else {
				if (i % 2 != 0)
					returnArray[(i - 1) / 2] = Float.parseFloat(splitArray[i]);
			}
		}
		return returnArray;
	}



}
