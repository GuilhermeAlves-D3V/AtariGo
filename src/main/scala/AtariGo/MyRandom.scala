package atarigo

/** A small immutable pseudo-random number generator. */
final case class MyRandom(seed: Long):
  private def next: (Int, MyRandom) =
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    ((newSeed >>> 16).toInt, MyRandom(newSeed))

  def nextInt(bound: Int): (Int, MyRandom) =
    require(bound > 0, "The bound must be positive")
    val (value, nextRandom) = next
    (Math.floorMod(value, bound), nextRandom)
