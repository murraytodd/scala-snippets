package example

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
    * @return
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
    * A published example of an approach that proves much shorter, mentioned in Alvin Alexander's blog here:
    * https://alvinalexander.com/text/prime-number-algorithm-scala-scala-stream-class
    * It is shorter and more concise, but also very inefficient!
    * @param s
    * @return
    */
  private def primeAsStream(s: Stream[Int]): Stream[Int] =
    Stream.cons(s.head, primeAsStream(s.tail filter { _ % s.head != 0 }))

  val primes = primeAsStream(Stream.from(2))
}
