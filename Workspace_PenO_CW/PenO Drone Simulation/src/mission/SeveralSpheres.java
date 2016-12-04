package mission;

import DroneAutopilot.MoveToTarget;
import DroneAutopilot.ClosestOrbs;
import p_en_o_cw_2016.Drone;

public class SeveralSpheres extends Mission {

	private final ClosestOrbs closestOrbs = new ClosestOrbs(this.getDrone());
	private boolean firstTime;

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public ClosestOrbs getClosestOrbs() {
		return closestOrbs;
	}

	public SeveralSpheres(MoveToTarget moveToTarget, Drone drone) {
		super(moveToTarget, drone);
	}

	@Override
	public void execute() {
		System.out.println("EXSevSph");
		if (!this.getMoveToTarget().getWorldScan().getFinished()) {
			this.getMoveToTarget().getWorldScan().scan(this.getDrone(), this.getMoveToTarget().getImageCalculations());
			this.setFirstTime(true);
		} else {
			try {
				if (this.isFirstTime()) {
					this.getClosestOrbs().determineClosestOrbs();
					this.getClosestOrbs().calculateFirstOrb();
					this.getClosestOrbs().calculateSecondOrb();
					this.setFirstTime(false);
//				} else if (dichtgenoeg) { // TODO boolean van movetotarget
//					this.getClosestOrbs().setColorFirstOrb(this.getClosestOrbs().getColorSecondOrb());
//					this.getClosestOrbs().calculateSecondOrb();
				}
			} catch (NullPointerException e) {
				this.getMoveToTarget().getWorldScan().scan(this.getDrone(),
						this.getMoveToTarget().getImageCalculations());
			}
			// TODO check of bol in beeld die dichter licht dan huidig doel
			this.getMoveToTarget().execute(this.getClosestOrbs().getColorFirstOrb());
		}
	}
}
