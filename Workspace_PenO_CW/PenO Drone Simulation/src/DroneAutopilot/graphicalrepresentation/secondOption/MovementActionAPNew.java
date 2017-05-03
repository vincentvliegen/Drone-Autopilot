package DroneAutopilot.graphicalrepresentation.secondOption;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class MovementActionAPNew extends AbstractAction {
	private GeneralCameraAPNew camera;
	private boolean changeX, changeY, changeZ, negative, rotate, release;

	public MovementActionAPNew(GeneralCameraAPNew camera2, boolean x, boolean y, boolean z, boolean negative, boolean rotate, boolean release) {
		camera = camera2;
		this.changeX = x;
		this.changeY = y;
		this.changeZ = z;
		this.negative = negative;
		this.rotate = rotate;
		this.release = release;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!release) {
			if (!negative) {
				if (changeX)
					camera.setChangeXP(true);
				if (changeY)
					camera.setChangeYP(true);
				if (changeZ)
					camera.setChangeZP(true);
				if (rotate)
					camera.setRotateP(true);
			} else {
				if (changeX)
					camera.setChangeXN(true);
				if (changeY)
					camera.setChangeYN(true);
				if (changeZ)
					camera.setChangeZN(true);
				if (rotate)
					camera.setRotateN(true);
			}
		} else {
			if (!negative) {
				if (changeX)
					camera.setChangeXP(false);
				if (changeY)
					camera.setChangeYP(false);
				if (changeZ)
					camera.setChangeZP(false);
				if (rotate)
					camera.setRotateP(false);
			} else {
				if (changeX)
					camera.setChangeXN(false);
				if (changeY)
					camera.setChangeYN(false);
				if (changeZ)
					camera.setChangeZN(false);
				if (rotate)
					camera.setRotateN(false);
			}
		}
	}

}
