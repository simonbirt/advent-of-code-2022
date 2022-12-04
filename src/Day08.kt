fun main() {
    Day8.printSolutionIfTest(21, 8)
}

object Day8 : Day<Int, Int>(8) {
    override fun part1(lines: List<String>): Int =
        buildGrid(lines) { tagVisible(it) }.flatten().count { it.visible }

    override fun part2(lines: List<String>): Int =
        buildGrid(lines) { tagScores(it) }.flatten().maxOf { it.score ?: 0 }

    private fun buildGrid(lines: List<String>, process: (List<Tree>) -> Unit): List<List<Tree>> =
        lines.map { line -> line.map { Tree(it.digitToInt()) } }.also { grid ->
            grid.sightLines().forEach {
                process(it)
            }
        }

    private fun tagVisible(trees: List<Tree>) =
        trees.map { it.height }.runningFold(0) { acc, h -> maxOf(acc, h) }.dropLast(1).withIndex()
            .filter { (index, max) -> trees[index].height > max }.forEach { (index, _) -> trees[index].visible = true }
            .also { trees.last().visible = true }

    private fun tagScores(trees: List<Tree>) = trees.drop(1).dropLast(1).forEachIndexed { index, tree ->
        val score = (index downTo 0).indexOfFirst { trees[it].height >= tree.height }.let {
            1 + if (it < 0) index else it
        }
        tree.score = tree.score?.times(score) ?: score
    }

    data class Tree(val height: Int, var visible: Boolean = false, var score: Int? = null)

    private fun <T> List<List<T>>.toCols() = List(this[0].size) { index -> this.map { it[index] } }
    private fun <T> List<List<T>>.sightLines() = listOf(this, this.toCols()).flatten().flatMap { listOf(it, it.reversed()) }
}
