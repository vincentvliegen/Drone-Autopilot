package DroneAutopilot.mission;

import DroneAutopilot.DroneAutopilot;

public class FlyToPosition extends Mission{
	
	
	public double[] previousPosition;
	public double[] target;
	
	public FlyToPosition(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
		this.setTarget(new double[] {3,0,1});
	}
	
	@Override
	public void execute() {
		this.getPhysicsCalculations().updateMovement(this.getTarget());
//		if(getPhysicsCalculations().getRollRate() != 0){
//			System.out.println("----");
//			System.out.println("currentYaw: " + this.getPhysicsCalculations().getDrone().getHeading());
//			System.out.println("currentPitch: " + this.getPhysicsCalculations().getDrone().getPitch());
//			System.out.println("currentRoll: " + this.getPhysicsCalculations().getDrone().getRoll());	
//			System.out.println("yawrate: " + this.getPhysicsCalculations().getYawRate());
//			System.out.println("pitchrate: " + this.getPhysicsCalculations().getPitchRate());
//			System.out.println("rollrate: " + this.getPhysicsCalculations().getRollRate());
//			System.out.println("----");
//		}
	}

	@Override
	public void updateGUI() {
		// TODO Auto-generated method stub
		
	}

	
	//////////GETTERS & SETTERS//////////

	public double[] getTarget() {
		return target;
	}

	public void setTarget(double[] target) {
		this.target = target;
	}

	
}
