
fun main() {
    Day7.printSolutionIfTest(95437,24933642)
}

object Day7: Day<Int, Int>(7) {
    override fun part1(lines: List<String>):Int =
        totals(lines).values.filter { it <= 100000 }.sum()

    override fun part2(lines: List<String>):Int =
        totals(lines).let{ totals ->
            val requiredSpace = 30000000 - (70000000 - totals["/"]!!)
            totals.values.sorted().first { it >= requiredSpace }
        }

    private fun totals(lines: List<String>):Map<String, Int> {
        var currentDir = mutableListOf("")
        val totals = HashMap<String, Int>()
        lines.forEach {
            when {
                it.startsWith("$ cd ") -> when (val dir = it.substring(5)) {
                    ".." -> currentDir.removeLast()
                    "/" -> currentDir = mutableListOf("")
                    else -> currentDir.add(dir)
                }
                it[0].isDigit() -> {currentDir.fold(""){ a,b -> "$a$b/".also{ d -> totals[d]= (totals[d] ?: 0) + it.split(" ")[0].toInt()}}}
            }
        }
        return totals
    }
}

