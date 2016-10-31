package simulator.camera;

import java.nio.ByteBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;

import p_en_o_cw_2016.Camera;
import simulator.objects.SimulationDrone;

public class DroneCamera extends GeneralCamera implements Camera{

	private GL gl;
	private GLAutoDrawable drawable;
	private SimulationDrone drone;

	public DroneCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX, float upY, float upZ, SimulationDrone drone){
		super(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ);
		this.setDrone(drone);
		this.gl = getDrone().getWorld().getGL();
		this.drawable = getDrone().getWorld().getDrawable();
	}

	public SimulationDrone getDrone() {
		return drone;
	}

	public void setDrone(SimulationDrone drone) {
		this.drone = drone;
	}

	@Override
	public float getHorizontalAngleOfView() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVerticalAngleOfView() {
		// TODO Auto-generated method stub
		return 0;
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
		return temp;
	}
}
