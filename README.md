# EE 361C Term Project

## Monitor with Aborts

This project implements a new type of monitor lock that enables user to have an additional command called `abort()`. Whenever an `abort` is issued, the state of the object is restored to the point at which this thread acquired the lock for that object.

This project utilizes the external library "Kryo" in order to implement our `MonitorLock`. Kryo is a fast, efficient binary object graph serialization framework for Java. **Kryo's source code and release history can be found [here](https://github.com/EsotericSoftware/kryo).**

Instructions for setting up Kryo so that our code may be run can be found below.

### Setting up Kryo

1) Unzip the `kryo-4.0.1-all.zip` file.

2) In the `kryo-4.0.1` folder, there are 3 JARs that must be added to your classpath. 

3) In addition, there are 3 dependency JARs inside the `lib` folder within `kryo-4.0.1` that must also be added to the classpath.

### Test cases

There are currently 4 test cases used to test this monitor lock implementation. Each of their files describe how the tests work, but here are some short descriptions:

1) `Main.java` and `Test.java` form one basic test, in which the main program simply locks on a `Test` object which contains an integer field (initially set to 5) to be incremented. It is incremented once and then the thread issues an `abort()`, causing the object's value to revert back to 5 from 6. It then locks the object again, increments twice, and aborts again, showing that the object's value goes from 7 to 5.

2) `MonitorLockTest.java` contains an interactive test in which the user may lock on an `ArrayList` of `Integer`s and then edit it as they please using various commands. A `print` command shows the current state of the `ArrayList`. The `abort` command issues an `abort` to the `ArrayList`'s monitor, and then `print` can be used to verify that the list's state has been returned to its state when its lock was acquired.

3) `MultiThreadTest2.java` contains a multi-threaded test in which several threads are adding elements to an `ArrayList` concurrently. The additions are synchronized using the `MonitorLock`. For some of the elements, an `abort()` is called rather than an `unlock()`, and so the resulting list must not include the values added by threads that issued an `abort`.

4) `ProposeAndTest.java` contains a complex multi-threaded test in which several threads contain their own "proposal" arrays and must concurrently write to a global "answer" array in a particular manner over the course of several rounds. If the threads don't write the correct value during one round, an `abort` is issued to revert the array back, and the round is restarted. See the `ProposeAndTest.java` file for more details.