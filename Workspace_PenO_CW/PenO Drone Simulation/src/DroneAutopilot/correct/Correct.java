package DroneAutopilot.correct;

import DroneAutopilot.GUI.GraphPI;
import DroneAutopilot.calculations.PhysicsCalculations;
import p_en_o_cw_2016.Drone;

public abstract class Correct {
	private Drone drone;
	private final PhysicsCalculations physicsCalculations;
	private GraphPI graphPI;

	private static final float underBoundary = -0.1f;
	private static final float upperBoundary = 0.1f;

	public Correct(Drone drone){
		this.setDrone(drone);
		this.physicsCalculations = new PhysicsCalculations(drone);
	}

	public void updategraphPI(int x, int y){
		this.getGraphPI().update(x,y);
	}


	//////////Getters & Setters//////////

	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}

	public static float getUnderboundary() {
		return underBoundary;
	}

	public static float getUpperboundary() {
		return upperBoundary;
	}

	public void setGraphPI(GraphPI graphPID) {
		this.graphPI = graphPID;
	}

	public GraphPI getGraphPI() {
		return this.graphPI;
	}
}