package AtariGo

import Stone.Stone
import AtariGoUtils.{showPrompt, getUserInput}

type Board = List[List[Stone]]
type Coord2D = (Int, Int)

case class AtariGo(board: Board) { //implementar os métodos para adicionar/meter peças + obter as coordenadas vazias
  def emptyPlaces: List[Coord2D] = AtariGo.getListOfEmptyPlaces(board)
}
/// bom dia
object AtariGo{
  def createBoard(size: Int): Board =
    List.fill(size)(List.fill(size)(Stone.Empty))

  def getListOfEmptyPlaces(lista: Board): List[Coord2D] = {
    def aux_row(rows: List[List[Stone]], rowIndex: Int, acc: List[Coord2D]): List[Coord2D] = rows match {
      case Nil => acc
      case head :: tail =>
        val newCoords = aux_columns(head, rowIndex, 0, Nil)
        aux_row(tail, rowIndex + 1, acc ++ newCoords) // ++ para concatenação das listas
    }

    def aux_columns(columns: List[Stone], rowIdx: Int, colIdx: Int, acc: List[Coord2D]): List[Coord2D] = columns match {
      case Nil => acc
      case Stone.Empty :: tail =>
        aux_columns(tail, rowIdx, colIdx + 1, (rowIdx, colIdx) :: acc)
      case _ :: tail =>
        aux_columns(tail, rowIdx, colIdx + 1, acc)
    }
    aux_row(lista, 0, Nil)
  }

    def liberty(coord:Coord2D,board: Board):Int = {
      val neighbors = List((coord._1+1, coord._2), (coord._1-1, coord._2), (coord._1, coord._2+1), (coord._1, coord._1-1))
      def isValid(c: Coord2D): Boolean = {
        c._1 >= 0 && c._1 <= 8 && c._2 >= 0 && c._2 <= 8
      }
      neighbors.foldLeft(0) { (count, neighbor) =>
        if (isValid(neighbor)) {
          board(neighbor._1)(neighbor._2) match {
            case Stone.Empty => count + 1
            case _           => count
          }
        } else {
          count
        }
      }
    }

  def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom): (Coord2D, MyRandom) = lstOpenCoords match {
    case Nil =>
      throw new IllegalArgumentException("Não há jogadas possíveis.")

    case _ =>
      val (randomIndex, nextRand) = rand.nextInt(lstOpenCoords.length)
      val selectedCoord = lstOpenCoords(randomIndex)
      (selectedCoord, nextRand)
  }

  //  private def putPiece(board: Board, coord: Coord2D, stone: Stone): Board = {
//    board.updated(index, alteração a fazer no tabuleiro)
//  }
}

object AtariGoApp extends App {
  val board = AtariGo.createBoard(9)
  val game = AtariGo(board)

  println("Tabuleiro inicial:")
  println(board)

  println("\nPosições vazias:")
  println(game.emptyPlaces)

  val r = MyRandom(System.currentTimeMillis())
  println("\nPosição aleatória + random seed:")
  println(AtariGo.randomMove(game.emptyPlaces,r))

  val coord = (8, 8)
  println(s"\nLiberdade da $coord: ${AtariGo.liberty(coord, board)}")
  println(board(2)(4))
}

