package generator;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

import simulator.objects.*;
import simulator.physics.MathCalculations;

public class Generator_V2 {

	public static void main(String[] args) throws FileNotFoundException {
		String path = "inputFiles/GeneratorV2Test.txt";
		boolean windOn = false;
		boolean obstaclesOn = true;
		boolean emptyWorld = false;

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

			if (windOn) {
			for (int i = 0; i < 6; i++) {
				stream.writeShort(6);
				for (int j = 0; j < 6; j++) {
					newQueue.add(r.nextFloat() * 15 + 3);
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
			} else {
				for (int i = 0; i<6; i++) {
					stream.writeShort(1);
					stream.writeFloat(1.0f);
					stream.writeFloat(0.0f);
				}
			}

			// Objects
			int numberOfObjects;
			if (!emptyWorld)
				numberOfObjects = r.nextInt(7) + 10;
			else
				numberOfObjects = 0;
			int numberOfObstacles = numberOfObjects / 2;
			int numberOfTargets = numberOfObjects - numberOfObstacles;
			if (obstaclesOn) {
				stream.writeShort(numberOfObjects);
			} else {
				stream.writeShort(numberOfObstacles);
			}
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
						
			// Target objects
			int currentPosIndex = 1;
			for (int i = 0; i < numberOfTargets; i++) {
				float x = (float) positionList.get(currentPosIndex)[0];
				float y = (float) positionList.get(currentPosIndex)[1];
				float z = (float) positionList.get(currentPosIndex)[2];
				currentPosIndex+=1;
				int randomPoly = r.nextInt(5);
				
				int[][] faces;
				double[][] points;
				
				switch(randomPoly) {
				case 0:
					// HollowCube
					faces = HollowCubePolyhedron.getFaces();
					points = HollowCubePolyhedron.getPoints();
					break;
				case 1:
					// LetterL
					faces = LetterLPolyhedron.getFaces();
					points = LetterLPolyhedron.getPoints();
					break;
				case 2:
					//SomeFigure
					faces = SomeFigure.getFaces();
					points = SomeFigure.getPoints();
					break;
				case 3:
					//ThreePeaks
					faces = ThreePeaks.getFaces();
					points = ThreePeaks.getPoints();
					break;
				case 4:
					//Star
					faces = TwinkleTwinkleLittleStarPolyhedron.getFaces();
					points = TwinkleTwinkleLittleStarPolyhedron.getPoints();
					break;
				default:
					throw new IllegalArgumentException();
				}
				stream.writeShort(points.length);
				
				for (double[] point:points) {
					stream.writeFloat(x + (float)point[0]);
					stream.writeFloat(y + (float)point[1]);
					stream.writeFloat(z + (float)point[2]);
				}
				
				for (int j = 0; j < faces.length; j++) {
					int hueDegrees = r.nextInt(361); 
					float hueRadians = (float)Math.toRadians(hueDegrees);
					float saturation = r.nextFloat() * (1 - 0.56f) + 0.56f;
					float brightness = r.nextFloat() * (1 - 0.56f) + 0.56f;
					int rgb = Color.HSBtoRGB(hueRadians, saturation, brightness);
					int[] color = new int[3];
					color[0] = (rgb >> 16) & 0xFF;
					color[1] = (rgb >> 8) & 0xFF;
					color[2] = rgb & 0xFF;
					colorListRGB.add(color);
				}
				
				stream.writeShort(faces.length);
				for (int k = 0; k < faces.length; k++){
					int[] currentColor = colorListRGB.get(k);
					stream.writeShort(faces[k][0]);
					stream.writeShort(faces[k][1]);
					stream.writeShort(faces[k][2]);
					stream.writeByte(currentColor[0]);
					stream.writeByte(currentColor[1]);
					stream.writeByte(currentColor[2]);
				}
				colorListRGB.clear();
			}

			
			// Obstacle
			if (obstaclesOn) {
			for (int i = 0; i < numberOfObstacles; i++) {
				float x = (float) positionList.get(currentPosIndex)[0];
				float y = (float) positionList.get(currentPosIndex)[1];
				float z = (float) positionList.get(currentPosIndex)[2];
				currentPosIndex+=1;
				int randomPoly = r.nextInt(5);
				
				int[][] faces;
				double[][] points;
				
				switch(randomPoly) {
				case 0:
					// HollowCube
					faces = HollowCubePolyhedron.getFaces();
					points = HollowCubePolyhedron.getPoints();
					break;
				case 1:
					// LetterL
					faces = LetterLPolyhedron.getFaces();
					points = LetterLPolyhedron.getPoints();
					break;
				case 2:
					//SomeFigure
					faces = SomeFigure.getFaces();
					points = SomeFigure.getPoints();
					break;
				case 3:
					//ThreePeaks
					faces = ThreePeaks.getFaces();
					points = ThreePeaks.getPoints();
					break;
				case 4:
					//Star
					faces = TwinkleTwinkleLittleStarPolyhedron.getFaces();
					points = TwinkleTwinkleLittleStarPolyhedron.getPoints();
					break;
				default:
					throw new IllegalArgumentException();
				}
				stream.writeShort(points.length);
				
				for (double[] point:points) {
					stream.writeFloat(x + (float)point[0]);
					stream.writeFloat(y + (float)point[1]);
					stream.writeFloat(z + (float)point[2]);
				}
				
				for (int j = 0; j < faces.length; j++) {
					int hueDegrees = r.nextInt(361);
					float hueRadians = (float)Math.toRadians(hueDegrees);
					float saturation = r.nextFloat() * (1 - 0.56f) + 0.56f;
					float brightness = r.nextFloat() * 0.44f;
					int rgb = Color.HSBtoRGB(hueRadians, saturation, brightness);
					int[] color = new int[3];
					color[0] = (rgb >> 16) & 0xFF;
					color[1] = (rgb >> 8) & 0xFF;
					color[2] = rgb & 0xFF;
					colorListRGB.add(color);
				}
				
				stream.writeShort(faces.length);
				for (int k = 0; k < faces.length; k++){
					int[] currentColor = colorListRGB.get(k);
					stream.writeShort(faces[k][0]);
					stream.writeShort(faces[k][1]);
					stream.writeShort(faces[k][2]);
					stream.writeByte(currentColor[0]);
					stream.writeByte(currentColor[1]);
					stream.writeByte(currentColor[2]);
				}
				colorListRGB.clear();
			}
			}
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
