package org.example;

/**
 * Worker thread that repeatedly takes the next number from the coordinator,
 * checks primality, and reports the result. Pause is implemented via Thread.interrupt().
 */
public class CounterThread extends Thread {
    private final InterfaceThread coordinator;
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

    @Override
    public void run() {
        while (true) {
            try {
                if (Thread.interrupted()) {
                    coordinator.enterPauseState();
                    continue;
                }
                int n = coordinator.getNextNumber();
                if (n < 0) break;
                lastNumber = n;
                if (n > 1 && Main.isPrime(n)) {
                    coordinator.addPrime();
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                coordinator.enterPauseState();
            }
        }
    }
}
