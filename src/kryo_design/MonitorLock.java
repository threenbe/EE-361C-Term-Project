package kryo_design;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is a new type of lock that will be used to implement our new monitor lock.
 * This lock will support the normal monitor synchronization methods, i.e. wait(), 
 * notify(), and notifyAll(). It will also need to acquire and release explicitly, rather
 * than implicitly like with standard Java monitors. 
 * In addition, we will implement a new function, abort().
 * 
 * This implementation utilizes external library Kryo in order to serialize and deserialize
 * objects to save their states. Kryo's source code and release history can be found here:
 * https://github.com/EsotericSoftware/kryo 
 * @param <T>
 */
public class MonitorLock<T> implements Locks<T> {
	private final ReentrantLock lock = new ReentrantLock();//for locking
	private final Condition cond = lock.newCondition();//for waiting and notifying
	private T lockedObject;//the object being locked/operated on
	private T savedState;//the state of the object upon initially locking it
	Kryo kryo = null;
	Output output = null;
	Input input = null;

	/**
	 * Lock constructor, this makes it so that this 
	 * monitor is associated with a particular object t.
	 * @param t
	 */
	private MonitorLock(T t) {
		this.lockedObject = t;
	}

	/**
	 * This is used when initializing the object/monitor relationship. For example,
	 * upon calling MonitorLock.from(o) for some "Object o," the method will return
	 * the monitor lock associated with Object o. 
	 * @param t
	 * @return t's monitor lock
	 */
	public static <T> MonitorLock<T> from(T t) {
		return new MonitorLock<>(t);
	}

	/**
	 * Locks on this monitor's object and saves a copy of its
	 * initial state via serialization using Kryo.
	 */
	public T lock() {
		lock.lock();
		savedState = deep_copy();
		return lockedObject;
	}
	
	/**
	 * Unlocks the lock on this monitor's object.
	 */
	public void unlock() {
		kryo = null;
		output = null;
		input = null;
		lock.unlock();
	}
	
	/**
	 * This method will retrieve the deserialized initial state of the
	 * object and revert the current object's state with it. It will then
	 * unlock the monitor.
	 * NOTE that in order for the user to actually retrieve the locked object's
	 * restored state, they must call the "show_state()" method or immediately
	 * lock again using the "lock()" method.
	 */
	public void abort() {
		lockedObject = savedState;
		unlock();
	}

	/**
	 * When abort() is called and the object's state is reverted back
	 * to what it was upon acquiring the lock, this method *should* be called
	 * in order to retrieve that version of the object, *unless* the thread 
	 * locks on the object again immediately, in which case lock() will return
	 * the correct object.
	 * 
	 * @return the locked object's current state
	 */
	public T show_state(){
		return deep_copy();
	}

	/**
	 * Causes the current thread to wait until another thread invokes signal()
	 * or signalAll(). The thread must own this object's monitor lock, and calling
	 * this method will release that lock until it wakes up.
	 */
	public void await() {
		try {
			cond.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wakes up one thread that is waiting on this monitor.
	 * This method must be called by an owner of the monitor lock.
	 */
	public void signal() {
		cond.signal();
	}

	/**
	 * Wakes up all threads waiting on this monitor.
	 * This method must be called by an owner of the monitor lock.
	 */
	public void signalAll() {
		cond.signalAll();
	}

	/**
	 * This method is responsible for the serialization of the locked
	 * object's initial state. Kryo is used to write the serialized data
	 * into a buffer. This way, a copy of the object can be constructed by 
	 * deserializing the data in said buffer, without having to know what
	 * its fields are or requiring a copy constructor/method. The copy of
	 * the object is then saved, only acquired when abort() is called.
	 * @return
	 */
	private T deep_copy(){
		kryo = new Kryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		output = new Output(baos);
		input = new Input(output.getBuffer());
		kryo.writeClassAndObject(output, lockedObject);//serialize the object
		try {
			T obj = (T) kryo.readClassAndObject(input);//deserialize the object to create a copy
			input.close();
			output.close();
			return obj;//set savedState equal to the copied object
		}catch(Exception e){
			unlock();
			return null;
		}
	}
}