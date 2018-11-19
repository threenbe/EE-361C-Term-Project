package kryo_design;
/*
 * This is an interactive test used solely to test the functionality of the MonitorLock's abort() 
 * method. 
 * 
 * First, the ArrayList must be locked on by this program. Then, the user may perform any add(), get(), 
 * or remove() operations on the ArrayList before either unlocking or aborting. The user can also print
 * the current state of the ArrayList at any time. So, the user may print the ArrayList's current state, lock it,
 * add or remove Integers as they please, and then abort before printing again. The ArrayList's state will return
 * to what it was upon being locked.
 * 
 * Commands:
 * lock: Locks the ArrayList
 * unlock: Unlocks the ArrayList
 * abort: Issues an "abort" to the monitor, reverting its ArrayList back to its state upon being locked
 * add <i>: Adds an Integer "i" to the ArrayList
 * get <i>: Returns the element at index "i" of the ArrayList
 * remove <i>: Removes the element at index "i" of the ArrayList
 * print: Prints the current state of the monitor lock's ArrayList
 * quit: Terminates the program
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MonitorLockTest {
	static MonitorLock<ArrayList<Integer>> lock = MonitorLock.from(new ArrayList<Integer>());
	boolean locked = false;
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<Integer> al = null;
		while (true) {
			System.out.println("Input a command; 'add #', 'get #', 'remove #', 'abort', 'lock'");
			String input = reader.readLine();
			String[] tokens = input.split(" ");
			if (tokens[0].equals("lock")) {
				al = lock.lock();
			} else if (tokens[0].equals("unlock")) {
				lock.unlock();
			} else if (tokens[0].equals("abort")) {
				lock.abort();
			} else if (tokens[0].equals("add")) {
				al.add(Integer.parseInt(tokens[1]));
			} else if (tokens[0].equals("get")) {
				System.out.println(al.get(Integer.parseInt(tokens[1])));
			} else if (tokens[0].equals("remove")) {
				al.remove(Integer.parseInt(tokens[1]));
			} else if (tokens[0].equals("print")) {
				System.out.println(lock.show_state());
			} else if (tokens[0].equals("quit")) {
				break;
			}else {
				System.out.println("not valid command!");
			}
		}
	}
}
