package kryo_design;

/**
 * This interface represents the functionality that must be
 * implemented by our MonitorLock class.
 *
 * @param <T>
 */
public interface Locks<T> {
	/**
	 * This method locks on an object.
	 * 
	 * @return the monitor lock for this object
	 */
	public T lock();
	
	/**
	 * This method unlocks the monitor lock.
	 */
	public void unlock();
	
	/**
	 * When an abort() is issued, the state of this monitor's object 
	 * must be restored to the point at which the current thread acquired 
	 * the lock for this object.
	 */
	public void abort();
	
	/**
	 * Causes the current thread to wait, relinquishing the lock.
	 */
	public void await();
	
	/**
	 * Wakes up one thread waiting on this lock.
	 */
	public void signal();
	
	/**
	 * Wakes up all threads waiting on this lock.
	 */
	public void signalAll();
}