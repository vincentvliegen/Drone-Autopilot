
package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;

public class World11 extends World {

	private static final long serialVersionUID = 1L;

	public World11() {
		super();
		super.addGeneralCamera(new GeneralCamera(-2, 1, -1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(-2, 1, 1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(10, 0, 5, 10, 0, 0, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	// TODO nodig??????
	// /**
	// * @return Some standard GL capabilities (with alpha).
	// */
	// public static GLCapabilities createGLCapabilities() {
	// GLCapabilities capabilities = new
	// GLCapabilities(GLProfile.get(GLProfile.GL2));
	// capabilities.setRedBits(8);
	// capabilities.setBlueBits(8);
	// capabilities.setGreenBits(8);
	// capabilities.setAlphaBits(8);
	// return capabilities;
	// }


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
		gl.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
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
		gl.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		getDrones().get(0).getLeftDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		// voor takeimage rechterCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
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

	// int i = 0;

	// TODO verplaats indien mogelijk naar World
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

		// TODO werk evt met objects ipv drones en spheres apart

		for (WorldObject o : getWorldObjectList()) 
			o.draw();

	}

	@Override
	protected void setup() {
		GL2 gl = getGL().getGL2();

		double[] translateSphere = { 10f, 0, 0f };
		float[] colorSphere = { 1f, 0f, 0f };
		Sphere sphere1 = new Sphere(gl, .5f, colorSphere, translateSphere);
		sphere1.draw();
		addSphere(sphere1);

		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .03f, .175f, .175f, colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();

	}

	@Override
	protected void handleCollision(WorldObject currentObject, SimulationDrone drone) {
		// Nothing happens with collision
		
	}

}
