package Graphics;

/**
 * @author J. Kent Wirant
 * @Version June 10, 2019
 */
public class ArrayUtility {
	
	//sorts double arrays by the element at the specified index. workArray is passed in so memory does not
	//need to be reallocated for each sort or iteration. This increases speed. workArray.length == array.length
	public static void mergeSortGroup(double[][] array, double[][] workArray, int sortIndex, int start, int end) {
		if(end - start > 1) {
			int middle = (start + end) / 2;
			mergeSortGroup(array, workArray, sortIndex, start, middle);
			mergeSortGroup(array, workArray, sortIndex, middle, end);
			mergeGroup(array, workArray, sortIndex, start, end);
		}
	}
	
	private static void mergeGroup(double[][] array, double[][] workArray, int sortIndex, int start, int end) {
		int middle = (start + end) / 2, i = start, j = middle, k = start;
		
		while(i < middle && j < end) {
			if(array[i][sortIndex] < array[j][sortIndex]) {
				workArray[k] = array[i];
				i++;
			} else {
				workArray[k] = array[j];
				j++;
			}
			
			k++;
		}
		
		while(i < middle) {
			workArray[k] = array[i];
			i++;
			k++;
		}
		
		while(j < end) {
			workArray[k] = array[j];
			j++;
			k++;
		}
		
		for(k = start; k < end; k++) {
			array[k] = workArray[k];
		}
	}
	
	public static void groupSwap(double[][] array, int index1, int index2) {
		double[] temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
	
	public static void printfArray2D(double[][] array, String format, int startRow, int endRow, int startCol, int endCol) {
		for(int i = startRow; i < endRow && i < array.length; i++) {
			for(int j = startCol; j < endCol && j < array[i].length; j++) {
				System.out.printf(format, array[i][j]);
			}
			
			System.out.println();
		}
	}
	
	//returns the index of the first element that is greater than the target value, or -1 if no such value exists.
	public static int binarySearchRange(double[][] array, int targetIndex, double value, int start, int end) {
		int middle = 0;
		
		while(start < end) {
			middle = (start + end) / 2;
			
			if(array[middle][targetIndex] > value) {
				end = middle - 1;
			} else {
				start = middle + 1;
			}
		}
		
		if(array[middle][targetIndex] > value && (middle == 0 || array[middle - 1][targetIndex] < value)) {
			return middle;
		} else {
			return -1;
		}
	}
	
	public static double[][] deepCopy(double[][] array) {
		double[][] copy = new double[array.length][];
		
		for(int i = 0; i < copy.length; i++) {
			copy[i] = new double[array[i].length];
			
			for(int j = 0; j < copy[i].length; j++) {
				copy[i][j] = array[i][j];
			}
		}
		
		return copy;
	}
	
}
