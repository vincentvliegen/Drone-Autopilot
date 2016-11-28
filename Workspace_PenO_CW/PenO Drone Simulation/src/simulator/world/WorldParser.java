package simulator.world;

import java.io.IOException;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.objects.ObstacleSphere;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.objects.WorldObject;
import simulator.parser.Parser;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class WorldParser extends World{
	public WorldParser(){
		super();
		super.addGeneralCamera(new GeneralCamera(-2, 1, -1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(-2, 1, 1, 2.5f, 0, 0, 0, 1, 0, this));
		super.addGeneralCamera(new GeneralCamera(10, 0, 5, 10, 0, 0, 0, 1, 0, this));
		setCurrentCamera(getGeneralCameras().get(0));
	}

	@Override
	protected void setup() {
		Parser parser = new Parser(this);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setParser(parser);
		GL2 gl = getGL().getGL2();
		
		double[] translateDrone = { 0, 0, 0 };
		float[] colorDrone = { 0f, 0f, 1f };
		SimulationDrone drone1 = new SimulationDrone(gl, .06f, .35f, .35f, colorDrone, translateDrone, this);
		addSimulationDrone(drone1);
		drone1.draw();
		
		
		gl.glGenFramebuffers(1, getFramebufferLeft(), 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferLeft()[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, parser.getImageWidth(), parser.getImageHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, colorRenderbufferLeft[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferLeft, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, parser.getImageWidth(), parser.getImageHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthRenderbufferLeft[0]);

		gl.glGenTextures(1, textureLeft, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureLeft[0]);

		//FBO voor rechts

		gl.glGenFramebuffers(1, getFramebufferRight(), 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);

		gl.glGenRenderbuffers(1, colorRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, colorRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_RGBA8, parser.getImageWidth(), parser.getImageHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, colorRenderbufferRight[0]);

		gl.glGenRenderbuffers(1, depthRenderbufferRight, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT16, parser.getImageWidth(), parser.getImageHeight());
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthRenderbufferRight[0]);

		gl.glGenTextures(1, textureRight, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureRight[0]);



		//set to default buffer
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}

	public static KeyboardMovement getMovement() {
		return movement;
	}
	
	@Override
	protected void handleCollision(WorldObject object, SimulationDrone drone) {
		if (object instanceof ObstacleSphere) {
			System.out.println("You crashed into an obstacleSphere.");
			//System.exit(0);
		} else if (object instanceof Sphere) {
			removeSphere((Sphere)object);			
			getWorldObjectList().remove(object);
		}
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
		gl.glViewport(0, 0, getParser().getImageWidth(), getParser().getImageHeight());
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getDrones().get(0).getLeftDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);

		// voor takeimage rechterCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);
		gl.glViewport(0, 0, getParser().getImageWidth(), getParser().getImageHeight());
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getDrones().get(0).getRightDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		
		// voor uitschrijven van bestand
		/*
		if(getDrones().size() != 0){
			if(i == 100){
		getDrones().get(0).getLeftDroneCamera().writeTakeImageToFile();
		 getDrones().get(0).getRightDroneCamera().writeTakeImageToFile();
		 i++;
		 }
		 else
		 i++;
		 }
		 */
	}

}
