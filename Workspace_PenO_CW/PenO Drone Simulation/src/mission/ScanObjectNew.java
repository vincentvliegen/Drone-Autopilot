package mission;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.graphicalrepresentation.*;
import DroneAutopilot.graphicalrepresentation.secondOption.Edge;
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
		world.setJPanel((JPanel) frame.getContentPane());
		world.requestFocus();
		frame.setVisible(true);
		dataWorld.addPolyhedron(datapoly);
		isSetup = true;
		System.out.println("init");
	}

	private boolean isSetup = false;

	@Override
	public void execute() {


		if (!isSetup) {
			init();
		}
//		this.getPhysicsCalculations().updateMovement(new double[]{2,0,0}, this.getPhysicsCalculations().getDirectionOfView());// blijf
//		this.getPhysicsCalculations().updateMovement(new double[]{2,0,1}, new double[] {2,0,0});// vlieg een beetje schuin

//		this.getPhysicsCalculations().updateMovement(this.getPhysicsCalculations().getPosition(), this.getPhysicsCalculations().getDirectionOfView());// blijf


		try {
			handleSeenTriangles();
		} catch (Exception e) {
			e.printStackTrace();
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
//		writeTakeImageToFile();
		double margin = 0.08;
		for (ArrayList<float[]> key : outerCorners.keySet()) {
			float[] outerkey = key.get(0);
			float[] innerkey = key.get(1);
			int rgb = Color.HSBtoRGB(outerkey[0], outerkey[1], outerkey[2]);
			int rgbinner = Color.HSBtoRGB(innerkey[0], innerkey[1], innerkey[2]);
			double[] p1 = outerCorners.get(key).get(0);
			double[] p2 = outerCorners.get(key).get(1);
			double[] p3 = outerCorners.get(key).get(2);

			// if this color was seen before, we try to match the points seen with the ones already registered
			if(datapoly.getIntegerColors().containsKey(rgb)) {
				for(Point p : datapoly.getColorPointsPairs().get(datapoly.getIntegerColors().get(rgb))) {
					if(p.matches(p1, margin)) {
						handleMatch(p, p1);
					}
					else if(p.matches(p2, margin)) {
						handleMatch(p,p2);
					}
					else if(p.matches(p3, margin)) {
						handleMatch(p,p3);
					}
					else {
						//TODO: moet hier iets gebeuren?
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
				//hierboven: punten die gezien zijn door image processing
				ArrayList<Point> matchedPoints = new ArrayList<>();

				// kijken of de punten gezien door image processing matchbaar zijn met punten die al gezien zijn, 
				// dus waar we een kleur aan kunnen toevoegen

				for(Point p: datapoly.getPoints()) {
					for(Iterator<double[]> iterator = pointsOfTriangle.iterator(); iterator.hasNext();) {
						double[] scannedPoint = iterator.next();
						// als we een bestaand punt vinden dat matcht, passen we de positie een beetje aan, en voegen we de kleur toe aan het punt
		
						if(p.matches(scannedPoint, margin)) {
							handleMatch(p, scannedPoint);
							p.addColor(c);
							iterator.remove();
							matchedPoints.add(p);
						}
					}
				}
				System.out.println(pointsOfTriangle.size() + matchedPoints.size() == 3);
				
				//we voegen de kleur toe aan de edges van de punten die reeds bestonden (matchedPoints dus)
				addColorToExistingEdges(matchedPoints, c);
				System.out.println("after");
				
				// van de punten die we niet konden matchen met een bestaand punt, maken we een nieuw Point + we maken een Edge tussen het neiuwe punt en degenen die reeds bestonden
				for(double[] d: pointsOfTriangle) {
					Point pointToAdd = new Point(d[0], d[1], d[2]);
					pointToAdd.addColor(c);
					
					
					for(Point matchedPoint: matchedPoints) {
						datapoly.addNewEdgeWithThesePoints(pointToAdd, matchedPoint, c);
					}
					matchedPoints.add(pointToAdd);
					datapoly.addPoint(pointToAdd);
				}
				// voeg de CustomColor toe, waartoe de nieuwe punten behoren en degenen die gematcht zijn; zij vormen samen een driehoek. 
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
	
	//voor uitschrijven naar bestand
	public void writeTakeImageToFile() {
		  //		int width = getWidth();
		  String path = "images/img";
		  path = path + "-left" + index +".png";
		  index ++;

		  File file = new File(path); // The file to save to.

		  String format = "PNG"; // Example: "PNG" or "JPG"
		  int[] temp = getDrone().getLeftCamera().takeImage();

		  int width = getDrone().getLeftCamera().getWidth();
		  int height = temp.length/getDrone().getLeftCamera().getWidth();
		  BufferedImage image = new BufferedImage(width, height , BufferedImage.TYPE_INT_RGB);

		  for(int x = 0; x < width; x++)
		  {
		    for(int y = 0; y < height; y++)
		    {
		      image.setRGB(x, y, temp[y*width+x]);
		    }
		  }
		  try {
		    ImageIO.write(image, format, file);
		  } catch (IOException e) { e.printStackTrace(); }
	}
	int index = 0;
	
	
	private void addColorToExistingEdges(ArrayList<Point> existingPoints, CustomColor c) {
		for(int firstIndex = 0; firstIndex < existingPoints.size(); firstIndex ++) {
			Point p1 = existingPoints.get(firstIndex);
			for(int secondIndex = firstIndex + 1; secondIndex < existingPoints.size(); secondIndex ++) {
//				Point p2 = existingPoints.get(secondIndex);
				innerLoop:
				for(Edge e: datapoly.getPointsWithTheirEdges().get(p1)) {
					if(e.consistsOfPoint(existingPoints.get(secondIndex))) {
						try {
							e.addColor(c);
							break innerLoop;
						} catch (Exception e1) {
							System.out.println("Error in addColorToExistingEdges(ScanObjectNew)");
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}


}
