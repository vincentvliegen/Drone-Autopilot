package DroneAutopilot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ShortestPath {

	private final MoveToTarget moveToTarget;
	private final static int numberOfOrbsConsidered = 5;
	private int colorFirstOrb;
	private int colorSecondOrb;
	private HashMap<Integer,  ArrayList<int[]>> allPixelsLeftImage;
	private HashMap<Integer,  ArrayList<int[]>> allPixelsRightImage;
	private HashMap<Integer, int[][]> closestOrbs;//<color,[[cogL],[cogr]]>

	public ShortestPath(MoveToTarget moveToTarget){
		this.moveToTarget = moveToTarget;
		
	}
	
	public void execute() {
		if(){
		//TODO eerste keer lijst van alle kleuren
		}
		if(dichtgenoeg){
			//TODO verwijder kleur uit lijst kleuren, tweede bol is eerste bol (tenzij beter), bereken nieuwe tweede bol
		}
		this.moveToTarget.execute(colorFirstOrb);			
	}
	
	//Geeft n grootste kleuren van bollen (of minder als er geen n bollen zijn)
	//checken voor twee camera's -> niet in beide beelden -> laten vallen
	//Left en Right hashmap parameters maken
	public int[] getBiggestOrbs() {
		System.out.println("getBiggestOrb");
		//left
		this.getMoveToTarget().getImageCalculations().calculatePixelsOfEachColor(this.getMoveToTarget().getDrone().getLeftCamera());
		this.setAllPixelsLeftImage(this.getMoveToTarget().getImageCalculations().getPixelsOfEachColor());
		Iterator it = this.getMoveToTarget().getImageCalculations().getPixelsOfEachColor().entrySet().iterator();
	    //TODO right
		
		
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return colorList;
	}
	
	//geeft kleur terug van dichtstbijzijnde
	public void calculateFirstOrb(int[] listOfColors) {
		int closestOrb;
		float[] distances = new float[getNumberoforbsconsidered()];
		for(int i = 0; i < listOfColors.length; i++){
			int color = listOfColors[i];
			//linkerkant zwaartepunt
			ArrayList<int[]> leftPixels = this.getAllPixelsLeftImage().get(color);
			float[] leftCOG = this.moveToTarget.findBestCenterOfGravity(leftPixels, this.getMoveToTarget().getDrone().getLeftCamera());
			//rechterkant zwaartepunt
			ArrayList<int[]> rightPixels = this.allPixelsRightImage.get(color);
			float[] rightCOG = this.moveToTarget.findBestCenterOfGravity(rightPixels, this.getMoveToTarget().getDrone().getLeftCamera());
			//afstand
			distances[i] = this.getMoveToTarget().getPhysicsCalculations().getDistance(leftCOG, rightCOG);
		}
		closestOrb = this.indexMinValueArray(distances);
		this.setColorFirstOrb(closestOrb);
	}
	
	public int indexMinValueArray(float[] array){
		int index = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i]<array[index]){
				index = i;
			}
		}
		return index;
	}
	
	//kleur bol dichtst bij bol 1
	//TODO verander listOfColors variable ipv parameter
	public void calculateSecondOrb(int[] listOfColors) {
		for(int i = 0; i < listOfColors.length; i++){
			
		}
		this.setColorSecondOrb(0);
	}
	
	public MoveToTarget getMoveToTarget() {
		return moveToTarget;
	}
	
	public HashMap<Integer, ArrayList<int[]>> getAllPixelsLeftImage() {
		return allPixelsLeftImage;
	}

	public void setAllPixelsLeftImage(HashMap<Integer, ArrayList<int[]>> allPixelsLeftImage) {
		this.allPixelsLeftImage = allPixelsLeftImage;
	}

	public HashMap<Integer, ArrayList<int[]>> getAllPixelsRightImage() {
		return allPixelsRightImage;
	}

	public void setAllPixelsRightImage(HashMap<Integer, ArrayList<int[]>> allPixelsRightImage) {
		this.allPixelsRightImage = allPixelsRightImage;
	}
	
	public int getColorFirstOrb() {
		return colorFirstOrb;
	}

	public void setColorFirstOrb(int colorFirstOrb) {
		this.colorFirstOrb = colorFirstOrb;
	}

	public int getColorSecondOrb() {
		return colorSecondOrb;
	}

	public void setColorSecondOrb(int colorSecondOrb) {
		this.colorSecondOrb = colorSecondOrb;
	}

	public static int getNumberoforbsconsidered() {
		return numberOfOrbsConsidered;
	}

	public HashMap<Integer, int[][]> getClosestOrbs() {
		return closestOrbs;
	}

	public void setClosestOrbs(HashMap<Integer, int[][]> closestOrbs) {
		this.closestOrbs = closestOrbs;
	}

	
}
