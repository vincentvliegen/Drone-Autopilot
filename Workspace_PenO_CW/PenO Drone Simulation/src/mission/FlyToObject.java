package mission;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.algoritmes.NewWorldScan;

public class FlyToObject extends Mission{

	private NewWorldScan worldScan;
	private double[] target;
	private boolean targetFound;

	public FlyToObject(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
		setWorldScan(new NewWorldScan(this.getDroneAutopilot()));
		setTargetFound(false);
	}

	@Override
	public void execute() {
		if(isTargetFound()){//we kennen target
			this.getPhysicsCalculations().updateMovement(getTarget());
		}else{//we kennen target niet
			this.getWorldScan().scan();
			if(this.getWorldScan().isFinished()){//target gevonden
				setTargetFound(true);
				setTarget(getPositionOfObject());
				this.getPhysicsCalculations().updateMovement(getTarget());
			}else{//target niet gevonden
				this.getPhysicsCalculations().updateMovement(this.getPhysicsCalculations().getPosition(),this.getWorldScan().getNewDirectionOfView());
			}
		}
	}

		public double[] getPositionOfObject(){
			//list of all cogs of triangles, fly to middle cog in list;
			HashMap<float[],ArrayList<double[]>> listColorAndCOG = this.getWorldScan().getColorAndCogs();
			int size = listColorAndCOG.size();
			float[][] keys = listColorAndCOG.keySet().toArray(new float[size][]);
			ArrayList<double[]> cogs = listColorAndCOG.get(keys[size/2]);
			return this.getPhysicsCalculations().calculatePositionObject(cogs.get(0), cogs.get(1));
		}
	
	@Override
	public void updateGUI() {//TODO
//		this.getGUI().update(this.getPhysicsCalculations().getDistanceDroneToPosition(getTarget()),0);
	}


	//////////GETTERS & SETTERS//////////

	public double[] getTarget() {
		return target;
	}

	public void setTarget(double[] target) {
		this.target = target;
	}

	public NewWorldScan getWorldScan() {
		return worldScan;
	}

	public void setWorldScan(NewWorldScan worldScan) {
		this.worldScan = worldScan;
	}

	public boolean isTargetFound() {
		return targetFound;
	}

	public void setTargetFound(boolean targetFound) {
		this.targetFound = targetFound;
	}

}
