package mission;

import DroneAutopilot.DroneAutopilot;

public class Hover extends Mission{
	
	public Hover(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
	}

	@Override
	public void execute() {
		this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());//blijf dezelfde richting kijken
	}

	@Override
	public void updateGUI() {
		// TODO Auto-generated method stub
		
	}

}
