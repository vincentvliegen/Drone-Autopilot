package simulator.generator;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Generator {

	public static void main(String[] args) {
		// Change path so that it loads into your inputFiles/GeneratorTest.txt
		String path = "inputFiles/GeneratorTest.txt";
		String path2 = "inputFiles/GeneratorTest.txt";
		File file = new File(path);
		FileWriter f;

		Random r = new Random();

		double horAngle = 120;
		double verAngle = 120;
		int imgWidth = 200;
		int imgHeigth = 200;
		double cameraSep = 0.25f;
		double weight = 1;
		double gravity = 9.81f;
		double drag = 0.2f;
		double maxThrust = 25;
		double maxPitch = 720;
		double maxRoll = 720;
		double maxYaw = 720;

		String entireString = horAngle + " " + verAngle + " " + imgWidth + " " + imgHeigth + " " + cameraSep + " "
				+ weight + " " + gravity + " " + drag + " " + maxThrust + " " + maxPitch + " " + maxRoll + " " + maxYaw;

		PriorityQueue<Double> newQueue = new PriorityQueue<>();

		for (int i = 0; i < 6; i++) {
			entireString += "\n";
			for (int j = 0; j < 6; j++) {
				newQueue.add(r.nextDouble() * 15);
			}
			if (i < 3)
				for (int j = 0; j < 6; j++) {
					entireString += newQueue.poll() + " " + r.nextDouble() * 0.05 + " ";
				}
			else {
				for (int j = 0; j < 6; j++) {
					entireString += newQueue.poll() + " " + r.nextDouble() * 0.5 + " ";
				}
			}
		}

		List<double[]> positionList = new ArrayList<>();
		positionList.add(new double[] { 0, 0, 0 });

		for (int i = 0; i < 20; i++) {
			entireString += "\n";
			if (i <= 9)
				entireString += "target_ball";
			else {
				entireString += "obstacle_ball";
			}
			boolean noValidNewPosition = true;
			while (noValidNewPosition) {
				double x = (r.nextDouble() - .5) * 30;
				double y = (r.nextDouble() - .5) * 30;
				double z = (r.nextDouble() - .5) * 30;
				noValidNewPosition = false;
				for (double[] otherPosition : positionList) {
					// Distance > 3*drone diameter (=1.5)
					if (Math.sqrt(Math.pow(x, otherPosition[0]) + Math.pow(y, otherPosition[1])
							+ Math.pow(z, otherPosition[2])) <= 1.5) {
						noValidNewPosition = true;
						break;
					}
					// Check if the vectors are linearly dependent (not needed for {0,0,0})
					if ((otherPosition[0] != 0) || (otherPosition[1] != 0) || (otherPosition[2] != 0)) {
						if ((x*otherPosition[1] - y*otherPosition[0]) == 0 && (x*otherPosition[2] - z*otherPosition[0]) == 0 && (y*otherPosition[2] - z*otherPosition[1]) == 0)
						{
						     noValidNewPosition = true;
						     break;
						}
						
					}
				}
				if (!noValidNewPosition) {
					double[] newPos = { x, y, z };
					positionList.add(newPos);
					entireString += " " + x + " " + y + " " + z;
				}
			}
		}

		try {
			f = new FileWriter(file);
			f.write(entireString);
			f.flush();
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * horizontalAngleOfView verticalAngleOfView imageWidth imageHeight
	 * cameraSeparation weight gravity drag maxThrust maxPitchRate maxRollRate
	 * maxYawRate windSpeedXPoints windSpeedYPoints windSpeedZPoints
	 * windRotationRateAroundXPoints // the wind causes the drone to rotate
	 * around an axis through its center parallel to the world X axis at this
	 * rate. Positive means clockwise when looking at 1 0 0 from 0 0 0. In
	 * degrees per second. windRotationRateAroundYPoints
	 * windRotationRateAroundZPoints "target_ball" x y z // one or more lines
	 * "obstacle_ball" x y z // zero or more lines; not applicable to Milestones
	 * 1.3 and 1.4
	 */
}
