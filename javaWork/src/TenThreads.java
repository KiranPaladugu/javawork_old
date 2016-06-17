import java.util.Random;

public class TenThreads {
	private static class WorkerThread extends Thread {
		int max = Integer.MIN_VALUE;
		int[] ourArray;

		public WorkerThread(int[] ourArray) {
			this.ourArray = ourArray;
		}

		// Find the maximum value in our particular piece of the array
		public void run() {
			for (int i = 0; i < ourArray.length; i++)
				max = Math.max(max, ourArray[i]);
		}

		public int getMax() {
			return max;
		}
	}

	public static void main(String[] args) {
		WorkerThread[] threads = new WorkerThread[10];
		int[][] bigMatrix = getBigHairyMatrix();
		int max = Integer.MIN_VALUE;
		// Give each thread a slice of the matrix to work with
		for (int i = 0; i < 10; i++) {
			threads[i] = new WorkerThread(bigMatrix[i]);
			threads[i].start();
		}
		// Wait for each thread to finish
		try {
			for (int i = 0; i < 10; i++) {
				threads[i].join();
				max = Math.max(max, threads[i].getMax());
			}
		} catch (InterruptedException e) {
			// fall through
		}
		System.out.println("Maximum value was " + max);
	}

	private static int[][] getBigHairyMatrix() {
		Random random = new Random();
		int r=random.nextInt(999);
		System.out.println("Generated r as:"+r);
		int c=random.nextInt(999);
		System.out.println("Generated C as:"+c);
		System.out.println();
		int[][] arr=new int[r][c];
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++){
				arr[i][j]=random.nextInt(255);
//				System.out.println("Generated:"+arr[i][j]+" as char:"+(char)arr[i][j]);
				System.out.print((char)arr[i][j]);
			}
		}
		
		 return arr;
	}
}