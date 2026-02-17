package org.example;

/**
 * Worker thread that repeatedly takes the next number from the coordinator,
 * checks primality, and reports the result. Reused for many numbers (no thread-per-number).
 */
public class CounterThread extends Thread {
    private final InterfaceThread coordinator;
    volatile boolean stop = false;
    /** Last number this worker was assigned (for pause-state logging). */
    private volatile int lastNumber = 0;

    public CounterThread(InterfaceThread coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Get the last number this worker was assigned (for pause state).
     */
    public int getCounter() {
        return lastNumber;
    }

    /**
     * stops thread <b>custom implementation</b>
     */
    public void stopThread() {
        stop = true;
    }

    /**
     * starts thread <b>custom implementation</b>
     */
    public void startThread() {
        stop = false;
    }


    /**
     * answers if number is prime <b>wait for thread to stop!</b>
     * @return if number is prime
     */

    @Override
    public void run() {
        while (true) {
            while (stop) {
                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
            int n = coordinator.getNextNumber();
            if (n < 0) break;
            lastNumber = n;
            if (n > 1 && Main.isPrime(n)) {
                coordinator.addPrime();
            }
        }
    }
}
