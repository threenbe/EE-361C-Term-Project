/*
SIZE threads are created. Each thread has a local "proposals" array of integers
of size SIZE. Each of these arrays are initialized slightly differently. For example 
Thread 0's array has a 0 at index 0 and 0 everywhere else. Thread 1
has a 1 at index 1 and 0 everywhere else. Thread 2 has 2 at index 2 and 0 everywhere
else etc.

There are SIZE rounds that all of the threads must go through. During each round,
all threads concurrently write to the global "answer" array at index "index" using
their proposed value at proposals[index]. In order for the threads to "pass" a round, 
Thread i must be the last to write to answer[i] during round i. For example, during 
round 1, thread 1 should be the last to write to answer[1] so that answer[1] ends up
having the value 1. For round 2, thread 2 should be the last to write to answer[2] so
that answer[2] ends up having the value 2 etc.

If the correct thread is the last to write its proposal value to the answers array
then index is incremented and we can move on to the next round. Else, we must abort
and try again.
*/

package kryo_design;

import java.util.ArrayList;
import java.util.Arrays;

public class ProposeAndTest {
	static final int SIZE = 20;
	static MonitorLock<int[]> m = MonitorLock.from(new int[SIZE]);
	static int index = 0;
	static ArrayList<Proposer> threads = new ArrayList<Proposer>();

	public static void main(String[] args) throws InterruptedException {

		while (true) {

			threads.clear();

			// Create SIZE threads, each of which will propose a value for answer[index]
			for (int i = 0; i < SIZE; i++)
				threads.add(new Proposer(i));
			for (int i = 0; i < SIZE; i++)
				threads.get(i).start();
			for (int i = 0; i < SIZE; i++)
				threads.get(i).join();

			if (index == SIZE) {
				break;
			}

		}

		System.out.println("Final answer: " + Arrays.toString(m.show_state()));

	}

	public static class Proposer extends Thread {
		int proposals[];

		public Proposer(int num) {
			proposals = new int[SIZE];
			proposals[num] = num;
		}

		// Propose my value
		public void run() {
			int[] answer = m.lock();
			if(index < answer.length) {
				answer[index] = proposals[index];
				// Check if the correct thread wrote to answer[index]
				if (answer[index] != index) {
					m.abort();
					System.out.println("Failed on " + Arrays.toString(answer));
				}
				// Success, move on to next index
				else {
					index++;
					m.unlock();
				}
			}

		}

	}

}

