package DroneAutopilot;

import java.util.ArrayList;
import java.util.List;
import implementedClasses.Camera;

public class ImageCalculations {
	
	//[i] -> (x,y)
	public int[] indexToCoordinates(int index, Camera camera){
		int width = camera.getWidth();
		int x = (int) (index / width);
		int y = (int) (index % width);
		int[] coord = {x,y};
	    return coord;
	}
	
	//(x,y) -> [i]
	public int coordinatesToIndex(int[] coordinates, Camera camera){
		int index = (int) (coordinates[0]+coordinates[1]*camera.getWidth());
	    return index;
	}

	//zwaartepunt van groepje pixels bepalen
	// COG = center of gravity
	public int[] getCOG (int[][] listOfPixelCoordinates){
		int sumX = 0;
		int sumY = 0;
		int[] cOG = {0,0};
		for(int i = 0; i < listOfPixelCoordinates.length; i++){
			sumX += listOfPixelCoordinates[i][0];
			sumY += listOfPixelCoordinates[i][1];
		}
		cOG[0] = sumX/listOfPixelCoordinates.length;
		cOG[1] = sumY/listOfPixelCoordinates.length;
		return cOG;
	}
	
	//uitgewerkt voor rood in volgende functie
	public int[][] getPixelsOfColor(Camera camera, int givenColor){
        int[] image = camera.takeImage();
        List<int[]> coloredPositions = new ArrayList<int[]>();
        for(int i = 0; image.length > i; i++){
            if(image[i] == givenColor){
            	coloredPositions.add(indexToCoordinates(i, camera));
            }
        }
        return coloredPositions.toArray(new int[coloredPositions.size()][]);		
	}
	
	//TODO waarde van red invullen
	private final static int red = 0;
	
	//pixels of red color
	public int[][]getRedPixels(Camera camera){
		return getPixelsOfColor(camera, ImageCalculations.red);
	}
	
	//check if image = red
	public boolean checkIfAllRed(Camera camera){
		int[][] listRedPixels = getRedPixels(camera);
		return listRedPixels.length == camera.takeImage().length;
	}
}
