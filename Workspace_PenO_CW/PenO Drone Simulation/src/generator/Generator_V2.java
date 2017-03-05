package generator;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.*;

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
			stream.writeShort(2);

			// Object 1 == target
			stream.writeShort(4);
			// Locations of points
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
			// Connect points + colors

			List<int[]> colorListRGB = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
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

			for (int i = 0; i < 4; i++) {
				int[] currentcolor = colorListRGB.get(i);
				switch (i) {
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

			colorListRGB.clear();
			// Object 2 == obstacle
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

			for (int i = 0; i < 4; i++) {
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

			for (int i = 0; i < 4; i++) {
				int[] currentcolor = colorListRGB.get(i);
				switch (i) {
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
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
