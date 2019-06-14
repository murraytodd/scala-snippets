package example

object TowerOfHanoi {

  val towers = Set("Left", "Middle", "Right")

  def towerOfHanoi(n: Int,
                   from: String = "Left",
                   to: String = "Right"): Seq[(Int, String, String)] = {

    require(towers.contains(from), from + " is not one of the towers: " + towers.mkString(" "))
    require(towers.contains(to), to + " is not one of the towers: " + towers.mkString(" "))

    if (n == 0) {
      Nil
    } else {
      val auxiliaryTower = (towers -- Set(from, to)).head

      towerOfHanoi(n - 1, from, auxiliaryTower) ++
        Seq((n, from, to)) ++
        towerOfHanoi(n - 1, auxiliaryTower, to)
    }
  }

  def main (args: Array[String]) {
    println(towerOfHanoi(2))
  }

}
