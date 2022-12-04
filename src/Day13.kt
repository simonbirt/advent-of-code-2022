import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

fun main() {
    Day13.printSolutionIfTest(13, 140)
}

object Day13 : Day<Int, Int>(13) {
    override fun part1(lines: List<String>) =
        lines.filter { it.isNotBlank() }
            .map { convert(it) }.chunked(2).withIndex()
            .filter { (_, v) -> compare(v[0], v[1]) <= 0 }
            .map { (i, _) -> i + 1 }.sumOf { it }

    override fun part2(lines: List<String>) =
        (listOf(2, 6).map { JsonArray(listOf(JsonPrimitive(it))) } + lines.filter { it.isNotBlank() }
            .map { convert(it) })
            .withIndex().sortedWith { a, b -> compare(a.value, b.value) }
            .withIndex().filter { (_, v) -> v.index in 0..1 }
            .map { (i, _) -> i + 1 }.reduce(Int::times)

    private fun convert(line: String) = Json.decodeFromString<JsonArray>(line)

    private fun compare(a: JsonArray, b: JsonArray): Int {
        return a.zip(b).map { (l, r) -> compareElements(l, r) }.firstOrNull { it != 0 } ?: (a.size - b.size)
    }

    private fun compareElements(a: JsonElement, b: JsonElement): Int {
        return when {
            a is JsonPrimitive && b is JsonPrimitive -> a.int - b.int
            a is JsonArray && b is JsonArray -> compare(a, b)
            a is JsonPrimitive && b is JsonArray -> compare(JsonArray(listOf(a)), b)
            a is JsonArray && b is JsonPrimitive -> compare(a, JsonArray(listOf(b)))
            else -> throw IllegalStateException("Unexpected values for compare $a $b")
        }
    }
}