package org.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Coordinates a fixed pool of CounterThread workers. Workers pull work via
 * getNextNumber(). Pause/resume uses Thread.interrupt() and a shared lock.
 */
public class InterfaceThread extends Thread {
    private final AtomicInteger current_num = new AtomicInteger(2);  // 0 and 1 are not primes
    private final int max;
    private final AtomicInteger count = new AtomicInteger(0);
    private final int amountOfThreads;
    private CounterThread[] workers;

    private final Object pauseLock = new Object();
    private volatile boolean paused = false;

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
     * Called by a worker when it receives an interrupt (pause request). Blocks until resumed.
     */
    void enterPauseState() {
        synchronized (pauseLock) {
            while (paused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    /**
     * Prints state of application before pause.
     */
    public void logPauseState() {
        System.out.println("Counting stopped on: " + getCurrent_num());
        for (int i = 0; i < amountOfThreads; i++) {
            System.out.printf("Thread %d is stopped on: %d\n", i + 1, workers[i].getCounter());
        }
    }

    /**
     * Constructs coordinator thread.
     * @param m upper bound to count primes to
     * @param a number of worker threads
     */
    InterfaceThread(int m, int a) {
        this.max = m;
        this.amountOfThreads = a;
        workers = new CounterThread[amountOfThreads];
    }

    /**
     * Pauses all worker threads using Thread.interrupt().
     */
    public void stopThread() {
        synchronized (pauseLock) {
            paused = true;
        }
        for (int i = 0; i < amountOfThreads; i++) {
            workers[i].interrupt();
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logPauseState();
    }

    /**
     * Get the next number that would be assigned (for pause state).
     */
    public int getCurrent_num() {
        return current_num.get();
    }

    /**
     * Resumes all worker threads.
     */
    public void startThread() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < amountOfThreads; i++) {
            workers[i] = new CounterThread(this);
            workers[i].start();
        }
        for (int i = 0; i < amountOfThreads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long end = System.currentTimeMillis();
        System.out.printf("With pauses all process took: %.3f\n", (end - start) / 1000.0);
    }
}
