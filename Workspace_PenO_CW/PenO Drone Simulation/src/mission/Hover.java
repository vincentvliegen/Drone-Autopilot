package mission;

import DroneAutopilot.MoveToTarget;
import p_en_o_cw_2016.Drone;

public class Hover extends Mission{
	
	public Hover(MoveToTarget moveToTarget, Drone drone){
		super(moveToTarget, drone);
	}

	@Override
	public void execute() {
		this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight());
	}
}
