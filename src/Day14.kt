
fun main() {
    Day14.printSolutionIfTest(24,93)
}

object Day14: Day<Int, Int>(14) {
    data class Point(val x:Int, val y:Int)
    override fun part1(lines: List<String>, ) = countSand(lines, false)
    override fun part2(lines: List<String>, ) = countSand(lines, true)

    fun countSand(lines: List<String>, floor: Boolean): Int {
        val grid = loadGrid(lines)
        val maxy = grid.maxOf { it.y } + 2
        var size = 0
        while(drop(Point(500, 0), grid, maxy, floor)) size++
        return size;
    }

    private fun loadGrid(lines: List<String>): MutableSet<Point> {
        val grid = mutableSetOf<Point>()
        lines.map { line ->
            line.split(" -> ").map { point ->
                point.split(",")
            }.map { (x, y) -> Point(x.toInt(), y.toInt()) }
        }.forEach { path -> addToGrid(path, grid) }
        return grid
    }

    fun drop(start: Point, grid: MutableSet<Point>, maxDepth: Int, floor: Boolean): Boolean {
        val below = Point(start.x, start.y+1)
        val dl = Point(start.x-1, start.y+1)
        val dr = Point(start.x+1, start.y+1)
        return when{
            !floor && start.y > maxDepth -> false
            floor && start.y == maxDepth-1 -> grid.add(start)
            grid.containsAll(listOf(below, dl, dr)) -> grid.add(start)
            !grid.contains(below) -> drop(below, grid, maxDepth, floor)
            !grid.contains(dl) -> drop(dl, grid, maxDepth, floor)
            !grid.contains(dr) -> drop(dr, grid, maxDepth, floor)
            else -> false
        }
    }

    private fun addToGrid(path: List<Point>, grid: MutableSet<Point>) {
        path.windowed(2, 1).forEach { (a, b) ->
            range(a.x, b.x).forEach{grid.add(Point(it, a.y))}
            range(a.y, b.y).forEach{grid.add(Point(a.x, it))}
        }
    }

    private fun range(a:Int, b:Int) = minOf(a,b) .. maxOf(a,b)
}

