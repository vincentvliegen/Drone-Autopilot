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



class Parser_v2 {

	int [] magic = new int[4];
	int version;
	float horizontalAngleOfView;
    float verticalAngleOfView;
    int imageWidth;
    int imageHeight;
    float cameraSeparation;
    float weight;
    float gravity;
    float drag;
    float maxThrust;
    float maxPitchRate;
    float maxRollRate;
    float maxYawRate;
    float[] windSpeedXPoints;
    float[] windSpeedYPoints;
    float[] windSpeedZPoints;
    float[] windRotationAroundXPoints;
    float[] windRotationAroundYPoints;
    float[] windRotationAroundZPoints;
    ArrayList<Polyhedron> objects;
	private World world;

	public Parser_v2(World world) {
		this.world = world;
	}


	public void parse() throws IOException {

		// print working dir
		// System.out.println(System.getProperty("user.dir"));

		DataInputStream dataIn = new DataInputStream(new FileInputStream("E:\\file.txt"));

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
	    imageWidth = dataIn.readUnsignedByte();
	    imageHeight = dataIn.readUnsignedByte();
	    cameraSeparation = dataIn.readFloat();
	    weight = dataIn.readFloat();
	    gravity = dataIn.readFloat();
	    drag = dataIn.readFloat();
	    maxThrust = dataIn.readFloat();
	    maxPitchRate = dataIn.readFloat();
	    maxRollRate = dataIn.readFloat();
	    maxYawRate = dataIn.readFloat();
	    int windSpeedXPointsCount = dataIn.readUnsignedShort();
	    windSpeedXPoints = new float[2*windSpeedXPointsCount];
	    for(int i = 0; i < 2 * windSpeedXPointsCount; i ++) {
	    	windSpeedXPoints[i] = dataIn.readFloat();
	    }
	    int windSpeedYPointsCount = dataIn.readUnsignedShort();
	    windSpeedYPoints = new float[2*windSpeedYPointsCount];
	    for(int i = 0; i < 2 * windSpeedYPointsCount; i ++) {
	    	windSpeedYPoints[i] = dataIn.readFloat();
	    }
	    int windSpeedZPointsCount = dataIn.readUnsignedShort();
	    windSpeedZPoints = new float[2*windSpeedZPointsCount];
	    for(int i = 0; i < 2 * windSpeedZPointsCount; i ++) {
	    	windSpeedZPoints[i] = dataIn.readFloat();
	    }
	    
	    int windRotationAroundXCount = dataIn.readUnsignedShort();
	    windRotationAroundXPoints = new float[2*windRotationAroundXCount];
	    for(int i = 0; i < 2 * windRotationAroundXCount; i ++) {
	    	windRotationAroundXPoints[i] = dataIn.readFloat();
	    }
	    
	    int windRotationAroundYCount = dataIn.readUnsignedShort();
	    windRotationAroundYPoints = new float[2*windRotationAroundYCount];
	    for(int i = 0; i < 2 * windRotationAroundYCount; i ++) {
	    	windRotationAroundYPoints[i] = dataIn.readFloat();
	    }
	    int windRotationAroundZCount = dataIn.readUnsignedShort();
	    windRotationAroundZPoints = new float[2*windRotationAroundZCount];
	    for(int i = 0; i < 2 * windRotationAroundZCount; i ++) {
	    	windRotationAroundZPoints[i] = dataIn.readFloat();
	    }
	    
		
	    readInObjects(dataIn);
		
	    dataIn.close();
	}

	
	private void readInObjects(DataInputStream dataIn) throws IOException {
	    //TODO: kan zijn dat er later nog andere soorten objecten bij komen, dan moet dit wat aangepast..
	    int objectCount = dataIn.readUnsignedShort();
	    objects = new ArrayList<Polyhedron>(objectCount);
	    for(int i = 0; i < objectCount; i++) {
	    	int vertexCount = dataIn.readUnsignedShort();
	    	double[][] vertices = new double[vertexCount][3];
	    	int faceCount = dataIn.readUnsignedShort();
	    	//TODO ook hier: kan zijn dat faces geen driehoeken meer hoeven zijn
	    	ArrayList<Triangle> faces = new ArrayList<Triangle>(faceCount);
	    	for(int j = 0; j < vertexCount; j ++) {
	    		vertices[j][0] = dataIn.readFloat();
	    		vertices[j][1] = dataIn.readFloat();
	    		vertices[j][2] = dataIn.readFloat();
	    	}
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

	private World getWorld() {
		return world;
	}


}
