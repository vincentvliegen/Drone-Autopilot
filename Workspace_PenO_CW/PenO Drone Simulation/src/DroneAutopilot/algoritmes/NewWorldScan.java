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
	private float[] newDirectionOfView;
	private HashMap<float[],ArrayList<float[]>> colorAndCogs;

	public NewWorldScan(PhysicsCalculations physicsCalculations) {
		this.physicsCalc = physicsCalculations;
		this.drone = getPhysics().getDrone();
		this.polyCalc = new PolyhedraCalculations();
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
		//TODO check of rotatie juist is
		private void calculateNewDirectionOfView(){// draait met een hoek die overeenkomt met de helft van de horizontale hoek van het gezichtsveld
			float cameraAngle = this.getDrone().getLeftCamera().getHorizontalAngleOfView();
			float[][] rotationMatrix = VectorCalculations.createRotationMatrix(cameraAngle/2, 0, 0);//dit zorgt ervoor dat je een vector kan jawen
			float[] newViewDrone = VectorCalculations.rotate(rotationMatrix, new float[] {0,0,-1});//dit is de oorspronkelijke view gejawd
			float[] newViewWorld = this.getPhysics().vectorDroneToWorld(newViewDrone);//omgezet naar het wereldassenstelsel
			setNewDirectionOfView(newViewWorld);
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

	public float[] getNewDirectionOfView() {
		return newDirectionOfView;
	}

	public void setNewDirectionOfView(float[] newDirectionOfView) {
		this.newDirectionOfView = newDirectionOfView;
	}

	public HashMap<float[], ArrayList<float[]>> getColorAndCogs() {
		return colorAndCogs;
	}

	public void setColorAndCogs(HashMap<float[], ArrayList<float[]>> colorAndCogs) {
		this.colorAndCogs = colorAndCogs;
	}

}
