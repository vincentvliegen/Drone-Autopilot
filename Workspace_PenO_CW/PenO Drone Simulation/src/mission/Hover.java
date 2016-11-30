package mission;

import p_en_o_cw_2016.Drone;

public class Hover extends Mission{
	
	public Hover(Drone drone){
		super(drone);
	}

	@Override
	public void execute() {
		this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight());
	}
}
