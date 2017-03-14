package simulator.movement;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import simulator.objects.*;

public class KeyboardMovement implements KeyListener{
	
	public double x=0,y=0,z=0;
	public boolean left, right, up, down, front, back;
	public boolean objFront, objLeft, objRight, objDown, objUp, objBack;
	public WorldObject object = null;
	public KeyboardMovement(){}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if((key.getKeyCode()== KeyEvent.VK_LEFT))
			left = true;
		else if((key.getKeyCode()== KeyEvent.VK_RIGHT))
			right = true;
		else if((key.getKeyCode()== KeyEvent.VK_UP))
			front = true;
		else if((key.getKeyCode()== KeyEvent.VK_DOWN))
			back = true;	
		else if((key.getKeyCode()== KeyEvent.VK_E))
			up = true;
		else if((key.getKeyCode()== KeyEvent.VK_D))
			down = true;	
		else if((key.getKeyCode()==KeyEvent.VK_I))
			objFront = true;
		else if((key.getKeyCode()==KeyEvent.VK_K))
			objBack = true;
		else if((key.getKeyCode()==KeyEvent.VK_J))
			objLeft = true;
		else if((key.getKeyCode()==KeyEvent.VK_L))
			objRight = true;
		else if((key.getKeyCode()==KeyEvent.VK_U))
			objUp = true;
		else if((key.getKeyCode()==KeyEvent.VK_O))
			objDown = true;
	}

	@Override
	public void keyReleased(KeyEvent key) {
		if((key.getKeyCode()== KeyEvent.VK_LEFT))
			left = false;
		else if((key.getKeyCode()== KeyEvent.VK_RIGHT))
			right = false;
		else if((key.getKeyCode()== KeyEvent.VK_UP))
			front = false;
		else if((key.getKeyCode()== KeyEvent.VK_DOWN))
			back = false;	
		else if((key.getKeyCode()== KeyEvent.VK_E))
			up = false;
		else if((key.getKeyCode()== KeyEvent.VK_D))
			down = false;	
		else if((key.getKeyCode()==KeyEvent.VK_I))
			objFront = false;
		else if((key.getKeyCode()==KeyEvent.VK_K))
			objBack = false;
		else if((key.getKeyCode()==KeyEvent.VK_J))
			objLeft = false;
		else if((key.getKeyCode()==KeyEvent.VK_L))
			objRight = false;
		else if((key.getKeyCode()==KeyEvent.VK_U))
			objUp = false;
		else if((key.getKeyCode()==KeyEvent.VK_O))
			objDown = false;
	}
	
	public void update(float dt){
		if(right){
			z += 1*dt;
		}
		if(left){
			z -= 1*dt;
		}
		if(up){
			y += 1*dt;
		}
		if(down){
			y -= 1*dt;
		}
		if(front){
			x += 1*dt;
		}
		if(back){
			x -= 1*dt;
		}
		if (objBack|| objFront ||objLeft || objRight || objUp|| objDown) {
			if (object == null)
				return;
			float x=0,y=0,z=0;
			if(objRight){
				z += 1*dt;
			}
			if(objLeft){
				z -= 1*dt;
			}
			if(objUp){
				y += 1*dt;
			}
			if(objDown){
				y -= 1*dt;
			}
			if(objFront){
				x += 1*dt;
			}
			if(objBack){
				x -= 1*dt;
			}
			if(object instanceof Sphere) {
				((Sphere)object).setPosition(new float[]{(float)object.getPosition()[0] + x, (float)object.getPosition()[1] + y, (float)object.getPosition()[2] + z});
			} else if (object instanceof Polyhedron) {
				((Polyhedron)object).translatePolyhedronOver(new double[]{(double)x, (double)y, (double)z});
			}
		}
	}

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

}
