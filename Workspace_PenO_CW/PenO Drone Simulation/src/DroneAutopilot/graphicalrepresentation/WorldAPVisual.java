package DroneAutopilot.graphicalrepresentation;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

//TODO enkel tekenen als nodig, niet gewoon standaard
@SuppressWarnings("serial")
public class WorldAPVisual extends GLCanvas implements GLEventListener{

	WorldAPData dataWorld;
	private FPSAnimator animator;
	private int fps = 60;
	GLU glu;

	public WorldAPVisual(WorldAPData dataWorld) {
		this.dataWorld = dataWorld;
		addGLEventListener(this);
	}
	
	private WorldAPData getDataWorld() {
		return dataWorld;
	}
	
	//TODO methodes
	//TODO camera
//	private float rquad = 0;
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );

		gl.glLoadIdentity();
		
		doCam();
		gl.glPushMatrix();
		
		for(PolyhedronAPData poly: getDataWorld().getPolyhedrons()) {
			PolyhedronAPDrawer.draw(poly, getGL().getGL2());
		}
		gl.glPopMatrix();
//		gl.glTranslatef( 0f, 0f, -5.0f ); 

		// Rotate The Cube On X, Y & Z
//		gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f); 
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		//method body
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

	private GLAutoDrawable getDrawable() {
		return drawable;
	}
	
	private void doCam() {
		int height = getDrawable().getSurfaceHeight();
		int width = getDrawable().getSurfaceWidth();
		// Change to projection matrix.
		getGL().getGL2().glMatrixMode(GL2.GL_PROJECTION);
		getGL().getGL2().glLoadIdentity();
	
		// Perspective.
		float widthHeightRatio = (float) width / (float) height;
		glu.gluPerspective(45, widthHeightRatio, 0.01, 500);
		glu.gluLookAt(0, 0, 0, 5, 0, 0, 0, 1, 0);

	
		// Change back to model view matrix.
		getGL().getGL2().glMatrixMode(GL2.GL_MODELVIEW);
		getGL().getGL2().glLoadIdentity();
	}
	

	@Override
	public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

		// TODO Auto-generated method stub
		final GL2 gl = drawable.getGL().getGL2();
		if( height == 0 )
			height = 1;

		final float h = ( float ) width / ( float ) height;
		gl.glMatrixMode( GL2.GL_PROJECTION );
		gl.glLoadIdentity();

		gl.glMatrixMode( GL2.GL_MODELVIEW );
		gl.glLoadIdentity();
	}


}
