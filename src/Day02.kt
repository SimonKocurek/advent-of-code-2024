import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {

    val input = readInput("Day02")

    val numbers = input.map { line ->
        line
            .split(" ")
            .map { it.toInt() }
    }

    fun part1() = numbers.count { line ->
        val sign = (line[0] - line[1]).sign

        line.zipWithNext { current, next ->
            val difference = current - next
            if (difference.sign != sign) {
                return@count false
            }

            if (difference.absoluteValue !in 1..3) {
                return@count false
            }
        }

        return@count true
    }

    fun part2() = numbers.count { originalLine ->
        val possibleLines = List(originalLine.size) { index ->
            originalLine.subList(0, index) + originalLine.subList(index + 1, originalLine.lastIndex + 1)
        }

        possibleLines.any { line ->
            val sign = (line[0] - line[1]).sign

            line.zipWithNext { current, next ->
                val difference = current - next
                if (difference.sign != sign) {
                    return@any false
                }

                if (difference.absoluteValue !in 1..3) {
                    return@any false
                }
            }

            true
        }
    }

    part1().println()
    part2().println()
}
