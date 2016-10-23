package simulator.camera;

import simulator.objects.Drone;

public class DroneCamera {
	
	public DroneCamera(Drone drone){
		this.drone = drone;
	}
	
	public float getEyeX() {
		return eyeX;
	}
	public void setEyeX(float eyeX) {
		this.eyeX = eyeX;
	}

	private Drone drone;

	
	
	private float eyeX=0, eyeY=0, eyeZ =0;
	private float lookAtX=0, lookAtY=0, lookAtZ=0;
	
	
	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public float getEyeY() {
		return eyeY;
	}

	public void setEyeY(float eyeY) {
		this.eyeY = eyeY;
	}

	public float getEyeZ() {
		return eyeZ;
	}

	public void setEyeZ(float eyeZ) {
		this.eyeZ = eyeZ;
	}

	public float getLookAtX() {
		return lookAtX;
	}

	public void setLookAtX(float lookAtX) {
		this.lookAtX = lookAtX;
	}

	public float getLookAtY() {
		return lookAtY;
	}

	public void setLookAtY(float lookAtY) {
		this.lookAtY = lookAtY;
	}

	public float getLookAtZ() {
		return lookAtZ;
	}

	public void setLookAtZ(float lookAtZ) {
		this.lookAtZ = lookAtZ;
	}
	
	
	
	

}