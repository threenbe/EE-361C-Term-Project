import java.io.Serializable;

@SuppressWarnings("serial")
public class Test implements Serializable {
	//private static final long serialVersionUID = 1L;
	int x = 0;
	
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