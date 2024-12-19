import kotlin.math.absoluteValue

fun main() {

    val input = readInput("Day01")

    val numbers = input.map { line ->
        line
            .split("\\s+".toRegex())
            .map { it.toInt() }
    }

    fun part1(): Int {
        val firstColumn = numbers.map { it.first() }.sorted()
        val secondColumn = numbers.map { it.last() }.sorted()

        return firstColumn.zip(secondColumn).sumOf { (it.first - it.second).absoluteValue }
    }

    fun part2(): Int {
        val scores = numbers.groupBy { it.last().toInt() }.mapValues { it.value.size }
        return numbers.map { it.first() }.sumOf { it * (scores[it] ?: 0) }
    }

    part1().println()
    part2().println()
}
