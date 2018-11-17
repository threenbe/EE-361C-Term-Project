package design2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class MyMonitor<T extends Copyable<T>> {
    private T lockedObject;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    private T savedState;
    private MyMonitor(T t) {
        this.lockedObject = t;
    }

    T lock() {
        lock.lock();
        savedState = lockedObject.copy();
        return lockedObject;
    }

    T readState(){
        return lockedObject.copy();
    }

    boolean isLocked() {
        return lock.isLocked();
    }

    void unlock() {
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

    void abort() {
        lockedObject = savedState;
        lock.unlock();
    }

    public static <T extends Copyable<T>> MyMonitor<T> from(T t) {
        return new MyMonitor<>(t);
    }
}
