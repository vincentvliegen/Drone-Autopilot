package implementedClasses;

public class Autopilot implements p_en_o_cw_2016.Autopilot{

	/** Called by the testbed in the AWT/Swing GUI thread at a high (but possibly variable)
    frequency, after simulated time has advanced and the simulated world has been
    updated to a new state. Simulated time is frozen for the duration of this call. */
	@Override
	public void timeHasPassed() {
		// TODO Auto-generated method stub
		
	}

	public void setDrone(Drone drone){
		this.drone = drone;
	}
	
	public Drone getDrone(){
		return this.drone;
	}
	
	private Drone drone;
	
}
