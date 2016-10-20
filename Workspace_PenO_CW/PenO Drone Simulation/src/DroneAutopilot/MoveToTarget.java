package DroneAutopilot;

import java.util.ArrayList;

public class MoveToTarget {

	public MoveToTarget(){
		
	}
	
	public void checkcasespixelsfound(ArrayList leftcamera, ArrayList rightcamera){
		if (leftcamera.isEmpty() && rightcamera.isEmpty())
			noTargetFound();
		else if (!leftcamera.isEmpty() && rightcamera.isEmpty())
			leftCameraFoundTarget();
		else if (leftcamera.isEmpty() && !rightcamera.isEmpty())
			rightCameraFoundTarget();
		else if (!leftcamera.isEmpty() && !rightcamera.isEmpty())
			targetVisible();
	}
	
	public void noTargetFound(){
		
	}
	
	public void leftCameraFoundTarget(){
		
	}
	
	public void rightCameraFoundTarget(){
		
	}
	
	public void targetVisible(){
		
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
