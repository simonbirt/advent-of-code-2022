fun main() {
    Day12.printSolutionIfTest(31, 29)
}

object Day12 : Day<Int, Int>(12) {
    override fun part1(lines: List<String>): Int {
        val map = Map2(lines)
        val start = map.select { it.label == 'S' }.first()
        val end = map.select { it.label == 'E' }
        return map.explore(start, end) { current, next -> next.height - current.height <= 1 }
    }

    override fun part2(lines: List<String>): Int {
        val map = Map2(lines)
        val start = map.select { it.label == 'E' }.first()
        val end = map.select { it.height == 'a'.code }
        return map.explore(start, end) { current, next -> current.height - next.height <= 1 }
    }

    class Map2(val lines: List<String>) {
        private val rows = lines.size
        private val cols = lines[0].length
        private val cells: List<Cell> = lines.flatMap { it.asIterable() }.mapIndexed { index, value ->
            when (value) {
                'S' -> Cell(value, index, 'a'.code)
                'E' -> Cell(value, index, 'z'.code)
                else -> Cell(value, index, value.code)
            }
        }

        private fun chooseNext(path: List<Cell>, test: (Cell, Cell) -> Boolean): Sequence<Cell> {
            val index = path.last().index
            return sequenceOf(::up, ::down, ::left, ::right).mapNotNull { it(index) }
                .filter { newIndex -> newIndex !in path.asReversed().map { it.index } }.map { cells[it] }
                .filter { test(path.last(), it) }
        }

        private fun up(index: Int): Int? = (index - cols).takeIf { it >= 0 }
        private fun down(index: Int): Int? = (index + cols).takeIf { it < rows * cols }
        private fun left(index: Int): Int? = (index - 1).takeIf { it >= 0 && index % cols > 0 }
        private fun right(index: Int): Int? = (index + 1).takeIf { it < rows * cols && index % cols < cols }

        fun select(test: (Cell) -> Boolean): List<Cell> {
            return cells.filter(test)
        }

        fun explore(start: Cell, end: List<Cell>, test: (Cell, Cell) -> Boolean): Int {
            end.forEach { explore(mutableListOf(start), it, test) }
            println("")
            return end.minOf { it.minPath }
        }

        private fun explore(path: MutableList<Cell>, end: Cell, test: (Cell, Cell) -> Boolean) {
            if (path.size == 0) {
                return
            }

            for (cell in chooseNext(path, test)) {
                if (path.size < cell.minPath) {
                    cell.minPath = path.size
                    if (cell.index == end.index) {
                        break
                    }
                    else {
                        path.add(cell)
                        explore(path, end, test)
                    }
                }
            }
            path.removeLast()
            return
        }
    }
}

data class Cell(val label: Char, val index: Int, val height: Int, var minPath: Int = Int.MAX_VALUE)

