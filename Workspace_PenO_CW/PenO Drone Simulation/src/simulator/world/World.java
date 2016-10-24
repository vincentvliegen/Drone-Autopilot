package simulator.world;

import java.util.*;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;

@SuppressWarnings("serial")
public abstract class World extends GLCanvas implements GLEventListener {
	
	/*
	 * Superklasse voor alle werelden die we nog moeten maken
	 * 
	 * Bevat lijst van alle objecten die er bestaan (op dit moment enkel Sphere en Drone)
	 * Linken met de physics?
	 * 
	 * 
	 * 
	 */
	
	//TODO
		//physics?
		//movement?
		
	
	//TODO sets van maken? --> niet voor camera's want dan verlies je ordening!
	//Linked list?
	private GeneralCamera[] generalCameras = {};
	private SimulationDrone[] drones = {};
	private List<Sphere> spheres = new ArrayList<>();
	
	public GeneralCamera[] getGeneralCameras() {
		return generalCameras;
	}
	
	public SimulationDrone[] getDrones() {
		return drones;
	}
	
	public List<Sphere> getSpheres() {
		return spheres;
	}
	
	private GLU glu;
	
	public GLU getGlu() {
		return glu;
	}

	
	//TODO meegeven in constructor?
	/** The frames per second setting. */
	private int fps = 60;

	/** The OpenGL animator. */
	private FPSAnimator animator;
	
	public FPSAnimator getAnimator() {
		return animator;
	}
	
	public World() {
		addGLEventListener(this);
	}

	@Override
	public abstract void display(GLAutoDrawable drawable);

	@Override
	public void dispose(GLAutoDrawable drawable){
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

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}
	

	
	
}
