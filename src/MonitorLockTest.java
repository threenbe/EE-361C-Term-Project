import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MonitorLockTest {
	static MonitorLock<ArrayList<Integer>> lock = new MonitorLock<ArrayList<Integer>>();
	static ArrayList<Integer> al = new ArrayList<Integer>();
	boolean locked = false;
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		while (true) {
			System.out.println("Input a command; 'add #', 'get #', 'remove #', 'abort', 'lock'");
			String input = reader.readLine();
			String[] tokens = input.split(" ");
			if (tokens[0].equals("lock")) {
				lock.lock(al);
			} else if (tokens[0].equals("unlock")) {
				lock.unlock();
			} else if (tokens[0].equals("abort")) {
				al = lock.abort();
			} else if (tokens[0].equals("add")) {
				al.add(Integer.parseInt(tokens[1]));
			} else if (tokens[0].equals("get")) {
				System.out.println(al.get(Integer.parseInt(tokens[1])));
			} else if (tokens[0].equals("remove")) {
				al.remove(Integer.parseInt(tokens[1]));
			} else if (tokens[0].equals("print")) {
				System.out.println(al);
			} else if (tokens[0].equals("quit")) {
				break;
			}else {
				System.out.println("not valid command!");
			}
		}
	}
}
