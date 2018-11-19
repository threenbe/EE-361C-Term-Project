package kryo_design;

import java.util.ArrayList;

import org.junit.Test;

public class MultiThreadTest2 {
	static MonitorLock<ArrayList<Integer>> lock = MonitorLock.from(new ArrayList<Integer>());
	
	@Test
	public void testLock1() {
		Thread[] threads = new Thread[3];
		threads[0] = new Thread(new MyThread(1, 5));
		threads[1] = new Thread(new MyThread(6, 10));
		threads[2] = new Thread(new MyThreadAbort(11, 20));
		threads[1].start(); threads[0].start(); threads[2].start();

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// should only have 1 to 10 here
		System.out.println("testLock1 result is: " + lock.show_state());
	}

  
  // Adding element to ArrayList
	private class MyThread implements Runnable {
		int begin;
		int end;

		MyThread(int begin, int end) {
			this.begin = begin;
			this.end = end;
		}

		@Override
		public void run() {
			for (int i = begin; i <= end; ++i) {
				ArrayList<Integer> al = lock.lock();
				al.add(i);
				System.out.println("  adding " + i + ": " + al);
				lock.unlock();
			}
		}
	}
  
  // Should not modify arrayList as it aborts
	private class MyThreadAbort implements Runnable {
		int begin;
		int end;

		MyThreadAbort(int begin, int end) {
			this.begin = begin;
			this.end = end;
		}

		@Override
		public void run() {
			for (int i = begin; i <= end; ++i) {
				ArrayList<Integer> al = lock.lock();
				al.add(i);
				System.out.println("aborting " + i + ": " + al);
				lock.abort();
			}
		}
	}
	
}
