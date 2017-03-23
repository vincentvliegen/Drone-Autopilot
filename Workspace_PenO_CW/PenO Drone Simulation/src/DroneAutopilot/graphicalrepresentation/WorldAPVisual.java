package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class WorldAPVisual extends GLCanvas implements GLEventListener{

	WorldAPData dataWorld;
	private FPSAnimator animator;
	private int fps = 60;
	GLU glu;

	public WorldAPVisual(WorldAPData dataWorld) {
		this.dataWorld = dataWorld;
	}

	//TODO methodes
	//TODO camera
	private float rquad = 0;
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		gl.glTranslatef( 0f, 0f, -5.0f ); 

		// Rotate The Cube On X, Y & Z
		gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f); 
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		//method body
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();

		drawable.setGL(gl);

		// Enable z- (depth) buffer for hidden surface removal.
		gl.glEnable(GL.GL_DEPTH_TEST);
		// Define "clear" color.
		gl.glClearColor(1f, 1f, 1f, 0.5f);

		// Create GLU.
		glu = new GLU();
		gl.setSwapInterval(1);
		animator = new FPSAnimator(this, fps);
		animator.start();

	}

	@Override
	public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

		// TODO Auto-generated method stub
		final GL2 gl = drawable.getGL().getGL2();
		if( height == 0 )
			height = 1;

		final float h = ( float ) width / ( float ) height;
		gl.glViewport( 0, 0, width, height );
		gl.glMatrixMode( GL2.GL_PROJECTION );
		gl.glLoadIdentity();

		glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
		gl.glMatrixMode( GL2.GL_MODELVIEW );
		gl.glLoadIdentity();
	}


}
