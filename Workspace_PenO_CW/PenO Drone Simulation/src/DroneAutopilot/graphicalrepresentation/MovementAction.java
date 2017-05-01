package DroneAutopilot.graphicalrepresentation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class MovementAction extends AbstractAction {
	private GeneralCameraAP camera;
	private boolean changeX, changeY, changeZ, negative, rotate;

	public MovementAction(GeneralCameraAP cam, boolean x, boolean y, boolean z, boolean negative, boolean rotate) {
		camera = cam;
		this.changeX = x;
		this.changeY = y;
		this.changeZ = z;
		this.negative = negative;
		this.rotate = rotate;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		float distance;
		if (!rotate)
			distance = (float) 1 / 30;
		else
			distance = 1;
		if (negative)
			distance = -distance;
		if (changeX)
			camera.setEyeX(distance + camera.getEyeX());
		if (changeY)
			camera.setEyeY(distance + camera.getEyeY());
		if (changeZ)
			camera.setEyeZ(distance + camera.getEyeZ());
		if (rotate)
			camera.setYaw(distance + camera.getYaw());
	}

}
