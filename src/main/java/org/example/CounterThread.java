package org.example;

public class CounterThread extends Thread{
    int num = 0;
    boolean isPrimeVar = false;
    boolean stop = false;
    int counter = 0;

    public CounterThread(int n) {
        this.num = n;
    }

    public int getCounter() {
        return counter;
    }

    public void stopThread() {
        stop = true;
    }

    public void startThread() {
        stop = false;
        start();
    }



    public boolean getIsPrime() {
        return isPrimeVar;
    }

    @Override
    public void run() {
        assert num > 1;
        int top = (int)Math.sqrt(num);
        for (; counter <= top && !stop; counter++) {
            if (num % counter == 0) {
                isPrimeVar = false;
                return;
            }
        }
        if (!stop) {
            isPrimeVar = true;
        }
    }
}
