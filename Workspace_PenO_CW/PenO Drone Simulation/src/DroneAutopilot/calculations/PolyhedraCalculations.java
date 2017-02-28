package DroneAutopilot.calculations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import p_en_o_cw_2016.Camera;

public class PolyhedraCalculations {

	public final static int white = 16777215;

	private HashMap<float[], ArrayList<int[]>> hashMapOuterColor;
	private HashMap<float[], ArrayList<int[]>> hashMapInnerColor;
	private HashMap<float[], ArrayList<int[]>> hashMapObstacle;
	private Camera leftCamera;
	private Camera rightCamera;
	
	public void execute(){
		this.SeparateTargetsAndObstacles(leftCamera);
		HashMap<float[],float[]> leftCOGS = this.findCornersOfFullTrianglesViaCOG();
		System.out.println(leftCOGS.size());
		this.SeparateTargetsAndObstacles(rightCamera);
		HashMap<float[],float[]> rightCOGS = this.findCornersOfFullTrianglesViaCOG();
		System.out.println(rightCOGS.size());
	}
	
	// [i] -> (x,y)
	public int[] indexToCoordinates(int index, Camera camera) {
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
		return hashMapDifferentColors;
	}
	
	public float[] colorIntToHSV(int color){
		//to RGB
		int B = color % 256;
		int G = (color / 256) % 256;
		int R = color / (256 * 256);
		
		//to HSV
		float[] HSV1 = {0,0,0};
		float[] HSV = Color.RGBtoHSB(R, G, B, HSV1);
		return HSV;
	}
	
	public void SeparateTargetsAndObstacles(Camera camera){
		HashMap<Integer, ArrayList<int[]>> hashMapDifferentColors = this.calculatePixelsOfEachColor(camera);
		Set<Integer> colorIntValuesSet = hashMapDifferentColors.keySet();
		ArrayList<Integer> colorIntValuesArray = new ArrayList<Integer>();
		colorIntValuesArray.addAll(colorIntValuesSet);
		
		HashMap<float[], ArrayList<int[]>> hashMapOuterColor= new HashMap<float[], ArrayList<int[]>>();
		HashMap<float[], ArrayList<int[]>> hashMapObstacle= new HashMap<float[], ArrayList<int[]>>();
		HashMap<float[], ArrayList<int[]>> hashMapInnerColor = new HashMap<float[], ArrayList<int[]>>();
		
		// S>0.55: outertriangle, S<0.45: innertriangle
		// V>0.55: target , V<0.45: obstacle
		// anders Error
		for(int i=0;i<colorIntValuesArray.size();i++){
			float[] HSV = this.colorIntToHSV(i);
			if(HSV[2]<0.45){  		//obstacle
				if(HSV[1]>0.55){	//outertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(i);
					hashMapObstacle.put(HSV, coordinateList);
				}
			}else if(HSV[2]>0.55){	//target
				if(HSV[1]<0.45){	//innertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(i);
					hashMapInnerColor.put(HSV, coordinateList);
				}else if(HSV[1]>0.55){	//outertriangle
					ArrayList<int[]> coordinateList = hashMapDifferentColors.get(i);
					hashMapOuterColor.put(HSV, coordinateList);
				}else{
					throw new IllegalArgumentException();
				}
			}else{
				throw new IllegalArgumentException();
			}
		}
		this.setHashMapTargetOuterColor(hashMapOuterColor);
		this.setHashMapTargetInnerColor(hashMapInnerColor);
		this.setHashMapObstacleOuterColor(hashMapObstacle);
	}
	
	public ArrayList<int[]> findMinimumCoordinates(ArrayList<int[]> coordinates, int choice){
		ArrayList<int[]> minEqualCoord = new ArrayList<int[]>();
		int infinity = (int) Double.POSITIVE_INFINITY;
		int minimum = infinity ;
		for(int[] coordinate:coordinates){
			if(coordinate[choice]<minimum){
				minimum = coordinate[choice];
				minEqualCoord.clear();
				minEqualCoord.add(coordinate);
			}else if(coordinate[choice] == minimum){
				minEqualCoord.add(coordinate);
			}
		}
		return minEqualCoord;
	}
	
	public ArrayList<int[]> findMaximumCoordinates(ArrayList<int[]> coordinates, int choice){
		ArrayList<int[]> maxEqualCoord = new ArrayList<int[]>();
		int maximum = 0 ;
		for(int[] coordinate:coordinates){
			if(coordinate[choice]>maximum){
				maximum = coordinate[choice];
				maxEqualCoord.clear();
				maxEqualCoord.add(coordinate);
			}else if(coordinate[choice] == maximum){
				maxEqualCoord.add(coordinate);
			}
		}
		return maxEqualCoord;
	}
	
	public HashMap<float[],ArrayList<int[]>> findThreePointsTriangles(HashMap<float[], ArrayList<int[]>> hashMapTarget){
		HashMap<float[],ArrayList<int[]>> solution = new HashMap<float[],ArrayList<int[]>>();
		//gevonden HSV kleuren in arraylist opslaan
		Set<float[]> HSVcolorSet = hashMapTarget.keySet();
		ArrayList<float[]> HSVcolorArray = new ArrayList<float[]>();
		HSVcolorArray.addAll(HSVcolorSet);
		
		//loopen over alle kleuren en daarvan de hoekpunten bepalen.
		for(int i=0;i<HSVcolorArray.size();i++){
			ArrayList<int[]> coordinates = hashMapTarget.get(HSVcolorArray.get(i));
			ArrayList<int[]> possibleCorners = new ArrayList<int[]>();
			//linker boven
			ArrayList<int[]> links = this.findMinimumCoordinates(coordinates, 0);
			ArrayList<int[]> linksBoven = this.findMinimumCoordinates(links, 1);
			possibleCorners.add(linksBoven.get(0));
			//linker onder
			ArrayList<int[]> links2 = this.findMinimumCoordinates(coordinates, 0);
			ArrayList<int[]> linksOnder = this.findMaximumCoordinates(links2, 1);
			possibleCorners.add(linksOnder.get(0));
			//rechts boven
			ArrayList<int[]> rechts = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsBoven = this.findMinimumCoordinates(rechts, 1);
			possibleCorners.add(rechtsBoven.get(0));
			//rechts onder
			ArrayList<int[]> rechts2 = this.findMaximumCoordinates(coordinates, 0);
			ArrayList<int[]> rechtsOnder = this.findMaximumCoordinates(rechts2, 1);
			possibleCorners.add(rechtsOnder.get(0));
			//boven links
			ArrayList<int[]> boven = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenLinks = this.findMinimumCoordinates(boven, 0);
			possibleCorners.add(bovenLinks.get(0));
			//boven rechts
			ArrayList<int[]> boven2 = this.findMinimumCoordinates(coordinates, 1);
			ArrayList<int[]> bovenRechts = this.findMaximumCoordinates(boven2, 0);
			possibleCorners.add(bovenRechts.get(0));
			//onder links
			ArrayList<int[]> onder = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderLinks = this.findMinimumCoordinates(onder, 0);
			possibleCorners.add(onderLinks.get(0));
			//onder rechts
			ArrayList<int[]> onder2 = this.findMaximumCoordinates(coordinates, 1);
			ArrayList<int[]> onderRechts = this.findMaximumCoordinates(onder2, 0);
			possibleCorners.add(onderRechts.get(0));
			
			boolean boolLinks= false;
			boolean boolRechts=false;
			boolean boolOnder=false;
			boolean boolBoven=false;
			
			//verwijder overlappende hoekpunten
			if(linksBoven.get(0)[0] == linksOnder.get(0)[0] &&  linksBoven.get(0)[1] == linksOnder.get(0)[1]){
				possibleCorners.remove(linksOnder);
				boolLinks = true;
			}
			if(rechtsBoven.get(0)[0] == rechtsOnder.get(0)[0] &&  rechtsBoven.get(0)[1] == rechtsOnder.get(0)[1]){
				possibleCorners.remove(rechtsBoven);
				boolRechts = true;
			} 
			if(bovenLinks.get(0)[0] == bovenRechts.get(0)[0] &&  bovenLinks.get(0)[1] == bovenRechts.get(0)[1]){
				possibleCorners.remove(bovenLinks);
				boolBoven = true;
			} 
			if(onderLinks.get(0)[0] == onderRechts.get(0)[0] && onderLinks.get(0)[1] == onderRechts.get(0)[1]){
				possibleCorners.remove(onderRechts);
				boolOnder = true;
			}
			if(linksBoven.get(0)[0] == bovenLinks.get(0)[0] && linksBoven.get(0)[1] == bovenLinks.get(0)[1]){
				if(boolBoven)
					possibleCorners.remove(bovenRechts);
				else
					possibleCorners.remove(bovenLinks);
			}
			if(bovenRechts.get(0)[0] == rechtsBoven.get(0)[0] && bovenRechts.get(0)[1] == rechtsBoven.get(0)[1]){
				if(boolRechts)
					possibleCorners.remove(rechtsOnder);
				else
					possibleCorners.remove(rechtsBoven);
			}
			if(rechtsOnder.get(0)[0] == onderRechts.get(0)[0] && rechtsOnder.get(0)[1] == onderRechts.get(0)[1]){
				if(boolOnder)
					possibleCorners.remove(onderLinks);
				else
					possibleCorners.remove(onderRechts);
			}
			if(linksOnder.get(0)[0] == onderLinks.get(0)[0] && linksOnder.get(0)[1] == onderLinks.get(0)[1]){
				if(boolLinks)
					possibleCorners.remove(linksBoven);
				else
					possibleCorners.remove(linksOnder);
			}
			
			if(possibleCorners.size() == 3){
				solution.put(HSVcolorArray.get(i), possibleCorners);
			}
		}
		return solution;
	}
	
	public HashMap<float[],float[]> findCOGOfAllColors(HashMap<float[],ArrayList<int[]>> hashMapCorners){
		HashMap<float[],float[]> COGHashMap = new HashMap<float[],float[]>();
		Set<float[]> colors = hashMapCorners.keySet();
		for(float[] color:colors){
			ArrayList<int[]> corners = hashMapCorners.get(color);
			//bereken zp
			float x = (corners.get(0)[0] + corners.get(1)[0] + corners.get(2)[0]) / 3;
			float y = (corners.get(0)[1] + corners.get(1)[1] + corners.get(2)[1]) / 3;
			float[] COG = {x,y};
			COGHashMap.put(color, COG);
		}
		return COGHashMap;
	}
	
	public HashMap<float[],float[]> findCornersOfFullTrianglesViaCOG(){
		HashMap<float[],ArrayList<int[]>> outerTriangles = this.findThreePointsTriangles(this.getHashMapTargetOuterColor());
		HashMap<float[],ArrayList<int[]>> innerTriangles = this.findThreePointsTriangles(this.getHashMapTargetInnerColor());
		
		HashMap<float[],float[]> outerTrianglesCOG = this.findCOGOfAllColors(outerTriangles);
		Set<float[]> setOuterCOG = outerTrianglesCOG.keySet();
		HashMap<float[],float[]> innerTrianglesCOG = this.findCOGOfAllColors(innerTriangles);
		Set<float[]> setInnerCOG = innerTrianglesCOG.keySet();

		HashMap<float[], float[]> resultingOuterTriangleCOGs = new HashMap<float[],float[]>();
		
		for(float[] outercolor:setOuterCOG){
			for(float[] innercolor:setInnerCOG){
				float[] outerCOG = outerTrianglesCOG.get(outercolor);
				float[] innerCOG = innerTrianglesCOG.get(innercolor);
				if(outerCOG[0] <= innerCOG[0]+1 && outerCOG[0] >= innerCOG[0]-1 && outerCOG[1] <= innerCOG[1]+1 
						&& outerCOG[1] >= innerCOG[1]-1){
					//buiten en binnen COG liggen max 1 pixel van elkaar
					resultingOuterTriangleCOGs.put(outercolor,outerCOG);
				}
			}
		}
		return resultingOuterTriangleCOGs;
	}
	
	
	public HashMap<float[], ArrayList<int[]>> getHashMapTargetOuterColor(){
		return this.hashMapOuterColor;
	}
	private void setHashMapTargetOuterColor(HashMap<float[], ArrayList<int[]>> hashMapOuterColor){
		this.hashMapOuterColor = hashMapOuterColor;
	}
	
	public HashMap<float[], ArrayList<int[]>> getHashMapTargetInnerColor(){
		return this.hashMapInnerColor;
	}
	private void setHashMapTargetInnerColor(HashMap<float[], ArrayList<int[]>> hashMapInnerColor){
		this.hashMapInnerColor = hashMapInnerColor;
	}
	
	public HashMap<float[], ArrayList<int[]>> getHashMapObstacleOuterColor(){
		return this.hashMapObstacle;
	}
	private void setHashMapObstacleOuterColor(HashMap<float[], ArrayList<int[]>> hashMapObstacle){
		this.hashMapObstacle = hashMapObstacle;
	}

}
