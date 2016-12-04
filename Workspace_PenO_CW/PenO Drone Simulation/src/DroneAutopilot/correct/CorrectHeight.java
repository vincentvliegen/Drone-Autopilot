package DroneAutopilot.correct;

import DroneAutopilot.controllers.HeightController;
import p_en_o_cw_2016.Drone;

public class CorrectHeight extends Correct{

	private final HeightController heightPI;

	public CorrectHeight(Drone drone) {
		super(drone);
		this.heightPI = new HeightController(3,0);
	}

	public void correctHeight(float[] cogL, float[] cogR){
		float tanVA = (float) Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)));
		float tanPitch = (float) Math.tan(Math.toRadians(this.getDrone().getPitch()));
		if((this.getDrone().getPitch() > 0 && tanVA <= 1.1*tanPitch && tanVA >= 0.9*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA >= 1.1*tanPitch && tanVA <= 0.9*tanPitch)){
			this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
		}else if ((this.getDrone().getPitch() > 0 && tanVA < 0.9*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA < 1.1*tanPitch)) {
			this.getHeightPI().resetSetpoint(tanPitch);
			float output = -this.getHeightPI().calculateRate((float)(Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() + Math.max(output, -this.getDrone().getMaxThrust()));
		}else if ((this.getDrone().getPitch() > 0 && tanVA > 1.1*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA > 0.9*tanPitch)){
			this.getHeightPI().resetSetpoint(tanPitch);
			float output = -this.getHeightPI().calculateRate((float)(Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()+Math.min(output, this.getDrone().getMaxThrust()));
		}	
	}

	//////////Getters & Setters//////////

	public final HeightController getHeightPI() {
		return this.heightPI;
	}
}