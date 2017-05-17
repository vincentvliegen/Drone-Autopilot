package simulator.movement;

public enum MovementActionType {
	POSITIVE_X, POSITIVE_Y, POSITIVE_Z, NEGATIVE_X, NEGATIVE_Y, NEGATIVE_Z, R_POSITIVE_X, R_POSITIVE_Y, R_POSITIVE_Z, R_NEGATIVE_X, R_NEGATIVE_Y, R_NEGATIVE_Z, O_POSITIVE_X, O_POSITIVE_Y, O_POSITIVE_Z, O_NEGATIVE_X, O_NEGATIVE_Y, O_NEGATIVE_Z, R_O_POSITIVE_X, R_O_POSITIVE_Y, R_O_POSITIVE_Z, R_O_NEGATIVE_X, R_O_NEGATIVE_Y, R_O_NEGATIVE_Z, O_DELETE;

	public boolean getObjectStatus() {
		if (this == O_POSITIVE_X || this == O_POSITIVE_Y || this == O_POSITIVE_Z || this == O_NEGATIVE_X
				|| this == O_NEGATIVE_Y || this == O_NEGATIVE_Z || this == R_O_POSITIVE_X || this == R_O_POSITIVE_Y
				|| this == R_O_POSITIVE_Z || this == R_O_NEGATIVE_X || this == R_O_NEGATIVE_Y || this == R_O_NEGATIVE_Z)
			return true;
		return false;
	}
}
