package mission;

import DroneAutopilot.MoveToTarget;
import DroneAutopilot.algoritmes.ClosestOrbs;
import DroneAutopilot.algoritmes.WorldScan;
import exceptions.FirstOrbNotVisibleException;
import p_en_o_cw_2016.Drone;

public class SeveralSpheres extends Mission {

	private final ClosestOrbs closestOrbs;
	private int refreshCounter;
	private final static int timeToRefresh = 10;
	private boolean firstTime;
	private boolean firstOrbAcquired;
	private boolean secondOrbAcquired;
	private final static float distanceToArrival = 0.6f;// TODO afstellen, straal is 0.5
	private final WorldScan worldScan;
	
	public SeveralSpheres(MoveToTarget moveToTarget, Drone drone) {
		super(moveToTarget, drone);
		closestOrbs = new ClosestOrbs(this.getDrone());
		this.worldScan = new WorldScan(drone);
		setRefreshCounter(0);
		setFirstTime(true);
	}

	@Override
	public void execute() {
		//System.out.println("EXSevSph");
		//System.out.println("is scan gedaan? " + this.getMoveToTarget().getWorldScan().getFinished());
		if (!this.getWorldScan().getFinished()) {// als de scanner nog geen bol heeft gevonden, blijf zoeken
			this.getWorldScan().scan(this.getDrone());
			this.setFirstTime(true);
			this.setRefreshCounter(0);
		} else { // als de scanner gedaan is met zoeken:

			// BEREKENING BOLLEN
			if (isFirstTime()) {// eerste keer nadat de scan gedaan is
				firstTimeSinceScan();
			} else if (!this.getMoveToTarget().isScanning() && this.getRefreshCounter() >= getTimeToRefresh()) {// wanneer de drone de 1ste bol in zicht heeft en er naar toe vliegt, herbereken de eerste en 2de bol op geregelde tijdstippen
				refreshWhenFlying();
			} 
			if(!isFirstTime()) {// kijk of we aangekomen zijn in de 1ste bol en hernieuw target (elke keer behalve de eerste keer na scannen)
				float[] cogL = this.getMoveToTarget().getCogL();
				float[] cogR = this.getMoveToTarget().getCogR();
				float distance = this.getClosestOrbs().getPhysicsCalculations().getDistance(cogL, cogR);
				//System.out.println("distance = " + distance);
				
				if (distance <= getDistancetoarrival()// wanneer de 1ste bol bereikt wordt (binnen afstand of (bijna) alles gekleurd)
						|| this.getClosestOrbs().getImageCalculations().checkImageFilled(
								this.getDrone().getLeftCamera(), this.getClosestOrbs().getColorFirstOrb())
						|| this.getClosestOrbs().getImageCalculations().checkImageFilled(
								this.getDrone().getRightCamera(), this.getClosestOrbs().getColorFirstOrb())) {
					ArrivedAtTarget();
				}
			}
			this.setFirstTime(false);// hierna is het niet meer de eerste keer
			
			// BEWEGING
			if (isFirstOrbAcquired()) {//vlieg naar de eerste bol als we deze weten
				this.getMoveToTarget().execute(this.getClosestOrbs().getColorFirstOrb()); 
				this.setRefreshCounter(this.getRefreshCounter() + 1);
			} else {// begin opnieuw te zoeken naar eerste bol
				//System.out.println("herstart scan");
				this.getWorldScan().scan(this.getDrone());
				setFirstTime(true);
			}
			
			if(isFirstOrbAcquired()){
				//System.out.println("firstOrb " + this.getClosestOrbs().getColorFirstOrb());
			}
			if(isSecondOrbAcquired()){
				//System.out.println("secondOrb " + this.getClosestOrbs().getColorSecondOrb());
			}
		}
	}

	public void firstTimeSinceScan(){
		this.getClosestOrbs().determineClosestOrbs(); // bepaal alle bollen in zicht
		try {
			this.getClosestOrbs().calculateFirstOrb(); // bepaal eerste bol
			this.setFirstOrbAcquired(true);
			try {
				this.getClosestOrbs().calculateSecondOrb(); // bepaal tweede bol
				this.setSecondOrbAcquired(true);
			} catch (NullPointerException e) {// geen tweede bol
				this.setSecondOrbAcquired(false);
			} catch (FirstOrbNotVisibleException e) {
			} // kan niet
		} catch (NullPointerException e) {// geen eerste bol
			this.setFirstOrbAcquired(false);
			this.setSecondOrbAcquired(false);
		}
	}
	
	public void refreshWhenFlying(){
		this.setRefreshCounter(0);
		this.getClosestOrbs().determineClosestOrbs();//bereken dichtstbijzijnde bollen
		try {//bereken eerste bol
			int previousFirst = this.getClosestOrbs().getColorFirstOrb();
			this.getClosestOrbs().calculateFirstOrb();//bereken eerste bol
			if (this.getClosestOrbs().getColorFirstOrb() == previousFirst) {//eerste bol blijft eerste bol 
				if (isSecondOrbAcquired()) {//is er een tweede bol?
					int previousSecond = this.getClosestOrbs().getColorSecondOrb();
					float previousDistance = this.getClosestOrbs().getDistanceSecondOrb();
					try {
						this.getClosestOrbs().calculateSecondOrb();
					} catch (NullPointerException | FirstOrbNotVisibleException e) {//tweede bol is reeds berekend, dus geen bol in zicht doet niets | eerste bol is in zicht
					}
					if(this.getClosestOrbs().getColorSecondOrb() != previousSecond) {//is tweede bol anders?
						if (previousDistance <= this.getClosestOrbs().getDistanceSecondOrb()) {//kijk of de nieuwe tweede bol dichterbij ligt dan de vorige
							this.getClosestOrbs().setColorSecondOrb(previousSecond);
							this.getClosestOrbs().setDistanceSecondOrb(previousDistance);
						}//geen else, tweede bol blijft
					}
				} else {//geen tweede bol, kijk of er toch een is
					try {
						this.getClosestOrbs().calculateSecondOrb();
						this.setSecondOrbAcquired(true);
					} catch (NullPointerException | FirstOrbNotVisibleException e) {
					this.setSecondOrbAcquired(false);}
				}
			} else {//eerste bol verandert
				try {
					this.getClosestOrbs().calculateSecondOrb();//nieuwe tweede bol bij de nieuwe eerste bol
					this.setSecondOrbAcquired(true);
				} catch (NullPointerException | FirstOrbNotVisibleException e) {
				this.setSecondOrbAcquired(false);}
			}

		} catch (NullPointerException e) {// geen eerste bol (kan niet wanneer we niet aan het zoeken zijn)
			
		}
	}
	
	public void ArrivedAtTarget(){
		if (isSecondOrbAcquired()) {//is er een tweede bol berekend?
			// verander 2de in eerste orb
			int previousFirstOrb = this.getClosestOrbs().getColorFirstOrb();
			this.getClosestOrbs().setColorFirstOrb(this.getClosestOrbs().getColorSecondOrb());
			setFirstOrbAcquired(true);

			// bereken 2de orb
			this.getClosestOrbs().determineClosestOrbs();
			this.getClosestOrbs().getClosestOrbs().remove(previousFirstOrb);// verwijdert de eerste als deze nog zichtbaar is
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
		} else {//geen eerste bol
			this.setFirstOrbAcquired(false);
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

	/**
	 * @return the worldScan
	 */
	public final WorldScan getWorldScan() {
		return worldScan;
	}


}
