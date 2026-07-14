package atarigo

enum Stone(val symbol: String):
  case Black extends Stone("●")
  case White extends Stone("○")
  case Empty extends Stone("·")

  def opponent: Stone = this match
    case Black => White
    case White => Black
    case Empty => Empty
