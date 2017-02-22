package DroneAutopilot.correct;

import p_en_o_cw_2016.Drone;

public class CorrectWind extends Correct{

	public float[] expectedPosition;
		
	public CorrectWind(Drone drone) {
		super(drone);
		// TODO Auto-generated constructor stub
	}

	public float[] findExpectedPosition() {
		float[] position = this.getPhysicsCalculations().getPosition();
				
	}
	
	public float[] calculateWind(){
		return null;
	}
}