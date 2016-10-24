package implementedClasses;

import implementedClasses.Autopilot;
import p_en_o_cw_2016.Drone;

public class AutopilotFactory implements p_en_o_cw_2016.AutopilotFactory{

	/** Called by the testbed in the AWT/Swing GUI thread to create and start an Autopilot.
    At this point, the drone exists in the virtual world, with zero pitch and roll. The
    Autopilot can request initial camera images and set initial values of thrust and
    rotation rates. It can also set up a Swing GUI. Simulated time is frozen for the
    duration of this method call; simulated time starts running after this call returns.
    The Drone and Camera objects are not thread-safe; calls of methods of these
    objects should occur only in the AWT/Swing GUI thread. */
	@Override
	public Autopilot create(Drone drone) {
		
		Autopilot autopilot = new Autopilot();
		drone.setThrust(-drone.getGravity());
		drone.setPitchRate(0);
		drone.setYawRate(0);
		drone.setRollRate(0);
		//TODO timehaspassed nodig?
//		autopilot.timeHasPassed();
		return autopilot;
	}

}
