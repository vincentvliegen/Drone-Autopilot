package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class AvoidObstacles {

	private Drone drone;
	private PolyhedraCalculations polyhedraCalculations;
	private DroneAutopilot droneAutopilot;
	private boolean obstacleInLine;
	private double width;

	public AvoidObstacles(DroneAutopilot droneAutopilot){
		this.setDroneAutopilot(droneAutopilot);
		this.setDrone(this.getDroneAutopilot().getDrone());
		this.setPolyhedraCalculations(new PolyhedraCalculations(droneAutopilot));
		this.setWidth(0.35); // De breedte van de drone.
	}

	public double[] execute(double[] target){
		double[] newTarget = target;
		double[] sphereInLine = this.obtacleInLine(target);
		if(this.isObstacleInLine()==true){
			
			//Voorlopig enkel y waarde aanpassen, dus die berekenen:
			double xSphere = sphereInLine[0];
			double ySphere = sphereInLine[1];
			double zSphere = sphereInLine[2];
			//straal om ervoorbij te vliegen met veiligheidsmarge voor drone
			double straalSphere = sphereInLine[3] + 1.5*this.getWidth();
			
			double newYSphere1 = ySphere+straalSphere;
			double newYSphere2 = ySphere-straalSphere;
			
			if(Math.abs(this.getDrone().getY()-newYSphere1) >= Math.abs(this.getDrone().getY()-newYSphere2)){
				newTarget = new double[]{xSphere,newYSphere2,zSphere};
			}else{
				newTarget = new double[]{xSphere,newYSphere1,zSphere};
			}
		}
		return newTarget;
	}

	//Geeft de bol weer die het vliegen belemmert 
	private double[] obtacleInLine(double[] target){
		this.setObstacleInLine(false);
		double[] dronePosition = new double[]{this.getDrone().getX(), this.getDrone().getY(), this.getDrone().getZ()};
		double[] targetPosVector = VectorCalculations.sum(target, VectorCalculations.inverse(dronePosition)); //Vector van positie naar target
		ArrayList<double[]> spheresAroundObstacle = calculatespheres();
		//Test als de vector een bol snijdt.
		double[] currentSphere = null;
		Iterator itrSpheres = spheresAroundObstacle.iterator();
		while (itrSpheres.hasNext() && this.isObstacleInLine()==false) {
			currentSphere = (double[]) itrSpheres.next();
			if(intersectionVectorSphere(targetPosVector, currentSphere) == false){
				currentSphere = null;
			}else{
				this.setObstacleInLine(true);
			}
		}
		return currentSphere;
	}

	//https://gamedev.stackexchange.com/questions/60630/how-do-i-find-the-circumcenter-of-a-triangle-in-3d
	private  ArrayList<double[]> calculatespheres(){
		HashMap<float[], ArrayList<double[]>> obstacleCorners = this.getPolyhedraCalculations().getObstacleCorners(this.getDrone().getLeftCamera(), this.getDrone().getRightCamera());
		ArrayList<double[]> spheres = new ArrayList<double[]>();
		Collection<ArrayList<double[]>> corners = obstacleCorners.values();
		Iterator itrCorners = corners.iterator();
		while (itrCorners.hasNext()) {
			//Bereken het middenpunt van de bol:
			ArrayList<double[]> punten = (ArrayList<double[]>) itrCorners.next();
			double[] a = punten.get(0);
			double[] b = punten.get(1);
			double[] c = punten.get(2);

			double[] ac = VectorCalculations.sum(c, VectorCalculations.inverse(a));
			double[] ab = VectorCalculations.sum(b, VectorCalculations.inverse(a));
			double[] abCrossac = VectorCalculations.crossProduct(ab, ac);

			double[] teller = VectorCalculations.sum(VectorCalculations.timesScalar(VectorCalculations.crossProduct(abCrossac, ab), Math.pow(VectorCalculations.size(ac),2)), 
					VectorCalculations.timesScalar(VectorCalculations.crossProduct(ac, abCrossac), Math.pow(VectorCalculations.size(ab),2)));
			double noemer = 2 * Math.pow(VectorCalculations.size(abCrossac), 2);
			double[] circumCenter = VectorCalculations.sum(a, VectorCalculations.timesScalar(teller, 1/noemer));
			//Bereken de straal van de bol (grootste afstand naar het punt):
			double straal = Math.max(Math.max(VectorCalculations.distance(a, circumCenter), VectorCalculations.distance(b, circumCenter)), VectorCalculations.distance(c, circumCenter));
			//Sphere bestaat uit het middelpunt(eerste 3 elementen) en de straal.
			double[] sphere = new double[]{circumCenter[0],circumCenter[1],circumCenter[2],straal};
			spheres.add(sphere);
		}
		return spheres;
	}

	//http://www.ambrsoft.com/TrigoCalc/Sphere/SpherLineIntersection_.htm
	private boolean intersectionVectorSphere(double[] vector, double[] sphere){
		boolean intersection = false;

		//DroneCoordinaten
		double x1 = this.getDrone().getX();
		double y1 = this.getDrone().getY();
		double z1 = this.getDrone().getZ();

		//Formules zie site:
		double a = Math.pow(vector[0], 2) + Math.pow(vector[1], 2) + Math.pow(vector[2], 2);
		double b = 2*(vector[0]*(x1-sphere[0])+vector[1]*(y1-sphere[1])+vector[2]*(z1-sphere[2])); 
		double c = Math.pow(sphere[0], 2) + Math.pow(sphere[1], 2) + Math.pow(sphere[2], 2) + Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2) -
				2*(x1*sphere[0]+y1*sphere[1]+z1*sphere[2]) - Math.pow(sphere[3],2);

		if(Math.pow(b, 2)-4*a*c >= 0){
			intersection = true;
		}
		return intersection;
	}
	

	//////////GETTERS & SETTERS//////////

	public boolean isObstacleInLine() {
		return obstacleInLine;
	}

	public void setObstacleInLine(boolean obstacleInLine) {
		this.obstacleInLine = obstacleInLine;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double d) {
		this.width = d;
	}

	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public PolyhedraCalculations getPolyhedraCalculations() {
		return polyhedraCalculations;
	}

	public void setPolyhedraCalculations(PolyhedraCalculations polyhedraCalculations) {
		this.polyhedraCalculations = polyhedraCalculations;
	}

	public DroneAutopilot getDroneAutopilot() {
		return droneAutopilot;
	}

	public void setDroneAutopilot(DroneAutopilot droneAutopilot) {
		this.droneAutopilot = droneAutopilot;
	}
	
}