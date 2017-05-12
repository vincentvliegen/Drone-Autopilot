package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	private final ArrayList<double[]> listOfObstacles;
	
	public AvoidObstacles(DroneAutopilot droneAutopilot){
		this.droneAutopilot = droneAutopilot;
		this.polyhedraCalculations = this.getDroneAutopilot().getPolyhedraCalculations();
		this.physicsCalculations = this.getDroneAutopilot().getPhysicsCalculations();
		this.setWidth(0.35*1.5); // De breedte van de drone. * 1.5 veiligheidsmarge
		this.listOfObstacles = new ArrayList<double[]>();
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
//		System.out.println("target: " + Arrays.toString(newTarget));
//		System.out.println("----------");
		return newTarget;
	}
	
		private void determineObstacleOnPath(double[] target){
			//check if an obstacle is blocking the path to the target
			calculateSpheres();
			double[] distanceToObstacles = distancesToSpheres();
			setObstacleOnPath(false);
			setObstacle(null);
			double distanceToCurrentObstacle = 25;//boven 25 afstand zal het niet veel uitmaken :)
			for(int i = 0; i < getListOfObstacles().size(); i++){
				double[] currentObstacle = getListOfObstacles().get(i);
//				System.out.println(Arrays.toString(currentObstacle));
				if(intersectionPathSphere(currentObstacle)){
					this.setObstacleOnPath(true);
					if(distanceToCurrentObstacle > distanceToObstacles[i]){//ligt dit obstakel dichterbij?
						setObstacle(currentObstacle);
						distanceToCurrentObstacle = distanceToObstacles[i];
					}
				}
			}
//			System.out.println("obstacle: " + Arrays.toString(getObstacle()));
//			System.out.println("-----------");
		}

			private void calculateSpheres(){
				HashMap<float[], ArrayList<double[]>> obstacleCorners = this.getPolyhedraCalculations().getObstacleCorners(this.getDroneAutopilot().getDrone().getLeftCamera(), this.getDroneAutopilot().getDrone().getRightCamera());
				Collection<ArrayList<double[]>> corners = obstacleCorners.values();
				Iterator<ArrayList<double[]>> itrCorners = corners.iterator();
				while (itrCorners.hasNext()) {
					//Bereken het middenpunt van de bol:
					ArrayList<double[]> punten = itrCorners.next();
					double[] a = punten.get(0);
					double[] b = punten.get(1);
					double[] c = punten.get(2);
					//afstanden tss punten
					double ab = VectorCalculations.distance(a, b);
					double ac = VectorCalculations.distance(a, c);
					double bc = VectorCalculations.distance(b, c);
					//langste afstand bepalen = 2R -> R = afstand/2 + veiligheidsmarge
					//centrum bol is het midden van dit lijnstuk
					double radius;
					double[] center;
					if(ab >= ac && ab >= bc){//ab is het langst
						radius = ab/2 + this.getWidth();
						center = VectorCalculations.timesScalar(VectorCalculations.sum(a, b), 0.5);
					}else if(ac >= ab && ac >= bc){//ac is het langst
						radius = ac/2 + this.getWidth();
						center = VectorCalculations.timesScalar(VectorCalculations.sum(a, c), 0.5);
					}else{
						radius = bc/2 + this.getWidth();
						center = VectorCalculations.timesScalar(VectorCalculations.sum(b, c), 0.5);
					}
							
					double[] sphere = new double[]{center[0],center[1],center[2],radius};					
					addSphereToList(sphere);
				}				
			}
			
				private void addSphereToList(double[] sphere){
					//check of de bol gedeeltelijk snijdt/overlapt/niet snijdt met een van de vorige bollen
					double[] positionSphere = new double[] {sphere[0],sphere[1],sphere[2]};
					double radiusSphere = sphere[3] - getWidth();
					boolean sphereDoesNotExistYet = true;
					for(int i = 0; i < this.getListOfObstacles().size(); i++){
						double[] obstacle = getListOfObstacles().get(i);
						double[] positionObstacle = new double[] {obstacle[0],obstacle[1],obstacle[2]};
						double radiusObstacle = obstacle[3] - getWidth();
						double distance = VectorCalculations.distance(positionSphere, positionObstacle);
						if(distance > radiusObstacle + radiusSphere){//sphere bestaat nog niet
							continue;
						}else{//sphere snijdt obstacle -> correct obstacle
							getListOfObstacles().remove(i);
							getListOfObstacles().add(i, correctObstacle(sphere, obstacle));
							sphereDoesNotExistYet = false;
							break;
						}
					}
					if(sphereDoesNotExistYet){
						this.getListOfObstacles().add(sphere);
					}
				}
				
					private double[] correctObstacle(double[] sphere, double[] obstacle){
						//om het makkelijk t visualiseren: denkbeeldige lijn, links staat de nieuwe sphere, rechts de oude obstacle
						double[] positionSphere = new double[] {sphere[0],sphere[1],sphere[2]};
						double radiusSphere = sphere[3] - getWidth();
						double[] positionObstacle = new double[] {obstacle[0],obstacle[1],obstacle[2]};
						double radiusObstacle = obstacle[3] - getWidth();
						//linkeruitwijking | distance | rechteruitwijking
						double distance = VectorCalculations.distance(positionObstacle, positionSphere);
						double deviationLeft = Math.max(radiusSphere, radiusObstacle-distance);
						double deviationRight = Math.max(radiusSphere-distance, radiusObstacle);
						
						double newRadius = (deviationLeft + distance + deviationRight)/2;
						double ratio = (newRadius-deviationLeft)/distance; //als je de zone tss sphere en obstacle verdeelt, ligt newPosition op ratio van de sphere kant
						if(ratio < 0){
							ratio = 0;
						}else if(ratio > 1){
							ratio = 1;
						}else if(Double.isNaN(ratio)){//distance = 0 
							ratio = 0;
						}
						double[] newPosition = VectorCalculations.sum(VectorCalculations.timesScalar(positionSphere, 1-ratio), VectorCalculations.timesScalar(positionObstacle, ratio));
						newRadius += getWidth();
//						System.out.println("correctSphere");
//						System.out.println("sphere: " + Arrays.toString(sphere));
//						System.out.println("obstacle: " + Arrays.toString(obstacle));
//						System.out.println("result: " + Arrays.toString(newPosition) + " " + newRadius);
						return new double[] {newPosition[0],newPosition[1],newPosition[2],newRadius};
					}

					
			private double[] distancesToSpheres(){
				ArrayList<double[]> spheres = this.getListOfObstacles();
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
				boolean intersection;
				double[] path = this.getPath();
				double[] positionDrone = this.getPhysicsCalculations().getPosition();
				double[] positionSphere = new double[] {sphere[0],sphere[1],sphere[2]};
				double radiusSphere = sphere[3];
				
				//waar ligt het object het dichtst op de rechte
				double[] directionToObstacle = this.getPhysicsCalculations().getDirectionDroneToPosition(positionSphere);
				if(!VectorCalculations.compareVectors(path, new double[] {0,0,0})){
					double[] positionOnLine = VectorCalculations.sum(VectorCalculations.projectOnAxis(directionToObstacle, path), positionDrone);//deze ligt op de rechte, het dichtst bij obstacle
					double distanceObstacleToLine = VectorCalculations.distance(positionSphere, positionOnLine);
					if(distanceObstacleToLine >= radiusSphere){
						intersection = false;
					}else{
						//de bol snijdt de rechte
						//snijdt hij ook het lijnstuk? we bepalen adhv de afstand tot de rechte en de straal van het obstakel hoe groot het stuk is van de rechte dat binnen de bol ligt
						double cos = distanceObstacleToLine/radiusSphere;
						double sin = Math.sqrt(1-cos*cos);
						double length = sin*radiusSphere;//*2 is lengte stuk onder bol
						//de rechte snijdt de bol in twee punten, elk length afstand van positionOnLine
						double[] unitPath = VectorCalculations.normalise(path);
						double[] position1 = VectorCalculations.sum(positionOnLine, VectorCalculations.timesScalar(unitPath, length));
						double[] position2 = VectorCalculations.sum(positionOnLine, VectorCalculations.timesScalar(unitPath, -length));
						//kijk of een van de posities binnen het lijnstuk liggen of allebei de posities rond het pad liggen
						double distancePos1 = VectorCalculations.distance(position1, positionDrone);
						double distancePos2 = VectorCalculations.distance(position2, positionDrone);	
						double sizePath = VectorCalculations.size(path);
						intersection = false;
						if(sizePath > distancePos1){
							if(VectorCalculations.compareVectors(positionDrone, VectorCalculations.sum(position1, VectorCalculations.timesScalar(unitPath, -distancePos1)))){
								intersection = true;
							}
						}
						if(sizePath > distancePos2){
							if(VectorCalculations.compareVectors(positionDrone, VectorCalculations.sum(position2, VectorCalculations.timesScalar(unitPath, -distancePos2)))){
								intersection = true;
							}
						}
					}
				}else{
					if(this.getPhysicsCalculations().getDistanceDroneToPosition(positionSphere) < radiusSphere){
						intersection = true;
					}else{
						intersection = false;
					}
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
			double[] edgePosition = VectorCalculations.sum(
									positionObstacle,
									VectorCalculations.timesScalar(
											VectorCalculations.normalise(obstacleToPath),
											radiusObstacle));
			//new target ligt 2 verder dan edge, om met enige snelheid de bol te paseren
			double[] directionToEdge = VectorCalculations.sum(edgePosition, VectorCalculations.inverse(this.getPhysicsCalculations().getPosition()));
			double distanceToEdge = VectorCalculations.size(directionToEdge);
			double[] newTarget = VectorCalculations.sum(this.getPhysicsCalculations().getPosition(), VectorCalculations.timesScalar(directionToEdge, (distanceToEdge+2)/distanceToEdge));
					
			setNewTarget(newTarget);
//			System.out.println("---------------");
//			System.out.println("position obstacle: " + Arrays.toString(positionObstacle));
//			System.out.println("radius: " + radiusObstacle);			
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


	public ArrayList<double[]> getListOfObstacles() {
		return listOfObstacles;
	}
		
}