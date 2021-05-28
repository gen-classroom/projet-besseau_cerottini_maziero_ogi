package ch.heig_vd.app.command.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exit handler to simplify working with thread when killing the program
 */
public class ExitHandler {

    /**
     * Exit logic to gracefully exit the program
     * @param watcher if the fileWatcher thread must also be stopped
     */
    public static void awaitSignal(boolean watcher){
        AtomicBoolean loop = new AtomicBoolean(true);
        final Lock lock = new ReentrantLock();
        final Condition exit  = lock.newCondition();

        // Register shutdown hook to cleanly exit
        Thread exitThread = new Thread(() -> {
            System.out.println();
            if (watcher){
                for(Thread t : Thread.getAllStackTraces().keySet()){
                    if(t.getName().equals("FileWatcher")){
                        t.interrupt();
                        System.out.println("File watcher interrupted");
                    }
                }
            }
            lock.lock();
            loop.set(false);
            exit.signal();
            lock.unlock();
        });
        exitThread.setName("ExitThread");

        Runtime.getRuntime().addShutdownHook(exitThread);

        try {
            lock.lock();
            while (loop.get()){
                exit.await();
            }
            lock.unlock();
            System.out.println("Service stopped");
        } catch (InterruptedException e) {
            lock.unlock();
            System.out.println("Service was interrupted. Shutting down");
        }
        System.out.println("System was closed");
    }
}
