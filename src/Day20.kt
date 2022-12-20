fun main() {
    Day20.printSolutionIfTest(3, 1623178306)
}

object Day20 : Day<Long, Long>(20) {
    override fun part1(lines: List<String>) = solve(lines,1,1)
    override fun part2(lines: List<String>) = solve(lines, 10, 811589153L)

    private fun solve(lines: List<String>, reps: Int, key: Long): Long {
        val initial = lines.map {it.toLong() * key}.withIndex()
        val size = initial.count()
        val lastIndex = size -1
        val final = initial.toMutableList()

        for(cycle in 1..reps) {
            initial.filter { it.value != 0L }.forEach {
                val pos = final.indexOf(it)
                val newPos = (pos + it.value).mod(lastIndex)
                final.removeAt(pos)
                final.add(newPos, it)
            }
        }

        return getResult(final, size)
    }

    private fun getResult(numbers:List<IndexedValue<Long>>, size: Int):Long {
        val indexZero = numbers.indexOfFirst { it.value == 0L }
        return listOf(1000,2000,3000).map{i -> numbers[(indexZero + i).mod(size)]}.map {it.value}.reduce(Long::plus)
    }

}



