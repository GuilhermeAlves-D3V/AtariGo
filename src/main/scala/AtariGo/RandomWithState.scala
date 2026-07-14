package atarigo

trait RandomWithState[R]:
  def nextInt(bound: Int): (Int, R)
