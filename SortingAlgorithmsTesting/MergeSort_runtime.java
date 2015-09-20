import java.util.*;
public class MergeSort_runtime {
	public static void main(String[] args){
		Random rand = new Random();
		int[] randomNums = new int[100000];
		for(int i = 0; i < 100000; ++i){ // generate a hundred thousand random numbers
			int random = rand.nextInt(100) + 1;
			randomNums[i] = random;
		}
		//Merge Sort
		long startTime_MS = System.nanoTime();
		mergeSort(randomNums);
		long endTime_MS = System.nanoTime() - startTime_MS;
		double seconds = (double) endTime_MS / 1000000000.00;
		/* You can test the results by displaying all sorted numbers
		 * 
		 * for(int i = 0; i < randomNums.length; i++){
		 * System.out.println(randomNums[i]);
		 * }
		 */
		System.out.println("To sort a hundred thousand random numbers, the estimated run-time of Merge Sort is: " + seconds + "seconds("+endTime_MS +" nanoseconds).");
	}
	
	public static void mergeSort(int[] inputArray) {
        int size = inputArray.length;
        if (size < 2)
            return;
        int mid = size / 2;
        int leftSize = mid;
        int rightSize = size - mid;
        int[] left = new int[leftSize];
        int[] right = new int[rightSize];
        for (int i = 0; i < mid; i++) {
            left[i] = inputArray[i];

        }
        for (int i = mid; i < size; i++) {
            right[i - mid] = inputArray[i];
        }
        mergeSort(left);
        mergeSort(right);
        merge(left, right, inputArray);
    }

    public static void merge(int[] left, int[] right, int[] arr) {
        int leftSize = left.length;
        int rightSize = right.length;
        int i = 0, j = 0, k = 0;
        while (i < leftSize && j < rightSize) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
                k++;
            } else {
                arr[k] = right[j];
                k++;
                j++;
            }
        }
        while (i < leftSize) {
            arr[k] = left[i];
            k++;
            i++;
        }
        while (j < leftSize) {
            arr[k] = right[j];
            k++;
            j++;
        }
    }
}
