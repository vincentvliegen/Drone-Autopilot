package mission;

import DroneAutopilot.MoveToTarget;
import DroneAutopilot.algoritmes.ClosestOrbs;
import DroneAutopilot.algoritmes.WorldScan;
import DroneAutopilot.calculations.PolyhedraCalculations;
import exceptions.FirstOrbNotVisibleException;
import p_en_o_cw_2016.Drone;

public class SeveralObjects extends Mission {

	private final ClosestOrbs closestObjects;
	private int refreshCounter;
	private final static int timeToRefresh = 10;
	private boolean firstTime;
	private boolean closestObjectAcquired;
	private boolean secondObjectAcquired;
	private final static float distanceToArrival = 0.55f;// TODO afstellen,
															// straal is 0.5
	private final WorldScan worldScan;
	private int targetLostCounter = 0;

	public SeveralObjects(MoveToTarget moveToTarget, Drone drone) {
		super(moveToTarget, drone);
		this.closestObjects = new ClosestOrbs(this.getDrone());
		this.worldScan = new WorldScan(drone);
		setRefreshCounter(0);
		setFirstTime(true);
	}

	@Override
	public void execute() {
		if (!this.getWorldScan().getFinished()) {// als de scanner nog geen
													// object heeft gevonden,
													// blijf zoeken
			this.getWorldScan().scan(this.getDrone());
			this.setFirstTime(true);
			this.setRefreshCounter(0);
		} else { // als de scanner gedaan heeft met zoeken:
			if (this.getMoveToTarget().isTargetLost()) {
				this.setTargetLostCounter(this.getTargetLostCounter() + 1);
				if (this.getTargetLostCounter() >= 20) {
					this.setClosestObjectAcquired(false);
					this.setTargetLostCounter(0);
				}
			}
			// BEREKENING OBJECTS
			if (isFirstTime()) {
				firstTimeSinceScan();
			} else if (!this.getMoveToTarget().isScanning() && this.getRefreshCounter() >= getTimeToRefresh()
					&& !otherObjectsFar()) { // TODO functie otherObjectsFar die
												// aangeeft of andere objecten
												// ver af liggen
				refreshWhenFlying();
			}
			if (!isFirstTime()) {
				float distance = this.getClosestObjects().getPhysicsCalculations()
						.getDistanceToPosition(this.getClosestObjects().getClosestObject());

				if (distance <= getDistancetoarrival()) {
					ArrivedAtTarget(); // TODO aanpassen voor objects
				}
			}
			this.setFirstTime(false);

			// BEWEGING
			if (isClosestObjectAcquired()) {
				this.getMoveToTarget().execute(this.getClosestObjects().getClosestObject()); // TODO
																								// execute
																								// kleur
																								// ->
																								// coordinaten
				this.setRefreshCounter(this.getRefreshCounter() + 1);
			} else {// begin opnieuw te zoeken naar dichtste object
				this.getWorldScan().scan(this.getDrone());
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

	public void firstTimeSinceScan() {
		// TODO functie die coordinaten van alle gezien objecten toevoegd met
		// updateObjectList // bepaal alle objects in zicht
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

	public void refreshWhenFlying() {
		this.setRefreshCounter(0);
		this.getClosestObjects().addVisibleObjects();
		try {
			float[] previousFirst = this.getClosestObjects().getClosestObject();
			this.getClosestObjects().determineClosestObject();

			if (this.getClosestObjects().getClosestObject() == previousFirst) {

				if (isSecondObjectAcquired()) {
					float[] previousSecond = this.getClosestObjects().getSecondObject();
					float previousDistance = this.getClosestObjects().getPhysicsCalculations()
							.calculateDistanceBetweenCoords(previousFirst, previousSecond);
					try {
						this.getClosestObjects().determineSecondObject();
					} catch (NullPointerException e) {
					}
					if (this.getClosestObjects().getSecondObject() != previousSecond) {
						if (previousDistance <= this.getClosestObjects().getPhysicsCalculations()
								.calculateDistanceBetweenCoords(previousFirst,
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

	public ClosestOrbs getClosestObjects() {
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

	/**
	 * @return the worldScan
	 */
	public final WorldScan getWorldScan() {
		return worldScan;
	}
}
