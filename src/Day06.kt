fun main() {

    fun part1(input: List<String>): Int {
        var directionIndex = 0
        val map = input.map { it.toMutableList() }

        var (y, x) = map.findPosition('^')
        while (true) {
            map[y][x] = 'X'

            val (yDiff, xDiff) = directions4[directionIndex]
            val newY = y + yDiff
            val newX = x + xDiff

            if (!map.inBounds(newY, newX)) {
                break
            }

            if (map[newY][newX] == '#') {
                directionIndex = (directionIndex + 1) % directions4.size
            } else {
                y = newY
                x = newX
            }
        }

        return map.sumOf { it.count { c -> c == 'X' } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }

        val (yStart, xStart) = map.findPosition('^')

        val changedMaps = mutableListOf<List<List<Char>>>()
        map.doubleLoop { y, x ->
            if (map[y][x] == '.') {
                val copy = map.map { it.toMutableList() }
                copy[y][x] = '#'
                changedMaps.add(copy)
            }
        }

        return changedMaps.count { map ->
            var directionIndex = 0
            var x = xStart
            var y = yStart

            val visited = mutableSetOf<Triple<Int, Int, Int>>()
            while (Triple(x, y, directionIndex) !in visited) {
                visited.add(Triple(x, y, directionIndex))

                val (yDiff, xDiff) = directions4[directionIndex]
                val newY = y + yDiff
                val newX = x + xDiff

                if (!map.inBounds(newY, newX)) {
                    return@count false
                }

                if (map[newY][newX] == '#') {
                    directionIndex = (directionIndex + 1) % directions4.size
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
