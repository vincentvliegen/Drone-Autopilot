package DroneAutopilot.correct;

import DroneAutopilot.controllers.PitchController;
import p_en_o_cw_2016.Drone;

public class CorrectPitch extends Correct{

	private final PitchController pitchPI;
	private boolean hoverStarted;

	public CorrectPitch(Drone drone) {
		super(drone);
		this.pitchPI = new PitchController(1,0);
	}

	public void hover() {
		if(!this.isHoverStarted()){
			this.getPitchPI().resetSetpoint(0);
			this.setHoverStarted(true);
		}
		if (this.getDrone().getPitch() > 0.1) {
			float output = this.getPitchPI().calculateRate(this.getDrone().getPitch(), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getDrone().getPitch())*10);
			this.getDrone().setPitchRate(Math.max(output,-this.getDrone().getMaxPitchRate()));
			System.out.println("output hover: " + output);
			float gravity = Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight();
			this.getDrone().setThrust(gravity/ (float) Math.cos(Math.toRadians(this.getDrone().getPitch())));
		} else if (this.getDrone().getPitch() < -0.1) {
			float output = this.getPitchPI().calculateRate(this.getDrone().getPitch(), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getDrone().getPitch())*10);
			this.getDrone().setPitchRate(Math.min(output,this.getDrone().getMaxPitchRate()));
			System.out.println("output hover2: " + output);
		} else {
			//System.out.println("hover recht");
			this.getDrone().setPitchRate(0);
			this.setHoverStarted(false);
		}
	}

	//////////Getters & Setters//////////

	public boolean isHoverStarted() {
		return hoverStarted;
	}

	public void setHoverStarted(boolean hoverStarted) {
		this.hoverStarted = hoverStarted;
	}

	public PitchController getPitchPI() {
		return pitchPI;
	}
}