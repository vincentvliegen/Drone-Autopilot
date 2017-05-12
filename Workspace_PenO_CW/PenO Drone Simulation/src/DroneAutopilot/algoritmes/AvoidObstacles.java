package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class AvoidObstacles {

	private final DroneAutopilot droneAutopilot;
	private final PolyhedraCalculations polyhedraCalculations;
	private final PhysicsCalculations physicsCalculations;

	private double width;
	
	private double[] path;
	
	private double[] obstacle;
	private double[] newTarget;
	private boolean obstacleOnPath;
	
	public AvoidObstacles(DroneAutopilot droneAutopilot){
		this.droneAutopilot = droneAutopilot;
		this.polyhedraCalculations = this.getDroneAutopilot().getPolyhedraCalculations();
		this.physicsCalculations = this.getDroneAutopilot().getPhysicsCalculations();
		this.setWidth(0.35); // De breedte van de drone.
	}	
	
	//////////CORRECT POSITION//////////
	
	public double[] correctPosition(double[] targetPosition){
		double[] newTarget;
		// check if obstacle on its path
		setPath(VectorCalculations.sum(targetPosition, VectorCalculations.inverse(this.getPhysicsCalculations().getPosition())));
		determineObstacleOnPath(targetPosition);
	
		if(isObstacleOnPath()){
			//determine new target
			calculateNewTarget();
			newTarget = this.getNewTarget();
		}else{
			//nothing
			newTarget = targetPosition;
		}
		return newTarget;
	}
	
		private void determineObstacleOnPath(double[] target){
			//check if an obstacle is blocking the path to the target
			ArrayList<double[]> obstacles = calculateSpheres();
			double[] distanceToObstacles = distancesToSpheres(obstacles);
			setObstacleOnPath(false);
			setObstacle(null);
			double distanceToCurrentObstacle = 25;//boven 25 afstand zal het niet veel uitmaken :)
			for(int i = 0; i < obstacles.size(); i++){
				double[] currentObstacle = obstacles.get(i);
				if(intersectionPathSphere(currentObstacle)){
					this.setObstacleOnPath(true);
					if(distanceToCurrentObstacle > distanceToObstacles[i]){//ligt dit obstakel dichterbij?
						setObstacle(currentObstacle);
						distanceToCurrentObstacle = distanceToObstacles[i];
					}
				}
			}
		}

			//https://gamedev.stackexchange.com/questions/60630/how-do-i-find-the-circumcenter-of-a-triangle-in-3d
			private  ArrayList<double[]> calculateSpheres(){
				HashMap<float[], ArrayList<double[]>> obstacleCorners = this.getPolyhedraCalculations().getObstacleCorners(this.getDroneAutopilot().getDrone().getLeftCamera(), this.getDroneAutopilot().getDrone().getRightCamera());
				ArrayList<double[]> spheres = new ArrayList<double[]>();
				Collection<ArrayList<double[]>> corners = obstacleCorners.values();
				Iterator<ArrayList<double[]>> itrCorners = corners.iterator();
				while (itrCorners.hasNext()) {
					//Bereken het middenpunt van de bol:
					ArrayList<double[]> punten = itrCorners.next();
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
					straal += 1.5*this.getWidth();// + veiligheidsmarge = 1.5 keer een drone
					
					//Sphere bestaat uit het middelpunt(eerste 3 elementen) en de straal.
					double[] sphere = new double[]{circumCenter[0],circumCenter[1],circumCenter[2],straal};
					spheres.add(sphere);
				}
				return spheres;
			}
			
			private double[] distancesToSpheres(ArrayList<double[]> spheres){
				double[] distances = new double[spheres.size()];
				for(int i = 0; i < spheres.size(); i++){
					double[] sphere = spheres.get(i);
					double[] positionSphere = new double[] {sphere[0], sphere[1], sphere[2]};
					double distance = this.getPhysicsCalculations().getDistanceDroneToPosition(positionSphere);
					distance -= sphere[3];//straal er van af trekken
					distances[i] = distance;
				}
				return distances;
			}

			//http://www.ambrsoft.com/TrigoCalc/Sphere/SpherLineIntersection_.htm
			private boolean intersectionPathSphere(double[] sphere){
				boolean intersection = false;
				double[] vector = this.getPath();
				
				//DroneCoordinaten
				double x1 = this.getPhysicsCalculations().getPosition()[0];
				double y1 = this.getPhysicsCalculations().getPosition()[1];
				double z1 = this.getPhysicsCalculations().getPosition()[2];
		
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
			
		private void calculateNewTarget(){
			double[] positionObstacle = new double[] {this.getObstacle()[0], this.getObstacle()[1], this.getObstacle()[2]};
			double radiusObstacle = this.getObstacle()[3];
			//bepaal nu de kortste afstand van het obstakel tot het pad
			double[] directionToObject = VectorCalculations.sum(positionObstacle, VectorCalculations.inverse(this.getPhysicsCalculations().getPosition()));
			double[] obstacleToPath;
			if(VectorCalculations.compareVectors(this.getPath(), new double[] {0,0,0})){
				obstacleToPath = VectorCalculations.inverse(directionToObject);
			}else{
				obstacleToPath = VectorCalculations.inverse(VectorCalculations.projectOnPlane(directionToObject, this.getPath()));
			}
			//de nieuwe positie ligt op afstand radius van het obstakel, volgens obstacleToPath
			double[] newTarget = VectorCalculations.sum(
									positionObstacle,
									VectorCalculations.timesScalar(
											VectorCalculations.normalise(obstacleToPath),
											radiusObstacle));
			setNewTarget(newTarget);
		}
	
		
	//////////GETTERS & SETTERS//////////

	public double getWidth() {
		return width;
	}

	public void setWidth(double d) {
		this.width = d;
	}

	public PolyhedraCalculations getPolyhedraCalculations() {
		return polyhedraCalculations;
	}

	public DroneAutopilot getDroneAutopilot() {
		return droneAutopilot;
	}

	
	public double[] getObstacle() {
		return obstacle;
	}

	
	public void setObstacle(double[] obstacle) {
		this.obstacle = obstacle;
	}


	public double[] getNewTarget() {
		return newTarget;
	}

	
	public void setNewTarget(double[] newTarget) {
		this.newTarget = newTarget;
	}


	public boolean isObstacleOnPath() {
		return obstacleOnPath;
	}

	
	public void setObstacleOnPath(boolean obstacleOnPath) {
		this.obstacleOnPath = obstacleOnPath;
	}


	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}



	public double[] getPath() {
		return path;
	}


	public void setPath(double[] path) {
		this.path = path;
	}
	
}