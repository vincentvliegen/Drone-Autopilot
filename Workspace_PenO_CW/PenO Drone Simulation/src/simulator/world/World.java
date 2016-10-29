package simulator.world;

import java.util.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;

@SuppressWarnings("serial")
public abstract class World extends GLCanvas implements GLEventListener {
	
	/*
	 * Superklasse voor alle werelden die we nog moeten maken
	 */
	
	//TODO
		//physics?
		//movement?
		
	private List<GeneralCamera> generalCameras = new ArrayList<>();
	private List<DroneCamera> droneCameras = new ArrayList<>();
	private List<SimulationDrone> drones = new ArrayList<>();
	private List<Sphere> spheres = new ArrayList<>();
	private float currentTime = 0;
	private float startTime;
	private float lastTime;
	
	private GLU glu;
	
	public List<GeneralCamera> getGeneralCameras() {
		return generalCameras;
	}
	
	public float getCurrentTime() {
		currentTime = (float) (System.nanoTime()*Math.pow(10, -9) - this.startTime);
		return currentTime; 
	}
	
	public float checkTimePassed() {
		return getCurrentTime() - lastTime;
	}
	
	public float getLastTime() {
		return this.lastTime;
	}
	
	public List<DroneCamera> getDroneCameras() {
		return droneCameras;
	}
	
	public List<SimulationDrone> getDrones() {
		return drones;
	}
	
	public List<Sphere> getSpheres() {
		return spheres;
	}
	
	public GLU getGlu() {
		return glu;
	}
	
	public void addGeneralCamera(GeneralCamera camera){
		generalCameras.add(camera);		
	}
	
	public void addDroneCamera(DroneCamera camera){
		droneCameras.add(camera);		
	}
	
	public void addSimulationDrone(SimulationDrone drone){
		drones.add(drone);		
	}
	
	public void addSphere(Sphere sphere){
		spheres.add(sphere);		
	}
	
	public void setLastTime(float value) {
		this.lastTime = value;
	}
	
	public float getStartTime() {
		return this.startTime;
	}
	
	public int getFps() {
		return animator.getFPS();
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
		this.startTime = (float) (System.nanoTime()*Math.pow(10, -9));
		this.lastTime = 0;
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
		animator = new FPSAnimator(this, fps, true);
		animator.start();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}
	

	
	
}
