import kotlin.math.abs
import kotlin.math.max

fun main() {
    Day15.printSolutionIfTest(26, 56000011)
}



object Day15: Day<Int, Long>(15) {
    override fun part1Test(lines: List<String>) = countPositions(lines, 10)
    override fun part1(lines: List<String>) = countPositions(lines, 2000000)
    override fun part2Test(lines: List<String>) = calculateFrequency(lines, 20)
    override fun part2(lines: List<String>) = calculateFrequency(lines, 4000000)

    private fun countPositions(lines: List<String>, row:Int):Int {
        val sensors = lines.map{ line -> parseSensor(line)}
        return points(sensors, row).flatten().distinct().count() -1
    }

    private fun calculateFrequency(lines: List<String>, max: Int):Long {
        val sensors = lines.map{ line -> parseSensor(line)}
        val missing = (0 .. max).asSequence().map{ row -> IndexedValue(row, points(sensors,row)) }
            .map{ IndexedValue(it.index, gap(it.value, max)) }.filter { it.value != null } .first()

        return ( (missing.value!! * 4000000L) + missing.index)
    }

    private fun gap(ranges: List<IntRange>, max:Int):Int? {
        var lastMax = 0

        for (r in ranges.sortedBy { it.first() }) {
            when {
                r.first > lastMax -> return lastMax+1
                r.last() >= max -> return null
                else -> lastMax = max(lastMax, r.last)
            }
        }
        return null
    }

    private fun points(sensors: List<Sensor>, row:Int): List<IntRange> {
        return sensors.mapNotNull { s -> s.points(row) }
    }

    private fun parseSensor(line: String): Sensor {
        val (x, y, bx, by) = line.split("=", ",", ":").mapNotNull { it.toIntOrNull() }
        return Sensor(x ,y, bx, by)
    }

    data class Sensor(val x:Int, val y:Int, val bx:Int, val by:Int) {
        private val distance = abs(x - bx)  + abs(y - by)
        fun points(lineHeight: Int):IntRange? {
            val xd = distance - abs(y-lineHeight)
            return if ( xd >= 0 ) {
                range(x-xd, x+xd)
            } else {
                null
            }
        }

    }

    private fun range(a:Int, b:Int) = minOf(a,b) .. maxOf(a,b)
}

