package DroneAutopilot;

import java.util.ArrayList;

import exceptions.EmptyPositionListException;
import p_en_o_cw_2016.Camera;

public class ImageCalculations {
	
	//[i] -> (x,y)
	public int[] indexToCoordinates(int index, Camera camera){
		int width = camera.getWidth();
		int x = (int) (index % width);
		int y = (int) (index / width);
		int[] coord = {x,y};
	    return coord;
	}

	//zwaartepunt van groepje pixels bepalen
	//als er geen rode pixels zijn, exception
	// COG = center of gravity
	public float[] getCOG (ArrayList<int[]> listOfPixelCoordinates) throws EmptyPositionListException{
		float sumX = 0;
		float sumY = 0;
		float[] cOG = {0,0};
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
	public final static int red = 255;
	public final static int white = 16777215;
	public final static int black = 0;
	public final static int green = 65280;
	public final static int blue = 16711680;
	
	//pixels of red color
	public ArrayList<int[]> getRedPixels(Camera camera){
		return getPixelsOfColor(camera, ImageCalculations.red);
	}
	
	//check if image = red
	public boolean checkIfAllRed(Camera camera){
		ArrayList<int[]> listRedPixels = getRedPixels(camera);
		return listRedPixels.size() == camera.takeImage().length;
	}
	
	
	//berekening centrum cirkel
	
	//TODO nauwkeurigere uitwerking, geeft zeer onnauwkeurige waarden bij dicht bij een liggende punten/punten op zelfde hoogte of breedte (zeer kleine bollen/ zeer grote bollen met 1 zijde bijna recht), zo onbruikbaar
	//center of circle GIVEN 3 points on circumference
	public int[] centerOfCircle(ArrayList<int[]> listOfPixelCoordinates, Camera camera) throws IllegalArgumentException{
		ArrayList<int[]> threePoints = new ArrayList<int[]>();
		try{
			threePoints.addAll(pointsOnCircumference(listOfPixelCoordinates, camera));
		}
		catch (IllegalArgumentException e){
			throw new IllegalArgumentException();
		}
		float X = 0;
		float Y = 0;
		float x1 = threePoints.get(0)[0];
		float y1 = threePoints.get(0)[1];
		float x2 = threePoints.get(1)[0];
		float y2 = threePoints.get(1)[1];
		float x3 = threePoints.get(2)[0];
		float y3 = threePoints.get(2)[1];
		//stelsel in x en y, met ay+b=0 TODO meer uitleg
		float a = 2*(y1-y3)+2*(y1-y2)/(x1-x2)*(x1-x3);
		float b = (float) (Math.pow(y3,2) + Math.pow(x3,2) - Math.pow(y1,2) - Math.pow(x1,2) + (Math.pow(y2,2) + Math.pow(x2,2) - Math.pow(y1,2) - Math.pow(x1,2))/(2*(x1-x2)));
		Y =  (-b/a);
		X =  (float) ((2*(y1-y2)*Y + Math.pow(y2,2) + Math.pow(x2,2) - Math.pow(y1,2) - Math.pow(x1,2))/(2*(x2-x1)));
		return new int[] {(int)Math.round(X),(int)Math.round(Y)};
	}
	
	//calculate 3 most representative points on circumference (3 furthest neighbours)
	//ONLY if at least 3 points on circumference
	public ArrayList<int[]> pointsOnCircumference (ArrayList<int[]> listOfPixelCoordinates, Camera camera) throws IllegalArgumentException{
		ArrayList<int[]> AllEdges = new ArrayList<int[]>();
		ArrayList<int[]> AllCPoints = new ArrayList<int[]>();
		ArrayList<int[]> result = new ArrayList<int[]>();
		if(listOfPixelCoordinates.size() == 0){
			throw new IllegalArgumentException();
		}
		int y1 = listOfPixelCoordinates.get(0)[1];
		int y2 = listOfPixelCoordinates.get(listOfPixelCoordinates.size()-1)[1];
		int[] currentPos = new int[] {0,y1};
		int[] previousPos = new int[] {0,y1};
		boolean addedPrevPos = false;
		int cameraWidth = camera.getWidth();
		int cameraHeight = (int) (camera.getWidth()*(Math.tan(Math.toRadians(camera.getVerticalAngleOfView()/2))/(Math.tan(Math.toRadians(camera.getHorizontalAngleOfView()/2)))));
		for(int i = 0; i < listOfPixelCoordinates.size();i++){//bepaal alle punten op de rand van de groep
			currentPos = listOfPixelCoordinates.get(i);
			if (currentPos[1] == previousPos[1] + 1){//de eerste en laatste waarde per rij worden toegevoegd
				if(!addedPrevPos){
					AllEdges.add(previousPos);//vermijdt dubbele toevoeging laatste element eerste rij
				}
				AllEdges.add(currentPos);
				addedPrevPos = true;
			}else if(currentPos[1] == y1 || currentPos[1] == y2){//bovenste en onderste rij worden toegevoegd
				AllEdges.add(currentPos);
				addedPrevPos = true;
			}else {
				addedPrevPos = false;
			}
			previousPos = currentPos;
		}
		for(int j = 0; j<AllEdges.size();j++){// bepaal alle punten op de cirkel uit de randgroep
			if(AllEdges.get(j)[1] < cameraHeight-1 && AllEdges.get(j)[1] > 0 && AllEdges.get(j)[0] < cameraWidth-1 && AllEdges.get(j)[0] > 0 ){// niet tegen de randen
				AllCPoints.add(AllEdges.get(j));
			}
		}
		if(AllCPoints.size() < 3){
			throw new IllegalArgumentException();
		}
		result.add(AllCPoints.get(0));
		result.add(AllCPoints.get(AllCPoints.size()/2));//middelste punt ligt verst van eerste en laatste punt
		result.add(AllCPoints.get(AllCPoints.size()-1));
		return result;
	}
	
	
	
	
	// debug
	
//		//(x,y) -> [i]
//		public int coordinatesToIndex(int[] coordinates, Camera camera){
//			int index = (int) (coordinates[0]+coordinates[1]*camera.getWidth());
//		    return index;
//		}
//	
//		//conversie int color naar leesbaar (R,G,B) formaat
//		public int[] colorIntToRGB(int color){
//			int [] RGB = {0,0,0};
//			/*R*/RGB[0] = color % 256;
//			/*G*/RGB[1] = (color / 256) % 256;
//			/*B*/RGB[2] = color / (256*256);
//			return RGB;
//		}
//		
//		//conversie (R,G,B) naar int  =   dec(BGR)
//		public int colorRGBToInt(int[] RGB){
//			int color = 0;
//			color = /*R*/RGB[0] + /*G*/ RGB[1]*256 + /*B*/RGB[2]*256*256;
//			return color;
//		}
//		
	
}
