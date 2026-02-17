package org.example;
import java.util.Scanner;

import static java.lang.Thread.sleep;


public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1_000_000; i <= 1_00_000_000; i *= 10) {
            // single Thread
            System.out.println("Starting single threaded job");
            long start = System.currentTimeMillis();
            int count = 0;
            for (int j = 2; j <= i; j++) {
                if (isPrime(j)) {
                    count++;
                }
            }
            long end = System.currentTimeMillis();
            System.out.printf("there was: %d primes; ", count);
            System.out.printf("Single threaded was computing for: %.3f seconds\n", (end - start) / 1000.0);


            //multithreaded
            for (int j = 2; j <= 4; j++) {
                InterfaceThread it = new InterfaceThread(i, j);
                it.start();
                try {
                    it.join(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                while (it.isAlive()) {
                    // pausing
                    System.out.println("enter line to pause (pause and start for new job if thread is stopped");
                    scanner.nextLine();
                    it.stopThread();
                    System.out.println("enter line to start");
                    scanner.nextLine();
                    it.startThread();
                    System.out.println("resumed");
                }
                System.out.printf("amount of primes is: %d\n", it.getCount());
            }
        }
    }


    /**
     * Test whether x is a prime number.
     * x is assumed to be greater than 1.
     */
    public static boolean isPrime(int x) {
        assert x > 1;
        int top = (int)Math.sqrt(x);
        for (int i = 2; i <= top; i++)
            if ( x % i == 0 )
                return false;
        return true;
    }
}
