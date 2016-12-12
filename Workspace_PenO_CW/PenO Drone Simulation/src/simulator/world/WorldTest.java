package simulator.world;

import com.jogamp.opengl.GL2;

import simulator.camera.GeneralCamera;
import simulator.objects.ObstacleSphere;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;
import simulator.physics.Physics;

public class WorldTest extends World {
	private boolean hitSphere;
	private boolean hitObstacle;
	private Sphere sphere1;
	private ObstacleSphere orb1;
	private float[] outOfReach = {100f,0f,0f};
	
	public WorldTest(){
		super();
		this.physics = new Physics(this);
		super.addGeneralCamera(new GeneralCamera(-2, 1, -1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(-2, 1, 1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(10, 0, 5, 10, 0, 0, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	@Override
	protected void setup() {
		GL2 gl = this.getGL().getGL2();
		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .06f, .35f, .35f,
				colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();

		double[] translateSphere = { 100f, 0, 0f };
		float[] colorSphere = { 1f, 0f, 0f };
		sphere1 = new Sphere(gl, colorSphere, translateSphere, this);
		sphere1.draw();
		addSphere(sphere1);

		double[] translateObstacle = { 100, 0, 0 };
		orb1 = new ObstacleSphere(gl, translateObstacle, this);
		orb1.draw();
		addObstacleSphere(orb1);
	}

	@Override
	protected void handleCollision(WorldObject object, SimulationDrone drone) {
		if (object instanceof ObstacleSphere) {
			hitObstacle = true;
		} else if (object instanceof Sphere) {
			hitSphere = true;
		}
	}

	public boolean hitSphere() {
		return hitSphere;
	}

	public boolean hitObstacle() {
		return hitObstacle;
	}
	
	public Sphere getSphere() {
		return sphere1;
	}
	
	public ObstacleSphere getObstacle() {
		return orb1;
	}
	
	public void resetWorld() {
		hitSphere= false;
		hitObstacle = false;
		sphere1.setPosition(outOfReach);
		orb1.setPosition(outOfReach);
	}
}
