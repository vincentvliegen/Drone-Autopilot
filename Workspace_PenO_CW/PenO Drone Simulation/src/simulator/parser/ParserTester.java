package simulator.parser;

import java.io.IOException;
import java.util.Arrays;

import simulator.world.World11;

public class ParserTester {
	
	public static void main(String[] args) throws IOException{
		World11 world = new World11();
		world.getParser().parse();
		
		
		System.out.println(world.getParser().getHorizontalAngleOfView());
		System.out.println(world.getParser().getVerticalAngleOfView());
		System.out.println(world.getParser().getImageWidth());
		System.out.println(world.getParser().getImageHeight());
		System.out.println(world.getParser().getCameraSeparation()); 
		System.out.println(world.getParser().getWeight()); ;
		System.out.println(world.getParser().getGravity()); 
		System.out.println(world.getParser().getDrag()) ;
		System.out.println(world.getParser().getMaxThrust()); ;
		System.out.println(world.getParser().getMaxPitchRate()); 
		System.out.println(world.getParser().getMaxRollRate()); 
		System.out.println(world.getParser().getMaxYawRate());
		
		
		System.out.println(Arrays.toString(world.getParser().getArrayXValues()));
		System.out.println(Arrays.toString(world.getParser().getArrayXTimes()));

		System.out.println(Arrays.toString(world.getParser().getArrayYValues()));
		System.out.println(Arrays.toString(world.getParser().getArrayYTimes()));
		               
		System.out.println(Arrays.toString(world.getParser().getArrayZValues()));
		System.out.println(Arrays.toString(world.getParser().getArrayZTimes()));
                    
		            
		System.out.println(Arrays.toString(world.getParser().getWindRotationXValues()));
		System.out.println(Arrays.toString(world.getParser().getWindRotationXTimes()));
		            
		System.out.println(Arrays.toString(world.getParser().getWindRotationYValues()));
		System.out.println(Arrays.toString(world.getParser().getWindRotationYTimes()));

		System.out.println(Arrays.toString(world.getParser().getWindRotationZValues()));
		System.out.println(Arrays.toString(world.getParser().getWindRotationZTimes()));
		              
//		System.out.println(Arrays.toString(world.getParser().getObstacleBalls()));
//		System.out.println(Arrays.toString(world.getParser().getTargetBalls()));
	}                                       

}
