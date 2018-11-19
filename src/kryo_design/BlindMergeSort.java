package kryo_design;

/*
 * This is a modification of the ProposeAndTest test case.
 * 
 * In this test case, SIZE threads each have their own randomly generated,
 * sorted sub-arrays. There is an "answer" array that must end up with a sorted
 * merged array. The master thread cannot modify the answer array but it orders 
 * the other threads to write to the answer array over the course of several rounds.
 * 
 * The master is able to discern whether or not the threads have proposed the 
 * correct value. If incorrect, it issues an abort() and restarts the round.
 * The end result should be a sorted array  of the proper size(it's okay to 
 * have repeat values).
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BlindMergeSort {
	static final int SIZE = 5;
	static int answer[] = new int[SIZE * SIZE];
	static MonitorLock<int[]> m = MonitorLock.from(answer);
	static ArrayList<Proposer> threads = new ArrayList<>();
	static ArrayList<SortedArray> data = new ArrayList<>();
	static int index = 0;

	public static void main(String[] args) throws InterruptedException {
		// Create SIZE sorted arrays of size SIZE
		for (int i = 0; i < SIZE; i++)
			data.add(new SortedArray());

		while (true) {
			answer = m.lock();

			threads.clear();

			// Create SIZE threads, each of which will propose a value for answer[index]
			for (int i = 0; i < SIZE; i++)
				threads.add(new Proposer(i));
			for (int i = 0; i < SIZE; i++)
				threads.get(i).start();
			for (int i = 0; i < SIZE; i++)
				threads.get(i).join();

			// Check if the correct thread wrote to answer[index]
			if (answer[index] != min()) {
				m.abort();
				System.out.println("Abort on " + Arrays.toString(m.show_state()));
			}
			// Success, move on to next index
			else {
				index++;
				if (index == SIZE * SIZE) { // Check if we're done
					break;
				}
				m.unlock();
			}

		}
		
		for(int i = 0; i < SIZE; i++) {
			System.out.println("Subarray " + i + ": " + Arrays.toString(data.get(i).proposals));
		}
		
		System.out.println("Merged Array: " + Arrays.toString(m.show_state()));
	}

	public static class SortedArray {
		static Random rand = new Random();
		int proposals[];
		int l_index = 0;

		public SortedArray() {
			proposals = new int[SIZE];
			for (int i = 0; i < SIZE; i++)
				proposals[i] = rand.nextInt(SIZE * SIZE * SIZE);
			Arrays.sort(proposals);
		}
	}

	public static class Proposer extends Thread {
		int id;

		// Each thread starts with a sorted array of random numbers
		public Proposer(int id) {
			this.id = id;
		}

		// Propose my value
		public void run() {
			if (data.get(id).l_index < SIZE) {
				answer[index] = data.get(id).proposals[data.get(id).l_index];
			}
		}
	}

	public static int min() {
		int min = SIZE * SIZE * SIZE;
		SortedArray a = null;

		for (int i = 0; i < SIZE; i++) {
			if (data.get(i).l_index < SIZE && data.get(i).proposals[data.get(i).l_index] <= min) {
				a = data.get(i);
				min = a.proposals[a.l_index];
			}
		}

		if (min == answer[index])
			a.l_index++;

		return min;

	}

}
