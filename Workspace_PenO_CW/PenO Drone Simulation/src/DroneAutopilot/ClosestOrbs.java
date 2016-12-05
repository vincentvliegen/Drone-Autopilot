package DroneAutopilot;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.FactoryConfigurationError;

import exceptions.FirstOrbNotVisibleException;
import p_en_o_cw_2016.Camera;
import p_en_o_cw_2016.Drone;

public class ClosestOrbs {

	private final Drone drone;
	private final ImageCalculations imageCalculations;
	private final PhysicsCalculations physicsCalculations;
	private final static int numberOfOrbsConsidered = 5;
	private int colorFirstOrb;
	private int colorSecondOrb;
	private HashMap<Integer, ArrayList<int[]>> allPixelsImage;
	private HashMap<Integer, float[][]> closestOrbs;// <color,[[cogL],[cogr]]>
	
	
	public ClosestOrbs(Drone drone) {
		this.drone = drone;
		this.imageCalculations = new ImageCalculations();
		this.physicsCalculations = new PhysicsCalculations(drone);

	}

	public void determineClosestOrbs() {
		HashMap<Integer, float[][]> closestOrbsList = new HashMap<Integer, float[][]>();
		HashMap<Integer,float[]>closestOrbsListLeft = this.getBiggestOrbs(this.getDrone().getLeftCamera());
		HashMap<Integer,float[]>closestOrbsListRight = this.getBiggestOrbs(this.getDrone().getRightCamera());
		for (int colorLeft : closestOrbsListLeft.keySet()){
			for(int colorRight : closestOrbsListRight.keySet()){
				if(colorLeft == colorRight){
					float[][] cogs = {closestOrbsListLeft.get(colorLeft),closestOrbsListRight.get(colorLeft)};
					closestOrbsList.put(colorLeft,cogs);
					break;					
				}
			}
		}
		this.setClosestOrbs(closestOrbsList);
	}
	
	//Geeft n grootste bollen op beeld van opgegeven camera
	public HashMap<Integer,float[]> getBiggestOrbs(Camera camera) {
		ArrayList<int[]> colorAndSizeList = new ArrayList<int[]>();
		this.getImageCalculations().calculatePixelsOfEachColor(camera);
		this.setAllPixelsImage(this.getImageCalculations().getPixelsOfEachColor());
		int minPixels = Integer.MAX_VALUE;
		for (int color : this.getAllPixelsImage().keySet()) {
			int size = this.getAllPixelsImage().get(color).size();
			int[] colorAndSize = { color, size };
			if (colorAndSizeList.size() < getNumberoforbsconsidered()) {

				colorAndSizeList.add(colorAndSize);
				minPixels = Math.min(minPixels, size);

			} else if (size > minPixels) {

				colorAndSizeList.add(colorAndSize);

				int i = 0;
				while (colorAndSizeList.size() > getNumberoforbsconsidered()) {
					if (colorAndSizeList.get(i)[1] <= minPixels) {
						colorAndSizeList.remove(i);
						int newSmallest = Integer.MAX_VALUE;
						for (int j = 0; j < colorAndSizeList.size(); j++) {
							newSmallest = Math.min(newSmallest, colorAndSizeList.get(i)[1]);
						}
						minPixels = newSmallest;
					}
					i++;
				}
			}
		}
		//zwaartepunt berekenen
		HashMap<Integer,float[]> colorList = new HashMap<Integer,float[]>();
		for (int i = 0; i < colorAndSizeList.size(); i++){
			float[] cog = this.getImageCalculations().findBestCenterOfGravity(getAllPixelsImage().get(colorAndSizeList.get(i)[0]), camera);
			colorList.put(colorAndSizeList.get(i)[0],cog);
		}
		return colorList;
	}

	// Geeft n grootste kleuren van bollen (of minder als er geen n bollen zijn)
	// checken voor twee camera's -> niet in beide beelden -> laten vallen
	// Left en Right hashmap parameters maken
//	public List<Integer> getBiggestOrbs() {
//		// left
//		List<int[]> colorAndSizeListLeft = new ArrayList<int[]>();
//		this.getImageCalculations()
//				.calculatePixelsOfEachColor(this.getDrone().getLeftCamera());
//		this.setAllPixelsLeftImage(this.getImageCalculations().getPixelsOfEachColor());
//
//		int minLeftPixels = Integer.MAX_VALUE;
//		for (int color : this.getAllPixelsLeftImage().keySet()) {
//			int size = this.getAllPixelsLeftImage().get(color).size();
//			int[] colorAndSize = { color, size };
//			if (colorAndSizeListLeft.size() < this.getNumberoforbsconsidered()) {
//
//				colorAndSizeListLeft.add(colorAndSize);
//				minLeftPixels = Math.min(minLeftPixels, size);
//
//			} else if (size > minLeftPixels) {
//
//				colorAndSizeListLeft.add(colorAndSize);
//
//				int i = 0;
//				while (colorAndSizeListLeft.size() > this.getNumberoforbsconsidered()) {
//					if (colorAndSizeListLeft.get(i)[1] <= minLeftPixels) {
//						colorAndSizeListLeft.remove(i);
//						int newSmallest = Integer.MAX_VALUE;
//						for (int j = 0; j < colorAndSizeListLeft.size(); j++) {
//							newSmallest = Math.min(newSmallest, colorAndSizeListLeft.get(i)[1]);
//						}
//						minLeftPixels = newSmallest;
//					}
//					i++;
//				}
//			}
//		}
//		// right
//		List<int[]> colorAndSizeListRight = new ArrayList<int[]>();
//		this.getImageCalculations()
//				.calculatePixelsOfEachColor(this.getDrone().getRightCamera());
//		this.setAllPixelsRightImage(this.getImageCalculations().getPixelsOfEachColor());
//
//		int minRightPixels = Integer.MAX_VALUE;
//		for (int color : this.getAllPixelsRightImage().keySet()) {
//			int size = this.getAllPixelsRightImage().get(color).size();
//			int[] colorAndSize = { color, size };
//			if (colorAndSizeListRight.size() < this.getNumberoforbsconsidered()) {
//
//				colorAndSizeListRight.add(colorAndSize);
//				minRightPixels = Math.min(minRightPixels, size);
//
//			} else if (size > minRightPixels) {
//
//				colorAndSizeListRight.add(colorAndSize);
//
//				int i = 0;
//				while (colorAndSizeListRight.size() > this.getNumberoforbsconsidered()) {
//					if (colorAndSizeListRight.get(i)[1] <= minRightPixels) {
//						colorAndSizeListRight.remove(i);
//						int newSmallest = Integer.MAX_VALUE;
//						for (int j = 0; j < colorAndSizeListRight.size(); j++) {
//							newSmallest = Math.min(newSmallest, colorAndSizeListRight.get(i)[1]);
//						}
//						minRightPixels = newSmallest;
//					}
//					i++;
//				}
//			}
//		}
//
//		// compare left and right list
//		
//		return colorListLeft;
//	}

	// geeft kleur terug van dichtstbijzijnde
	public void calculateFirstOrb() throws NullPointerException {
		int size = getClosestOrbs().size();
		if(size != 0){
			float[] distances = new float[size];
			int[] keys = new int[size];
			int i = 0;
			for (int key : getClosestOrbs().keySet()) {
				distances[i] = this.getPhysicsCalculations().getDistance(getClosestOrbs().get(key)[0],
						getClosestOrbs().get(key)[1]);
				keys[i] = key;
				i++;
			}
			int index = this.indexMinValueArray(distances);
			this.setColorFirstOrb(keys[index]);
		} else{
			throw new NullPointerException();
		}
	}

	public int indexMinValueArray(float[] array) {
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] < array[index]) {
				index = i;
			}
		}
		return index;
	}

	// kleur bol dichtst bij bol 1
	public void calculateSecondOrb() throws NullPointerException,FirstOrbNotVisibleException {
		if(this.getClosestOrbs().containsKey(this.getColorFirstOrb())){
			int size = getClosestOrbs().size() - 1;
			if(size != 0){
				float[] distances = new float[size];
				int[] keys = new int[size];
				int i = 0;
				for (int key : getClosestOrbs().keySet()) {
					if (key != this.getColorFirstOrb()) {
						distances[i] = getDistanceBetweenOrbs(getClosestOrbs().get(key)[0], getClosestOrbs().get(key)[1],
								getClosestOrbs().get(getColorFirstOrb())[0], getClosestOrbs().get(getColorFirstOrb())[1]);
						keys[i] = key;
						i++;
					}
				}
				int index = this.indexMinValueArray(distances);
				this.setColorSecondOrb(keys[index]);
			} else{
				throw new NullPointerException();
			}
		} else {
			throw new FirstOrbNotVisibleException(getColorFirstOrb());
		}
	}

	public float getDistanceBetweenOrbs(float[] cogL1, float[] cogR1, float[] cogL2, float[] cogR2) {
		float depth1 = this.getPhysicsCalculations().getDepth(cogL1, cogR1);
		float depth2 = this.getPhysicsCalculations().getDepth(cogL2, cogR2);
		float alpha1 = this.getPhysicsCalculations().horizontalAngleDeviation(cogL1, cogR1);
		float alpha2 = this.getPhysicsCalculations().horizontalAngleDeviation(cogL2, cogR2);
		float beta1 = this.getPhysicsCalculations().verticalAngleDeviation(cogL1);
		float beta2 = this.getPhysicsCalculations().verticalAngleDeviation(cogL2);
		// distance = sqrt( (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2 )
		float x1 = (float) (depth1 * Math.tan(alpha1));
		float x2 = (float) (depth1 * Math.tan(alpha2));
		float y1 = (float) (depth1 * Math.tan(beta1));
		float y2 = (float) (depth1 * Math.tan(beta2));
		float z1 = depth1;
		float z2 = depth2;
		float distance = (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
		return distance;
	}

	
	
	
	
	public ImageCalculations getImageCalculations() {
		return imageCalculations;
	}

	public HashMap<Integer, ArrayList<int[]>> getAllPixelsImage() {
		return allPixelsImage;
	}

	public void setAllPixelsImage(HashMap<Integer, ArrayList<int[]>> allPixelsImage) {
		this.allPixelsImage = allPixelsImage;
	}

	public int getColorFirstOrb() {
		return colorFirstOrb;
	}

	public void setColorFirstOrb(int colorFirstOrb) {
		this.colorFirstOrb = colorFirstOrb;
	}

	public int getColorSecondOrb() {
		return colorSecondOrb;
	}

	public void setColorSecondOrb(int colorSecondOrb) {
		this.colorSecondOrb = colorSecondOrb;
	}

	public static int getNumberoforbsconsidered() {
		return numberOfOrbsConsidered;
	}

	public HashMap<Integer, float[][]> getClosestOrbs() {
		return closestOrbs;
	}

	public void setClosestOrbs(HashMap<Integer, float[][]> closestOrbs) {
		this.closestOrbs = closestOrbs;
	}

	/**
	 * @return the physicsCalculations
	 */
	public PhysicsCalculations getPhysicsCalculations() {
		return physicsCalculations;
	}

	public Drone getDrone() {
		return drone;
	}

}
