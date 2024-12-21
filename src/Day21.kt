import kotlin.math.abs

fun main() {

    val input = readInput("Day21")

    fun List<String>.toPointMap(): Map<Char, Pair<Int, Int>> {
        val result = mutableMapOf<Char, Pair<Int, Int>>()

        doubleLoop { y, x ->
            result[this[y][x]] = y to x
        }

        return result
    }

    fun Map<Char, Pair<Int, Int>>.toMoveOptions(): Map<Pair<Char, Char>, List<String>> {
        val result = mutableMapOf<Pair<Char, Char>, MutableSet<String>>()

        val (invalidY, invalidX) = this[' ']!!

        for ((fromChar, fromPoint) in this) {
            val (fromY, fromX) = fromPoint
            if (fromY == invalidY && fromX == invalidX) {
                continue // Not a valid point
            }

            for ((toChar, toPoint) in this) {
                val (toY, toX) = toPoint
                if (toY == invalidY && toX == invalidX) {
                    continue // Not a valid point
                }

                val dX = fromX - toX
                val dY = fromY - toY

                val paths = result.getOrPut(fromChar to toChar) { mutableSetOf() }

                val stepsX = (if (dX < 0) ">" else "<").repeat(abs(dX))
                val stepsY = (if (dY < 0) "v" else "^").repeat(abs(dY))

                // Moving in X first is only possible, when we wouldn't move through invalid point
                if (!(fromY == invalidY && toX == invalidX)) {
                    paths.add(stepsX + stepsY)
                }
                if (!(fromX == invalidX && toY == invalidY)) {
                    paths.add(stepsY + stepsX)
                }
            }
        }

        return result.mapValues { it.value.toList() }
    }

    val keypad = listOf(
        "789",
        "456",
        "123",
        " 0A",
    ).toPointMap().toMoveOptions()

    val robotKeypad = listOf(
        " ^A",
        "<v>",
    ).toPointMap().toMoveOptions()

    fun shortestOptions(moveOptions: Map<Pair<Char, Char>, List<String>>, sequence: String): List<String> {
        var options = mutableSetOf("")

        // We start at "A", so the first transition should be from 'A' to the first character
        ("A" + sequence).zipWithNext { fromChar, toChar ->
            val newOptions = mutableSetOf<String>()

            moveOptions[fromChar to toChar]?.forEach { added ->
                options.forEach { previous ->
                    newOptions.add(previous + added + "A")
                }
            }

            options = newOptions
        }

        val shortestOptionLength = options.minOf { it.length }
        return options.filter { it.length == shortestOptionLength }
    }

    fun part1() = input.sumOf { line ->
        val length = shortestOptions(keypad, line).minOf { generatedLine ->
            shortestOptions(robotKeypad, generatedLine).minOf { secondGenerated ->
                shortestOptions(robotKeypad, secondGenerated).minOf {
                    it.length
                }
            }
        }

        length * line.takeWhile { it.isDigit() }.toInt()
    }

    fun part2() = 0

    part1().println()
    part2().println()
}
