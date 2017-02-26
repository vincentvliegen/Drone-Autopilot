package simulator.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WorldEditor implements MouseListener{
	private int mouseLocationX = 0;
	private int mouseLocationY = 0;
	private boolean mousePressed = false;

	public WorldEditor() {
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouseLocationX = arg0.getX();
		mouseLocationY = arg0.getY();
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public int getMouseX() {
		return mouseLocationX;
	}

	public int getMouseY() {
		return mouseLocationY;
	}
	
	public boolean wasMousePressed() {
		return mousePressed;
	}

	public void completedMouseCheck() {
		mousePressed = false;
	}
	
}
