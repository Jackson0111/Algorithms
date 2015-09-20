import java.util.*;
public class BubbleSort_runtime {
	public static void main(String[] args){
		Random rand = new Random();
		int[] randomNums = new int[100000];
		for(int i = 0; i < 100000; ++i){ // generate a hundred thousand random numbers
			int random = rand.nextInt(100) + 1;
			randomNums[i] = random;
		}
		long startTime_BS = System.nanoTime();
		//Bubble Sort
		for(int j = 0; j < randomNums.length; ++j){
			for(int i = 0; i < randomNums.length - 1; ++i){
				if(randomNums[i] > randomNums[i+1]){
					swapNumbers(i, randomNums);
				}
			}
		}
		long endTime_BS = System.nanoTime() -startTime_BS;
		double seconds = (double)endTime_BS / 1000000000.00;
		/* You can test the results by displaying all sorted numbers
		 * 
		 * for(int i = 0; i < randomNums.length; i++){
		 * System.out.println(randomNums[i]);
		 * }
		 */
		System.out.println("To sort a hundred thousand random numbers, the estimated run-time of Bubble Sort is: " + seconds + "seconds( "+endTime_BS+" nanoseconds).");
		//end of Bubble Sort
	}
	
	public static void swapNumbers(int i, int[] randomNums){
		int temp = randomNums[i];
		randomNums[i] = randomNums[i+1];
		randomNums[i+1] = temp;
	}
}
// worst case: when the numbers are sorted from the largest to the smallest, so we have to check through every single number and swap them all. n-1 comparisons 
