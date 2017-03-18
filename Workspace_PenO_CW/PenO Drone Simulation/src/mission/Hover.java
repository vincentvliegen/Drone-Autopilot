package mission;

import DroneAutopilot.DroneAutopilot;

public class Hover extends Mission{
	
	public float[] previousPosition;
	public float[] target;
	
	public Hover(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
	}

	@Override
	public void execute() {
		if(this.getDroneAutopilot().isFirstHover()){
			this.setTarget(new float[] {this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ()});
		}
		this.getPhysicsCalculations().updatePosition(this.getTarget()); //blijf opdezelfde plaats
		//this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());//blijf dezelfde richting kijken
	}

	@Override
	public void updateGUI() {
		// TODO Auto-generated method stub
		
	}
	
	//////////GETTERS & SETTERS//////////

	public float[] getTarget() {
		return target;
	}

	public void setTarget(float[] target) {
		this.target = target;
	}
	
	

}
