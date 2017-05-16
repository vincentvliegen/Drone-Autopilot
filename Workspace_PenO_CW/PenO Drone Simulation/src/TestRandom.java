import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import simulator.generator.Generator_V2;
import simulator.objects.HollowCubePolyhedron;
import simulator.objects.LetterLPolyhedron;
import simulator.objects.Polyhedron;
import simulator.objects.PolyhedronType;
import simulator.objects.SomeFigure;
import simulator.objects.ThreePeaks;
import simulator.objects.Triangle;
import simulator.objects.TwinkleTwinkleLittleStarPolyhedron;
import simulator.parser.Parser_v2;
import simulator.physics.MathCalculations;

public class TestRandom {
	
	static double horizontalAngleOfView;
	static double verticalAngleOfView;
	static int imageWidth;
	static int imageHeight;
	static float cameraSeparation; 
	static double weight ;
	static double gravity; 
	static double drag ;
	static double maxThrust ;
	static double maxPitchRate; 
	static double maxRollRate; 
	static double maxYawRate; 
	static float[] arrayXValues;
	static float[] arrayXTimes;
	static float[] arrayYValues;
	static float[] arrayYTimes;
	static float[] arrayZValues;
	static float[] arrayZTimes;
    
	static float[] windRotationXValues;
	static float[] windRotationXTimes;
	static float[] windRotationYValues;
	static float[] windRotationYTimes;
	static float[] windRotationZValues;
	static float[] windRotationZTimes;
	static float[] hsv = new float[3];

//	public static void main(String[] args) throws IOException {
//		Random r = new Random();
//		float saturationBefore;
//		boolean v = true;
//		int i = 0;
//		DataInputStream dataIn= new DataInputStream(new FileInputStream("inputFiles/outputtest.text"));
//
//		while(v) {
//			saturationBefore = r.nextFloat() * 0.45f + 0.55f;
//			
//			
//    		int red = dataIn.readUnsignedByte();
//    		int g = dataIn.readUnsignedByte();
//    		int b = dataIn.readUnsignedByte();
//    		float[] hsv = new float[3];
//			Color.RGBtoHSB(red,g,b,hsv);					
//			
//			
//			if(saturationBefore < 0.55 && saturationBefore > 0.45) {
//				v = false;
//				System.out.println(saturationBefore);
//			}
//			System.out.println(i);
//			i++;
//
//		}
//
//
//	}
	static int [] magic = new int[4];
	static int version;

	
	public static void main(String[] args) throws IOException {
		boolean noError = true;
		while(noError) {
			generate();
			parse();
			if (hsv[2] < 0.45) { // obstacle
				if (hsv[1] > 0.55) { // outertriangle
				}
			} else if (hsv[2] > 0.55) { // target
				if (hsv[1] < 0.45) { // innertrianglef
				} else if (hsv[1] > 0.55) { // outertriangle
				} else {
					System.out.println(hsv[1]);
					throw new IllegalArgumentException("Foute Sat " + hsv[1]);
				}
			} else {
				System.out.println(hsv[2]);
				throw new IllegalArgumentException("Foute Value " + hsv[2]);
			}
			
		}
		
	}
	
	
	public static void parse() throws IOException {

		// print working dir
		// System.out.println(System.getProperty("user.dir"));

		DataInputStream dataIn= new DataInputStream(new FileInputStream("inputFiles/TestForRandom.text"));
//		DataInputStream dataIn = new DataInputStream(new FileInputStream("inputFiles/world.bin"));

		//u1: writeByte, readUnsignedByte
		//u2: writeShort, readUnsignedShort
		//float: readFloat, writeFloat

		// line 1: magic
		magic[0] = dataIn.readUnsignedByte();
		magic[1] = dataIn.readUnsignedByte();
		magic[2] = dataIn.readUnsignedByte();
		magic[3] = dataIn.readUnsignedByte();

		// line 2
		version = dataIn.readUnsignedByte();
		
		horizontalAngleOfView = dataIn.readFloat();
	    verticalAngleOfView = dataIn.readFloat();
	    imageWidth = dataIn.readUnsignedShort();
	    imageHeight = dataIn.readUnsignedShort();
	    cameraSeparation = dataIn.readFloat();
	    weight = dataIn.readFloat();
	    gravity = dataIn.readFloat();
	    drag = dataIn.readFloat();
	    maxThrust = dataIn.readFloat();
	    maxPitchRate = dataIn.readFloat();
	    maxRollRate = dataIn.readFloat();
	    maxYawRate = dataIn.readFloat();
	    
	    
	    int windSpeedXPointsCount = dataIn.readUnsignedShort();
	    float[] windSpeedXPoints = readInArray(dataIn, windSpeedXPointsCount);
	    arrayXValues = readInHelper(windSpeedXPointsCount, false, windSpeedXPoints);
	    arrayXTimes = readInHelper(windSpeedXPointsCount, true, windSpeedXPoints);
	    int windSpeedYPointsCount = dataIn.readUnsignedShort();
	    float[] windSpeedYPoints = readInArray(dataIn, windSpeedYPointsCount);
	    arrayYValues = readInHelper(windSpeedYPointsCount, false, windSpeedYPoints);
	    arrayYTimes= readInHelper(windSpeedYPointsCount, true, windSpeedYPoints);
	    
	    int windSpeedZPointsCount = dataIn.readUnsignedShort();
	    float[] windSpeedZPoints = readInArray(dataIn, windSpeedZPointsCount);
	    arrayZValues = readInHelper(windSpeedZPointsCount, false, windSpeedZPoints);
	    arrayZTimes = readInHelper(windSpeedZPointsCount, true, windSpeedZPoints);

	    
	    

	    
	    int windRotationAroundXCount = dataIn.readUnsignedShort();
	    float[] windRotationAroundXPoints = readInArray(dataIn, windRotationAroundXCount);
	    windRotationXValues = readInHelper(windRotationAroundXCount, false, windRotationAroundXPoints);
	    windRotationXTimes = readInHelper(windRotationAroundXCount, true, windRotationAroundXPoints);
	    
	    
	    int windRotationAroundYCount = dataIn.readUnsignedShort();
	    float[] windRotationAroundYPoints = readInArray(dataIn, windRotationAroundYCount);
	    windRotationYValues = readInHelper(windRotationAroundYCount, false, windRotationAroundYPoints);
	    windRotationYTimes = readInHelper(windRotationAroundYCount, true, windRotationAroundYPoints);

	    int windRotationAroundZCount = dataIn.readUnsignedShort();
	    float[] windRotationAroundZPoints = readInArray(dataIn, windRotationAroundZCount);
	    windRotationZValues = readInHelper(windRotationAroundZCount, false, windRotationAroundZPoints);
	    windRotationZTimes = readInHelper(windRotationAroundZCount, true, windRotationAroundZPoints);

		
	    readInObjects(dataIn);
		
	    dataIn.close();
	}

	
	private static float[] readInArray(DataInputStream dataIn, int count) throws IOException {
        float[] temp = new float[2*count];
        for(int i = 0; i < 2 * count; i ++) {
            temp[i] = dataIn.readFloat();
        }
        
        return temp;

		
	}

	
	
	private static float[] readInHelper(int count, boolean time, float[] sourceArray) {
		float[] returnArray = new float[count];
		for (int i = 0; i < count * 2; i++) {
			if (time) {
				if (i % 2 == 0) {
					returnArray[i / 2] = sourceArray[i];
				}
			} else {
				if (i % 2 != 0)
					returnArray[(i - 1) / 2] = sourceArray[i];
			}
		}
		return returnArray;
	}	

	
	private static void readInObjects(DataInputStream dataIn) throws IOException {
	    //TODO: kan zijn dat er later nog andere soorten objecten bij komen, dan moet dit wat aangepast..
	    int objectCount = dataIn.readUnsignedShort();
	    for(int i = 0; i < objectCount; i++) {
	    	int vertexCount = dataIn.readUnsignedShort();
	    	double[][] vertices = new double[vertexCount][3];
	    	for(int j = 0; j < vertexCount; j ++) {
	    		vertices[j][0] = dataIn.readFloat();
	    		vertices[j][1] = dataIn.readFloat();
	    		vertices[j][2] = dataIn.readFloat();
	    	}
	    	int faceCount = dataIn.readUnsignedShort();
	    	//TODO ook hier: kan zijn dat faces geen driehoeken meer hoeven zijn
	    	ArrayList<Triangle> faces = new ArrayList<Triangle>(faceCount);

	    	
	    	for (int j = 0; j < faceCount; j ++) {
	    		int v1 = dataIn.readUnsignedShort();
	    		int v2 = dataIn.readUnsignedShort();
	    		int v3 = dataIn.readUnsignedShort();
	    		
	    		
	    		int r = dataIn.readUnsignedByte();
	    		int g = dataIn.readUnsignedByte();
	    		int b = dataIn.readUnsignedByte();
	    		Color.RGBtoHSB(r,g,b,hsv);
	    		
	    	}
	    	//TODO kijk naar type van Polyhedron
	    	//TODO definieer binnenkleur voor driehoeken
	    	PolyhedronType type;
	    	if(hsv[2] < 0.45) {
	    		type = PolyhedronType.OBSTACLE;
	    	}
	    	//TODO else if > 0.55, else throw exception
	    	else {
	    		type = PolyhedronType.TARGET;
	    	}}
	    	

	    }

	
	static void generate() throws FileNotFoundException {
		String path = "inputFiles/TestForRandom.text";
		boolean windOn = false;
		boolean obstaclesOn = true;

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
			} else {
				for (int i = 0; i<6; i++) {
					stream.writeShort(1);
					stream.writeFloat(1.0f);
					stream.writeFloat(0.0f);
				}
			}

			// Objects
			int numberOfObjects = r.nextInt(7) + 10;
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