package DroneAutopilot;

import oldClasses.ScanObject;
import DroneAutopilot.GUI.GUI;
import DroneAutopilot.GUI.MissionType;
import DroneAutopilot.algoritmes.AvoidObstacles;
import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.mission.*;
import p_en_o_cw_2016.Autopilot;
import p_en_o_cw_2016.Drone;

public class DroneAutopilot implements Autopilot {

	private final Drone drone;
	private final PhysicsCalculations physicsCalculations;//deze mag niet verschillen tussen de missies, omdat de snelheid/positie/wind elk frame moet worden geupdate
	private final PolyhedraCalculations polyhedraCalculations;
	private final GUI GUI;
	
	private final Mission hover;
	private final Mission flyToPosition;
	private final Mission scanObject;
	private final Mission scanObjectNew;
	private final Mission flyToMultiplePositions;
	private final Mission flyToSingleObject;
//	private final Mission severalSpheres;
//	private final Mission severalObjects;
	
	private boolean firstHover;
	
	private final static int RED = 16711680;

	
	
	public DroneAutopilot(Drone drone) {
		this.drone = drone;
		this.physicsCalculations = new PhysicsCalculations(this);
		this.polyhedraCalculations = new PolyhedraCalculations(this);
		this.GUI = new GUI();
		this.scanObject = new ScanObject(this);
		this.flyToMultiplePositions = new FlyToMultiplePositions(this);
		this.flyToSingleObject = new FlyToObject(this);
		this.hover = new Hover(this);
		this.flyToPosition = new FlyToPosition(this);
		this.scanObjectNew = new ScanObjectNew(this);
		this.setFirstHover(true);
	}

	/**
	 * Called by the testbed in the AWT/Swing GUI thread at a high (but possibly
	 * variable) frequency, after simulated time has advanced and the simulated
	 * world has been updated to a new state. Simulated time is frozen for the
	 * duration of this call.
	 * 
	 * Check if task "fly to red orb" is enabled, if so the drone will start
	 * flying towards it. Otherwise it will hover until it is given a task.
	 */
	@Override
	public void timeHasPassed() {
		//Moet altijd gebeuren
		getPhysicsCalculations().updateDroneData();
		
		if (getGUI().getMissionType() == MissionType.HOVER) {
			getHover().execute();
			setFirstHover(false);
		}
		else{
			if(getGUI().getMissionType() == MissionType.FLYTOPOSITION){
				this.getFlyToPosition().execute();
			}else if(getGUI().getMissionType() == MissionType.SINGLEOBJECT){
				this.getFlyToSingleObject().execute();
			}else if(getGUI().getMissionType() == MissionType.SCANOBJECT) {
//				this.getScanObject().execute();
				this.scanObjectNew.execute();
			}else if(getGUI().getMissionType() == MissionType.FLYMULTIPLEPOS){
				this.getFlyToMultiplePositions().execute();
			}else if(getGUI().getMissionType() == MissionType.TEST){
				//iets
			}else{
				System.out.println("mission does not exist");
			}
			setFirstHover(true);
		}
	}

	
	//////////GETTERS & SETTERS//////////
	
	/**
	 * Return the drone linked to the Autopilot.
	 */
	public Drone getDrone() {
		return this.drone;
	}


	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}

//	public Mission getSeveralSpheres() {
//		return severalSpheres;
//	}
//
//	public Mission getSeveralObjects() {
//		return severalObjects;
//	}

	public Mission getHover() {
		return hover;
	}

	public static int getRed() {
		return RED;
	}

	public GUI getGUI() {
		return GUI;
	}

	public boolean isFirstHover() {
		return firstHover;
	}

	private void setFirstHover(boolean firstHover) {
		this.firstHover = firstHover;
	}

	public Mission getFlyToPosition() {
		return flyToPosition;
	}

	public Mission getScanObject() {
		return scanObject;
	}
	
	public Mission getFlyToMultiplePositions(){
		return flyToMultiplePositions;
	}

	public Mission getFlyToSingleObject() {
		return flyToSingleObject;
	}

	public PolyhedraCalculations getPolyhedraCalculations() {
		return polyhedraCalculations;
	}

	public Mission getScanObjectNew() {
		return scanObjectNew;
	}	
	
}
