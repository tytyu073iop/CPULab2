package org.example;

public class CounterThread extends Thread{
    int num = 0;
    boolean isPrimeVar = false;

    public CounterThread(int n) {
        this.num = n;
    }

    boolean getIsPrime() {
        return isPrimeVar;
    }

    @Override
    public void run() {
        isPrimeVar = Main.isPrime(num);
    }
}
