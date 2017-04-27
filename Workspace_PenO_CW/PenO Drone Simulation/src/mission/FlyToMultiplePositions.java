package mission;

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
		// TODO Auto-generated constructor stub
		listOfTargets.add(new double[]{3,0,0});
		listOfTargets.add(new double[]{5,2,2});
		listOfTargets.add(new double[]{10,-5,-5});
		this.setTarget(new double[]{3,0,2});
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if (this.getDrone().getX()<= this.getTarget()[0]+cte && this.getDrone().getX()>=this.getTarget()[0]-cte &&
				this.getDrone().getY()<= this.getTarget()[1]+cte && this.getDrone().getY()>=this.getTarget()[1]-cte &&
				this.getDrone().getZ()<= this.getTarget()[2]+cte && this.getDrone().getZ()>=this.getTarget()[2]-cte &&
				listOfTargets.size()>1){
			listOfTargets.remove(0);
			this.setTarget(listOfTargets.get(0));
			System.out.println("change target" + Arrays.toString(this.getTarget()));
		}
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
