package simulator.movement;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import simulator.objects.*;

public class MovementAction extends AbstractAction {

	private KeyboardMovement movement;
	private boolean changeX, changeY, changeZ, negative, object;

	public MovementAction(KeyboardMovement mov, boolean x, boolean y, boolean z, boolean negative, boolean object) {
		movement = mov;
		this.changeX = x;
		this.changeY = y;
		this.changeZ = z;
		this.negative = negative;
		this.object = object;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		double distance = (double)1 / 30;
		if (negative)
			distance = -distance;
		if (!object) {
			if (changeX)
				movement.setX(distance + movement.getX());
			if (changeY)
				movement.setY(distance + movement.getY());
			if (changeZ)
				movement.setZ(distance + movement.getZ());
		} else {
			if (movement.getObject() == null)
				return;
			float x = 0,y = 0, z= 0;
			if (changeX)
				x += distance;
			if (changeY)
				y += distance;
			if (changeZ)
				z += distance; 
			WorldObject object = movement.getObject();
			if(object instanceof Sphere) {
				((Sphere)object).setPosition(new float[]{(float)object.getPosition()[0] + x, (float)object.getPosition()[1] + y, (float)object.getPosition()[2] + z});
			} else if (object instanceof Polyhedron) {
				((Polyhedron)object).translatePolyhedronOver(new double[]{(double)x, (double)y, (double)z});
			}
		}
	}

}
