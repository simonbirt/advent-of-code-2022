fun main() {
    Day6.printSolutionIfTest(10,29)
}
object Day6: Day<Int, Int>(6) {
    override fun part1(lines: List<String>):Int =
        solve(lines[0], 4)

    override fun part2(lines: List<String>):Int =
        solve(lines[0], 14)

    private fun solve(line: String, window: Int):Int =
        window + line.windowed(window,1)
            .indexOfFirst{ it.toSet().size == window }
}

