fun main() {
    Day5.printSolutionIfTest("CMZ", "MCD")
}

typealias Stacks = MutableMap<Int, List<String>>
object Day5 : Day<String, String>(5) {
    override fun part1(lines: List<String>) = solve(lines, true)
    override fun part2(lines: List<String>) = solve(lines, false)

    private fun solve(lines: List<String>, rev:Boolean) =
        readStacks(lines)
            .also {applyInstructions(lines, it, rev)}
            .values.joinToString("") { it.first() }

    private fun readStacks(lines: List<String>) =
        lines.takeWhile { it.contains("[") }
            .flatMap { l -> l.chunked(4).withIndex().filter { it.value.isNotBlank() }}
            .groupBy({ it.index + 1 }, { it.value[1].toString() })
            .toSortedMap()

    private fun applyInstructions(lines: List<String>, stacks: Stacks, rev:Boolean) {
        lines.filter { it.contains("move") }
            .map { l -> l.split("move ", " from ", " to ").filter { it.isNotBlank() }.map { it.toInt() } }
            .forEach { (m, f, t) ->  move(stacks, m, f, t, rev)}
    }

    private fun move(stacks: Stacks, quantity: Int, source: Int, destination: Int, rev: Boolean) {
        stacks[destination] = stacks[source]?.take(quantity)?.let{ if (rev) it.reversed() else it }!! + stacks[destination]!!
        stacks[source] = stacks[source]?.drop(quantity)!!
    }

}

