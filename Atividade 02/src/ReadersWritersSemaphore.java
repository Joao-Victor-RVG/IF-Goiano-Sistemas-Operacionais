import java.util.concurrent.Semaphore;

public class ReadersWritersSemaphore {
    private static int readCount = 0;
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore writeLock = new Semaphore(1);

    public static void main(String[] args) {
        Runnable writer = () -> {
            try {
                writeLock.acquire();
                System.out.println(Thread.currentThread().getName() + " is writing.");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " finished writing.");
                writeLock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable reader = () -> {
            try {
                mutex.acquire();
                readCount++;
                if (readCount == 1) {
                    writeLock.acquire();
                }
                mutex.release();

                System.out.println(Thread.currentThread().getName() + " is reading.");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " finished reading.");

                mutex.acquire();
                readCount--;
                if (readCount == 0) {
                    writeLock.release();
                }
                mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
