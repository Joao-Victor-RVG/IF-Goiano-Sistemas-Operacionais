import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Garfo {
    private final Lock lock = new ReentrantLock();

    public boolean pegar() {
        return lock.tryLock();
    }

    public void soltar() {
        lock.unlock();
    }
}

class Filosofo extends Thread {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;

    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void comer() throws InterruptedException {
        System.out.println("Filósofo " + id + " está comendo.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    @Override
    public void run() {
        try {
            while (true) {
                pensar();
                if (garfoEsquerdo.pegar()) {
                    try {
                        if (garfoDireito.pegar()) {
                            try {
                                comer();
                            } finally {
                                garfoDireito.soltar();
                            }
                        }
                    } finally {
                        garfoEsquerdo.soltar();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class JantarFilosofos {
    public static void main(String[] args) {
        int numFilosofos = 5;
        Garfo[] garfos = new Garfo[numFilosofos];
        Filosofo[] filosofos = new Filosofo[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) {
            garfos[i] = new Garfo();
        }

        for (int i = 0; i < numFilosofos; i++) {
            filosofos[i] = new Filosofo(i, garfos[i], garfos[(i + 1) % numFilosofos]);
            filosofos[i].start();
        }
    }
}
