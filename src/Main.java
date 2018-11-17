public class Main {
	public static void main(String[] args) {
		MonitorLock<Test> m = new MonitorLock<Test>();
		Test t = new Test(5);
		m.lock(t);
		t.increment();
		System.out.println(t);
		t = m.abort();
		System.out.println(t);
		
	}
}