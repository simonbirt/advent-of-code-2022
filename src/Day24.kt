fun main() {
    Day24.printSolutionIfTest(18, 54)
}

object Day24 : Day<Int, Int>(24) {
    override fun part1(lines: List<String>): Int {
        val map = parseMap(lines)
        return solve(map, map.entryPoint(), map.exitPoint())
    }

    override fun part2(lines: List<String>): Int {
        val map = parseMap(lines)
        return solve(map, map.entryPoint(), map.exitPoint(), map.entryPoint(), map.exitPoint())
    }

    private fun List<List<String>>.entryPoint() = Point(this[0].indexOfFirst { it == "." }, 0)
    private fun List<List<String>>.exitPoint() = Point(this[this.lastIndex].indexOfFirst { it == "." }, this.lastIndex)
    private val List<List<String>>.maxx: Int get() = this[0].lastIndex
    private val List<List<String>>.maxy: Int get() = lastIndex

    private fun solve(startMap: List<List<String>>, vararg route: Point): Int {
        var total = 0
        var map = startMap
        route.asList().windowed(2, 1).forEach {
            val (out, nextMap) = calculate(map, it[0], it[1])
            map = nextMap
            total += out
        }
        return total
    }

    private fun calculate(startMap: List<List<String>>, entryPoint: Point, exitPoint: Point): Pair<Int, List<List<String>>> {
        var possiblePositions = listOf(entryPoint)
        var map = startMap
        var count = 0
        while (!possiblePositions.contains(exitPoint)) {
            map = cycle(map)
            possiblePositions = checkPositions(map, possiblePositions).distinct()
            count++
        }
        return Pair(count, map)
    }

    private fun checkPositions(map: List<List<String>>, positions: List<Point>) = positions.flatMap {
        adjacentPoints(it, map.maxx, map.maxy).filter { newPos -> map[newPos.y][newPos.x] == "." }
    }

    private fun adjacentPoints(pos: Point, maxx: Int, maxy: Int) = listOfNotNull(
        pos,
        if (pos.x > 0) Point(pos.x - 1, pos.y) else null,
        if (pos.y > 0) Point(pos.x, pos.y - 1) else null,
        if (pos.x < maxx) Point(pos.x + 1, pos.y) else null,
        if (pos.y < maxy) Point(pos.x, pos.y + 1) else null
    )

    private fun cycle(map: List<List<String>>): List<List<String>> {
        val maxx = map.maxx
        val maxy = map.maxy
        val result = map.map { row -> row.map { if (it == "#") "#" else "." }.toMutableList() }.toMutableList()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                cell.chunked(1).forEach {
                    when (it) {
                        ">" -> setCell(maxx, maxy, x + 1, y, result, it)
                        "<" -> setCell(maxx, maxy, x - 1, y, result, it)
                        "^" -> setCell(maxx, maxy, x, y - 1, result, it)
                        "v" -> setCell(maxx, maxy, x, y + 1, result, it)
                    }
                }
            }
        }
        return result
    }

    private fun setCell(maxx: Int, maxy: Int, x: Int, y: Int, result: MutableList<MutableList<String>>, s: String) {
        val newx = when (x) { 0 -> maxx-1; maxx -> 1 else -> x}
        val newy = when (y) { 0 -> maxy-1; maxy -> 1 else -> y}
        result[newy][newx] += s
    }

    data class Point(val x: Int, val y: Int)

    private fun parseMap(lines: List<String>) = lines.map { l -> l.chunked(1) }
}



