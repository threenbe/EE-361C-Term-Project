/**
 * This custom monitor is associated with a new monitor lock that supports the standard
 * acquire/release methods of a normal lock; the wait(), notify(), and notifyAll() methods of 
 * standard Java monitors (renamed so as to not "override the final method(s) from Object," apparently); 
 * and the new abort() function. 
 */
interface NewMonitor<T> {
	final MonitorLock m = new MonitorLock();
	
	default void lock(T obj) {
		m.lock(obj);
	}
	
	default void unlock() {
		m.unlock();
	}
	
	default void await() {
		m.await();
	}
	
	default void signal() {
		m.signal();
	}
	
	default void signalAll() {
		m.signalAll();
	}
	
	/**
	 * Not sure if that warning is an indicator of some potential problem, we'll see
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	default T abort() {
		return (T) m.abort();
	}
}
