package simulator.world;

import java.util.*;

import javax.swing.*;

import p_en_o_cw_2016.AutopilotFactory;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import demo.DemoEnum;
import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.editor.WorldEditor;
import simulator.movement.*;
import simulator.objects.HollowCubePolyhedron;
import simulator.objects.LetterLPolyhedron;
import simulator.objects.ObstacleSphere;
import simulator.objects.Polyhedron;
import simulator.objects.PolyhedronType;
import simulator.objects.SimulationDrone;
import simulator.objects.Pyramid;
import simulator.objects.Sphere;
import simulator.objects.Triangle;
import simulator.objects.WorldObject;
import simulator.parser.Parser;
import simulator.parser.Parser_v1;
import simulator.physics.Collision;
import simulator.physics.MathCalculations;
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
	private List<Polyhedron> polyhedrons = new ArrayList<>();

	private List<ObstacleSphere> obstacleSpheres = new ArrayList<>();
	private GLU glu;

	private Parser parser = new Parser_v1(this);
	private int[] framebufferRight = new int[1];
	private int[] framebufferLeft = new int[1];

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
	private int frames = 0;
	private AutopilotFactory factory;
	private static WorldEditor editor = new WorldEditor();
	private Collision collision = new Collision(this);
	private JPanel panel;
	private DemoEnum demoEnum;

	public World() {
		addGLEventListener(this);
	}

	public void setAutopilotFactory(AutopilotFactory factory) {
		this.factory = factory;
	}

	public AutopilotFactory getAutopilotFactory() {
		return factory;
	}

	public void addGeneralCamera(GeneralCamera camera) {
		generalCameras.add(camera);
	}

	public void addDroneCamera(DroneCamera camera) {
		droneCameras.add(camera);
	}

	public void addSimulationDrone(SimulationDrone drone) {
		worldObjectsList.add(drone);
		drones.add(drone);
	}

	public void addSphere(Sphere sphere) {
		worldObjectsList.add(sphere);
		spheres.add(sphere);
		getTargetColors().add(sphere.getColor());
	}

	public void removeSphere(Sphere sphere) {
		worldObjectsList.remove(sphere);
		spheres.remove(sphere);
		getTargetColors().remove(sphere.getColor());
	}

	public void addPolyhedron(Polyhedron poly) {
		worldObjectsList.add(poly);
		polyhedrons.add(poly);
		for (Triangle triangle : poly.getTriangles()) {
			triangleColors.add(triangle.getIntColor());
		}
	}

	public void removePolyhedron(Polyhedron poly) {
		worldObjectsList.remove(poly);
		polyhedrons.remove(poly);
		for (Triangle triangle : poly.getTriangles()) {
			triangleColors.remove(triangle.getColor());
		}
	}

	private ArrayList<float[]> targetColors = new ArrayList<>();
	private ArrayList<float[]> obstacleColors = new ArrayList<>();

	private ArrayList<int[]> triangleColors = new ArrayList<>();

	public ArrayList<int[]> getTriangleColors() {
		return triangleColors;
	}

	public void addSphereWithRandomColor(double[] position) {
		Random rand = new Random();
		float r = 0, g = 0, b = 0;
		float[] color = { r, g, b };
		while ((r == g && r == b) || getTargetColors().contains(color)) {
			r = rand.nextFloat();
			g = rand.nextFloat();
			b = rand.nextFloat();
			color[0] = r;
			color[1] = g;
			color[2] = b;
		}
		addSphere(new Sphere(getGL().getGL2(), color, position, this));

	}

	public void addObstacleSphereWithRandomColor(double[] position) {
		Random rand = new Random();
		float value = rand.nextFloat();
		float[] color = { value, value, value };
		while ((getObstacleColors().contains(color))) {
			value = color[0] = value;
			color[1] = value;
			color[2] = value;

		}
		addObstacleSphere(new ObstacleSphere(getGL().getGL2(), color, position,
				this));
	}

	public void addObstacleSphere(ObstacleSphere sphere) {
		worldObjectsList.add(sphere);
		obstacleSpheres.add(sphere);
		obstacleColors.add(sphere.getColor());
	}

	protected abstract void setup();

	protected abstract void handleCollision(WorldObject object,
			SimulationDrone drone);

	protected void draw() {

		// translate camera.
		if (!(this.getCurrentCamera() instanceof DroneCamera)) {
			movement.update((float)1/60);
			this.getCurrentCamera().setEyeX(
					(float) movement.getX()
							+ this.getCurrentCamera().getStartEyeX());
			this.getCurrentCamera().setEyeY(
					(float) movement.getY()
							+ this.getCurrentCamera().getStartEyeY());
			this.getCurrentCamera().setEyeZ(
					(float) movement.getZ()
							+ this.getCurrentCamera().getStartEyeZ());
			this.getCurrentCamera().setLookAtX(
					(float) movement.getX()
							+ this.getCurrentCamera().getStartLookAtX());
			this.getCurrentCamera().setLookAtY(
					(float) movement.getY()
							+ this.getCurrentCamera().getStartLookAtY());
			this.getCurrentCamera().setLookAtZ(
					(float) movement.getZ()
							+ this.getCurrentCamera().getStartLookAtZ());
		}

		for (WorldObject object : getWorldObjectList()) {
			object.draw();
		}
	}

	public void display(GLAutoDrawable drawable) {
		updateFrames();
		getPhysics().run((float) 1 / 60);

		for (SimulationDrone drone : getDrones()) {
			drone.timeHasPassed((float) 1 / 60);

		}

		if (!super.getAnimator().isAnimating()) {
			return;
		}
		GL2 gl = getGL().getGL2();

		// voor scherm
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, drawable.getSurfaceWidth(),
				drawable.getSurfaceHeight());
		getCurrentCamera().setCamera(gl, getGlu());
		draw();

		/*
		 * TODO Moet slimmer aangepakt worden, wat als er nu meerdere drones
		 * zijn? Dan moeten er voor elke drone (manueel) 2 buffers aangemaakt
		 * worden (een voor linker en een voor rechter camera); dus vermijden..!
		 * --> idee: ipv telkens een nieuwe int[] te maken, gewoon een grotere
		 * te gebruiken en de offset aan te passen?
		 */

		// voor takeimage linkerCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferLeft()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, drawable.getSurfaceWidth(),
				drawable.getSurfaceHeight());
		getDrones().get(0).getLeftDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		// voor takeimage rechterCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, drawable.getSurfaceWidth(),
				drawable.getSurfaceHeight());
		getDrones().get(0).getRightDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		// voor uitschrijven van bestand
		// if(getDrones().size() != 0){
		// if(i == 100){
		// getDrones().get(0).getLeftDroneCamera().writeTakeImageToFile();
		// getDrones().get(0).getRightDroneCamera().writeTakeImageToFile();
		// i++;
		// }
		// else
		// i++;
		//
		// }
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	/**
	 * Sets up the screen.
	 */
	public void init(GLAutoDrawable drawable) {
		this.drawable = drawable;
		this.physics = new Physics(this);
		final GL2 gl = drawable.getGL().getGL2();
		drawable.setGL(gl);
		createKeybindings();

		// Enable z- (depth) buffer for hidden surface removal.
		gl.glEnable(GL.GL_DEPTH_TEST);
		// Define "clear" color.
		gl.glClearColor(1f, 1f, 1f, 0.5f);


		// Create GLU.
		glu = new GLU();

		// FBO voor links

		gl.glGenFramebuffers(1, framebufferLeft, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebufferLeft[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, getWidth(),
				getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
				GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER,
				colorRenderbufferLeft[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16,
				getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT,
				GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);

		gl.glGenTextures(1, textureLeft, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureLeft[0]);

		// FBO voor rechts

		gl.glGenFramebuffers(1, framebufferRight, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebufferRight[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, getWidth(),
				getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
				GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER,
				colorRenderbufferRight[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16,
				getWidth(), getHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT,
				GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);

		gl.glGenTextures(1, textureRight, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureRight[0]);

		// set to default buffer
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		setup();
//		Pyramid fig = new Pyramid(this, PolyhedronType.TARGET, new double[]{2,0,0});
//		getWorldObjectList().add(fig);
		if (demoEnum == null) {
			HollowCubePolyhedron figL = new HollowCubePolyhedron(this, PolyhedronType.TARGET, new double[]{1,0,0});
			getWorldObjectList().add(figL);
		} else {
			if (demoEnum.getCreateSingleObjectStatus()) {
				if (demoEnum.getTargetStatus()) {
					if (demoEnum == DemoEnum.SCAN_LETTERL) {
					LetterLPolyhedron figL = new LetterLPolyhedron(this, PolyhedronType.TARGET, new double[]{1,0,0});
					getWorldObjectList().add(figL);
					} else if (demoEnum == DemoEnum.SCAN_HOLLOWCUBE) {
						HollowCubePolyhedron figL = new HollowCubePolyhedron(this, PolyhedronType.TARGET, new double[]{1,0,0});
						getWorldObjectList().add(figL);
					}
				} else {
					LetterLPolyhedron figL = new LetterLPolyhedron(this, PolyhedronType.OBSTACLE, new double[]{2,0,0});
					getWorldObjectList().add(figL);
					LetterLPolyhedron figR = new LetterLPolyhedron(this, PolyhedronType.TARGET, new double[]{3,0.75,0});
					getWorldObjectList().add(figR);					
				}
			}
		}

		// Start animator.
		gl.setSwapInterval(1);
		animator = new FPSAnimator(this, fps);
		animator.start();
		gl.glEnable(GL.GL_CULL_FACE);
	}

	public void checkCollision(SimulationDrone drone) {
		List<WorldObject> copyList = new ArrayList<WorldObject>();
		copyList.addAll(getWorldObjectList());
		for (WorldObject currentObject : copyList) {
			if (currentObject == drone)
				continue;
			double[] objectPos = currentObject.getPosition();
			double[] dronePos = drone.getPosition();
			double distance = Math.sqrt(Math.pow(objectPos[0] - dronePos[0], 2)
					+ Math.pow(objectPos[1] - dronePos[1], 2)
					+ Math.pow(objectPos[2] - dronePos[2], 2));
			if (distance <= (currentObject.getRadius() + drone.getRadius()))
				handleCollision(currentObject, drone);
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	private ArrayList<float[]> getTargetColors() {
		return targetColors;
	}

	public ArrayList<float[]> getObstacleColors() {
		return obstacleColors;
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

	public List<Polyhedron> getPolyhedrons() {
		return polyhedrons;
	}

	public List<ObstacleSphere> getObstacleSpheres() {
		return obstacleSpheres;
	}

	public GeneralCamera getCurrentCamera() {
		return currentCamera;
	}

	public GLU getGlu() {
		return glu;
	}

	public List<GeneralCamera> getGeneralCameras() {
		return generalCameras;
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

	public int getFps() {
		return animator.getFPS();
	}

	public Parser getParser() {
		return parser;
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

	public double getWindSpeedX() {
		return xWindSpeed;
	}

	public double getWindSpeedY() {
		return yWindSpeed;
	}

	public double getWindSpeedZ() {
		return zWindSpeed;
	}

	public void setCurrentCamera(GeneralCamera camera) {
		this.currentCamera = camera;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
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

	public void setWindSpeedX(double value) {
		xWindSpeed = value;
	}

	public void setWindSpeedY(double value) {
		yWindSpeed = value;
	}

	public void setWindSpeedZ(double value) {
		zWindSpeed = value;
	}

	public static KeyboardMovement getMovement() {
		return movement;
	}

	public void updateFrames() {
		frames += 1;
	}

	public float getCurrentTime() {
		return frames * (float) 1 / 60;
	}

	public static WorldEditor getEditor() {
		return editor;
	}

	public void mousePressed() {
		int viewport[] = new int[4];
		double modelViewMatrix[] = new double[16];
		double projectionMatrix[] = new double[16];
		int yCoordChanged = 0;
		double worldCoordNear[] = new double[4];
		double worldCoordFar[] = new double[4];

		int x = getEditor().getMouseX();
		int y = getEditor().getMouseY();
		GL2 gl = (GL2) getGL();
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);
		gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);
		yCoordChanged = viewport[3] - (int) y - 1;

		glu.gluUnProject((double) x, (double) yCoordChanged, 0.0,
				modelViewMatrix, 0, projectionMatrix, 0, viewport, 0,
				worldCoordNear, 0);
		glu.gluUnProject((double) x, (double) yCoordChanged, 1.0,
				modelViewMatrix, 0, projectionMatrix, 0, viewport, 0,
				worldCoordFar, 0);

		double[] vector = { (worldCoordFar[0] - worldCoordNear[0]) / 10000,
				(worldCoordFar[1] - worldCoordNear[1]) / 10000,
				(worldCoordFar[2] - worldCoordNear[2]) / 10000 };		
		
		double maxDistance = Double.MAX_VALUE;
		for (int i = 0; i < 10000; i++) {
			worldCoordNear[0] += vector[0];
			worldCoordNear[1] += vector[1];
			worldCoordNear[2] += vector[2];
			for (WorldObject object : getWorldObjectList()) {
				if (object instanceof SimulationDrone)
					continue;
				double distance = MathCalculations.getDistanceBetweenPoints(object.getPosition(),
						worldCoordNear);
				if (distance <= 1) {
					if (distance <= maxDistance) {
						maxDistance = distance;
						getMovement().setObject(object);
					}
				}
			}
		}
		getEditor().completedMouseCheck();
	}

	public Collision getCollision() {
		return collision;
	}
	
	private void createKeybindings() {
		InputMap im = panel.getInputMap();
		ActionMap am = panel.getActionMap();
		
		im.put(KeyStroke.getKeyStroke("UP"), "Forward");
        am.put("Forward", new MovementAction(getMovement(), MovementActionType.POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("DOWN"), "Backwards");
        am.put("Backwards", new MovementAction(getMovement(), MovementActionType.NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("LEFT"), "Left");
        am.put("Left", new MovementAction(getMovement(), MovementActionType.NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("RIGHT"), "Right");
        am.put("Right", new MovementAction(getMovement(), MovementActionType.POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("E"), "Upwards");
        am.put("Upwards", new MovementAction(getMovement(), MovementActionType.POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("D"), "Downwards");
        am.put("Downwards", new MovementAction(getMovement(), MovementActionType.NEGATIVE_Y));
        
        im.put(KeyStroke.getKeyStroke("released UP"), "RForward");
        am.put("RForward", new MovementAction(getMovement(), MovementActionType.R_POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("released DOWN"), "RBackwards");
        am.put("RBackwards", new MovementAction(getMovement(), MovementActionType.R_NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("released LEFT"), "RLeft");
        am.put("RLeft", new MovementAction(getMovement(), MovementActionType.R_NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "RRight");
        am.put("RRight", new MovementAction(getMovement(), MovementActionType.R_POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("released E"), "RUpwards");
        am.put("RUpwards", new MovementAction(getMovement(), MovementActionType.R_POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("released D"), "RDownwards");
        am.put("RDownwards", new MovementAction(getMovement(), MovementActionType.R_NEGATIVE_Y));
        
        im.put(KeyStroke.getKeyStroke("I"), "ForwardObject");
        am.put("ForwardObject", new MovementAction(getMovement(), MovementActionType.O_POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("K"), "BackwardsObject");
        am.put("BackwardsObject", new MovementAction(getMovement(), MovementActionType.O_NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("J"), "LeftObject");
        am.put("LeftObject", new MovementAction(getMovement(), MovementActionType.O_NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("L"), "RightObject");
        am.put("RightObject", new MovementAction(getMovement(), MovementActionType.O_POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("U"), "UpwardsObject");
        am.put("UpwardsObject", new MovementAction(getMovement(), MovementActionType.O_POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("O"), "DownwardsObject");
        am.put("DownwardsObject", new MovementAction(getMovement(), MovementActionType.O_NEGATIVE_Y));
        
        im.put(KeyStroke.getKeyStroke("released I"), "RForwardObject");
        am.put("RForwardObject", new MovementAction(getMovement(), MovementActionType.R_O_POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("released K"), "RBackwardsObject");
        am.put("RBackwardsObject", new MovementAction(getMovement(), MovementActionType.R_O_NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("released J"), "RLeftObject");
        am.put("RLeftObject", new MovementAction(getMovement(), MovementActionType.R_O_NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("released L"), "RRightObject");
        am.put("RRightObject", new MovementAction(getMovement(), MovementActionType.R_O_POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("released U"), "RUpwardsObject");
        am.put("RUpwardsObject", new MovementAction(getMovement(), MovementActionType.R_O_POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("released O"), "RDownwardsObject");
        am.put("RDownwardsObject", new MovementAction(getMovement(), MovementActionType.R_O_NEGATIVE_Y));
        
        im.put(KeyStroke.getKeyStroke("DELETE"), "DelObject");
        am.put("DelObject", new MovementAction(getMovement(), MovementActionType.O_DELETE));
	}
	
	public void setJPanel(JPanel panel){
		this.panel = panel;
	}
	
	public void setDemoEnum(DemoEnum d) {
		demoEnum = d;
	}
	
	public DemoEnum getDemoEnum() {
		return demoEnum;
	}
}
