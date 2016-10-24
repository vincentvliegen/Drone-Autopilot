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
	
	// kleur in int
	private final static int red = 255;
	//private final static int white = 16777215;
	//private final static int black = 0;
	//private final static int green = 65280;
	//private final static int blue = 16711680;
	
	//pixels of red color
	public ArrayList<int[]> getRedPixels(Camera camera){
		return getPixelsOfColor(camera, ImageCalculations.red);
	}
	
	//check if image = red
	public boolean checkIfAllRed(Camera camera){
		ArrayList<int[]> listRedPixels = getRedPixels(camera);
		return listRedPixels.size() == camera.takeImage().length;
	}
	
	//kleurenconversies voor debug
	
	//conversie int color naar leesbaar (R,G,B) formaat
	public int[] colorIntToRGB(int color){
		int [] RGB = {0,0,0};
		/*R*/RGB[0] = color % 256;
		/*G*/RGB[1] = color / 256;
		/*B*/RGB[2] = color / 256*256;
		return RGB;
	}
	
	//conversie (R,G,B) naar int  =   dec(BGR)
	public int colorRGBToInt(int[] RGB){
		int color = 0;
		color = /*R*/RGB[0] + /*G*/ RGB[1]*256 + /*B*/RGB[2]*256*256;
		return color;
	}
	
}
