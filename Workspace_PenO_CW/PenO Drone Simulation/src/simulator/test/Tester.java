package simulator.test;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import DroneAutopilot.DroneAutopilotFactory;
import simulator.GUI.GUI;
import simulator.world.World;
import simulator.world.WorldForTester;
import simulator.world.WorldParser;

public class Tester {

	static WorldForTester world;
	static DroneAutopilotFactory factory;
	private static JFrame frame;
	private static GUI gui;


	@BeforeClass
	public static void init() {
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
		world.addMouseListener(World.getEditor());
		frame.setVisible(true);

		waitForAMoment(3);
		pauseExec();

	}

	private static void pauseExec() {
		world.getAnimator().pause();
		System.out.println("-------------------");
		System.out.println("paused");
		System.out.println("-------------------");
	}

	private static void waitForAMoment(int i) {
		try {
			TimeUnit.SECONDS.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testSpeed() {
		System.out.println("Speed:");
//		System.out.println(Arrays.toString(world.getDrones().get(0).getMovement().getVelocity()));
//		System.out.println(Arrays.toString(world.getAp().getPhysicsCalculations().getSpeed()));

		long t= System.currentTimeMillis();
		int flag = 100;
		long flagTime = t + flag;
		long end = t+15000;
		while(System.currentTimeMillis() < end) {
			if(System.currentTimeMillis() > flagTime) {
				flagTime = System.currentTimeMillis() + flag;
				for (int i = 0; i < world.getDrones().get(0).getMovement().getVelocity().length; i++) {
					float v1 = world.getDrones().get(0).getMovement().getVelocity()[i];
					float v2 = (float) world.getAp().getPhysicsCalculations().getSpeed()[i];
//					System.out.println((double) Math.abs(v1 - v2));
					assertTrue( Math.abs(v1 - v2) < 0.001);
				}
			}
		}
	}

	@Test
	public void testForces() {
		System.out.println("Forces:");
		System.out.println(Arrays.toString(world.getPhysics().getAcceleration(world.getDrones().get(0))));
		System.out.println(Arrays.toString(world.getAp().getPhysicsCalculations().getExternalForces()));
		for (int i = 0; i < world.getDrones().get(0).getMovement().getVelocity().length; i++) {
			float v1 = world.getPhysics().getAcceleration(world.getDrones().get(0))[i];
			float v2 = (float) world.getAp().getPhysicsCalculations().getExternalForces()[i];
			System.out.println(v2);
			System.out.println(Math.abs(v1 - v2));
			assertTrue( Math.abs(v1 - v2) < 0.00001);


		}
	}

}
