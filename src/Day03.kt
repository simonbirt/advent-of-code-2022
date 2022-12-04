fun main() {
    Day3.printSolutionIfTest(157,70)
}

object Day3: Day<Int, Int>(3){
    private fun Char.priority(): Int =
        1 + if (isUpperCase()) this - 'A' + 26 else this - 'a'

    private fun sumPriority(groups: List<List<String>>): Int =
        groups.sumOf { it[0].first { c -> it.drop(1).all { l -> l.contains(c) } }.priority() }

    override fun part1(lines: List<String>) =
        sumPriority(lines.map{it.chunked(it.length/2)})

    override fun part2(lines: List<String>) =
        sumPriority(lines.chunked(3))
}
