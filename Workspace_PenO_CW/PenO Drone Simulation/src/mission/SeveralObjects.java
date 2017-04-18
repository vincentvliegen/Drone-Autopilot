package mission;


import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.algoritmes.ClosestObjects;
import DroneAutopilot.algoritmes.NewWorldScan;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class SeveralObjects extends Mission {

	private final ClosestObjects closestObjects;
	private int refreshCounter;
	private final static int timeToRefresh = 10;
	private boolean firstTime;
	private boolean closestObjectAcquired;
	private boolean secondObjectAcquired;
	private final static float distanceToArrival = 0.55f;// TODO
															// afstellen,straal
															// is 0.5
	private final static float factorD = 2;

	private final NewWorldScan scan;
	private int targetLostCounter = 0;
	private double[] currentTarget = null;
	private float inFrontFactor;

	public SeveralObjects(DroneAutopilot droneAutopilot) {
		super(droneAutopilot);
		this.closestObjects = new ClosestObjects(this.getDrone());
		this.scan = new NewWorldScan(this.getPhysicsCalculations());
		setRefreshCounter(0);
		setFirstTime(true);
	}

	@Override
	public void execute() {
		if (!this.getScan().isFinished()) {// als de scanner nog geen
													// object heeft gevonden,
													// blijf zoeken
			this.getScan().scan();
			this.setFirstTime(true);
			this.setRefreshCounter(0);
		} else { // als de scanner gedaan heeft met zoeken:
//			if (this.getMoveToTarget().isTargetLost()) {
//				this.setTargetLostCounter(this.getTargetLostCounter() + 1);
//				if (this.getTargetLostCounter() >= 20) {
//					this.setClosestObjectAcquired(false);
//					this.setTargetLostCounter(0);
//				}
//			}
			// BEREKENING OBJECTS
			if (isFirstTime()) {
				firstTimeSinceScan();
			} else if (this.getRefreshCounter() >= getTimeToRefresh()) { // TODO
																													// functie
																													// otherObjectsFar
																													// die
																													// aangeeft
																													// of
																													// andere
																													// objecten
																													// ver
																													// af
																													// liggen
				refreshWhenFlying();
			}
			else {
				double distance = this.getPhysicsCalculations().getDistanceDroneToPosition(this.getClosestObjects().getClosestObject());

				if (distance <= getDistancetoarrival()) {
					ArrivedAtTarget(); // TODO aanpassen voor objects
				}
			}
			this.setFirstTime(false);

			// BEWEGING
			if (isClosestObjectAcquired()) {
				if (this.getClosestObjects().getClosestObject() != this.getCurrentTarget()) {
					this.setCurrentTarget(this.getClosestObjects().getClosestObject());
				}
				this.setRefreshCounter(this.getRefreshCounter() + 1);

				this.getClosestObjects().getPhysicsCalculations().updatePosition(this.getCurrentTarget());
				////
				/*
				if (this.getClosestObjects().getPhysicsCalculations().getSpeedTowardsPosition(this.getCurrentTarget()) < 0) {
					System.out.println("SPEED < 0");
					this.getClosestObjects().getPhysicsCalculations().updateMovement(this.getCurrentTarget(),this.determineAcceleration(1));
				}
				if (this.getClosestObjects().getPhysicsCalculations().getDistanceToPosition(this.getCurrentTarget()) <= this.getClosestObjects().getPhysicsCalculations().getSpeedTowardsPosition(this.getCurrentTarget())* this.getInFrontFactor()) {
					System.out.println("DISTANCE < SPEED*factor");
					this.getClosestObjects().getPhysicsCalculations().updateMovement(this.getCurrentTarget(),this.determineAcceleration(0));
				}
				*/
				//////
				
			} else {// begin opnieuw te zoeken naar dichtste object
				this.getScan().scan();
				setFirstTime(true);
			}
			/*
			 * if (isFirstOrbAcquired()) { System.out.println("firstOrb " +
			 * this.getClosestOrbs().getColorFirstOrb()); } if
			 * (isSecondOrbAcquired()) { System.out.println("secondOrb " +
			 * this.getClosestOrbs().getColorSecondOrb()); }
			 */
		}
	}
	


/*
	public float determineAcceleration(int x) {
		if (this.getClosestObjects().getPhysicsCalculations()
				.getDistanceToPosition(this.getCurrentTarget()) <= getFactord()) {
			return this.getClosestObjects().getPhysicsCalculations()
					.getMaxAccelerationValues(this.getCurrentTarget())[x]
					* this.getClosestObjects().getPhysicsCalculations().getDistanceToPosition(this.getCurrentTarget())
					/ getFactord();
		} else {
			return this.getClosestObjects().getPhysicsCalculations()
					.getMaxAccelerationValues(this.getCurrentTarget())[x];
		}
	}
*/
	
	public void firstTimeSinceScan() {
		this.getClosestObjects().addVisibleObjects();
		try {
			this.getClosestObjects().determineClosestObject();
			this.setClosestObjectAcquired(true);
			try {
				this.getClosestObjects().determineSecondObject();
				this.setSecondObjectAcquired(true);
			} catch (NullPointerException e) {
				this.setSecondObjectAcquired(false);
			}
		} catch (NullPointerException e) {
			this.setClosestObjectAcquired(false);
			this.setSecondObjectAcquired(false);
		}
	}
/*
 * - Reset refresh counter
 * - Voegt coordinaten zichtbare polyhedra toe
 * - Kies dichtstbijzijnde coordinaat als nieuw target
 */
	public void refreshWhenFlying() {
		this.setRefreshCounter(0);
		this.getClosestObjects().addVisibleObjects();
		try {
			double[] previousFirst = this.getClosestObjects().getClosestObject();
			this.getClosestObjects().determineClosestObject();

			if (this.getClosestObjects().getClosestObject() == previousFirst) {

				if (isSecondObjectAcquired()) {
					double[] previousSecond = this.getClosestObjects().getSecondObject();
					double previousDistance = VectorCalculations.calculateDistanceBetweenCoords(previousFirst,
							previousSecond);
					try {
						this.getClosestObjects().determineSecondObject();
					} catch (NullPointerException e) {
					}
					if (this.getClosestObjects().getSecondObject() != previousSecond) {
						if (previousDistance <= VectorCalculations.calculateDistanceBetweenCoords(previousFirst,
								this.getClosestObjects().getSecondObject())) {
							this.getClosestObjects().setSecondObject(previousSecond);
						}
					}
				} else {
					try {
						this.getClosestObjects().determineSecondObject();
						this.setSecondObjectAcquired(true);
					} catch (NullPointerException e) {
						this.setSecondObjectAcquired(false);
					}
				}
			} else {
				try {
					this.getClosestObjects().determineSecondObject();
					this.setSecondObjectAcquired(true);
				} catch (NullPointerException e) {
					this.setSecondObjectAcquired(false);
				}
			}
		} catch (NullPointerException e) {

		}
	}

	public void ArrivedAtTarget() {
		//oude target verwijderen
		this.getClosestObjects().getObjectList().remove(this.getCurrentTarget());
		//nieuw target selecteren
		if (isSecondObjectAcquired()) {
			this.getClosestObjects().setClosestObject(this.getClosestObjects().getSecondObject());
			setClosestObjectAcquired(true);
			try {
				this.getClosestObjects().determineSecondObject();
				this.setSecondObjectAcquired(true);
			} catch (NullPointerException e) {
				this.setSecondObjectAcquired(false);
			}
		} else {
			this.setClosestObjectAcquired(false);
		}
	}

	public int getRefreshCounter() {
		return refreshCounter;
	}

	public void setRefreshCounter(int refreshCounter) {
		this.refreshCounter = refreshCounter;
	}

	public static int getTimeToRefresh() {
		return timeToRefresh;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	/**
	 * @return the distancetoarrival
	 */
	public static float getDistancetoarrival() {
		return distanceToArrival;
	}

	public static float getFactord() {
		return factorD;
	}

	public ClosestObjects getClosestObjects() {
		return closestObjects;
	}

	public boolean isClosestObjectAcquired() {
		return closestObjectAcquired;
	}

	public void setClosestObjectAcquired(boolean closestObjectAcquired) {
		this.closestObjectAcquired = closestObjectAcquired;
	}

	public boolean isSecondObjectAcquired() {
		return secondObjectAcquired;
	}

	public void setSecondObjectAcquired(boolean secondObjectAcquired) {
		this.secondObjectAcquired = secondObjectAcquired;
	}

	public int getTargetLostCounter() {
		return targetLostCounter;
	}

	public void setTargetLostCounter(int targetLostCounter) {
		this.targetLostCounter = targetLostCounter;
	}

	public double[] getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(double[] currentTarget) {
		this.currentTarget = currentTarget;
	}

	public void setInFrontFactor(boolean inFront) {
			this.inFrontFactor = (float) 0.5; // TODO factor bepalen
	}

	public float getInFrontFactor() {
		return inFrontFactor;
	}

	public NewWorldScan getScan() {
		return scan;
	}

	@Override
	public void updateGUI() {
		// TODO Auto-generated method stub
		
	}

}
