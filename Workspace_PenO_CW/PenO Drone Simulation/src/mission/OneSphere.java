package mission;

import p_en_o_cw_2016.Drone;

public class OneSphere extends Mission{
	private final int RED = 16711680;
	
	public OneSphere(Drone drone){
		super(drone);
	}

	@Override
	public void execute() {
		this.getMoveToTarget().execute(RED);
		// Kijken om 1 bol te zoeken
	}
}
