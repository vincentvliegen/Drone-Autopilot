package simulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;

import simulatietestjes.FlyingSpace;
import simulator.GUI.GUI;
import simulator.world.World11;

public class SimulatorMain {

	
	/*
	 * Zal waarschijnlijk eerder als testmain voor de Simulator dienen, hoeft niet noodzakelijk 
	 * de definitieve main te worden ..
	 */
	
	
	public final static void main(String[] args) {
		GLCapabilities capabilities = World11.createGLCapabilities();
		FlyingSpace canvas = new FlyingSpace(capabilities, 800, 500);
		JFrame frame = new JFrame("Mini JOGL Demo (breed)");
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		//frame.add(gui);
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//JDesktopPane desktopPane = new JDesktopPane();
		GUI gui = new GUI();
		//desktopPane.add(gui);
		canvas.requestFocus();
		canvas.addKeyListener(World11.camera);
	}

}
