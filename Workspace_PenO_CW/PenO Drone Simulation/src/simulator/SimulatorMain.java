package simulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;


import simulator.GUI.GUI;
import simulator.world.World11;

public class SimulatorMain {
	
	public final static void main(String[] args) {
		GLCapabilities capabilities = World11.createGLCapabilities();
		
		World11 canvas = new World11(capabilities, 800, 500);
		JFrame frame = new JFrame("Mini JOGL Demo (breed)");
		GUI gui = new GUI();
		
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		canvas.requestFocus();
		canvas.addKeyListener(World11.camera);
		frame.setVisible(true);
		
	}

}
