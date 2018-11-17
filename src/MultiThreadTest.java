import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MultiThreadTest {
	static MonitorLock<ArrayList<Integer>> lock = new MonitorLock<ArrayList<Integer>>();
	static ArrayList<Integer> al = new ArrayList<Integer>();

	@Test
	public void testLock() {
		Thread[] threads = new Thread[3];
		threads[0] = new Thread(new MyThread(0, 5, al));
		threads[1] = new Thread(new MyThread(6, 10, al));
		threads[2] = new Thread(new MyThread(11, 20, al));
		threads[1].start(); threads[0].start(); threads[2].start();

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    
    //should have 0 to 20
		System.out.println(al);
	}

	@Test
	public void testLock2() {
		Thread[] threads = new Thread[3];
		threads[0] = new Thread(new MyThread(0, 5, al));
		threads[1] = new Thread(new MyThread(6, 10, al));
		threads[2] = new Thread(new MyThreadAbort(11, 20, al));
		threads[1].start(); threads[0].start(); threads[2].start();

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// should only have 0 to 10 here
		System.out.println(al);
	}

  
  // Adding element to ArrayList
	private class MyThread implements Runnable {

		int begin;
		int end;
		ArrayList<Integer> list;

		MyThread(int begin, int end, ArrayList<Integer> list) {
			this.begin = begin;
			this.end = end;
			this.list = list;
		}

		@Override
		public void run() {
			for (int i = begin; i <= end; ++i) {
				lock.lock(list);
				list.add(i);
				lock.unlock();
			}
		}
	}
  
  // Should not modify arrayList as it aborts
	private class MyThreadAbort implements Runnable {
		int begin;
		int end;
		ArrayList<Integer> list;

		MyThreadAbort(int begin, int end, ArrayList<Integer> list) {
			this.begin = begin;
			this.end = end;
			this.list = list;
		}

		@Override
		public void run() {
			for (int i = begin; i <= end; ++i) {
				lock.lock(list);
				list.add(i);
				list = lock.abort();
			}
		}
	}
}
