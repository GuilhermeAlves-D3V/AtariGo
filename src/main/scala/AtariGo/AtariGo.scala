package AtariGo

import Stone.Stone
import AtariGoUtils.{showPrompt, getUserInput}

type Board = List[List[Stone]]
type Coord2D = (Int, Int)

case class AtariGo(board: Board) //implementar os métodos para adicionar/meter peças + obter as coordenadas vazias

object AtariGo extends App {
  //Game(createBoard(9))
  println(getListOfEmptyPlaces(createBoard(9)))

  def createRow(size: Int): List[Stone] = {
    if (size == 0) Nil
    else Stone.Empty :: createRow(size - 1)
  }

  def createBoard(size: Int): Board = {
    if (size == 0) Nil
    else createRow(9) :: createBoard(size - 1)
  }

  def getListOfEmptyPlaces(lista: List[List[Stone]]): List[Coord2D] = {
    def aux_row(rows: List[List[Stone]], rowIndex: Int, acc: List[Coord2D]): List[Coord2D] = rows match {
      case Nil => acc
      case head :: tail =>
        val newCoords = aux_columns(head, rowIndex, 0, Nil)
        aux_row(tail, rowIndex + 1, acc ++ newCoords)
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

//  private def putPiece(board: Board, coord: Coord2D, stone: Stone): Board = {
//    board.updated(index, alteração a fazer no tabuleiro)
//  }

}

