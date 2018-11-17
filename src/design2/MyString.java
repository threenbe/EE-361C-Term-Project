package design2;

public class MyString implements Copyable<MyString> {
    String s;

    public MyString(String s) {
        this.s = s;
    }

    @Override
    public MyString copy() {
        return new MyString(s);
    }

    @Override
    public String toString() {
        return s;
    }
}
