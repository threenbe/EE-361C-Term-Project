package kryo_design;

/*
 * This is a very basic custom class used to test the 
 * functionality of the MonitorLock's abort() method.
 */

public class Test {
	int x = 0;
	//static int y = 0;
	
	public Test() {
		this(0);
	}
	
	public Test(int x) {
		this.x = x;
	}
	
	public String toString() {
		return Integer.toString(x)/* + " " + Integer.toString(y)*/;
	}
	
	public void increment() {
		x++;
		//y++;
	}
}