import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException

fun main() {

    rockPaperScissors(listOf("A Y", "B X", "C Z"), false)
    rockPaperScissors(readInput("Day02"), false)
    rockPaperScissors(listOf("A Y", "B X", "C Z"), true)
    rockPaperScissors(readInput("Day02"), true)

}

fun rockPaperScissors(input: List<String>, secondPartStrategy: Boolean) {

    val score = input.sumOf {
        val signs = it.split(" ")
        val myHand =
            if (secondPartStrategy) {
                Hand.createHandFor(signs[0], ExpectedResult.create(signs[1]))
            } else {
                Hand.fromString(signs[1])
            }
        Game(Hand.fromString(signs[0]), myHand).play()
    }

    println(score)
}


sealed class Hand {
    object Rock : Hand()
    object Paper : Hand()
    object Scissor : Hand()

    companion object {
        fun fromString(string: String): Hand {
            return when (string) {
                "A" -> Rock
                "B" -> Paper
                "C" -> Scissor
                "X" -> Rock
                "Y" -> Paper
                "Z" -> Scissor
                else -> throw IllegalArgumentException()
            }
        }

        fun createHandFor(opponent: String, expectedResult: ExpectedResult): Hand {
            return when (fromString(opponent)) {
                Paper -> {
                    when (expectedResult) {
                        ExpectedResult.WIN -> Scissor
                        ExpectedResult.DRAW -> Paper
                        ExpectedResult.LOSE -> Rock
                    }
                }

                Rock -> when (expectedResult) {
                    ExpectedResult.WIN -> Paper
                    ExpectedResult.DRAW -> Rock
                    ExpectedResult.LOSE -> Scissor
                }

                Scissor -> when (expectedResult) {
                    ExpectedResult.WIN -> Rock
                    ExpectedResult.DRAW -> Scissor
                    ExpectedResult.LOSE -> Paper
                }
            }
        }
    }
}

enum class ExpectedResult {
    WIN,
    DRAW,
    LOSE;

    companion object {
        fun create(string: String): ExpectedResult {
            return when (string) {
                "X" -> LOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw UnsupportedOperationException()
            }
        }
    }
}

data class Game(val elf: Hand, val me: Hand) {
    fun play(): Int {
        var point = 0

        point += when (me) {
            Hand.Rock -> 1
            Hand.Paper -> 2
            Hand.Scissor -> 3
        }

        point += when (me) {
            Hand.Rock -> {
                when (elf) {
                    Hand.Rock -> 3
                    Hand.Paper -> 0
                    Hand.Scissor -> 6
                }
            }

            Hand.Paper -> {
                when (elf) {
                    Hand.Rock -> 6
                    Hand.Paper -> 3
                    Hand.Scissor -> 0
                }
            }

            Hand.Scissor -> {
                when (elf) {
                    Hand.Rock -> 0
                    Hand.Paper -> 6
                    Hand.Scissor -> 3
                }
            }
        }

        return point
    }
}
