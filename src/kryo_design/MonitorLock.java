package kryo_design;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MonitorLock<T> {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition cond = lock.newCondition();
	Kryo kryo = null;
	Output output = null;
	Input input = null;

	public void lock(T obj) {
		lock.lock();
		kryo = new Kryo();
		try {
			output = new Output(new FileOutputStream("file.dat"));
			input = new Input(new FileInputStream("file.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		kryo.writeClassAndObject(output, obj);
		output.close();
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
	
	@SuppressWarnings("unchecked")
	public T abort() {
		try {
			T obj = (T) kryo.readClassAndObject(input);
			input.close();
			return obj;
		} finally {
			unlock();
		}
	}
}