package design2;

public class Test {
    public static void main(String[] args){
        MyMonitor<MyString> monitor = MyMonitor.from(new MyString("test"));
        System.out.println(monitor.readState());
        MyString temp = monitor.lock();
        temp.s = "changed";
        monitor.abort();
        System.out.println(monitor.readState());
    }


}
