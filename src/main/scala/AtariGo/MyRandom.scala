package AtariGo

case class MyRandom(seed: Long) {
  def randomInt: (Int, MyRandom) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL //origina uma nova semente
    val nextRandom = MyRandom(newSeed) //cria uma instância MyRandom com a semente previamente criada
    val n = (newSeed >>> 16).toInt //extrai os 32 bits superiores de newSeed
    (n, nextRandom) //retorna uma tupla com o número gerado e a nova semente.
  }

  def nextInt(n: Int): (Int, MyRandom) = { //calcula um número aleatório de [0-n]
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL //origina uma nova semente
    val nextRandom = MyRandom(newSeed) //cria uma instância MyRandom com a semente previamente criada
    val nn = ((newSeed >>> 16).toInt) % n
    (if (nn < 0) -nn else nn, nextRandom) //nn for negativo, converte para positivo com -nn
  }
  }
  //Cada nextRandom carrega a seed atualizada para a próxima operação, garantindo que a sequência seja sempre reproduzível!
  //Deste modo a sequência não será sempre a mesma
