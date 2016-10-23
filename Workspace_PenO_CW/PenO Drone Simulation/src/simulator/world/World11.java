package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import simulator.objects.Camera;
import simulator.objects.Drone;
import simulator.objects.Sphere;

public class World11 extends GLCanvas implements GLEventListener {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The GL unit (helper class). */
	private GLU glu;

	/** The frames per second setting. */
	private int fps = 60;

	/** The OpenGL animator. */
	private FPSAnimator animator;

	private boolean setup;
	private Drone drone1;

	/**
	 * @param capabilities
	 *            The GL capabilities.
	 * @param width
	 *            The window width.
	 * @param height
	 *            The window height.
	 */
	public World11(GLCapabilities capabilities, int width, int height) {
		addGLEventListener(this);
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

	/**
	 * Sets up the screen.
	 * 
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(gl);
		// Enable z- (depth) buffer for hidden surface removal.
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);

		// Enable smooth shading.
		gl.glShadeModel(GL2.GL_SMOOTH);

		// Define "clear" color.
		gl.glClearColor(1f, 1f, 1f, 0.5f);

		// We want a nice perspective.
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

		// Create GLU.
		glu = new GLU();

		// Start animator.
		animator = new FPSAnimator(this, fps);
		animator.start();
	}

	/**
	 * The only method that you should implement by yourself.
	 * 
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */

	public static Camera camera = new Camera();

	public void display(GLAutoDrawable drawable) {
		if (!animator.isAnimating()) {
			return;
		}
		final GL2 gl = drawable.getGL().getGL2();

		// Clear screen.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// Set camera.
		setCamera(gl, glu, 200);

		// translate camera.
		camera.update();
		gl.glTranslated(camera.getX(), camera.getY(), camera.getZ());
		gl.glRotated(camera.getRotateX(), 1, 0, 0);
		gl.glRotated(camera.getRotateY(), 0, 1, 0);
		gl.glRotated(camera.getRotateZ(), 0, 0, 1);
		
		// Input Sphere.
		double[] translateSphere = { 0, 20, 0 };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, 6.378f, 64, 64, colorSphere, translateSphere);
		sphere1.drawSphere();
		
		// Input Drone.
		if (!setup) {
			double[] translateDrone = { 0, 25, 0 };
			float[] colorDrone = { 0f, 0f, 1f };
			Drone drone1 = new Drone(gl, 3.378f, 6.378f, 40, 40, colorDrone, translateDrone);
			this.drone1 = drone1;
			drone1.drawDrone();
			setup = true;
		} else {
			drone1.getPhysics().calculateMovement();
			drone1.drawDrone();
		}

	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		throw new UnsupportedOperationException("Changing display is not supported.");
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

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}
}
