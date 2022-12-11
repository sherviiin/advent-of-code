fun main() {


    check(10605 == findHighestChance(input = readInput("Day11_sample_data"), rounds = 20, divideBy3 = true).toInt())
    println(findHighestChance(input = readInput("Day11"), rounds = 10000, divideBy3 = false))
}

fun findHighestChance(input: List<String>, rounds: Int, divideBy3: Boolean): Long {
    val monkeys = mutableListOf<Monkey>()

    input.forEach { l ->
        val line = l.trim()
        if (line.startsWith("Monkey")) {
            val id = line.removePrefix("Monkey ").removeSuffix(":").toInt()
            monkeys.add(Monkey(id, mutableListOf()))
        }
        if (line.trim().startsWith("Starting items:")) {
            val items = line.removePrefix("Starting items: ").split(", ").map { it.toLong() }
            monkeys.last().items.addAll(items)
        }
        if (line.startsWith("Operation: new = old ")) {
            val split = line.removePrefix("Operation: new = old ").split(" ")
            monkeys.last().operation = Operation(Operator.fromString(split[0]), split[1])
        }
        if (line.startsWith("Test: divisible by ")) {
            val condition = line.removePrefix("Test: divisible by ").toLong()
            monkeys.last().condition = condition
        }
        if (line.startsWith("If true: throw to monkey ")) {
            val id = line.removePrefix("If true: throw to monkey ").toInt()
            monkeys.last().trueTargetMonkey = id
        }
        if (line.startsWith("If false: throw to monkey ")) {
            val id = line.removePrefix("If false: throw to monkey ").toInt()
            monkeys.last().falseTargetMonkey = id
        }
    }

    round(monkeys, rounds, divideBy3)
    monkeys.sortByDescending { it.inspected }
    return monkeys.take(2).map { it.inspected }.reduce { i, j -> i * j }
}

fun round(monkeys: List<Monkey>, rounds: Int, divideBy3: Boolean) {
    val base = monkeys.fold(1L) { acc, monkey -> acc * monkey.condition }
    repeat(rounds) {
        monkeys.forEach { monkey ->
            val itemsIterator = monkey.items.iterator()
            while (itemsIterator.hasNext()) {
                val item = itemsIterator.next()
                var worryLevel = monkey.operation!!.let { op ->
                    val operand = if (op.operand == "old") item else op.operand.toLong()
                    when (op.operator) {
                        Operator.Add -> item + operand
                        Operator.Divide -> item / operand
                        Operator.Multiply -> item * operand
                        Operator.Subtract -> item - operand
                    }
                }

                if (divideBy3) {
                    worryLevel /= 3
                } else {
                    worryLevel = worryLevel.mod(base)
                }

                if (worryLevel % monkey.condition == 0L) {
                    monkeys.first { it.id == monkey.trueTargetMonkey }.items.add(worryLevel)
                } else {
                    monkeys.first { it.id == monkey.falseTargetMonkey }.items.add(worryLevel)
                }
                monkey.inspected++
                itemsIterator.remove()
            }
        }
    }
}

data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    var operation: Operation? = null,
    var condition: Long = 0,
    var trueTargetMonkey: Int = 0,
    var falseTargetMonkey: Int = 0,
    var inspected: Long = 0
)

sealed class Operator {
    object Multiply : Operator()
    object Divide : Operator()
    object Add : Operator()
    object Subtract : Operator()

    companion object {
        fun fromString(string: String): Operator {
            return when (string) {
                "*" -> Multiply
                "/" -> Divide
                "-" -> Subtract
                "+" -> Add
                else -> throw UnsupportedOperationException()
            }
        }

    }
}

data class Operation(val operator: Operator, val operand: String)