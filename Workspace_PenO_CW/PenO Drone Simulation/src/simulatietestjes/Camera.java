package simulatietestjes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener{
	
	public double x=0,y=0,z=0, angle;
	public boolean left, right, up, down, rotateUp, rotateDown, rotateRight, rotateLeft;
	public int[] array={0,0,0};
	public Camera(){}

	public double getAngle() {
		return angle;
	}
	
	public int[] getArray(){
		return this.array;
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
		if((key.getKeyCode()== KeyEvent.VK_E))
			rotateUp = true;
		if((key.getKeyCode()== KeyEvent.VK_D))
			rotateDown = true;
		if((key.getKeyCode()== KeyEvent.VK_F))
			rotateRight = true;
		if((key.getKeyCode()== KeyEvent.VK_S))
			rotateLeft = true;
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
		if((key.getKeyCode()== KeyEvent.VK_E))
			rotateUp = false;
		if((key.getKeyCode()== KeyEvent.VK_D))
			rotateDown = false;
		if((key.getKeyCode()== KeyEvent.VK_F))
			rotateRight = false;
		if((key.getKeyCode()== KeyEvent.VK_S))
			rotateLeft = false;
	}
	
	public void update(){
		if(right){
			x -= 0.2;
		}
		if(left){
			x += 0.2;
		}
		if(up){
			z -= 0.2;
		}
		if(down){
			z += 0.2;
		}
		if(rotateUp){
			 angle += 5;
			 array[0]=1;
			 array[1]=0;
			 array[2]=0;
		}
		if(rotateDown){
			angle -= 5;
			array[0]=1;
			 array[1]=0;
			 array[2]=0;
			
		}
		if(rotateRight){
			angle += 5;
			array[0]=0;
			 array[1]=1;
			 array[2]=0;
		}
		if(rotateLeft){
			angle -= 5;
			array[0]=0;
			 array[1]=1;
			 array[2]=0;
		}
		
	}

}
