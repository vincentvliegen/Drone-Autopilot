package simulatietestjes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener{
	
	public double x=0,y=0,z=0;
	public boolean left, right, up, down;
	
	public Camera(){}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
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


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent key) {
		if((key.getKeyCode()== KeyEvent.VK_LEFT))
			left = true;
		if((key.getKeyCode()== KeyEvent.VK_RIGHT))
			right = true;
		if((key.getKeyCode()== KeyEvent.VK_UP))
			up = true;
		if((key.getKeyCode()== KeyEvent.VK_DOWN))
			down = true;	
	}


	@Override
	public void keyReleased(KeyEvent key) {
		if((key.getKeyCode()== KeyEvent.VK_LEFT))
			left = false;
		if((key.getKeyCode()== KeyEvent.VK_RIGHT))
			right = false;
		if((key.getKeyCode()== KeyEvent.VK_UP))
			up = false;
		if((key.getKeyCode()== KeyEvent.VK_DOWN))
			down = false;	
	}
	
	public void update(){
		if(right){
			x -= 0.2;
		}
		if(left){
			x += 0.2;
		}
		if(up){
			y -= 0.2;
		}
		if(down){
			y += 0.2;
		}
	}

}
