package src.simulator;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import simulator.GUI.GUI;
import simulator.world.*;

public class SimulatorMain11 {
	
	public final static void main(String[] args) {
		
		World11 world = new World11();
		JFrame frame = new JFrame("Drone Simulator");
		GUI gui = new GUI(world);
		
		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);
		
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Full-screen
		
		frame.setSize(1024, 768); // width, height
		frame.setResizable(false); //Not resizable
		world.requestFocus();
		world.addKeyListener(World11.movement);
		frame.setVisible(true);
		
	}

}
