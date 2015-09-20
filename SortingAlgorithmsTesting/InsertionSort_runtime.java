import java.util.*;
public class InsertionSort_runtime {
	public static void main(String[] args){
		Random rand = new Random();
		int[] randomNums = new int[100000];
		for(int i = 0; i < 100000; ++i){ // generate a hundred thousand random numbers
			int random = rand.nextInt(100) + 1;
			randomNums[i] = random;
		}
		long startTime_IS = System.nanoTime();
		//Insertion Sort
		for(int i = 1; i < randomNums.length; ++i){
			for(int j = i; j > 0; --j){
				if(randomNums[j] < randomNums[j - 1]){
					action(j, randomNums);
				}
			}
		}
		//end of Insertion Sort
		long endTime_IS = System.nanoTime() - startTime_IS;
		double seconds = (double) endTime_IS / 1000000000.00;
		/* You can test the results by displaying all sorted numbers
		 * 
		 * for(int i = 0; i < randomNums.length; i++){
		 * System.out.println(randomNums[i]);
		 * }
		 */
		System.out.println("To sort a hundred thousand random numbers, the estimated run-time of Insertion Sort is: " + seconds+ "seconds( "+endTime_IS+" nanoseconds).");
	}
	
	public static void action(int j, int[] randomNums){
		int temp = randomNums[j - 1];
		randomNums[j - 1] = randomNums[j];
		randomNums[j] = temp;
	}
}
//worst case: when array is sorted in reverse order
