fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var cur = 0
        input.forEach {

            if (it.isBlank()){
                max = if (cur > max)  cur else max
                cur=0
            } else {
                cur += it.toInt()
            }
        }
        return max
    }

    fun part2(input: List<String>): Int {
        var cur = 0
        val totals = ArrayList<Int>()
        input.forEach {
            if (it.isBlank()){
                totals.add(cur)
                cur=0
            } else {
                cur += it.toInt()
            }
        }
        return totals.sorted().reversed().take(3).sum()
    }

    val input = readInput("day1")
    println(part1(input))
    println(part2(input))
}
