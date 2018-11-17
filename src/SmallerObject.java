import java.io.Serializable;

public class SmallerObject implements Serializable {
    int smallNum = 18;


    public String toString() {
        return Integer.toString(smallNum);
    }

    public void setNum(int num) {
        this.smallNum = num;
    }

    public int getNum(){
        return this.smallNum;
    }
}
