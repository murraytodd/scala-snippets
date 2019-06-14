package example

object PrimitivePrime {

  def nextPrime(primes: Array[Int], count : Int) : Int = {
    import util.control.Breaks.{breakable,break}
    val last = if (count==0) 0 else if (count==1) 1 else primes(count - 1)
    for (i <- last + 2 to Int.MaxValue by 2) {
      breakable {
        for (pIdx <- 0 until count) {
          val p = primes(pIdx)
          if ((p * p) <= i) return i
          if (i % p == 0) break
        }
        return i
      }
    }
    return 0 // should never reach this line
  }

  def nextPrimeFunctional(primes: Array[Int], count: Int) : Int = {
    val last = if (count==0) 0 else if (count==1) 1 else primes(count - 1)
    for (i <- last + 2 to Int.MaxValue by 2) {
      if (primes.take(count).takeWhile(p => ((p * p) <= i)).forall(p => i % p != 0)) return i
    }
    return 0
  }

  def nextPrimeFunctional2(primes: Array[Int], count: Int) : Int = {
    val last = if (count==0) 0 else if (count==1) 1 else primes(count - 1)
    (last + 2 to Int.MaxValue by 2).toStream.filter(i =>
      primes
        .take(count).takeWhile(p => ((p * p) <= i))
        .forall(p => i % p != 0)
    ).head
  }

  def getPrimes(count: Int, f: (Array[Int], Int) => Int = nextPrime) = {
    val primes = JavaPrime.makeArray(count)
    for (i <- 0 until count) primes(i) = f(primes, i)
    primes
  }
}
