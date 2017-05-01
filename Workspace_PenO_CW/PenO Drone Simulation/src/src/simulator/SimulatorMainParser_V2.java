package src.simulator;

import java.awt.BorderLayout;
import com.jogamp.newt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import p_en_o_cw_2016.*;
import simulator.GUI.GUI;
import simulator.world.*;
import DroneAutopilot.DroneAutopilotFactory;

public class SimulatorMainParser_V2 {
	
	public final static void main(String[] args) {
		
		WorldParser_v2 world = new WorldParser_v2();
		//Change this autopilotFactory to change to other autopilot
		AutopilotFactory factory = new DroneAutopilotFactory();
		world.setAutopilotFactory(factory);
		JFrame frame = new JFrame("Drone Simulator");
		GUI gui = new GUI(world);
		
		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);

		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Full-screen
		
		frame.setSize(1024, 768); // width, height
		frame.setResizable(false); //Not resizable
		world.setJPanel((JPanel) frame.getContentPane());
		world.requestFocus();
		world.addMouseListener(World.getEditor());
		frame.setVisible(true);
		
	}

}