package DroneAutopilot;

import java.util.ArrayList;
import java.util.List;

import exceptions.EmptyPositionListException;
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
	//als er geen rode pixels zijn, exception
	// COG = center of gravity
	public int[] getCOG (ArrayList<int[]> listOfPixelCoordinates) throws EmptyPositionListException{
		int sumX = 0;
		int sumY = 0;
		int[] cOG = {0,0};
		if(listOfPixelCoordinates.size() == 0){
			throw new EmptyPositionListException(listOfPixelCoordinates);
		}
		for(int i = 0; i < listOfPixelCoordinates.size(); i++){
			sumX += listOfPixelCoordinates.get(i)[0];
			sumY += listOfPixelCoordinates.get(i)[1];
		}
		cOG[0] = sumX/listOfPixelCoordinates.size();
		cOG[1] = sumY/listOfPixelCoordinates.size();
		return cOG;
	}
	
	//uitgewerkt voor rood in volgende functie
	public ArrayList<int[]> getPixelsOfColor(Camera camera, int givenColor){
        int[] image = camera.takeImage();
        ArrayList<int[]> coloredPositions = new ArrayList<int[]>();
        for(int i = 0; image.length > i; i++){
            if(image[i] == givenColor){
            	coloredPositions.add(indexToCoordinates(i, camera));
            }
        }
        return coloredPositions;	
	}
	
	//TODO waarde van red invullen
	private final static int red = 0;
	
	//pixels of red color
	public ArrayList<int[]> getRedPixels(Camera camera){
		return getPixelsOfColor(camera, ImageCalculations.red);
	}
	
	//check if image = red
	public boolean checkIfAllRed(Camera camera){
		ArrayList<int[]> listRedPixels = getRedPixels(camera);
		return listRedPixels.size() == camera.takeImage().length;
	}
}
