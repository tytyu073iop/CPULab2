package org.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Coordinates a fixed pool of CounterThread workers. Workers pull work via
 * getNextNumber(); no new threads are created per number.
 */
public class InterfaceThread extends Thread {
    private final AtomicInteger current_num = new AtomicInteger(2);  // 0 and 1 are not primes
    private final int max;
    private final AtomicInteger count = new AtomicInteger(0);
    private final int amountOfThreads;
    volatile boolean stop = false;
    private CounterThread[] workers;

    /**
     * Get total number of primes found so far.
     */
    public int getCount() {
        return count.get();
    }

    /**
     * Called by worker threads to get the next number to check. Returns -1 when no more work.
     */
    int getNextNumber() {
        while (stop) {
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        int n = current_num.getAndIncrement();
        return n <= max ? n : -1;
    }

    /**
     * Called by a worker when it finds a prime.
     */
    void addPrime() {
        count.incrementAndGet();
    }

    /**
     * prints state of application before pause
     */
    public void logPauseState() {
        System.out.println("Counting stopped on: " + getCurrent_num());
        for (int i = 0; i < amountOfThreads; i++) {
            System.out.printf("Thread %d is stopped on: %d\n", i + 1, workers[i].getCounter());
        }
    }

    /**
     * constructs thread
     * @param m to which number count primes
     * @param a amount of threads
     */
    InterfaceThread(int m, int a) {
        this.max = m;
        this.amountOfThreads = a;
        workers  = new CounterThread[amountOfThreads];
    }

    /**
     * stops thread <b>custom implementation</b>
     */
    public void stopThread() {
        stop = true;
        // Wait for worker threads to process the stop signal
        for (int i = 0; i < amountOfThreads; i++) {
            workers[i].stopThread();
        }
        // Give threads a moment to exit their loops and update counter
        try {
            Thread.sleep(50); // Allow threads to exit loops and update counter
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logPauseState();
    }

    /**
     * Get the next number that would be assigned (for pause state).
     */
    public int getCurrent_num() { return current_num.get(); }

    /**
     * starts thread <b>custom implementation</b>
     */
    public void startThread() {
        for (int i = 0; i < amountOfThreads; i++) {
            workers[i].startThread();
        }
        stop = false;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < amountOfThreads; i++) {
            workers[i] = new CounterThread(this);
            workers[i].start();
        }
        for (; current_num.get() <= max;) {
            for (int i = 0; i < amountOfThreads; i++) {
                try {
                    workers[i].join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.printf("With pauses all process took: %.3f\n", (end - start) / 1000.0);
    }
}
