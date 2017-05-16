package DroneAutopilot.mission;

import java.util.ArrayList;
import java.util.Arrays;

import DroneAutopilot.DroneAutopilot;

public class FlyToMultiplePositions extends Mission {
	
	public double[] previousPosition;
	private double[] target;
	private ArrayList<double[]> listOfTargets = new ArrayList<double[]>();
	private static final double cte = 0.1;
	
	public FlyToMultiplePositions(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
		listOfTargets.add(new double[]{3,0,2});
		listOfTargets.add(new double[]{3,0,0});
		listOfTargets.add(new double[]{5,2,2});
		listOfTargets.add(new double[]{10,-5,-5});
		listOfTargets.add(new double[]{0,0,0});
		this.setTarget(listOfTargets.get(0));
	}

	@Override
	public void execute() {
		double distance = getPhysicsCalculations().getDistanceDroneToPosition(getTarget());
		if(distance <= cte && listOfTargets.size()>1){		
			double[] removedTarget = listOfTargets.remove(0);
			this.setTarget(listOfTargets.get(0));
			System.out.println("Changed target from " + Arrays.toString(removedTarget) + " to: " + Arrays.toString(this.getTarget()));
		}
		this.getPhysicsCalculations().updateMovement(this.getTarget());
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
