package src.simulator;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import simulator.GUI.GUI;
import simulator.world.*;

public class SimulatorMain13 {
	
	public final static void main(String[] args) {
		
		World13 world = new World13();
		JFrame frame = new JFrame("Drone Simulator");
		GUI gui = new GUI(world);
		
		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);
		
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Full-screen
		
		frame.setSize(1024, 768); // width, height
		frame.setResizable(false); //Not resizable
		world.requestFocus();
		world.addKeyListener(World.movement);
		frame.setVisible(true);
		
	}

}