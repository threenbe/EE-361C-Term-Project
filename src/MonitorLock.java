import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a new type of lock that will be associated with our new monitor interface.
 * This lock will support the normal monitor synchronization methods, i.e. wait(), 
 * notify(), and notifyAll(). It will also need to acquire and release explicitly, rather
 * than implicitly like with standard Java monitors. 
 * In addition, we will implement a new function, abort().
 * @param <T>
 */
public class MonitorLock<T> {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition cond = lock.newCondition();
	private T oldObj = null;
	
	public void lock(T obj) {
		lock.lock();
		oldObj = obj;
	}
	
	public void unlock() {
		oldObj = null;
		lock.unlock();
	}
	
	public void await() {
		try {
			cond.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void signal() {
		cond.signal();
	}
	
	public void signalAll() {
		cond.signalAll();
	}
	
	/**
	 * Just putting down something we can try for now basically
	 * This'll require the user to explicitly do, for example,
	 * "original_thing_that_was_locked_on = abort()," which isn't ideal, but this is just what
	 * I've got off the top of my head and I haven't figured out anything 
	 * better yet.
	 * @return
	 */
	public T abort() {
		T ret = oldObj;
		unlock();
		return ret;
	}
}