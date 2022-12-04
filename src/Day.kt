import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

abstract class Day<P1, P2> (day: Int) {
    private val input = readInput("day$day")
    private val testInput = readInput("day${day}_test")

    fun printSolutionIfTest(test1: P1, test2: P2) {
        printIfTest("Part1", this::part1, this::part1Test, test1)
        printIfTest("Part2", this::part2, this::part2Test, test2)
    }


    @OptIn(ExperimentalTime::class)
    private fun <T> printIfTest(label: String, func: (List<String>) -> T, testFunc: (List<String>) -> T, test: T) {
        testFunc(testInput).let {
            if (it == test) {
                val (value, duration) = measureTimedValue { func(input) }
                println("$label: \n${value} (${duration.inWholeMilliseconds}ms)")
            }
            else println("$label failed - expected \n[$test] but was \n[$it]")
        }
    }

    abstract fun part1(lines: List<String>): P1
    abstract fun part2(lines: List<String>): P2

    open fun part1Test(lines: List<String>): P1  = part1(lines)

    open fun part2Test(lines: List<String>): P2  = part2(lines)
}