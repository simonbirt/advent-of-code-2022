fun main() {
    Day23.printSolutionIfTest(110, 20)
}

object Day23 : Day<Int, Int>(23) {

    override fun part1(lines: List<String>): Int {
        val map = lines.flatMapIndexed { y: Int, s: String -> s.chunked(1).mapIndexed { x, v -> Pair(Point(x, y), v) } }
            .toMap().toMutableMap()
        (1..10).forEach { round(it, map) }
        val elves = map.filter { (_, v) -> v == "#" }.keys

        val minx = elves.minOf { it.x }
        val miny = elves.minOf { it.y }
        val maxx = elves.maxOf { it.x }
        val maxy = elves.maxOf { it.y }

        val totalArea = (1 + maxx - minx) * (1 + maxy - miny)
        return totalArea - elves.size
    }

    override fun part2(lines: List<String>): Int {
        val map = lines.flatMapIndexed { y: Int, s: String -> s.chunked(1).mapIndexed { x, v -> Pair(Point(x, y), v) } }
            .toMap().toMutableMap()
        (1..Int.MAX_VALUE).forEach { if (round(it, map) == 0) return it }
        return -1
    }

    data class Point(val x: Int, val y: Int)
    data class Move(val from: Point, val to: Point)


    enum class Direction(val x: Int, val y: Int) {
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0),
        NW(-1, -1);

        fun apply(from: Point) = Point(from.x + x, from.y + y)

        companion object {
            private val directions = listOf(listOf(N, NE, NW), listOf(S, SE, SW), listOf(W, NW, SW), listOf(E, NE, SE))
            fun directionsForRound(round: Int) = (round..round + 3).map { directions[it % 4] }
        }
    }


    private fun round(num: Int, map: MutableMap<Point, String>): Int {
        val moves = mutableListOf<Move>()
        val alreadyProposed = mutableListOf<Point>()
        map.filter { (_, v) -> v == "#" }.forEach { (k, _) ->
            if (Direction.values().any { d -> (map[d.apply(k)] ?: ".") == "#" }) {
                Direction.directionsForRound(num - 1).firstOrNull { d ->
                    d.all { (map[it.apply(k)] ?: ".") == "." }
                }?.get(0)?.apply(k)?.let {
                    if (!alreadyProposed.contains(it)) {
                        moves.add(Move(from = k, to = it))
                        alreadyProposed.add(it)
                    } else {
                        moves.removeIf { (_, to) -> to == it }
                    }
                }
            }
        }
        moves.forEach { (from, to) ->
            map[from] = "."
            map[to] = "#"
        }
        return moves.size
    }


}



