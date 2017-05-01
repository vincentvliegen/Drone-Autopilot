package DroneAutopilot.graphicalrepresentation;

import javax.swing.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

//TODO enkel tekenen als nodig, niet gewoon standaard
@SuppressWarnings("serial")
public class WorldAPVisual extends GLCanvas implements GLEventListener {

	WorldAPData dataWorld;
	private FPSAnimator animator;
	private int fps = 60;
	GLU glu;
	private GeneralCameraAP camera = new GeneralCameraAP(0, 0, 0, 100, 0, 0, 0, 1, 0, this);
	private JPanel panel;

	public WorldAPVisual(WorldAPData dataWorld) {
		this.dataWorld = dataWorld;
		addGLEventListener(this);
	}

	private WorldAPData getDataWorld() {
		return dataWorld;
	}

	// TODO camera
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		camera.update((float)1/60);
		camera.setCamera(gl, glu);
		gl.glPushMatrix();

		for (PolyhedronAPData poly : getDataWorld().getPolyhedrons()) {
			PolyhedronAPDrawer.draw(poly, getGL().getGL2());
		}
		gl.glPopMatrix();
		// gl.glTranslatef( 0f, 0f, -5.0f );

		// Rotate The Cube On X, Y & Z
		// gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// method body
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		this.drawable = drawable;

		createKeybindings();
		drawable.setGL(gl);

		// Enable z- (depth) buffer for hidden surface removal.
		gl.glEnable(GL.GL_DEPTH_TEST);
		// Define "clear" color.
		gl.glClearColor(1f, 1f, 1f, 0.5f);

		// Create GLU.
		glu = new GLU();
		animator = new FPSAnimator(this, fps);
		animator.start();

	}

	GLAutoDrawable drawable;

	GLAutoDrawable getDrawable() {
		return drawable;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		// TODO Auto-generated method stub
		final GL2 gl = drawable.getGL().getGL2();
		if (height == 0)
			height = 1;

		final float h = (float) width / (float) height;
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	private void createKeybindings() {
		InputMap im = panel.getInputMap();
		ActionMap am = panel.getActionMap();
		
		im.put(KeyStroke.getKeyStroke("UP"), "Forward");
        am.put("Forward", new MovementActionAP(camera, true, false, false, false, false, false));
        im.put(KeyStroke.getKeyStroke("DOWN"), "Backwards");
        am.put("Backwards", new MovementActionAP(camera, true, false, false, true, false, false));
        im.put(KeyStroke.getKeyStroke("LEFT"), "Left");
        am.put("Left", new MovementActionAP(camera, false, false, true, true, false, false));
        im.put(KeyStroke.getKeyStroke("RIGHT"), "Right");
        am.put("Right", new MovementActionAP(camera, false, false, true, false, false, false));
        im.put(KeyStroke.getKeyStroke("E"), "Upwards");
        am.put("Upwards", new MovementActionAP(camera, false, true, false, false, false, false));
        im.put(KeyStroke.getKeyStroke("D"), "Downwards");
        am.put("Downwards", new MovementActionAP(camera, false, true, false, true, false, false));
        im.put(KeyStroke.getKeyStroke("R"), "RotateRight");
        am.put("RotateRight", new MovementActionAP(camera, false, false, false, false, true, false));
        im.put(KeyStroke.getKeyStroke("F"), "RotateLeft");
        am.put("RotateLeft", new MovementActionAP(camera, false, false, false, true, true, false));
        
        im.put(KeyStroke.getKeyStroke("released UP"), "RForward");
        am.put("RForward", new MovementActionAP(camera, true, false, false, false, false, true));
        im.put(KeyStroke.getKeyStroke("released DOWN"), "RBackwards");
        am.put("RBackwards", new MovementActionAP(camera, true, false, false, true, false, true));
        im.put(KeyStroke.getKeyStroke("released LEFT"), "RLeft");
        am.put("RLeft", new MovementActionAP(camera, false, false, true, true, false, true));
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "RRight");
        am.put("RRight", new MovementActionAP(camera, false, false, true, false, false, true));
        im.put(KeyStroke.getKeyStroke("released E"), "RUpwards");
        am.put("RUpwards", new MovementActionAP(camera, false, true, false, false, false, true));
        im.put(KeyStroke.getKeyStroke("released D"), "RDownwards");
        am.put("RDownwards", new MovementActionAP(camera, false, true, false, true, false, true));
        im.put(KeyStroke.getKeyStroke("released R"), "RRotateRight");
        am.put("RRotateRight", new MovementActionAP(camera, false, false, false, false, true, true));
        im.put(KeyStroke.getKeyStroke("released F"), "RRotateLeft");
        am.put("RRotateLeft", new MovementActionAP(camera, false, false, false, true, true, true));
        
	}

	public void setJPanel(JPanel panel) {
		this.panel = panel;
	}
}
