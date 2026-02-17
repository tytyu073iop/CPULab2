package org.example;


public class Main {
    static void main() {
        for (int i = 1_000_000; i <= 1_00_000_000; i *= 10) {
            // single Thread
            long start = System.currentTimeMillis();
            int count = 0;
            for (int j = 0; j <= i; j++) {
                if (isPrime(j)) {
                    count++;
                }
            }
            long end = System.currentTimeMillis();
            System.out.printf("there was: %d primes; ", count);
            System.out.printf("Single threaded was computing for: %.3f seconds\n", (end - start) / 1000.0);


            //multithreaded

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
