package simulator.movement;

import simulator.objects.*;

public class KeyboardMovement{
	
	public double x=0,y=0,z=0;
	public boolean changeXP, changeYP, changeZP, changeXN, changeYN, changeZN, changeObject;
	public boolean objFront, objLeft, objRight, objDown, objUp, objBack;
	public WorldObject object = null;
	public KeyboardMovement(){}

	public void setChangeXP(boolean x) {
		this.changeXP = x;
	}

	public void setChangeYP(boolean y) {
		this.changeYP = y;
	}

	public void setChangeZP(boolean z) {
		this.changeZP = z;
	}
	
	public void setChangeXN(boolean x) {
		this.changeXN = x;
	}

	public void setChangeYN(boolean y) {
		this.changeYN = y;
	}

	public void setChangeZN(boolean z) {
		this.changeZN = z;
	}
	
	public void setChangeObj(boolean ob) {
		changeObject = ob;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public void setObject(WorldObject object){
		this.object = object;
	}
	
	public WorldObject getObject() {
		return object;
	}

	public void update(float dt) {
		double distance = dt;
		if (!changeObject) {
			if (changeXP)
				x += distance;
			if (changeYP)
				y += distance;
			if (changeZP)
				z += distance;
			if (changeXN)
				x -= distance;
			if (changeYN)
				y -= distance;
			if (changeZN)
				z -= distance;
			} else {
			if (getObject() == null)
				return;
			float x = 0,y = 0, z= 0;
			if (changeXP)
				x += distance;
			if (changeYP)
				y += distance;
			if (changeZP)
				z += distance; 
			if (changeXN)
				x -= distance;
			if (changeYN)
				y -= distance;
			if (changeZN)
				z -= distance;
			if(object instanceof Sphere) {
				((Sphere)object).setPosition(new float[]{(float)object.getPosition()[0] + x, (float)object.getPosition()[1] + y, (float)object.getPosition()[2] + z});
			} else if (object instanceof Polyhedron) {
				((Polyhedron)object).translatePolyhedronOver(new double[]{(double)x, (double)y, (double)z});
			}
		}
	}
}
