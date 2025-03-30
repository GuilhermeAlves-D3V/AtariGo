package AtariGo

import scala.io.StdIn.readLine

object AtariGoUtils {
  def showPrompt(): Coord2D = {
    print("\n Onde deseja inserir a peça ? \n Coordenada do X:")
    val x = getUserInput()
    print("\n Coordenada do Y:")
    val y = getUserInput()
    (x,y)
  }

  def getUserInput(): Int = readLine.trim.toInt
}
