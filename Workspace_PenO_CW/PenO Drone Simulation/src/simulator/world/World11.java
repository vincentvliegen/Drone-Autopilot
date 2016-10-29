package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;

import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;

public class World11 extends World {

	private static final long serialVersionUID = 1L;
	private boolean setup;
	private SimulationDrone drone1;
	private Sphere sphere1;

	public World11() {
		super();
		super.addGeneralCamera(new GeneralCamera(0, 20, 200, 0, 0, 0));
		super.addGeneralCamera(new GeneralCamera(0, 20, 50, 0, 0, 0));
		super.addGeneralCamera(new GeneralCamera(0, 20, 1000, 0, 0, 0));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	/**
	 * @return Some standard GL capabilities (with alpha).
	 */
	public static GLCapabilities createGLCapabilities() {
		GLCapabilities capabilities = new GLCapabilities(
				GLProfile.get(GLProfile.GL2));
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		return capabilities;
	}

	public static KeyboardMovement movement = new KeyboardMovement();

	public void display(GLAutoDrawable drawable) {
		if (!super.getAnimator().isAnimating()) {
			return;
		}
		final GL2 gl = drawable.getGL().getGL2();

		// Clear screen.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// Set camera.
		setCamera(gl, super.getGlu());
		
		// translate camera.
		movement.update();
		gl.glTranslated(movement.getX(), movement.getY(), movement.getZ());
		gl.glRotated(movement.getRotateX(), 1, 0, 0);
		gl.glRotated(movement.getRotateY(), 0, 1, 0);
		gl.glRotated(movement.getRotateZ(), 0, 0, 1);

		// Input Drone.
		if (!setup) {
			double[] translateDrone = { 0, 20, 0 };
			float[] colorDrone = { 0f, 0f, 1f };
			SimulationDrone drone1 = new SimulationDrone(gl, 2.378f, 4.378f,
					64, 64, colorDrone, translateDrone, this);
			this.drone1 = drone1;
			addSimulationDrone(drone1);
			drone1.drawDrone();
		} else {
			drone1.getMovement().calculateMovement();
			drone1.drawDrone();
		}

		// Input Sphere.
		if (!setup) {
			double[] translateSphere = { 0, -20, 0 };
			float[] colorSphere = { 1f, 0f, 0f };
			Sphere sphere1 = new Sphere(gl, 6.378f, 64, 64, colorSphere,
					translateSphere);
			sphere1.drawSphere();
			this.sphere1 = sphere1;
			addSphere(sphere1);
		} else {
			sphere1.drawSphere();
		}
		setup = true;
		super.delta = (float) (System.nanoTime()*Math.pow(10, -9) - super.startTime);
		//System.out.println(delta);
	}
	
	// Update position camera's
	public void setCamera(GL2 gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(currentCamera.getEyeX(), currentCamera.getEyeY(), currentCamera.getEyeZ(), currentCamera.getLookAtX(), currentCamera.getLookAtY(), currentCamera.getLookAtZ(), 0, -1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
