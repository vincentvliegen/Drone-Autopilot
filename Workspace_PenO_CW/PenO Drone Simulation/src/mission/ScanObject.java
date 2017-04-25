package mission;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.graphicalrepresentation.PolyhedronAPData;
import DroneAutopilot.graphicalrepresentation.TriangleAPData;
import DroneAutopilot.graphicalrepresentation.WorldAPData;
import DroneAutopilot.graphicalrepresentation.WorldAPVisual;

public class ScanObject extends Mission {

	public float[] previousPosition;
	public float[] target;
	private ArrayList<float[]> drawnTriangles = new ArrayList<float[]>();

	private PolyhedraCalculations polycalc = new PolyhedraCalculations(getDrone());

	private final float quadraticMargin = 0.001f;
	private ArrayList<ArrayList<double[]>> scannedLines = new ArrayList<ArrayList<double[]>>();

	JFrame frame;

	public ScanObject(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
	}

	WorldAPData dataWorld = new WorldAPData();
	WorldAPVisual world = new WorldAPVisual(dataWorld);

	PolyhedronAPData datapolyly = new PolyhedronAPData();

	private void init() {
		frame = new JFrame("AP world");

		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
//		frame.setSize(1024, 768); // width, height
		frame.setBounds(900, 0, 1024, 768);
		frame.setResizable(false); // Not resizable
		world.requestFocus();
		frame.setVisible(true);
		dataWorld.addPolyhedron(datapolyly);
		isSetup = true;
	}

	private boolean isSetup = false;

	@Override
	public void execute() {

		/*
		 * if(this.getDroneAutopilot().isFirstHover()){ this.setTarget(new
		 * float[] {this.getDrone().getX(), this.getDrone().getY(),
		 * this.getDrone().getZ()}); }
		 * this.getPhysicsCalculations().updatePosition(this.getTarget());
		 * //blijf opdezelfde plaats
		 */

		if (!isSetup) {
			init();
		}

		this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());// blijf
																											// dezelfde
																											// richting
																											// kijkenen
		HashMap<ArrayList<float[]>, ArrayList<double[]>> outerCorners = polycalc
				.getMatchingCorners(getDrone().getLeftCamera(), getDrone().getRightCamera());
//		System.out.println(outerCorners == null);
		for (ArrayList<float[]> key : outerCorners.keySet()) {
			float[] outerkey = key.get(0);
			float[] innerkey = key.get(1);
			try {
				if (!drawnTriangles.contains(outerkey)) {
					int rgb = Color.HSBtoRGB(outerkey[0], outerkey[1], outerkey[2]);
					int rgbinner = Color.HSBtoRGB(innerkey[0], innerkey[1], innerkey[2]);
					datapolyly.addTriangleToPolyhedron(new TriangleAPData(outerCorners.get(key).get(0),
							outerCorners.get(key).get(1), outerCorners.get(key).get(2), rgbinner, rgb));
					drawnTriangles.add(outerkey);
				}
			}

			catch (Exception e) {
			}

		}
		
		addScannedLines(outerCorners);
System.out.println("scannedLines size: "+scannedLines.size());
	}

	@Override
	public void updateGUI() {
		world.display();

	}

	public void addScannedLines(HashMap<ArrayList<float[]>, ArrayList<double[]>> corners) {
		for (ArrayList<float[]> key : corners.keySet()) {
			double[] corner1 = corners.get(key).get(0);
			double[] corner2 = corners.get(key).get(1);
			double[] corner3 = corners.get(key).get(2);
			boolean[] linePrevFound = { false, false, false }; // lijn12,lijn13,lijn23

			
			
		for (Iterator<ArrayList<double[]>> it = scannedLines.iterator(); it.hasNext(); ) {
			ArrayList<double[]> line = it.next();
				// if corner1 binnen marge 1e punt lijn
				double cor1line1 = (Math.pow(line.get(0)[0] - corner1[0], 2) + Math.pow(line.get(0)[1] - corner1[1], 2) + Math.pow(line.get(0)[2] - corner1[2],2));
				System.out.println("cor1line1: "+cor1line1);
				double cor2line1 = Math.pow(line.get(0)[0] - corner2[0], 2) + Math.pow(line.get(0)[1] - corner2[1], 2) + Math.pow(line.get(0)[2] - corner2[2], 2);
				System.out.println("cor2line1: "+cor2line1);
				double cor3line1 = Math.pow(line.get(0)[0] - corner3[0], 2) + Math.pow(line.get(0)[1] - corner3[1], 2) + Math.pow(line.get(0)[2] - corner3[2], 2);
				System.out.println("cor3line1: "+cor3line1);
				if (Math.pow(line.get(0)[0] - corner1[0], 2) + Math.pow(line.get(0)[1] - corner1[1], 2) + Math.pow(line.get(0)[2] - corner1[2],2) < this
						.getQuadraticMargin()) {
					// if corner2 binnen marge 2e punt lijn
					if (Math.pow(line.get(1)[0] - corner2[0], 2) + Math.pow(line.get(1)[1] - corner2[1], 2) + Math.pow(line.get(1)[2] - corner2[2],2) < this
							.getQuadraticMargin()) {
						linePrevFound[0] = true;
						it.remove();
						continue;
					}
					// if corner3 binnen marge 2e punt lijn
					else if (Math.pow(line.get(1)[0] - corner3[0], 2) + Math.pow(line.get(1)[1] - corner3[1], 2) + Math.pow(line.get(1)[2] - corner3[2],2) < this
							.getQuadraticMargin()) {
						linePrevFound[1] = true;
						it.remove();
						continue;
					}

				}
				// if corner2 binnen marge 1e punt lijn
				else if (Math.pow(line.get(0)[0] - corner2[0], 2) + Math.pow(line.get(0)[1] - corner2[1], 2) + Math.pow(line.get(0)[2] - corner2[2], 2)< this
						.getQuadraticMargin()) {
					// if corner1 binnen marge 2e punt lijn
					if (Math.pow(line.get(1)[0] - corner1[0], 2) + Math.pow(line.get(1)[1] - corner1[1], 2) + Math.pow(line.get(1)[2] - corner1[2], 2)< this
							.getQuadraticMargin()) {
						linePrevFound[0] = true;
						it.remove();
						continue;
					}
					// if corner3 binnen marge 2e punt lijn
					else if (Math.pow(line.get(1)[0] - corner3[0], 2) + Math.pow(line.get(1)[1] - corner3[1], 2) + Math.pow(line.get(1)[2] - corner3[2], 2)< this
							.getQuadraticMargin()) {
						linePrevFound[2] = true;
						it.remove();
						continue;
					}

				}
		
				// if corner3 binnen marge 1e punt lijn
				else if (Math.pow(line.get(0)[0] - corner3[0], 2) + Math.pow(line.get(0)[1] - corner3[1], 2) + Math.pow(line.get(0)[2] - corner3[2], 2)< this
						.getQuadraticMargin()) {
					// if corner1 binnen marge 2e punt lijn
					if (Math.pow(line.get(1)[0] - corner1[0], 2) + Math.pow(line.get(1)[1] - corner1[1], 2) + Math.pow(line.get(1)[2] - corner1[2], 2)< this
							.getQuadraticMargin()) {
						linePrevFound[0] = true;
						it.remove();
						continue;
					}
					// if corner2 binnen marge 2e punt lijn
					else if (Math.pow(line.get(1)[0] - corner2[0], 2) + Math.pow(line.get(1)[1] - corner2[1], 2) + Math.pow(line.get(1)[2] - corner2[2], 2)< this
							.getQuadraticMargin()) {
						linePrevFound[2] = true;
						it.remove();
						continue;
					}

				}
			}
			if (!linePrevFound[0]) {
				ArrayList<double[]> line = new ArrayList<double[]>();
				line.add(corners.get(key).get(0));
				line.add(corners.get(key).get(1));
				scannedLines.add(line);
			}
			if (!linePrevFound[1]) {
				ArrayList<double[]> line = new ArrayList<double[]>();
				line.add(corners.get(key).get(0));
				line.add(corners.get(key).get(2));
				scannedLines.add(line);
			}
			if (!linePrevFound[2]) {
				ArrayList<double[]> line = new ArrayList<double[]>();
				line.add(corners.get(key).get(1));
				line.add(corners.get(key).get(2));
				scannedLines.add(line);
			}
		}
	}

	

	////////// GETTERS & SETTERS//////////

	public float[] getTarget() {
		return target;
	}

	public void setTarget(float[] target) {
		this.target = target;

	}

	public float getQuadraticMargin() {
		return quadraticMargin;
	}

}
