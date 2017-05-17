package simulator.movement;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import simulator.objects.*;

public class MovementAction extends AbstractAction {

	private KeyboardMovement movement;
	private MovementActionType type;

	public MovementAction(KeyboardMovement mov, MovementActionType e) {
		movement = mov;
		this.type = e;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		movement.setChangeObj(type.getObjectStatus());
		if (type == MovementActionType.POSITIVE_X || type == MovementActionType.O_POSITIVE_X)
			movement.setChangeXP(true);
		if (type == MovementActionType.POSITIVE_Y || type == MovementActionType.O_POSITIVE_Y)
			movement.setChangeYP(true);
		if (type == MovementActionType.POSITIVE_Z || type == MovementActionType.O_POSITIVE_Z)
			movement.setChangeZP(true);
		if (type == MovementActionType.NEGATIVE_X || type == MovementActionType.O_NEGATIVE_X)
			movement.setChangeXN(true);
		if (type == MovementActionType.NEGATIVE_Y || type == MovementActionType.O_NEGATIVE_Y)
			movement.setChangeYN(true);
		if (type == MovementActionType.NEGATIVE_Z || type == MovementActionType.O_NEGATIVE_Z)
			movement.setChangeZN(true);

		if (type == MovementActionType.R_POSITIVE_X || type == MovementActionType.R_O_POSITIVE_X)
			movement.setChangeXP(false);
		if (type == MovementActionType.R_POSITIVE_Y || type == MovementActionType.R_O_POSITIVE_Y)
			movement.setChangeYP(false);
		if (type == MovementActionType.R_POSITIVE_Z || type == MovementActionType.R_O_POSITIVE_Z)
			movement.setChangeZP(false);
		if (type == MovementActionType.R_NEGATIVE_X || type == MovementActionType.R_O_NEGATIVE_X)
			movement.setChangeXN(false);
		if (type == MovementActionType.R_NEGATIVE_Y || type == MovementActionType.R_O_NEGATIVE_Y)
			movement.setChangeYN(false);
		if (type == MovementActionType.R_NEGATIVE_Z || type == MovementActionType.R_O_NEGATIVE_Z)
			movement.setChangeZN(false);

		
		if (type == MovementActionType.O_DELETE)
			movement.deleteObject();
	}

}
