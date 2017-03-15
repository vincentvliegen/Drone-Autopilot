package mission;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.algoritmes.NewWorldScan;

public class OneSphere extends Mission{

	private NewWorldScan worldScan;
	private float[] target;
	private boolean targetFound;

	public OneSphere(DroneAutopilot droneAutopilot){
		super(droneAutopilot);
		setWorldScan(new NewWorldScan(this.getPhysicsCalculations()));
		setTargetFound(false);
	}

	@Override
	public void execute() {
		if(isTargetFound()){
			this.getPhysicsCalculations().updatePosition(getTarget());
		}else{
			this.getWorldScan().scan();
			if(this.getWorldScan().isFinished()){
				HashMap<float[],ArrayList<float[]>> listColorAndCOG = this.getWorldScan().getColorAndCogs();
				setTargetFound(true);
				int size = listColorAndCOG.size();
				float[][] keys = listColorAndCOG.keySet().toArray(new float[size][]);
				ArrayList<float[]> cogs = listColorAndCOG.get(keys[size/2]);
				setTarget(this.getPhysicsCalculations().calculatePositionObject(cogs.get(0), cogs.get(1)));
				this.getPhysicsCalculations().updatePosition(getTarget());
			}else{
				this.getPhysicsCalculations().updateOrientation(this.getWorldScan().getNewDirectionOfView());
			}
		}
	}

	@Override
	public void updateGUI() {
		this.getGUI().update(this.getPhysicsCalculations().getDistanceDroneToPosition(getTarget()),0);//TODO iets voor de kleur
	}


	//////////GETTERS & SETTERS//////////

	public float[] getTarget() {
		return target;
	}

	public void setTarget(float[] target) {
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
