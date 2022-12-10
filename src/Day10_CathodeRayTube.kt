private val regex = "addx\\s(-?\\d.*)|(noop)".toRegex()
fun main() {

    check(13140 == findSignalStrength("Day10_sample_data"))
    val result = findSignalStrength("Day10")
    println(result)

    println(findSprite("Day10"))
}

fun findSignalStrength(fileName: String): Int {

    val points: List<Int> = listOf(20, 60, 100, 140, 180, 220)
    var sum = 0
    var i = 0
    var register = 1
    fun cycle() {
        i++
        if (points.contains(i)) {
            sum += register * i
        }
    }

    readInput(fileName).forEach { line ->

        val result = regex.find(line)
        result?.groups?.get(1)?.let {
            cycle()
            cycle()
            register += it.value.toInt()

        } ?: result?.groups?.get(2)?.let {
            cycle()
        }
    }

    return sum
}


fun findSprite(fileName: String): String = buildString {
    val input = readInput(fileName)
    val iterator = input.iterator()
    var i = 0
    var register = 1
    for (row in 0 until 240 step 40) {
        if (row != 0) append('\n')
        repeat(40) { col ->
            append(if (register - col in -1..1) "#" else ".")
            while (i < row + col) {

                val result = regex.find(iterator.next())
                result?.groups?.get(1)?.let {

                    i += 2
                    register += it.value.toInt()

                } ?: result?.groups?.get(2)?.let {
                    i++
                }
            }
        }
    }
}