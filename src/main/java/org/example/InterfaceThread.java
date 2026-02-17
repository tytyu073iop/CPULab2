package org.example;

public class InterfaceThread extends Thread{
    int current_num = 0;
    int max;
    int count = 0;
    int amountOfThreads;
    boolean stop = false;
    CounterThread[] workers = new CounterThread[amountOfThreads];

    public int getCount() {
        return count;
    }

    public void logPauseState() {
        System.out.println("Counting stoped on: " + getCount());
        for (int i = 0; i < amountOfThreads; i++) {
            System.out.printf("Thread %d is stoped on: %d\n", i + 1, workers[i].getCounter());
        }
    }

    InterfaceThread(int m, int a) {
        this.max = m;
        this.amountOfThreads = a;
    }

    public void stopThread() {
        stop = true;
        logPauseState();
    }

    public int getCurrent_num() {return  current_num; }

    public void startThread() {
        stop = false;
    }

    public void run() {
        while(current_num <= max) {
            for (int i = 0; i < amountOfThreads; i++) {
                workers[i] = new CounterThread(current_num++);
                workers[i].start();
            }
            while (true) {
                if (!stop) {
                    for (int i = 0; i < amountOfThreads; i++) {
                        if(!workers[i].isAlive()) {
                          if (workers[i].getIsPrime()) {
                              count++;
                          }
                          workers[i] = new CounterThread(current_num++);
                        }
                    }
                }
            }
        }
    }
}
