package DroneAutopilot;

import java.util.ArrayList;

import p_en_o_cw_2016.Drone;

public class PhysicsCalculations {
	
	public PhysicsCalculations(Drone drone){
		this.setDrone(drone);
		timeDistanceList = new ArrayList<float[]>();
		setSpeed(0);
		firstDistanceTime = true;
	}

	private Drone drone;

	public float horizontalAngleDeviation(float[] centerOfGravityL, float[] centerOfGravityR){
		float x = (this.getDepth(centerOfGravityL, centerOfGravityR) * this.getX1(centerOfGravityL)) / this.getfocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth(centerOfGravityL, centerOfGravityR);
		return (float) Math.toDegrees(Math.atan(tanAlfa));
	}
	
	public float verticalAngleDeviation(float[] centerOfGravity){
		return (float) Math.toDegrees(Math.atan(this.getY(centerOfGravity) / this.getfocalDistance()));
	}
	
	public void setDrone(Drone drone){
		this.drone = drone;
	}

	public float getX1(float[] centerofGravityL){
		float distance = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		return distance;
	}

	public float getX2(float[] centerofGravityR){
		float distance = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		return distance;
	}

	/**
	 * The amount of pixels in height of the camera view.
	 */
	public int getCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getfocalDistance()*2);
		return height;
	}

	public float getY(float[] centerofGravity){
		float distance = ((float) this.getCameraHeight())/2 - centerofGravity[1];
		return distance;
	}

	public float getfocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		return focal;
	}

	public float getDepth(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth=0;
		try{
		depth = (this.getDrone().getCameraSeparation() * this.getfocalDistance())/(this.getX1(centerOfGravityL) - this.getX2(centerOfGravityR));
		depth = Math.abs(depth);
		} catch(IllegalArgumentException e){
		}
		return depth;
	}

	public float getVisiblePitch(float[] centerOfGravityL, float[] centerOfGravityR){
		if (this.getDistance(centerOfGravityL, centerOfGravityR) <= getDecelerationDistance()){
			return  (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getDecelerationFactor());
		}
		return (float) ((this.getDrone().getLeftCamera().getVerticalAngleOfView()/2)*getVisibilityFactor());
	}
	
	public float getThrust(float[] cog) {
		float thrust;
		float beta = this.verticalAngleDeviation(cog);
		thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / Math.cos(Math.toRadians(beta)));
//		System.out.println("thrust "+thrust);
//		System.out.println("pitch "+this.getDrone().getPitch());
		return thrust;
	}
	
	public float getDistance(float[] centerOfGravityL, float[]centerOfGravityR){
		float depth = this.getDepth(centerOfGravityL, centerOfGravityR);
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.horizontalAngleDeviation(centerOfGravityL, centerOfGravityR)))*Math.cos(Math.toRadians(this.verticalAngleDeviation(centerOfGravityL)))));
		return distance;
	}
	
	private boolean firstDistanceTime;
	private final static float SpeedCorrector = 3f;//speed is veel te klein om een of andere reden
	
	public float calculateSpeed(float time, float distance){
		float[] newTD = {time,distance};
		timeDistanceList.add(newTD);
		if(firstDistanceTime){
			this.setPreviousTimeDistance(newTD);
			firstDistanceTime = false;
		}
		if(timeDistanceList.size() >= avgcounter){
			
			//methode 1: filteredavg of distances
//			timeDistanceList = filterAvg(timeDistanceList);//kan soms een lege lijst returnen, wanneer deviationLinReg te klein is
//			float avgTime = 0;
//			float avgDistance = 0;
//			float size = timeDistanceList.size();
//			for(int i = timeDistanceList.size()-1; i >= 0; i--){
//				float[] currentTD = timeDistanceList.get(i);
//				avgTime += currentTD[0];
//				avgDistance += currentTD[1];
//				timeDistanceList.remove(i);
//			}
//			avgTime = avgTime/size;
//			avgDistance = avgDistance/size;
//			this.setSpeed(SpeedCorrector* (getPreviousTimeDistance()[1] - avgDistance)/(avgTime - getPreviousTimeDistance()[0]));
//			this.setPreviousTimeDistance(new float[]{avgTime,avgDistance});
//		}
		//System.out.println(distance);
		//System.out.println("speed: " + this.getSpeed());
		
		
		//methode 2: snelheid ifv versnelling
//		float speed = this.getSpeed()+(newTD[0]-previousTimeDistance[0])*this.getAcceleration();
//		this.setSpeed(speed);
			
		//methode 3: lineaire regressie, om te voorspellen wat de huidige afstand is	LIJKT NAUWKEURIGER DAN GEMIDDELDE
			float[] ab = linRegExpectedValue(timeDistanceList);
			timeDistanceList.clear();
			float expectedDistance = ab[0]*newTD[0] + ab[1];
			this.setSpeed((getPreviousTimeDistance()[1] - expectedDistance)/(newTD[0] - getPreviousTimeDistance()[0]));
		}
		return this.getSpeed();
	}
	
	public ArrayList<float[]> filterAvg(ArrayList<float[]> TDList){//gebaseerd op lineaire regressie (hoort eigenlijk zelfs kwadratisch te zijn, nauwkeurig genoeg voor kleinere waardes van avgcounter) (https://en.wikipedia.org/wiki/Simple_linear_regression)
		if(TDList.size() > 2){
			float Sx = 0;
			float Sy = 0;
			float Sxx = 0;
			float Sxy = 0;
			//float Syy = 0;
			int n = TDList.size();
			for(int i = 0; i < n; i++ ){
				float x = TDList.get(i)[0];
				float y = TDList.get(i)[1];
				Sx += x;
				Sy += y;
				Sxx += x*x;
				Sxy += x*y;
			}
			//ax+b = y
			float a = (n*Sxy-Sx*Sy)/(n*Sxx-Sx*Sx);
			float b = (Sy - a*Sx)/n;
			
			//weghalen van uitschieters
			
//			System.out.println("a " + a);
//			System.out.println("b "+b);
//			System.out.println(TDList);
			for(int i = TDList.size()-1; i>=0; i--){
//				System.out.println("x "+TDList.get(i)[0]);
//				System.out.println("y "+TDList.get(i)[1]);
				//te klein
				if((a*TDList.get(i)[0] + b)*(1+deviationLinReg) < TDList.get(i)[1]){
					TDList.remove(i);
				}//te groot
				else if((a*TDList.get(i)[0] + b)*(1-deviationLinReg) > TDList.get(i)[1]){
					TDList.remove(i);
				}
			}
		}
		return TDList;
	}
	
	public float[] linRegExpectedValue(ArrayList<float[]> TDList){
		float Sx = 0;
		float Sy = 0;
		float Sxx = 0;
		float Sxy = 0;
		//float Syy = 0;
		int n = TDList.size();
		for(int i = 0; i < n; i++ ){
			float x = TDList.get(i)[0];
			float y = TDList.get(i)[1];
			Sx += x;
			Sy += y;
			Sxx += x*x;
			Sxy += x*y;
		}
		//ax+b = y
		float a = (n*Sxy-Sx*Sy)/(n*Sxx-Sx*Sx);
		float b = (Sy - a*Sx)/n;
		float[] result = {a,b};
		return result;
	}
	
	public float calculateAcceleration(float[] cog){
		float thrust = this.getThrust(cog);
		float weight = this.getDrone().getWeight();
		float drag = this.getDrone().getDrag();
		float gravity = this.getDrone().getGravity();
		float cosPitch = (float) Math.cos(Math.toRadians(this.getDrone().getPitch()));
		float speed = this.getSpeed();
		
		float thrustToTarget=(float) Math.sqrt(Math.abs(Math.pow(thrust, 2)+2*cosPitch*thrust*weight*gravity + Math.pow((weight*gravity),2)));
		float force = thrustToTarget - drag*speed;
		float acceleration = force/weight;
//		System.out.println(acceleration);
		this.setAcceleration(acceleration);
		return this.getAcceleration();
	}

	public float calculateDecelerationDistance(){
		float tegenpitch = 5f;
		float distance = 0;
		if (this.getSpeed() >= 0){
			float at = this.getAcceleration()*(this.getDrone().getPitch()/this.getDrone().getMaxPitchRate());
			float deceleration = (float) (this.getDrone().getDrag()*this.getSpeed() + Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight()*Math.tan(Math.toRadians(tegenpitch)))/this.getDrone().getWeight();
			float counterpitch = this.getDrone().getPitch()/this.getDrone().getMaxPitchRate()*(this.getSpeed()+at);
			float backpitch = tegenpitch/this.getDrone().getMaxPitchRate()*(this.getSpeed());
			distance = (float) Math.pow(this.getSpeed(),2) / (2*deceleration) + counterpitch+ backpitch;
//			System.out.println("speed " + this.getSpeed());
//			System.out.println("pitch " + this.getDrone().getPitch());
//			System.out.println("decel " + deceleration);
//			System.out.println("counterpitch " + counterpitch);
//			System.out.println("backpitch "+ backpitch);
		}
//		System.out.println("deceldistance "+ distance);	
		return distance;
	}
	
	public Drone getDrone(){
		return this.drone;
	}

	public static final float getVisibilityFactor(){
		return PhysicsCalculations.visibilityFactor;
	}

	public static final float getDecelerationFactor(){
		return PhysicsCalculations.decelerationFactor;
	}

//	public static float getDecelerationDistance() {
//		return DecelerationDistance;
//	}
	
	/**
	 * @return the timeDistanceList
	 */
	public ArrayList<float[]> getTimeDistanceList() {
		return timeDistanceList;
	}

	private ArrayList<float[]> timeDistanceList;
	
	/**
	 * @return the previousTimeDistance
	 */
	public float[] getPreviousTimeDistance() {
		return previousTimeDistance;
	}

	/**
	 * @param previousTimeDistance the previousTimeDistance to set
	 */
	public void setPreviousTimeDistance(float[] previousTimeDistance) {
		this.previousTimeDistance = previousTimeDistance;
	}
	
	private float[] previousTimeDistance;
	
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	private float speed;
	
	/**
	 * @return the acceleration
	 */
	public float getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	private float acceleration;
	

	public float getDecelerationDistance() {
		return decelerationDistance;
	}


	public void setDecelerationDistance(float distance) {
		this.decelerationDistance = distance;
	}
	
	private float decelerationDistance;
	
	// hoe groter hoe nauwkeuriger maar te groot = te traag updaten van speed (nu schommelt speed = s+-0.5)
	// speed schommelt minder voor avgcounter = 9, maar daar zijn de waardes om een of andere reden te klein in vgl met de simulator speed... (ongeveer 3-4 keer kleiner)
	public final static int avgcounter = 7; 
	
	//als avg counter groter wordt, kan deze groter en dus nauwkeuriger worden
	public final static float deviationLinReg = 0.006f;	
	
	
	private static final float visibilityFactor = 0.8f;
	private static final float decelerationFactor = 0.4f;
	
	
//	private static final float DecelerationDistance = 25f;
	
}
