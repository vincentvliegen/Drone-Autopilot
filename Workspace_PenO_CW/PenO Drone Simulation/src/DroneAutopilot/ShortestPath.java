package DroneAutopilot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ShortestPath {

	private final MoveToTarget moveToTarget;
	private int colorFirstOrb;
	private int colorSecondOrb;
	private HashMap<Integer,  ArrayList<int[]>> allPixelsLeftImage;
	private HashMap<Integer,  ArrayList<int[]>> allPixelsRightImage;


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
	
	public void calculateFirstOrb(int[] listOfColors) {
		for(int i = 0; i < listOfColors.length; i++);
	}
	
	public void calculateSecondOrb() {
		//TODO set color closest orb to firstOrb
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
	
}
