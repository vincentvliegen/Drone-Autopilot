package mission;

import DroneAutopilot.MoveToTarget;
import p_en_o_cw_2016.Drone;

public class OneSphere extends Mission{
	private int color;
	
	public OneSphere(MoveToTarget moveToTarget, Drone drone, int color){
		super(moveToTarget, drone);
		this.color = color;
	}

	@Override
	public void execute() {
		this.getMoveToTarget().execute(color);
		// Kijken om 1 bol te zoeken
	}
}
