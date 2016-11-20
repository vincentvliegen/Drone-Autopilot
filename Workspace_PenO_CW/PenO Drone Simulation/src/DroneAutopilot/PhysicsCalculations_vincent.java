package DroneAutopilot;


import p_en_o_cw_2016.Drone;

public class PhysicsCalculations_vincent {

	private Drone drone;
	private float speed;
	private float acceleration;
	private float[] previousTimeAndDistance;
	private float decelerationDistance;
	private float distance;
	private float depth;
	private float cameraHeight;
	private float Y;
	private float X1;
	private float X2;
	private float thrust;
	private float verticalAngleDeviation;
	private float horizontalAngleDeviation;
	private float focalDistance;
	private boolean firstDistanceNeeded;
	private float firstDistance;

	
	public PhysicsCalculations_vincent(Drone drone){
		this.setDrone(drone);
		
		//Zolang er geen bol in zicht is, zullen acceleration, speed en decelDist 0 geven
		setFirstDistanceNeeded(true);
		setSpeed(0);
		setAcceleration(0);
		setDecelerationDistance(0);
	}

	
	// berekent alles in PhysicsCalculations
	// TODO deze functie moet in het begin opgeroepen worden (in moveToTarget) voordat er een waarde wordt gebruikt
	public void updateAll(float[] centerOfGravityL, float[]centerOfGravityR){
	
	// altijd berekenbaar, onafhankelijk van cog's
		this.calculateFocalDistance();
		this.calculateCameraHeight();

	// afhankelijk van cog's
		this.calculateX1(centerOfGravityL);
		this.calculateX2(centerOfGravityR);
		this.calculateY(centerOfGravityL);
		
		this.calculateDepth();//heeft x1, x2 en focalDistance nodig
		this.calculateDistance();
		
		this.calculateHorizontalAngleDeviation();//heeft depth nodig
		this.calculateVerticalAngleDeviation();
		
		this.calculateThrust();//heeft verticalAngleDeviation nodig
		
	// vanaf dat de afstand bepaald is, kunnen volgende waardes berekend worden
		if(isFirstDistanceNeeded()){
			setFirstDistanceNeeded(false);;
			firstDistance = getDistance();
		}else{
			this.calculateAcceleration();//heeft distance nodig
			this.calculateSpeed();//heeft acceleration nodig
			this.calculateDecelerationDistance();//heeft speed nodig (nog niet uitgewerkt)
		}
		
	//nadat alle bovenstaande waardes zijn berekend, kunnen de vorige tijd en afstand opnieuw ingesteld worden voor volgende cyclus.
		this.setPreviousTimeAndDistance(new float[] {this.getDrone().getCurrentTime(),this.getDistance()});
	}
	
	
	//CALCULATORS
	
	public void calculateFocalDistance(){
		float focal =(float) ((this.getDrone().getLeftCamera().getWidth()/2) / Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getHorizontalAngleOfView()/2)));
		this.setFocalDistance(focal);
	}
	
	public void calculateCameraHeight(){
		int height =  (int) Math.round(Math.tan(Math.toRadians(this.getDrone().getLeftCamera().getVerticalAngleOfView()/2))*this.getFocalDistance()*2);
		this.setCameraHeight(height);
	}

	public void calculateX1(float[] centerofGravityL){
		float x1 = centerofGravityL[0] - this.getDrone().getLeftCamera().getWidth()/2;
		this.setX1(x1);
	}
	
	public void calculateX2(float[] centerofGravityR){
		float x2 = centerofGravityR[0] - this.getDrone().getRightCamera().getWidth()/2;
		this.setX2(x2);
	}
	
	public void calculateY(float[] centerofGravity){
		float y = ((float) ((this.getCameraHeight()/2) - centerofGravity[1])) ;
		this.setY(y);
	}
	
	public void calculateDepth(){
		float depth=0;
		try{
			depth = (this.getDrone().getCameraSeparation() * this.getFocalDistance())/(this.getX1() - this.getX2());
			depth = Math.abs(depth);
		} catch(IllegalArgumentException e){}
		this.setDepth(depth);
	}
	
	public void calculateDistance(){
		float depth = this.getDepth();
		float distance =(float) (depth/(Math.cos(Math.toRadians(this.getHorizontalAngleDeviation()))*Math.cos(Math.toRadians(this.getVerticalAngleDeviation()))));
		this.setDistance(distance);
	}

	public void calculateHorizontalAngleDeviation(){
		float x = (this.getDepth() * this.getX1()) / this.getFocalDistance();
		float tanAlfa = (x - this.getDrone().getCameraSeparation()/2) / this.getDepth();
		this.setHorizontalAngleDeviation((float) Math.toDegrees(Math.atan(tanAlfa)));
	}

	public void calculateVerticalAngleDeviation(){
		this.setVerticalAngleDeviation((float) Math.toDegrees(Math.atan(this.getY() / this.getFocalDistance())));
	}
	
	public void calculateThrust() {
		float thrust;
		float beta = this.getVerticalAngleDeviation();
		thrust = (float) (Math.abs(this.getDrone().getGravity())*this.getDrone().getWeight() * 
				Math.cos(Math.toRadians(beta - this.getDrone().getPitch())) / 
				Math.cos(Math.toRadians(beta)));
		//		System.out.println("thrust "+thrust);
		//		System.out.println("pitch "+this.getDrone().getPitch());
		this.setThrust(thrust);
	}
	
	public void calculateSpeed(){
		float currentTime = this.getDrone().getCurrentTime();//t1
		float previousTime = this.getPreviousTimeAndDistance()[0];//t0
		float acceleration = this.getAcceleration();//a
		float previousSpeed = this.getSpeed();//v0
		//v2 = a*(t1-t0) + v0
		float speed =  acceleration * (currentTime-previousTime) + previousSpeed;
		this.setSpeed(speed);
	}
	
	public void calculateAcceleration(){
		float currentTime = this.getDrone().getCurrentTime();//t1
		float currentDistance = this.getDistance();//x1
		float previousTime = this.getPreviousTimeAndDistance()[0]; //t0
		float previousDistance = this.getPreviousTimeAndDistance()[1]; //x0
		float previousSpeed = this.getSpeed(); //v0
		float previousAcceleration = this.getAcceleration(); //a0
		float acceleration = 0; //a
		if(currentTime!=previousTime){
			//a = 2*((x0-x1)-(v0*(t1-t0)))/((t1-t0)^2)
			acceleration=(float) (2*((previousDistance-currentDistance)-(previousSpeed*(currentTime-previousTime)))/Math.pow((currentTime-previousTime), 2));
		}else{
			//a=a0
			acceleration = previousAcceleration;
		}
		this.setAcceleration(acceleration);
	}

	public void calculateDecelerationDistance(){
		//TODO afstand moet afhankelijk zijn van speed, acceleration, eventueel een extra vertraging omwille van pitch 
		float decelerationDistance = firstDistance/2;		
//				(float) (Math.pow(this.getSpeed(), 2)/
//				(2*this.getAcceleration()) // hier moet er nog een bepaalde distance bij zodanig dat de tijd vh pitchen gecompenseerd wordt.
//				+3);
		this.setDecelerationDistance(decelerationDistance);
	}

	
	//GETTERS AND SETTERS
	
	public Drone getDrone(){
		return this.drone;
	}
	
	private void setDrone(Drone drone){
		this.drone = drone;
	}

	public float getSpeed() {
		return speed;
	}
	
	private void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAcceleration() {
		return acceleration;
	}
	
	private void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	private float[] getPreviousTimeAndDistance() {
		return previousTimeAndDistance;
	}

	private void setPreviousTimeAndDistance(float[] previousTimeDistance) {
		this.previousTimeAndDistance = previousTimeDistance;
	}
	
	public float getDecelerationDistance(){
		return this.decelerationDistance;
	}
	
	private void setDecelerationDistance(float distance){
		this.decelerationDistance = distance;
	}

	/**
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	private void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * @return the depth
	 */
	public float getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	private void setDepth(float depth) {
		this.depth = depth;
	}

	/**
	 * @return the cameraHeight
	 */
	public float getCameraHeight() {
		return cameraHeight;
	}

	/**
	 * @param cameraHeight the cameraHeight to set
	 */
	private void setCameraHeight(float cameraHeight) {
		this.cameraHeight = cameraHeight;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return Y;
	}

	/**
	 * @param y the y to set
	 */
	private void setY(float y) {
		Y = y;
	}

	/**
	 * @return the x1
	 */
	public float getX1() {
		return X1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	private void setX1(float x1) {
		X1 = x1;
	}

	/**
	 * @return the x2
	 */
	public float getX2() {
		return X2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	private void setX2(float x2) {
		X2 = x2;
	}

	/**
	 * @return the thrust
	 */
	public float getThrust() {
		return thrust;
	}

	/**
	 * @param thrust the thrust to set
	 */
	private void setThrust(float thrust) {
		this.thrust = thrust;
	}

	/**
	 * @return the verticalAngleDeviation
	 */
	public float getVerticalAngleDeviation() {
		return verticalAngleDeviation;
	}
	
	/**
	 * @param verticalAngleDeviation the verticalAngleDeviation to set
	 */
	private void setVerticalAngleDeviation(float verticalAngleDeviation) {
		this.verticalAngleDeviation = verticalAngleDeviation;
	}

	/**
	 * @return the horizontalAngleDeviation
	 */
	public float getHorizontalAngleDeviation() {
		return horizontalAngleDeviation;
	}
	
	/**
	 * @param horizontalAngleDeviation the horizontalAngleDeviation to set
	 */
	private void setHorizontalAngleDeviation(float horizontalAngleDeviation) {
		this.horizontalAngleDeviation = horizontalAngleDeviation;
	}

	/**
	 * @return the focalDistance
	 */
	public float getFocalDistance() {
		return focalDistance;
	}

	/**
	 * @param focalDistance the focalDistance to set
	 */
	private void setFocalDistance(float focalDistance) {
		this.focalDistance = focalDistance;
	}




	
	/**
	 * @return the firstDistanceNeeded
	 */
	public boolean isFirstDistanceNeeded() {
		return firstDistanceNeeded;
	}
	




	/**
	 * @param firstDistanceNeeded the firstDistanceNeeded to set
	 */
	public void setFirstDistanceNeeded(boolean firstDistanceNeeded) {
		this.firstDistanceNeeded = firstDistanceNeeded;
	}
	
	public float getFirstDistance() {
		return firstDistance;
	}

	public void setFirstDistance(float firstDistance) {
		this.firstDistance = firstDistance;
	}

}
