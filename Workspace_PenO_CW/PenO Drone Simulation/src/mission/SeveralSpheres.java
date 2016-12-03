package mission;

import DroneAutopilot.MoveToTarget;
import p_en_o_cw_2016.Drone;

public class SeveralSpheres extends Mission{
	
	public boolean firstTime; // TODO terug aanzetten wanneer opdracht opnieuw geselecteerd wordt

	
	public SeveralSpheres(MoveToTarget moveToTarget, Drone drone){
		super(moveToTarget, drone);
	}

	@Override
	public void execute() {
		//TODO implementation.
		System.out.println("EXSevSph");	
		//TODO execute moet naar several orbs missie
		int color = null;
		if (scanning){ //TODO echte scanboolean
			color = scanresultaat; //TODO echt scanresultaat
			if (color != null)
				scanning = false;
		} else {
			//			if (this.firstTime) {
			//				// TODO eerste keer lijst van alle kleuren
			//				this.firstTime = false;
			//			}
			if (dichtgenoeg) { //TODO boolean van movetotarget
				// TODO tweede bol is eerste bol
				// TODO bereken nieuwe tweede bol
			}
			//TODO check of bol in beld die dichter licht dan huidig doel
			this.moveToTarget.execute(colorFirstOrb);
		}}
}
