package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.calculations.ImageCalculations;
import DroneAutopilot.calculations.PhysicsCalculations;
import p_en_o_cw_2016.Drone;

public class AvoidObstacles {

	private Drone drone;
	private ImageCalculations imageCalculations;
	private PhysicsCalculations physicsCalculations;
	private boolean obstacleFound;
	private float shortestDistance;

	public AvoidObstacles(Drone drone){
 		this.setDrone(drone);
 		this.imageCalculations = new ImageCalculations();
 		this.physicsCalculations = new PhysicsCalculations(drone);
 	}
	
	public ArrayList<float[]> closestObstacle(float[] cogLeftTarget, float[] cogRightTarget ){
		shortestDistance = Float.POSITIVE_INFINITY;
		ArrayList<float[]> closestObst = new ArrayList<float[]>();
		HashMap<Integer, ArrayList<float[]>> obstacleCOG = this.removeObstacleFarFromTarget(cogLeftTarget, cogRightTarget);
		if (obstacleCOG.isEmpty()){
			this.setObstacleFound(false);
		}else{
			this.setObstacleFound(true);
			for(int obstacle: obstacleCOG.keySet()){
				float distanceObst = this.getPhysicsCalculations().getDistance(obstacleCOG.get(obstacle).get(0), obstacleCOG.get(obstacle).get(2));
				if (distanceObst< shortestDistance){
					shortestDistance = distanceObst;
					closestObst.clear();
					closestObst.add(obstacleCOG.get(obstacle).get(0));					
					closestObst.add(obstacleCOG.get(obstacle).get(1));
				}
			}
		}
		return closestObst;
	}
	
	
	public  HashMap<Integer, ArrayList<float[]>> removeObstacleFarFromTarget(float[] cogLeftTarget, float[] cogRightTarget ){
		HashMap<Integer, ArrayList<float[]>> obstacleCOG = this.obstaclesCOG();
		float targetHorizDev = this.getPhysicsCalculations().horizontalAngleDeviation(cogLeftTarget, cogRightTarget);
		float targetVertDev = this.getPhysicsCalculations().verticalAngleDeviation(cogLeftTarget);
		float targetDistance = this.getPhysicsCalculations().getDistance(cogLeftTarget, cogRightTarget);
			for (int obstacle: obstacleCOG.keySet()){
				float distanceObst = this.getPhysicsCalculations().getDistance(obstacleCOG.get(obstacle).get(0), obstacleCOG.get(obstacle).get(2));
				float horizDevObst = this.getPhysicsCalculations().horizontalAngleDeviation(obstacleCOG.get(obstacle).get(0), obstacleCOG.get(obstacle).get(2));
				float vertDevObst = this.getPhysicsCalculations().verticalAngleDeviation(obstacleCOG.get(obstacle).get(0));
				
				if (Math.abs(horizDevObst - targetHorizDev) > 2){
					obstacleCOG.remove(obstacle,obstacleCOG.get(obstacle));
				}else if(Math.abs(vertDevObst - targetVertDev )> 2){
					obstacleCOG.remove(obstacle,obstacleCOG.get(obstacle));
				}else if(distanceObst > targetDistance+5){
					obstacleCOG.remove(obstacle,obstacleCOG.get(obstacle));
				}
			}
		return obstacleCOG;
	}
	
	
	public HashMap<Integer, ArrayList<float[]>> obstaclesCOG(){
		HashMap<Integer, ArrayList<float[]>> obstacleCOG = new HashMap<Integer, ArrayList<float[]>>();
		HashMap<Integer, ArrayList<ArrayList<int[]>>> obstacleCoordinates = this.obstacleCoordinates();
		for(int obstacle: obstacleCoordinates.keySet()){
			float[] cogObstLeft = this.getImageCalculations().findBestCenterOfGravity(obstacleCoordinates.get(obstacle).get(0), this.getDrone().getLeftCamera());
			float[] cogObstRight = this.getImageCalculations().findBestCenterOfGravity(obstacleCoordinates.get(obstacle).get(1), this.getDrone().getRightCamera());
			ArrayList<float[]> cogsObst = new ArrayList<float[]>();
			cogsObst.add(cogObstLeft);
			cogsObst.add(cogObstRight);
			obstacleCOG.put(obstacle, cogsObst);
		}
		return obstacleCOG;
	}
	
	public HashMap<Integer, ArrayList<ArrayList<int[]>>> obstacleCoordinates(){
		HashMap<Integer, ArrayList<ArrayList<int[]>>> hashMapOfColorsBoth = new HashMap<Integer, ArrayList<ArrayList<int[]>>>();
		this.getImageCalculations().calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
		HashMap<Integer, ArrayList<int[]>> hashMapOfColorsLeft = this.getImageCalculations().getGreyPixels();
		this.getImageCalculations().calculatePixelsOfEachColor(this.getDrone().getRightCamera());
		HashMap<Integer, ArrayList<int[]>> hashMapOfColorsRight = this.getImageCalculations().getGreyPixels();
		
		//samenvoegen L en R coordinaten van zelfde obstakels
		for(int color: hashMapOfColorsLeft.keySet()){
			if(hashMapOfColorsRight.containsKey(color)){
				ArrayList<ArrayList<int[]>> both = new ArrayList<ArrayList<int[]>>();
				both.add(hashMapOfColorsLeft.get(color));
				both.add(hashMapOfColorsRight.get(color));
				hashMapOfColorsBoth.put(color, both);
			}
		}
		return hashMapOfColorsBoth;
	}	
	
	//////////Getters & Setters//////////
 	
 	public Drone getDrone() {
 		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}
	
	public ImageCalculations getImageCalculations(){
		return imageCalculations;
	}
	
	public void setImageCalculations(ImageCalculations imageCalc){
		this.imageCalculations = imageCalc;
	}
	
	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}
	
	public void setPhysicsCalculations(PhysicsCalculations physicsCalculations) {
		this.physicsCalculations = physicsCalculations;
	}
	
	public void setObstacleFound(boolean found){
		this.obstacleFound = found;
	}
	
	public boolean getObstacleFound(){
		return this.obstacleFound;
	}
}