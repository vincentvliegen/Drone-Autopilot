package DroneAutopilot.calculations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class PolyhedraCalculations {

	public final static int white = 16777215;

	private final PhysicsCalculations physicsCalc;
	
	private HashMap<float[], ArrayList<int[]>> hashMapOuterColor;
	private HashMap<float[], ArrayList<int[]>> hashMapInnerColor;
	private HashMap<float[], ArrayList<int[]>> hashMapObstacle;
	private HashMap<float[], ArrayList<float[]>> outerCorners;
	private HashMap<float[], ArrayList<float[]>> innerCorners;

	public PolyhedraCalculations(Drone drone) {
		this.physicsCalc = new PhysicsCalculations(drone);

	}
	public HashMap<float[], ArrayList<float[]>> findAllCOGs(Camera leftCamera, Camera rightCamera) {
		HashMap<float[], ArrayList<float[]>> COGS = new HashMap<float[], ArrayList<float[]>>();
		this.SeparateTargetsAndObstacles(leftCamera);
		HashMap<float[], float[]> leftCOGS = this.findCornersOfFullTrianglesViaCOG();
		// System.out.println("foundL " + leftCOGS.keySet());

		this.SeparateTargetsAndObstacles(rightCamera);
		HashMap<float[], float[]> rightCOGS = this.findCornersOfFullTrianglesViaCOG();
		// System.out.println("foundR "+rightCOGS.keySet());

		for (float[] colorLeft : leftCOGS.keySet()) {
			for (float[] colorRight : rightCOGS.keySet()) {
				if (colorLeft[0] == colorRight[0] && colorLeft[1] == colorRight[1] && colorLeft[2] == colorRight[2]) {
					ArrayList<float[]> COG = new ArrayList<float[]>();
					COG.add(leftCOGS.get(colorLeft));
					COG.add(rightCOGS.get(colorRight));
					COGS.put(colorLeft, COG);
				}
			}
		}
		System.out.println("final " + COGS.size());
		return COGS;
	}

	public void getMatchingCorners(Camera leftCamera, Camera rightCamera) {
		this.SeparateTargetsAndObstacles(leftCamera);
		HashMap<float[], ArrayList<int[]>> outerTrianglesL = this
				.findThreePointsTriangles(this.getHashMapTargetOuterColor());
		HashMap<float[], ArrayList<int[]>> innerTrianglesL = this
				.findThreePointsTriangles(this.getHashMapTargetInnerColor());
		this.SeparateTargetsAndObstacles(rightCamera);
		HashMap<float[], ArrayList<int[]>> outerTrianglesR = this
				.findThreePointsTriangles(this.getHashMapTargetOuterColor());
		HashMap<float[], ArrayList<int[]>> innerTrianglesR = this
				.findThreePointsTriangles(this.getHashMapTargetInnerColor());

		HashMap<float[], ArrayList<float[]>> cornersOuter = this.findMatchingCorners(outerTrianglesL,
				outerTrianglesR);
		HashMap<float[], ArrayList<float[]>> cornersInner = this.findMatchingCorners(innerTrianglesL,
				innerTrianglesR);


		this.setOuterCorners(cornersOuter);
		this.setInnerCorners(cornersInner);
	}

	public HashMap<float[], ArrayList<float[]>> findMatchingCorners(HashMap<float[], ArrayList<int[]>> targetListLeft,
			HashMap<float[], ArrayList<int[]>> targetListRight) {
		HashMap<float[], ArrayList<float[]>> result = new HashMap<float[], ArrayList<float[]>>();
		for (float[] colorLeft : targetListLeft.keySet()) {
			for (float[] colorRight : targetListRight.keySet()) {
				if (colorLeft[0] == colorRight[0] && colorLeft[1] == colorRight[1] && colorLeft[2] == colorRight[2]) {
					ArrayList<float[]> corners = new ArrayList<float[]>();

					ArrayList<int[]> allCornersL = new ArrayList<int[]>();
					allCornersL.addAll(targetListLeft.get(colorLeft));
					ArrayList<int[]> allCornersR = new ArrayList<int[]>();
					allCornersL.addAll(targetListRight.get(colorRight));

					ArrayList<int[]> foundmaxyL = this.findMaxCoordsWithMargin(targetListLeft.get(colorLeft), 1);
					ArrayList<int[]> foundmaxyR = this.findMaxCoordsWithMargin(targetListRight.get(colorRight), 1);

					ArrayList<int[]> foundminyL = this.findMinCoordsWithMargin(targetListLeft.get(colorLeft), 1);
					ArrayList<int[]> foundminyR = this.findMinCoordsWithMargin(targetListRight.get(colorRight), 1);

					//bovenste hoekpunt toevoegen
					if (foundmaxyL.size() == 1 && foundmaxyR.size() == 1) {
						float[] coordinate = this.getPhysics().calculatePositionObject(
								intListToFloatList(foundmaxyL.get(0)), intListToFloatList(foundmaxyR.get(0)));
						corners.add(coordinate);
						allCornersL.remove(foundmaxyL.get(0));
						allCornersR.remove(foundmaxyR.get(0));
					}

					//onderste hoekpunt toevoegen
					if (foundminyL.size() == 1 && foundminyR.size() == 1) {
						float[] coordinate = this.getPhysics().calculatePositionObject(
								intListToFloatList(foundminyL.get(0)), intListToFloatList(foundminyR.get(0)));
						corners.add(coordinate);
						allCornersL.remove(foundminyL.get(0));
						allCornersR.remove(foundminyR.get(0));
					}
					
					//als 2 onderste of bovenste zijn het meest linkse toevoegen
					if (allCornersL.size() > 1 && allCornersR.size() > 1) {
						ArrayList<int[]> foundmaxxL = new ArrayList<int[]>();
						ArrayList<int[]> foundmaxxR = new ArrayList<int[]>();
						float[] coordinate ={};
						if(foundmaxyL.size() > 1 && foundmaxyR.size() > 1) {
							foundmaxxL = this.findMaxCoordsWithMargin(foundmaxyL, 0);
							foundmaxxR = this.findMaxCoordsWithMargin(foundmaxyR, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToFloatList(foundmaxxL.get(0)), intListToFloatList(foundmaxxR.get(0)));
							allCornersL.remove(foundmaxxL.get(0));
							allCornersR.remove(foundmaxxR.get(0));
							
						}
						else if (foundmaxyL.size() > 1) {
							foundmaxxL = this.findMaxCoordsWithMargin(foundmaxyL, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToFloatList(foundmaxxL.get(0)), intListToFloatList(foundmaxyR.get(0)));
							allCornersL.remove(foundmaxxL.get(0));
							allCornersR.remove(foundmaxyR.get(0));
						}
						else if (foundmaxyR.size() > 1) {
							foundmaxxR = this.findMaxCoordsWithMargin(foundmaxyR, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToFloatList(foundmaxyL.get(0)), intListToFloatList(foundmaxxR.get(0)));
							allCornersL.remove(foundmaxyL.get(0));
							allCornersR.remove(foundmaxxR.get(0));
						}
						
						else {
							ArrayList<int[]> foundminxL = new ArrayList<int[]>();
							ArrayList<int[]> foundminxR = new ArrayList<int[]>();
							if(foundminyL.size() > 1 && foundminyR.size() > 1) {
								foundminxL = this.findMinCoordsWithMargin(foundminyL, 0);
								foundminxR = this.findMinCoordsWithMargin(foundminyR, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToFloatList(foundminxL.get(0)), intListToFloatList(foundminxR.get(0)));
								allCornersL.remove(foundminxL.get(0));
								allCornersR.remove(foundminxR.get(0));
								
							}
							else if (foundminyL.size() > 1) {
								foundmaxxL = this.findMinCoordsWithMargin(foundminyL, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToFloatList(foundminxL.get(0)), intListToFloatList(foundminyR.get(0)));
								allCornersL.remove(foundminxL.get(0));
								allCornersR.remove(foundminyR.get(0));
							}
							else if (foundminyR.size() > 1) {
								foundminxR = this.findMinCoordsWithMargin(foundminyR, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToFloatList(foundminyL.get(0)), intListToFloatList(foundminxR.get(0)));
								allCornersL.remove(foundminyL.get(0));
								allCornersR.remove(foundminxR.get(0));
							}
						}
						corners.add(coordinate);
					
					}
					
					//middelste hoekpunt toevoegen
					float[] coordinate = this.getPhysics().calculatePositionObject(
							intListToFloatList(allCornersL.get(0)), intListToFloatList(allCornersR.get(0)));
					corners.add(coordinate);

					result.put(colorLeft, corners);
				}
			}
		}
		return result;
	}

	private float[] intListToFloatList(int[] a) {
		return new float[] { a[0], a[1] };
	}

	// [i] -> (x,y)
	private int[] indexToCoordinates(int index, Camera camera) {
		int width = camera.getWidth();
		int x = (int) (index % width);
		int y = (int) (index / width);
		int[] coord = { x, y };
		return coord;
	}

	public HashMap<Integer, ArrayList<int[]>> calculatePixelsOfEachColor(Camera camera) {
		int[] image = camera.takeImage();
		HashMap<Integer, ArrayList<int[]>> hashMapDifferentColors = new HashMap<Integer, ArrayList<int[]>>();
		for (int i = 0; i < image.length; i++) {
			if (image[i] != white) {
				if (hashMapDifferentColors.containsKey(image[i])) {
					ArrayList<int[]> hashMapTemp = hashMapDifferentColors.get(image[i]);
					hashMapDifferentColors.remove(image[i], hashMapTemp);
					hashMapTemp.add(indexToCoordinates(i, camera));
					hashMapDifferentColors.put(image[i], hashMapTemp);
				} else {
					ArrayList<int[]> coloredPositions = new ArrayList<int[]>();
					coloredPositions.add(indexToCoordinates(i, camera));
					hashMapDifferentColors.put(image[i], coloredPositions);
				}
			}
		}
		// System.out.println("calculate pixels of each color: " +
		// hashMapDifferentColors.keySet().size());
		return hashMapDifferentColors;
	}

	public float[] colorIntToHSV(int color) {
		// to RGB
		int B = color % 256;
		int G = (color / 256) % 256;
		int R = color / (256 * 256);

		// to HSV
		float[] HSV1 = { 0, 0, 0 };
		float[] HSV = Color.RGBtoHSB(R, G, B, HSV1);
		return HSV;
	}

	private void SeparateTargetsAndObstacles(Camera camera) throws IllegalArgumentException {
		HashMap<Integer, ArrayList<int[]>> hashMapDifferentColors = this.calculatePixelsOfEachColor(camera);
		Set<Integer> colorIntValuesSet = hashMapDifferentColors.keySet();
		ArrayList<Integer> colorIntValuesArray = new ArrayList<Integer>();
		colorIntValuesArray.addAll(colorIntValuesSet);

		HashMap<float[], ArrayList<int[]>> hashMapOuterColor = new HashMap<float[], ArrayList<int[]>>();
		HashMap<float[], ArrayList<int[]>> hashMapObstacle = new HashMap<float[], ArrayList<int[]>>();
		HashMap<float[], ArrayList<int[]>> hashMapInnerColor = new HashMap<float[], ArrayList<int[]>>();

		// S>0.55: outertriangle, S<0.45: innertriangle
		// V>0.55: target , V<0.45: obstacle
		// anders Error
		for (int i = 0; i < colorIntValuesArray.size(); i++) {
			float[] HSV = this.colorIntToHSV(colorIntValuesArray.get(i));
			if (HSV[2] < 0.45) { // obstacle
				if (HSV[1] > 0.55) { // outertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(colorIntValuesArray.get(i));
					hashMapObstacle.put(HSV, coordinateList);
				}
			} else if (HSV[2] > 0.55) { // target
				if (HSV[1] < 0.45) { // innertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(colorIntValuesArray.get(i));
					hashMapInnerColor.put(HSV, coordinateList);
				} else if (HSV[1] > 0.55) { // outertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(colorIntValuesArray.get(i));
					hashMapOuterColor.put(HSV, coordinateList);
				} else {
					throw new IllegalArgumentException();
				}
			} else {
				throw new IllegalArgumentException();
			}
		}
		// System.out.println("outerTargetColors =" + hashMapOuterColor.size());
		// System.out.println("innerTargetColors =" + hashMapInnerColor.size());
		// System.out.println("obstaclesTargetColors =" +
		// hashMapObstacle.size());
		this.setHashMapTargetOuterColor(hashMapOuterColor);
		this.setHashMapTargetInnerColor(hashMapInnerColor);
		this.setHashMapObstacleOuterColor(hashMapObstacle);
	}

	private ArrayList<int[]> findMinimumCoordinates(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> minEqualCoord = new ArrayList<int[]>();
		int infinity = (int) Double.POSITIVE_INFINITY;
		int minimum = infinity;
		for (int[] coordinate : coordinates) {
			if (coordinate[choice] < minimum) {
				minimum = coordinate[choice];
				minEqualCoord.clear();
				minEqualCoord.add(coordinate);
			} else if (coordinate[choice] == minimum) {
				minEqualCoord.add(coordinate);
			}
		}
		return minEqualCoord;
	}

	private ArrayList<int[]> findMaximumCoordinates(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> maxEqualCoord = new ArrayList<int[]>();
		int maximum = 0;
		for (int[] coordinate : coordinates) {
			if (coordinate[choice] > maximum) {
				maximum = coordinate[choice];
				maxEqualCoord.clear();
				maxEqualCoord.add(coordinate);
			} else if (coordinate[choice] == maximum) {
				maxEqualCoord.add(coordinate);
			}
		}
		return maxEqualCoord;
	}

	public ArrayList<int[]> findMaxCoordsWithMargin(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> maxEqualCoord = new ArrayList<int[]>();
		int[] high1 = {0,0};
		int[] high2 = {0,0};
		
		for (int[] coordinate:coordinates) {
			if (coordinate[choice] > high1[choice]) {
				high2 = high1;
				high1 = coordinate;
			} else if (coordinate[choice] > high2[choice]) {
				high2 = coordinate;
			}
		}
		maxEqualCoord.add(high2);
		if (high1[choice] - high2[choice] < 3) {
			maxEqualCoord.add(high2);
		} 
		return maxEqualCoord;
	}
	
	public ArrayList<int[]> findMinCoordsWithMargin(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> minEqualCoord = new ArrayList<int[]>();
		int[] low1 = {(int) Double.MAX_VALUE,(int) Double.MAX_VALUE};
		int[] low2 = {(int) Double.MAX_VALUE,(int) Double.MAX_VALUE};
		
		for (int[] coordinate:coordinates) {
			if (coordinate[choice] < low1[choice]) {
				low2 = low1;
				low1 = coordinate;
			} else if (coordinate[choice] < low2[choice]) {
				low2 = coordinate;
			}
		}
		minEqualCoord.add(low2);
		if (low2[choice] - low1[choice] < 3) {
			minEqualCoord.add(low2);
		} 
		return minEqualCoord;
	}

	private HashMap<float[], ArrayList<int[]>> findThreePointsTriangles(
			HashMap<float[], ArrayList<int[]>> hashMapTarget) {
		HashMap<float[], ArrayList<int[]>> solution = new HashMap<float[], ArrayList<int[]>>();
		// gevonden HSV kleuren in arraylist opslaan
		Set<float[]> HSVcolorSet = hashMapTarget.keySet();
		ArrayList<float[]> HSVcolorArray = new ArrayList<float[]>(HSVcolorSet);

		// loopen over alle kleuren en daarvan de hoekpunten bepalen.
		for (int i = 0; i < HSVcolorArray.size(); i++) {
			ArrayList<int[]> coordinates = hashMapTarget.get(HSVcolorArray.get(i));
			ArrayList<int[]> possibleCorners = new ArrayList<int[]>();
			// linker boven
			ArrayList<int[]> links = this.findMinimumCoordinates(coordinates, 0);
			ArrayList<int[]> linksBoven = this.findMinimumCoordinates(links, 1);
			// System.out.println("linksboven" + linksBoven.get(0)[0] + " " +
			// linksBoven.get(0)[1]);
			possibleCorners.add(linksBoven.get(0));
			// linker onder
			ArrayList<int[]> links2 = this.findMinimumCoordinates(coordinates, 0);
			ArrayList<int[]> linksOnder = this.findMaximumCoordinates(links2, 1);
			// System.out.println("linksonder" + linksOnder.get(0)[0] + " " +
			// linksOnder.get(0)[1]);
			possibleCorners.add(linksOnder.get(0));
			// rechts boven
			ArrayList<int[]> rechts = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsBoven = this.findMinimumCoordinates(rechts, 1);
			// System.out.println("rechtsboven" + rechtsBoven.get(0)[0] + " " +
			// rechtsBoven.get(0)[1]);
			possibleCorners.add(rechtsBoven.get(0));
			// rechts onder
			ArrayList<int[]> rechts2 = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsOnder = this.findMaximumCoordinates(rechts2, 1);
			// System.out.println("rechtsonder" + rechtsOnder.get(0)[0] + " " +
			// rechtsOnder.get(0)[1]);
			possibleCorners.add(rechtsOnder.get(0));
			// boven links
			ArrayList<int[]> boven = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenLinks = this.findMinimumCoordinates(boven, 0);
			// System.out.println("bovenLinks" + bovenLinks.get(0)[0] + " " +
			// bovenLinks.get(0)[1]);
			possibleCorners.add(bovenLinks.get(0));
			// boven rechts
			ArrayList<int[]> boven2 = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenRechts = this.findMaximumCoordinates(boven2, 0);
			// System.out.println("bovenRechts" + bovenRechts.get(0)[0] + " " +
			// bovenRechts.get(0)[1]);
			possibleCorners.add(bovenRechts.get(0));
			// onder links
			ArrayList<int[]> onder = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderLinks = this.findMinimumCoordinates(onder, 0);
			// System.out.println("onderLinks" + onderLinks.get(0)[0] + " " +
			// onderLinks.get(0)[1]);
			possibleCorners.add(onderLinks.get(0));
			// onder rechts
			ArrayList<int[]> onder2 = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderRechts = this.findMaximumCoordinates(onder2, 0);
			// System.out.println("onderRechts" + onderRechts.get(0)[0] + " " +
			// onderRechts.get(0)[1]);
			possibleCorners.add(onderRechts.get(0));

			boolean boolLinks = false;
			boolean boolRechts = false;
			boolean boolOnder = false;
			boolean boolBoven = false;

			// verwijder overlappende hoekpunten
			if (this.equalPixelCorner(linksOnder, linksBoven)) {
				possibleCorners.remove(linksOnder.get(0));
				boolLinks = true;
			}
			if (this.equalPixelCorner(rechtsOnder, rechtsBoven)) {
				possibleCorners.remove(rechtsBoven.get(0));
				boolRechts = true;
			}
			if (this.equalPixelCorner(bovenRechts, bovenLinks)) {
				possibleCorners.remove(bovenLinks.get(0));
				boolBoven = true;
			}
			if (this.equalPixelCorner(onderLinks, onderRechts)) {
				possibleCorners.remove(onderRechts.get(0));
				boolOnder = true;
			}

			if (this.equalPixelCorner(linksBoven, bovenLinks)) {
				if (boolBoven)
					possibleCorners.remove(bovenRechts.get(0));
				else
					possibleCorners.remove(bovenLinks.get(0));
			}
			if (this.equalPixelCorner(bovenRechts, rechtsBoven)) {
				if (boolRechts)
					possibleCorners.remove(rechtsOnder.get(0));
				else
					possibleCorners.remove(rechtsBoven.get(0));
			}
			if (this.equalPixelCorner(rechtsOnder, onderRechts)) {
				if (boolOnder)
					possibleCorners.remove(onderLinks.get(0));
				else
					possibleCorners.remove(onderRechts.get(0));
			}
			if (this.equalPixelCorner(linksOnder, onderLinks)) {
				if (boolLinks)
					possibleCorners.remove(linksBoven.get(0));
				else
					possibleCorners.remove(linksOnder.get(0));
			}

			// System.out.println("corners"+possibleCorners.size());

			if (possibleCorners.size() == 3) {
				solution.put(HSVcolorArray.get(i), possibleCorners);
			}
		}
		// System.out.println(solution.size() +"solution");
		return solution;
	}

	private boolean equalPixelCorner(ArrayList<int[]> list1, ArrayList<int[]> list2) {
		if (list1.get(0)[0] <= list2.get(0)[0] + 3 && list1.get(0)[0] >= list2.get(0)[0] - 3
				&& list1.get(0)[1] <= list2.get(0)[1] + 3 && list1.get(0)[1] >= list2.get(0)[1] - 3) {
			return true;
		}
		return false;
	}

	private HashMap<float[], float[]> findCOGOfAllColors(HashMap<float[], ArrayList<int[]>> hashMapCorners) {
		HashMap<float[], float[]> COGHashMap = new HashMap<float[], float[]>();
		Set<float[]> colors = hashMapCorners.keySet();
		for (float[] color : colors) {
			ArrayList<int[]> corners = hashMapCorners.get(color);
			// bereken zp
			float x = (corners.get(0)[0] + corners.get(1)[0] + corners.get(2)[0]) / 3;
			float y = (corners.get(0)[1] + corners.get(1)[1] + corners.get(2)[1]) / 3;
			// System.out.println("cog" + x + " " + y);
			float[] COG = { x, y };
			COGHashMap.put(color, COG);
		}
		return COGHashMap;
	}

	private HashMap<float[], float[]> findCornersOfFullTrianglesViaCOG() {
		HashMap<float[], ArrayList<int[]>> outerTriangles = this
				.findThreePointsTriangles(this.getHashMapTargetOuterColor());
		HashMap<float[], ArrayList<int[]>> innerTriangles = this
				.findThreePointsTriangles(this.getHashMapTargetInnerColor());

		HashMap<float[], float[]> outerTrianglesCOG = this.findCOGOfAllColors(outerTriangles);
		Set<float[]> setOuterCOG = outerTrianglesCOG.keySet();
		HashMap<float[], float[]> innerTrianglesCOG = this.findCOGOfAllColors(innerTriangles);
		Set<float[]> setInnerCOG = innerTrianglesCOG.keySet();

		HashMap<float[], float[]> resultingOuterTriangleCOGs = new HashMap<float[], float[]>();

		for (float[] outercolor : setOuterCOG) {
			for (float[] innercolor : setInnerCOG) {
				float[] outerCOG = outerTrianglesCOG.get(outercolor);
				float[] innerCOG = innerTrianglesCOG.get(innercolor);
				// afwijking zwaaartepunten hier aanpassen
				if (outerCOG[0] <= innerCOG[0] + 3 && outerCOG[0] >= innerCOG[0] - 3 && outerCOG[1] <= innerCOG[1] + 3
						&& outerCOG[1] >= innerCOG[1] - 3) {
					// buiten en binnen COG liggen max 3 pixel van elkaar
					resultingOuterTriangleCOGs.put(outercolor, outerCOG);
				}
			}
		}
		return resultingOuterTriangleCOGs;
	}

	public HashMap<float[], ArrayList<float[]>> getInnerCorners() {
		return this.innerCorners;
	}

	private void setInnerCorners(HashMap<float[], ArrayList<float[]>> innercorner) {
		this.innerCorners = innercorner;
	}

	public HashMap<float[], ArrayList<float[]>> getOuterCorners() {
		return this.outerCorners;
	}

	private void setOuterCorners(HashMap<float[], ArrayList<float[]>> outercorner) {
		this.outerCorners = outercorner;
	}

	public HashMap<float[], ArrayList<int[]>> getHashMapTargetOuterColor() {
		return this.hashMapOuterColor;
	}

	private void setHashMapTargetOuterColor(HashMap<float[], ArrayList<int[]>> hashMapOuterColor) {
		this.hashMapOuterColor = hashMapOuterColor;
	}

	public HashMap<float[], ArrayList<int[]>> getHashMapTargetInnerColor() {
		return this.hashMapInnerColor;
	}

	private void setHashMapTargetInnerColor(HashMap<float[], ArrayList<int[]>> hashMapInnerColor) {
		this.hashMapInnerColor = hashMapInnerColor;
	}

	public HashMap<float[], ArrayList<int[]>> getHashMapObstacleOuterColor() {
		return this.hashMapObstacle;
	}

	private void setHashMapObstacleOuterColor(HashMap<float[], ArrayList<int[]>> hashMapObstacle) {
		this.hashMapObstacle = hashMapObstacle;
	}

	private PhysicsCalculations getPhysics() {
		return this.physicsCalc;
	}
}
