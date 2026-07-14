import atarigo.*
import scala.io.StdIn.readLine

object Main:
  def main(args: Array[String]): Unit =
    val size = args.headOption.flatMap(_.toIntOption).filter(n => n >= 2 && n <= 19).getOrElse(9)
    println("Atari Go — capture the first opposing group to win")
    println("You are Black (●). Enter moves as: row column. Type q to quit.\n")
    gameLoop(AtariGo(size), MyRandom(System.nanoTime()))

  @annotation.tailrec
  private def gameLoop(game: AtariGo, random: MyRandom): Unit =
    println(game.render)
    print("\nYour move: ")
    Option(readLine()).map(_.trim) match
      case None | Some("q" | "quit" | "exit") => println("Game finished.")
      case Some(input) =>
        AtariGoUtils.parseCoordinate(input) match
          case Left(error) =>
            println(s"$error\n")
            gameLoop(game, random)
          case Right(coord) => game.play(coord) match
            case MoveResult.Invalid(reason) =>
              println(s"Invalid move: $reason\n")
              gameLoop(game, random)
            case MoveResult.Won(finalGame, _) => showWinner(finalGame, "You win!")
            case MoveResult.Continue(afterHuman) =>
              afterHuman.randomMove(random) match
                case None => showWinner(afterHuman, "Draw: there are no legal moves.")
                case Some((computerMove, nextRandom)) =>
                  println(s"Computer plays ${computerMove._1} ${computerMove._2}.\n")
                  afterHuman.play(computerMove) match
                    case MoveResult.Won(finalGame, _) => showWinner(finalGame, "The computer wins.")
                    case MoveResult.Continue(nextGame) => gameLoop(nextGame, nextRandom)
                    case MoveResult.Invalid(_) => throw new IllegalStateException("The computer selected an invalid move")

  private def showWinner(game: AtariGo, message: String): Unit =
    println(game.render)
    println(s"\n$message")
