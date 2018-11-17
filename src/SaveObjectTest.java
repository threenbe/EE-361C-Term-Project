import org.junit.Assert;
import org.junit.Test;

public class SaveObjectTest {
    private static MonitorLock lock = new MonitorLock();

    @Test
    public void testSuperField() {
        TestSerialize test = new TestSerialize();
        int expected = test.getNum();
        lock.lock(test);
        test.setNum(100);
        test = (TestSerialize) lock.abort();
        Assert.assertEquals(test.getNum(), expected);
    }

    @Test
    public void testOwnField() {
        TestSerialize test = new TestSerialize();
        int expected = test.aNum;
        lock.lock(test);
        test.aNum = 20;
        test = (TestSerialize) lock.abort();
        Assert.assertEquals(test.aNum, expected);
    }








}
