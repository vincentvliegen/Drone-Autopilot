package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
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
		float norm = this.norm(getViewDirection());
		float x = (float) (norm*Math.cos(Math.toRadians(this.getCameraWidth())));
		float z = (float) (norm*Math.sin(Math.toRadians(this.getCameraWidth())));
		float[] newDirection = {x,getViewDirection()[1],z};
		this.getPhysics().updateOrientation(newDirection);
		
		//checken rond of iets gevonden
		if (this.degreesTurned>420){
			 this.getPhysics().updatePosition(new float[]{0,0,0});
		}else{
			float inwendigProduct = this.dotProduct(this.getViewDirection(), this.getPreviousView());
			float angle = inwendigProduct / (this.norm(this.getViewDirection())*this.norm(this.getPreviousView()));
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
	
	private float dotProduct(float[] a, float[] b){
		float sum = 0;
        for (int i = 0; i < a.length; i++)
            sum = sum + (a[i] * b[i]);
        return sum;
	}
	
	private float norm(float[] a){
		return (float) Math.sqrt(Math.pow(a[0],2) + Math.pow(a[1], 2) + Math.pow(a[2], 2));
	}
	
	
	private float[] getViewDirection(){
		return this.getPhysics().getDirectionOfView();
	}
	private int getCameraWidth(){
		return this.getDrone().getLeftCamera().getWidth();
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
