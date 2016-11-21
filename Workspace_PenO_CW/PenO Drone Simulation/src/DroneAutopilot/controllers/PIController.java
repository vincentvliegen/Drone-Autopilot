package DroneAutopilot.controllers;

public abstract class PIController {
	
	private double Kp;
	private double Ki;
	private float setpoint;
	private float integraal = 0;
	private float currTime;
	private float lastTime;
	
	public PIController(double Kp, double Ki){
		this.Kp = Kp;
		this.Ki = Ki;
	}
	
	public void resetSetpoint(float setpoint){
		this.setpoint = setpoint;
		this.integraal = 0;
		
	}
	
	public float calculateRate(float value, float currentTime){
		this.currTime = currentTime;
		if (this.lastTime == 0){
			this.lastTime = this.currTime;
			return 0;
		}
		float error = this.setpoint - value;
		float dt = this.currTime - this.lastTime;
		this.integraal += error*dt;
		this.lastTime = this.currTime;
		float output = (float) (this.Kp*error + this.Ki * this.integraal);
		return output;
	}
}
