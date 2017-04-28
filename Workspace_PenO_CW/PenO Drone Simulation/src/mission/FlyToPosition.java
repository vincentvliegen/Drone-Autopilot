package mission;

import DroneAutopilot.DroneAutopilot;

public class FlyToPosition extends Mission{
	
	
	public double[] previousPosition;
	public double[] target;
	
	public FlyToPosition(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
		this.setTarget(new double[] {0,-1,0});
	}
	
	@Override
	public void execute() {
		this.getPhysicsCalculations().updatePosition(this.getTarget());
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
