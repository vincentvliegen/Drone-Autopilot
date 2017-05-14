package DroneAutopilot.graphicalrepresentation.secondOption;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;


//TODO enkel tekenen als nodig, niet gewoon standaard
@SuppressWarnings("serial")
public class WorldAPVisualNew extends GLCanvas implements GLEventListener {

	WorldAPDataNew dataWorld;
	private FPSAnimator animator;
	private int fps = 60;
	GLU glu;
	private GeneralCameraAPNew camera = new GeneralCameraAPNew(0, 0, 0, 100, 0, 0, 0, 1, 0, this);
	private JPanel panel;

	public WorldAPVisualNew(WorldAPDataNew dataWorld2) {
		this.dataWorld = dataWorld2;
		addGLEventListener(this);
	}

	private WorldAPDataNew getDataWorld() {
		return dataWorld;
	}

	// TODO camera
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		camera.update((float) 1/60);
		camera.setCamera(gl, glu);
		gl.glPushMatrix();

		for (PolyhedronAPDataNew poly : getDataWorld().getPolyhedrons()) {
			PolyhedronAPDrawerNew.draw(poly, getGL().getGL2());
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
        am.put("Forward", new MovementActionAPNew(camera, MovementActionType.POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("DOWN"), "Backwards");
        am.put("Backwards", new MovementActionAPNew(camera, MovementActionType.NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("LEFT"), "Left");
        am.put("Left", new MovementActionAPNew(camera, MovementActionType.NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("RIGHT"), "Right");
        am.put("Right", new MovementActionAPNew(camera, MovementActionType.POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("E"), "Upwards");
        am.put("Upwards", new MovementActionAPNew(camera, MovementActionType.POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("D"), "Downwards");
        am.put("Downwards", new MovementActionAPNew(camera, MovementActionType.NEGATIVE_Y));
        im.put(KeyStroke.getKeyStroke("R"), "RotateRight");
        am.put("RotateRight", new MovementActionAPNew(camera, MovementActionType.ROTATE_R));
        im.put(KeyStroke.getKeyStroke("F"), "RotateLeft");
        am.put("RotateLeft", new MovementActionAPNew(camera, MovementActionType.ROTATE_L));
        
        im.put(KeyStroke.getKeyStroke("released UP"), "RForward");
        am.put("RForward", new MovementActionAPNew(camera, MovementActionType.R_POSITIVE_X));
        im.put(KeyStroke.getKeyStroke("released DOWN"), "RBackwards");
        am.put("RBackwards", new MovementActionAPNew(camera, MovementActionType.R_NEGATIVE_X));
        im.put(KeyStroke.getKeyStroke("released LEFT"), "RLeft");
        am.put("RLeft", new MovementActionAPNew(camera, MovementActionType.R_NEGATIVE_Z));
        im.put(KeyStroke.getKeyStroke("released RIGHT"), "RRight");
        am.put("RRight", new MovementActionAPNew(camera, MovementActionType.R_POSITIVE_Z));
        im.put(KeyStroke.getKeyStroke("released E"), "RUpwards");
        am.put("RUpwards", new MovementActionAPNew(camera, MovementActionType.R_POSITIVE_Y));
        im.put(KeyStroke.getKeyStroke("released D"), "RDownwards");
        am.put("RDownwards", new MovementActionAPNew(camera, MovementActionType.R_NEGATIVE_Y));
        im.put(KeyStroke.getKeyStroke("released R"), "RRotateRight");
        am.put("RRotateRight", new MovementActionAPNew(camera, MovementActionType.R_ROTATE_R));
        im.put(KeyStroke.getKeyStroke("released F"), "RRotateLeft");
        am.put("RRotateLeft", new MovementActionAPNew(camera, MovementActionType.R_ROTATE_L));
        
	}

	public void setJPanel(JPanel panel) {
		this.panel = panel;
	}

}
