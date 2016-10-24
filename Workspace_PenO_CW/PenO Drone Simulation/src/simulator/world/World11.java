package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import simulator.movement.KeyboardMovement;
import simulator.objects.Drone;
import simulator.objects.Sphere;

public class World11 extends World {

	private static final long serialVersionUID = 1L;
	private boolean setup;
	private Drone drone1;

	
	public World11() {
		super();
	}

	/**
	 * @return Some standard GL capabilities (with alpha).
	 */
	public static GLCapabilities createGLCapabilities() {
		GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
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
		setCamera(gl, super.getGlu(), 200);

		// translate camera.
		movement.update();
		gl.glTranslated(movement.getX(), movement.getY(), movement.getZ());
		gl.glRotated(movement.getRotateX(), 1, 0, 0);
		gl.glRotated(movement.getRotateY(), 0, 1, 0);
		gl.glRotated(movement.getRotateZ(), 0, 0, 1);
		
		// Input Sphere.
		double[] translateSphere = { 0, 20, 0 };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, 6.378f, 64, 64, colorSphere, translateSphere);
		sphere1.drawSphere();
		
		// Input Drone.
		if (!setup) {
			double[] translateDrone = { 0, 25, 0 };
			float[] colorDrone = { 0f, 0f, 1f };
			Drone drone1 = new Drone(gl, 2.378f, 4.378f, 64, 64, colorDrone, translateDrone);
			this.drone1 = drone1;
			drone1.drawDrone();
			setup = true;
		} else {
			drone1.getPhysics().calculateMovement();
			drone1.drawDrone();
		}
	}

	private void setCamera(GL2 gl, GLU glu, float distance) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 20, distance, 0, 0, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
