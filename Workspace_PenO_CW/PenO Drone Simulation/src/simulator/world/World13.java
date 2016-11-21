package simulator.world;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;

import com.jogamp.opengl.*;

public class World13 extends World {
	
	public World13(){
		super();
		super.addGeneralCamera(new GeneralCamera(-1, 1, 2, 0, 0, -2.5f, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(1, 1, 2, 0, 0, -2.5f, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(3, 0, -8, 0, 0, -10, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}
	
	@Override
	protected void setup() {
		GL2 gl = getGL().getGL2();
		
		double[] translateSphere = { 0, 0, -10f };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, .2f, 64, 64, colorSphere, translateSphere);
		sphere1.draw();
		addSphere(sphere1);
		
		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .05f, .1f, .1f, colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();
	}

	@Override
	protected void handleCollision(WorldObject object, SimulationDrone drone) {
		if (object instanceof Sphere) {
			getWorldObjectList().remove(object);
			getSpheres().remove(object);
		} else{
			// Collision with something other than a sphere???
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		super.updateTimePassed();
		super.getPhysics().run((float) checkTimePassed());

		for (SimulationDrone drone : getDrones()) {
			drone.timeHasPassed((float) checkTimePassed());

		}

		super.setLastTime(System.nanoTime());

		// TODO plaats van dit?
		if (!super.getAnimator().isAnimating()) {
			return;
		}
		GL2 gl = getGL().getGL2();

		// voor scherm
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
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
		getDrones().get(0).getLeftDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		// voor takeimage rechterCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
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
	protected void draw() {
		GL2 gl = getGL().getGL2();

		// translate camera.
		if (!(this.getCurrentCamera() instanceof DroneCamera)) {
			movement.update((float)checkTimePassed());
			gl.glTranslated(movement.getX(), movement.getY(), movement.getZ());
			gl.glRotated(movement.getRotateX(), 1, 0, 0);
			gl.glRotated(movement.getRotateY(), 0, 1, 0);
			gl.glRotated(movement.getRotateZ(), 0, 0, 1);
		}
		
		for (WorldObject object: getWorldObjectList()){
			object.draw();
		}
	}

}
