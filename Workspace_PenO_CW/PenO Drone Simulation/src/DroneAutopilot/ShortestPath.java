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
	private HashMap<Integer, float[][]> closestOrbs;//<color,[[cogL],[cogr]]>

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
	public void calculateFirstOrb() {
		int size = getClosestOrbs().size();
		float[] distances = new float[size];
		int[] keys = new int[size];
		int i = 0;
		for(int key : getClosestOrbs().keySet()){
			distances[i] = this.getMoveToTarget().getPhysicsCalculations().getDistance(getClosestOrbs().get(key)[0], getClosestOrbs().get(key)[1]);
			keys[i] = key;
			i++;
		}
		int index = this.indexMinValueArray(distances);
		this.setColorFirstOrb(keys[index]);
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
	public void calculateSecondOrb(int[] listOfColors) {
		int size = getClosestOrbs().size()-1;
		float[] distances = new float[size];
		int[] keys = new int[size];
		int i = 0;
		for(int key : getClosestOrbs().keySet()){
			if(key != this.getColorFirstOrb()){
				distances[i] = getDistanceBetweenOrbs(getClosestOrbs().get(key)[0], getClosestOrbs().get(key)[1], getClosestOrbs().get(getColorFirstOrb())[0], getClosestOrbs().get(getColorFirstOrb())[1]);
				keys[i] = key;
				i++;	
			}
		}
		int index = this.indexMinValueArray(distances);
		this.setColorSecondOrb(keys[index]);
	}
	
	public float getDistanceBetweenOrbs(float[] cogL1, float[] cogR1, float[] cogL2, float[] cogR2){
		float depth1 = this.getMoveToTarget().getPhysicsCalculations().getDepth(cogL1, cogR1);
		float depth2 = this.getMoveToTarget().getPhysicsCalculations().getDepth(cogL2, cogR2);
		float alpha1 = this.getMoveToTarget().getPhysicsCalculations().horizontalAngleDeviation(cogL1, cogR1);
		float alpha2 = this.getMoveToTarget().getPhysicsCalculations().horizontalAngleDeviation(cogL2, cogR2);
		float beta1 = this.getMoveToTarget().getPhysicsCalculations().verticalAngleDeviation(cogL1);
		float beta2 = this.getMoveToTarget().getPhysicsCalculations().verticalAngleDeviation(cogL2);
		//distance = sqrt( (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2 )
		float x1 = (float) (depth1*Math.tan(alpha1));
		float x2 = (float) (depth1*Math.tan(alpha2));
		float y1 = (float) (depth1*Math.tan(beta1));
		float y2 = (float) (depth1*Math.tan(beta2));
		float z1 = depth1;
		float z2 = depth2;
		float distance = (float) Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2)+Math.pow((z1-z2),2));
		return distance;
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

	public HashMap<Integer, float[][]> getClosestOrbs() {
		return closestOrbs;
	}

	public void setClosestOrbs(HashMap<Integer, float[][]> closestOrbs) {
		this.closestOrbs = closestOrbs;
	}

	
}
