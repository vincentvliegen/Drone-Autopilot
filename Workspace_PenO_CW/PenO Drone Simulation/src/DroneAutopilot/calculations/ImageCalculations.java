package DroneAutopilot.calculations;

import java.util.*;

import exceptions.EmptyPositionListException;
import exceptions.SmallCircleException;
import p_en_o_cw_2016.Camera;

public class ImageCalculations {

	// kleur in int
	public final static int red = 16711680;
	public final static int green = 65280;
	public final static int blue = 255;
	public final static int white = 16777215;
	public final static int black = 0;

	// Aantal punten om cirkel te berekenen.
	public final static int minimalSizeCircumferenceCircle = 20;
	public float radius;

	private HashMap<Integer, ArrayList<int[]>> pixelsOfEachColor;
	private HashMap<Integer, ArrayList<int[]>> greyPixels;

	// [i] -> (x,y)
	public int[] indexToCoordinates(int index, Camera camera) {
		int width = camera.getWidth();
		int x = (int) (index % width);
		int y = (int) (index / width);
		int[] coord = { x, y };
		return coord;
	}

	public ArrayList<int[]> getPixelsOfColor(Camera camera, int givenColor) {
		int[] image = camera.takeImage();
		this.setImage(image);
		ArrayList<int[]> coloredPositions = new ArrayList<int[]>();
		for (int i = 0; image.length > i; i++) {
			if (image[i] == givenColor) {
				coloredPositions.add(indexToCoordinates(i, camera));
			}
		}
		return coloredPositions;
	}

	public int[] getImage() {
		return image;
	}

	public void setImage(int[] image) {
		this.image = image;
	}

	private int[] image;

	private final static float percentageFilled = 0.999f;

	public static float getPercentagefilled() {
		return percentageFilled;
	}

	public boolean checkImageFilled(Camera camera, int color) {
		return this.getPixelsOfColor(camera, color).size() >= this.getImage().length * getPercentagefilled();
	}

	// Geeft en hashmap weer met als key: color (int) en arraylist van
	// coordinaten.
	public void calculatePixelsOfEachColor(Camera camera) {
		int[] image = camera.takeImage();
		HashMap<Integer, ArrayList<int[]>> hashMapDifferentColors = new HashMap<Integer, ArrayList<int[]>>();
		HashMap<Integer, ArrayList<int[]>> hashMapGreyColors = new HashMap<Integer, ArrayList<int[]>>();
		for (int i = 0; i < image.length; i++) {
			if (image[i] != white) {
				int[] RGB = this.colorIntToRGB(image[i]);
				// if grijstint
				if (RGB[0] == RGB[1] && RGB[1] == RGB[2]) {
					if (hashMapGreyColors.containsKey(image[i])) {
						ArrayList<int[]> hashMapTemp = hashMapGreyColors.get(image[i]);
						hashMapGreyColors.remove(image[i], hashMapTemp);
						hashMapTemp.add(indexToCoordinates(i, camera));
						hashMapDifferentColors.put(image[i], hashMapTemp);
					} else {
						ArrayList<int[]> coloredPositions = new ArrayList<int[]>();
						coloredPositions.add(indexToCoordinates(i, camera));
						hashMapDifferentColors.put(image[i], coloredPositions);
					}
					// geen grijstint
				} else {
					if (hashMapDifferentColors.containsKey(image[i])) {
						ArrayList<int[]> hashMapTemp = hashMapDifferentColors.get(image[i]);
						hashMapDifferentColors.remove(image[i], hashMapTemp);
						hashMapTemp.add(indexToCoordinates(i, camera));
						hashMapDifferentColors.put(image[i], hashMapTemp);
					} else {
						ArrayList<int[]> coloredPositions = new ArrayList<int[]>();
						coloredPositions.add(indexToCoordinates(i, camera));
						hashMapDifferentColors.put(image[i], coloredPositions);
					}
				}
			}
		}
		this.setPixelsOfEachColor(hashMapDifferentColors);
		this.setGreyPixels(hashMapGreyColors);
	}

	// conversie int color naar leesbaar (R,G,B) formaat
	public int[] colorIntToRGB(int color) {
		int[] RGB = { 0, 0, 0 };
		/* B */RGB[2] = color % 256;
		/* G */RGB[1] = (color / 256) % 256;
		/* R */RGB[0] = color / (256 * 256);
		return RGB;
	}

	// circlefit,zolang genoeg randpunten te zien zijn, anders zwaartepunt
	public float[] findBestCenterOfGravity(ArrayList<int[]> pixelsFound, Camera camera) {
		float[] cog = { 0, 0 };
		try {
			cog = this.centerOfCircle(pixelsFound, camera);
		} catch (SmallCircleException e) {
			try {
				cog = this.getCOG(pixelsFound);
			} catch (EmptyPositionListException e1) {
				e1.printStackTrace();
			}
		} catch (EmptyPositionListException e) {
			e.printStackTrace();
		}
		return cog;
	}

	// Zwaartepunt van gekleurde pixels bepalen.
	// Als er geen rode pixels zijn, exception
	// COG = center of gravity
	public float[] getCOG(ArrayList<int[]> listOfPixelCoordinates) throws EmptyPositionListException {
		float sumX = 0;
		float sumY = 0;
		float[] cOG = { 0, 0 };
		if (listOfPixelCoordinates.size() == 0) {
			throw new EmptyPositionListException(listOfPixelCoordinates);
		}
		for (int i = 0; i < listOfPixelCoordinates.size(); i++) {
			sumX += listOfPixelCoordinates.get(i)[0];
			sumY += listOfPixelCoordinates.get(i)[1];
		}
		cOG[0] = sumX / listOfPixelCoordinates.size();
		cOG[1] = sumY / listOfPixelCoordinates.size();		
		return cOG;
	}

	public float[] centerOfCircle(ArrayList<int[]> listOfPixelCoordinates, Camera camera)
			throws SmallCircleException, EmptyPositionListException {
		ArrayList<int[]> AllPoints = new ArrayList<int[]>();
		try {
			AllPoints.addAll(pointsOnCircumference(listOfPixelCoordinates, camera));
		} catch (SmallCircleException e) {
			throw new SmallCircleException(e.getSizeListCircPos());
		} catch (EmptyPositionListException e) {
			throw new EmptyPositionListException(e.getList());
		}
		int N = AllPoints.size();
		float xavg = 0;
		float yavg = 0;
		for (int i = 0; i < AllPoints.size(); i++) {
            xavg += AllPoints.get(i)[0];
            yavg += AllPoints.get(i)[1];
		}
		xavg = xavg / N;
		yavg = yavg / N;
		float Suu = 0;
		float Suv = 0;
		float Svv = 0;
		float Suuu = 0;
		float Suuv = 0;
		float Suvv = 0;
		float Svvv = 0;
		for (int i = 0; i < N; i++) {
			float u = AllPoints.get(i)[0] - xavg;
			float v = AllPoints.get(i)[1] - yavg;
			Suu += u * u;
			Suv += u * v;
			Svv += v * v;
			Suuu += u * u * u;
			Suuv += u * u * v;
			Suvv += u * v * v;
			Svvv += v * v * v;
		}
		float uc;
		float vc;
		float frac = 1 / (2 * (Suu * Svv - Suv * Suv));
		uc = frac * (Svv * (Suuu + Suvv) - Suv * (Svvv + Suuv));
		vc = frac * (-Suv * (Suuu + Suvv) + Suu * (Svvv + Suuv));
		float X = uc + xavg;
		float Y = vc + yavg;
		float radius = (float) Math.sqrt(uc*uc+vc*vc+(Suu+Svv)/N);
		setRadius(radius);		
		return new float[] { X, Y };
	}

	// calculate 3 most representative points on circumference (3 furthest
	// neighbours)
	// ONLY if at least 3 points on circumference
	public ArrayList<int[]> pointsOnCircumference(ArrayList<int[]> listOfPixelCoordinates, Camera camera)
			throws SmallCircleException, EmptyPositionListException {
		ArrayList<int[]> AllEdges = new ArrayList<int[]>();
		ArrayList<int[]> AllCPoints = new ArrayList<int[]>();
		if (listOfPixelCoordinates.size() == 0) {
			throw new EmptyPositionListException(listOfPixelCoordinates);
		}
		int y1 = listOfPixelCoordinates.get(0)[1];
		int y2 = listOfPixelCoordinates.get(listOfPixelCoordinates.size() - 1)[1];
		int[] currentPos = new int[] { 0, y1 };
		int[] previousPos = new int[] { 0, y1 };
		boolean addedPrevPos = false;
		int cameraWidth = camera.getWidth();
		int cameraHeight = (int) (camera.getWidth() * (Math.tan(Math.toRadians(camera.getVerticalAngleOfView() / 2))
				/ (Math.tan(Math.toRadians(camera.getHorizontalAngleOfView() / 2)))));
		for (int i = 0; i < listOfPixelCoordinates.size(); i++) {// bepaal alle
																	// punten op
																	// de rand
																	// van de
																	// groep
			currentPos = listOfPixelCoordinates.get(i);
			if (currentPos[1] == previousPos[1] + 1) {// de eerste en laatste
														// waarde per rij worden
														// toegevoegd
				if (!addedPrevPos) {
					AllEdges.add(previousPos);// vermijdt dubbele toevoeging
												// laatste element eerste rij
				}
				AllEdges.add(currentPos);
				addedPrevPos = true;
			} else if (currentPos[1] == y1 || currentPos[1] == y2) {// bovenste
																	// en
																	// onderste
																	// rij
																	// worden
																	// toegevoegd
				AllEdges.add(currentPos);
				addedPrevPos = true;
			} else {
				addedPrevPos = false;
			}
			previousPos = currentPos;
		}
		for (int j = 0; j < AllEdges.size(); j++) {// bepaal alle punten op de
													// cirkel uit de randgroep
			if (AllEdges.get(j)[1] < cameraHeight - 1 && AllEdges.get(j)[1] > 0 && AllEdges.get(j)[0] < cameraWidth - 1
					&& AllEdges.get(j)[0] > 0) {// niet tegen de randen
				AllCPoints.add(AllEdges.get(j));
				// System.out.println(AllEdges.get(j)[0]+" ,
				// "+AllEdges.get(j)[1]);
			}
		}
		if (AllCPoints.size() < minimalSizeCircumferenceCircle) {
			throw new SmallCircleException(AllCPoints.size());
		}
		// result.add(AllCPoints.get(0));
		// result.add(AllCPoints.get(AllCPoints.size()/2));//middelste punt ligt
		// verst van eerste en laatste punt
		// result.add(AllCPoints.get(AllCPoints.size()-1));
		// return result;
		return AllCPoints;
	}

	public HashMap<Integer, ArrayList<int[]>> getPixelsOfEachColor() {
		return pixelsOfEachColor;
	}

	public void setPixelsOfEachColor(HashMap<Integer, ArrayList<int[]>> pixelsOfEachColor) {
		this.pixelsOfEachColor = pixelsOfEachColor;
	}

	public HashMap<Integer, ArrayList<int[]>> getGreyPixels() {
		return greyPixels;
	}

	public void setGreyPixels(HashMap<Integer, ArrayList<int[]>> greyPixels) {
		this.greyPixels = greyPixels;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	
	// conversie int color naar leesbaar (R,G,B) formaat
	// public int[] colorIntToRGB(int color){
	// int [] RGB = {0,0,0};
	// /*R*/RGB[2] = color % 256;
	// /*G*/RGB[1] = (color / 256) % 256;
	// /*B*/RGB[0] = color / (256*256);
	// return RGB;
	// }

}
