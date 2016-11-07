package simulator.world;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;

import simulator.camera.DroneCamera;
import simulator.camera.GeneralCamera;
import simulator.movement.KeyboardMovement;
import simulator.objects.SimulationDrone;
import simulator.objects.Sphere;
import simulator.physics.Force;

public class World12 extends World {

	private static final long serialVersionUID = 1L;
	private boolean setup;
	private SimulationDrone drone1;
	private Sphere sphere1;
	private Force windForce;
	private float windForceX = 0;
	private float windForceY = 0;
	private float windForceZ = 0;

	public World12() {
		super();
		super.addGeneralCamera(new GeneralCamera(0, 20, 200, 0, 0, 0, 0, 1, 0));
		super.addGeneralCamera(new GeneralCamera(0, 50, 100, 0, 0, 0, 0, 1, 0));
		super.addGeneralCamera(new GeneralCamera(0, 100, 200, 0, 0, 0, 0, 1, 0));
		setCurrentCamera(getGeneralCameras().get(0));
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

	public static KeyboardMovement movement = new KeyboardMovement();

	public void display(GLAutoDrawable drawable) {
		super.updateTimePassed();
		if (!setup) {
			this.windForce = new Force(0,0,0);
			super.getPhysics().addForce(windForce);
		}
		if (setup) {
			float timePassed = super.checkTimePassed();
			windForce.setXNewton(windForceX);
			windForce.setYNewton(windForceY);
			windForce.setZNewton(windForceZ);
			super.physics.run(timePassed);
			drone1.timeHasPassed(timePassed);
			super.setLastTime((float) (System.nanoTime()*Math.pow(10, -9)));
		}
		if (!super.getAnimator().isAnimating()) {
			return;
		}
		GL2 gl = getGL().getGL2();

		//voor scherm
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		setCamera(gl, getGlu());
		draw();


		/*
		 * TODO
		 * Moet slimmer aangepakt worden, wat als er nu meerdere drones zijn? Dan moeten er voor elke drone
		 * (manueel) 2 buffers aangemaakt worden (een voor linker en een voor rechter camera);
		 * dus vermijden..! 
		 * --> idee: ipv telkens een nieuwe int[] te maken, gewoon een grotere te gebruiken en de offset aan te passen?
		 */

		//voor takeimage linkerCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferLeft()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getDrones().get(0).getLeftDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);


		//voor takeimage rechterCamera
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getFramebufferRight()[0]);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		getDrones().get(0).getRightDroneCamera().setCamera(gl, getGlu());
		draw();
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}	


	private void draw() {

		GL2 gl = getGL().getGL2();

		// translate camera.
		if(!(this.getCurrentCamera() instanceof DroneCamera)){
			movement.update();
			gl.glTranslated(movement.getX(), movement.getY(), movement.getZ());
			gl.glRotated(movement.getRotateX(), 1, 0, 0);
			gl.glRotated(movement.getRotateY(), 0, 1, 0);
			gl.glRotated(movement.getRotateZ(), 0, 0, 1);
		}
		
		// Input Sphere.
		if (!setup) {
			double[] translateSphere = { 0, 0, -30 };
			float[] colorSphere = { 1f, 0f, 0f };
			Sphere sphere1 = new Sphere(gl, 5f, 64, 64, colorSphere, translateSphere);
			sphere1.drawSphere();
			this.sphere1 = sphere1;
			addSphere(sphere1);
		} else {
			sphere1.drawSphere();
		}
		// Input Drone.
		if (!setup) {
			double[] translateDrone = { 0, 0, 0 };
			float[] colorDrone = { 0f, 0f, 1f };
			SimulationDrone drone1 = new SimulationDrone(gl, 3f, 4f, 4f, colorDrone, translateDrone, this);
			this.drone1 = drone1;
			addSimulationDrone(drone1);
			drone1.drawDrone();
		} else {
			drone1.drawDrone();
		}

		setup = true;

	}


	// Update position camera's
	public void setCamera(GL2 gl, GLU glu) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(currentCamera.getEyeX(), currentCamera.getEyeY(), currentCamera.getEyeZ(), 
				currentCamera.getLookAtX(), currentCamera.getLookAtY(), currentCamera.getLookAtZ(), 
				currentCamera.getUpX(), currentCamera.getUpY(), currentCamera.getUpZ());
		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void setWindForceX(float value) {
		this.windForceX = value;
	}
	
	public void setWindForceY(float value) {
		this.windForceY = value;
	}
	
	public void setWindForceZ(float value) {
		this.windForceZ = value;
	}
}
