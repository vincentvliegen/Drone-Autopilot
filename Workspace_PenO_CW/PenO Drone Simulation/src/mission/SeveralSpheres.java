package mission;

import DroneAutopilot.MoveToTarget;
import exceptions.FirstOrbNotVisibleException;
import DroneAutopilot.ClosestOrbs;
import p_en_o_cw_2016.Drone;

public class SeveralSpheres extends Mission {

	private final ClosestOrbs closestOrbs;
	private int refreshCounter;
	private final static int timeToRefresh = 10;
	private boolean firstTime;
	private boolean firstOrbAcquired;
	private boolean secondOrbAcquired;
	private final static float distanceToArrival = 0.8f;// TODO afstellen

	public SeveralSpheres(MoveToTarget moveToTarget, Drone drone) {
		super(moveToTarget, drone);
		closestOrbs = new ClosestOrbs(this.getDrone());
		setRefreshCounter(0);
		setFirstTime(true);
	}

	@Override
	public void execute() {
		System.out.println("EXSevSph");

		System.out.println("is scan gedaan? " + this.getMoveToTarget().getWorldScan().getFinished());
		if (!this.getMoveToTarget().getWorldScan().getFinished()) {// als de scanner nog geen bol heeft gevonden, blijf zoeken
			this.getMoveToTarget().getWorldScan().scan(this.getDrone(), this.getClosestOrbs().getImageCalculations());
			this.setFirstTime(true);
		} else { // als de scanner gedaan is met zoeken:

			// BEREKENING NAAR WAAR TE BEWEGEN
			if (isFirstTime()) {// eerste keer: bepaal 1ste en tweede bol TODO refresh
				this.getClosestOrbs().determineClosestOrbs(); // bepaal alle bollen in zicht
				try {
					this.getClosestOrbs().calculateFirstOrb(); // bepaal eerste
																// bol
					this.setFirstOrbAcquired(true);
					try {
						this.getClosestOrbs().calculateSecondOrb(); // bepaal
																	// tweede
																	// bol
						this.setSecondOrbAcquired(true);
					} catch (NullPointerException e) {// geen tweede bol
						this.setSecondOrbAcquired(false);
					} catch (FirstOrbNotVisibleException e) {
					} // kan niet
				} catch (NullPointerException e) {// geen eerste bol
					this.setFirstOrbAcquired(false);
					this.setSecondOrbAcquired(false);
				}
				this.setFirstTime(false);
			} else {// elke keer behalve de eerste
				float[] cogL = this.getMoveToTarget().getCogL(); // van vorige
																	// cyclus
				float[] cogR = this.getMoveToTarget().getCogR(); // van vorige
																	// cyclus
				float distance = this.getClosestOrbs().getPhysicsCalculations().getDistance(cogL, cogR);
				System.out.println("distance = " + distance);
				// wanneer de eerste bol gepasseerd is, ga verder naar de tweede
				if (distance <= getDistancetoarrival()
						|| this.getClosestOrbs().getImageCalculations().checkImageFilled(this.getDrone().getLeftCamera(), this.getClosestOrbs().getColorFirstOrb())
						|| this.getClosestOrbs().getImageCalculations().checkImageFilled(this.getDrone().getRightCamera(), this.getClosestOrbs().getColorFirstOrb())) {// wanneer de bol bereikt wordt
					if (isSecondOrbAcquired()) {
						// verander 2de in eerste orb
						int previousFirstOrb = this.getClosestOrbs().getColorFirstOrb();
						this.getClosestOrbs().setColorFirstOrb(this.getClosestOrbs().getColorSecondOrb());
						setFirstOrbAcquired(true);

						// bereken 2de orb
						this.getClosestOrbs().determineClosestOrbs();
						this.getClosestOrbs().getClosestOrbs().remove(previousFirstOrb);// verwijdert de oorspronnkelijke eerste als deze nog zichtbaar is
						if (this.getClosestOrbs().getClosestOrbs().size() > 1) {
							try {
								this.getClosestOrbs().calculateSecondOrb();// bepaal nieuwe tweede
								this.setSecondOrbAcquired(true);
							} catch (NullPointerException | FirstOrbNotVisibleException e) {
								this.setSecondOrbAcquired(false);
							}
						} else {
							this.setSecondOrbAcquired(false);
						}
					} else {
						this.setFirstOrbAcquired(false);
					}
				}
			}
			System.out.println("firstOrb " + this.getClosestOrbs().getColorFirstOrb());

			// BEWEGING
			if (isFirstOrbAcquired()) {
				this.getMoveToTarget().execute(this.getClosestOrbs().getColorFirstOrb()); // vlieg nar first orb
			} else {// begin opnieuw te zoeken
				System.out.println("herstart scan");
				this.getMoveToTarget().getWorldScan().scan(this.getDrone(),
						this.getClosestOrbs().getImageCalculations());
				setFirstTime(true);
			}
		}
	}

	/**
	 * @return the refreshCounter
	 */
	public int getRefreshCounter() {
		return refreshCounter;
	}

	/**
	 * @param refreshCounter
	 *            the refreshCounter to set
	 */
	public void setRefreshCounter(int refreshCounter) {
		this.refreshCounter = refreshCounter;
	}

	public ClosestOrbs getClosestOrbs() {
		return closestOrbs;
	}

	public final static int getTimeToRefresh() {
		return timeToRefresh;
	}

	/**
	 * @return the firstTime
	 */
	public boolean isFirstTime() {
		return firstTime;
	}

	/**
	 * @param firstTime
	 *            the firstTime to set
	 */
	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	/**
	 * @return the distancetoarrival
	 */
	public static float getDistancetoarrival() {
		return distanceToArrival;
	}

	/**
	 * @return the firstOrbAcquired
	 */
	public boolean isFirstOrbAcquired() {
		return firstOrbAcquired;
	}

	/**
	 * @param firstOrbAcquired
	 *            the firstOrbAcquired to set
	 */
	public void setFirstOrbAcquired(boolean firstOrbAcquired) {
		this.firstOrbAcquired = firstOrbAcquired;
	}

	/**
	 * @return the secondOrbAcquired
	 */
	public boolean isSecondOrbAcquired() {
		return secondOrbAcquired;
	}

	/**
	 * @param secondOrbAcquired
	 *            the secondOrbAcquired to set
	 */
	public void setSecondOrbAcquired(boolean secondOrbAcquired) {
		this.secondOrbAcquired = secondOrbAcquired;
	}
}
