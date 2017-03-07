package generator;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.*;

import simulator.physics.MathCalculations;

public class Generator_V2 {

	public static void main(String[] args) throws FileNotFoundException {
		String path = "inputFiles/GeneratorV2Test.txt";

		File f = new File(path);
		OutputStream fileStream = new FileOutputStream(f);
		DataOutputStream stream = new DataOutputStream(fileStream);
		Random r = new Random();

		try {
			// Magic
			stream.writeByte(87);
			stream.writeByte(68);
			stream.writeByte(70);
			stream.writeByte(70);

			// Standard constants
			stream.writeByte(2);
			stream.writeFloat(120);
			stream.writeFloat(120);
			stream.writeShort(200);
			stream.writeShort(200);
			stream.writeFloat(0.25f);
			stream.writeFloat(1.0f);
			stream.writeFloat(9.81f);
			stream.writeFloat(0.2f);
			stream.writeFloat(25);
			stream.writeFloat(720.0f);
			stream.writeFloat(720.0f);
			stream.writeFloat(720.0f);

			// Wind
			PriorityQueue<Float> newQueue = new PriorityQueue<>();

			for (int i = 0; i < 6; i++) {
				stream.writeShort(6);
				for (int j = 0; j < 6; j++) {
					newQueue.add(r.nextFloat() * 15);
				}
				if (i < 3)
					for (int j = 0; j < 6; j++) {
						stream.writeFloat(newQueue.poll());
						stream.writeFloat(r.nextFloat() * 0.05f);
					}
				else {
					for (int j = 0; j < 6; j++) {
						stream.writeFloat(newQueue.poll());
						stream.writeFloat(r.nextFloat() * 0.5f);
					}
				}
			}

			// Objects
			int numberOfObjects = r.nextInt(7) + 5;
			int numberOfObstacles = numberOfObjects / 2;
			int numberOfTargets = numberOfObjects - numberOfObstacles;
			stream.writeShort(numberOfObjects);

			// Calculate all positions
			List<double[]> positionList = new ArrayList<>();
			positionList.add(new double[] { 0, 0, 0 });

			for (int i = 0; i < numberOfObjects; i++) {
				boolean noValidNewPosition = true;
				while (noValidNewPosition) {
					double x = (r.nextDouble() - .5) * 10;
					double y = (r.nextDouble() - .5) * 10;
					double z = (r.nextDouble() - .5) * 10;
					double[] newPos = { x, y, z };
					noValidNewPosition = false;
					for (double[] otherPosition : positionList) {
						// Distance > 3*drone diameter (=1.5)
						if (MathCalculations.getDistanceBetweenPoints(
								otherPosition, newPos) <= 2) {
							noValidNewPosition = true;
							break;
						}
						// Check if the vectors are linearly dependent (not
						// needed for {0,0,0})
						if ((otherPosition[0] != 0) || (otherPosition[1] != 0)
								|| (otherPosition[2] != 0)) {
							if ((x * otherPosition[1] - y * otherPosition[0]) == 0
									&& (x * otherPosition[2] - z
											* otherPosition[0]) == 0
									&& (y * otherPosition[2] - z
											* otherPosition[1]) == 0) {
								noValidNewPosition = true;
								break;
							}

						}
					}
					if (!noValidNewPosition) {
						positionList.add(newPos);
					}
				}
			}

			// create target colors
			List<int[]> colorListRGB = new ArrayList<>();
			for (int i = 0; i < numberOfTargets * 4; i++) {
				int hue = r.nextInt(361);
				float saturation = r.nextFloat() * 0.45f + 0.55f;
				float brightness = r.nextFloat() * 0.45f + 0.55f;
				int rgb = Color.HSBtoRGB(hue, saturation, brightness);
				int[] color = new int[3];
				color[0] = ((rgb >> 16) & 0xFF);
				color[1] = ((rgb >> 8) & 0xFF);
				color[2] = (rgb & 0xFF);
				colorListRGB.add(color);
			}

			// Target objects
			int currentPosIndex = 1;
			for (int i = 0; i < numberOfTargets; i++) {
				stream.writeShort(4);
				float x = (float) positionList.get(currentPosIndex)[0];
				float y = (float) positionList.get(currentPosIndex)[1];
				float z = (float) positionList.get(currentPosIndex)[2];
				currentPosIndex+=1;
				
				stream.writeFloat(-0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(-0.2f + z);

				stream.writeFloat(-0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(0.2f + z);

				stream.writeFloat(0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(z);

				stream.writeFloat(x);
				stream.writeFloat(y + 0.2f);
				stream.writeFloat(z);

				stream.writeShort(4);

				for (int j = 0; j < 4 * numberOfTargets; j++) {
					int[] currentcolor = colorListRGB.get(j);
					switch (j) {
					case 0:
						stream.writeShort(0);
						stream.writeShort(1);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 1:
						stream.writeShort(0);
						stream.writeShort(1);
						stream.writeShort(3);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 2:
						stream.writeShort(0);
						stream.writeShort(3);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 3:
						stream.writeShort(1);
						stream.writeShort(3);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					}
				}

			}

			colorListRGB.clear();
			// Obstacle
			for (int i = 0; i < 4 * numberOfObstacles; i++) {
				int hue = r.nextInt(361);
				float saturation = r.nextFloat() * 0.45f + 0.55f;
				float brightness = r.nextFloat() * 0.45f;
				int rgb = Color.HSBtoRGB(hue, saturation, brightness);
				int[] color = new int[3];
				color[0] = ((rgb >> 16) & 0xFF);
				color[1] = ((rgb >> 8) & 0xFF);
				color[2] = (rgb & 0xFF);
				colorListRGB.add(color);
			}

			for (int i = 0; i < numberOfObstacles; i++) {
				stream.writeShort(4);
				float x = (float) positionList.get(currentPosIndex)[0];
				float y = (float) positionList.get(currentPosIndex)[1];
				float z = (float) positionList.get(currentPosIndex)[2];
				currentPosIndex+=1;
				stream.writeFloat(-0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(-0.2f + z);

				stream.writeFloat(-0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(0.2f + z);

				stream.writeFloat(0.2f + x);
				stream.writeFloat(y);
				stream.writeFloat(z);

				stream.writeFloat(x);
				stream.writeFloat(y + 0.2f);
				stream.writeFloat(z);

				stream.writeShort(4);

				for (int j = 0; j < 4 * numberOfObstacles; j++) {
					int[] currentcolor = colorListRGB.get(j);
					switch (j) {
					case 0:
						stream.writeShort(0);
						stream.writeShort(1);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 1:
						stream.writeShort(0);
						stream.writeShort(1);
						stream.writeShort(3);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 2:
						stream.writeShort(0);
						stream.writeShort(3);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					case 3:
						stream.writeShort(1);
						stream.writeShort(3);
						stream.writeShort(2);
						stream.writeByte(currentcolor[0]);
						stream.writeByte(currentcolor[1]);
						stream.writeByte(currentcolor[2]);
						break;
					}
				}

			}

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
