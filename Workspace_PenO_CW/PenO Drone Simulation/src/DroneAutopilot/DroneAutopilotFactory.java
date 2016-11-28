package DroneAutopilot;

import DroneAutopilot.GUI.GUI;
import DroneAutopilot.GUI.GraphPI;
import p_en_o_cw_2016.AutopilotFactory;
import p_en_o_cw_2016.Drone;

public class DroneAutopilotFactory implements AutopilotFactory{

	/** Called by the testbed in the AWT/Swing GUI thread to create and start an Autopilot.
    At this point, the drone exists in the virtual world, with zero pitch and roll. The
    Autopilot can request initial camera images and set initial values of thrust and
    rotation rates. It can also set up a Swing GUI. Simulated time is frozen for the
    duration of this method call; simulated time starts running after this call returns.
    The Drone and Camera objects are not thread-safe; calls of methods of these
    objects should occur only in the AWT/Swing GUI thread. */
	@Override
	public DroneAutopilot create(Drone drone) {		
		DroneAutopilot autopilot = new DroneAutopilot(drone);
		GUI gui = new GUI();
		autopilot.getMoveToTarget().setGUI(gui);
		//GraphPI graphPI = new GraphPI();
		//autopilot.getMoveToTarget().setGraphPI(graphPI);
		drone.setThrust(Math.abs(drone.getGravity()*drone.getWeight()));
		drone.setPitchRate(0);
		drone.setYawRate(0);
		drone.setRollRate(0);
		return autopilot;
	}

}
