# Atari Go

Uma implementação curricular de **Atari Go** em Scala 3. Nesta variante simplificada de Go, vence o primeiro jogador que capturar um grupo adversário.

## Destaques

- estado de jogo imutável e funções puras para as regras;
- deteção de grupos, liberdades, capturas e jogadas suicidas;
- adversário com gerador pseudoaleatório reproduzível;
- interface de terminal e tabuleiro configurável;
- testes automatizados com MUnit.

## Executar

Requer Java 17+ e [sbt](https://www.scala-sbt.org/).

```bash
sbt run
```

O tamanho predefinido é 9×9. Para escolher outro tamanho (entre 2 e 19):

```bash
sbt "run 5"
```

O jogador usa as pedras pretas (`●`) e começa. Introduz cada jogada no formato `linha coluna`, por exemplo `2 3`. Escreve `q` para sair.

## Testes

```bash
sbt test
```

## Conceitos demonstrados

O projeto aplica modelação de domínio com `enum` e `case class`, pattern matching, recursão terminal, coleções imutáveis, tipos algébricos para resultados e propagação explícita de estado.

## Contexto académico

Projeto desenvolvido no âmbito de Programação Orientada a Objetos / Programação Multiparadigma no ISCTE.
