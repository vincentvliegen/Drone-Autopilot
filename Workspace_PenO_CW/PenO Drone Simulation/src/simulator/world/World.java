package simulator.world;

import java.util.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
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
	public GeneralCamera currentCamera;
	
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
	
	public GeneralCamera getCurrentCamera(){
		return currentCamera;
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

	public void setCurrentCamera(GeneralCamera camera){
		this.currentCamera = camera;
	}

	
	private GLAutoDrawable drawable;
	
	public GLAutoDrawable getDrawable() {
		return drawable;
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

	public int[] getFramebufferLeft() {
		return framebufferLeft;
	}
	public int[] getFramebufferRight() {
		return framebufferRight;
	}
	
	
	private int[] framebufferRight = new int[1];
	
	
	private int[] framebufferLeft = new int[1];
	
	int[] colorRenderbufferRight = new int[1];
	int[] depthRenderbufferRight = new int[1];
	int[] textureRight = new int[1];
	
	int[] colorRenderbufferLeft = new int[1];
	int[] depthRenderbufferLeft = new int[1];
	int[] textureLeft = new int[1];
	
	
	/**
	 * Sets up the screen.
	 * 
	 * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
	 */
	public void init(GLAutoDrawable drawable) {
		this.drawable = drawable;
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

		
		//FBO voor links
		
		gl.glGenFramebuffers(1, framebufferLeft, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebufferLeft[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, colorRenderbufferLeft[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);

		gl.glGenTextures(1, textureLeft, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureLeft[0]);
		
		//FBO voor rechts
		
		gl.glGenFramebuffers(1, framebufferRight, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebufferRight[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, colorRenderbufferRight[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);

		gl.glGenTextures(1, textureRight, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureRight[0]);
		
			
		
		//set to default buffer
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		
		
		// Start animator.
		drawable.getGL().setSwapInterval(1);
		animator = new FPSAnimator(this, fps, true);
		animator.start();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}
	
}
