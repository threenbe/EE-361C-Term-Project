package kryo_design;

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
