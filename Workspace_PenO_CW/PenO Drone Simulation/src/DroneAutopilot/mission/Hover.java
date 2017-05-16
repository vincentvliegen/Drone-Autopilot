package DroneAutopilot.mission;

import DroneAutopilot.DroneAutopilot;

public class Hover extends Mission{
	
	public double[] previousPosition;
	public double[] target;
	
	public Hover(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
	}

	@Override
	public void execute() {
		if(this.getDroneAutopilot().isFirstHover()){
			this.setTarget(new double[] {(double) this.getDrone().getX(), (double) this.getDrone().getY(), (double) this.getDrone().getZ()});
		}
		this.getPhysicsCalculations().updateMovement(this.getTarget()); //blijf opdezelfde plaats
		//this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());//blijf dezelfde richting kijken
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
