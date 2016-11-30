package DroneAutopilot;

import java.util.ArrayList;

import DroneAutopilot.GUI.GUI;
import DroneAutopilot.GUI.GraphPI;
import DroneAutopilot.controllers.PitchController;
import DroneAutopilot.controllers.RollController;
import DroneAutopilot.controllers.DistanceController;
import DroneAutopilot.controllers.HeightController;
import DroneAutopilot.controllers.YawController;
import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class MoveToTarget{

	private final PhysicsCalculations physicsCalculations;
	private final ImageCalculations imageCalculations;
	private final WorldScan worldScan;
	private final YawController yawPI;
	private final RollController rollPI;
	private final PitchController pitchPI;
	private final HeightController heightPI;
	private final DistanceController distancePI;

	private Drone drone;
	private GUI gui;
	private GraphPI graphPI;
	private boolean yawStarted;
	private boolean rollStarted;
	private boolean pitchStarted;
	private boolean hoverStarted;

	private static final float underBoundary = -0.2f;
	private static final float upperBoundary = 0.2f;

	public MoveToTarget(Drone drone) {
		this.setDrone(drone);
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);
		this.worldScan = new WorldScan(drone);
		this.yawPI = new YawController(30,2);
		this.rollPI = new RollController(1,0);
		this.pitchPI = new PitchController(1,0);
		this.heightPI = new HeightController(3,0);
		this.distancePI = new DistanceController(0.8,0);
	}

	public void execute(int color){
		ArrayList<int[]> leftCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getLeftCamera(), color);
		ArrayList<int[]> rightCameraList = this.getImageCalculations().getPixelsOfColor(this.getDrone().getRightCamera(), color);

		correctRoll();
		if(this.getWorldScan().Scan(leftCameraList, rightCameraList, color) == true){
			System.out.println("True");
			this.targetVisible(leftCameraList, rightCameraList);
		}
	}

	public void correctRoll() {
		if (this.getDrone().getRoll() <= upperBoundary && this.getDrone().getRoll() >= underBoundary) {
			this.getDrone().setRollRate(0);
			this.setRollStarted(false);
		}
		else if (this.getDrone().getRoll() > upperBoundary){
			if(!this.getRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.max(output, -this.getDrone().getMaxYawRate()));
			}
		}
		else if (this.getDrone().getRoll() < underBoundary){
			if(!this.getRollStarted()){
				this.getRollPI().resetSetpoint(0);
				this.setRollStarted(true);
			}else{
				float output = this.getRollPI().calculateRate(this.getDrone().getRoll(), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) this.getDrone().getRoll()*10);
				this.getDrone().setRollRate(Math.min(output, this.getDrone().getMaxYawRate()));
			}
		}
	}

	public void targetVisible(ArrayList<int[]> leftCameraList, ArrayList<int[]> rightCameraList) {
		float[] cogLeft = this.findBestCenterOfGravity(leftCameraList, this
				.getDrone().getLeftCamera());
		System.out.println(cogLeft);
		float[] cogRight = this.findBestCenterOfGravity(rightCameraList, this
				.getDrone().getRightCamera());
		System.out.println(cogRight);
		updateGUI(cogLeft, cogRight);
		this.flyTowardsTarget(cogLeft, cogRight);
	}

	public void correctYaw(float[] cogLeft, float[] cogRight){
		if(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) >= underBoundary 
				&& this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) <= upperBoundary){
			this.setYawStarted(false);
			this.getDrone().setYawRate(0);
		}
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) < upperBoundary) {
			if(!this.getYawStarted()){
				this.getYawPI().resetSetpoint(0);
				this.setYawStarted(true);
			}else{
				float output = -this.getYawPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) this.getDrone().getCurrentTime(), (int) this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight));
				this.getDrone().setYawRate(Math.max(output, -this.getDrone().getMaxYawRate()));
			}
		} 
		else if (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight) > upperBoundary){
			if(!this.getYawStarted()){
				this.getYawPI().resetSetpoint(0);
				this.setYawStarted(true);
			}else{
				float output = -this.getYawPI().calculateRate(this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().horizontalAngleDeviation(cogLeft, cogRight)*10));
				this.getDrone().setYawRate(Math.min(output, this.getDrone().getMaxYawRate()));
			}
		}	
	}

	public void updateGUI(float[] centerOfGravityL, float[] centerOfGravityR) {
		this.getGUI().update(this.getPhysicsCalculations().getDistance(centerOfGravityL, centerOfGravityR));
	}

	public void updategraphPI(int x, int y){
		this.getGraphPI().update(x,y);
	}

	public float[] findBestCenterOfGravity(ArrayList<int[]> pixelsFound,
			Camera camera) {
		float[] cog = { 0, 0 };
		try {
			cog = this.getImageCalculations().centerOfCircle(pixelsFound,
					camera);
		} catch (SmallCircleException e) {
			try {
				cog = this.getImageCalculations().getCOG(pixelsFound);
			} catch (EmptyPositionListException e1) {
				e1.printStackTrace();
			}
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
		return cog;
	}

	public void hover() {
		if(!this.getHoverStarted()){
			this.getPitchPI().resetSetpoint(0);
			this.setHoverStarted(true);
		}
		if (this.getDrone().getPitch() > 0.1) {
			float output = this.getPitchPI().calculateRate(this.getDrone().getPitch(), this.getDrone().getCurrentTime());
			this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getDrone().getPitch())*10);
			this.getDrone().setPitchRate(Math.max(output,-this.getDrone().getMaxPitchRate()));
		} else if (this.getDrone().getPitch() < -0.1) {
			this.getDrone().setPitchRate(this.getDrone().getMaxPitchRate());
			float output = this.getPitchPI().calculateRate(this.getDrone().getPitch(), this.getDrone().getCurrentTime());
			this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getDrone().getPitch())*10);
			this.getDrone().setPitchRate(Math.min(output,this.getDrone().getMaxPitchRate()));
		} else {
			this.getDrone().setPitchRate(0);
			this.setHoverStarted(false);
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight());
		}
	}

	public void correctHeight(float[] cogL, float[] cogR){
		float tanVA = (float) Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)));
		float tanPitch = (float) Math.tan(Math.toRadians(this.getDrone().getPitch()));
		if((this.getDrone().getPitch() > 0 && tanVA <= 1.2*tanPitch && tanVA >= 0.8*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA >= 1.2*tanPitch && tanVA <= 0.8*tanPitch)){
			this.getDrone().setThrust(this.getPhysicsCalculations().getThrust(cogL));
		}else if ((this.getDrone().getPitch() > 0 && tanVA < 0.8*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA < 1.2*tanPitch)) {
			this.getHeightPI().resetSetpoint(tanPitch);
			float output = -this.getHeightPI().calculateRate((float)(Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() + Math.max(output, -this.getDrone().getMaxThrust()));
		}else if ((this.getDrone().getPitch() > 0 && tanVA > 1.2*tanPitch) || (this.getDrone().getPitch() < 0 && tanVA > 0.8*tanPitch)){
			this.getHeightPI().resetSetpoint(tanPitch);
			float output = -this.getHeightPI().calculateRate((float)(Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))), this.getDrone().getCurrentTime());
			//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDepth(cogL,cogR)*Math.tan(Math.toRadians(this.getPhysicsCalculations().verticalAngleDeviation(cogL)))*10));
			this.getDrone().setThrust(Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()+Math.min(output, this.getDrone().getMaxThrust()));
		}	
	}

	public void flyTowardsTarget(float[] cogL, float[] cogR) { 
		this.correctYaw(cogL, cogR);
		this.correctHeight(cogL, cogR);
		this.getPhysicsCalculations().updateAccSpeed(cogL);
		if(this.getPhysicsCalculations().getDistance(cogL, cogR)==0){
			this.hover();
		}else{
			if(this.getPhysicsCalculations().getDistance(cogL, cogR)-this.getDistancePI().getSetpoint()>0.2f){
				this.getDistancePI().resetSetpoint(this.getPhysicsCalculations().getDistance(cogL, cogR)-0.1f);
			}
			if(this.getDrone().getPitch()>0 && !this.getPitchStarted()){
				float newTarget = this.getPhysicsCalculations().getDistance(cogL, cogR)-0.1f;
				if(newTarget<2){
					this.getDistancePI().resetSetpoint(2);
				}else{
					this.getDistancePI().resetSetpoint(newTarget);
				}
				this.getDrone().setPitchRate(0);
				this.setPitchStarted(true);
			}else{
				float output = -this.getDistancePI().calculateRate(this.getPhysicsCalculations().getDistance(cogL, cogR), this.getDrone().getCurrentTime());
				//this.updategraphPI((int) (this.getDrone().getCurrentTime()), (int) (this.getPhysicsCalculations().getDistance(cogL, cogR))*10);
				this.getDrone().setPitchRate(output);
				if(output<0){
					this.setPitchStarted(false);
				}
			}
		}

		//System.out.println("remafstand: " + this.getPhysicsCalculations().getDecelerationDistance());
		//System.out.println("distance: " + this.getPhysicsCalculations().getDistance(cogL, cogR));

		//if-statement die deceleration uitschakelt als 1.1 of 1.2 niet
		//		if(this.getGUI().lastOrbEnabled){
		//			if (this.getPhysicsCalculations().getDistance(cogL, cogR) <= this.getPhysicsCalculations().getDecelerationDistance()) {
		//				this.startDeceleration(cogL,cogR);
		//				deceleration = true;
		//			}
		//
		//			if(Math.abs(this.getPhysicsCalculations().getSpeed())<=0.3 && deceleration){
		//				this.hover();
		//			}
		//			else if(deceleration){
		//				this.startDeceleration(cogL, cogR);
		//			}}

	}

	public void startDeceleration(float[] cogL, float[] cogR) {
		float partAngleView = this.getDrone().getLeftCamera().getVerticalAngleOfView()/20;
		if(this.getDrone().getPitch()>-partAngleView){
			this.getDrone().setPitchRate(-this.getDrone().getMaxPitchRate());
			System.out.println("TRGPITCHEN");
		}else{
			this.getDrone().setPitchRate(0);
		}
	}

	public final PhysicsCalculations getPhysicsCalculations() {
		return this.physicsCalculations;
	}

	public final ImageCalculations getImageCalculations() {
		return this.imageCalculations;
	}

	public final YawController getYawPI() {
		return this.yawPI;
	}

	public final RollController getRollPI() {
		return this.rollPI;
	}

	public final PitchController getPitchPI() {
		return this.pitchPI;
	}

	public final HeightController getHeightPI() {
		return this.heightPI;
	}

	public final DistanceController getDistancePI() {
		return this.distancePI;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	public Drone getDrone() {
		return this.drone;
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	public GUI getGUI() {
		return this.gui;
	}

	public void setGraphPI(GraphPI graphPID) {
		this.graphPI = graphPID;
	}

	public GraphPI getGraphPI() {
		return this.graphPI;
	}

	public void setYawStarted(boolean isStarted){
		this.yawStarted = isStarted;
	}

	public boolean getYawStarted(){
		return this.yawStarted;
	}

	public void setRollStarted(boolean isStarted){
		this.rollStarted = isStarted;
	}

	public boolean getRollStarted(){
		return this.rollStarted;
	}

	public void setPitchStarted(boolean isStarted){
		this.pitchStarted = isStarted;
	}

	public boolean getPitchStarted(){
		return this.pitchStarted;
	}

	public void setHoverStarted(boolean isStarted){
		this.hoverStarted = isStarted;
	}

	public boolean getHoverStarted(){
		return this.hoverStarted;
	}

	public WorldScan getWorldScan() {
		return worldScan;
	}
}
