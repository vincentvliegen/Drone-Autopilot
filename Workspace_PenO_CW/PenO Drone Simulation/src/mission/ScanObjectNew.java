package mission;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.graphicalrepresentation.*;
import DroneAutopilot.graphicalrepresentation.secondOption.PolyhedronAPDataNew;
import DroneAutopilot.graphicalrepresentation.secondOption.WorldAPDataNew;
import DroneAutopilot.graphicalrepresentation.secondOption.WorldAPVisualNew;
import DroneAutopilot.scanning.Point;

public class ScanObjectNew extends Mission {

	public float[] previousPosition;
	private PolyhedraCalculations polycalc = new PolyhedraCalculations(getDrone());


	JFrame frame;

	public ScanObjectNew(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
	}

	WorldAPDataNew dataWorld = new WorldAPDataNew();
	WorldAPVisualNew world = new WorldAPVisualNew(dataWorld);

	PolyhedronAPDataNew datapoly = new PolyhedronAPDataNew();

	private void init() {
		frame = new JFrame("AP world");

		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// frame.setSize(1024, 768); // width, height
		frame.setBounds(900, 0, 1024, 768);
		frame.setResizable(false); // Not resizable
		world.requestFocus();
		frame.setVisible(true);
		dataWorld.addPolyhedron(datapoly);
		isSetup = true;
	}

	private boolean isSetup = false;

	@Override
	public void execute() {


		if (!isSetup) {
			init();
		}
		this.getPhysicsCalculations().updateMovement(this.getPhysicsCalculations().getPosition(), this.getPhysicsCalculations().getDirectionOfView());// blijf
//		this.getPhysicsCalculations().updateMovement(this.getPhysicsCalculations().getDirectionOfView());// blijf dezelfde richting kijkenen
		// System.out.println(outerCorners == null);


		try {
			handleSeenTriangles();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void updateGUI() {
		world.display();
	}

	/**
	 * 
	 */
	private void handleSeenTriangles() {
		HashMap<ArrayList<float[]>, ArrayList<double[]>> outerCorners = polycalc
				.getMatchingCorners(getDrone().getLeftCamera(), getDrone().getRightCamera());

		// System.out.println(outerCorners == null);
		//TODO
		double margin = 0.08;
		for (ArrayList<float[]> key : outerCorners.keySet()) {
			float[] outerkey = key.get(0);
			float[] innerkey = key.get(1);
			int rgb = Color.HSBtoRGB(outerkey[0], outerkey[1], outerkey[2]);
			int rgbinner = Color.HSBtoRGB(innerkey[0], innerkey[1], innerkey[2]);
			double[] p1 = outerCorners.get(key).get(0);
			double[] p2 = outerCorners.get(key).get(1);
			double[] p3 = outerCorners.get(key).get(2);

			
			if(datapoly.getIntegerColors().containsKey(rgb)) {
				for(Point p : datapoly.getColorPointsPairs().get(datapoly.getIntegerColors().get(rgb))) {
//					System.out.println(Arrays.toString(p1));
//					System.out.println(p.getX() + " " + p.getY() + " " + p.getZ());
					if(p.matches(p1, margin)) {
						handleMatch(p, p1);
					}
					else if(p.matches(p2, margin)) {
						handleMatch(p,p2);
					}
					else if(p.matches(p3, margin)) {
						handleMatch(p,p3);
					}
				}
			}			
			else {
				// zoek of er punten zijn in de points lijst die overeenkomen
				// als ja: pas punten aan, voeg CustomColor toe
				// als nee: voeg punt toe met de binnen- en buitenkleur
				// voeg een CustomColor toe aan de hashmap met de overeenkomstige punten
				// als een gevonden: pas data aan + voeg extra kleur toe
				// stop na 3 (in het begin een risico, als er teveel gematcht worden ga je totaal foute resultaten krijgen
				CustomColor c = new CustomColor(rgb, rgbinner);
				ArrayList<double[]> pointsOfTriangle = new ArrayList<double[]>();
				pointsOfTriangle.add(p1);
				pointsOfTriangle.add(p2);
				pointsOfTriangle.add(p3);

				ArrayList<Point> matchedPoints = new ArrayList<>();
				for(Point p: datapoly.getPoints()) {

					for(Iterator<double[]> iterator = pointsOfTriangle.iterator(); iterator.hasNext();) {
						double[] scannedPoint = iterator.next();
						if(p.matches(scannedPoint, margin)) {
							handleMatch(p, scannedPoint);
							p.addColor(c);
							iterator.remove();
							matchedPoints.add(p);
						}
					}
				}
				//TODO: voeg overige pointsOfTriangle 
				for(double[] d: pointsOfTriangle) {
					Point pointToAdd = new Point(d[0], d[1], d[2]);
					pointToAdd.addColor(c);
					matchedPoints.add(pointToAdd);
					datapoly.addPoint(pointToAdd);
				}
				datapoly.addColor_Point(c, matchedPoints);

			}
		}
	}




	//		zoek geziene driehoeken
	//		voor elke geziene driehoek: 
	//		kijk in datapoly of al 


	private void handleMatch(Point p, double[] d) {
		double weightOld = 0.85;
		double weightNew = 1-weightOld;
		p.setX(weightOld*p.getX() + weightNew * d[0]);
		p.setY(weightOld*p.getY() + weightNew * d[1]);
		p.setZ(weightOld*p.getZ() + weightNew * d[2]);

	}

}
