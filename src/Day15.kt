fun main() {

    val direction = mapOf(
        '^' to (-1 to 0),
        'v' to (1 to 0),
        '<' to (0 to -1),
        '>' to (0 to 1),
    )

    fun List<List<Char>>.sumCoordinates(char: Char): Long {
        var result = 0L
        doubleLoop { y, x ->
            if (this[y][x] == char) {
                result += 100L * y + x
            }
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val map = input.takeWhile { it.isNotBlank() }.map { it.toMutableList() }
        val instructions = input
            .takeLastWhile { it.isNotBlank() }
            .joinToString("")

        var (y, x) = map.findPosition('@')
        map[y][x] = '.'

        for (instruction in instructions) {
            val (yDiff, xDiff) = direction[instruction]!!

            val newX = x + xDiff
            val newY = y + yDiff

            if (map[newY][newX] == '#') {
                continue
            }

            if (map[newY][newX] == '.') {
                y = newY
                x = newX
                continue
            }

            // Pushing
            var pushedX = newX + xDiff
            var pushedY = newY + yDiff
            while (map[pushedY][pushedX] == 'O') {
                pushedX += xDiff
                pushedY += yDiff
            }

            if (map[pushedY][pushedX] == '.') {
                map[newY][newX] = '.'
                map[pushedY][pushedX] = 'O'
                y = newY
                x = newX
            }
        }

        return map.sumCoordinates('O')
    }

    fun part2(input: List<String>): Long {
        var map = input
            .takeWhile { it.isNotBlank() }
            .map {
                it.flatMap { c ->
                    when (c) {
                        '.' -> listOf('.', '.')
                        '#' -> listOf('#', '#')
                        '@' -> listOf('@', '.')
                        'O' -> listOf('[', ']')
                        else -> emptyList()
                    }
                }
            }
            .map { it.toMutableList() }

        val instructions = input
            .takeLastWhile { it.isNotBlank() }
            .joinToString("")

        fun List<MutableList<Char>>.move(y: Int, x: Int, yDiff: Int, xDiff: Int): Boolean {
            // Always align to the left edge of the box, to eliminate edge-cases
            val currX = if (this[y][x] == ']') x - 1 else x

            if (this[y][currX] == '#') {
                return false
            }

            if (this[y][currX] == '[') {
                this[y][currX] = '.'
                this[y][currX + 1] = '.'

                val pushedX = currX + xDiff
                val pushedY = y + yDiff

                if (
                    !when (xDiff) {
                        -1 -> move(pushedY, pushedX, yDiff, xDiff)
                        1 -> move(pushedY, pushedX + 1, yDiff, xDiff) // We need to move from the right edge
                        // On vertical moves We need to check both edges
                        else -> move(pushedY, pushedX, yDiff, xDiff) && move(pushedY, pushedX + 1, yDiff, xDiff)
                    }
                ) {
                    return false
                }

                this[pushedY][pushedX] = '['
                this[pushedY][pushedX + 1] = ']'
            }

            return true
        }

        var (y, x) = map.findPosition('@')
        map[y][x] = '.'

        for (instruction in instructions) {
            val (yDiff, xDiff) = direction[instruction]!!

            val newMap = map.map { it.toMutableList() }
            val newY = y + yDiff
            val newX = x + xDiff

            if (!newMap.move(newY, newX, yDiff, xDiff)) {
                continue
            }

            map = newMap
            y = newY
            x = newX
        }

        return map.sumCoordinates('[')
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
