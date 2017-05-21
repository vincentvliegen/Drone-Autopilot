package DroneAutopilot.calculations;

import java.util.ArrayList;
import java.util.Arrays;

import DroneAutopilot.DroneAutopilot;
import Jama.Matrix;

public class Pinhole {

	private DroneAutopilot ap;
	private Matrix leftR1;
	private Matrix leftR2;
	private Matrix leftR3;
	private Matrix rightR1;
	private Matrix rightR2;
	private Matrix rightR3;
	private double[][] globalpoint;

	public Pinhole(DroneAutopilot ap) {
		this.ap = ap;
		setup();
	}

	private void setup() {
		int cameraWidth = ap.getDrone().getLeftCamera().getWidth();
		int cameraHeight = (int) (ap.getDrone().getLeftCamera().takeImage().length / cameraWidth);
		double f = ap.getPhysicsCalculations().getFocalDistance();
		double camSep = ap.getDrone().getCameraSeparation();
		double tu = cameraWidth / 2;
		double tv = cameraHeight / 2;
		double TxLeft = camSep/2;
		double TxRight = -camSep/2;

		Matrix p1 = new Matrix(new double[] { 5.8, 2.5, -1.9, 1 }, 4);
		Matrix p2 = new Matrix(new double[] { -3.2, 5.9, 4.9, 1 }, 4);
		Matrix p3 = new Matrix(new double[] { -5.3, -3.4, -3.4, 1 }, 4);
		Matrix p4 = new Matrix(new double[] { 2.7, -4.3, 1.4, 1 }, 4);
		Matrix p5 = new Matrix(new double[] { 4.2, 1.6, 4.6, 1 }, 4);
		Matrix p6 = new Matrix(new double[] { 1.9, -1, 1.9, 1 }, 4);

//		Matrix KLeft = new Matrix(new double[][] { 	{ f, 0, tu, TzLeft * tu }, 
//													{ 0, -f, tv, TzLeft * tv }, 
//													{ 0, 0, 1, TzLeft } });
//		Matrix KRight = new Matrix(new double[][] { { f, 0, tu, TzRight*tu }, { 0, -f, tv, TzRight*tv }, { 0, 0, 1, TzRight } });

		Matrix KLeft = new Matrix(new double[][] { 	
			{ -f, 0, tu, TxLeft * -f }, 
			{ 0, f, tv, TxLeft * f }, 
			{ 0, 0, 1, 0 } });
		Matrix KRight = new Matrix(new double[][] { 
			{ f, 0, tu, TxRight*f }, 
			{ 0, -f, tv, TxRight*(-f) }, 
			{ 0, 0, 1, 0 } });

		ArrayList<Matrix> points = new ArrayList<>(6);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);

		double[][] coeffL = new double[12][12];
		double[][] coeffR = new double[12][12];

		for (int i = 0; i < points.size(); i++) {
			globalpoint = points.get(i).getArray();
			double x = points.get(i).getArray()[0][0];
			double y = points.get(i).getArray()[1][0];
			double z = points.get(i).getArray()[2][0];
			System.out.println("www");
			System.out.println(points.get(i).getArray()[3][0]);
			double w = points.get(i).getArray()[3][0]; // for clarity of formulas

			Matrix uvwL = KLeft.times(points.get(i));
			int u = (int) (uvwL.getArray()[0][0] / uvwL.getArray()[2][0]);
			int v = (int) (uvwL.getArray()[1][0] / uvwL.getArray()[2][0]);

			coeffL[2 * i] = new double[] { -x, 0, u * x, -y, 0, u * y, -z, 0, u * z, -w, 0, u * w };
			coeffL[2 * i + 1] = new double[] { 0, -x, v * x, 0, -y, v * y, 0, -z, v * z, 0, -w, v * w };
			
			
			uglobalL = u;
			vglobalL = v;
			// right

			// 
			
			Matrix uvwR = KRight.times(points.get(i));
			u = (int) (uvwR.getArray()[0][0] / uvwR.getArray()[2][0]);
			v = (int) (uvwR.getArray()[1][0] / uvwR.getArray()[2][0]);
			uglobalR = u;
			vglobalR = v;

			coeffR[2 * i] = new double[] { -x, 0, u * x, -y, 0, u * y, -z, 0, u * z, -w, 0, u * w };
			coeffR[2 * i + 1] = new double[] { 0, -x, v * x, 0, -y, v * y, 0, -z, v * z, 0, -w, v * w };
			
	
		}

		coeffL[0] = new double[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		coeffR[0] = new double[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		Matrix zeroes = new Matrix(new double[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 12);

		Matrix coeffLMatrix = new Matrix(coeffL);
		Matrix coeffRMatrix = new Matrix(coeffR);
//		coeffLMatrix.print(10, 10);
//		coeffRMatrix.print(10, 10);
//		zeroes.print(10, 10);

		Matrix CvectorLeft = coeffLMatrix.solve(zeroes);
		Matrix CvectorRight = coeffRMatrix.solve(zeroes);

		leftR1 = CvectorLeft.getMatrix(0, 3, 0, 0).transpose();
		leftR2 = CvectorLeft.getMatrix(4, 7, 0, 0).transpose();
		leftR3 = CvectorLeft.getMatrix(8, 11, 0, 0).transpose();

		rightR1 = CvectorRight.getMatrix(0, 3, 0, 0).transpose();
		rightR2 = CvectorRight.getMatrix(4, 7, 0, 0).transpose();
		rightR3 = CvectorRight.getMatrix(8, 11, 0, 0).transpose();

	}
	
	int uglobalL;
	int vglobalL;
	int uglobalR;
	int vglobalR;
	
	

	public double[] calculateXYZ(int xLeft, int yLeft, int xRight, int yRight) {
		int vLeft = yLeft;
		int vRight = yRight;
		int uLeft = xLeft;
		int uRight = xRight;
		double[] firstL = (leftR3.times(uLeft)).minus(leftR1).getArray()[0];
		double[] firstR = (rightR3.times(uRight)).minus(rightR1).getArray()[0];
		double[] secondL = (leftR3.times(vLeft)).minus(leftR2).getArray()[0];
		double[] secondR = (rightR3.times(vRight)).minus(rightR2).getArray()[0];

		Matrix C = new Matrix(new double[][] { secondR, firstR, secondL, {0,0,0,1} });
		Matrix b = new Matrix(new double[] { 0, 0, 0, 1}, 4);
		Matrix XYZ = C.solve(b);
		return new double[] { 
				XYZ.getArray()[0][0] / XYZ.getArray()[3][0],
				XYZ.getArray()[1][0] / XYZ.getArray()[3][0], 
				-XYZ.getArray()[2][0] / XYZ.getArray()[3][0] };

	}
	
	void testje() {
		System.out.println("--------------");
		System.out.println("Test--");
		System.out.println("left:");
		System.out.println(uglobalL);
		System.out.println(vglobalL);
		System.out.println("right:");
		System.out.println(uglobalR);
		System.out.println(vglobalR);
		System.out.println(globalpoint[0][0] + ", " + globalpoint[1][0] + ", " + globalpoint[2][0]);
		System.out.println(Arrays.toString(calculateXYZ(uglobalL, vglobalL, uglobalR, vglobalR)));

	}

}
