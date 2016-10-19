package simulatietestjes;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener{
	
	public double x=0,y=0,z=0;
	public boolean left, right, up, down, front, back, rotateUp, rotateDown, rotateRight, rotateLeft, rotateZ1, rotateZ2;
	// public int[] array={0,0,0};
	private float rotateX, rotateY, rotateZ;
	public Camera(){}
	
	public float getRotateX(){
		return rotateX;
	}
	
	public float getRotateY(){
		return rotateY;
	}
	
	public float getRotateZ(){
		return rotateZ;
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
		else if((key.getKeyCode()== KeyEvent.VK_RIGHT))
			right = true;
		else if((key.getKeyCode()== KeyEvent.VK_UP))
			front = true;
		else if((key.getKeyCode()== KeyEvent.VK_DOWN))
			back = true;	
		else if((key.getKeyCode()== KeyEvent.VK_P))
			up = true;
		else if((key.getKeyCode()== KeyEvent.VK_M))
			down = true;	
		else if((key.getKeyCode()== KeyEvent.VK_E))
			rotateUp = true;
		else if((key.getKeyCode()== KeyEvent.VK_D))
			rotateDown = true;
		else if((key.getKeyCode()== KeyEvent.VK_F))
			rotateRight = true;
		else if((key.getKeyCode()== KeyEvent.VK_S))
			rotateLeft = true;
		else if((key.getKeyCode()== KeyEvent.VK_A))
			rotateZ1 = true;
		else if((key.getKeyCode()== KeyEvent.VK_Z))
			rotateZ2 = true;
		else if (key.getKeyCode()== KeyEvent.VK_SPACE)
			rotateX = rotateY = rotateZ = 0;
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
		else if((key.getKeyCode()== KeyEvent.VK_P))
			up = false;
		else if((key.getKeyCode()== KeyEvent.VK_M))
			down = false;	
		else if((key.getKeyCode()== KeyEvent.VK_E))
			rotateUp = false;
		else if((key.getKeyCode()== KeyEvent.VK_D))
			rotateDown = false;
		else if((key.getKeyCode()== KeyEvent.VK_F))
			rotateRight = false;
		else if((key.getKeyCode()== KeyEvent.VK_A))
			rotateZ1 = false;
		else if((key.getKeyCode()== KeyEvent.VK_Z))
			rotateZ2 = false;
		else if((key.getKeyCode()== KeyEvent.VK_S))
			rotateLeft = false;
	}
	
	public void update(){
		if(right){
			x -= 1;
		}
		else if(left){
			x += 1;
		}
		else if(up){
			y -= 1;
		}
		else if(down){
			y += 1;
		}
		else if(front){
			z -= 1;
		}
		else if(back){
			z += 1;
		}
		else if(rotateUp){
			 rotateX -=10;
		}
		else if(rotateDown){
			rotateX +=10;
		}
		else if(rotateRight){
			rotateY += 10;
		}
		else if(rotateLeft){
			rotateY -= 10;
		}
		else if(rotateZ1){
			rotateZ += 10;
		}
		else if(rotateZ2){
			rotateZ -= 10;
		}
		
	}

}
