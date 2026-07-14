package atarigo

class AtariGoSuite extends munit.FunSuite:
  test("a new board contains only empty positions") {
    val game = AtariGo(5)
    assertEquals(game.emptyPlaces.size, 25)
    assertEquals(game.currentPlayer, Stone.Black)
  }

  test("playing alternates players and occupies the coordinate") {
    AtariGo(3).play((1, 1)) match
      case MoveResult.Continue(game) =>
        assertEquals(game.stoneAt((1, 1)), Some(Stone.Black))
        assertEquals(game.currentPlayer, Stone.White)
        assertEquals(game.libertiesAt((1, 1)).size, 4)
      case result => fail(s"Expected a valid move, got $result")
  }

  test("occupied and out-of-board coordinates are rejected") {
    val game = AtariGo(3)
    assert(game.play((-1, 0)).isInstanceOf[MoveResult.Invalid])
    val next = game.play((0, 0)).asInstanceOf[MoveResult.Continue].game
    assert(next.play((0, 0)).isInstanceOf[MoveResult.Invalid])
  }

  test("surrounding a stone wins the game") {
    val moves = List((2, 2), (1, 2), (0, 0), (2, 1), (0, 4), (3, 2), (4, 0), (2, 3))
    val result = moves.foldLeft[MoveResult](MoveResult.Continue(AtariGo(5))) {
      case (MoveResult.Continue(game), move) => game.play(move)
      case (finished, _)                     => finished
    }
    result match
      case MoveResult.Won(_, Stone.White) => assert(true)
      case other                           => fail(s"Expected White to capture, got $other")
  }

  test("coordinate parser reports malformed input") {
    assertEquals(AtariGoUtils.parseCoordinate("2 3"), Right((2, 3)))
    assert(AtariGoUtils.parseCoordinate("two 3").isLeft)
    assert(AtariGoUtils.parseCoordinate("2").isLeft)
  }

  test("random generator stays within the requested bound") {
    val values = Iterator.iterate((0, MyRandom(42L))) { case (_, random) => random.nextInt(7) }
      .drop(1).take(100).map(_._1).toList
    assert(values.forall(value => value >= 0 && value < 7))
  }
