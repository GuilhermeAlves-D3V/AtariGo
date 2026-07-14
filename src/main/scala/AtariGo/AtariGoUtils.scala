package atarigo

object AtariGoUtils:
  def parseCoordinate(input: String): Either[String, Coord] =
    input.trim.split("\\s+").toList match
      case row :: column :: Nil =>
        for
          r <- row.toIntOption.toRight("Use two whole numbers, for example: 2 3")
          c <- column.toIntOption.toRight("Use two whole numbers, for example: 2 3")
        yield (r, c)
      case _ => Left("Use two coordinates separated by a space, for example: 2 3")
