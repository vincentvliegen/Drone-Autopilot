package DroneAutopilot;

public class MoveToTarget {

	public MoveToTarget(){
		
	}
	
	
	public void setPitchRate(float value){
		this.pitchRate = value;
	}
	
	public float getPitchRate(){
		return this.pitchRate;
	}
	
	private float pitchRate;
	
	public void setYawRate(float value){
		this.yawRate = value;
	}
	
	public float getYawRate(){
		return this.yawRate;
	}
	
	private float yawRate;
	
	public void setRollRate(float value){
		this.rollRate = value;
	}
	
	public float getRollRate(){
		return this.rollRate;
	}
	
	private float rollRate;
	
}
