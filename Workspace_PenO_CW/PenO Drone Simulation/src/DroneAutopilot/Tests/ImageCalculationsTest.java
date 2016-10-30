package DroneAutopilot.Tests;

import java.util.ArrayList;
import java.util.Arrays;
import DroneAutopilot.ImageCalculations;
import exceptions.EmptyPositionListException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ImageCalculationsTest{

	@Before
	public void setUp() {
		calc = new ImageCalculations();
		
		emptyList = new ArrayList<int[]>(); 
		pixelsPos1 = new ArrayList<int[]>(Arrays.asList(new int[][] {{0,0},{0,1},{0,2},{1,0},{1,1},{1,2},{2,0},{2,1},{2,2}}));
	    pixelsPos2 = new ArrayList<int[]>(Arrays.asList(new int[][] {{73,75},{74,74},{74,75},{74,76},{75,75}}));
	    
	    image10x10Empty = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    							 ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image10x10Square4InCenter = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    									   ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image10x10Square3InTopLeftCorner = new int[] {ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
	    											  ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};
	    image9x9Square4InBottomRightCorner = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,
					 									ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red};
		image9x9Square3InTopMiddle = new int[] {ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.red,ImageCalculations.red,ImageCalculations.red,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,
												ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white,ImageCalculations.white};

	    
	}
		
	@Test
    public void cOGTest() {
    	try {
			assertArrayEquals(calc.getCOG(pixelsPos1),new int[] {1,1});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}    
    	try {
			assertArrayEquals(calc.getCOG(pixelsPos2),new int[] {74,75});
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
    }
	
	@Test(expected = EmptyPositionListException.class)
	public void cOGOfEmptyList() throws EmptyPositionListException {
		calc.getCOG(emptyList);
	}
    
	//@Test
    public void GetRedPixelsInImageTest(){
    	//TODO manier om afbeelding naar keuze te gebruiken ipv camera/camera afbeeldingen naar keuze te laten geven
    }

    
    private ImageCalculations calc;

    private ArrayList<int[]> emptyList;
    private ArrayList<int[]> pixelsPos1;
    private ArrayList<int[]> pixelsPos2;
    
    private int[] image10x10Empty;
    private int[] image10x10Square4InCenter;   									 
    private int[] image10x10Square3InTopLeftCorner;
    private int[] image9x9Square4InBottomRightCorner;
    private int[] image9x9Square3InTopMiddle;
}
