package simulator.camera;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import p_en_o_cw_2016.Camera;
import simulator.objects.SimulationDrone;
import simulator.world.World;
import simulator.world.WorldParser;

public class DroneCamera extends GeneralCamera implements Camera {

	private GL2 gl;
	private SimulationDrone drone;
	private DroneCameraPlace place;

	public DroneCamera(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ, float upX,
			float upY, float upZ, World world, SimulationDrone drone, DroneCameraPlace place) {
		super(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ, upX, upY, upZ, world);
		this.setDrone(drone);
		this.gl = getWorld().getGL().getGL2();
		this.place = place;
	}
	
	
	public int getHeight() {
		if (getDrone().getWorld() instanceof WorldParser)
			return getDrone().getWorld().getParser().getImageHeight();
		return getWorld().getDrawable().getSurfaceHeight();
	}

	@Override
	public int[] takeImage() {
		int[] temp = new int[getWidth() * getHeight()];
		ByteBuffer buffer = ByteBuffer.allocateDirect(3 * getWidth() * getHeight());

		// TODO eventueel met switch werken

		if (getPlace() == DroneCameraPlace.LEFT)
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getWorld().getFramebufferLeft()[0]);
		else
			gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, getWorld().getFramebufferRight()[0]);

		gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
		gl.glReadPixels(0, 0, getWidth(), getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer);
		buffer.rewind();
		int bpp = 3;
		int i = 0;

		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				i = ((getHeight() - r - 1) * getWidth() + c) * bpp;
				int blue = buffer.get(i) & 0xFF;
				int green = buffer.get(i + 1) & 0xFF;
				int red = buffer.get(i + 2) & 0xFF;
				temp[r * getWidth() + c] = red | green << 8 | blue << 16;

			}
		}
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		return temp;
	}

	public void updateDroneCamera() {
		drone.createRotateMatrix();
		
		if (this.place.equals(DroneCameraPlace.MIDDLE)) {
			System.out.println("--------------------");
			System.out.println("EyeX " + getEyeX());
			System.out.println("EyeY " + getEyeY());
			System.out.println("EyeZ " + getEyeZ());
			System.out.println("LookX " + getLookAtX());
			System.out.println("LookY " + getLookAtY());
			System.out.println("LookZ " + getLookAtZ());
			System.out.println("UpX " + getUpX());
			System.out.println("UpY " + getUpY());
			System.out.println("UpZ " + getUpZ());
			System.out.println("StartX " + drone.getRotateMatrix().get(3)*getStartEyeX());
			System.out.println("StartY " + drone.getRotateMatrix().get(4)*getStartEyeY());
			System.out.println("StartZ " + drone.getRotateMatrix().get(5)*getStartEyeZ());
			System.out.println("---------------------");
		}
		
		setEyeX((float) (getStartEyeX() * drone.getRotateMatrix().get(0)
				+ getStartEyeY() * drone.getRotateMatrix().get(1) + getStartEyeZ() * drone.getRotateMatrix().get(2) + drone.getPosition()[0]));
		setEyeY((float) (getStartEyeX() * drone.getRotateMatrix().get(3)
				+ getStartEyeY() * drone.getRotateMatrix().get(4) + getStartEyeZ() * drone.getRotateMatrix().get(5) + drone.getPosition()[1]));
		setEyeZ((float) (getStartEyeX() * drone.getRotateMatrix().get(6)
				+ getStartEyeY() * drone.getRotateMatrix().get(7) + getStartEyeZ() * drone.getRotateMatrix().get(8) + drone.getPosition()[2]));
		setLookAtX((float) (getStartLookAtX() * drone.getRotateMatrix().get(0)
				+ getStartLookAtY() * drone.getRotateMatrix().get(1)
				+ getStartLookAtZ() * drone.getRotateMatrix().get(2) + drone.getPosition()[0]));
		setLookAtY((float) (getStartLookAtX() * drone.getRotateMatrix().get(3)
				+ getStartLookAtY() * drone.getRotateMatrix().get(4)
				+ getStartLookAtZ() * drone.getRotateMatrix().get(5) + drone.getPosition()[1]));
		setLookAtZ((float) (getStartLookAtX() * drone.getRotateMatrix().get(6)
				+ getStartLookAtY() * drone.getRotateMatrix().get(7)
				+ getStartLookAtZ() * drone.getRotateMatrix().get(8) + drone.getPosition()[2]));
		setUpX((float) (drone.getRotateMatrix().get(1) * 1));
		setUpY((float) (drone.getRotateMatrix().get(4) * 1));
		setUpZ((float) (drone.getRotateMatrix().get(7) * 1));
	}
	

	public void setDrone(SimulationDrone drone) {
		this.drone = drone;
	}

	public DroneCameraPlace getPlace() {
		return place;
	}

	public SimulationDrone getDrone() {
		return drone;
	}

	/**
	 * FOVH = 2*atan(aspectRatio (tan(FOVV/2))
	 * ratio = width/height
	 * 
	 * bron: http://gamedev.stackexchange.com/questions/43922/opengl-fovx-question
	 * 
	 */
	
	private float fovy = 45;

	@Override
	public float getHorizontalAngleOfView() {
		if (getDrone().getWorld() instanceof WorldParser)
			return (float) getDrone().getWorld().getParser().getHorizontalAngleOfView();
		float aspectRatio = (float) getWidth()/getHeight();		
		return (float) Math.toDegrees(2 * Math.atan(aspectRatio * Math.tan(Math.toRadians(getVerticalAngleOfView()/2))));
		
		
	}


	@Override
	public float getVerticalAngleOfView() {
		if (getDrone().getWorld() instanceof WorldParser)
			return (float) getDrone().getWorld().getParser().getVerticalAngleOfView();
		return fovy;
	}

	@Override
	public int getWidth() {
		if (getDrone().getWorld() instanceof WorldParser)
			return getDrone().getWorld().getParser().getImageWidth();
		return getWorld().getDrawable().getSurfaceWidth();
	}
	
	
	//voor uitschrijven naar bestand
	public void writeTakeImageToFile() {
		  //		int width = getWidth();
		  String path = "/home/r0578402/bb";
		  if(getPlace() == DroneCameraPlace.LEFT)
		    path = path + "-left.png";
		  else
		    path = path + "-right.png";

		  File file = new File(path); // The file to save to.

		  String format = "PNG"; // Example: "PNG" or "JPG"
		  int[] temp = takeImage();


		  BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		  for(int x = 0; x < getWidth(); x++)
		  {
		    for(int y = 0; y < getHeight(); y++)
		    {

		      //TODO: geeft kleuren omgekeerd; krijgt autopilot ze wel juist door?
		      image.setRGB(x, y, temp[y*getWidth()+x]);

		      //						image.setRGB(x, getHeight() - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);

		    }
		  }
		  try {
		    ImageIO.write(image, format, file);
		  } catch (IOException e) { e.printStackTrace(); }
		}
}
