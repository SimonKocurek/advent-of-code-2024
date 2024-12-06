fun main() {

    val directions = listOf(
        -1 to 0,
        0 to 1,
        1 to 0,
        0 to -1
    )

    fun startPosition(map: List<List<Char>>): Pair<Int, Int> {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '^') {
                    return y to x
                }
            }
        }
        throw IllegalStateException()
    }

    fun part1(input: List<String>): Int {
        var directionIndex = 0
        val map = input.map { it.toMutableList() }

        var (y, x) = startPosition(map)
        while (true) {
            map[y][x] = 'X'

            val (yDiff, xDiff) = directions[directionIndex]
            val newY = y + yDiff
            val newX = x + xDiff

            if (newY < 0 || newY >= map.size || newX < 0 || newX >= map[newY].size) {
                break
            }

            if (map[newY][newX] == '#') {
                directionIndex = (directionIndex + 1) % directions.size
            } else {
                y = newY
                x = newX
            }
        }

        return map.sumOf { it.count { c -> c == 'X' } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }

        val (yStart, xStart) = startPosition(map)

        val changedMaps = mutableListOf<List<List<Char>>>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '.') {
                    val copy = map.map { it.toMutableList() }
                    copy[y][x] = '#'
                    changedMaps.add(copy)
                }
            }
        }

        return changedMaps.count { map ->
            var directionIndex = 0
            var x = xStart
            var y = yStart

            val visited = mutableSetOf<Triple<Int, Int, Int>>()
            while (Triple(x, y, directionIndex) !in visited) {
                visited.add(Triple(x, y, directionIndex))

                val (yDiff, xDiff) = directions[directionIndex]
                val newY = y + yDiff
                val newX = x + xDiff

                if (newY < 0 || newY >= map.size || newX < 0 || newX >= map[newY].size) {
                    return@count false
                }

                if (map[newY][newX] == '#') {
                    directionIndex = (directionIndex + 1) % directions.size
                } else {
                    y = newY
                    x = newX
                }
            }

            true
        }
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
