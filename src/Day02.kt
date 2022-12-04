fun main() {
    val scores = mapOf("A" to 1, "B" to 2, "C" to 3, "X" to 1, "Y" to 2, "Z" to 3)

    fun choose(them:Int, req:Int):Int {
        return when (them) {
            1 -> when (req) { 1 -> 3 2 -> 1 else -> 2}
            2 -> when (req) { 1 -> 1 2 -> 2 else -> 3}
            3 -> when (req) { 1 -> 2 2 -> 3 else -> 1}
            else -> 0
        }
    }
    fun game(them:Int, me:Int):Int {
        return when (them) {
            1 -> when (me) { 2 -> 6 3 -> 0 else -> 3}
            2 -> when (me) { 3 -> 6 1 -> 0 else -> 3}
            3 -> when (me) { 1 -> 6 2 -> 0 else -> 3}
            else -> 0
        }

    }

    fun part1(input: List<String>): Int {
        var total = 0
        input.forEach {
            val (elf, me) = it.split(" ").map { c -> scores[c] }
            total += game (elf!!, me!!) + me
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0
        input.forEach {
            val (elf, req) = it.split(" ").map { c -> scores[c] }
            val me = choose(elf!!, req!!)
            total += game (elf, me) + me
        }
        return total

    }

    val input = readInput("day2")
    println(part1(input))
    println(part2(input))
}
