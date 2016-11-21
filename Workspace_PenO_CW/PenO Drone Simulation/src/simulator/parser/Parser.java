package simulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;

/**
 * 
 * source: http://stackoverflow.com/questions/5819772/java-parsing-text-file
 * 
 */

public class Parser {

	
	//TODO verander naar float?
	double horizontalAngleOfView;
	double verticalAngleOfView;
	double imageWidth;
	double imageHeight;
	double cameraSeparation; 
	double weight ;
	double gravity; 
	double drag ;
	double maxThrust ;
	double maxPitchRate; 
	double maxRollRate; 
	double maxYawRate; 
	float[] arrayXValues;
	float[] arrayXTimes;
	float[] arrayYValues;
	float[] arrayYTimes;
	float[] arrayZValues;
	float[] arrayZTimes;
	
	float[] windRotationXValues;
	float[] windRotationXTimes;
	float[] windRotationYValues;
	float[] windRotationYTimes;
	float[] windRotationZValues;
	float[] windRotationZTimes;
	

	/*
	 * 1 	horizontalAngleOfView verticalAngleOfView imageWidth imageHeight cameraSeparation weight gravity drag maxThrust maxPitchRate maxRollRate maxYawRate 
	 * 2 	windSpeedXPoints 
	 * 3	windSpeedYPoints 
	 * 4	windSpeedZPoints 
	 * 5	windRotationRateAroundXPoints 
	  				the wind causes the drone to rotate around an axis through its center parallel 
	  				to the world X axis at this rate. Positive means clockwise when looking at 1 0 0 from 0 0 0. In degrees per second. 
	 * 6	windRotationRateAroundYPoints 
	 * 7	windRotationRateAroundZPoints 
	 * 8	"target_ball" x y z // one or more lines 
	 * 9	"obstacle_ball" x y z // zero or more lines; not applicable to Milestones 1.3 and 1.4
	 */

	private void parse() {

		
		//TODO kan fileLocation met relatief path?
		FileReader input = new FileReader("fileLocation");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		// line 1
		myLine = bufRead.readLine();
		String[] splitArray = myLine.split(" ");
		horizontalAngleOfView = Double.parseDouble(splitArray[0]);
		verticalAngleOfView = Double.parseDouble(splitArray[1]);
		imageWidth = Double.parseDouble(splitArray[2]);
		imageHeight = Double.parseDouble(splitArray[3]);
		cameraSeparation = Double.parseDouble(splitArray[4]);
		weight = Double.parseDouble(splitArray[5]);
		gravity = Double.parseDouble(splitArray[6]);
		drag = Double.parseDouble(splitArray[7]);
		maxThrust = Double.parseDouble(splitArray[8]);
		maxPitchRate = Double.parseDouble(splitArray[9]);
		maxRollRate = Double.parseDouble(splitArray[10]);
		maxYawRate = Double.parseDouble(splitArray[11]);

		// line 2
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		int splitArrayLength = splitArray.length;
		arrayXValues = new float[splitArrayLength];
		arrayXTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayXTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayXValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
		// line 3
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		arrayYValues = new float[splitArrayLength];
		arrayYTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayYTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayYValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
		// line 4
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		arrayZValues = new float[splitArrayLength];
		arrayZTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				arrayZTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				arrayZValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
		// line 5
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationXTimes= new float[splitArrayLength];
		windRotationXValues = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationXTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				windRotationXValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
		// line 6
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationYValues = new float[splitArrayLength];
		windRotationYTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationYTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				windRotationYValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
		// line 7
		myLine = bufRead.readLine();
		splitArray = myLine.split(" ");
		splitArrayLength = splitArray.length;
		windRotationZValues = new float[splitArrayLength];
		windRotationZTimes = new float[splitArrayLength];				
		for(int i = 0; i < splitArrayLength; i++) {
			if(i % 2 == 0){
				windRotationZTimes[i/2]=Float.parseFloat(splitArray[i]);
			}
			else{
				windRotationZValues[(i-1)/2]=Float.parseFloat(splitArray[i]);
			}
		}
		
	
		//target_balls
		while ( (myLine = bufRead.readLine()) != null)
		{    
		    String[] array1 = myLine.split(":");
		    // check to make sure you have valid data
		    String[] array2 = array1[1].split(" ");
		    for (int i = 0; i < array2.length; i++)
		        function(array1[0], array2[i]);
		}
		
	}

}
