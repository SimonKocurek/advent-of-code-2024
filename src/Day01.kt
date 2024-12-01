import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { line ->
            line
                .split("\\s+".toRegex())
                .map { it.toInt() }
        }

        val firstColumn = numbers.map { it.first() }.sorted()
        val secondColumn = numbers.map { it.last() }.sorted()

        return firstColumn.zip(secondColumn).sumOf { (it.first - it.second).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { line ->
            line
                .split("\\s+".toRegex())
                .map { it.toInt() }
        }

        val scores = numbers.groupBy { it.last().toInt() }.mapValues { it.value.size }
        return numbers.map { it.first() }.sumOf { it * (scores[it] ?: 0) }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
