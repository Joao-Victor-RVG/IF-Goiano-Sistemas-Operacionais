import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadersWritersMutex {
    private static int readCount = 0;
    private static final Lock lock = new ReentrantLock();
    private static final Lock writeLock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable writer = () -> {
            writeLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " is writing.");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " finished writing.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        };

        Runnable reader = () -> {
            lock.lock();
            try {
                readCount++;
                if (readCount == 1) {
                    writeLock.lock();
                }
            } finally {
                lock.unlock();
            }

            try {
                System.out.println(Thread.currentThread().getName() + " is reading.");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " finished reading.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.lock();
            try {
                readCount--;
                if (readCount == 0) {
                    writeLock.unlock();
                }
            } finally {
                lock.unlock();
            }
        };

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(reader, "Reader-" + i);
            threads[i + 5] = new Thread(writer, "Writer-" + i);
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
