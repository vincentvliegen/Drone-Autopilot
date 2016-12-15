package DroneAutopilot.correct;

import DroneAutopilot.controllers.YawController;
import p_en_o_cw_2016.Drone;

public class CorrectYaw extends Correct{

	private final YawController yawPI;
	private boolean yawStarted;
	private final float yawRate;

	public CorrectYaw(Drone drone) {
		super(drone);
		this.yawPI = new YawController(3,1);
		this.yawRate = this.getDrone().getMaxYawRate()/4;
	}

	public void correctYaw(float[] cogLeft, float[] cogRight){
		if(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= Correct.getUnderboundary() 
				&& this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= Correct.getUpperboundary()){
			this.setYawStarted(false);
			this.getDrone().setYawRate(0);
		}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) < Correct.getUnderboundary()) {
			if(!this.isYawStarted()){
				this.getYawPI().resetSetpoint(0);
				this.setYawStarted(true);
			}else{
				float output = -this.getYawPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) this.getDrone().getCurrentTime(), (int) this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight));
				this.getDrone().setYawRate(Math.max(output, -this.getYawRate()));
			}
		} 
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) > Correct.getUpperboundary()){
			if(!this.isYawStarted()){
				this.getYawPI().resetSetpoint(0);
				this.setYawStarted(true);
			}else{
				float output = -this.getYawPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight)*10));
				this.getDrone().setYawRate(Math.min(output, this.getYawRate()));
			}
		}	
	}

	
	//////////Getters & Setters//////////

	public YawController getYawPI() {
		return yawPI;
	}
	
	public boolean isYawStarted() {
		return yawStarted;
	}

	public void setYawStarted(boolean yawStarted) {
		this.yawStarted = yawStarted;
	}
	
	public float getYawRate() {
		return yawRate;
	}
}