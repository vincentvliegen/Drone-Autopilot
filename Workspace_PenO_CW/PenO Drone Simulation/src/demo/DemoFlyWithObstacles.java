package demo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import DroneAutopilot.DroneAutopilotFactory;
import p_en_o_cw_2016.AutopilotFactory;
import simulator.GUI.GUI;
import simulator.generator.Generator_V2;
import simulator.world.World;
import simulator.world.WorldParser_v2;

public class DemoFlyWithObstacles {

	public static void main(String[] args) throws Exception{
		DemoEnum en = DemoEnum.HIT_OBJECT_WITH_OBSTACLE;
		String[] strArr = new String[]{en.toString()};
		Generator_V2.main(strArr);
		
		WorldParser_v2 world = new WorldParser_v2();
		world.setDemoEnum(en);
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