package simulator.objects;

public enum PolyhedronType {
	OBSTACLE, TARGET;

	@Override
	public String toString() {
		if (this.equals(OBSTACLE)) {
			return "Obstacle";
		} else {
			return "Target";
		}
	}

	public int getIndicator() {
		if (this.equals(OBSTACLE)) {
			return -1;
		} 
		else {
			//Is a target ball
			return 1;
		}
	}

}
