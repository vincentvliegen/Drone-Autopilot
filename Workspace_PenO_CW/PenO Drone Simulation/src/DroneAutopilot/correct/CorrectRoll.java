package DroneAutopilot.correct;

import DroneAutopilot.controllers.RollController;
import p_en_o_cw_2016.Drone;

public class CorrectRoll extends Correct{

	private final RollController rollPI;
	private boolean rollStarted;
	private boolean rollStartedFlying;
	private final float rollRate;

	public CorrectRoll(Drone drone) {
		super(drone);
		this.rollPI = new RollController(4,1);
		this.rollRate = this.getDrone().getMaxRollRate();
	}

	public void correctRoll() {
		if (this.getDrone().getRoll() <= Correct.getUpperboundary() && this.getDrone().getRoll() >= Correct.getUnderboundary()) {
			this.getDrone().setRollRate(0);
			this.setRollStarted(false);
		}
		else if (this.getDrone().getRoll() > Correct.getUpperboundary()){
			if(!this.isRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.max(output, -this.getRollRate()));
			}
		}
		else if (this.getDrone().getRoll() < Correct.getUnderboundary()){
			if(!this.isRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.min(output, this.getRollRate()));
			}
		}
	}
	
	public void correctRollFlying(float[] cogL, float[] cogR) {
		if (this.getDrone().getRoll() <= Correct.getUpperboundary() && this.getDrone().getRoll() >= Correct.getUnderboundary()) {
			this.getDrone().setRollRate(0);
			this.setRollStartedFlying(false);
		}
		else if (this.getDrone().getRoll() > Correct.getUpperboundary()){
			if(!this.isRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStartedFlying(true);
			}else{
				float output = -this.getRollPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogL, cogR), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.max(output, -this.getRollRate()));
			}
		}
		else if (this.getDrone().getRoll() < Correct.getUnderboundary()){
			if(!this.isRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStartedFlying(true);
			}else{
				float output = -this.getRollPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogL, cogR), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.min(output, this.getRollRate()));
			}
		}
	}

	
	//////////Getters & Setters//////////

	public final RollController getRollPI() {
		return this.rollPI;
	}

	public boolean isRollStarted() {
		return rollStarted;
	}

	public void setRollStarted(boolean rollStarted) {
		this.rollStarted = rollStarted;
	}

	public float getRollRate() {
		return rollRate;
	}

	public boolean isRollStartedFlying() {
		return rollStartedFlying;
	}

	public void setRollStartedFlying(boolean rollStartedFlying) {
		this.rollStartedFlying = rollStartedFlying;
	}
}