import kotlin.math.max
import kotlin.math.min

fun main() {
    Day4.printSolutionIfTest(2, 4)
}
object Day4: Day<Int, Int>(4) {
    private fun ranges(lines: List<String>) =
        lines.map{ it.split(",","-").map(String::toInt) }

    override fun part1(lines: List<String>) =
        ranges(lines).count { (a, b, c, d) -> (a >= c && b <= d) || (c >= a && d <= b) }

    override fun part2(lines: List<String>) =
        ranges(lines).count { (a, b, c, d) -> max (a, c) <= min (b, d) }
}

