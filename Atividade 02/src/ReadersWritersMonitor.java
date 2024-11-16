public class ReadersWritersMonitor {
    private static int readCount = 0;

    public static synchronized void startRead() throws InterruptedException {
        readCount++;
    }

    public static synchronized void endRead() {
        readCount--;
    }

    public static synchronized void startWrite() throws InterruptedException {
        while (readCount > 0) {
            ReadersWritersMonitor.class.wait();
        }
    }

    public static synchronized void endWrite() {
        ReadersWritersMonitor.class.notifyAll();
    }

    public static void main(String[] args) {

    }
}
