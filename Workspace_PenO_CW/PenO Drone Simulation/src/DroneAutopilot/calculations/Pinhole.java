package DroneAutopilot.calculations;

import java.util.ArrayList;
import java.util.Arrays;

import DroneAutopilot.DroneAutopilot;
import Jama.Matrix;

public class Pinhole {
	
	
	private DroneAutopilot ap;
	private Matrix leftC;
	private Matrix rightC;
	private Matrix leftR1;
	private Matrix leftR2;
	private Matrix leftR3;
	private Matrix rightR1;
	private Matrix rightR2;
	private Matrix rightR3;


	public Pinhole(DroneAutopilot ap) {
		this.ap = ap;
		setup();
	}
	
	private void setup() {
		double horizfov = ap.getDrone().getLeftCamera().getHorizontalAngleOfView();
		double vertfov = ap.getDrone().getLeftCamera().getVerticalAngleOfView();
		int cameraWidth = ap.getDrone().getLeftCamera().getWidth();
		int cameraHeight = (int) (ap.getDrone().getLeftCamera().takeImage().length/cameraWidth);
		double f = ap.getPhysicsCalculations().getFocalDistance();
		double mx = cameraWidth / (2 * Math.tan(horizfov * Math.PI/360));
		double ax = f*mx;
		double camSep = 0.35;
		
		
		Matrix Cpre = new Matrix(new double[][]{{ax,0,0,0}, {0,ax,0,0}, {0,0,1,0}});
		
		Matrix p1r = new Matrix(new double[]{5.8, 2.5, 1.9, 1}, 	4);
		Matrix p2r = new Matrix(new double[]{3.2, 5.9, 4.9, 1}, 	4);
		Matrix p3r = new Matrix(new double[]{5.3, 3.4, 3.4, 1}, 	4);
		Matrix p4r = new Matrix(new double[]{2.7, 4.3, 1.4, 1}, 	4);
		Matrix p5r = new Matrix(new double[]{4.2, 1.6, 4.6, 1}, 	4);
		Matrix p6r = new Matrix(new double[]{1.9, 1, 1.9, 	1}	,	4);
		ArrayList<Matrix> rightPoints = new ArrayList<>(6);
		rightPoints.add(p1r);
		rightPoints.add(p2r);
		rightPoints.add(p3r);
		rightPoints.add(p4r);
		rightPoints.add(p5r);
		rightPoints.add(p6r);
		

		Matrix p1l = new Matrix(new double[]{5.8 + camSep, 2.5, 1.9, 1}, 	4);
		Matrix p2l = new Matrix(new double[]{3.2 + camSep, 5.9, 4.9, 1}, 	4);
		Matrix p3l = new Matrix(new double[]{5.3 + camSep, 3.4, 3.4, 1}, 	4);
		Matrix p4l = new Matrix(new double[]{2.7 + camSep, 4.3, 1.4, 1}, 	4);
		Matrix p5l = new Matrix(new double[]{4.2 + camSep, 1.6, 4.6, 1}, 	4);
		Matrix p6l = new Matrix(new double[]{1.9 + camSep, 1, 1.9, 	1},		4);
		ArrayList<Matrix> leftPoints = new ArrayList<>(6);
		leftPoints.add(p1l);
		leftPoints.add(p2l);
		leftPoints.add(p3l);
		leftPoints.add(p4l);
		leftPoints.add(p5l);
		leftPoints.add(p6l);

//		5.8 5.5 1.9 
//		3.2 5.9 4.9 
//		5.3 5.4 3.4 
//		2.7 4.3 1.4 
//		4.2 1.6 4.6 
//		5.9 1 	1.9
		
		
		double[][] coeffL = new double[12][12];
		double[][] coeffR = new double[12][12];
		
		for(int i = 0; i < rightPoints.size(); i++) {
			Matrix uvw = Cpre.times(rightPoints.get(i));

			double u_accent = (uvw.get(0, 0)/uvw.get(2, 0));
			double v_accent =  (uvw.get(1, 0)/uvw.get(2, 0));

			
			//add coefficients to coefficients matrix
			coeffR[2 * i] = new double[] { -rightPoints.get(i).get(0, 0), 0, u_accent*rightPoints.get(i).get(0, 0),-rightPoints.get(i).get(1, 0), 0, u_accent * rightPoints.get(i).get(1, 0), -rightPoints.get(i).get(2, 0), 0, u_accent*rightPoints.get(i).get(2, 0), -rightPoints.get(i).get(3, 0), 0, u_accent*rightPoints.get(i).get(3, 0) };
			coeffR[2 * i + 1] = new double[] { 0, -rightPoints.get(i).get(0, 0), v_accent*rightPoints.get(i).get(0, 0), 0, -rightPoints.get(i).get(1, 0), v_accent*rightPoints.get(i).get(1, 0), 0, -rightPoints.get(i).get(2, 0), v_accent*rightPoints.get(i).get(2, 0), 0, -rightPoints.get(i).get(3, 0), v_accent*rightPoints.get(i).get(3, 0) };
			
			coeffL[2 * i] = new double[] { -leftPoints.get(i).get(0, 0), 0, u_accent*leftPoints.get(i).get(0, 0), -leftPoints.get(i).get(1, 0), 0, u_accent * leftPoints.get(i).get(1, 0), -leftPoints.get(i).get(2, 0), 0, u_accent*leftPoints.get(i).get(2, 0), -leftPoints.get(i).get(3, 0), 0, u_accent*leftPoints.get(i).get(3, 0) };
			coeffL[2 * i + 1] = new double[] { 0, -leftPoints.get(i).get(0, 0), v_accent*leftPoints.get(i).get(0, 0), 0, -leftPoints.get(i).get(1, 0), v_accent*leftPoints.get(i).get(1, 0), 0, -leftPoints.get(i).get(2, 0), v_accent*leftPoints.get(i).get(2, 0), 0, -leftPoints.get(i).get(3, 0), v_accent*leftPoints.get(i).get(3, 0) };
		}
		coeffR[11] = new double[]{0,0,0,0,0,0,0,0,0,0,0,1};
		coeffL[11] = new double[]{0,0,0,0,0,0,0,0,0,0,0,1};
		double[] b = new double[]{0,0,0,0,0,0,0,0,0,0,0,1};
		
		Matrix zeros = new Matrix(b, 12);
		Matrix coeffMatrixR = new Matrix(coeffR);
		Matrix Cright = coeffMatrixR.solve(zeros);
		Matrix coeffMatrixL = new Matrix(coeffL);
		Matrix CLeft = coeffMatrixL.solve(zeros);
		double[][] arr = CLeft.getArray();
		
		this.leftC = new Matrix(new double[][]{{arr[0][0], arr[3][0], arr[6][0], arr[9][0]}, {arr[1][0], arr[4][0], arr[7][0], arr[10][0]}, {arr[2][0], arr[5][0], arr[8][0], arr[11][0]}});
		this.leftR1 = new Matrix(new double[]{arr[0][0], arr[3][0], arr[6][0], arr[9][0]}, 1);
		this.leftR2 = new Matrix(new double[]{arr[1][0], arr[4][0], arr[7][0], arr[10][0]}, 1);
		this.leftR3 = new Matrix(new double[]{arr[2][0], arr[5][0], arr[8][0], arr[11][0]}, 1);

		arr = Cright.getArray();
		this.rightC = new Matrix(new double[][]{{arr[0][0], arr[3][0], arr[6][0], arr[9][0]}, {arr[1][0], arr[4][0], arr[7][0], arr[10][0]}, {arr[2][0], arr[5][0], arr[8][0], arr[11][0]}});
		
		this.rightR1 = new Matrix(new double[]{arr[0][0], arr[3][0], arr[6][0], arr[9][0]}, 1);
		this.rightR2 = new Matrix(new double[]{arr[1][0], arr[4][0], arr[7][0], arr[10][0]}, 1);
		this.rightR3 = new Matrix(new double[]{arr[2][0], arr[5][0], arr[8][0], arr[11][0]}, 1);
//		
	}
	
	public double[] calculateXYZ(int xLeft, int yLeft, int xRight, int yRight) {
		//TODO x = u, y = v of omgekeerd?
		int vLeft = yLeft;
		int vRight = yRight;
		int uLeft = xLeft;
		int uRight = xRight;
		double[] firstL = (leftR3.times(uLeft)).minus(leftR1).getArray()[0];
		double[] firstR = (rightR3.times(uRight)).minus(rightR1).getArray()[0];
		double[] secondL = (leftR3.times(vLeft)).minus(leftR2).getArray()[0];
		double[] secondR = (rightR3.times(vRight)).minus(rightR2).getArray()[0];
		
		Matrix C = new Matrix(new double[][]{firstL, firstR, secondL, {0,0,0,1}});
		C.print(10, 10);
		Matrix b = new Matrix(new double[]{0,0,0,1}, 4);
		Matrix XYZ = C.solve(b);
		//TODO delen door de 4e waarde van XYZ
		System.out.println(Arrays.toString(new double[]{XYZ.getArray()[0][0], XYZ.getArray()[1][0], XYZ.getArray()[2][0]}));
		return new double[]{XYZ.getArray()[0][0], XYZ.getArray()[1][0], XYZ.getArray()[2][0]};

	}
	
	

}
