package DroneAutopilot;

import implementedInterfaces.Camera;
import implementedInterfaces.Drone;
import p_en_o_cw_2016.*;
import p_en_o_cw_2016.wireprotocol.TestbedStub;
import java.util.ArrayList;
import java.util.List;

public class PhysicsCalculations {
	
	public PhysicsCalculations(){
		this.cameraSeparation = this.getDrone().getCameraSeparation();
		this.horizontalAngleOfLeftView = this.getDrone().getLeftCamera().getHorizontalAngleOfView();
		this.leftCameraWidth = this.getDrone().getLeftCamera().getWidth();
	}
	

	private float cameraSeparation;
	private float horizontalAngleOfLeftView;
	private int leftCameraWidth;
	
	//TODO zwaartepunt kunnen opvragen
	public int getX1(){
		//xafstand linker tussen zwaartepunt en middelpunt
		return 0;
	}
	
	//TODO zwaartepunt kunnen opvragen
	public int getX2(){
		//xafstand rechter tussen zwaartepunt en middelpunt
		return 0;
	}
	
	//TODO zwaartepunt kunnen opvragen
	public int getY1(){
		//yafstand zwaartepunt en middelpunt
		return 0;
	}
	
	public float getfocalDistance(){
		return (float) ((this.leftCameraWidth/2) / Math.tan(this.horizontalAngleOfLeftView/2));
	}
	
	public float getDepth(){
		return (this.cameraSeparation * this.getfocalDistance())/(this.getX1() - this.getX2());
	}
		
	public float horizontalAngleDeviation(){
		float x = (this.getDepth() * this.getX1()) / this.getfocalDistance();
		float tanAlfa = (x - this.cameraSeparation/2) / this.getDepth();
		return (float) Math.atan(tanAlfa);
	}
	
	public float verticalAngleDeviation(){
		return (float) Math.atan(this.getY1() / this.getfocalDistance());
	}
	
}
