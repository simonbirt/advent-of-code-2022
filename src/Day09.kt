import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    Day9.printSolutionIfTest(88, 36)
}

object Day9 : Day<Int, Int>(9) {
    override fun part1(lines: List<String>) = solveForLength(lines, 1)

    override fun part2(lines: List<String>) = solveForLength(lines, 9)

    private val moves = mapOf("R" to Pair(1, 0), "L" to Pair(-1, 0), "U" to Pair(0, 1), "D" to Pair(0, -1))

    private fun solveForLength(lines: List<String>, length: Int): Int {
        val (head, tail) = ropeOfLength(length)
        lines.map { it.split(" ") }.forEach { (direction, count) ->
            repeat(count.toInt()) {
                moves[direction]?.let { (x, y) -> head.move(x, y) }
            }
        }
        return tail.countPositions()
    }

    private fun ropeOfLength(length: Int) = Knot().let { Pair(it, it.addLength(length))}

    class Knot(private var x: Int = 0, private var y: Int = 0) {
        private val positions = mutableSetOf(Pair(x, y))
        private var next: Knot? = null

        fun move(dx: Int, dy: Int) {
            x += dx
            y += dy
            positions.add(Pair(x, y))
            next?.follow(this)
        }

        private fun follow(prior: Knot) {
            val xdiff = prior.x - x
            val ydiff = prior.y - y
            if (xdiff.absoluteValue > 1 || ydiff.absoluteValue > 1) {
                move(xdiff.sign, ydiff.sign)
            }
        }

        fun countPositions() = positions.size
        fun addLength(count: Int):Knot {
            return (1..count).map{ Knot() }.fold(this) { a, b -> a.next = b; b }
        }
    }
}
