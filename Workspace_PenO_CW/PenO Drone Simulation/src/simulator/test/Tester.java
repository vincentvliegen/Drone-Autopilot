package simulator.test;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;

import DroneAutopilot.DroneAutopilotFactory;
import simulator.GUI.GUI;
import simulator.world.World;
import simulator.world.WorldForTester;
import simulator.world.WorldParser;

public class Tester {

	WorldForTester world;
	DroneAutopilotFactory factory;
	private JFrame frame;
	private GUI gui;

	@Before
	public void init() {
		factory = new DroneAutopilotFactory();
		world = new WorldForTester(factory);

		world.setAutopilotFactory(factory);
		frame = new JFrame("Drone Simulator");
		gui = new GUI(world);

		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);

		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Full-screen

		frame.setSize(1024, 768); // width, height
		frame.setResizable(false); // Not resizable
		world.requestFocus();
		world.addKeyListener(WorldParser.getMovement());
		world.addMouseListener(World.getEditor());
		frame.setVisible(true);

		waitForAMoment(3);

	}

	private void pauseExec() {
		world.getAnimator().pause();
		System.out.println("paused");
	}

	private void waitForAMoment(int i) {
		try {
			TimeUnit.SECONDS.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testje() {
		System.out.println(Arrays.toString(world.getDrones().get(0).getMovement().getVelocity()));
		System.out.println(Arrays.toString(world.getAp().getPhysicsCalculations().getSpeed()));
		for(int i = 0; i < world.getDrones().get(0).getMovement().getVelocity().length; i ++) {
			float v1 = world.getDrones().get(0).getMovement().getVelocity()[i];
			float v2 = (float) world.getAp().getPhysicsCalculations().getSpeed()[i];
			System.out.println((double) Math.abs(v1 - v2));
			assert((double) Math.abs(v1 - v2) < 0.00001);
			//TODO dit klopt altijd, heeft wss iets met types te maken!
		}

	}
}
