package simulator.test;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.junit.Test;

import simulator.GUI.GUI;
import simulator.world.WorldTest;

public class CollisionTest {
	
	@Test
	public void mainTestCaller() {
		WorldTest world = new WorldTest();
		JFrame frame = new JFrame("Drone Simulator");
		GUI gui = new GUI(world);
		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui, BorderLayout.EAST);
		frame.setSize(1024, 768); 
		frame.setResizable(false); 
		world.requestFocus();
		frame.setVisible(true);
		this.world = world;
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//All tests!
		noHitTest();
		sphereHitTest();
		obstacleHitTest();
		bothHitTest();
		hitSphereOnEdge();
		missSphereOnEdge();
		hitSphereDiagonally();
		missSphereDiagonally();
	}
	
	public void noHitTest() {
		world.resetWorld();
		
		world.getPhysics().run(0);
		assertFalse(world.hitSphere());
		assertFalse(world.hitObstacle());
	}
	
	public void sphereHitTest(){
		world.resetWorld();
		
		world.getSphere().setPosition(dronePos);
			
		world.getPhysics().run(0);
		assertTrue(world.hitSphere());
		assertFalse(world.hitObstacle());
	}
	
	public void obstacleHitTest(){
		world.resetWorld();
		
		world.getObstacle().setPosition(dronePos);
		
		world.getPhysics().run(0);
		assertFalse(world.hitSphere());
		assertTrue(world.hitObstacle());
	}
	
	public void bothHitTest() {
		world.resetWorld();
		
		world.getSphere().setPosition(dronePos);
		world.getObstacle().setPosition(dronePos);
		
		world.getPhysics().run(0);
		assertTrue(world.hitSphere());
		assertTrue(world.hitObstacle());
	}
	
	public void hitSphereOnEdge() {
		world.resetWorld();
		
		float[] pos = dronePos.clone();
		pos[0] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() - 0.00001);
		world.getSphere().setPosition(pos);
		
		world.getPhysics().run(0);
		assertTrue(world.hitSphere());
	}
	
	public void missSphereOnEdge() {
		world.resetWorld();
		
		float[] pos = dronePos.clone();
		pos[0] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() + 0.00001);
		world.getSphere().setPosition(pos);
		
		world.getPhysics().run(0);
		assertFalse(world.hitSphere());
	}
	
	public void hitSphereDiagonally() {
		world.resetWorld();
		
		float[] pos = dronePos.clone();
		pos[0] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() - 0.00001)*Math.cos(Math.PI/4);
		pos[1] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() - 0.00001)*Math.sin(Math.PI/4);
		world.getSphere().setPosition(pos);
		
		world.getPhysics().run(0);
		assertTrue(world.hitSphere());
	}
	
	public void missSphereDiagonally() {
		world.resetWorld();
		
		float[] pos = dronePos.clone();
		pos[0] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() + 0.00001)*Math.cos(Math.PI/4);
		pos[1] += (world.getDrones().get(0).getRadius() + world.getSphere().getRadius() + 0.00001)*Math.sin(Math.PI/4);
		world.getSphere().setPosition(pos);
		
		world.getPhysics().run(0);
		assertFalse(world.hitSphere());
	}
	
	private WorldTest world;
	private float[] dronePos = {0f,0f,0f};
}
