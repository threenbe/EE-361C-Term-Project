package kryo_design;

import java.util.ArrayList;
import java.util.Arrays;

public class ProposeAndTest {
	static final int SIZE = 20;
	static int answer[] = new int[SIZE];
	static MonitorLock<int[]> m = new MonitorLock<int[]>();
	static int index = 0;
	static ArrayList<Proposer> threads = new ArrayList<Proposer>();

	public static void main(String[] args) throws InterruptedException {

		while (true) {
			m.lock(answer);
			threads.clear();

			// Create SIZE threads, each of which will propose a value for answer[index]
			for (int i = 0; i < SIZE; i++)
				threads.add(new Proposer(i));
			for (int i = 0; i < SIZE; i++)
				threads.get(i).start();
			for (int i = 0; i < SIZE; i++)
				threads.get(i).join();

			// Check if the correct thread wrote to answer[index]
			if (answer[index] != index) {
				answer = m.abort();
				System.out.println("Failed on " + Arrays.toString(answer));
				continue;	// Retry
			} 
			
			
			// Success, move on to next index
			else {
				index++;
				if (index == SIZE) {	// Check if we're done
					break;
				}
			}

			m.unlock();

		}

		System.out.println("Final answer: " + Arrays.toString(answer));

	}

	public static class Proposer extends Thread {
		int proposals[];

		public Proposer(int num) {
			proposals = new int[SIZE];
			proposals[num] = num;
		}

		// Propose my value
		public void run() {
			answer[index] = proposals[index];
		}

	}

}

