package object example {

  /**
    * Prints time taken to execute inner function. Borrowed from
    * http://www.scala-lang.org/old/node/8022
    * @param f Any function that will be run
    * @return The results from execution of function f
    */
  def ptime[A](f: => A) = {
    val t0 = System.nanoTime
    val ans = f
    printf("Elapsed: %.3f sec\n", (System.nanoTime - t0) * 1e-9)
    ans
  }

}
