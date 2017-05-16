package DroneAutopilot.scanning;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import DroneAutopilot.DroneAutopilot;
import DroneAutopilot.calculations.PhysicsCalculations;

public class FlyScan {

	private final PhysicsCalculations physicsCalc;
	private double[] centerEdge;
	private double[] gravityPoint;
	
	public FlyScan(DroneAutopilot droneAutopilot){
		this.physicsCalc = droneAutopilot.getPhysicsCalculations();
	}
	
	//Stap 1: zoek tot ge het doel tegenkomt
	//Stap 2: scan:
	// a) zoek zwaartepunt driehoek: trek rechte tussen drone en zwaartepunt: leg centerpunt iets verder
	// b) trek rechte tussen centerpunt en midden ribbe: zoek punt met afstand 1
	// c) vlieg naar dat punt + kijk naar centerpunt
	
	public void execute(){
		// set middelpoint
		// set gravitypoint
		double[] nextpos = this.calculateNextPos();
		double[] nextdir = this.calculateNextDir(nextpos);
		this.getPhysics().updateMovement(nextpos, nextdir);
		
	}
	
	private double[] calculateNextPos(){
		//centerpunt
		double k1 = this.calculateK(getGravityPoint(), this.getPhysics().getPosition(), 0.2) ;
		double[] centerpoint = this.calculatePointatRechte(this.getGravityPoint(), this.getPhysics().getPosition(), k1);
		//next pos
		double k2 = this.calculateK(getCenterEdgePoint(), centerpoint, 1);
		double[] nextPos = this.calculatePointatRechte(getCenterEdgePoint(), centerpoint, k2);
		
		return nextPos;
	}
	
	private double[] calculateNextDir(double[] nextPos){
		double[] newDir = new double[]{0,0,0};
		newDir[0] = nextPos[0] - getCenterEdgePoint()[0];
		newDir[1] = nextPos[1] - getCenterEdgePoint()[1];
		newDir[2] = nextPos[3] - getCenterEdgePoint()[2];
		return newDir;
	}	
	
	private double calculateK(double[] p1, double[] p2, double d){
		double k = 1+ d/(Math.sqrt(Math.pow(p1[0]-p2[0], 2)+Math.pow(p1[1]-p2[1], 2)+Math.pow(p1[2]-p2[2], 2)));
		return k;
	}
	
	private double[] calculatePointatRechte(double[] p1, double[] p2, double k){
		double[] point = new double[]{0,0,0};
		point[0] = p1[0] + k*(p2[0]-p1[0]);
		point[1] = p1[1] + k*(p2[1]-p1[1]);
		point[2] = p1[2] + k*(p2[2]-p1[2]);
		return point;
	}
	
	
	//GETTERS EN SETTERS//
	
	private double[] getGravityPoint(){
		return this.gravityPoint;
	}
	
	private void setGravityPoint(double[] gravity){
		this.gravityPoint = gravity;
	}
	
	private double[] getCenterEdgePoint(){
		return this.centerEdge;
	}
	
	private void setMiddlePoint(double[] centerEdge){
		this.centerEdge = centerEdge;
	}
	
	private PhysicsCalculations getPhysics() {
		return this.physicsCalc;
	}
}
