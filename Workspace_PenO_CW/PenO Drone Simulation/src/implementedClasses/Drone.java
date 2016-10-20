package implementedClasses;

public class Drone implements p_en_o_cw_2016.Drone{

	 /** The distance between the cameras, in meters. Note: the (pinholes of the)
    cameras are on the pitch axis, so pitching rotates the cameras but does not
    translate them. */
	@Override
	public float getCameraSeparation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Camera getLeftCamera() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Camera getRightCamera() {
		// TODO Auto-generated method stub
		return null;
	}

	 /** The weight in kg. */
	@Override
	public float getWeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** The gravity in newtons per kg. */
	@Override
	public float getGravity() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** The drag force per unit of speed, in kilograms per second. This drone is highly
    aerodynamic, so the drag force is linear in the speed. */
	@Override
	public float getDrag() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** Gets the maximum of the absolute value of the thrust, in newtons. */
	@Override
	public float getMaxThrust() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxPitchRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxRollRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxYawRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** The current pitch angle (the angle between the direction of view and the horizontal
    plane), in degrees. A positive pitch angle means the drone is looking down.
    This value is always between -90 and 90. */
	@Override
	public float getPitch() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** The current roll angle (the angle between the line that connects the cameras and the
    horizontal line perpendicular to the direction of view), in degrees.
    A positive roll angle means the drone is banking to the right.
    This value is always between -180 and 180. If the absolute value is greater than
    90, this means the drone is upside down. */
	@Override
	public float getRoll() {
		// TODO Auto-generated method stub
		return 0;
	}

	 /** The amount of simulated time that has passed since the start of the simulation,
    in seconds. */
	@Override
	public float getCurrentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/** Sets the pitch rate (the rate of rotation around the line that connects the cameras),
    in degrees per second. At each point in time between this call and the next call of this method,
    the rate of rotation of the drone around the line that connects the cameras at that point in time
    will be the given value (except for wind influences).
    Note that the pitch rate is NOT always equal to the rate of change of the pitch angle.
    Specifically, the pitch rate is equal to the rate of change of the pitch angle if and only if
    the roll angle is zero. */
	@Override
	public void setPitchRate(float value) {
		// TODO Auto-generated method stub
	}

	/** Sets the roll rate, in degrees per second. This equals the rate of change of the roll angle. */
	@Override
	public void setRollRate(float value) {
		// TODO Auto-generated method stub
		
	}

	 /** Sets the yaw rate (rate at which the drone rotates around the axis through the center
    between the cameras, perpendicular to the line that connects the cameras and the direction of view),
    in degrees per second. A positive yaw rate means the drone turns to the right (from the point of view
    of someone fixed to the drone and looking in the direction of view, such that the left camera is to
    their left-hand side and the right camera is to their right-hand side).
    At each point in time between this call and the next call of this method, the rate of rotation of the
    drone around the axis through the center of the cameras, perpendicular to the line that connects the cameras
    and the direction of view at that point in time will be the given value (except for wind influences). */
	@Override
	public void setYawRate(float value) {
		// TODO Auto-generated method stub
		
	}

	 /** Sets the thrust (the force exerted by the propellors) in newtons. This force is
    perpendicular to the plane defined by the direction of view and the line connecting
    the cameras. Positive means upward (from the point of view of someone fixed to the drone, looking in
    the direction of view, such that the left camera is to their left-hand side and the right camera is to their
    right-hand side). */
	@Override
	public void setThrust(float value) {
		// TODO Auto-generated method stub
		
	}

}
