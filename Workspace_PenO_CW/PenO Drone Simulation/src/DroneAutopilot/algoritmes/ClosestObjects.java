package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.ImageCalculations;
import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class ClosestObjects {

	private final Drone drone;
	private final PhysicsCalculations physicsCalculations;
	private final PolyhedraCalculations polyhedraCalculations;
	
	private final static float minDistance = 1;
	private double[] closestObject;
	private double[] secondObject;

	private final List<double[]> objectList;
	
	
	//////////CONSTRUCTOR//////////

	public ClosestObjects(DroneAutopilot ap) {
		this.drone = ap.getDrone();
		this.physicsCalculations = ap.getPhysicsCalculations();
		this.polyhedraCalculations = ap.getPolyhedraCalculations();
		this.objectList = new ArrayList<double[]>();
	}
	
	
	//////////OBJECTS//////////
	
	public void addVisibleObjects() {//[0] links [1] rechts
		HashMap<float[], double[]> visibleCogs = this.getPolyhedraCalculations().newCOGmethod(this.getDrone().getLeftCamera(),this.getDrone().getRightCamera());
		for (float[] color : visibleCogs.keySet()) {
				this.updateObjectList(visibleCogs.get(color));
		}
	}
	
		//functie die controleert of object van geg coordinaten al eerder gedetecteerd is
		private void updateObjectList(double[] coords) {
			for (double[] checkcoords : this.getObjectList()) {
				if (VectorCalculations.distance(coords, checkcoords) >= ClosestObjects.getMindistance()) {
					this.getObjectList().add(coords);
				}
			}
		}

		
	public void determineClosestObject() {
		int size = this.getObjectList().size();
		if (size != 0) {
			double[] distances = new double[size];
			int i = 0;
			for (double[] coord : this.getObjectList()) {
				distances[i] = this.getPhysicsCalculations().getDistanceDroneToPosition(coord);
				i++;
			}
			int index = this.indexMinValueArray(distances);
			this.setClosestObject(this.getObjectList().get(index));
		}
	}
	
		private int indexMinValueArray(double[] array) {
			int index = 0;
			for (int i = 0; i < array.length; i++) {
				if (array[i] < array[index]) {
					index = i;
				}
			}
			return index;
		}

	
	public void determineSecondObject() throws NullPointerException {
		int size = this.getObjectList().size() - 1;
		if (size != 0) {
			double[] distances = new double[size];
			int i = 0;
			for (double[] coord : this.getObjectList()) {
				distances[i] = VectorCalculations.distance(coord,this.getClosestObject());
				i++;
			}
			int index = (int) this.twoHighestValues(distances, 2, true);
			this.setSecondObject(this.getObjectList().get(index));
		}
	}
		
		//Berekent twee hoogste values van array en geeft hiervan (afhankelijk van de argumenten) de waarde of index van terug
		private double twoHighestValues(double[] array, int value, boolean index) {
			double high1 = 0;
			double high2 = 0;
			int index1 = 0;
			int index2 = 0;
			for (int i = 0; i < array.length; i++) {
				if (array[i] >= high1) {
					high2 = high1;
					index2 = index1;
					high1 = array[i];
					index1 = i;
				} else if (array[i] >= high2) {
					high2 = array[i];
					index2 = i;
				}
			}
			if (value == 1) {
				if (index) {
					return index1;
				} else {
					return high1;
				}
			} else if (value == 2) {
				if (index) {
					return index2;
				} else {
					return high2;
				}
			}
			return -1;
		}
		
	
	////////// GETTERS & SETTERS//////////
	
	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}

	public Drone getDrone() {
		return drone;
	}
		
	public PolyhedraCalculations getPolyhedraCalculations() {
		return polyhedraCalculations;
	}
	
	public List<double[]> getObjectList() {
		return objectList;
	}
	
	public double[] getClosestObject() {
		return closestObject;
	}

	public void setClosestObject(double[] closestObject) {
		this.closestObject = closestObject;
	}
	
	public double[] getSecondObject() {
		return secondObject;
	}

	public void setSecondObject(double[] secondObject) {
		this.secondObject = secondObject;
	}



	public static float getMindistance() {
		return minDistance;
	}
	

}
