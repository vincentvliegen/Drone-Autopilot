package simulator.camera;

public class GeneralCamera {
	
	private float eyeX=0, eyeY=0, eyeZ =0;
	private float lookAtX=0, lookAtY=0, lookAtZ=0;
	private float upX=0, upY=0, upZ=0;
	
	public GeneralCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX, float upY, float upZ){
		this.setEyeX(eyeX);
		this.setEyeY(eyeY);
		this.setEyeZ(eyeZ);
		this.setLookAtX(lookAtX);
		this.setLookAtY(lookAtY);
		this.setLookAtZ(lookAtZ);
		this.setUpX(upX);
		this.setUpY(upY);;
		this.setUpZ(upZ);
	}
	
	//TODO voeg translatie/rotatie toe van de vaste camera's!!
	
	public float getUpX() {
		return upX;
	}

	public void setUpX(float upX) {
		this.upX = upX;
	}

	public float getUpY() {
		return upY;
	}

	public void setUpY(float upY) {
		this.upY = upY;
	}

	public float getUpZ() {
		return upZ;
	}

	public void setUpZ(float upZ) {
		this.upZ = upZ;
	}

	public float getEyeX() {
		return eyeX;
	}
	public void setEyeX(float eyeX) {
		this.eyeX = eyeX;
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
