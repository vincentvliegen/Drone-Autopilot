package src.simulator;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import simulator.GUI.GUI;
import simulator.world.*;

public class SimulatorMain12 {
	
	public final static void main(String[] args) {
		
		World12 world = new World12();
		JFrame frame = new JFrame("Drone Simulator");
		GUI gui = new GUI(world);
		
		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		world.requestFocus();
		world.addKeyListener(World12.movement);
		frame.setVisible(true);
		
	}

}