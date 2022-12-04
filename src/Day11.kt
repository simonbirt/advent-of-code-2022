fun main() {
    Day11.printSolutionIfTest(10605, 2713310158L)
}

typealias Operation = (Long) -> Long

object Day11 : Day<Long, Long>(11) {

    override fun part1(lines: List<String>) =
        parseMonkeys(lines).doRounds(20) { it / 3 }.monkeyBusiness()
    override fun part2(lines: List<String>):Long {
        val monkeys = parseMonkeys(lines)
        return monkeys.doRounds(10000) { it % monkeys.testProduct }.monkeyBusiness()
    }

    private fun parseMonkeys(lines: List<String>): Monkeys {
        val monkeys: MutableList<Monkey> = mutableListOf()
        lines.forEach { line ->
            when {
                line.startsWith("Monkey") -> monkeys.add(Monkey())
                line.contains("Starting items: ") -> monkeys.last().items.addAll(
                        line.substringAfter(": ").split(", ").map { it.toLong() })

                line.contains("Operation") -> monkeys.last().operation =
                    parseOperation(line.substringAfter("new = old "))

                line.contains("Test") -> monkeys.last().test = line.substringAfterLast(" ").toLong()
                line.contains("If true: ") -> monkeys.last().trueDest = line.substringAfterLast(" ").toInt()
                line.contains("If false: ") -> monkeys.last().falseDest = line.substringAfterLast(" ").toInt()
            }
        }
        return Monkeys(monkeys)
    }

    private fun parseOperation(expr: String): Operation {
        val op: (Long, Long) -> Long = when (expr[0]) {
            '+' -> Long::plus
            '*' -> Long::times
            else -> throw Error("Unsupported op")
        }
        return when (val operand = expr.substringAfterLast(" ")) {
            "old" -> { a -> op(a, a) }
            else -> { a -> op(a, operand.toLong()) }
        }
    }

    class Monkeys(private val monkeys: MutableList<Monkey>) {
        val testProduct: Long = monkeys.map { it.test }.reduce { a, b -> a * b }

        fun monkeyBusiness() =
            monkeys.map { monkey -> monkey.inspections }.sortedDescending().take(2).reduce(Long::times)

        fun doRounds(rounds: Int, reductionStrategy: (Long) -> Long): Monkeys =
            this.also { repeat(rounds) { round(reductionStrategy) } }

        private fun round(reductionStrategy: (Long) -> Long) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.inspections++

                    val newItem = reductionStrategy(monkey.operation(item))

                    if (newItem % monkey.test == 0L) {
                        monkeys[monkey.trueDest].items.add(newItem)
                    } else {
                        monkeys[monkey.falseDest].items.add(newItem)
                    }
                }
                monkey.items.clear()
            }
        }
    }

    class Monkey {
        var inspections = 0L
        var operation: Operation = { a -> a }
        var test = 0L
        var trueDest = -1
        var falseDest = -1
        val items = mutableListOf<Long>()
    }
}
