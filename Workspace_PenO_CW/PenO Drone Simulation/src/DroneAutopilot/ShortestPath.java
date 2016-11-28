package DroneAutopilot;

import java.util.Iterator;
import java.util.Map;


public class ShortestPath {

	private final MoveToTarget moveToTarget;
	private int colorFirstOrb;
	private int colorSecondOrb;
	
	public MoveToTarget getMoveToTarget() {
		return moveToTarget;
	}
	
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
		Iterator it = this.getMoveToTarget().getImageCalculations().getPixelsOfEachColor().entrySet().iterator();
	    //TODO right
		
		
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    return colorList;
	}
	
	public void calculateFirstOrb(listOfColors) {
		for(int i; i<)
	}
	
	public void calculateSecondOrb() {
		//TODO set color closest orb to firstOrb
	}
}
