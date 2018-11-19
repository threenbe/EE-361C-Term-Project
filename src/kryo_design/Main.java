package kryo_design;

/*
 * This program uses the Test class to test the MonitorLock's abort() method in a simple fashion.
 * 
 * It simply retrieves the monitor associated with a Test object with a value of 5.
 * Then, it acquires the lock and increments the value to 6 before aborting. The prints 
 * should show that the object's value reverts back to 5 upon aborting.
 * 
 * A second test acquires the lock again and increments twice before aborting. Again, the 
 * object's value in this case should go from 7 to 5 as a result of the abort.
 */

public class Main {
	public static void main(String[] args) {
		MonitorLock<Test> m = MonitorLock.from(new Test(5));
		Test t = m.lock();
		t.increment();
		System.out.println(m.show_state());
		m.abort();
		System.out.println(m.show_state());

		t = m.lock();
		t.increment();
		t.increment();
		System.out.println(m.show_state());
		m.abort();
		System.out.println(m.show_state());
	}
}
