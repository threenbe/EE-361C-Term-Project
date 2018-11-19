package kryo_design;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorLock<T> {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition cond = lock.newCondition();
	private T lockedObject;
	private T savedState;
	Kryo kryo = null;
	Output output = null;
	Input input = null;

	private MonitorLock(T t) {
		this.lockedObject = t;
	}

	public static <T> MonitorLock<T> from(T t) {
		return new MonitorLock<>(t);
	}


	@SuppressWarnings("unchecked")
	public T lock() {
		lock.lock();
		savedState = deep_copy();
		return lockedObject;
	}

	public T show_state(){
		return deep_copy();
	}

	public void unlock() {
		kryo = null;
		output = null;
		input = null;
		lock.unlock();
	}

	public void await() {
		try {
			cond.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void signal() {
		cond.signal();
	}

	public void signalAll() {
		cond.signalAll();
	}


	public void abort() {
		lockedObject = savedState;
		unlock();
	}

	private T deep_copy(){
		kryo = new Kryo();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		output = new Output(baos);
		input = new Input(output.getBuffer());
		kryo.writeClassAndObject(output, lockedObject);
		try {
			T obj = (T) kryo.readClassAndObject(input);
			input.close();
			output.close();
			return obj;
		}catch(Exception e){
			unlock();
			return null;
		}
	}
}