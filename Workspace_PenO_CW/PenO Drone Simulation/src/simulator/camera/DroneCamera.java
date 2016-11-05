package simulator.camera;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

import p_en_o_cw_2016.Camera;
import simulator.objects.SimulationDrone;

public class DroneCamera extends GeneralCamera implements Camera{

	private GL2 gl;
	private GLAutoDrawable drawable;
	private SimulationDrone drone;
	private DroneCameraPlace place;

	public DroneCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX, float upY, float upZ, SimulationDrone drone, DroneCameraPlace place){
		super(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
		this.setDrone(drone);
		this.gl = getDrone().getWorld().getGL().getGL2();
		this.drawable = getDrone().getWorld().getDrawable();
		this.place = place;
	}

	public DroneCameraPlace getPlace() {
		return place;
	}
	
	public SimulationDrone getDrone() {
		return drone;
	}

	public void setDrone(SimulationDrone drone) {
		this.drone = drone;
	}

	@Override
	public float getHorizontalAngleOfView() {
		return 60f;
	}

	@Override
	public float getVerticalAngleOfView() {
		return 50f;
	}

	@Override
	public int getWidth() {
		return drawable.getSurfaceWidth();
	}

	@Override
	public int[] takeImage() {
		int height = drawable.getSurfaceHeight();
		int[] temp = new int[getWidth() * height ];
		ByteBuffer buffer = ByteBuffer.allocateDirect(3 *getWidth()*height);
		
		//TODO eventueel met switch werken
		
		if(getPlace() == DroneCameraPlace.LEFT)
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getDrone().getWorld().getFramebufferLeft()[0]);
		else
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getDrone().getWorld().getFramebufferRight()[0]);

		gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
		gl.glReadPixels(0, 0, getWidth(), height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer);
		buffer.rewind();
		int bpp = 3;
		int i = 0;


		for(int r = 0; r < height; r++) {
			for(int c = 0; c < getWidth(); c++) {
				i = ((height-r-1)*getWidth() + c) * bpp;
				int red = buffer.get(i) & 0xFF;
				int green = buffer.get(i + 1) & 0xFF;
				int blue = buffer.get(i + 2) & 0xFF;
				temp[r * getWidth() + c] = red|green<<8|blue<<16;

			}
		}
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		return temp;
	}

	public void setCamera(GL2 gl, GLU glu) {
		int height = drawable.getSurfaceHeight();
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) height;
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		//System.out.println("X " + getEyeX());
		//System.out.println("LookAtX " + getLookAtX());
		
		glu.gluLookAt(getEyeX(), getEyeY(), getEyeZ(), getLookAtX(), getLookAtY(), getLookAtZ(), getUpX(), getUpY(), getUpZ());

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void updateDroneCamera(){
		drone.createRotateMatrix();
		setEyeX(getEyeX() * new BigDecimal(drone.getRotateMatrix().get(0)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getEyeY() * new BigDecimal(drone.getRotateMatrix().get(1)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getEyeZ()* new BigDecimal(drone.getRotateMatrix().get(2)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setEyeY(getEyeX() * new BigDecimal(drone.getRotateMatrix().get(3)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getEyeY() * new BigDecimal(drone.getRotateMatrix().get(4)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getEyeZ()* new BigDecimal(drone.getRotateMatrix().get(5)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setEyeZ(getEyeX() * new BigDecimal(drone.getRotateMatrix().get(6)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getEyeY() * new BigDecimal(drone.getRotateMatrix().get(7)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getEyeZ()* new BigDecimal(drone.getRotateMatrix().get(8)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setLookAtX(getLookAtX() * new BigDecimal(drone.getRotateMatrix().get(0)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getLookAtY() * new BigDecimal(drone.getRotateMatrix().get(1)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getLookAtZ()* new BigDecimal(drone.getRotateMatrix().get(2)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setLookAtY(getLookAtX() * new BigDecimal(drone.getRotateMatrix().get(3)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getLookAtY() * new BigDecimal(drone.getRotateMatrix().get(4)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getLookAtZ()* new BigDecimal(drone.getRotateMatrix().get(5)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setLookAtZ(getLookAtX() * new BigDecimal(drone.getRotateMatrix().get(6)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue()  + getLookAtY() * new BigDecimal(drone.getRotateMatrix().get(7)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue() + getLookAtZ()* new BigDecimal(drone.getRotateMatrix().get(8)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setUpX(new BigDecimal(drone.getRotateMatrix().get(1)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setUpY(new BigDecimal(drone.getRotateMatrix().get(4)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
		setUpZ(new BigDecimal(drone.getRotateMatrix().get(7)).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue());
	}
}
