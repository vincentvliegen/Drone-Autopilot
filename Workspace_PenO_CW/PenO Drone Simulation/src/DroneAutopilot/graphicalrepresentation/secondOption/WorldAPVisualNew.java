package DroneAutopilot.graphicalrepresentation.secondOption;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import DroneAutopilot.graphicalrepresentation.PolyhedronAPData;
import DroneAutopilot.graphicalrepresentation.PolyhedronAPDrawer;
import DroneAutopilot.graphicalrepresentation.WorldAPData;

//TODO enkel tekenen als nodig, niet gewoon standaard
@SuppressWarnings("serial")
public class WorldAPVisualNew extends GLCanvas implements GLEventListener {

	WorldAPDataNew dataWorld;
	private FPSAnimator animator;
	private int fps = 60;
	GLU glu;
	private GeneralCameraAPNew camera = new GeneralCameraAPNew(0, 0, 0, 100, 0, 0, 0, 1, 0, this);

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

}
