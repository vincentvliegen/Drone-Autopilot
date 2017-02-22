package DroneAutopilot.calculations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import p_en_o_cw_2016.Camera;

public class PolyhedraCalculations {

	public final static int white = 16777215;

	private HashMap<float[], ArrayList<int[]>> hashMapOuterColor;
	private HashMap<float[], ArrayList<int[]>> hashMapObstacle;
	
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
		
		HashMap<float[], ArrayList<int[]>> hashMapOuterColor= new HashMap<float[], ArrayList<int[]>>();;
		HashMap<float[], ArrayList<int[]>> hashMapObstacle= new HashMap<float[], ArrayList<int[]>>();;
		
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
				if(HSV[1]<0.45){
					//innertriangle
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
		this.setHashMapObstacleOuterColor(hashMapObstacle);
	}
	
	public void findTriangleCorners(){
		Set<float[]> colorIntValuesSet = this.getHashMapTargetOuterColor().keySet();
		ArrayList<float[]> colorHSVValuesArray = new ArrayList<float[]>();
		colorHSVValuesArray.addAll(colorIntValuesSet);
		
		for(int i=0;i<colorHSVValuesArray.size();i++){
			ArrayList<int[]> coordinates = this.getHashMapTargetOuterColor().get(i);
			for(int j=0;j<coordinates.size();j++){
				
			}
		}
	}
	
	
	
	public HashMap<float[], ArrayList<int[]>> getHashMapTargetOuterColor(){
		return this.hashMapOuterColor;
	}
	private void setHashMapTargetOuterColor(HashMap<float[], ArrayList<int[]>> hashMapOuterColor){
		this.hashMapOuterColor = hashMapOuterColor;
	}
	
	public HashMap<float[], ArrayList<int[]>> getHashMapObstacleOuterColor(){
		return this.hashMapObstacle;
	}
	private void setHashMapObstacleOuterColor(HashMap<float[], ArrayList<int[]>> hashMapObstacle){
		this.hashMapObstacle = hashMapObstacle;
	}

}
