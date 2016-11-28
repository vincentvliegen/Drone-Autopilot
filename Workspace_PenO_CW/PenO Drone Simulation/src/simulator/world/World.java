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
import simulator.movement.KeyboardMovement;
import simulator.objects.ObstacleSphere;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;
import simulator.parser.Parser;
import simulator.physics.Physics;

@SuppressWarnings("serial")
public abstract class World extends GLCanvas implements GLEventListener {

	/*
	 * Superklasse voor alle werelden aan te maken werelden
	 */



	private List<GeneralCamera> generalCameras = new ArrayList<>();
	private List<DroneCamera> droneCameras = new ArrayList<>();
	private List<SimulationDrone> drones = new ArrayList<>();
	private List<Sphere> spheres = new ArrayList<>();
	private List<ObstacleSphere> obstacleSpheres = new ArrayList<>();
	private long startTime;
	private long lastTime;
	private double timePassed = 0;
	private double currentTime = 0;
	private GLU glu;

	//TODO verplaatsen
	private Parser parser;
	public Parser getParser() {
		return parser;
	}


	private int[] framebufferRight = new int[1];
	private int[] framebufferLeft = new int[1];
	//TODO meegeven in constructor?
	/** The frames per second setting. */
	private int fps = 60;
	/** The OpenGL animator. */
	private FPSAnimator animator;
	private GLAutoDrawable drawable;
	private GeneralCamera currentCamera;
	int[] colorRenderbufferRight = new int[1];
	int[] depthRenderbufferRight = new int[1];
	int[] textureRight = new int[1];
	int[] colorRenderbufferLeft = new int[1];
	int[] depthRenderbufferLeft = new int[1];
	int[] textureLeft = new int[1];
	Physics physics;
	private List<WorldObject> worldObjectsList = new ArrayList<>();
	public static KeyboardMovement movement = new KeyboardMovement();
	private double xWindRotation = 0;
	private double yWindRotation = 0;
	private double zWindRotation = 0;
	private double xWindSpeed = 0;
	private double yWindSpeed = 0;
	private double zWindSpeed = 0;

	public World() {
		addGLEventListener(this);
	}

	public void addGeneralCamera(GeneralCamera camera){
		generalCameras.add(camera);		
	}

	public void addDroneCamera(DroneCamera camera){
		droneCameras.add(camera);		
	}

	public void addSimulationDrone(SimulationDrone drone){
		worldObjectsList.add(drone);
		drones.add(drone);		
	}

	public void addSphere(Sphere sphere){
		worldObjectsList.add(sphere);
		spheres.add(sphere);
		getColors().add(sphere.getColor());
	}
	
	public void removeSphere(Sphere sphere) {
		worldObjectsList.remove(sphere);
		spheres.remove(sphere);
		getColors().remove(sphere.getColor());
	}

	
	private ArrayList<float[]> colors = new ArrayList<>();
	
	private ArrayList<float[]> getColors() {
		return colors;
	}
	
	

	public void addSphereWithRandomColor(double[] position) {
		Random rand = new Random();
		float r = 0, g=0,b=0;
		float[] color = {r,g,b};
		while((r == g && r == b) || getColors().contains(color)) {
			r = rand.nextFloat();
			g = rand.nextFloat();
			b = rand.nextFloat();
			color[0] = r;
			color[1] = g;
			color[2] = b;
		}
		addSphere(new Sphere(getGL().getGL2(), color, position));
		
		
		}




	public void addObstacleSphere(ObstacleSphere sphere){
		worldObjectsList.add(sphere);
		obstacleSpheres.add(sphere);		
	}



	public double checkTimePassed() {
		return (System.nanoTime() - getLastTime())*Math.pow(10, -9);
	}

	public void updateTimePassed() {
		this.timePassed += (System.nanoTime() - getLastTime())*Math.pow(10, -9);
	}

	protected abstract void setup();
	protected abstract void handleCollision(WorldObject object, SimulationDrone drone);
	protected abstract void draw();

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
		this.drawable = drawable;
		this.startTime = System.nanoTime();
		this.lastTime = startTime;
		this.physics = new Physics(this);
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

		//TODO op de juiste plaats?
		setup();

		// Start animator.
		gl.setSwapInterval(1);
		animator = new FPSAnimator(this, fps);
		animator.start();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	public void setLastTime(long value) {
		this.lastTime = value;
	}

	public void setCurrentCamera(GeneralCamera camera){
		this.currentCamera = camera;
	}

	public long getLastTime() {
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

	public List<ObstacleSphere> getObstacleSpheres() {
		return obstacleSpheres;
	}

	public GeneralCamera getCurrentCamera(){
		return currentCamera;
	}

	public GLU getGlu() {
		return glu;
	}

	public List<GeneralCamera> getGeneralCameras() {
		return generalCameras;
	}

	public float getCurrentTime() {
		return (float) timePassed;
	}

	public int[] getFramebufferLeft() {
		return framebufferLeft;
	}

	public int[] getFramebufferRight() {
		return framebufferRight;
	}

	public Physics getPhysics() {
		return this.physics;
	}

	public double getStartTime() {
		return this.startTime;
	}

	public int getFps() {
		return animator.getFPS();
	}

	public GLAutoDrawable getDrawable() {
		return drawable;
	}

	public FPSAnimator getAnimator() {
		return animator;
	}

	public List<WorldObject> getWorldObjectList() {
		return this.worldObjectsList;
	}

	public double getWindRotationX() {
		return xWindRotation;
	}

	public double getWindRotationY() {
		return yWindRotation;
	}

	public double getWindRotationZ() {
		return zWindRotation;
	}

	public void setWindRotationX(double value) {
		xWindRotation = value;
	}

	public void setWindRotationY(double value) {
		yWindRotation = value;
	}

	public void setWindRotationZ(double value) {
		zWindRotation = value;
	}

	public double getWindSpeedX() {
		return xWindSpeed;
	}

	public double getWindSpeedY() {
		return yWindSpeed;
	}

	public double getWindSpeedZ() {
		return zWindSpeed;
	}

	public void setWindSpeedX(double value) {
		xWindSpeed = value;
	}

	public void setWindSpeedY(double value) {
		yWindSpeed = value;
	}

	public void setWindSpeedZ(double value) {
		zWindSpeed = value;
	}
	
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public void checkCollision(SimulationDrone drone) {
		List<WorldObject> copyList = new ArrayList();
		copyList.addAll(getWorldObjectList());
		for (WorldObject currentObject: copyList) {
			if (currentObject == drone)
				continue;
			double[] objectPos = currentObject.getPosition();
			double[] dronePos = drone.getPosition();
			double distance = Math.sqrt(Math.pow(objectPos[0] - dronePos[0], 2) + Math.pow(objectPos[1] - dronePos[1], 2) + Math.pow(objectPos[2] - dronePos[2], 2));
			if (distance <= (currentObject.getRadius() + drone.getRadius()))
				handleCollision(currentObject, drone);
		}
	}
}
