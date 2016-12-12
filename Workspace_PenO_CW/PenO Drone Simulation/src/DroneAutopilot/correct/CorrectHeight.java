package DroneAutopilot.correct;

import DroneAutopilot.controllers.HeightController;
import p_en_o_cw_2016.Drone;

public class CorrectHeight extends Correct{

	private final HeightController heightPI;

	public CorrectHeight(Drone drone) {
		super(drone);
		this.heightPI = new HeightController(1,0);
	}

	public void correctHeight(float[] cogL, float[] cogR){
		float tanVA = (float) Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)));
		float tanPitch = (float) Math.tan(Math.toRadians(this.getDrone().getPitch()));
		//System.out.println("tanPitch: " + tanPitch);
		//System.out.println("tanVA: " + tanVA);
		if(tanVA==tanPitch){
			this.getDrone().setThrust((float) (this.getPhysicsCalculations().getThrust(cogL)));
		}
		else if((this.getDrone().getPitch() > 0 && tanVA <= 1.2*tanPitch && tanVA >= tanPitch && tanVA!=tanPitch)){
			this.getDrone().setThrust((float) (this.getPhysicsCalculations().getThrust(cogL)*0.8));
			//System.out.println("Lagere thurst");
		}else if((this.getDrone().getPitch() > 0 && tanVA <= tanPitch && tanVA >= 0.8*tanPitch && tanVA!=tanPitch)){
			this.getDrone().setThrust((float) (this.getPhysicsCalculations().getThrust(cogL)*1.2));
			//System.out.println("Hogere thurst");
		}else if( (this.getDrone().getPitch() < 0 && tanVA >= 1.2*tanPitch && tanVA <= tanPitch && tanVA!=tanPitch)){
			this.getDrone().setThrust((float) (this.getPhysicsCalculations().getThrust(cogL)*1.2));
			//System.out.println("Hogere thurst negatief");
		}else if( (this.getDrone().getPitch() < 0 && tanVA >= tanPitch && tanVA <= 0.8*tanPitch && tanVA!=tanPitch)){
			this.getDrone().setThrust((float) (this.getPhysicsCalculations().getThrust(cogL)*0.8));
			//System.out.println("Lagere thurst negatief");
		}else if ((this.getDrone().getPitch() > 0 && tanVA < 0.8*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA < 1.2*tanPitch)) {
			this.getHeightPI().resetSetpoint(tanVA);
			float output = this.getHeightPI().calculateRate((float) Math.tan(Math.toRadians(this.getDrone().getPitch())), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (output*10));
			//System.out.println("output: " + 10*output);
			//System.out.println("time: " + this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			//System.out.println("error: " + (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))));
			this.getDrone().setThrust(Math.max(this.getPhysicsCalculations().getThrust(cogL) + output, -this.getDrone().getMaxThrust()));
			//System.out.println((this.getPhysicsCalculations().getThrust(cogL) + output));
		}else if ((this.getDrone().getPitch() > 0 && tanVA > 1.2*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA > 0.8*tanPitch)){
			this.getHeightPI().resetSetpoint(tanVA);
			float output = this.getHeightPI().calculateRate((float) Math.tan(Math.toRadians(this.getDrone().getPitch())), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (output*10));
			//System.out.println("output: " + 10*output);
			//System.out.println("time: " + this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			//System.out.println("error: " + (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))));
			this.getDrone().setThrust(Math.min(this.getPhysicsCalculations().getThrust(cogL) + output, this.getDrone().getMaxThrust()));
			//System.out.println((this.getPhysicsCalculations().getThrust(cogL) + output));
		}	
	}

	//////////Getters & Setters//////////

	public final HeightController getHeightPI() {
		return this.heightPI;
	}
}