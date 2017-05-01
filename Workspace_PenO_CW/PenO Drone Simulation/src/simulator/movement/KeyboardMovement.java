package simulator.movement;

import simulator.objects.*;

public class KeyboardMovement{
	
	public double x=0,y=0,z=0;
	public boolean left, right, up, down, front, back;
	public boolean objFront, objLeft, objRight, objDown, objUp, objBack;
	public WorldObject object = null;
	public KeyboardMovement(){}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
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

}
