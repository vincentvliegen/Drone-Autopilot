package mission;

import DroneAutopilot.DroneAutopilot;

public class Hover extends Mission{
	
	public float[] previousPosition;
	
	public Hover(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
		this.setPreviousPosition(new float[] {this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ()}); 
	}

	@Override
	public void execute() {
		this.getPhysicsCalculations().updatePosition(this.getPreviousPosition()); //blijf opdezelfde plaats
		this.setPreviousPosition(new float[] {this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ()});
		//this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());//blijf dezelfde richting kijken
	}

	@Override
	public void updateGUI() {
		// TODO Auto-generated method stub
		
	}
	
	//////////GETTERS & SETTERS//////////

	public float[] getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(float[] previousPosition) {
		this.previousPosition = previousPosition;
	}
	
	

}
