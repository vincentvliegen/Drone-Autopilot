package simulatietestjes;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import simulator.objects.Camera;
import simulator.objects.Sphere;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A minimal JOGL demo.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since 26 Feb 2009
 */
public class FlyingSpace extends GLCanvas implements GLEventListener {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The GL unit (helper class). */
	private GLU glu;

	/** The frames per second setting. */
	private int fps = 60;

	/** The OpenGL animator. */
	private FPSAnimator animator;

	/**
	 * A new mini starter.
	 * 
	 * @param capabilities The GL capabilities.
	 * @param width The window width.
	 * @param height The window height.
	 */
	public FlyingSpace(GLCapabilities capabilities, int width, int height) {
		addGLEventListener(this);
	}

	/**
	 * @return Some standard GL capabilities (with alpha).
	 */
	private static GLCapabilities createGLCapabilities() {
		GLCapabilities capabilities = new GLCapabilities(GLProfile.get( GLProfile.GL2 ));
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		return capabilities;
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

	/**
	 * The only method that you should implement by yourself.
	 * 
	 * @see javax.media.opengl.GLEventListener#display(javax.media.opengl.GLAutoDrawable)
	 */

	private static Camera camera = new Camera();

	public void display(GLAutoDrawable drawable) {
		if (!animator.isAnimating()) {
			return;
		}
		final GL2 gl = drawable.getGL().getGL2();

		// Clear screen.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);


		// Set camera.
		setCamera(gl, glu, 200);

		// translate
		camera.update();
		gl.glTranslated(camera.getX(), camera.getY(), camera.getZ());
		gl.glRotated(camera.getRotateX(), 1, 0, 0);
		gl.glRotated(camera.getRotateY(), 0, 1, 0);
		gl.glRotated(camera.getRotateZ(), 0, 0, 1);

	/*	//light

		gl.glEnable( GL2.GL_LIGHTING );  
		gl.glEnable( GL2.GL_LIGHT0 );  
		gl.glEnable( GL2.GL_NORMALIZE );  
		//		//		
		float[] light = {1f, 0, 0f, 0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light, 0);

		float[] ambientLight = { 1f, 0f, 0f,0f };  
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);  

		//		// multicolor diffuse 
		float[] diffuseLight = { 1f,0f,0f,0f };  
		gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0 );

		
		float[] colorSurface = {0.5f, 1f, 0.5f};
		float[] p1 = {-50, 0, -50};
		float[] p2 = {50, 0, -50};
		float[] p3 = {50, 0, 50};
		float[] p4 = {-50, 0, 50};
		Surface surface1 = new Surface(gl, p1, p2, p3, p4, colorSurface);
		surface1.drawSurface();*/
		
	/*	gl.glColor3f(0.5f, 1f, 0.5f);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3f(-50, 0, -50);
		gl.glVertex3f(50, 0, -50);
		gl.glVertex3f(50, 0, 50);
		gl.glVertex3f(-50, 0, 50);
		gl.glEnd();*/
		
		double[] translate = {0,20,0};
		float[] colorSphere = {1f,0f,0f};
		Sphere sphere1 = new Sphere(gl, 6.378f, 64, 64, colorSphere, translate);
		sphere1.drawSphere();
		
	/*	gl.glColor3f(1f, 0f, 0f);
		gl.glTranslated(0, 20, 0);
		GLUT glut = new GLUT();
		final float radius = 6.378f;
		final int slices = 16;
		final int stacks = 16;
		glut.glutSolidSphere(radius, stacks, slices);
		
		*/ 
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		throw new UnsupportedOperationException("Changing display is not supported.");
	}

	private void setCamera(GL2 gl, GLU glu, float distance) {
		// Change to projection matrix.
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		// Perspective.
		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 20, distance, 0, 0, 0, 0, 1, 0);

		// Change back to model view matrix.
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	
	
	/**
	 * Starts the JOGL mini demo.
	 * 
	 * @param args Command line args.
	 */
	/*public final static void main(String[] args) {
		GLCapabilities capabilities = createGLCapabilities();
		FlyingSpace canvas = new FlyingSpace(capabilities, 800, 500);
		JFrame frame = new JFrame("Mini JOGL Demo (breed)");
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		//frame.add(gui);
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//JDesktopPane desktopPane = new JDesktopPane();
		SimulationGUI gui = new SimulationGUI();
		//desktopPane.add(gui);
		canvas.requestFocus();
		canvas.addKeyListener(camera);
	}*/



	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

}