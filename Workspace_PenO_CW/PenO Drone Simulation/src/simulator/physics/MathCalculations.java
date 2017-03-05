package simulator.physics;

public class MathCalculations {
	public static double[] getVector(double[] point1, double[] point2) {
		return new double[] { point1[0] - point2[0], point1[1] - point2[1], point1[2] - point2[2] };
	}

	public static double getDotProduct(double[] vector1, double[] vector2) {
		double sum = 0;
		for (int i = 0; i < vector1.length; i++)
			sum += vector1[i] * vector2[i];
		return sum;
	}

	public static double[] getCrossProduct(double[] vector1, double[] vector2) {
		// AxB = (AyBz - AzBy, AzBx - AxBz, AxBy - AyBx)
		double x = vector1[1] * vector2[2] - vector1[2] * vector2[1];
		double y = vector1[2] * vector2[0] - vector1[0] * vector2[2];
		double z = vector1[0] * vector2[1] - vector1[1] * vector2[0];
		return new double[] { x, y, z };
	}

	public static double getDistanceBetweenPoints(double[] point1, double[] point2) {
		return Math.sqrt(Math.pow(point1[0] - point2[0], 2) 
				+ Math.pow(point1[1] - point2[1], 2)
				+ Math.pow(point1[2] - point2[2], 2));
	}
}
