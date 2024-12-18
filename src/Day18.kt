fun main() {

    val maxY = 70
    val maxX = 70

    fun shortestPath(input: List<String>, bytes: Int): Int {
        val taken = input
            .map { it.split(",").map { it.toInt() } }
            .take(bytes)
            .map { it[1] to it[0] }
            .toMutableSet()

        var distance = 1
        var positions = listOf(0 to 0)
        taken.add(0 to 0)
        while (positions.isNotEmpty()) {
            val newPositions = mutableListOf<Pair<Int, Int>>()

            positions.forEach { (x, y) ->
                directions4.forEach { (xDiff, yDiff) ->
                    val newX = x + xDiff
                    val newY = y + yDiff

                    if (newY < 0 || newY > maxY || newX < 0 || newX > maxX) {
                        return@forEach
                    }

                    if (newX to newY in taken) {
                        return@forEach
                    }

                    if (newX == maxX && newY == maxY) {
                        return distance
                    }

                    newPositions.add(newX to newY)
                    taken.add(newX to newY)
                }
            }

            positions = newPositions
            distance++
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val bytes = 1024
        return shortestPath(input, bytes)
    }

    fun part2(input: List<String>): String {
        for (bytes in 1..input.size) {
            if (shortestPath(input, bytes) == -1) {
                return input[bytes - 1]
            }
        }

        return "ERROR"
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
