package simulator.parser;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import simulator.objects.Polyhedron;
import simulator.objects.PolyhedronType;
import simulator.objects.Triangle;
import simulator.world.World;



public class Parser_v2 extends Parser{

	int [] magic = new int[4];
	int version;
    ArrayList<Polyhedron> objects;

	public Parser_v2(World world) {
		super(world);
	}


	public void parse() throws IOException {

		// print working dir
		// System.out.println(System.getProperty("user.dir"));

		DataInputStream dataIn= new DataInputStream(new FileInputStream("inputFiles/GeneratorV2Test.txt"));
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

	
	private float[] readInArray(DataInputStream dataIn, int count) throws IOException {
        float[] temp = new float[2*count];
        for(int i = 0; i < 2 * count; i ++) {
            temp[i] = dataIn.readFloat();
        }
        
        return temp;

		
	}

	
	
	private float[] readInHelper(int count, boolean time, float[] sourceArray) {
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

	
	private void readInObjects(DataInputStream dataIn) throws IOException {
	    //TODO: kan zijn dat er later nog andere soorten objecten bij komen, dan moet dit wat aangepast..
	    int objectCount = dataIn.readUnsignedShort();
	    objects = new ArrayList<Polyhedron>(objectCount);
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

	    	float[] hsv = new float[3];
	    	for (int j = 0; j < faceCount; j ++) {
	    		int v1 = dataIn.readUnsignedShort();
	    		int v2 = dataIn.readUnsignedShort();
	    		int v3 = dataIn.readUnsignedShort();
	    		
	    		
	    		int r = dataIn.readUnsignedByte();
	    		int g = dataIn.readUnsignedByte();
	    		int b = dataIn.readUnsignedByte();
	    		Color.RGBtoHSB(r,g,b,hsv);
	    		
	    		faces.add(new Triangle(getWorld().getGL().getGL2(), vertices[v1], vertices[v2], vertices[v3], new int[]{r,g,b}));
	    	}
	    	//TODO kijk naar type van Polyhedron
	    	//TODO definieer binnenkleur voor driehoeken
	    	PolyhedronType type;
	    	if(hsv[1] < 0.45) {
	    		type = PolyhedronType.OBSTACLE;
	    	}
	    	//TODO else if > 0.55, else throw exception
	    	else
	    		type = PolyhedronType.TARGET;
	    	
	    	Polyhedron poly = new Polyhedron(getWorld(), type, vertices);
	    	poly.addTriangleList(faces);
	    	getWorld().addPolyhedron(poly);
	    	

	    }

	}


}
