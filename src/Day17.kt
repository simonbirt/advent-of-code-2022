import kotlin.math.min

fun main() {
    Day17.printSolutionIfTest(3068, 56000011)
}



object Day17: Day<Long, Long>(17) {
    private fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

    private val shapes = listOf(
        "####",
        """
            .#.
            ###
            .#.
        """.trimIndent(),
        """
            ..#
            ..#
            ###
        """.trimIndent(),
        """
            #
            #
            #
            #
        """.trimIndent(),
        """
            ##
            ##
        """.trimIndent())

    private fun parseShape(shapeDef: String): Shape{
        return Shape(shapeDef.split("\n"))
    }

    class Shape(def: List<String>) {
        private val def = def.map { it.toList() }.reversed()
        private val width = def[0].length
        private var overlap = -3
        private var x = 2

        fun touchesBelow(stack: List<List<Char>>):Boolean {
            if (overlap < 0) return false
            //for (i in 0 .. overlap) {
                // compare top of stack to bottom of shape, overlapped by rows
                val rowsToCompare = min(overlap+1, def.size)
                if (compareRows(stack.subList(stack.lastIndex - overlap, stack.lastIndex - overlap + rowsToCompare), def.subList(0, rowsToCompare)))
                    return true
            //}
            return false
        }

        private fun compareRows(base: List<List<Char>>, shape: List<List<Char>>): Boolean {
            shape.forEachIndexed{index, value -> if (compareRow(base[index], value)) return true}
            return false
        }

        private fun compareRow(base: List<Char>, shape: List<Char>):Boolean{
            shape.forEachIndexed{ index, value  -> if (base[index + x] == '#' && value == '#') return true}
            return false
        }

        fun move(dir: Char, stack: List<List<Char>>) {

            x = when(dir) {
                '<' -> if (!isBlockedLeft(stack)) x-1 else x
                else -> if (!isBlockedRight(stack)) x+1 else x
            }
            //println("Move $dir to $x")
        }

        private fun isBlockedLeft(stack: List<List<Char>>): Boolean {
            if (x==0) return true
            (0 until min(overlap, def.size)).forEach{
                    row -> if (isBlockedLeft(def[row], stack[stack.lastIndex - (overlap-1) + row])) return true
            }
            return false
        }
        private fun isBlockedLeft(shape: List<Char>, base: List<Char>):Boolean {
            val shapeLeftIndex = x + shape.indexOfFirst { it == '#' }
            return base[shapeLeftIndex-1] == '#'
        }
        private fun isBlockedRight(stack: List<List<Char>>): Boolean {
            if (x==7-width) return true
            (0 until min(overlap, def.size)).forEach{
                    row -> if (isBlockedRight(def[row], stack[stack.lastIndex - (overlap-1) + row])) return true
            }
            return false
        }
        private fun isBlockedRight(shape: List<Char>, base: List<Char>):Boolean {
            val shapeRightIndex = x + shape.indexOfLast { it == '#' }
            return base[shapeRightIndex+1] == '#'
        }

        private val paddings = listOf("", ".","..","...","....",".....","......",".......").map {it.toList()}
        fun stamp(stack: MutableList<MutableList<Char>>):Int {
            val padLeft = paddings[x]
            val padRight = paddings[7-(x+width)]
            val paddedShape = def.map { l -> (padLeft + l + padRight) }
            val rowsToCombine = min(overlap, def.size)
            var index = 0
            val minStackIndex = stack.lastIndex - (overlap-1)
            (0 until rowsToCombine).forEach{ row ->
                val stackIndex = minStackIndex + row
                combineStackRow(paddedShape[index++], stack[stackIndex])
            }
            (index until paddedShape.size).forEach{
                stack.add(paddedShape[it].toMutableList())
            }
            return minStackIndex
        }

        private fun combineStackRow(shapeRow: List<Char>, stackRow: MutableList<Char>) {
            shapeRow.forEachIndexed{index, value -> if (value == '#') stackRow[index] = value}
        }

        fun reset() {
            x = 2
            overlap = -3
        }

        fun drop() {
            //println("Move down")
            overlap++
        }
    }

    /*
####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##


     */

    override fun part1(lines: List<String>):Long = solve(lines, 2022L)

    private fun solve(lines: List<String>, count: Long): Long {
        val shapeSeq = shapes.map { parseShape(it) }.asSequence().repeat().iterator()
        val directions = lines[0].asSequence().repeat().asIterable().iterator()
        val stack = mutableListOf("#######".toMutableList())
        var stackSize = 0L
        (1..count).forEach {
            stackSize += drop(shapeSeq, directions, stack)
            if (it%100000 == 0L){ println(it)
            }
        }
        stackSize += stack.size
        return stackSize - 1

    }

    private fun drop (shapes: Iterator<Shape>, directions: Iterator<Char>, stack: MutableList<MutableList<Char>>):Int {
        val shape = shapes.next()
        while (true) {
            shape.move(directions.next(), stack)
            if(shape.touchesBelow(stack)){
                break
            } else {
                shape.drop()
            }
        }
        val minIndex = shape.stamp(stack)

        var test=0
        (minIndex..min(minIndex+3, stack.lastIndex)).forEach{ stack[it].forEachIndexed{index, c ->
            if (c == '#') test = test or (1 shl index)
        } }
        shape.reset()
        if (test == 0b1111111){
            stack.subList(0, minIndex).clear()
            return minIndex
        }
        return 0
    }

    override fun part2(lines: List<String>) = solve(lines, 1_000_000_000_000L)


}

