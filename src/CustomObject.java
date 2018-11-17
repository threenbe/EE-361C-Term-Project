import java.io.Serializable;



public class CustomObject implements Serializable {
    int num = 0;
    String name = "custom";
    boolean bool = false;
    SmallerObject SmallerObject = new SmallerObject();
    int[] arr = {1, 2, 3, 4};


    public String toString(){
        return num + " " + name + " " + bool + " " + SmallerObject.toString() + " " + arr.toString();
    }
}
