package example;

import java.util.*;

/**
 * Java implementations of the prime number generator, one using lists and the other using arrays. These were
 * written both to compare the implementations with the Scala version and to allow some performance testing.
 */
public class JavaPrime {

    public static int nextPrime(List<Integer> primes) {
        int candidate = (primes.isEmpty() ? 0 : (
                primes.size()==1 ? 1 : primes.get(primes.size()-1)
        ));
        candidatesLoop:
        while(true) {
            candidate += 2;
            for (int p : primes) {
                if ((p * p) > candidate) break;
                if (candidate % p == 0) continue candidatesLoop;
            }
            return candidate;
        }
    }

    public static List<Integer> getPrimes(int count) throws IllegalArgumentException {
        if (count < 0) throw new IllegalArgumentException("Count must be a non-negative integer.");
        List<Integer> primes = new ArrayList<Integer>(count);
        while (primes.size()<count) {
            primes.add(nextPrime(primes));
        }
        return primes;
    }

    public static int nextPrime(int[] primes, int count) {
        int candidate = (count == 0) ? 0 : ((count == 1) ? 1 : primes[count - 1]);
        candidatesLoop:
        while(true) {
            candidate += 2;
            for (int pIdx = 0; pIdx < count; pIdx++) {
                int p = primes[pIdx];
                if ((p * p) > candidate) break;
                if (candidate % p == 0) continue candidatesLoop;
            }
            return candidate;
        }
    }

    public static int[] getPrimesArray(int count) throws IllegalArgumentException {
        if (count < 0) throw new IllegalArgumentException("Count must be a non-negative integer.");
        int[] primes = new int[count];
        for (int i = 0; i < count; i++) primes[i] = nextPrime(primes, i);
        return primes;
    }

    public static int[] makeArray(int count) { return new int[count]; }
}