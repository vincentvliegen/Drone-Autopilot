package simulator.camera;

public class GeneralCamera {
	
	private float eyeX=0, eyeY=0, eyeZ =0;
	private float lookAtX=0, lookAtY=0, lookAtZ=0;
	private float upX=0, upY=0, upZ=0;
	private float startEyeX=0, startEyeY=0, startEyeZ =0;
	private float startLookAtX=0, startLookAtY=0, startLookAtZ=0;
	private float startUpX=0, startUpY=0, startUpZ=0;
	
	public GeneralCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX, float upY, float upZ){
		this.setEyeX(eyeX);
		this.startEyeX = eyeX;
		this.setEyeY(eyeY);
		this.startEyeY = eyeY;
		this.setEyeZ(eyeZ);
		this.startEyeZ = eyeZ;
		this.setLookAtX(lookAtX);
		this.startLookAtX = lookAtX;
		this.setLookAtY(lookAtY);
		this.startLookAtY = lookAtY;
		this.setLookAtZ(lookAtZ);
		this.startLookAtZ = lookAtZ;
		this.setUpX(upX);
		this.startUpX = upX;
		this.setUpY(upY);
		this.startUpY = upY;
		this.setUpZ(upZ);
		this.startUpZ = upZ;
	}
	
	//TODO voeg translatie/rotatie toe van de vaste camera's!!
	
	public void setUpX(float upX) {
		this.upX = upX;
	}

	public void setUpY(float upY) {
		this.upY = upY;
	}

	public void setUpZ(float upZ) {
		this.upZ = upZ;
	}

	public void setEyeX(float eyeX) {
		this.eyeX = eyeX;
	}

	public void setEyeY(float eyeY) {
		this.eyeY = eyeY;
	}

	public void setEyeZ(float eyeZ) {
		this.eyeZ = eyeZ;
	}

	public void setLookAtX(float lookAtX) {
		this.lookAtX = lookAtX;
	}

	public void setLookAtY(float lookAtY) {
		this.lookAtY = lookAtY;
	}

	public void setLookAtZ(float lookAtZ) {
		this.lookAtZ = lookAtZ;
	}

	public float getUpX() {
		return upX;
	}

	public float getUpY() {
		return upY;
	}

	public float getUpZ() {
		return upZ;
	}

	public float getEyeX() {
		return eyeX;
	}
	public float getEyeY() {
		return eyeY;
	}

	public float getEyeZ() {
		return eyeZ;
	}

	public float getLookAtX() {
		return lookAtX;
	}

	public float getLookAtY() {
		return lookAtY;
	}

	public float getLookAtZ() {
		return lookAtZ;
	}

	public float getStartUpX() {
		return startUpX;
	}

	public float getStartUpY() {
		return startUpY;
	}

	public float getStartUpZ() {
		return startUpZ;
	}


	public float getStartEyeX() {
		return startEyeX;
	}

	public float getStartEyeY() {
		return startEyeY;
	}

	public float getStartEyeZ() {
		return startEyeZ;
	}

	public float getStartLookAtX() {
		return startLookAtX;
	}

	public float getStartLookAtY() {
		return startLookAtY;
	}

	public float getStartLookAtZ() {
		return startLookAtZ;
	}

}
