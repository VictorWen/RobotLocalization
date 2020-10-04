package Graphics;

/**
 * @author J. Kent Wirant
 * @Version June 10, 2019
 */
public class Matrix {
	
	//columns should be initialized to 1, square matrix required (but not checked)
	public static int calcDeterminant(int[][] matrix, int r1, int r2, int[] columns) {
		if(r2 - r1 <= 1) {
			int index1 = 0;
			int index2;
			
			while(index1 < columns.length && columns[index1] == 0) {
				index1++;
			}
			
			index2 = index1 + 1;
			
			while(index2 < columns.length && columns[index2] == 0) {
				index2++;
			}
			
			return matrix[r1][index1] * matrix[r2][index2] - matrix[r2][index1] * matrix[r1][index2];
			
		} else {
			int sign = 1;
			int sum = 0;
			
			for(int i = 0; i < columns.length; i++) {
				if(columns[i] != 0) {
					columns[i] = 0;
					sum += sign * matrix[r1][i] * calcDeterminant(matrix, r1 + 1, r2, columns);
					columns[i] = 1;
					sign = -sign;
				}
			}
			
			return sum;
		}
	}
	
	//angle is on range [0, 1] (counterclockwise)
	public static void rotate(double[][] points, double angle) {
		double sine = Math.sin(angle * (2 * Math.PI));
		double cosine = Math.cos(angle * (2 * Math.PI));
		double xOriginal, yOriginal;
		
		for(int i = 0; i < points.length; i++) {
			xOriginal = points[i][0];
			yOriginal = points[i][1];
			
			points[i][0] = xOriginal * cosine - yOriginal * sine;
			points[i][1] = xOriginal * sine + yOriginal * cosine;
		}
	}
	
	public static void rotate(double[][] points, double[] origin, double angle) {
		double sine = Math.sin(angle * (2 * Math.PI));
		double cosine = Math.cos(angle * (2 * Math.PI));
		double xOriginal, yOriginal;
		
		for(int i = 0; i < points.length; i++) {
			xOriginal = points[i][0] - origin[0];
			yOriginal = points[i][1] - origin[1];
			
			points[i][0] = xOriginal * cosine - yOriginal * sine + origin[0];
			points[i][1] = xOriginal * sine + yOriginal * cosine + origin[1];
		}
	}
	
	public static void translate(double[][] points, double[] amount) {
		for(int i = 0; i < points.length; i++) {
			points[i][0] += amount[0];
			points[i][1] += amount[1];
		}
	}
	
}
