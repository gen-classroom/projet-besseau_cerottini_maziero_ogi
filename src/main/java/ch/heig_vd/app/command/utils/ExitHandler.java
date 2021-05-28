package ch.heig_vd.app.command.utils;

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
        final Lock lock = new ReentrantLock();
        final Condition exit  = lock.newCondition();

        // Register shutdown hook to cleanly wxit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (watcher){
                for(Thread t : Thread.getAllStackTraces().keySet()){
                    if(t.getName().equals("FileWatcher")){
                        t.interrupt();
                        System.out.println("File watcher interrupted");
                    }
                }
            }

            lock.lock();
            exit.signal();
            lock.unlock();
        }));

        try {
            lock.lock();
            exit.await();
            lock.unlock();
            System.out.println("Server stopped");
        } catch (InterruptedException e) {
            System.out.println("Server was interrupted. Shutting down");
        }
    }
}
