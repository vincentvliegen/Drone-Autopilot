package DroneAutopilot.controllers;

public class ThrustController extends PIController{
	
	public ThrustController(double Kp, double Ki, float currentTime) {
		super(Kp, Ki, currentTime);
	}
}
