package oldClasses.graphres;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import simulator.world.World;

public class GeneralCameraAP {
	
	private float eyeX=0, eyeY=0, eyeZ =0;
	private float lookAtX=0, lookAtY=0, lookAtZ=0;
	private float upX=0, upY=0, upZ=0;
	private float startEyeX=0, startEyeY=0, startEyeZ =0;
	private float startLookAtX=0, startLookAtY=0, startLookAtZ=0;
	private float startUpX=0, startUpY=0, startUpZ=0;
	private float yaw = 0;
	private boolean changeXP, changeYP, changeZP, changeXN, changeYN, changeZN, rotateP, rotateN;
	
	public GeneralCameraAP(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX, float upY, float upZ, WorldAPVisual world){
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
		this.world = world;
	}
	
	//TODO voeg translatie/rotatie toe van de vaste camera's!!
	
	private WorldAPVisual world;
	
	protected WorldAPVisual getWorld() {
		return world;
	}
	
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
	
	public void setYaw(float y) {
		this.yaw = y;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public void setChangeXP(boolean x) {
		this.changeXP = x;
	}

	public void setChangeYP(boolean y) {
		this.changeYP = y;
	}

	public void setChangeZP(boolean z) {
		this.changeZP = z;
	}
	
	public void setChangeXN(boolean x) {
		this.changeXN = x;
	}

	public void setChangeYN(boolean y) {
		this.changeYN = y;
	}

	public void setChangeZN(boolean z) {
		this.changeZN = z;
	}
	
	public void setRotateP(boolean r) {
		rotateP = r;
	}
	
	public void setRotateN(boolean r) {
		rotateN = r;
	}
	
	public void setCamera(GL2 gl, GLU glu) {
		int height = getWorld().getDrawable().getSurfaceHeight();
		int width = getWorld().getDrawable().getSurfaceWidth();
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
	
		// Perspective.
		float widthHeightRatio = (float) width / (float) height;
		glu.gluPerspective(120, widthHeightRatio, 0.01, 500);
		glu.gluLookAt(getEyeX(), getEyeY(), getEyeZ(), getLookAtX()*Math.cos(Math.toRadians(yaw)), getLookAtY(), getLookAtX()*Math.sin(Math.toRadians(yaw)), getUpX(), getUpY(),
				getUpZ());
	
		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void update(float dt) {
		float distance = dt;
		if (changeXP)
			eyeX += distance;
		if (changeYP)
			eyeY += distance;
		if (changeZP)
			eyeZ += distance;
		if (changeXN)
			eyeX -= distance;
		if (changeYN)
			eyeY -= distance;
		if (changeZN)
			eyeZ -= distance;
		if (rotateP)
			yaw += distance*3;
		if (rotateN)
			yaw -= distance*3;
	}

}
