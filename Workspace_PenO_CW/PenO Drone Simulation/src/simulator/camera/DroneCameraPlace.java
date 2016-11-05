package simulator.camera;

/**
 * 	An enumeration of the possible places of the camera on the drone.
 *  In its current definition, the class only distinguishes between
 *  right and left.
 */
public enum DroneCameraPlace {
	RIGHT, LEFT;
	
	public int getIndicator() {
		if (this.equals(LEFT)) {
			return -1;
		} else {
			return 1;
		}
	}
}
