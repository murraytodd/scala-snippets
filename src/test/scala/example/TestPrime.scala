package example

import org.scalatest._

class TestPrime extends FunSuite {

  test("nextPrime gets the next expected value") {
    assertResult(11)(Prime.nextPrime(Seq(2,3,5,7)))
  }

  test("nextPrime can handle an empty set") {
    assertResult(2)(Prime.nextPrime(Seq()))
  }

  test("Primes generates primes") {
    assertResult(Seq(2,3,5,7,11))(Prime.getPrimes(5))
    assertResult(Seq(2))(Prime.getPrimes(1))
  }

  test("Requesting zero primes works as expected.") {
    assert(Prime.getPrimes(0).isEmpty)
  }

  test("Trivial argument checking: requested number is non-negative.") {
    assertThrows[IllegalArgumentException] { Prime.getPrimes(-1) }
  }

  test("Other stream-based prime generator makes primes.") {
    assertResult(Seq(2,3,5,7,11))(Prime.primes.take(5))
  }
}
