package kryo_design;

public class Test {
	int x = 0;
	
	public Test() {
		this(0);
	}
	
	public Test(int x) {
		this.x = x;
	}
	
	public String toString() {
		return Integer.toString(x);
	}
	
	public void increment() {
		x++;
	}
}