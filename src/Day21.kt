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

    fun Map<Char, Pair<Int, Int>>.toMoves(): Map<Pair<Char, Char>, String> {
        val result = mutableMapOf<Pair<Char, Char>, String>()

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

                val stepsX = (if (dX < 0) ">" else "<").repeat(abs(dX))
                val stepsY = (if (dY < 0) "v" else "^").repeat(abs(dY))

                // In the robot keypad, we want to end up the closest
                // to 'A' at the end of the move:
                // - < is the furthest from 'A'
                // - v is the second furthest
                // - ^ and > are equally distant... but `>` has better movement options
                val ordering = mapOf(
                    null to -1,
                    '<' to 0,
                    'v' to 1,
                    '^' to 2,
                    '>' to 3,
                )

                val paths = mutableListOf<String>()

                // Moving in X first is only possible, when we wouldn't move through invalid point
                if (!(fromY == invalidY && toX == invalidX)) {
                    paths.add(stepsX + stepsY)
                }
                if (!(fromX == invalidX && toY == invalidY)) {
                    paths.add(stepsY + stepsX)
                }

                result[fromChar to toChar] = paths.sortedWith(
                    compareBy(
                        { it.length },
                        { ordering[it.firstOrNull()] },
                    )
                ).first()
            }
        }

        return result
    }

    val keypad = listOf(
        "789",
        "456",
        "123",
        " 0A",
    ).toPointMap().toMoves()

    val robotKeypad = listOf(
        " ^A",
        "<v>",
    ).toPointMap().toMoves()

    fun shortestOptions(sequence: String, layer: Int, endLayer: Int, layerCache: MutableMap<Triple<Int, Char, Char>, Long>): Long {
        var result = 0L

        // We start at "A", so the first transition should be from 'A' to the first character
        ("A" + sequence).zipWithNext { fromChar, toChar ->

            val cacheEntry = Triple(layer, fromChar, toChar)
            if (cacheEntry !in layerCache) {
                val moveOptions = if (layer == 0) keypad else robotKeypad

                val combination = moveOptions[fromChar to toChar] + "A"

                if (layer == endLayer) {
                    layerCache[cacheEntry] = combination.length.toLong()
                } else {
                    layerCache[cacheEntry] = shortestOptions(combination, layer + 1, endLayer, layerCache)
                }
            }

            result += layerCache[cacheEntry]!!
        }

        return result
    }

    fun combinationValue(combination: String, robots: Int): Long {
        val layerCache = mutableMapOf<Triple<Int, Char, Char>, Long>()

        val shortestOption = shortestOptions(combination, 0, robots, layerCache)
        val digitValue = combination.takeWhile { it.isDigit() }.toLong()

        return shortestOption * digitValue
    }

    fun part1() = input.sumOf { combinationValue(combination = it, robots = 2) }

    fun part2() = input.sumOf { combinationValue(combination = it, robots = 25) }

    part1().println()
    part2().println()
}
