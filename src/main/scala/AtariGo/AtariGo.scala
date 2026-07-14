package atarigo

type Board = Vector[Vector[Stone]]
type Coord = (Int, Int)

enum MoveResult:
  case Invalid(reason: String)
  case Continue(game: AtariGo)
  case Won(game: AtariGo, winner: Stone)

/** Immutable state and rules for Atari Go (the first capture wins). */
final case class AtariGo private (board: Board, currentPlayer: Stone):
  val size: Int = board.size

  def emptyPlaces: Vector[Coord] =
    for
      row <- board.indices.toVector
      column <- board(row).indices
      if board(row)(column) == Stone.Empty
    yield (row, column)

  def stoneAt(coord: Coord): Option[Stone] =
    Option.when(AtariGo.isInside(coord, size))(board(coord._1)(coord._2))

  def libertiesAt(coord: Coord): Set[Coord] = stoneAt(coord) match
    case Some(Stone.Black | Stone.White) => AtariGo.liberties(board, coord)
    case _                               => Set.empty

  def play(coord: Coord): MoveResult =
    if !AtariGo.isInside(coord, size) then MoveResult.Invalid("Coordinate outside the board.")
    else if board(coord._1)(coord._2) != Stone.Empty then MoveResult.Invalid("That position is occupied.")
    else
      val nextBoard = board.updated(coord._1, board(coord._1).updated(coord._2, currentPlayer))
      val captured = AtariGo.neighbours(coord, size)
        .filter(c => nextBoard(c._1)(c._2) == currentPlayer.opponent)
        .exists(c => AtariGo.liberties(nextBoard, c).isEmpty)

      val nextGame = AtariGo(nextBoard, currentPlayer.opponent)
      if captured then MoveResult.Won(nextGame, currentPlayer)
      else if AtariGo.liberties(nextBoard, coord).isEmpty then MoveResult.Invalid("Suicide moves are not allowed.")
      else MoveResult.Continue(nextGame)

  def randomMove(random: MyRandom): Option[(Coord, MyRandom)] =
    val legalMoves = emptyPlaces.filter { coord =>
      play(coord) match
        case MoveResult.Invalid(_) => false
        case _                     => true
    }
    Option.when(legalMoves.nonEmpty) {
      val (index, nextRandom) = random.nextInt(legalMoves.size)
      (legalMoves(index), nextRandom)
    }

  def render: String =
    val width = (size - 1).toString.length
    val header = " " * (width + 1) + board.indices.map(i => f"$i%2d").mkString
    val rows = board.zipWithIndex.map { case (row, index) =>
      index.toString.reverse.padTo(width, ' ').reverse + " " +
        row.map(stone => s" ${stone.symbol}").mkString
    }
    (header +: rows).mkString("\n")

object AtariGo:
  def apply(size: Int = 9, firstPlayer: Stone = Stone.Black): AtariGo =
    require(size >= 2, "The board must be at least 2x2")
    require(firstPlayer != Stone.Empty, "The first player must be black or white")
    new AtariGo(Vector.fill(size, size)(Stone.Empty), firstPlayer)

  private def isInside(coord: Coord, size: Int): Boolean =
    coord._1 >= 0 && coord._1 < size && coord._2 >= 0 && coord._2 < size

  private def neighbours(coord: Coord, size: Int): Vector[Coord] =
    val (row, column) = coord
    Vector((row - 1, column), (row + 1, column), (row, column - 1), (row, column + 1))
      .filter(isInside(_, size))

  private def group(board: Board, start: Coord): Set[Coord] =
    val colour = board(start._1)(start._2)
    @annotation.tailrec
    def visit(pending: List[Coord], seen: Set[Coord]): Set[Coord] = pending match
      case Nil => seen
      case head :: tail =>
        val connected = neighbours(head, board.size)
          .filter(c => board(c._1)(c._2) == colour)
          .filterNot(seen)
        visit(connected.toList ::: tail, seen ++ connected)
    visit(List(start), Set(start))

  private def liberties(board: Board, start: Coord): Set[Coord] =
    group(board, start)
      .flatMap(neighbours(_, board.size))
      .filter(c => board(c._1)(c._2) == Stone.Empty)
