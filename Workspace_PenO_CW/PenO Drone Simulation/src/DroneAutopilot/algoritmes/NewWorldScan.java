package DroneAutopilot.algoritmes;

import java.util.ArrayList;
import java.util.HashMap;

import DroneAutopilot.calculations.PhysicsCalculations;
import DroneAutopilot.calculations.PolyhedraCalculations;
import DroneAutopilot.calculations.VectorCalculations;
import p_en_o_cw_2016.Drone;

public class NewWorldScan {

	private final Drone drone;
	private final  PhysicsCalculations physicsCalc;
	private final PolyhedraCalculations polyCalc;
	
	private boolean finished;
	private double[] newDirectionOfView;
	private HashMap<float[],ArrayList<double[]>> colorAndCogs;

	public NewWorldScan(PhysicsCalculations physicsCalculations) {
		this.physicsCalc = physicsCalculations;
		this.drone = getPhysics().getDrone();
		this.polyCalc = new PolyhedraCalculations(this.getPhysics().getDroneAutopilot());
	}

	/**
	 * de scan kijkt of er bruikbare punten zijn in het beeld (zwaartepunten). 
	 * Als er geen bruikbare punten zijn, wordt isFinished() false en kan je de nieuwe kijkrichting opvragen via getNewDirectionOfView().
	 * Als er wel bruikbare punten zijn, wordt isFinished() true en kan je deze punten opvragen via getColorsAndCogs().
	 */
	public void scan(){
		setColorAndCogs(this.getPolyCalc().findAllCOGs(this.getDrone().getLeftCamera(), this.getDrone().getRightCamera()));
		if(getColorAndCogs().isEmpty()){//nieuwe viewDirection
			setFinished(false);
			calculateNewDirectionOfView();
		}else{//geef de lijst mee
			setFinished(true);
		}
	}	
	
		private void calculateNewDirectionOfView(){// draait met een hoek die overeenkomt met de helft van de horizontale hoek van het gezichtsveld
			float cameraAngle = this.getDrone().getLeftCamera().getHorizontalAngleOfView();
			double[] newView = VectorCalculations.rotateVectorAroundAxis(this.getPhysics().getDirectionOfView(), this.getPhysics().getOrientationDrone()[1], cameraAngle/2);
			setNewDirectionOfView(newView);
		}
	
	
	//////////GETTERS & SETTERS//////////

	private  PolyhedraCalculations getPolyCalc(){
		return this.polyCalc;
	}

	private  PhysicsCalculations getPhysics(){
		return this.physicsCalc;
	}

	private  Drone getDrone() {
		return this.drone;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public double[] getNewDirectionOfView() {
		return newDirectionOfView;
	}

	public void setNewDirectionOfView(double[] newDirectionOfView) {
		this.newDirectionOfView = newDirectionOfView;
	}

	public HashMap<float[], ArrayList<double[]>> getColorAndCogs() {
		return colorAndCogs;
	}

	public void setColorAndCogs(HashMap<float[], ArrayList<double[]>> colorAndCogs) {
		this.colorAndCogs = colorAndCogs;
	}

}
