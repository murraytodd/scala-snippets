package example

import scala.annotation.tailrec
import scala.collection.JavaConverters

/**
  * Example of writing an efficient Prime-number generator in Scala. The two primary goals
  * here were first, to write this using the functional paradigm, avoiding for-loops; and
  * second, to write something that was high-performance and could calculate large numbers
  * of primes.
  */
object Prime {

  /**
    * Given a sequence of Prime numbers, find the next number.
    * @param primes A sequence of the first n Prime numbers. Can be empty.
    * @return The next prime number to follow the sequence.
    */
  def nextPrime(primes: Seq[Int]) = {
    val last = if (primes.isEmpty) 0 else if (primes.last == 2) 1 else primes.last
    // toSteam makes sure that each of these is lazily evaluated
    (last + 2 to Int.MaxValue by 2).toStream.filter(i =>
      primes // start with the seed of primes
        .takeWhile(p => ((p * p) <= i)) // only test primes up to the sqrt of the test value
        .forall(p => i % p != 0)) // make sure all those primes don't divide evenly
      .head // works with the "stream" so computation exists when the first is encountered
  }

  /**
    * Calculate the first N primes.
    * @param count The number of primes to be calculated
    * @return A sequence of the first N primes.
    */
  def getPrimes(count: Int) : Seq[Int] = {
    require(count >= 0, "Count must be a non-negative integer.")
    if (count == 0) {
      Seq[Int]()
    } else {
      @tailrec
      def buildUntil(primes: Vector[Int]) : Vector[Int] = {
        if (primes.size==count)
          primes
        else
          buildUntil(primes :+ nextPrime(primes))
      }
      buildUntil(Vector.empty)
    }
  }

  /**
    * Given a sequence of Prime numbers, find the next number. (alternative)
    *
    * An alternative approach to nextPrime, where we don't use a Stream but try some
    * other way iterating through all candidates (from the last prime + 2 until infinity)
    * to look for the next number that is actually prime.
    * @param primes A sequence of the first n Prime numbers. Can be empty.
    * @return The next prime number to follow the sequence.
    */
  def nextPrimeRecursive(primes: Seq[Int]) = {
    @tailrec
    def tryPrimeCandidate(candidate: Int, primes: Seq[Int]) : Int = {
      if (primes.takeWhile(p => ((p * p) <= candidate)).forall(p => candidate % p != 0))
        candidate
      else
        tryPrimeCandidate(candidate + 2, primes)
    }

    val last = if (primes.isEmpty) 0 else if (primes.last == 2) 1 else primes.last
    tryPrimeCandidate(last + 2, primes)
  }

  /**
    * Generalized function to calculate N prime numbers.
    * @param count Number of prime numbers to calculate
    * @param nextFunc Any function that, given a sequence of primes, finds the next candidate.
    * @return A sequence of the first N primes.
    */
  def getPrimesGeneralized(count: Int, nextFunc: Seq[Int] => Int = nextPrime) = {
    require(count >= 0, "Count must be a non-negative integer.")
    if (count == 0) {
      Seq[Int]()
    } else {
      @tailrec
      def buildUntil(primes: Vector[Int]) : Vector[Int] = {
        if (primes.size==count)
          primes
        else
          buildUntil(primes :+ nextFunc(primes))
      }
      buildUntil(Vector.empty)
    }
  }

  /**
    * A published example of an approach that proves much shorter, mentioned in Alvin Alexander's blog here:
    * https://alvinalexander.com/text/prime-number-algorithm-scala-scala-stream-class
    * It is shorter and more concise, but also very inefficient!
    */
  private def primeAsStream(s: Stream[Int]): Stream[Int] =
    Stream.cons(s.head, primeAsStream(s.tail filter { _ % s.head != 0 }))

  /**
    * This stream will generate prime numbers on the fly as they are needed, caching the results
    *
    * This solution would be very efficient if you had a situation where you might be asking for the
    * first N primes quite often, and you never wanted to recompute primes once they had been figured out.
    * Unfortunately, this turns out to be very problematic if you just want to calculate a single long
    * sequence, as it causes strange garbage collection death.
    *
    */
  val primes = primeAsStream(Stream.from(2))


}

object Profile {

  val values = Seq(2, 4, 3, 1, 5, 8)

  // looping example (not the need of the "var"
  var x = 0.0
  for (v <- values) { x += (v * v) }

  // using collections

  def factorial2(x: Int): Int = {
    if (x > 1)
      x * factorial2(x - 1)
    else
      1
  }

  def factorial(n: Int): Int = {
    @tailrec
    def factorialRec(accumulator: Int, n: Int): Int = {
      if (n == 1)
        accumulator
      else
        factorialRec(accumulator * n, n - 1)
    }
    factorialRec(1, n)
  }

  def main(args: Array[String]): Unit = {

    val count = 1000000

    //(1 to 10).foreach(i => { Thread.sleep(1000); println(i)})

    ptime(Prime.getPrimes(count),"Classic with Stream")

    ptime(Prime.getPrimesGeneralized(count, Prime.nextPrimeRecursive),"Classic with TailRec")

    ptime(JavaPrime.getPrimes(count), "Pure Java")

    ptime(JavaPrime.getPrimesArray(count),"Pure Java with Primivite Array")

    def wrappedJavaNextPrime(s: Seq[Int]) : Int = JavaPrime.nextPrime(JavaConverters.seqAsJavaList(s).asInstanceOf[java.util.List[Integer]])

    ptime(Prime.getPrimesGeneralized(count,wrappedJavaNextPrime),"Wrapped Java nextPrime")

    ptime(PrimitivePrime.getPrimes(count),"Scala primitive attempt pure")

    // ptime(PrimitivePrime.getPrimes(count, PrimitivePrime.nextPrimeFunctional),"Scala primitive attempt functional testing")

    // ptime(PrimitivePrime.getPrimes(count, PrimitivePrime.nextPrimeFunctional2),"Scala primitive attempt mostly functional")
  }

}
