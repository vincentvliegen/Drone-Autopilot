package DroneAutopilot.graphicalrepresentation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class MovementActionAPNew extends AbstractAction {
	private GeneralCameraAPNew camera;
	private MovementActionType type;

	public MovementActionAPNew(GeneralCameraAPNew camera2, MovementActionType e) {
		camera = camera2;
		this.type = e;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (type == MovementActionType.POSITIVE_X)
			camera.setChangeXP(true);
		if (type == MovementActionType.POSITIVE_Y)
			camera.setChangeYP(true);
		if (type == MovementActionType.POSITIVE_Z)
			camera.setChangeZP(true);
		if (type == MovementActionType.ROTATE_R)
			camera.setRotateP(true);
		if (type == MovementActionType.NEGATIVE_X)
			camera.setChangeXN(true);
		if (type == MovementActionType.NEGATIVE_Y)
			camera.setChangeYN(true);
		if (type == MovementActionType.NEGATIVE_Z)
			camera.setChangeZN(true);
		if (type == MovementActionType.ROTATE_L)
			camera.setRotateN(true);

		// Release button
		if (type == MovementActionType.R_POSITIVE_X)
			camera.setChangeXP(false);
		if (type == MovementActionType.R_POSITIVE_Y)
			camera.setChangeYP(false);
		if (type == MovementActionType.R_POSITIVE_Z)
			camera.setChangeZP(false);
		if (type == MovementActionType.R_ROTATE_R)
			camera.setRotateP(false);
		if (type == MovementActionType.R_NEGATIVE_X)
			camera.setChangeXN(false);
		if (type == MovementActionType.R_NEGATIVE_Y)
			camera.setChangeYN(false);
		if (type == MovementActionType.R_NEGATIVE_Z)
			camera.setChangeZN(false);
		if (type == MovementActionType.R_ROTATE_L)
			camera.setRotateN(false);

	}

}
