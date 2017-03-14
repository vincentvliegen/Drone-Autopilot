package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class NewWorldScan {

	private Drone drone;
	private PhysicsCalculations physicsCalc;
	private PolyhedraCalculations polyCalc;
	private float degreesTurned;
	private float[] previousView = null;
	private boolean finished;

	public NewWorldScan(Drone drone) {
		this.setDrone(drone);
		this.setPhysicsCalc(new PhysicsCalculations(drone));
		this.setPolyCalc(new PolyhedraCalculations());
	}

	public void scan(){
		this.setFinishedScan(false);
		if(this.getPreviousView() == null){
			this.setPreviousView(this.getPreviousView());
		}
		float norm = VectorCalculations.size(getViewDirection());
		float x = (float) (norm*Math.cos(Math.toRadians(this.getCameraAngle())));
		float z = (float) (norm*Math.sin(Math.toRadians(this.getCameraAngle())));
		float[] newDirection = {x,getViewDirection()[1],z};
		this.getPhysics().updateOrientation(newDirection);
		
		//checken rond of iets gevonden
		if (this.degreesTurned>420){
			 this.getPhysics().updatePosition(new float[]{0,0,0});
		}else{
			float inwendigProduct = VectorCalculations.dotProduct(this.getViewDirection(), this.getPreviousView());
			float angle = inwendigProduct / (VectorCalculations.size(this.getViewDirection())*VectorCalculations.size(this.getPreviousView()));
			this.degreesTurned += angle;
			this.setPreviousView(this.getViewDirection());
		
			if(this.foundTarget() && 
					this.getPolyCalc().execute(this.getDrone().getLeftCamera(), this.getDrone().getRightCamera()).size() >= 1){
				this.setPreviousView(null);
				this.degreesTurned = 0;
				this.setFinishedScan(true);
			}
		}
	}

	private boolean foundTarget(){
		HashMap<Integer, ArrayList<int[]>> colors = this.getPolyCalc().calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
		if(colors.size()>=1){
			return true;
		}
		return false;
	}
	
	
	private float[] getViewDirection(){
		return this.getPhysics().getDirectionOfView();
	}
	private float getCameraAngle(){
		return this.getDrone().getLeftCamera().getHorizontalAngleOfView();
	}
	public boolean getFinishedScan(){
		return this.finished;
	}
	private void setFinishedScan(boolean finished){
		this.finished = finished;
	}
	private float[] getPreviousView(){
		return this.previousView;
	}
	private void setPreviousView(float[] view){
		this.previousView = view;
	}
	private PolyhedraCalculations getPolyCalc(){
		return this.polyCalc;
	}
	private void setPolyCalc(PolyhedraCalculations polyCalc){
		this.polyCalc = polyCalc;
	}
	private PhysicsCalculations getPhysics(){
		return this.physicsCalc;
	}
	private void setPhysicsCalc(PhysicsCalculations physics){
		this.physicsCalc = physics;
	}
	private Drone getDrone() {
		return this.drone;
	}
	private void setDrone(Drone drone) {
		this.drone = drone;
	}

}
