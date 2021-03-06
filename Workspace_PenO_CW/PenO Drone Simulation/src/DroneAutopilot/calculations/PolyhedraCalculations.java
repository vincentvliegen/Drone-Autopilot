package DroneAutopilot.calculations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import DroneAutopilot.DroneAutopilot;
import exceptions.EmptyPositionListException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class PolyhedraCalculations {

	public final static int white = 16777215;

	private final PhysicsCalculations physicsCalc;

	private HashMap<float[], ArrayList<int[]>> hashMapOuterColor;
	private HashMap<float[], ArrayList<int[]>> hashMapInnerColor;
	private HashMap<float[], ArrayList<int[]>> hashMapObstacle;

	public PolyhedraCalculations(DroneAutopilot droneAutopilot) {
		this.physicsCalc = droneAutopilot.getPhysicsCalculations();

	}

	//functie om de zwaartepunten van alle driehoeken terug te geven
	public HashMap<float[],double[]> newCOGmethod(Camera leftCamera, Camera rightCamera){
		HashMap<float[],double[]> COGS = new HashMap<float[],double[]>();
		this.SeparateTargetsAndObstacles(leftCamera);
		HashMap<float[], double[]> outerL = this.getCOGofEverythingVisible(this.getHashMapTargetOuterColor());

		this.SeparateTargetsAndObstacles(rightCamera);
		HashMap<float[], double[]> outerR = this.getCOGofEverythingVisible(this.getHashMapTargetOuterColor());

		for (float[] colorLeft : outerL.keySet()) {
			for (float[] colorRight : outerR.keySet()) {
				if (colorLeft[0] == colorRight[0] && colorLeft[1] == colorRight[1] && colorLeft[2] == colorRight[2]) {
					double[] coordinate = this.getPhysics().calculatePositionObject(outerL.get(colorLeft), outerR.get(colorRight));
					COGS.put(colorLeft, coordinate);
				}
			}
		}
		return COGS;
	}

	private HashMap<float[],double[]> getCOGofEverythingVisible(HashMap<float[], ArrayList<int[]>> allCoordinates){
		HashMap<float[],double[]> result = new HashMap<float[],double[]>();
		for(float[] color: allCoordinates.keySet()){
			ArrayList<int[]> coord = allCoordinates.get(color);
			if(coord.size()>1){
				double sumX = 0;
				double sumY = 0;
				double[] COG = { 0, 0 };

				for (int i = 0; i < coord.size(); i++) {
					sumX += coord.get(i)[0];
					sumY += coord.get(i)[1];
				}
				COG[0] = sumX / coord.size();
				COG[1] = sumY / coord.size();		
				result.put(color, COG);
			}
		}
		return result;
	}

	//functie om de hoekpunten te krijgen per kleur
	public HashMap<ArrayList<float[]>, ArrayList<double[]>> getMatchingCorners(Camera leftCamera, Camera rightCamera) {
		this.SeparateTargetsAndObstacles(leftCamera);
//		System.out.println("outerpix");
		HashMap<float[], ArrayList<int[]>> outerTrianglesL = this
				.findThreePointsTriangles(this.getHashMapTargetOuterColor());
//		System.out.println("innerpix");
		HashMap<float[], ArrayList<int[]>> innerTrianglesL = this
				.findThreePointsTriangles(this.getHashMapTargetInnerColor());
		this.SeparateTargetsAndObstacles(rightCamera);
//		System.out.println("outerpixR");
		HashMap<float[], ArrayList<int[]>> outerTrianglesR = this
				.findThreePointsTriangles(this.getHashMapTargetOuterColor());
//		System.out.println("innerpixR");
		HashMap<float[], ArrayList<int[]>> innerTrianglesR = this
				.findThreePointsTriangles(this.getHashMapTargetInnerColor());

//		System.out.println("Outer");
		HashMap<float[], ArrayList<double[]>> cornersOuter = this.findMatchingCorners(outerTrianglesL, outerTrianglesR);
//		System.out.println("Innerdriehoekpunten");
		HashMap<float[], ArrayList<double[]>> cornersInner = this.findMatchingCorners(innerTrianglesL, innerTrianglesR);
		//check binnen zichtbare marge
		HashMap<float[], float[]> combinedColors = this.matchInnerAndOuterColor(cornersOuter, cornersInner);

		HashMap<ArrayList<float[]>, ArrayList<double[]>> result = new HashMap<ArrayList<float[]>, ArrayList<double[]>>();
		for (float[] color : cornersOuter.keySet()) {
			float[] inner = combinedColors.get(color);
			if (inner != null) {
				ArrayList<float[]> augmentedColor = new ArrayList<float[]>();
				augmentedColor.add(color);
				augmentedColor.add(inner);
				result.put(augmentedColor, cornersOuter.get(color));
			}
		}
		return result;
	}

	public HashMap<float[], ArrayList<int[]>> findThreePointsTriangles(
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
			possibleCorners.add(linksBoven.get(0));
			// linker onder
			ArrayList<int[]> links2 = this.findMinimumCoordinates(coordinates, 0);
			ArrayList<int[]> linksOnder = this.findMaximumCoordinates(links2, 1);
			possibleCorners.add(linksOnder.get(0));
			// rechts boven
			ArrayList<int[]> rechts = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsBoven = this.findMinimumCoordinates(rechts, 1);
			possibleCorners.add(rechtsBoven.get(0));
			// rechts onder
			ArrayList<int[]> rechts2 = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsOnder = this.findMaximumCoordinates(rechts2, 1);
			possibleCorners.add(rechtsOnder.get(0));
			// boven links
			ArrayList<int[]> boven = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenLinks = this.findMinimumCoordinates(boven, 0);
			possibleCorners.add(bovenLinks.get(0));
			// boven rechts
			ArrayList<int[]> boven2 = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenRechts = this.findMaximumCoordinates(boven2, 0);
			possibleCorners.add(bovenRechts.get(0));
			// onder links
			ArrayList<int[]> onder = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderLinks = this.findMinimumCoordinates(onder, 0);
			possibleCorners.add(onderLinks.get(0));
			// onder rechts
			ArrayList<int[]> onder2 = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderRechts = this.findMaximumCoordinates(onder2, 0);
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

			if(possibleCorners.size() >3){
				this.antiTrapjes(possibleCorners);
			}
			
			if (possibleCorners.size() == 3) {
//				System.out.println("------");
//				for(int l=0;l<possibleCorners.size();l++){
//					System.out.println(Arrays.toString(possibleCorners.get(l)));
//				}
				solution.put(HSVcolorArray.get(i), possibleCorners);
			}
		}
		// System.out.println(solution.size() +"solution");
		return solution;
	}

	private HashMap<float[], ArrayList<double[]>> findMatchingCorners(HashMap<float[], ArrayList<int[]>> targetListLeft,
			HashMap<float[], ArrayList<int[]>> targetListRight) {
		HashMap<float[], ArrayList<double[]>> result = new HashMap<float[], ArrayList<double[]>>();
		for (float[] colorLeft : targetListLeft.keySet()) {
			for (float[] colorRight : targetListRight.keySet()) {
				if (colorLeft[0] == colorRight[0] && colorLeft[1] == colorRight[1] && colorLeft[2] == colorRight[2]) {
//					System.out.println("************************");
					ArrayList<double[]> corners = new ArrayList<double[]>();

					ArrayList<int[]> allCornersL = new ArrayList<int[]>();
					allCornersL.addAll(targetListLeft.get(colorLeft));
					ArrayList<int[]> allCornersR = new ArrayList<int[]>();
					allCornersR.addAll(targetListRight.get(colorRight));

					ArrayList<int[]> foundmaxyL = this.findMaxCoordsWithMargin(targetListLeft.get(colorLeft), 1);
					ArrayList<int[]> foundmaxyR = this.findMaxCoordsWithMargin(targetListRight.get(colorRight), 1);
					ArrayList<int[]> foundminyL = this.findMinCoordsWithMargin(targetListLeft.get(colorLeft), 1);
					ArrayList<int[]> foundminyR = this.findMinCoordsWithMargin(targetListRight.get(colorRight), 1);

					// onderste hoekpunt toevoegen
					if (foundmaxyL.size() == 1 && foundmaxyR.size() == 1) {
						double[] coordinate = this.getPhysics().calculatePositionObject(
								intListToDoubleList(foundmaxyL.get(0)), intListToDoubleList(foundmaxyR.get(0)));
//						System.out.println(Arrays.toString(coordinate));
						corners.add(coordinate);
						//						System.out.println("coordinaat" + coordinate[0] + " " + coordinate[1] + " " + coordinate[2]);
						allCornersL.remove(foundmaxyL.get(0));
						allCornersR.remove(foundmaxyR.get(0));
					}

					// bovenste hoekpunt toevoegen
					if (foundminyL.size() == 1 && foundminyR.size() == 1) {
						double[] coordinate = this.getPhysics().calculatePositionObject(
								intListToDoubleList(foundminyL.get(0)), intListToDoubleList(foundminyR.get(0)));
//						System.out.println(Arrays.toString(coordinate));
						corners.add(coordinate);
						//						System.out.println("coordinaat" + coordinate[0] + " " + coordinate[1] + " " + coordinate[2]);
						allCornersL.remove(foundminyL.get(0));
						allCornersR.remove(foundminyR.get(0));
					}

					// als 2 onderste of bovenste zijn het meest linkse
					// toevoegen
					if (allCornersL.size() > 1 && allCornersR.size() > 1) {
						ArrayList<int[]> foundmaxxL = new ArrayList<int[]>();
						ArrayList<int[]> foundmaxxR = new ArrayList<int[]>();
						double[] coordinate = {};
						if (foundmaxyL.size() > 1 && foundmaxyR.size() > 1) {
							foundmaxxL = this.findMaxCoordsWithMargin(foundmaxyL, 0);
							foundmaxxR = this.findMaxCoordsWithMargin(foundmaxyR, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToDoubleList(foundmaxxL.get(0)), intListToDoubleList(foundmaxxR.get(0)));
							allCornersL.remove(foundmaxxL.get(0));
							allCornersR.remove(foundmaxxR.get(0));

						} else if (foundmaxyL.size() > 1) {
							foundmaxxL = this.findMaxCoordsWithMargin(foundmaxyL, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToDoubleList(foundmaxxL.get(0)), intListToDoubleList(foundmaxyR.get(0)));
							allCornersL.remove(foundmaxxL.get(0));
							allCornersR.remove(foundmaxyR.get(0));
						} else if (foundmaxyR.size() > 1) {
							foundmaxxR = this.findMaxCoordsWithMargin(foundmaxyR, 0);
							coordinate = this.getPhysics().calculatePositionObject(
									intListToDoubleList(foundmaxyL.get(0)), intListToDoubleList(foundmaxxR.get(0)));
							allCornersL.remove(foundmaxyL.get(0));
							allCornersR.remove(foundmaxxR.get(0));
						}

						else {
							ArrayList<int[]> foundminxL = new ArrayList<int[]>();
							ArrayList<int[]> foundminxR = new ArrayList<int[]>();
							if (foundminyL.size() > 1 && foundminyR.size() > 1) {
								foundminxL = this.findMinCoordsWithMargin(foundminyL, 0);
								foundminxR = this.findMinCoordsWithMargin(foundminyR, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToDoubleList(foundminxL.get(0)), intListToDoubleList(foundminxR.get(0)));
								allCornersL.remove(foundminxL.get(0));
								allCornersR.remove(foundminxR.get(0));

							} else if (foundminyL.size() > 1) {
								foundminxL = this.findMinCoordsWithMargin(foundminyL, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToDoubleList(foundminxL.get(0)), intListToDoubleList(foundminyR.get(0)));
								allCornersL.remove(foundminxL.get(0));
								allCornersR.remove(foundminyR.get(0));
							} else if (foundminyR.size() > 1) {
								foundminxR = this.findMinCoordsWithMargin(foundminyR, 0);
								coordinate = this.getPhysics().calculatePositionObject(
										intListToDoubleList(foundminyL.get(0)), intListToDoubleList(foundminxR.get(0)));
								allCornersL.remove(foundminyL.get(0));
								allCornersR.remove(foundminxR.get(0));
							}
						}
//						System.out.println(Arrays.toString(coordinate));
						corners.add(coordinate);
						//System.out.println("coordinaat" + coordinate[0] + " " + coordinate[1] + " " + coordinate[2]);

					}

					// middelste hoekpunt toevoegen
					double[] coordinate = this.getPhysics().calculatePositionObject(
							intListToDoubleList(allCornersL.get(0)), intListToDoubleList(allCornersR.get(0)));
//					System.out.println(Arrays.toString(coordinate));
					corners.add(coordinate);
					if(corners.size()==3){
						double[] COG = this.calculateCOG(corners.get(0), corners.get(1), corners.get(2));
						double afstand = Math.sqrt(Math.pow(this.getPhysics().getPosition()[0]-COG[0], 2)+
								Math.pow(this.getPhysics().getPosition()[1]-COG[1], 2)+
								Math.pow(this.getPhysics().getPosition()[2]-COG[2], 2));
//											System.out.println(afstand);
						if((afstand<1.4 && afstand>0.8) || (colorLeft[2]<0.45)){
							//System.out.println("coordinaat" + coordinate[0] + " " + coordinate[1] + " " + coordinate[2]);
							result.put(colorLeft, corners);
						}
					}
				}
			}
		}
		return result;
	}

	private HashMap<float[], float[]> matchInnerAndOuterColor(HashMap<float[], ArrayList<double[]>> cornersOuter,
			HashMap<float[], ArrayList<double[]>> cornersInner) {
		HashMap<float[], float[]> result = new HashMap<float[], float[]>();
		for (float[] outerColor : cornersOuter.keySet()) {
			ArrayList<double[]> Ocorners = cornersOuter.get(outerColor);
			if (Ocorners.size() == 3) {
				double xO = (Ocorners.get(0)[0] + Ocorners.get(1)[0] + Ocorners.get(2)[0]) / 3;
				double yO = (Ocorners.get(0)[1] + Ocorners.get(1)[1] + Ocorners.get(2)[1]) / 3;
				double zO = (Ocorners.get(0)[2] + Ocorners.get(1)[2] + Ocorners.get(2)[2]) / 3;
				for (float[] innerColor : cornersInner.keySet()) {
					ArrayList<double[]> Icorners = cornersInner.get(innerColor);
					if (Icorners.size() == 3) {
						double xI = (Icorners.get(0)[0] + Icorners.get(1)[0] + Icorners.get(2)[0]) / 3;
						double yI = (Icorners.get(0)[1] + Icorners.get(1)[1] + Icorners.get(2)[1]) / 3;
						double zI = (Icorners.get(0)[2] + Icorners.get(1)[2] + Icorners.get(2)[2]) / 3;

						if (xO >= xI - 0.1 && xO <= xI + 0.1 && yO >= yI - 0.1 && yO <= yI + 0.1 && zO >= zI - 0.1
								&& zO <= zI + 0.1 && outerColor[2] == innerColor[2]) {
							//System.out.println(xO + " " + yO + " " + zO + " " +xI+" "+yI+" "+zI );
							//System.out.println(outerColor[0] + " " + innerColor[0]);
							result.put(outerColor, innerColor);
							break;
						}
					}
				}
			}
		}
		return result;
	}

	//functie om de obstakels te herkennen.
	public HashMap<float[], ArrayList<double[]>> getObstacleCorners(Camera leftCamera, Camera rightCamera){
		this.SeparateTargetsAndObstacles(leftCamera);
		HashMap<float[], ArrayList<int[]>> outerTrianglesL = this
				.findThreePointsTriangles(this.getHashMapObstacleOuterColor());
		this.SeparateTargetsAndObstacles(rightCamera);
		HashMap<float[], ArrayList<int[]>> outerTrianglesR = this
				.findThreePointsTriangles(this.getHashMapObstacleOuterColor());
		HashMap<float[], ArrayList<double[]>> cornersOuter = this.findMatchingCorners(outerTrianglesL, outerTrianglesR);
		return cornersOuter;
	}

	//beeldanalyse
	private HashMap<Integer, ArrayList<int[]>> calculatePixelsOfEachColor(Camera camera) {
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
		return hashMapDifferentColors;
	}

	public void SeparateTargetsAndObstacles(Camera camera) throws IllegalArgumentException {
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
					throw new IllegalArgumentException("Foute Sat " + HSV[1]);
				}
			} else {
				throw new IllegalArgumentException("Foute Value " + HSV[2]);
			}
		}
		this.setHashMapTargetOuterColor(hashMapOuterColor);
		this.setHashMapTargetInnerColor(hashMapInnerColor);
		this.setHashMapObstacleOuterColor(hashMapObstacle);
	}



	//-------------------HELPFUNCTIES-----------------------//

	private int[] indexToCoordinates(int index, Camera camera) {
		int width = camera.getWidth();
		int x = (int) (index % width);
		int y = (int) (index / width);
		int[] coord = { x, y };
		return coord;
	}

	public float[] colorIntToHSV(int color) {
		// to RGB
		int B = color & 0xFF;
		int G = (color >> 8) & 0xFF;
		int R = (color >> 16) & 0xFF;

		// to HSV
		float[] HSV1 = { 0, 0, 0 };
		float[] HSV = Color.RGBtoHSB(R, G, B, HSV1);
		return HSV;
	}

	private void antiTrapjes(ArrayList<int[]> possibleCorners){
		for(int i=0;i<possibleCorners.size();i++){
			if(possibleCorners.size()==3){
				break;
			}
			ArrayList<int[]> sameY = new ArrayList<int[]>();
			sameY.add(possibleCorners.get(i));
			ArrayList<int[]> sameX = new ArrayList<int[]>();
			sameX.add(possibleCorners.get(i));
			for(int j=i+1; j<possibleCorners.size();j++){
				if(possibleCorners.get(i)[0]+1 >= possibleCorners.get(j)[0] && possibleCorners.get(i)[0]-1 <= possibleCorners.get(j)[0]){
					sameX.add(possibleCorners.get(j));
				}
				if(possibleCorners.get(i)[1]+1 >= possibleCorners.get(j)[1] && possibleCorners.get(i)[1]-1 <= possibleCorners.get(j)[1]){
					sameY.add(possibleCorners.get(j));
				}
			}
			if(sameX.size()>2){
				this.sortCorners(sameX, 1);
				for(i=1;i<sameX.size()-1;i++){
					possibleCorners.remove(sameX.get(i));
				}
			}else if(sameY.size()>2){
				this.sortCorners(sameY, 0);
				for(i=1;i<sameY.size()-1;i++){
					possibleCorners.remove(sameY.get(i));
				}
			}
		}
	}
	
	private void sortCorners(ArrayList<int[]> toSort, int choice){
		Collections.sort(toSort, new Comparator<int[]>(){

			@Override
			public int compare(int[] o1, int[] o2) {
				// TODO Auto-generated method stub
				if(o1[choice]<o2[choice])
					return -1;
				else if(o1[choice] == o2[choice])
					return 0;
				else
					return 1;
			}
		});
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

	private ArrayList<int[]> findMaxCoordsWithMargin(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> maxEqualCoord = new ArrayList<int[]>();
		int[] high1 = { 0, 0 };
		int[] high2 = { 0, 0 };

		for (int[] coordinate : coordinates) {
			if (coordinate[choice] > high1[choice]) {
				high2 = high1;
				high1 = coordinate;
			} else if (coordinate[choice] > high2[choice]) {
				high2 = coordinate;
			}
		}
		maxEqualCoord.add(high1);
		if (high1[choice] - high2[choice] < 3) {
			maxEqualCoord.add(high2);
		}
		return maxEqualCoord;
	}

	private ArrayList<int[]> findMinCoordsWithMargin(ArrayList<int[]> coordinates, int choice) {
		ArrayList<int[]> minEqualCoord = new ArrayList<int[]>();
		int[] low1 = { (int) Double.MAX_VALUE, (int) Double.MAX_VALUE };
		int[] low2 = { (int) Double.MAX_VALUE, (int) Double.MAX_VALUE };

		for (int[] coordinate : coordinates) {
			if (coordinate[choice] < low1[choice]) {
				low2 = low1;
				low1 = coordinate;
			} else if (coordinate[choice] < low2[choice]) {
				low2 = coordinate;
			}
		}
		minEqualCoord.add(low1);
		if (low2[choice] - low1[choice] < 3) {
			minEqualCoord.add(low2);
		}
		return minEqualCoord;
	}

	private boolean equalPixelCorner(ArrayList<int[]> list1, ArrayList<int[]> list2) {
		if (list1.get(0)[0] <= list2.get(0)[0] + 3 && list1.get(0)[0] >= list2.get(0)[0] - 3
				&& list1.get(0)[1] <= list2.get(0)[1] + 3 && list1.get(0)[1] >= list2.get(0)[1] - 3) {
			return true;
		}
		return false;
	}

	private double[] calculateCOG(double[] c1, double[] c2, double[] c3){
		double[] COG = new double[]{0,0,0};
		COG[0] = (c1[0] + c2[0] + c3[0])/3;
		COG[1] = (c1[1] + c2[1] + c3[1])/3;
		COG[2] = (c1[2] + c2[2] + c3[2])/3;
		return COG;
	}

	private double[] intListToDoubleList(int[] a) {
		return new double[] { a[0], a[1] };
	}



	//---------------------GETTERS&SETTERS------------------------------------//

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

	public PhysicsCalculations getPhysics() {
		return this.physicsCalc;
	}
}
