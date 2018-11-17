package design2;

public class Test {
    public static void main(String[] args){
		MyString str = new MyString("test");
		MyMonitor<MyString> monitor = MyMonitor.from(str);
		System.out.println(monitor.readState());
		MyString temp = monitor.lock();
		temp.s = "changed";
		System.out.println(monitor.readState());
		monitor.abort();
		System.out.println(monitor.readState());
    }


}
