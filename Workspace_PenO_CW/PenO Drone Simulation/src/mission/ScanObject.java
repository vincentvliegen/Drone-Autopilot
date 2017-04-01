package mission;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.graphicalrepresentation.PolyhedronAPData;
import DroneAutopilot.graphicalrepresentation.TriangleAPData;
import DroneAutopilot.graphicalrepresentation.WorldAPData;
import DroneAutopilot.graphicalrepresentation.WorldAPVisual;
import simulator.world.World11;

public class ScanObject extends Mission{

	public float[] previousPosition;
	public float[] target;
	private ArrayList<float[]> drawnTriangles = new ArrayList<float[]>();

	private PolyhedraCalculations polycalc = new PolyhedraCalculations(getDrone());

	JFrame frame;
	public ScanObject(DroneAutopilot droneAutopilot){
		super(droneAutopilot);

		frame = new JFrame("AP world");

		frame.getContentPane().add(world, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(1024, 768); // width, height
		frame.setResizable(false); //Not resizable
		world.requestFocus();
		world.addKeyListener(World11.movement);
		frame.setVisible(true);
		dataWorld.addPolyhedron(datapoly);


	}
	WorldAPData dataWorld = new WorldAPData();
	WorldAPVisual world = new WorldAPVisual(dataWorld);

	PolyhedronAPData datapoly = new PolyhedronAPData();




	@Override
	public void execute() {
		//		if(this.getDroneAutopilot().isFirstHover()){
		//			this.setTarget(new float[] {this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ()});
		//		}
		//		this.getPhysicsCalculations().updatePosition(this.getTarget()); //blijf opdezelfde plaats
		//		//this.getPhysicsCalculations().updateOrientation(this.getPhysicsCalculations().getDirectionOfView());//blijf dezelfde richting kijken

		HashMap<float[], ArrayList<float[]>> outerCorners = polycalc.getMatchingCorners(getDrone().getLeftCamera(), getDrone().getRightCamera());
		System.out.println(outerCorners == null);
		for(float[] key: outerCorners.keySet()) {
			try{
				if(!drawnTriangles.contains(key)){
					int rgb = Color.HSBtoRGB(key[0], key[1], key[2]);
					int rgbinner = Color.HSBtoRGB(outerCorners.get(key).get(3)[0], outerCorners.get(key).get(3)[1], outerCorners.get(key).get(3)[2]);
					datapoly.addTriangleToPolyhedron(new TriangleAPData(outerCorners.get(key).get(0), outerCorners.get(key).get(1), outerCorners.get(key).get(2), rgbinner, rgb));
					drawnTriangles.add(key);
				}
			}
			catch(Exception e) {
			}

		}

	}

	@Override
	public void updateGUI() {
		world.display();

	}




	//////////GETTERS & SETTERS//////////

	public float[] getTarget() {
		return target;
	}

	public void setTarget(float[] target) {
		this.target = target;
	}



}
