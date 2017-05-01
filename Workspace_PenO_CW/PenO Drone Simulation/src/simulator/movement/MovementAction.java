package simulator.movement;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import simulator.objects.*;

public class MovementAction extends AbstractAction {

	private KeyboardMovement movement;
	private boolean changeX, changeY, changeZ, negative, object, release;

	public MovementAction(KeyboardMovement mov, boolean x, boolean y, boolean z, boolean negative, boolean object,
			boolean release) {
		movement = mov;
		this.changeX = x;
		this.changeY = y;
		this.changeZ = z;
		this.negative = negative;
		this.object = object;
		this.release = release;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		movement.setChangeObj(object);
		if (!release) {
			if (!negative) {
				if (changeX)
					movement.setChangeXP(true);
				if (changeY)
					movement.setChangeYP(true);
				if (changeZ)
					movement.setChangeZP(true);
			} else {
				if (changeX)
					movement.setChangeXN(true);
				if (changeY)
					movement.setChangeYN(true);
				if (changeZ)
					movement.setChangeZN(true);
			}
		} else {
			if (!negative) {
				if (changeX)
					movement.setChangeXP(false);
				if (changeY)
					movement.setChangeYP(false);
				if (changeZ)
					movement.setChangeZP(false);
			} else {
				if (changeX)
					movement.setChangeXN(false);
				if (changeY)
					movement.setChangeYN(false);
				if (changeZ)
					movement.setChangeZN(false);
			}
		}
	}

}
