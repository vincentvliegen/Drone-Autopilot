package mission;

import DroneAutopilot.MoveToTarget;
import DroneAutopilot.WorldScan;

public abstract class Mission {

	private WorldScan worldscan;
	private MoveToTarget moveToTarget;
	
	public abstract void execute();
	
	public WorldScan getWorldscan() {
		return worldscan;
	}

	public MoveToTarget getMoveToTarget() {
		return moveToTarget;
	}

}
