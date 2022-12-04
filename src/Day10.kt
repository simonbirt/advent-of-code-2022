fun main() {
    Day10.printSolutionIfTest(13140, part2TestValue)
}

object Day10 : Day<Int, String>(10) {
    override fun part1(lines: List<String>) =
        lines.asSequence().flatMap { decode(it) }
            .runningFold(1) { a, b -> a + b }
            .mapIndexed { a, b -> Pair(a + 1, b) }
            .filter { (index, _) -> index in (20..220 step 40) }
            .sumOf { (index, value) -> index * value }

    override fun part2(lines: List<String>) =
        lines.asSequence().flatMap { decode(it) }
            .runningFold(1) { a, b -> a + b }
            .mapIndexed { index, value -> if (index % 40 - value in (-1.. 1)) "⚪" else "⚫️" }
            .windowed(40, 40)
            .joinToString("\n") { it.joinToString("") }

    private fun decode(code: String) = when {
        code.startsWith("addx") -> listOf(0, code.substringAfter(" ").toInt())
        else -> listOf(0)
    }
}

val part2TestValue = """
⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️⚪⚪⚫️⚫️
⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️⚫️⚫️⚪⚪⚪⚫️
⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚫️⚫️⚫️⚫️
⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️
⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪
⚪⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️⚫️⚫️⚪⚪⚪⚪⚪⚪⚪⚫️⚫️⚫️⚫️⚫️
""".trimIndent()