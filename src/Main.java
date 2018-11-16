import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws IOException {
		Test al = new Test(5);
		MonitorLock<Test> lock = new MonitorLock();
		
		lock.lock(al);
		al.increment();
		System.out.println(al);
		al = lock.abort();
		System.out.println(al);
	}
}