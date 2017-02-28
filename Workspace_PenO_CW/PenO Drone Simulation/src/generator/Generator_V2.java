package generator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.PriorityQueue;
import java.util.Random;

public class Generator_V2 {

	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * WorldDescriptionFile_v2 { 
		 * 		u1 magic[4] = {'W','D','F','F'}; // The
		 * first four bytes of the file are the ASCII characters 'W', 'D', 'F',
		 * 'F'. 
		 * 		u1 version = 2; 
		 * 		float horizontalAngleOfView; 
		 * 		float verticalAngleOfView; 
		 * 		u2 imageWidth; 
		 * 		u2 imageHeight; 
		 * 		float cameraSeparation; 
		 * 		float weight; 
		 *		float gravity; 
		 * 		float drag; 
		 *		float maxThrust; 
		 * 		float maxPitchRate; 
		 * 		float maxRollRate; 
		 * 		float maxYawRate;
		 * 		u2 windSpeedXPointsCount; 
		 * 		float windSpeedXPoints[2*windSpeedXPointsCount]; 
		 * 		u2 windSpeedYPointsCount;
		 *		float windSpeedYPoints[2*windSpeedYPointsCount]; 
		 * 		u2 windSpeedZPointsCount; 
		 * 		float windSpeedZPoints[2*windSpeedZPointsCount]; 
		 * 		u2 windRotationAroundXCount; 
		 * 		float windRotationAroundXPoints[2*windRotationAroundXPointsCount]; 
		 * 		u2 windRotationAroundYCount; 
		 * 		float windRotationAroundYPoints[2*windRotationAroundYPointsCount]; 
		 * 		u2 windRotationAroundZCount; 
		 * 		float windRotationAroundZPoints[2*windRotationAroundZPointsCount]; 
		 * 		u2 objectCount; 
		 * 		object objects[objectCount]; 
		 * }
		 * 
		 * object { 
		 * 		u2 vertexCount; 
		 * 		vertex vertices[vertexCount]; 
		 * 		u2 faceCount;
		 * 		face faces[faceCount]; 
		 * }
		 * 
		 * vertex { 	
		 * 		float x; 
		 * 		float y; 
		 * 		float z; 
		 * }
		 * 
		 * face { // The three vertices are sorted clockwise when viewed from
		 * outside the object 
		 * 		u2 vertex1; // 0-based index into array 'vertices'
		 * 		u2 vertex2; 
		 * 		u2 vertex3; // Outline color 
		 * 		u1 red; 
		 * 		u1 green; 
		 * 		u1 blue;
		 * // The inside color should be a desaturated version of the outline
		 * color. A future version of the file format may allow separately
		 * specifying the color or texture of the inside. }
		 * 
		 * u1 = byte
		 * u2 = short
		 * float = float :^)
		 */
		
		String path = "C:/Users/versy/git/zilver/Workspace_PenO_CW/PenO Drone Simulation/inputFiles/GeneratorV2Test.txt";
		String path2 = "/home/r0578402/git/zilver/Workspace_PenO_CW/PenO Drone Simulation/inputFiles/GeneratorV2Test.txt";
		
		File f = new File(path2);
		OutputStream fileStream = new FileOutputStream(f);
		DataOutputStream stream = new DataOutputStream(fileStream);
		Random r = new Random();
		
		
		try {
			//Magic
			stream.writeByte(87);
			stream.writeByte(68);
			stream.writeByte(70);
			stream.writeByte(70);
			
			//Standard constants
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
			
			//Wind
			PriorityQueue<Float> newQueue = new PriorityQueue<>();

			for (int i = 0; i < 6; i++) {
				stream.writeShort(6);
				for (int j = 0; j < 6; j++) {
					newQueue.add(r.nextFloat() * 15);
				}
				if (i < 3)
					for (int j = 0; j < 6; j++) {
						stream.writeFloat(newQueue.poll());
						stream.writeFloat(r.nextFloat()*0.05f);
					}
				else {
					for (int j = 0; j < 6; j++) {
						stream.writeFloat(newQueue.poll());
						stream.writeFloat(r.nextFloat()*0.5f);
					}
				}
			}
			
			//Objects
			stream.writeShort(2);
			
			//Object 1
			stream.writeShort(4);
			
			stream.writeFloat(4.8f);
			stream.writeFloat(0);
			stream.writeFloat(-0.2f);
			
			stream.writeFloat(4.8f);
			stream.writeFloat(0);
			stream.writeFloat(0.2f);
			
			stream.writeFloat(5.2f);
			stream.writeFloat(0);
			stream.writeFloat(0);
			
			stream.writeFloat(5);
			stream.writeFloat(0.2f);
			stream.writeFloat(0);
			
			stream.writeShort(4);
			
			stream.writeShort(0);
			stream.writeShort(1);
			stream.writeShort(2);
			stream.writeByte(0);
			stream.writeByte(1);
			stream.writeByte(0);
			
			stream.writeShort(0);
			stream.writeShort(1);
			stream.writeShort(3);
			stream.writeByte(1);
			stream.writeByte(1);
			stream.writeByte(0);
			
			stream.writeShort(0);
			stream.writeShort(3);
			stream.writeShort(2);
			stream.writeByte(0);
			stream.writeByte(0);
			stream.writeByte(1);
			
			stream.writeShort(1);
			stream.writeShort(3);
			stream.writeShort(2);
			stream.writeByte(1);
			stream.writeByte(0);
			stream.writeByte(0);
			
			//Object 2
			stream.writeShort(4);
			
			stream.writeFloat(-0.2f);
			stream.writeFloat(5);
			stream.writeFloat(-0.2f);
			
			stream.writeFloat(-0.2f);
			stream.writeFloat(5);
			stream.writeFloat(0.2f);
			
			stream.writeFloat(0.2f);
			stream.writeFloat(5);
			stream.writeFloat(0);
			
			stream.writeFloat(0);
			stream.writeFloat(5.2f);
			stream.writeFloat(0);
			
			stream.writeShort(4);
			
			stream.writeShort(0);
			stream.writeShort(1);
			stream.writeShort(2);
			stream.writeByte(2);
			stream.writeByte(2);
			stream.writeByte(2);
			
			stream.writeShort(0);
			stream.writeShort(1);
			stream.writeShort(3);
			stream.writeByte(3);
			stream.writeByte(3);
			stream.writeByte(3);
			
			stream.writeShort(0);
			stream.writeShort(3);
			stream.writeShort(2);
			stream.writeByte(4);
			stream.writeByte(4);
			stream.writeByte(4);
			
			stream.writeShort(1);
			stream.writeShort(3);
			stream.writeShort(2);
			stream.writeByte(5);
			stream.writeByte(5);
			stream.writeByte(5);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		
	}

}
