package generator;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Generator {

	public static void main(String[] args) {
		String path = "/home/r0578402/git/zilver/Workspace_PenO_CW/PenO Drone Simulation/inputFiles/GeneratorTest";
		File file = new File(path);
		FileWriter f;

		Random r = new Random();

		double horAngle = 120;
		double verAngle = 120;
		double imgWidth = 200;
		double imgHeigth = 200;
		double cameraSep = 0.25f;
		double weight = 1;
		double gravity = 9.81f;
		double drag = 0.2f;
		double maxThrust = 25;
		double maxPitch = 720;
		double maxRoll = 720;
		double maxYaw = 720;

		String entireString = horAngle + " " + verAngle + " " + imgWidth + " "
				+ imgHeigth + " " + cameraSep + " " + weight + " " + gravity
				+ " " + drag + " " + maxThrust + " " + maxPitch + " " + maxRoll
				+ " " + maxYaw;
		
		for (int i = 0; i < 3; i++) {
			entireString += "\n";
			for (int j = 0; j < 6; j++) {
				entireString += (j*2 + r.nextDouble()) + " " + r.nextDouble()
						* 0.05 + " ";
			}
		}

		for (int i = 0; i < 3; i++) {
			entireString += "\n";
			for (int j = 0; j < 6; j++) {
				entireString += (j*2 + r.nextDouble()) + " " + r.nextDouble()
						* 0.5 + " ";
			}
		}
		
		//TODO Add target ball, obstacle balls (Placement restrictions!)
		
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
